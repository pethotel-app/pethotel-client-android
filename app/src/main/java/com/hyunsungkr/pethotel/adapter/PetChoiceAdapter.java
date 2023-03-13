package com.hyunsungkr.pethotel.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyunsungkr.pethotel.HotelInfoActivity;
import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.model.Pet;
import com.hyunsungkr.pethotel.model.PetInfoList;

import java.util.List;

public class PetChoiceAdapter extends RecyclerView.Adapter<PetChoiceAdapter.ViewHolder> {



    private Context context;
    private List<Pet> petList;
    private List<PetInfoList> petInfoList;








    public PetChoiceAdapter(Context context, List<Pet> petList) {
        this.context = context;
        this.petList = petList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.petinfo_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Pet pet = petList.get(position);



        // Pet 객체에서 데이터 가져와서 UI에 연결
        holder.txtPetName.setText(pet.getName());
        holder.txtPetGender.setText(pet.getGenderString());
        holder.txtPetClassification.setText(pet.getClassificationString());
        holder.txtSpecies.setText(pet.getSpecies());
        holder.txtAge.setText(pet.getAge() + "살");
        holder.txtWeight.setText(pet.getWeight() + "kg");

        // Pet 이미지 가져오기
        if (pet.getPetImgUrl() != null) {
            Glide.with(context)
                    .load(pet.getPetImgUrl())
                    .into(holder.imgPet);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭한 행의 데이터를 가져옴
                Pet pet = petList.get(position);

                // Intent 생성 및 데이터 추가
                Intent intent = new Intent(context, HotelInfoActivity.class);
                intent.putExtra("pet", pet);

                // 보내기
                context.startActivity(intent);

                ((Activity)context).finish();

            }
        });


    }

    @Override
    public int getItemCount() {
        return petList.size();
    }





    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgPet;
        private TextView txtPetName;
        private TextView txtPetGender;
        private TextView txtPetClassification;
        private TextView txtSpecies;
        private TextView txtAge;
        private TextView txtWeight;
        private List<Pet> petList;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            imgPet = itemView.findViewById(R.id.imgProfile);
            txtPetName = itemView.findViewById(R.id.txtPetName);
            txtPetGender = itemView.findViewById(R.id.txtPetGender);
            txtPetClassification = itemView.findViewById(R.id.txtPetClassification);
            txtSpecies = itemView.findViewById(R.id.txtSpecies);
            txtAge = itemView.findViewById(R.id.txtAge);
            txtWeight = itemView.findViewById(R.id.txtWeight);
            cardView = itemView.findViewById(R.id.cardView);



        }


    }


}
