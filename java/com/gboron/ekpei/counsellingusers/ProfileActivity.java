package com.gboron.ekpei.counsellingusers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gboron.ekpei.counsellingusers.Editing.EditprofileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static android.R.attr.data;

public class ProfileActivity extends AppCompatActivity {
    private static final int GALLERY_PICK = 1;
    private Button meditproflebtn;
    private TextView mnametextview, moccupationtextview, mhoobietextview, mphonenotextview;
    private ImageView mdisplaymage;
    private Toolbar mtoolbar;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentuser;
    private String thumbimage, image;
    private ProgressDialog progressDialog;
    private Bitmap thumb_bitmap;
   private String currentuseruid;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference  mdatabasereference;
    private StorageReference mimagestorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mdisplaymage = (ImageView) findViewById(R.id.insertimageview);
        meditproflebtn = (Button) findViewById(R.id.editprofilebtn);
        mnametextview = (TextView) findViewById(R.id.nametextview);
        mphonenotextview = (TextView) findViewById(R.id.phonenotextview);
        moccupationtextview = (TextView) findViewById(R.id.occupationtextview);
      mhoobietextview = (TextView) findViewById(R.id.hobbiestextview);
        mtoolbar = (Toolbar) findViewById(R.id.appbarss);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("About Me");

        //create progress dialog
        progressDialog = new ProgressDialog(this);


        //retrieve all detail stored in database
        //--------so watch and learn-------------
        //initialize firebaseauth
        firebaseAuth =FirebaseAuth.getInstance();

        //to store the path of imagi strorage
       mimagestorage = FirebaseStorage.getInstance().getReference();

        //am gonna get the current user id
        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        currentuseruid = mCurrentuser.getUid();

        mdatabasereference= FirebaseDatabase.getInstance().getReference().child("Users").child(currentuseruid);

        //this points to the current user id
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentuseruid);

        //retrieving all data in real time
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("username").getValue().toString();
                String oocupation = dataSnapshot.child("occupation").getValue().toString();
               String hobbies = dataSnapshot.child("hobbies").getValue().toString();
              //  thumbimage = dataSnapshot.child("thumbimage").getValue().toString();
                image = dataSnapshot.child("image").getValue().toString();
                String phoneno = dataSnapshot.child("phone number").getValue().toString();


                //setting values to the views
             mhoobietextview.setText(hobbies);
                moccupationtextview.setText(oocupation);
                mnametextview.setText(name);
                mphonenotextview.setText(phoneno);
              Glide.with(getApplicationContext()).load(image).placeholder(R.drawable.ava).into(mdisplaymage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //passing an intent to the edit profile activity
        meditproflebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //passing the values of image and thumbimage string
                //so as update the database with the others




                //intent to another activity
                Intent main_intent = new Intent(ProfileActivity.this, EditprofileActivity.class);
                startActivity(main_intent);


            }
        });


        //seting onclik listener on imageview to point to gallery
        mdisplaymage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            /* CropImage.activity()
                     .setGuidelines(CropImageView.Guidelines.ON)
                     .start(getApplicationContext());*/
            }
        });


    }// end of oncreate method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
           progressDialog.setTitle("Uploading image");
            progressDialog.setMessage("please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            Uri imageuri = data.getData();

            if (imageuri != null && firebaseAuth.getCurrentUser() !=null) {

                final File thumb_filepath = FileUtils.getFile(this, imageuri);
              //  String current_user_id = mCurrentuser.getUid();

                //pre-checking i case for error during compression
              try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(40)
                            .compressToBitmap(thumb_filepath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,40, baos);
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath=mimagestorage.child("profileimage").child(currentuseruid +".jpg");
                final StorageReference thumbfilepath=mimagestorage.child("profileimage").child("thumbs").child(currentuseruid+"jpg");
                filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            final String download_url=task.getResult().getDownloadUrl().toString();
                            //upload task for uploading bytes to storage
                            UploadTask uploadTask=thumbfilepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    String thumbimage_url=task.getResult().getDownloadUrl().toString();
                                    if(task.isSuccessful()){
                                        //creatig a map to store the image and thumbimage  url to the database
                                        Map update=new HashMap();
                                        update.put("image",download_url);
                                        update.put("thumbimage",thumbimage_url);

                                        //updating the database with the new data of the image and thumbimage
                                        //downloadurl
                                        mUserDatabase.updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    progressDialog.dismiss();

                                                }
                                            }
                                        });//end of mUserdatabse
                                    }
                                }
                            });//end of uploadtask oncompetiton listener


                        }else{
                            Toast.makeText(ProfileActivity.this, "failed in uploading image", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });//end of filepath.putfile method


            }

        }
    }//end of onActivity method

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
}// end of entire class
