package com.telvo.telvoterminaladmin.admin.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.model.admin.history.home.HomeWithdraw;
import com.telvo.telvoterminaladmin.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by invar on 12-Nov-17.
 */

public class AdminHomeWithdrawAdapter extends RecyclerView.Adapter<AdminHomeWithdrawAdapter.HomeWithdrawViewHolder> {

    private List<HomeWithdraw> homeWithdraws;

    public class HomeWithdrawViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLinearLayout;
        public TextView tvDate, tvAmount, tvMobile;

        public HomeWithdrawViewHolder(View itemView) {
            super(itemView);
            this.itemLinearLayout = itemView.findViewById(R.id.linear_layout_item);
            this.tvDate = itemView.findViewById(R.id.text_view_date);
            this.tvAmount = itemView.findViewById(R.id.text_view_amount);
            this.tvMobile = itemView.findViewById(R.id.text_view_mobile_number);
        }
    }

    public AdminHomeWithdrawAdapter(Context context, List<HomeWithdraw> homeWithdraws) {
        this.homeWithdraws = homeWithdraws;
    }

    @Override
    public AdminHomeWithdrawAdapter.HomeWithdrawViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_three_row, parent, false);
        return new HomeWithdrawViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdminHomeWithdrawAdapter.HomeWithdrawViewHolder holder, int position) {
        HomeWithdraw homeWithdraw = homeWithdraws.get(position);

        if ((position % 2) == 1) {
            //holder.itemLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorHistoryItem));
        }

        holder.tvDate.setText(new SimpleDateFormat("dd MMM yyyy, hh:mm aa").format(DateUtils.getFormattedDate(homeWithdraw.getCreatedAt())));
        holder.tvAmount.setText("" + homeWithdraw.getAmount());
        holder.tvMobile.setText(homeWithdraw.getUser().getMobileNumber());
    }

    @Override
    public int getItemCount() {
        return homeWithdraws.size();
    }
}
