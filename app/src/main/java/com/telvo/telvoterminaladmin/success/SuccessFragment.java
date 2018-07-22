package com.telvo.telvoterminaladmin.success;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.admin.AdminHomeFragment;
import com.telvo.telvoterminaladmin.agent.AgentHomeFragment;
import com.telvo.telvoterminaladmin.util.Constants;

/**
 * A simple {@link Fragment} subclass.
 */

public class SuccessFragment extends Fragment implements View.OnClickListener {

    private TextView amountTextView;
    private TextView mobileNumberTextView;
    private TextView addressTextView;
    private LinearLayout addressLinearLayout;
    private Button doneButton;
    private TextView[] textViews = new TextView[7];
    private FragmentManager fragmentManager;

    String actionType;

    public SuccessFragment() {
        // Required empty public constructor
    }

    public void setTextViews(TextView[] textViews) {
        this.textViews = textViews;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_success, container, false);

        actionType = getArguments().getString(Constants.ACTION_TYPE);

        initializeViews(rootView);
        setupResponseValues();

        return rootView;
    }

    private void setupResponseValues() {
        switch (actionType) {
            case Constants.HOME_WITHDRAW:
                amountTextView.setText(getArguments().getString(Constants.AMOUNT));
                mobileNumberTextView.setText(getArguments().getString(Constants.MOBILE_NUMBER));
                addressTextView.setText(getArguments().getString(Constants.ADDRESS));
                break;
            case Constants.DEPOSIT_AGENT:
                amountTextView.setText(getArguments().getString(Constants.AMOUNT));
                mobileNumberTextView.setText(getArguments().getString(Constants.MOBILE_NUMBER));
                addressLinearLayout.setVisibility(View.GONE);
                break;
            case Constants.WITHDRAW_AGENT:
                amountTextView.setText(getArguments().getString(Constants.AMOUNT));
                mobileNumberTextView.setText(getArguments().getString(Constants.MOBILE_NUMBER));
                addressLinearLayout.setVisibility(View.GONE);
                break;
            case Constants.WITHDRAW_SHOP:
                amountTextView.setText(getArguments().getString(Constants.AMOUNT));
                mobileNumberTextView.setText(getArguments().getString(Constants.MOBILE_NUMBER));
                addressLinearLayout.setVisibility(View.GONE);
                break;
            case Constants.AGENT_DEPOSIT:
                amountTextView.setText(getArguments().getString(Constants.AMOUNT));
                mobileNumberTextView.setText(getArguments().getString(Constants.MOBILE_NUMBER));
                addressLinearLayout.setVisibility(View.GONE);
                break;
            case Constants.AGENT_WITHDRAW:
                amountTextView.setText(getArguments().getString(Constants.AMOUNT));
                mobileNumberTextView.setText(getArguments().getString(Constants.MOBILE_NUMBER));
                addressLinearLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void initializeViews(View view) {
        amountTextView = view.findViewById(R.id.amountTextView);
        mobileNumberTextView = view.findViewById(R.id.mobileNumberTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        addressLinearLayout = view.findViewById(R.id.addressLinearLayout);
        doneButton = view.findViewById(R.id.btn_done);
        doneButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_done:
                if (actionType.equals(Constants.AGENT_DEPOSIT) || actionType.equals(Constants.AGENT_WITHDRAW)) {
                    goToAgentHomeFragment();
                } else {
                    goToAdminHomeFragment();
                }
                break;
            default:
        }
    }

    private void goToAgentHomeFragment() {
        AgentHomeFragment agentHomeFragment = new AgentHomeFragment();
        agentHomeFragment.setTextViews(textViews);
        agentHomeFragment.setFragmentManager(fragmentManager);

        fragmentManager.beginTransaction()
                .replace(R.id.agent_detail_container, agentHomeFragment)
                .commit();

        changeMasterItemsBackgroundForAgentHome();
    }

    private void changeMasterItemsBackgroundForAgentHome() {
        textViews[0].setBackgroundResource(R.drawable.bg_white_master_item);
        textViews[0].setTextColor(getResources().getColor(R.color.colorText));
        textViews[1].setBackgroundResource(R.drawable.bg_red_master_item);
        textViews[1].setTextColor(getResources().getColor(R.color.colorWhite));
        textViews[2].setBackgroundResource(R.drawable.bg_red_master_item);
        textViews[2].setTextColor(getResources().getColor(R.color.colorWhite));
        textViews[3].setBackgroundResource(R.drawable.bg_red_master_item);
        textViews[3].setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void goToAdminHomeFragment() {
        AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
        adminHomeFragment.setTextViews(textViews);
        adminHomeFragment.setFragmentManager(fragmentManager);

        fragmentManager.beginTransaction()
                .replace(R.id.admin_detail_container, adminHomeFragment)
                .commit();

        changeMasterItemsBackgroundForAdminHome();
    }

    private void changeMasterItemsBackgroundForAdminHome() {
        textViews[0].setBackgroundResource(R.drawable.bg_white_master_item);
        textViews[0].setTextColor(getResources().getColor(R.color.colorText));
        textViews[1].setBackgroundResource(R.drawable.bg_red_master_item);
        textViews[1].setTextColor(getResources().getColor(R.color.colorWhite));
        textViews[2].setBackgroundResource(R.drawable.bg_red_master_item);
        textViews[2].setTextColor(getResources().getColor(R.color.colorWhite));
        textViews[3].setBackgroundResource(R.drawable.bg_red_master_item);
        textViews[3].setTextColor(getResources().getColor(R.color.colorWhite));
        textViews[4].setBackgroundResource(R.drawable.bg_red_master_item);
        textViews[4].setTextColor(getResources().getColor(R.color.colorWhite));
        textViews[5].setBackgroundResource(R.drawable.bg_red_master_item);
        textViews[5].setTextColor(getResources().getColor(R.color.colorWhite));
        textViews[6].setBackgroundResource(R.drawable.bg_red_master_item);
        textViews[6].setTextColor(getResources().getColor(R.color.colorWhite));
    }
}
