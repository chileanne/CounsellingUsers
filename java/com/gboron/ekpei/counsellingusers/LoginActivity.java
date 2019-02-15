package com.gboron.ekpei.counsellingusers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import static android.R.attr.y;

public class LoginActivity extends AppCompatActivity {
   // private Toolbar mtoolbar;
    private Button mbuttonsignin;
    private EditText edittextemail;
    private EditText edittextpassword;
    private TextView textviewsignup,forgotpass;
    private FirebaseAuth firebaseAuth;
   private ProgressDialog progressDialog;
    private ProgressBar mp;
    private DatabaseReference mdatabaseref;
    private CardView mca,mcd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing and setting views
       /* mtoolbar =(Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Login");*/


        //initialize firebaseauth
        firebaseAuth =FirebaseAuth.getInstance();

        //initializing
        textviewsignup = (TextView) findViewById(R.id.textviewsignup);
        forgotpass = (TextView) findViewById(R.id.forgot);
        edittextemail = (EditText) findViewById(R.id.edittextemail1);
        mbuttonsignin =(Button) findViewById(R.id.buttonsignin3);
        edittextpassword =(EditText) findViewById(R.id.edittextpassword2);
       mp=(ProgressBar) findViewById(R.id.spin);
        mca=(CardView) findViewById(R.id.cardView2);
        mcd=(CardView) findViewById(R.id.cardView);

       // mp.setVisibility();

        //initializing or creating a progress dialog
     //  progressDialog = new ProgressDialog(this);


        mdatabaseref= FirebaseDatabase.getInstance().getReference().child("Users");


        //going to the signup activity
        textviewsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(main_intent);
                finish();

            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main_intent=new Intent(LoginActivity.this,ForgetActivity.class);
                startActivity(main_intent);
                finish();
            }
        });

        //check if the user is currently logged in if yes go to the profile ativity
        if (firebaseAuth.getCurrentUser() !=null)
        {   //start profile activity
            finish();//close current profile
            startActivity(new Intent(getApplicationContext(),MainActivity.class));}

        mbuttonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =  edittextemail.getText().toString().trim();
                String password = edittextpassword.getText().toString().trim();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                    mca.setVisibility(View.INVISIBLE);
                    mcd.setVisibility(View.INVISIBLE);
                        mp.setVisibility(View.VISIBLE);
                  //  progressDialog.setTitle("registering user");
                   // progressDialog.setCanceledOnTouchOutside(false);
                    //progressDialog.show();
                    loginuser(email,password);
                }else if( TextUtils.isEmpty(email)  ){
                    mca.setVisibility(View.VISIBLE);
                    mcd.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this,"Email field is empty",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(password)){
                    mca.setVisibility(View.VISIBLE);
                    mcd.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this,"password field is empty",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }//end of oncreate method

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void loginuser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){


                    FirebaseUser muser=FirebaseAuth.getInstance().getCurrentUser();
                    final String uid=muser.getUid();
                  /*  muser.getToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if(task.isSuccessful()){
                                String token_id= FirebaseInstanceId.getInstance().getToken();
                                mdatabaseref.child(uid).child("devicetoken").setValue(token_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            progressDialog.dismiss();

                                            Intent main_intent=new Intent(LoginActivity.this,MainActivity.class);
                                            startActivity(main_intent);
                                            finish();
                                        }
                                    }
                                });

                            }
                        }
                    });*/

                    String token_id= FirebaseInstanceId.getInstance().getToken();
                    HashMap usermaps=new HashMap<>();
                    usermaps.put("devicetoken",token_id);
                    mdatabaseref.child(uid).updateChildren(usermaps).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mp.setVisibility(View.INVISIBLE);
                               // progressDialog.dismiss();

                                Intent main_intent=new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(main_intent);
                                finish();
                            }
                        }
                    });

                }
                else{
                    String m=task.getException().getMessage().toString();
                    mp.setVisibility(View.INVISIBLE);
                    mca.setVisibility(View.VISIBLE);
                    mcd.setVisibility(View.VISIBLE);
                   // progressDialog.hide();
                    Toast.makeText(LoginActivity.this,m,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
