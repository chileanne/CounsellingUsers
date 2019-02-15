package com.gboron.ekpei.counsellingusers.Adapterclass;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gboron.ekpei.counsellingusers.Modelclass.podcastmodelclass;
import com.gboron.ekpei.counsellingusers.R;
import com.gboron.ekpei.counsellingusers.ReadmoreActivity;
import com.gboron.ekpei.counsellingusers.podcastcomment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EKPEI on 11/22/2018.
 */

public class podcastadapter extends  RecyclerView.Adapter<podcastadapter.MyHolder> {
    private  List<podcastmodelclass> mlist;
    private Context mcontext;
    private DatabaseReference mno_ofcomment_databaserefrence;
    private DatabaseReference mno_ofseen_databaserefrence;
    private FirebaseAuth mfirebaseauth;
    private String mcurrentuser_id;

    public podcastadapter(FragmentActivity activity, List<podcastmodelclass> mlist) {
        this.mlist=mlist;
        this.mcontext=activity;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.podcast_single_layout,parent,false);
        return new  MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        podcastmodelclass b=mlist.get(position);
        final String podimage=b.getThumbimage();
        final String title=b.getAdmin_article_title();
        final String article=b.getAdmin_article();
        final String pushid=b.getPushid();

        //initializing firebasauth
        mfirebaseauth=FirebaseAuth.getInstance();
        FirebaseUser currentuser=mfirebaseauth.getCurrentUser();
        mcurrentuser_id=currentuser.getUid();



        //setting the data to the views
        holder.title.setText(title);
        holder.article.setText(article);
        if(podimage.equals("default")){
            holder.mg.removeView(holder.podimage);
        }else {
            Glide.with(mcontext).load(podimage).into(holder.podimage);
        }


        //retrieve the number of comment
        mno_ofcomment_databaserefrence= FirebaseDatabase.getInstance().getReference().child("article_comment").child(pushid);
        mno_ofcomment_databaserefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count =dataSnapshot.getChildrenCount();
                int a=(int)count;
                String m=Integer.toString(a);
                holder.no_of_comment.setText(m);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               // String error=databaseError.getDetails().
            }
        });

        //retreive the number of person who has read the podcast
        //initializing this databaseref
        mno_ofseen_databaserefrence=FirebaseDatabase.getInstance().getReference().child("Seen_Articles").child(pushid);
        mno_ofseen_databaserefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count =dataSnapshot.getChildrenCount();
                int c=(int)count;
               // int e=c-1;
                String d=Integer.toString(c);
                holder.no_of_seen.setText(d);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //setting on click to the readmore activity
        holder.readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating map to store data of user that read post
                //initializing this databaseref
                mno_ofseen_databaserefrence=FirebaseDatabase.getInstance().getReference().child("Seen_Articles").child(pushid).child(mcurrentuser_id);
                Map seen=new HashMap();
                seen.put("article_title",title);
                seen.put("user_that_read", mcurrentuser_id);

                //save map to database
                mno_ofseen_databaserefrence.setValue(seen);
               // Toast.makeText(mcontext,pushid,Toast.LENGTH_SHORT).show();


                Intent podcast= new Intent(mcontext, ReadmoreActivity.class);
                podcast.putExtra("article",article);
                podcast.putExtra("article_title",title);
                podcast.putExtra("thumbimage",podimage);
                podcast.putExtra("post_pushid",pushid);
                mcontext.startActivity(podcast);
            }
        });


        //setting onclicklistener to the podcastcomment activity
        holder.comment_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent g= new Intent(mcontext, podcastcomment.class);
                g.putExtra("post_pushid",pushid);
                mcontext.startActivity(g);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView podimage,comment_image;
        TextView title,article,readmore,no_of_comment,no_of_seen;
        LinearLayout mg;
        public MyHolder(View v) {
            super(v);
            mg=v.findViewById(R.id.ko);
            podimage=v.findViewById(R.id.podcastimage);
            comment_image=v.findViewById(R.id.podcast_commentimage);
            title=v.findViewById(R.id.podcast_title);
            article=v.findViewById(R.id.podcast_article);
            readmore=v.findViewById(R.id.podcat_readmore);
            no_of_seen=v.findViewById(R.id.podcast_numberofseen);
            no_of_comment=v.findViewById(R.id.podcast_no_ofcomment);
        }
    }
}
