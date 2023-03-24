package com.hyunsungkr.pethotel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.model.ChatDTO;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private List<ChatDTO> chatList;

    public ChatAdapter(Context context, List<ChatDTO> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatDTO chat = chatList.get(position);
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtMessage;


        public ViewHolder(View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);

        }

        public void bind(ChatDTO chat) {
            txtMessage.setText(chat.getUserName()+ ":" + chat.getEditText());

        }
    }
}
