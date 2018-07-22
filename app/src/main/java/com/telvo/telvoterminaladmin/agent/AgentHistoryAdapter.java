package com.telvo.telvoterminaladmin.agent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.model.agent.history.AgentHistory;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by monir on 8/9/17.
 */

public class AgentHistoryAdapter extends RecyclerView.Adapter<AgentHistoryAdapter.HistoryViewHolder> {

    private Context context;
    private List<AgentHistory> agentHistories;

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLinearLayout;
        public TextView tvDate, tvAmount, tvMobile, tvTransactionType;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            this.itemLinearLayout = itemView.findViewById(R.id.linear_layout_item);
            this.tvDate = itemView.findViewById(R.id.text_view_history_date);
            this.tvAmount = itemView.findViewById(R.id.text_view_history_amount);
            this.tvMobile = itemView.findViewById(R.id.text_view_history_mobile);
            this.tvTransactionType = itemView.findViewById(R.id.text_view_history_type);
        }
    }

    public AgentHistoryAdapter(Context context, List<AgentHistory> agentHistories) {
        this.context = context;
        this.agentHistories = agentHistories;
    }

    @Override
    public AgentHistoryAdapter.HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_four_row, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AgentHistoryAdapter.HistoryViewHolder holder, int position) {
        AgentHistory agentHistory = agentHistories.get(position);

        if ((position % 2) == 1) {
            //holder.itemLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorHistoryItem));
        }

        holder.tvDate.setText(new SimpleDateFormat("dd MMM yyyy, hh:mm aa").format(agentHistory.getDate()));
        holder.tvAmount.setText(String.valueOf(agentHistory.getAmount()));
        holder.tvMobile.setText(agentHistory.getMobileNumber());
        holder.tvTransactionType.setText(agentHistory.getTransactionType());
    }

    @Override
    public int getItemCount() {
        return agentHistories.size();
    }
}
