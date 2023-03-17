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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.AllNearbyHotelsActivity;
import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.ResSearchActivity;
import com.hyunsungkr.pethotel.model.Hotel;
import com.hyunsungkr.pethotel.model.HotelReview;

import java.util.List;

public class SearchHotelAdapter extends RecyclerView.Adapter<SearchHotelAdapter.ViewHolder> {
     static Context context;
     List<Hotel> searchList;

    public interface OnItemClickListener {
        void onImageClick(int index);
    }

    public static OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public SearchHotelAdapter(Context context, List<Hotel> searchList) {
        this.context = context;
        this.searchList = searchList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Hotel hotel = searchList.get(position);

        holder.txtTitle.setText(hotel.getTitle());
        holder.txtCnt.setText("(" + String.valueOf(hotel.getCnt())+")");
        holder.txtAvg.setText(String.format("%.1f", hotel.getAvg()));
        float rating = (float) hotel.getAvg();
        holder.ratingBar.setRating(rating);
        if(hotel.getFavorite() == 1){
            holder.imgFavorite.setImageResource(R.drawable.baseline_favorite_24);
        }else{
            holder.imgFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
        }


        // hotel 이미지 가져오기
        if (hotel.getImgUrl() != null) {
            Glide.with(context)
                    .load(hotel.getImgUrl())
                    .into(holder.imgPhoto);
        }

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private TextView txtAvg;
        private TextView txtCnt;
        private ImageView imgPhoto;
        private RatingBar ratingBar;
        private CardView cardView;
        private ImageView imgFavorite;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAvg = itemView.findViewById(R.id.txtAvg);
            txtCnt = itemView.findViewById(R.id.txtCnt);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            cardView = itemView.findViewById(R.id.cardView);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    listener.onImageClick(index);
                }
            });

            imgFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 어느번째의 데이터의 좋아요를 누른 것인지 확인
                    int index = getAdapterPosition();

                    ((ResSearchActivity)context).favoriteProcess(index);


                }
            });


        }
    }
}