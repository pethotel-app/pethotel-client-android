package com.hyunsungkr.pethotel.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunsungkr.pethotel.ChatActivity;
import com.hyunsungkr.pethotel.R;

import java.util.List;

public class ChatRoomListAdapter extends RecyclerView.Adapter<ChatRoomListAdapter.ViewHolder> {
    private List<String> mChatRoomIds;
    Context context;

    public ChatRoomListAdapter(Context context,List<String> chatRoomIds) {
        this.context = context;
        this.mChatRoomIds = chatRoomIds;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String chatId = mChatRoomIds.get(position);
        holder.txtRoom.setText("UserId :" + chatId + " 고객의 문의채팅"); // 변경된 부분
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("chatRoomId", chatId);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChatRoomIds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtRoom;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtRoom = itemView.findViewById(R.id.txtRoom);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
