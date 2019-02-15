package com.gboron.ekpei.counsellingusers.Adapterclass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gboron.ekpei.counsellingusers.Modelclass.podcastacommentmodelclass;
import com.gboron.ekpei.counsellingusers.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by EKPEI on 11/23/2018.
 */

public class podcastcommetadapter extends RecyclerView.Adapter <podcastcommetadapter.MyHolder>{
    private Context mcontext;
    private List<podcastacommentmodelclass> mlist;
    private FirebaseAuth mfirebaseauth;
    private String mcurrent_user;
    private DatabaseReference mdataref;
    public podcastcommetadapter(Context applicationContext, List<podcastacommentmodelclass> mlist) {
        this.mcontext=applicationContext;
        this.mlist=mlist;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.podcomment_single_layout,parent,false);
        return new  MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        //initializing firebase instances
        mfirebaseauth=FirebaseAuth.getInstance();
        mcurrent_user=mfirebaseauth.getCurrentUser().getUid();

        //getting data from arraylist
        podcastacommentmodelclass h=mlist.get(position);
        //retrieve all data from modelclass
        String comment=h.getComments();
        String userid=h.getCurrent_admin_userid();


        //get comments
        holder.currentuser_comment.setText(comment);

        //initializing firebase database ref
        mdataref= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        mdataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("username").getValue().toString();
                String  thumbimage = dataSnapshot.child("thumbimage").getValue().toString();


                holder.currentuser_name.setText(name);
                Glide.with(mcontext).load(thumbimage).placeholder(R.mipmap.person).into(holder.currentuserimage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        CircleImageView currentuserimage;
        TextView currentuser_name,currentuser_comment;
        public MyHolder(View v) {
            super(v);
            currentuserimage=v.findViewById(R.id.display_user_immges);
            currentuser_name=v.findViewById(R.id.user_username);
            currentuser_comment=v.findViewById(R.id.retrievepodcoments);
        }
    }
}
