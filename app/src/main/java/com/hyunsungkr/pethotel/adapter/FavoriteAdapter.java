package com.hyunsungkr.pethotel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.AllNearbyHotelsActivity;
import com.hyunsungkr.pethotel.Favorite;
import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.model.Hotel;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>{

    public Context context;
    public ArrayList<Hotel> hotelList;
    public Fragment fragment;

    public FavoriteAdapter(Context context, ArrayList<Hotel> hotelList, Fragment fragment) {
        this.context = context;
        this.hotelList = hotelList;
        this.fragment = fragment;
    }

    public interface OnItemClickListener{
        void onCardViewClick(int index);
    }
    public FavoriteAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(FavoriteAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);
        return new FavoriteAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {

        Hotel hotel = hotelList.get(position);

        holder.txtTitle.setText(hotel.getTitle());
        holder.txtAvg.setText(String.format("%.1f", hotel.getAvg()));
        holder.txtCnt.setText("("+hotel.getCnt()+")");

        // ratingBar 채우기
        holder.ratingBar.setRating((float) hotel.getAvg());

        if (hotel.getFavorite() == 1){
            holder.imgFavorite.setImageResource(R.drawable.baseline_favorite_24);
        }

        Glide.with(context).load(hotel.getImgUrl()).placeholder(R.drawable.icon1).into(holder.imgPhoto);

    }

    @Override
    public int getItemCount() {
        return hotelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imgPhoto;
        TextView txtTitle;
        ImageView imgFavorite;
        RatingBar ratingBar;
        TextView txtAvg;
        TextView txtCnt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            txtAvg = itemView.findViewById(R.id.txtAvg);
            txtCnt = itemView.findViewById(R.id.txtCnt);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    listener.onCardViewClick(index);
                }
            });

            imgFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 어느번째의 데이터의 좋아요를 누른 것인지 확인
                    int index = getAdapterPosition();
                    ((Favorite)fragment).favoriteProcess(index);
                }
            });

        }
    }
}
