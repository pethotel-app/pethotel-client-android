package com.hyunsungkr.pethotel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.model.MyReservation;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyReservationAdapter extends RecyclerView.Adapter<MyReservationAdapter.ViewHolder> {

    Context context;
    ArrayList<MyReservation> MyReservationList;

    public MyReservationAdapter(Context context, ArrayList<MyReservation> myReservationList) {
        this.context = context;
        MyReservationList = myReservationList;
    }

    @NonNull
    @Override
    public MyReservationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reservation_row, parent, false);
        return new MyReservationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReservationAdapter.ViewHolder holder, int position) {

        MyReservation myReservation = MyReservationList.get(position);

        holder.txtTitle.setText(myReservation.getTitle());
        holder.txtContent.setText(myReservation.getContent());
        holder.txtPetName.setText(myReservation.getName());

        String reservationPeriod = myReservation.getCheckInDate().substring(0, 10) + " ~ " + myReservation.getCheckOutDate().substring(0, 10);
        holder.txtDate.setText(reservationPeriod);


    }

    @Override
    public int getItemCount() {
        return MyReservationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle;
        TextView txtDate;
        TextView txtPetName;
        TextView txtContent;
        Button btnCancle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPetName = itemView.findViewById(R.id.txtPetName);
            txtContent = itemView.findViewById(R.id.txtContent);
            btnCancle = itemView.findViewById(R.id.btnCancle);

            btnCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo: 취소버튼 클릭시, 취소 액티비티 생성하면 intent로 넘겨주기.
                }
            });



        }
    }
}
