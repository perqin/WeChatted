package com.perqin.wechatted.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.perqin.wechatted.R;
import com.perqin.wechatted.database.EnMicroMsgHelper;
import com.perqin.wechatted.utils.MD5;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.chainfire.libsuperuser.Shell;


public class ExtractActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int RESULT_DONE = 0;
    public static final int RESULT_OPEN = 1;
    public static final String EXTRA_EXTRACTION_NAME = "EXTRA_EXTRACTION_NAME";
    private static final int PERMISSIONS_REQUEST_FOR_EXTRACTION = 0;

    private TextInputEditText mExtractionNameEdit;
    private Button mStartButton;
    private ProgressBar mProgressBar;
    private TextView mStatusText;
    private Button mRetryButton;
    private Button mDoneButton;
    private Button mOpenButton;

    private File mTempDbDir;
    private ExtractionTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);

        mExtractionNameEdit = (TextInputEditText) findViewById(R.id.extraction_name_edit);
        mStartButton = (Button) findViewById(R.id.start_button);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mStatusText = (TextView) findViewById(R.id.status_text);
        mRetryButton = (Button) findViewById(R.id.retry_button);
        mDoneButton = (Button) findViewById(R.id.done_button);
        mOpenButton = (Button) findViewById(R.id.open_button);

        mStartButton.setOnClickListener(this);
        mRetryButton.setOnClickListener(this);
        mDoneButton.setOnClickListener(this);
        mOpenButton.setOnClickListener(this);

        mProgressBar.setVisibility(View.GONE);
        mStatusText.setVisibility(View.GONE);
        mRetryButton.setVisibility(View.GONE);
        mDoneButton.setVisibility(View.GONE);
        mOpenButton.setVisibility(View.GONE);

        mTempDbDir = new File(getFilesDir(), "copiedDbs");
        if (!mTempDbDir.isDirectory() && !mTempDbDir.mkdirs()) {
            Toast.makeText(this, "Fail to create folder", Toast.LENGTH_SHORT).show();
            finish();
        }


        mExtractionNameEdit.setText(getString(R.string.extraction_name_format, new java.text.SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime())));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED) {
            mTask.cancel(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FOR_EXTRACTION:
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mTask = new ExtractionTask();
                    mTask.execute(mExtractionNameEdit.getText().toString());
                } else {
                    Toast.makeText(this, getString(R.string.request_permissions_not_granted), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_button:
                startExtraction();
                break;
            case R.id.retry_button:
                retryExtraction();
                break;
            case R.id.done_button:
                doneExtraction();
                break;
            case R.id.open_button:
                openExtraction();
                break;
            default:
                break;
        }
    }

    private void startExtraction() {
        mExtractionNameEdit.setEnabled(false);
        mStartButton.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
        mStatusText.setVisibility(View.VISIBLE);
        runExtractionTask();
    }

    private void retryExtraction() {
        mProgressBar.setVisibility(View.VISIBLE);
        mStatusText.setVisibility(View.VISIBLE);
        mRetryButton.setVisibility(View.GONE);
        runExtractionTask();
    }

    private void doneExtraction() {
        setResult(RESULT_DONE);
        finish();
    }

    private void openExtraction() {
        Intent data = new Intent();
        data.putExtra(EXTRA_EXTRACTION_NAME, mExtractionNameEdit.getText().toString());
        setResult(RESULT_OPEN, data);
        finish();
    }

    private void runExtractionTask() {
        // Runtime permissions request
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user asynchronously
                new AlertDialog.Builder(this)
                        .setTitle(R.string.request_permissions)
                        .setMessage(R.string.request_extraction_permissions_explanation)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(ExtractActivity.this, new String[]{ Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSIONS_REQUEST_FOR_EXTRACTION);
                            }
                        })
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, PERMISSIONS_REQUEST_FOR_EXTRACTION);
            }
        } else {
            mTask = new ExtractionTask();
            mTask.execute(mExtractionNameEdit.getText().toString());
        }
    }

    private class ExtractionTask extends AsyncTask<String, String, Boolean> {
        private String error;

        @Override
        protected Boolean doInBackground(String... params) {
            String extractionDirName = params[0];

            publishProgress(getString(R.string.request_root_permission));
            // Request root access
            if (!Shell.SU.available()) {
                error = getString(R.string.root_access_is_required_to_read_wechat_history);
                return false;
            }

            publishProgress(getString(R.string.read_imei));
            // Get IMEI
            @SuppressLint("HardwareIds") String imei = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

            publishProgress(getString(R.string.read_uin));
            // Get UIN
            String uin = null;
            Pattern uinPattern = Pattern.compile(".*?default_uin.+?value=\"([-0-9]+).*?");
            List<String> lines = Shell.SU.run("cat /data/data/com.tencent.mm/shared_prefs/system_config_prefs.xml");
            for (String line : lines) {
                Matcher matcher = uinPattern.matcher(line);
                if (matcher.matches()) {
                    uin = matcher.group(1);
                }
            }
            if (uin == null || uin.equals("0")) {
                error = getString(R.string.failed_to_read_uin);
                return false;
            }

            publishProgress(getString(R.string.copy_history));
            // Get current username
            Pattern idPattern = Pattern.compile("^uid=[0-9]+?\\((\\w+)\\).+");
            String id = Shell.SH.run("id").get(0);
            Matcher idMatcher = idPattern.matcher(id);
            if (!idMatcher.matches()) {
                error = getString(R.string.failed_to_get_current_uid);
                return false;
            }
            String currentUser = idMatcher.group(1);
            // Get path
            File srcDir;
            try {
                srcDir = new File(getPackageManager().getPackageInfo("com.tencent.mm", 0).applicationInfo.dataDir, String.format("MicroMsg/%s", new MD5("mm" + uin).getMD5edString()));
            } catch (PackageManager.NameNotFoundException e) {
                error = "MicroMsg not installed";
                return false;
            }
            File srcEnMicroMsgDbFile = new File(srcDir, "EnMicroMsg.db");
            File tempEnMicroMsgDbFile = new File(mTempDbDir, "EnMicroMsg.db");
            // Copy database
            List<String> copyResult = Shell.run("su", new String[] { String.format("cp -f %s %s", srcEnMicroMsgDbFile.getAbsolutePath(), tempEnMicroMsgDbFile.getAbsolutePath()) }, null, true);
            if (!copyResult.isEmpty()) {
                error = copyResult.get(0);
                return false;
            }
            // Chown
            List<String> chownResult = Shell.run("su", new String[]{ String.format("chown %s:%s %s", currentUser, currentUser, tempEnMicroMsgDbFile.getAbsolutePath()) }, null, true);
            if (!chownResult.isEmpty()) {
                error = chownResult.get(0);
                return false;
            }

            publishProgress(getString(R.string.save_decrypted_databases));
            // Make destination dir
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                error = "Unable to write external storage.";
                return false;
            }
            File extractionDir = new File(Environment.getExternalStorageDirectory(), String.format("WeChatted/%s", extractionDirName));
            if (!extractionDir.isDirectory() && !extractionDir.mkdirs()) {
                error = "Fail to create extractions directory";
                return false;
            }
            // Save to no-password db
            String password = new MD5(imei + uin).getMD5edString().substring(0, 7);
            EnMicroMsgHelper.createDecryptedDatabase(tempEnMicroMsgDbFile, new File(extractionDir, "EnMicroMsg.db"), password);
            // TODO: Copy other resources files (images, etc.)
            // Delete temp db
            boolean deleted = tempEnMicroMsgDbFile.delete();
            if (!deleted) Log.i("FILE", "Temp db files not deleted");
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            mStatusText.setText(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean succeed) {
            if (succeed) {
                mProgressBar.setVisibility(View.GONE);
                mStatusText.setVisibility(View.GONE);
                mDoneButton.setVisibility(View.VISIBLE);
                mOpenButton.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.GONE);
                mStatusText.setText(error);
                mRetryButton.setVisibility(View.VISIBLE);
            }
        }
    }
}
