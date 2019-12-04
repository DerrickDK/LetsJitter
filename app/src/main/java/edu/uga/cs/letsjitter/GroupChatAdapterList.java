package edu.uga.cs.letsjitter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class GroupChatAdapterList extends RecyclerView.Adapter<GroupChatAdapterList.ViewHolder> {
    private static final int MSG_RIGHT = 1;
    private static final int MSG_LEFT = 0;
    private FirebaseUser myUser;
    private DatabaseReference myUserReference;
    private String userName;
    private Context myContext;
    private List<Chat> myChat;

    public GroupChatAdapterList(Context myContext, List<Chat> myChat) {
        this.myContext = myContext;
        this.myChat = myChat;
    }

    @NonNull
    @Override
    public GroupChatAdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_RIGHT){
            View view = LayoutInflater.from(myContext).inflate(R.layout.chat_widget_right, parent, false);
            return new GroupChatAdapterList.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(myContext).inflate(R.layout.chat_widget_left, parent, false);
            return new GroupChatAdapterList.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
//        myChat.get(position);
//        myUserReference = FirebaseDatabase.getInstance().getReference("Users").child(myUser.getUid());
//        myUserReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.hasChild("username")){
//                    userName = dataSnapshot.child("username").getValue().toString(); //get current username
//                    holder.showMessage.setText(myChat.get(position).getSender()+": "+myChat.get(position).getMessage());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        holder.showMessage.setText(myChat.get(position).getMessage());
        holder.userText.setText(myChat.get(position).getSenderName()+":");
      //  holder.profile_image.setImageResource(R.drawable.profileicon);
    }


    /**
     * Gets the group chat list size
     * @return
     */
    @Override
    public int getItemCount() {
        return myChat.size();
    }

    /**
     * This method
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        myUser = FirebaseAuth.getInstance().getCurrentUser();
        if(myChat.get(position).getSender().equals(myUser.getUid())){
            return MSG_RIGHT;
        }else{
            return MSG_LEFT;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView showMessage, userText;
        public ImageView profile_image;

        public ViewHolder(View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.show_message); //left or right message will be shown depending on user and receiver
            userText = itemView.findViewById(R.id.user_Text); //left or right message will be shown depending on user and receiver
            profile_image = itemView.findViewById(R.id.profileImage); //profile pic of user texting, if needed
        }
    }
}
