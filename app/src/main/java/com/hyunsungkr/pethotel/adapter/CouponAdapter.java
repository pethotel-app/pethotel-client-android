package com.hyunsungkr.pethotel.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.SearchActivity;
import com.hyunsungkr.pethotel.model.Coupon;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {


    Context context;
    ArrayList<Coupon> couponList;

    public CouponAdapter(Context context, ArrayList<Coupon> couponList) {
        this.context = context;
        this.couponList = couponList;
    }

    @NonNull
    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coupon, parent, false);
        return new CouponAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.ViewHolder holder, int position) {
        Coupon coupon = couponList.get(position);

        holder.txtCpTitle.setText(coupon.getTitle());
        holder.txtCpDescription.setText(coupon.getDescription());
        String reservationPeriod = coupon.getDateOfUseStart().substring(0, 10) + " ~ " + coupon.getDateOfUseEnd().substring(0, 10);
        holder.txtCpDate.setText(reservationPeriod);

        DecimalFormat myFormatter = new DecimalFormat("###,###");
        holder.txtPrice.setText(myFormatter.format(coupon.getDiscount()) + "원");

    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtCpTitle;
        TextView txtCpDescription;
        TextView txtCpDate;
        TextView txtPrice;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCpTitle = itemView.findViewById(R.id.txtCpTitle);
            txtCpDescription = itemView.findViewById(R.id.txtCpDescription);
            txtCpDate = itemView.findViewById(R.id.txtCpDate);
            txtPrice = itemView.findViewById(R.id.txtPrice);



        }
    }
}
