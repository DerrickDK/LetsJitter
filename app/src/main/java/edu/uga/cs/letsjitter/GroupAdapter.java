package edu.uga.cs.letsjitter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private FirebaseUser myUser;
    private DatabaseReference myDatabase;
    private Group group;
    private Context myContext;
    private List<Group> myGroups;
    private String userName;

    /**
     * Object of contact's list
     * @param myContext the context of the application
     * @param myGroups the list of user's in database
     */
    public GroupAdapter(Context myContext, List<Group> myGroups) {
        this.myContext = myContext; //this class context
        this.myGroups = myGroups; //this class user
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.group_item, parent, false);
        return new GroupAdapter.ViewHolder(view);
    }
    /**
     * This function displays the groups on the screen
     * @param holder object wraps user object in a view to be displayed
     * @param position position of the group object in the user list array
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        System.out.println("Group: "+myGroups.get(position).toString() + " "+position);
        holder.showGroupName.setText(myGroups.get(position).getGroupName());

        if(myGroups.get(position).getGroupName() != null){ //if a groupName exist
            holder.groupImage.setImageResource(R.drawable.groupicon);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() { //deletes a group
            @Override
            public boolean onLongClick(View view) {
                myDatabase = FirebaseDatabase.getInstance().getReference("Groups");
                myDatabase.child(myGroups.get(position).getGroupName()).removeValue();
                Toast.makeText(myContext, "Removed: "+myGroups.get(position).getGroupName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // System.out.println("I have been clicked "+myGroups.get(position));

                Intent intent = new Intent(myContext, GroupChatActivity.class);
                intent.putExtra("groupName", myGroups.get(position).getGroupName());
                myContext.startActivity(intent);
            }
        });

    }
    /**
     * @return the size of the list of groups array
     */
    @Override
    public int getItemCount() {
        return myGroups.size();
    }

    /**
     * Object constructor of viewing the users on the screen.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView showGroupName;
        public ImageView groupImage;

        public ViewHolder(View itemView) {
            super(itemView);
            showGroupName = itemView.findViewById(R.id.groupName); //left or right message will be shown depending on user and receiver
            groupImage = itemView.findViewById(R.id.groupImage); //profile pic of user texting, if needed
        }
    }
}
