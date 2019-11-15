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

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context myContext;
    private List<ContactUser> myUser;
    private ContactUser user;

    public ContactAdapter(Context myContext, List<ContactUser> myUser) {
        this.myContext = myContext;
        this.myUser = myUser;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.contact_item, parent, false);
        return new ContactAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        user = new ContactUser();
        user = myUser.get(position); //get a spacific index
        System.out.println("Full: "+user.toString() + " "+position);
        holder.username.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.drawable.profileicon);
        }else{
            Glide.with(myContext).load(user.getImageURL()).into(holder.profile_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // System.out.println("Position clicked: "+holder.getLayoutPosition());
               // System.out.println("User clicked: "+myUser.get(holder.getAdapterPosition()));
               // System.out.println("User clicked: "+user.toString());
               // System.out.println("Item :"+ holder.getAdapterPosition());
                Intent intent = new Intent(myContext, MessagingActivity.class);
                intent.putExtra("userID", myUser.get(holder.getAdapterPosition()).getUserID()); //use list value instead
                myContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return myUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username); //username from contactUser_item or other
            profile_image = itemView.findViewById(R.id.profileImage);

        }
    }

}
