package com.hyunsungkr.pethotel.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.UseCouponActivity;
import com.hyunsungkr.pethotel.model.Coupon;

import java.util.List;

public class UseCouponAdapter extends RecyclerView.Adapter<UseCouponAdapter.ViewHolder>{

    Context context;
    List<Coupon> couponList;

    public UseCouponAdapter(Context context, List<Coupon> couponList) {
        this.context = context;
        this.couponList = couponList;
    }

    public interface OnItemClickListener {
        void onImageClick(int index);
    }

    public UseCouponAdapter.OnItemClickListener listener;
    public void setOnItemClickListener(UseCouponAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public UseCouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.use_coupon_row, parent, false);
        return new UseCouponAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UseCouponAdapter.ViewHolder holder, int position) {
        Coupon coupon = couponList.get(position);
        holder.txtTitle.setText(coupon.getTitle());
        holder.txtDiscount.setText(coupon.getDiscount());

        // 쿠폰 기한 데이터 가공해서 입력
        String start = coupon.getDateOfUseStart().split("T")[0];
        String end = coupon.getDateOfUseEnd().split("T")[0];
        holder.txtDeadline.setText(start + " - " + end);

    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtDiscount;
        TextView txtDeadline;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDiscount = itemView.findViewById(R.id.txtDiscount);
            txtDeadline = itemView.findViewById(R.id.txtDeadline);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    listener.onImageClick(index);
                }
            });


        }
    }

}
