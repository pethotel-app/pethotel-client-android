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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.model.Hotel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AllnearhotelAdapter extends RecyclerView.Adapter<AllnearhotelAdapter.ViewHolder> {


    public interface OnItemClickListener{
        void onCardViewClick(int index);
    }
    public OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    Context context;
    ArrayList<Hotel> hotelList;

    public AllnearhotelAdapter(Context context, ArrayList<Hotel> hotelList) {
        this.context = context;
        this.hotelList = hotelList;
    }

    @NonNull
    @Override
    public AllnearhotelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllnearhotelAdapter.ViewHolder holder, int position) {

        Hotel hotel = hotelList.get(position);




        holder.txtTitle.setText(hotel.getTitle());
        holder.txtAvg.setText(hotel.getAvg()+"");
        holder.txtCnt.setText(hotel.getCnt()+"");
        // ratingBar 채우기
        holder.ratingBar.setRating((float) hotel.getAvg());

        if(hotel.getFavorite() == 1){
            holder.imgFavorite.setImageResource(R.drawable.baseline_favorite_24);
        }else{
            holder.imgFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
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


            // todo : imgFavorite 클릭시 좋아요 / 좋아요 취소하는 코드 작성

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
