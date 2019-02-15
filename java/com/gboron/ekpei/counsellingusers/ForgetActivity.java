package com.gboron.ekpei.counsellingusers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgetActivity extends AppCompatActivity {
    private EditText pass,email;
    private Button login;
    private  String emails,passed;
  private FirebaseUser user;
    private FirebaseAuth mauth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

       // pass=(EditText) findViewById(R.id.forpass);
        email=(EditText) findViewById(R.id.mailaddress);
        login=(Button) findViewById(R.id.log);

        //create progress dialog
        progressDialog = new ProgressDialog(this);
      //  user= FirebaseAuth.getInstance().getCurrentUser();
        mauth=FirebaseAuth.getInstance();

        emails=email.getText().toString();
       // passed=pass.getText().toString();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Authenticating");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                emails=email.getText().toString();
               // passed=pass.getText().toString();
               // updatsd(passed);
                passreset(emails);

            }
        });
    }

    private void passreset(String emails) {
        if(mauth!=null && !TextUtils.isEmpty(emails)){
            mauth.sendPasswordResetEmail(emails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Succesful",Toast.LENGTH_SHORT).show();
                        Intent main_intent=new Intent(ForgetActivity.this,LoginActivity.class);
                        startActivity(main_intent);
                        finish();


                    }else{
                        String m=task.getException().getMessage().toString();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),m,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if(TextUtils.isEmpty(emails)) {

            Toast.makeText(getApplicationContext(),"Input Email",Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private void updatsd( String passed) {
       // AuthCredential credentials= EmailAuthProvider.getCredential(emails);
       // user.reauthenticate()



       /* user.updatePassword(passed).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Succesful",Toast.LENGTH_SHORT).show();
                    Intent main_intent=new Intent(ForgetActivity.this,LoginActivity.class);
                    startActivity(main_intent);
                    finish();


                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
}
