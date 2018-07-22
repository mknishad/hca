package com.telvo.telvoterminaladmin.shop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.model.shop.history.ShopHistory;

import java.util.List;

/**
 * Created by monir on 8/21/17.
 */

public class ShopHistoryAdapter extends RecyclerView.Adapter<ShopHistoryAdapter.ShopHistoryViewHolder> {
    private List<ShopHistory> shopHistories;

    public class ShopHistoryViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemLinearLayout;
        public TextView tvDate, tvAmount, tvMobile;

        public ShopHistoryViewHolder(View itemView) {
            super(itemView);
            this.itemLinearLayout = itemView.findViewById(R.id.linear_layout_item);
            this.tvDate = itemView.findViewById(R.id.text_view_date);
            this.tvAmount = itemView.findViewById(R.id.text_view_amount);
            this.tvMobile = itemView.findViewById(R.id.text_view_mobile_number);
        }
    }

    public ShopHistoryAdapter(Context context, List<ShopHistory> shopHistories) {
        this.shopHistories = shopHistories;
    }

    @Override
    public ShopHistoryAdapter.ShopHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_three_row, parent, false);
        return new ShopHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShopHistoryAdapter.ShopHistoryViewHolder holder, int position) {
        ShopHistory shopHistory = shopHistories.get(position);

        if ((position % 2) == 1) {
            //holder.itemLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorHistoryItem));
        }

        holder.tvDate.setText(shopHistory.getDate());
        holder.tvAmount.setText("" + shopHistory.getAmount());
        holder.tvMobile.setText(shopHistory.getMobileNumber());
    }

    @Override
    public int getItemCount() {
        return shopHistories.size();
    }
}
