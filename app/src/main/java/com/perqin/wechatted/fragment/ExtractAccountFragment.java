package com.perqin.wechatted.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.perqin.wechatted.R;
import com.perqin.wechatted.bean.WeChatAccount;
import com.perqin.wechatted.database.WeChattedHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.chainfire.libsuperuser.Shell;

public class ExtractAccountFragment extends Fragment implements View.OnClickListener {
    private OnExtractAccountInteractionListener mListener;
    private ArrayList<WeChatAccount> mAccounts;
    private RadioGroup mAccountRadioGroup;

    public ExtractAccountFragment() {
        // Required empty public constructor
    }

    public static ExtractAccountFragment newInstance() {
        return new ExtractAccountFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExtractAccountInteractionListener) {
            mListener = (OnExtractAccountInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccounts = WeChattedHelper.getInstance(getActivity()).getWeChatAccounts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_extract_account, container, false);

        mAccountRadioGroup = (RadioGroup) view.findViewById(R.id.account_radio_group);

        prepareRadioGroups();

        view.findViewById(R.id.manage_accounts_button).setOnClickListener(this);
        view.findViewById(R.id.read_button).setOnClickListener(this);

        return view;
    }

    private void prepareRadioGroups() {
        for (int i = 0; i < mAccounts.size(); ++i) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setId(i);
            radioButton.setText(mAccounts.get(i).getTitle());
            mAccountRadioGroup.addView(radioButton);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.manage_accounts_button:
                if (mListener != null) mListener.onManageAccountsButtonClick();
                break;
            case R.id.read_button:
                if (mListener != null) mListener.onReadButtonClick();
                break;
            default:
                break;
        }
    }

    private String readCurrentUin() {
        if (!Shell.SU.available()) {
            Toast.makeText(getActivity(), getString(R.string.root_access_is_required_to_read_wechat_uin), Toast.LENGTH_SHORT).show();
            return null;
        }
        Pattern pattern = Pattern.compile(".*?default_uin.+?value=\"([-0-9]+).*?");
        List<String> lines = Shell.SU.run("cat /data/data/com.tencent.mm/shared_prefs/system_config_prefs.xml");
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        Toast.makeText(getActivity(), getString(R.string.failed_to_read_uin), Toast.LENGTH_SHORT).show();
        return null;
    }

    public WeChatAccount getSelectedAccount() {
        int id = mAccountRadioGroup.getCheckedRadioButtonId();
        if (id == R.id.detect_current_account_radio) {
            String uin = readCurrentUin();
            if (uin == null) {
                return null;
            }
            WeChatAccount account = new WeChatAccount();
            account.setUin(uin);
            account.setTitle("Current account");
            return account;
        } else {
            return mAccounts.get(id);
        }
    }

    public interface OnExtractAccountInteractionListener {
        void onManageAccountsButtonClick();
        void onReadButtonClick();
    }
}
