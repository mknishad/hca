package com.telvo.telvoterminaladmin.admin;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.model.admin.history.home.HomeWithdraw;
import com.telvo.telvoterminaladmin.util.Constants;
import com.telvo.telvoterminaladmin.util.NumberFormatUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RequestDetailsFragment";

    private Context context;

    private CircularImageView userImageView;
    private TextView userNameTextView;
    private TextView userMobileTextView;
    private TextView userAreaTextView;
    private TextView userAddressTextView;
    private TextView amountTextView;
    private Button cancelButton;
    private Button printButton;
    private Button verifyButton;
    private TextView[] textViews = new TextView[7];
    private FragmentManager fragmentManager;

    private HomeWithdraw homeWithdraw;

    public RequestDetailsFragment() {
        // Required empty public constructor
    }

    public void setTextViews(TextView[] textViews) {
        this.textViews = textViews;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setHomeWithdraw(HomeWithdraw homeWithdraw) {
        this.homeWithdraw = homeWithdraw;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_request_details, container, false);

        context = getActivity();

        initializeViews(rootView);
        setValueOnViews();

        return rootView;
    }

    private void setValueOnViews() {
        Picasso.with(context).load(homeWithdraw.getUser().getImage().equals("") ? "No Image" : Constants.IMAGE_BASE + homeWithdraw.getUser().getImage()).placeholder(R.drawable.ic_home_user_photo).error(R.drawable.ic_home_user_photo).into(userImageView);
        userNameTextView.setText(homeWithdraw.getUser().getName());
        userMobileTextView.setText(homeWithdraw.getUser().getMobileNumber());
        userAreaTextView.setText(homeWithdraw.getArea());
        userAddressTextView.setText(homeWithdraw.getAddress());
        amountTextView.setText(NumberFormatUtils.getFormattedDouble(homeWithdraw.getAmount()));
    }

    private void initializeViews(View view) {
        userImageView = view.findViewById(R.id.userImageView);
        userNameTextView = view.findViewById(R.id.userNameTextView);
        userMobileTextView = view.findViewById(R.id.userMobileTextView);
        userAreaTextView = view.findViewById(R.id.userAreaTextView);
        userAddressTextView = view.findViewById(R.id.userAddressTextView);
        amountTextView = view.findViewById(R.id.amountTextView);
        cancelButton = view.findViewById(R.id.backButton);
        cancelButton.setOnClickListener(this);
        printButton = view.findViewById(R.id.printButton);
        printButton.setOnClickListener(this);
        verifyButton = view.findViewById(R.id.verifyButton);
        verifyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.backButton:
                goToHomeFragment();
                break;
            case R.id.printButton:
                printRequest();
                break;
            case R.id.verifyButton:
                goToHomeWithdrawFragment();
                break;
        }
    }

    private void goToHomeFragment() {
        AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
        adminHomeFragment.setTextViews(textViews);
        adminHomeFragment.setFragmentManager(fragmentManager);

        fragmentManager.beginTransaction()
                .replace(R.id.admin_detail_container, adminHomeFragment)
                .commit();

        changeListItemBackgroundForHome();
    }

    private void changeListItemBackgroundForHome() {
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

    private void printRequest() {

    }

    private void goToHomeWithdrawFragment() {
        AdminHomeWithdrawFragment adminHomeWithdrawFragment = new AdminHomeWithdrawFragment();
        adminHomeWithdrawFragment.setTextViews(textViews);
        adminHomeWithdrawFragment.setFragmentManager(fragmentManager);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PENDING_HOME_WITHDRAW, homeWithdraw);
        adminHomeWithdrawFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.admin_detail_container, adminHomeWithdrawFragment)
                .commit();

        changeListItemBackgroundForHomeWithdraw();
    }

    private void changeListItemBackgroundForHomeWithdraw() {
        textViews[0].setBackgroundResource(R.drawable.bg_red_master_item);
        textViews[0].setTextColor(getResources().getColor(R.color.colorWhite));
        textViews[1].setBackgroundResource(R.drawable.bg_white_master_item);
        textViews[1].setTextColor(getResources().getColor(R.color.colorText));
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
