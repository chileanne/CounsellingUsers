package com.gboron.ekpei.counsellingusers;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gboron.ekpei.counsellingusers.Adapterclass.podcastcommetadapter;
import com.gboron.ekpei.counsellingusers.Modelclass.podcastacommentmodelclass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.list;

public class podcastcomment extends AppCompatActivity {
    private String post_pushid, mcurrentuser_id;
    private List<podcastacommentmodelclass> mlist;
    private FirebaseAuth mfirebaseauth;
    private podcastcommetadapter madapter;
    private  podcastacommentmodelclass mcommentmodel;
    private DatabaseReference mpodcastdabaseref;
    private EditText medit;
    private ImageView send;
    private Toolbar mtoolbar;
    private RecyclerView mrecyclerview;
    private  DatabaseReference mdatabasereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcastcomment);

        mlist=new ArrayList<>();
        //getting intent
        //take note the post_pushid will
        //be the current post id
        post_pushid=getIntent().getStringExtra("post_pushid");


        //setting up databaserefernce
        mpodcastdabaseref= FirebaseDatabase.getInstance().getReference().child("article_comment").child(post_pushid);
        mfirebaseauth =FirebaseAuth.getInstance();
        mcurrentuser_id=mfirebaseauth.getCurrentUser().getUid();


        mdatabasereference= FirebaseDatabase.getInstance().getReference().child("Users").child(mcurrentuser_id);



        //initializing and setting views
        mtoolbar =(Toolbar) findViewById(R.id.pocommentappbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Comments");

        //initializing recyclerview
        mrecyclerview=(RecyclerView) findViewById(R.id.recyarticlescomm);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getApplication()));
        madapter=new podcastcommetadapter(getApplicationContext(),mlist);
        mrecyclerview.setAdapter( madapter);

        medit=(EditText) findViewById(R.id.articlecommedit);
        send=(ImageView) findViewById(R.id.sendarticle);

        //reading from commndatabase
        mpodcastdabaseref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String comments= dataSnapshot.child("user_comment").getValue().toString();
                String current_admn_userid=dataSnapshot.child("user_id").getValue().toString();

                mcommentmodel=new podcastacommentmodelclass(comments,current_admn_userid);
                mlist.add(mcommentmodel);
                madapter=new podcastcommetadapter(getApplicationContext(),mlist);
                mrecyclerview.setAdapter(madapter);
                madapter.notifyDataSetChanged();



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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment=medit.getText().toString();
                if(!comment.isEmpty()){
                    DatabaseReference current_articlecomment_push= mpodcastdabaseref.push();
                    String push_id= current_articlecomment_push.getKey();

                    Map update=new HashMap();
                    update.put("user_comment",comment);
                    update.put("post_id", post_pushid);
                    update.put("user_id", mcurrentuser_id);
                    update.put("user_posting","Admin");





                    //bining the push key to the usercoment
                    Map updates=new HashMap();
                    updates.put("/"+push_id,update);

                    mpodcastdabaseref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Successfull", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mdatabasereference.child("online").setValue("true");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mdatabasereference.child("online").setValue("false");
    }
}
