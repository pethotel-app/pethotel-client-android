package com.hyunsungkr.pethotel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.model.Point;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PointAdapter extends RecyclerView.Adapter<PointAdapter.ViewHolder> {

    Context context;
    ArrayList<Point> pointList;

    public PointAdapter(Context context, ArrayList<Point> pointList) {
        this.context = context;
        this.pointList = pointList;
    }

    @NonNull
    @Override
    public PointAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pointlist, parent, false);
        return new PointAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointAdapter.ViewHolder holder, int position) {

        Point point = pointList.get(position);

        holder.txtHotelName.setText(point.getContent());

        // 날짜 형식 변환 "yyyy-MM-dd'T'HH:mm:ss" to "yyyy.MM.dd"
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = originalFormat.parse(point.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd");
        String formattedDate = outputFormat.format(date);

        holder.txtSaveDate.setText(formattedDate);

        // 양수이면 앞에 + 붙여서 출력
        if (point.getAddPoint() >= 0) {
            holder.txtAddPoint.setText("+" + point.getAddPoint());
        } else {
            holder.txtAddPoint.setText(String.valueOf(point.getAddPoint()));
        }


    }

    @Override
    public int getItemCount() {
        return pointList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        TextView txtHotelName;
        TextView txtSaveDate;
        TextView txtAddPoint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtHotelName = itemView.findViewById(R.id.txtHotelName);
            txtSaveDate = itemView.findViewById(R.id.txtSaveDate);
            txtAddPoint = itemView.findViewById(R.id.txtAddPoint);

        }
    }
}
