package com.hyunsungkr.pethotel.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.model.HotelReview;

import java.util.List;

public class HotelReviewAdapter extends RecyclerView.Adapter<HotelReviewAdapter.ViewHolder> {
    private Context context;
    private List<HotelReview> hotelReviewList;

    public HotelReviewAdapter(Context context, List<HotelReview> hotelReviewList) {
        this.context = context;
        this.hotelReviewList = hotelReviewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HotelReview hotelReview = hotelReviewList.get(position);

        holder.txtReview.setText(hotelReview.getContent());
        holder.txtUsername.setText(hotelReview.getName());
        holder.txtDate.setText(hotelReview.getCreatedAt());
        float rating = hotelReview.getRating();
        holder.ratingBar.setRating(rating);


        // hotelReview 이미지 가져오기
        if (hotelReview.getUserImgUrl() != null) {
            Glide.with(context)
                    .load(hotelReview.getUserImgUrl())
                    .into(holder.imgProfile);
        }
    }

    @Override
    public int getItemCount() {
        return hotelReviewList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtReview;
        private TextView txtUsername;
        private TextView txtDate;
        private ImageView imgProfile;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReview = itemView.findViewById(R.id.txtReview);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}