package com.telvo.telvoterminaladmin.admin.history;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.fragmentadapter.FragmentCategoryAdapter;
import com.telvo.telvoterminaladmin.fragmentadapter.FragmentHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminHistoryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AdminHistoryFragment";

    private Context context;

    private FragmentManager fragmentManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static EditText startDateEditText;
    private static EditText endDateEditText;
    private Button submitButton;
    private FragmentCategoryAdapter fragmentCategoryAdapter;
    private List<FragmentHolder> fragmentHolderList;

    private int tabIndex;
    private static String start;
    private static String end;

    public AdminHistoryFragment() {
        // Required empty public constructor
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_history, container, false);

        context = getActivity();

        initializeViews(rootView);
        setupViewPager();

        return rootView;
    }

    private void setupViewPager() {
        fragmentHolderList = new ArrayList<>();
        fragmentHolderList.add(new FragmentHolder(new AdminHomeRequestHistoryFragment(), getActivity().getString(R.string.home_request)));
        fragmentHolderList.add(new FragmentHolder(new AdminDepositHistoryFragment(), getActivity().getString(R.string.deposit)));
        fragmentHolderList.add(new FragmentHolder(new AdminWithdrawHistoryFragment(), getActivity().getString(R.string.withdraw)));

        fragmentCategoryAdapter = new FragmentCategoryAdapter(getChildFragmentManager(), fragmentHolderList);

        viewPager.setAdapter(fragmentCategoryAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                startDateEditText.setText("");
                endDateEditText.setText("");
            }
        });
    }

    private void initializeViews(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabIndex = tab.getPosition();
                Log.e(TAG, "Selected tab position: " + tabIndex);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        startDateEditText = view.findViewById(R.id.startDateEditText);
        startDateEditText.setOnClickListener(this);
        endDateEditText = view.findViewById(R.id.endDateEditText);
        endDateEditText.setOnClickListener(this);
        submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.startDateEditText:
                showStartDatePickerDialog();
                break;
            case R.id.endDateEditText:
                showEndDatePickerDialog();
                break;
            case R.id.submitButton:
                switch (tabIndex) {
                    case 0:
                        AdminHomeRequestHistoryFragment.getPreciseHomeWithdraws(start, end);
                        break;
                    case 1:
                        AdminDepositHistoryFragment.getPreciseDepositHistory(start, end);
                        break;
                    case 2:
                        AdminWithdrawHistoryFragment.getPreciseWithdrawHistory(start, end);
                        break;
                }
                break;
            default:
        }
    }

    private void showStartDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(fragmentManager, "startDatePicker");
        Log.e(TAG, "showStartDatePickerDialog");
    }

    private void showEndDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(fragmentManager, "endDatePicker");
        Log.e(TAG, "showEndDatePickerDialog");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            if (getTag().equals("startDatePicker")) {
                start = year + "-" + (month+1) + "-" + day;
                startDateEditText.setText(start);
            } else if (getTag().equals("endDatePicker")) {
                end = year + "-" + (month+1) + "-" + day;
                endDateEditText.setText(end);
            }
        }
    }
}
