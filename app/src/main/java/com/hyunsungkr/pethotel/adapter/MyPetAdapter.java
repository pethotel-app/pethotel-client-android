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
import com.hyunsungkr.pethotel.model.Pet;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyPetAdapter extends RecyclerView.Adapter<MyPetAdapter.ViewHolder> {

    Context context;
    ArrayList<Pet> petList;

    public interface OnItemClickListener {
        void onCardViewClick(int index);
    }
    public OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public MyPetAdapter(Context context, ArrayList<Pet> petList) {
        this.context = context;
        this.petList = petList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.petinfo_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Pet pet = petList.get(position);

        holder.txtPetName.setText(pet.getName());
        holder.txtAge.setText(pet.getAge()+"살 ");
        holder.txtWeight.setText(pet.getWeight()+"kg");
        holder.txtSpecies.setText(pet.getSpecies());
        Glide.with(context).load(pet.getPetImgUrl()).placeholder(R.drawable.icon2).into(holder.imgProfile);

        // 반려동물 성별
        if(pet.getGender() == 0){
            holder.txtPetGender.setText("남");
        }else{
            holder.txtPetGender.setText("여");
        }
        // 반려동물 분류
        if(pet.getClassification() == 0){
            holder.txtPetClassfication.setText("강아지");
        }else{
            holder.txtPetClassfication.setText("고양이");
        }

    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        CardView cardView;
        ImageView imgProfile;
        TextView txtPetName;
        TextView txtPetGender;
        TextView txtPetClassfication;
        TextView txtSpecies;
        TextView txtAge;
        TextView txtWeight;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtPetName = itemView.findViewById(R.id.txtPetName);
            txtPetGender = itemView.findViewById(R.id.txtPetGender);
            txtPetClassfication = itemView.findViewById(R.id.txtPetClassification);
            txtSpecies = itemView.findViewById(R.id.txtSpecies);
            txtAge = itemView.findViewById(R.id.txtAge);
            txtWeight = itemView.findViewById(R.id.txtWeight);

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
