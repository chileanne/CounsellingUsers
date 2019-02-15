package com.gboron.ekpei.counsellingusers;

import android.content.Intent;
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

import com.gboron.ekpei.counsellingusers.Adapterclass.Commentadapter;
import com.gboron.ekpei.counsellingusers.Adapterclass.PostAdapter;
import com.gboron.ekpei.counsellingusers.Modelclass.commentsmodelclass;
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

public class CommentActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private String mcurrentuser_id;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mdatabaserefernce;
    private RecyclerView mrecyclerview;
    private EditText medittext;
    private ImageView msend;
    private Commentadapter commentadapter;
    private  commentsmodelclass commentsmodelclass;
    private List<commentsmodelclass> commetlist;
    private  String post_id;
    private String  post_pushid;
    private DatabaseReference mdatabasereference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        //initializing and setting views
        mtoolbar =(Toolbar) findViewById(R.id.commentappbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Comments");

         post_id= getIntent().getStringExtra("user_id");
        post_pushid= getIntent().getStringExtra("post_pushid");

        //instiating the araylist
        commetlist=new ArrayList<>();
        medittext=(EditText) findViewById(R.id.commedit);
        msend=(ImageView) findViewById(R.id.send);

        mrecyclerview=(RecyclerView) findViewById(R.id.recycomm);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getApplication()));
       commentadapter=new Commentadapter(getApplicationContext(),commetlist);
        mrecyclerview.setAdapter(commentadapter);



        //initializing firebase
        firebaseAuth =FirebaseAuth.getInstance();
        mcurrentuser_id=firebaseAuth.getCurrentUser().getUid();

        mdatabasereference= FirebaseDatabase.getInstance().getReference().child("Users").child(mcurrentuser_id);
        mdatabaserefernce= FirebaseDatabase.getInstance().getReference().child("Comment").child( post_pushid);
       // final String push_id=mdatabaserefernce.getKey();

        //read from database
        mdatabaserefernce.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String comments= dataSnapshot.child("user_comment").getValue().toString();
                String currentuserid=dataSnapshot.child("currentuserid").getValue().toString();

                commentsmodelclass=new commentsmodelclass(comments,currentuserid);
            commetlist.add(commentsmodelclass);
                commentadapter=new Commentadapter(getApplicationContext(),commetlist);
                mrecyclerview.setAdapter(commentadapter);
                commentadapter.notifyDataSetChanged();


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

        //set onclick listener
        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment= medittext.getText().toString();
                if(!comment.isEmpty()){

                    DatabaseReference current_user_push= mdatabaserefernce.push();
                    String push_id= current_user_push.getKey();
                    Map update=new HashMap();
                    update.put("user_comment",comment);
                    update.put("post_id", post_id);
                    update.put("currentuserid", mcurrentuser_id);



                    //bining the push key to the usercoment
                    Map updates=new HashMap();
                    updates.put("/"+push_id,update);

                    mdatabaserefernce.updateChildren(updates).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Successfull", Toast.LENGTH_LONG).show();
                            medittext.setText("");
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
