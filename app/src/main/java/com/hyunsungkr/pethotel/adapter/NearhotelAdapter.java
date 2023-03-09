package com.hyunsungkr.pethotel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.model.Hotel;

import java.util.ArrayList;

public class NearhotelAdapter extends RecyclerView.Adapter<NearhotelAdapter.ViewHolder> {

    Context context;
    ArrayList<Hotel> NearhotelList;

    public NearhotelAdapter(Context context, ArrayList<Hotel> nearhotelList) {
        this.context = context;
        NearhotelList = nearhotelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_hotel_info_row, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hotel hotel = NearhotelList.get(position);

        holder.txtTitle.setText(hotel.getTitle());
        holder.txtDescription.setText(hotel.getDescription());
        Glide.with(context).load(hotel.getImgUrl()).placeholder(R.drawable.icon1).into(holder.imgPhoto);

    }

    @Override
    public int getItemCount() {
        return NearhotelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imgPhoto;
        TextView txtTitle;
        TextView txtDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDescription);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo: 호텔 카드뷰 클릭시 이벤트 처리
                }
            });

        }
    }


}
