package com.hyunsungkr.pethotel.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyunsungkr.pethotel.R;
import com.hyunsungkr.pethotel.model.ChatDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private List<ChatDTO> chatList;
    private String userId;
    private SimpleDateFormat dateFormat;
    private String lastUserName = ""; // 이전 유저 이름 저장 변수 추가
    private String lastSentTime = "";
    String sentTimeInMinutesString;

    public ChatAdapter(Context context, List<ChatDTO> chatList, String userId) {
        this.context = context;
        this.chatList = chatList;
        this.userId = userId;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row2, parent, false);
            // 좌측 말풍선이 필요할 경우 아래 주석을 해제하고 R.layout.chat_row_left로 수정하세요.
            // view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_row_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatDTO chat = chatList.get(position);

        // 현재 뷰홀더에 표시되는 유저의 이름과 보낸 시간이 이전 뷰홀더와 같으면
        // 이름과 보낸 시간을 숨김 처리한다.
        Log.i("제바좀 되라", lastSentTime);
        Log.i("제바좀 되라2", chat.getSentTime());
        Log.i("제바좀 되라3", chat.getUserName());
        Log.i("제바좀 되라3", lastUserName);
        if (chat.getUserName().equals(lastUserName) && chat.getSentTime().equals(lastSentTime)) {
            holder.hideUserNameAndSendTime();
        } else { // 다른 유저 또는 다른 시간이라면 이름과 보낸 시간을 표시하고 lastUserName, lastSentTime을 업데이트한다.
            holder.showUserNameAndSendTime(chat.getUserName(), chat.getSentTime());
            lastUserName = chat.getUserName();
            lastSentTime = chat.getSentTime();
        }

        // 현재 뷰홀더에 표시되는 메시지의 시간이 이전에 표시한 메시지의 시간과 같으면 시간을 숨김 처리한다.
        if (position > 0 && chat.getSentTime().equals(chatList.get(position - 1).getSentTime())) {
            holder.hideSendTime();
        } else {
            holder.showSendTime(chat.getSentTime());
        }


        // 메시지 보낸 사람에 따라 말풍선을 오른쪽/왼쪽에 위치시킴
        if (chat.getUserId().equals(userId)) { // Message I sent
            holder.txtMessage.setGravity(Gravity.END);

            holder.txtSendTime.setGravity(Gravity.END);
        }else if (chat.getUserId().equals("55")){
            holder.txtUserName.setGravity(Gravity.END);

            holder.txtSendTime.setGravity(Gravity.END);
        }

        else { // message sent by the other party
            holder.txtMessage.setGravity(Gravity.START);

            holder.txtSendTime.setGravity(Gravity.START);
        }


        // 메시지 내용을 설정
        holder.txtMessage.setText(chat.getEditText());



        // 메시지 보낸 시간을 설정


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatDTO chat = chatList.get(position);
        if (chat.getUserId().equals(userId)) { // 내가 보낸 메시지일 경우
            return 0; // 오른쪽 말풍선을 사용
        } else { // 상대방이 보낸 메시지일 경우
            return 1; // 왼쪽 말풍선을 사용
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtMessage, txtSendTime;

        public ViewHolder(View itemView) {
            super(itemView);

            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtSendTime = itemView.findViewById(R.id.txtSendTime);
        }


        public void showUserNameAndSendTime(String userName, String sendTime) {
            txtUserName.setVisibility(View.VISIBLE);
            txtSendTime.setVisibility(View.VISIBLE);
            txtUserName.setText(userName);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date date = null;
            try {
                date = sdf.parse(sendTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                txtSendTime.setText(DateFormat.format("MMM dd, hh:mm aa", date));
                txtSendTime.setVisibility(View.VISIBLE);
            } else {
                txtSendTime.setVisibility(View.GONE);
            }
        }

        public void hideSendTime() {
            txtSendTime.setVisibility(View.GONE);
        }

        public void showSendTime(String sendTime) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date date = null;
            try {
                date = sdf.parse(sendTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                txtSendTime.setText(DateFormat.format("MMM dd, hh:mm aa", date));
                txtSendTime.setVisibility(View.VISIBLE);
            } else {
                txtSendTime.setVisibility(View.GONE);
            }
        }


        public void hideUserNameAndSendTime() {
            txtUserName.setVisibility(View.GONE);
            txtSendTime.setVisibility(View.GONE);
        }
    }
}

