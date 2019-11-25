package edu.uga.cs.letsjitter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> { //chatAdapter for MessagingActivity
    private static final int MSG_RIGHT = 1;
    private static final int MSG_LEFT = 0;
    private FirebaseUser myUser;
    private Chat chat;
    private Context myContext;
    private List<Chat> myChat;
    private String imageURL;

    public ChatAdapter(Context myContext, List<Chat> myChat, String imageURL) {
        this.myContext = myContext;
        this.myChat = myChat;
        this.imageURL = imageURL;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == MSG_RIGHT){
            View view = LayoutInflater.from(myContext).inflate(R.layout.chat_widget_right, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(myContext).inflate(R.layout.chat_widget_left, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        myChat.get(position);
        holder.showMessage.setText(myChat.get(position).getMessage());
//        if(imageURL.equals("default")){
//            holder.profile_image.setImageResource(R.drawable.profileicon);
//        }else{
//            Glide.with(myContext).load(imageURL).into(holder.profile_image);
//        }


    }
    @Override
    public int getItemCount() {
        return myChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        myUser = FirebaseAuth.getInstance().getCurrentUser();
        if(myChat.get(position).getSender().equals(myUser.getUid())){ //if the sender is the current user, then the text will be the right
            return MSG_RIGHT;
        }else{
            return MSG_LEFT;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView showMessage;
        public ImageView profile_image;

        public ViewHolder(View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.show_message); //left or right message will be shown depending on user and receiver
            profile_image = itemView.findViewById(R.id.profileImage); //profile pic of user texting, if needed
        }
    }

}
