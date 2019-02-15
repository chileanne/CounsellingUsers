package com.gboron.ekpei.counsellingusers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private Button btnregister;
    private EditText editTextEmail;
    private EditText editTextusername;
    private EditText editTextpasword;
    private TextView textviewsignin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mdatabaseref,mpostdabaseref;
  //  private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initialize views
        editTextusername = (EditText) findViewById(R.id.editTextusername);
        btnregister = (Button) findViewById(R.id.btnRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextpasword = (EditText) findViewById(R.id.editTextPasword);
        textviewsignin = (TextView) findViewById(R.id.textc);

        //toolbar set
       /* mtoolbar=(Toolbar) findViewById(R.id.registertoolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Create user");*/

        //going to the login activity
        textviewsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(main_intent);
                finish();

            }
        });

        //create progress dialog
        progressDialog = new ProgressDialog(this);

        //intalize firebaseauthobject
        firebaseAuth = FirebaseAuth.getInstance();
        //check if the user is currently logged in if yes go to the profile ativity
        if (firebaseAuth.getCurrentUser() != null) {   //start userinfo activity
            finish();//close current profile
            startActivity(new Intent(getApplicationContext(), MainActivity.class));


        }


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get email and password fron edit text
                String display_user=editTextusername.getText().toString().trim();
                String email =editTextEmail.getText().toString().trim();
                String password =editTextpasword.getText().toString().trim();

                if(!TextUtils.isEmpty(display_user) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                    progressDialog.setTitle("registering user");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    register_user(display_user,email,password);

                }else if( TextUtils.isEmpty(email)  ){
                    Toast.makeText(SignupActivity.this,"Email field is empty",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(password)){
                    Toast.makeText(SignupActivity.this,"password field is empty",Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(display_user)){
                    Toast.makeText(SignupActivity.this," Username  field is empty",Toast.LENGTH_SHORT).show();

                }


            }
        });

    }//end of oncreate

    private void register_user(final String display_user, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    //get firebase user uid
                    FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
                    String uid=currentuser.getUid();

                    //instatiating the databaseref and ading a users and curent user uid node to the rootview
                    mdatabaseref= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    //intitiating another databaseref in order to store data for the posting database
                    mpostdabaseref=FirebaseDatabase.getInstance().getReference().child("Posts").child(uid);

                    //if this task is successful i want to
                    //add the display user into firebase databse and uses it on the profile activity




                    String token_id= FirebaseInstanceId.getInstance().getToken();

                    //input uservalues into firebase using Hasmap
                    HashMap<String, String> usermap=new HashMap<>();
                    usermap.put("username", display_user);
                    usermap.put("online", "default");
                    usermap.put("devicetoken",token_id);
                    usermap.put("occupation", "");
                    usermap.put("image", "default");
                    usermap.put("thumbimage","default");
                    usermap.put("hobbies","");
                    usermap.put("phone number","");


                    //progressDialog.dismiss();
                    //setting the inputed values and checking of the task is succesful
                    mdatabaseref.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task .isSuccessful()){
                                progressDialog.dismiss();
                                Intent main_intent=new Intent(SignupActivity.this,MainActivity.class);
                                startActivity(main_intent);
                                finish();
                            }
                        }
                    });








                }
                else{
                    String m=task.getException().getMessage().toString();
                    progressDialog.hide();
                    Toast.makeText(SignupActivity.this,m,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
