package com.hyunsungkr.pethotel.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.model.Keyword;

import java.util.ArrayList;
import java.util.List;

public class SearchRankAdapter extends RecyclerView.Adapter<SearchRankAdapter.ViewHolder> {
    Context context;
    List<Keyword> keywordRankList;
    OnItemClickListener listener;


    public interface OnItemClickListener {
        void onImageClick(int index);
    }

    public void setOnItemClickListener(SearchRankAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public SearchRankAdapter(Context context, List<Keyword> keywordRankList) {
        this.context = context;
        this.keywordRankList = keywordRankList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.keywordrank_row, parent, false);
        return new SearchRankAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRankAdapter.ViewHolder holder, int position) {

        Keyword keywordRank = keywordRankList.get(position);
        holder.txtKeywordRank.setText(String.valueOf(position+1));
        holder.txtKeyword.setText(keywordRank.getKeyword());
        Log.i("값 확인",keywordRank.getKeyword());
    }

    @Override
    public int getItemCount() {
        return keywordRankList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtKeywordRank;
        private TextView txtKeyword;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtKeywordRank = itemView.findViewById(R.id.txtKeywordRank);
            txtKeyword = itemView.findViewById(R.id.txtKeyword);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    if (listener != null) {
                        listener.onImageClick(index);
                    }
                }
            });
        }
    }
}
