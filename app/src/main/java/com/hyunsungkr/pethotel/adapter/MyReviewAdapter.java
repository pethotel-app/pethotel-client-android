package com.hyunsungkr.pethotel.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.MyReviewActivity;
import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.model.HotelReview;
import com.hyunsungkr.pethotel.model.Review;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.ViewHolder>{

    Context context;
    ArrayList<Review> reviewArrayList;
    SimpleDateFormat sf;
    SimpleDateFormat df;

    public MyReviewAdapter(Context context, ArrayList<Review> reviewArrayList) {
        this.context = context;
        this.reviewArrayList = reviewArrayList;
        // UTC => Local Time
        sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        sf.setTimeZone(TimeZone.getTimeZone("UTC"));
        df.setTimeZone(TimeZone.getDefault());
    }

    public interface OnItemClickListener{
        void onCardViewClick(int index);
    }
    public MyReviewAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(MyReviewAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row, parent, false);
        return new MyReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReviewAdapter.ViewHolder holder, int position) {

        Review review = reviewArrayList.get(position);

        holder.txtHotelName.setText(review.getTitle());
        holder.txtUsername.setText(review.getName());

        try{
            Date date = sf.parse(review.getCreatedAt());
            long timeInMillis = date.getTime() + 9 * 60 * 60 * 1000; // UTC -> KST
            date.setTime(timeInMillis);
            holder.txtDate.setText(df.format(date).split(" ")[0]);
        }catch (ParseException e){
            e.printStackTrace();
        }

        holder.txtReview.setText(review.getContent());
        holder.ratingBar.setRating(review.getRating());

        // 유저 이미지와 리뷰 이미지 가져오기
        if (review.getUserImgUrl() != null) {
            Glide.with(context).load(review.getUserImgUrl()).into(holder.imgProfile);
        }

        if(review.getImgUrl() != null){
            // 리뷰 이미지가 있다면 비저블로 변경 후 셋팅
            holder.imgReview.setVisibility(View.VISIBLE);
            Glide.with(context).load(review.getImgUrl()).into(holder.imgReview);
        }
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtHotelName;
        TextView txtUsername;
        TextView txtDate;
        TextView txtReview;
        ImageView imgProfile;
        RatingBar ratingBar;
        ImageView imgDelete;
        ImageView imgReview;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtHotelName = itemView.findViewById(R.id.txtHotelName);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtReview = itemView.findViewById(R.id.txtReview);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgReview = itemView.findViewById(R.id.imgReview);
            cardView = itemView.findViewById(R.id.cardView);

            // 리뷰 삭제
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("리뷰 삭제");
                    builder.setMessage("정말 삭제하시겠습니까");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int index = getAdapterPosition();
                            ((MyReviewActivity)context).deleteReview(index);
                        }
                    });
                    builder.setNegativeButton("NO", null);
                    builder.show();

                }
            });

            // 카드뷰 클릭시 해당 호텔로 이동
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    listener.onCardViewClick(index);
                }
            });

        }
    }
}