package com.gboron.ekpei.counsellingusers.Editing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gboron.ekpei.counsellingusers.R;
import com.gboron.ekpei.counsellingusers.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditprofileActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private EditText nmanmeedittext,moccupationedittext,mhobbiedittext,mphonenoedittext;
    private Button muploadbtn;
    private DatabaseReference muploadDatabase;
    private FirebaseUser mcurrentuser;
    private ProgressDialog mprogress;
    private  String image,thumbimage;
    private DatabaseReference  mdatabasereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        //toolbar set
        mtoolbar=(Toolbar) findViewById(R.id.appbared);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Edit Profile");

        mcurrentuser= FirebaseAuth.getInstance().getCurrentUser();
        String userid=mcurrentuser.getUid();
        muploadDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

        mdatabasereference=FirebaseDatabase.getInstance().getReference().child("Users").child(userid);




        //initializing other views
        nmanmeedittext=(EditText) findViewById(R.id.nameedittext);
        moccupationedittext=(EditText) findViewById(R.id.occupationedittext);
       mhobbiedittext=(EditText) findViewById(R.id.hobbiesedittext);
       mphonenoedittext=(EditText) findViewById(R.id.phonenumberedittext);
        muploadbtn=(Button) findViewById(R.id.uploadbtn);


        //saving info to databse
        muploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String name=nmanmeedittext.getText().toString();
                String occupation=moccupationedittext.getText().toString();
                String hobbies=mhobbiedittext.getText().toString();
                String phoneno=mphonenoedittext.getText().toString();
             //  String images=getIntent().getStringExtra("imagesent");

              // String thumbimages=getIntent().getStringExtra("thumbimagesent");

                //input uservalues into firebase using Hasmap
               Map usermap=new HashMap<>();
                usermap.put("username", name);
                usermap.put("occupation", occupation);
               // usermap.put("image", images);
               // usermap.put("thumbimage",thumbimages);
              usermap.put("hobbies",hobbies);
                usermap.put("phone number",phoneno);


                //initalizing progressdialog
                mprogress=new ProgressDialog(EditprofileActivity.this);
                mprogress.setTitle("updating changes");
                mprogress.setMessage("please wait abeg oooooo");
                mprogress.show();


            muploadDatabase.updateChildren(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mprogress.dismiss();

                }
                }
            });
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
