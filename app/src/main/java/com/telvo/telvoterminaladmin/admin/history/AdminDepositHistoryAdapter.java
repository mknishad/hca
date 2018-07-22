package com.telvo.telvoterminaladmin.admin.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.model.admin.history.deposit.AgentDeposit;
import com.telvo.telvoterminaladmin.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by invar on 14-Nov-17.
 */

public class AdminDepositHistoryAdapter extends RecyclerView.Adapter<AdminDepositHistoryAdapter.AdminDepositHistoryViewHolder> {

    private List<AgentDeposit> agentDeposits;

    public class AdminDepositHistoryViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLinearLayout;
        public TextView dateTextView, amountTextView, mobileNumberTextView;

        public AdminDepositHistoryViewHolder(View itemView) {
            super(itemView);
            this.itemLinearLayout = itemView.findViewById(R.id.linear_layout_item);
            this.dateTextView = itemView.findViewById(R.id.text_view_date);
            this.amountTextView = itemView.findViewById(R.id.text_view_amount);
            this.mobileNumberTextView = itemView.findViewById(R.id.text_view_mobile_number);
        }
    }

    public AdminDepositHistoryAdapter(Context context, List<AgentDeposit> agentDeposits) {
        this.agentDeposits = agentDeposits;
    }

    @Override
    public AdminDepositHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_three_row, parent, false);
        return new AdminDepositHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdminDepositHistoryViewHolder holder, int position) {
        AgentDeposit agentDeposit = agentDeposits.get(position);

        holder.dateTextView.setText(new SimpleDateFormat("dd MMM yyyy, hh:mm aa").format(DateUtils.getFormattedDate(agentDeposit.getCreatedAt())));
        holder.amountTextView.setText("" + agentDeposit.getAmount());
        holder.mobileNumberTextView.setText(agentDeposit.getAgent().getMobileNumber());
    }

    @Override
    public int getItemCount() {
        return agentDeposits.size();
    }
}
