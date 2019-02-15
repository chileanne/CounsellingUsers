package com.gboron.ekpei.counsellingusers.Adapterclass;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gboron.ekpei.counsellingusers.Modelclass.commentsmodelclass;
import com.gboron.ekpei.counsellingusers.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by EKPEI on 11/17/2018.
 */

public class Commentadapter extends RecyclerView.Adapter<Commentadapter.MyHolder> {
    private Context mcontext;
    private List<commentsmodelclass> commetlist;
    private DatabaseReference mdatabasereference;

    public Commentadapter(Context applicationContext, List<commentsmodelclass> commetlist) {
        this.mcontext=applicationContext;
        this.commetlist=commetlist;
    }

    @Override
    public Commentadapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_single_layout,parent,false);
        return new  MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final Commentadapter.MyHolder holder, int position) {
        commentsmodelclass f=commetlist.get(position);
        //retrrieve all comment
        String comment=f.getComments();
        holder.currentuser_comment.setText(comment);

        //retrieve current user id
        String currentuser_id=f.getCurrentuserid();

        //i want to access the database of the currentuser
        //so that i could retrieve is username and image

        mdatabasereference= FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabasereference.child(currentuser_id).addValueEventListener(new ValueEventListener() {
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
        return commetlist.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CircleImageView currentuserimage;
        TextView currentuser_name,currentuser_comment;
        public MyHolder(View v) {
            super(v);
            currentuserimage=v.findViewById(R.id.displayimmges);
            currentuser_name=v.findViewById(R.id.username);
            currentuser_comment=v.findViewById(R.id.retrievecoment);

          /*  Typeface face= Typeface.createFromAsset(mcontext.getAssets(), "font/Roboto-Italic.ttf");
            currentuser_comment.setTypeface(face);

            Typeface face1= Typeface.createFromAsset(mcontext.getAssets(), "font/Roboto-BoldItalic.ttf");
            currentuser_comment.setTypeface(face1);*/
        }
    }
}
