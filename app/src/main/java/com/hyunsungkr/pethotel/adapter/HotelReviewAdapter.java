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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class HotelReviewAdapter extends RecyclerView.Adapter<HotelReviewAdapter.ViewHolder> {
    private Context context;
    private List<HotelReview> hotelReviewList;

    SimpleDateFormat sf;
    SimpleDateFormat df;

    public HotelReviewAdapter(Context context, List<HotelReview> hotelReviewList) {
        this.context = context;
        this.hotelReviewList = hotelReviewList;
        // UTC => Local Time
        sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
        sf.setTimeZone(TimeZone.getTimeZone("UTC"));
        df.setTimeZone(TimeZone.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_row2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HotelReview hotelReview = hotelReviewList.get(position);

        holder.txtReview.setText(hotelReview.getContent());
        holder.txtUsername.setText(hotelReview.getName());
        try {
            Date date = sf.parse(hotelReview.getCreatedAt());
            long timeInMillis = date.getTime() + 9 * 60 * 60 * 1000; // UTC -> KST
            date.setTime(timeInMillis);
            holder.txtDate.setText(df.format(date).split(" ")[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float rating = hotelReview.getRating();
        holder.ratingBar.setRating(rating);


        // hotelReview 이미지 가져오기
        if (hotelReview.getUserImgUrl() != null) {
            Glide.with(context)
                    .load(hotelReview.getUserImgUrl())
                    .into(holder.imgProfile);
        }

        if(hotelReview.getImgUrl() != null){
            Glide.with(context)
                    .load(hotelReview.getImgUrl()).into(holder.imgReview);
        }

        // 호텔 이미지와 리뷰 이미지 가져오기
        if (hotelReview.getUserImgUrl() != null) {
            Glide.with(context).load(hotelReview.getUserImgUrl()).into(holder.imgProfile);
        }

        if(hotelReview.getImgUrl() != null){
            // 리뷰 이미지가 있다면 비저블로 변경 후 셋팅
            holder.imgReview.setVisibility(View.VISIBLE);
            Glide.with(context).load(hotelReview.getImgUrl()).into(holder.imgReview);
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

        private ImageView imgReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReview = itemView.findViewById(R.id.txtReview);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imgReview = itemView.findViewById(R.id.imgReview);

        }
    }


}
