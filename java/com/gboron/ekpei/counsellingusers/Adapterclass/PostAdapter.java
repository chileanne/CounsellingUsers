package com.gboron.ekpei.counsellingusers.Adapterclass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gboron.ekpei.counsellingusers.CommentActivity;
import com.gboron.ekpei.counsellingusers.Modelclass.Forummodelclass;
import com.gboron.ekpei.counsellingusers.Modelclass.commentsmodelclass;
import com.gboron.ekpei.counsellingusers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.Build.VERSION_CODES.O;

/**
 * Created by EKPEI on 11/6/2018.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyHolder> {

  private  List<Forummodelclass> a;
   private Context mcontext;
    private DatabaseReference mdatabasereference;
    private DatabaseReference mlikesreference;
    private String Username,userthumbimage,mcurrentuser_id;
    private FirebaseAuth mfirebaseauth;
    private String currentstate;
    private  int d=1;

    public PostAdapter( Context applicationContext, List<Forummodelclass> postlist) {
        this.a=postlist;
        this.mcontext=applicationContext;
    }



    @Override
    public PostAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_single_layout,parent,false);
        return new  MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostAdapter.MyHolder holder, int position) {
        currentstate="not_liked";
        holder.setIsRecyclable(false);
        //setting firebase auth to retrieve the currentuser id
        mfirebaseauth=FirebaseAuth.getInstance();
        FirebaseUser current_user=mfirebaseauth.getCurrentUser();
        mcurrentuser_id=current_user.getUid();




        Forummodelclass g=a.get(position);
        //retrieving the postimage in the imaeview
        String posted_image=g.getThumbimage();
        if ( posted_image.equals("default")){
           // Toast.makeText(mcontext,"defult",Toast.LENGTH_SHORT).show();
           // holder.ml.removeView(holder.mpostimage);
            holder.mpostimage.setVisibility(View.GONE);
        }else{
            Glide.with(mcontext).load(posted_image)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.mpostimage.setVisibility(View.GONE);
                           // Toast.makeText(mcontext,"error displaying picture",Toast.LENGTH_SHORT).show();

                            //holder.ml.removeView(holder.mpostimage);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.mpostimage.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(holder.mpostimage);

        }



        //retrieving date of the post
        String dates=g.getDate();
        holder.mdate.setText(dates);

        //retrieving the text post
        String text_post=g.getPost_text();
        holder.mpostdetails.setText(text_post);


        //retrieving   userid(the person who posted in the blod, id)
        // and userthumbimage(the person postedin the blog, thumbimage)
        final String postuserid=g.getUser_id();
        final String pushid=g.getPushid();

        mdatabasereference= FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabasereference.child(postuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("username").getValue().toString();
                String  thumbimage = dataSnapshot.child("thumbimage").getValue().toString();

                //storing name and thumbimage
                Username=name;
                userthumbimage=thumbimage;

                holder.musername.setText(name);
                Glide.with(mcontext).load(thumbimage).placeholder(R.mipmap.person).into(holder.muserthumbimage);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //retrieving the no of likes realtime
        mlikesreference= FirebaseDatabase.getInstance().getReference().child("Comment").child(pushid);
        mlikesreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    long count =dataSnapshot.getChildrenCount();
                    int a=(int)count;
                    String h=Integer.toString(a);

                    //i had to subtract 1 from the size
                    //bcos the datasnapshot size was 2
                   // int b=a-1;
                    holder.likestext.setText(h);
                }else {
                    holder.likestext.setText("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

     /*   holder.mlikesimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(mcontext,"just liked",Toast.LENGTH_SHORT).show();
               /* mlikesreference= FirebaseDatabase.getInstance().getReference().child("Likes").child(pushid);
                mlikesreference.child(mcurrentuser_id).child("user_that_like").setValue(mcurrentuser_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(mcontext,"post liked",Toast.LENGTH_SHORT).show();
                    }
                });*/


              /*  mlikesreference.child(mcurrentuser_id).child("user_that_like").setValue(mcurrentuser_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(mcontext,"post liked",Toast.LENGTH_SHORT).show();
                    }
                });*/


               /* Toast.makeText(mcontext,"just liked",Toast.LENGTH_SHORT).show();
                mlikesreference= FirebaseDatabase.getInstance().getReference().child("Likes").child(pushid);
                mlikesreference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(mcontext,"just clicked",Toast.LENGTH_SHORT).show();
                            if (dataSnapshot.hasChild(mcurrentuser_id)) {
                                Toast.makeText(mcontext,"working",Toast.LENGTH_SHORT).show();
                                mlikesreference.child(mcurrentuser_id).child("user_that_like").setValue(mcurrentuser_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(mcontext,"post liked",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else if(!dataSnapshot.hasChild(mcurrentuser_id)){
                                mlikesreference.child(mcurrentuser_id).removeValue();
                                Toast.makeText(mcontext,"unliked liked",Toast.LENGTH_SHORT).show();
                            }

                        }else if(!dataSnapshot.exists()){

                            Toast.makeText(mcontext,"no datasnapxhot",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });*/


        //intent to comment activity
        holder.mcommentimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comment= new Intent(mcontext, CommentActivity.class);
                comment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                comment.putExtra("userid",postuserid);
                comment.putExtra("post_pushid",pushid);
                mcontext.startActivity(comment);
            }
        });


    }

    @Override
    public int getItemCount() {
        return a.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView mpostdetails,musername,mdate,likestext;
        ImageView mpostimage,mlikesimage,mcommentimage;
        CircleImageView muserthumbimage;
        LinearLayout ml;

        public MyHolder(View v) {
            super(v);
            ml=v.findViewById(R.id.lv);
            muserthumbimage= v.findViewById(R.id.userimage);
            musername= v.findViewById(R.id.username);
            mdate= v.findViewById(R.id.date);
            mpostimage=v.findViewById(R.id.userpostedmage);
           // mlikesimage=v.findViewById(R.id.likes);
            mcommentimage=v.findViewById(R.id.comment);
            likestext=v.findViewById(R.id.likestext);
            mpostdetails= v.findViewById(R.id.userpost);

        }
    }
}
