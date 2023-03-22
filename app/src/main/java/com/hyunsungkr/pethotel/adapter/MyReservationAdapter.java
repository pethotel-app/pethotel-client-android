package com.hyunsungkr.pethotel.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunsungkr.pethotel.CancelActivity;
import com.hyunsungkr.pethotel.MyReservationActivity;
import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.ReviewWriteActivity;
import com.hyunsungkr.pethotel.model.MyReservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyReservationAdapter extends RecyclerView.Adapter<MyReservationAdapter.ViewHolder> {

    Context context;
    ArrayList<MyReservation> MyReservationList;

    MyReservation myReservation;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        myReservation = MyReservationList.get(position);

        holder.txtTitle.setText(myReservation.getTitle());
        holder.txtContent.setText(myReservation.getContent());
        holder.txtPetName.setText(myReservation.getName());

        String reservationPeriod = myReservation.getCheckInDate().substring(0, 10) + " ~ " + myReservation.getCheckOutDate().substring(0, 10);
        holder.txtDate.setText(reservationPeriod);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date today = new Date();
        Date deadline = null;
        try {
            deadline = dateFormat.parse(myReservation.getCheckOutDate().substring(0, 10));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(deadline);
        calendar.add(Calendar.DATE, 7);
        Date sevenDaysAfterDeadline = calendar.getTime();

        if (deadline.after(today)) {
            // 마감일이 오늘 이후인 경우 예약취소 버튼 보여주기
            holder.txtCancle.setVisibility(View.VISIBLE);
        } else if (today.after(deadline) && today.before(sevenDaysAfterDeadline)) {
            // 마감일 이후 7일 이내인 경우 후기작성 버튼 보여주기
            holder.txtReview.setVisibility(View.VISIBLE);
        } else {
            // 그외 후기만료 버튼 보여주기
            holder.txtExpiration.setVisibility(View.VISIBLE);
        }
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
        TextView txtCancle;
        TextView txtReview;
        TextView txtExpiration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPetName = itemView.findViewById(R.id.txtSearchHotel);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtCancle = itemView.findViewById(R.id.txtCancle);
            txtReview = itemView.findViewById(R.id.txtReview);
            txtExpiration = itemView.findViewById(R.id.txtExpiration);

            txtCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CancelActivity.class);
                    intent.putExtra("myReservation", myReservation);
                    context.startActivity(intent);
                }
            });

            txtReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ReviewWriteActivity.class);
                    intent.putExtra("myReservation", myReservation);
                    context.startActivity(intent);
                }
            });
        }
    }
}
