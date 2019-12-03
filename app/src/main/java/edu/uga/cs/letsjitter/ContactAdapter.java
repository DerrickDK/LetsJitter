package edu.uga.cs.letsjitter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context myContext;
    private DatabaseReference myDatabase;
    private List<ContactUser> myUser;
    private ContactUser user;

    public ContactAdapter(Context myContext, List<ContactUser> myUser) {
        this.myContext = myContext; //this class context
        this.myUser = myUser; //this class user
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.contact_item, parent, false);
        return new ContactAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      //  user = new ContactUser();
       // user = myUser.get(position); //get a specific index
        System.out.println("Full: "+myUser.get(position).toString() + " "+position);
        holder.username.setText(myUser.get(position).getUsername());
        if(myUser.get(position).getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.drawable.profileicon);
        }else{
            Glide.with(myContext).load(myUser.get(position).getImageURL()).into(holder.profile_image);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { //deletes a group
            @Override
            public boolean onLongClick(View view) {
                myDatabase = FirebaseDatabase.getInstance().getReference("Users");
                myDatabase.child(myUser.get(position).getUserID()).removeValue();
                Toast.makeText(myContext, "Remove: "+myUser.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });


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
        return myUser.size(); //show all items in myUser ContactUsers array
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
