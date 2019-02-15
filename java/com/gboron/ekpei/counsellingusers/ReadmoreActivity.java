package com.gboron.ekpei.counsellingusers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReadmoreActivity extends AppCompatActivity {
    private TextView marticles;
    private ImageView mimage;
    private FirebaseAuth firebaseAuth;
    private String articlestring,titlestring,thumimage,mcurrentuser_id,post_pushid;
    private Toolbar mtoolbar;
    private ImageView commentimage;
    private DatabaseReference  mdatabasereference;
    private LinearLayout mlinear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readmore);

        titlestring= getIntent().getStringExtra("article_title");
        articlestring= getIntent().getStringExtra("article");
        thumimage=getIntent().getStringExtra("thumbimage");
        post_pushid=getIntent().getStringExtra("post_pushid");

        mtoolbar =(Toolbar) findViewById(R.id.readmoreappbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle(titlestring);

        firebaseAuth =FirebaseAuth.getInstance();
        mcurrentuser_id=firebaseAuth.getCurrentUser().getUid();

        mdatabasereference= FirebaseDatabase.getInstance().getReference().child("Users").child(mcurrentuser_id);

        marticles=(TextView) findViewById(R.id.readarticles);
        mimage=(ImageView) findViewById(R.id.readimage);
        commentimage=(ImageView) findViewById(R.id.readcomment);
        mlinear=(LinearLayout) findViewById(R.id.ll);




        marticles.setText(articlestring);
        Glide.with(getApplicationContext()).load(thumimage).placeholder(R.drawable.ava).into(mimage);



        commentimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comment= new Intent(getApplicationContext(), podcastcomment.class);
                comment.putExtra("post_pushid",post_pushid);
                startActivity(comment);

               // mlinear.removeView(mimage);
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
