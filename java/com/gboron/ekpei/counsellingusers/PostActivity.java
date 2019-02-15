package com.gboron.ekpei.counsellingusers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gboron.ekpei.counsellingusers.myfragments.ForumFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static android.R.attr.data;

public class PostActivity extends AppCompatActivity {
    private static final int GALLERY_PICK = 1;
    private Toolbar mtoolbar;
    private ImageView postimage;
    private EditText medittext;
    private Button mbutton;
    private ProgressDialog progressDialog;
    private  Uri imageuri;
    private Bitmap thumb_bitmap;
    private DatabaseReference mPostDatabase;
    private FirebaseUser mCurrentuser;
   public String currentuseruid;
    private FirebaseAuth firebaseAuth;
    private StorageReference mimagestorage;
    private String Current_date;
    private  DatabaseReference mdatabasereference;
    private  byte[] thumb_byte;
    private  String thumbimage_url;
    private String desc;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mtoolbar=(Toolbar) findViewById(R.id.postappbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Speak");




        //create progress dialog
        progressDialog = new ProgressDialog(this);

        postimage=(ImageView) findViewById(R.id.postimage);
        medittext=(EditText) findViewById(R.id.postedittext);
        mbutton=(Button) findViewById(R.id.postbtn);

        //initializing date
        Current_date= DateFormat.getDateTimeInstance().format(new Date());

        //initializing firebase stuff
        //initialize firebaseauth
        firebaseAuth =FirebaseAuth.getInstance();

        //to store the path of imagi strorage
        mimagestorage = FirebaseStorage.getInstance().getReference();


        //am gonna get the current user id
        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        currentuseruid = mCurrentuser.getUid();

        mdatabasereference= FirebaseDatabase.getInstance().getReference().child("Users").child(currentuseruid);


        //this points to the current user id
       // mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Posts").child(currentuseruid);

        mPostDatabase = FirebaseDatabase.getInstance().getReference().child("Postsed");


        //opening gallery
        postimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

                /*progressDialog.setTitle("Uploading image");
                progressDialog.setMessage("please wait");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                final File thumb_filepath = FileUtils.getFile(PostActivity.this, imageuri);
                //  String current_user_id = mCurrentuser.getUid();
                if (imageuri != null) {
                    //pre-checking i case for error during compression
                    try {
                        thumb_bitmap = new Compressor(PostActivity.this)
                                .setMaxWidth(200)
                                .setMaxHeight(200)
                                .setQuality(70)
                                .compressToBitmap(thumb_filepath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    // final byte[] thumb_byte = baos.toByteArray();
                    thumb_byte = baos.toByteArray();

                    StorageReference filepath=mimagestorage.child("PostImages").child(currentuseruid +".jpg");
                    final StorageReference thumbfilepath=mimagestorage.child("PostImages").child("thumbs").child(currentuseruid+"jpg");
                    filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            final String download_url=task.getResult().getDownloadUrl().toString();

                            //upload task for uploading bytes to storage
                            UploadTask uploadTask=thumbfilepath.putBytes(thumb_byte);
                              thumbimage_url=task.getResult().getDownloadUrl().toString();
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "Image uploaded", Toast.LENGTH_LONG).show();
                                    }else {
                                        String m=task.getException().getMessage().toString();
                                        Toast.makeText(PostActivity.this, m, Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                        }
                    }) ;
                }*/
            }

        });


        //uploading data to firebse

       /* mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Uploading Content");
                progressDialog.setMessage("please wait");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();

                //stroring user input from edittext in desc
                final String desc=medittext.getText().toString();


                        //creating a pushkey to enable user put
                        //multiple post in database
                        DatabaseReference current_user_push=mPostDatabase.push();
                        String push_id= current_user_push.getKey();
                        // String current_user_ref="Posts/"+currentuseruid;
                        //creatig a map to store the image and thumbimage  url to the database
                        Map update=new HashMap();
                        update.put("user_post",desc);
                        // update.put("image",download_url);
                        update.put("thumbimage",thumbimage_url);
                        update.put("User_id",currentuseruid);
                        update.put("Time_of_post",Current_date);
                        update.put("pushid",push_id);


                        //bining the push key to the userpost,thumbimae,userid and time of post
                        Map updates=new HashMap();
                        updates.put("/"+push_id,update);






                        //creating the database and addig th value of the map into it
                        mPostDatabase.updateChildren(updates).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(PostActivity.this, "Post Added", Toast.LENGTH_LONG).show();

                                }else{
                                    Toast.makeText(PostActivity.this, "Network error", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
            }
        });*/
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //stroring user input from edittext in desc
                desc=medittext.getText().toString();

                    final File thumb_filepath = FileUtils.getFile(PostActivity.this, imageuri);
                    //  String current_user_id = mCurrentuser.getUid();
                    if(imageuri!=null &&!TextUtils.isEmpty(desc)) {

                        progressDialog.setTitle("Uploading Content");
                        progressDialog.setMessage("please wait");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        //pre-checking i case for error during compression
                        try {
                            thumb_bitmap = new Compressor(PostActivity.this)
                                    .setMaxWidth(200)
                                    .setMaxHeight(200)
                                    .setQuality(70)
                                    .compressToBitmap(thumb_filepath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                       // final byte[] thumb_byte = baos.toByteArray();
                        thumb_byte = baos.toByteArray();

                   StorageReference filepath=mimagestorage.child("PostImages").child(currentuseruid +".jpg");
                    final StorageReference thumbfilepath=mimagestorage.child("PostImages").child("thumbs").child(currentuseruid+"jpg");
                    filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            final String download_url=task.getResult().getDownloadUrl().toString();

                            //upload task for uploading bytes to storage
                            UploadTask uploadTask=thumbfilepath.putBytes(thumb_byte);
                            final String thumbimage_url=task.getResult().getDownloadUrl().toString();
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    //creating a pushkey to enable user put
                                    //multiple post in database
                                    DatabaseReference current_user_push=mPostDatabase.push();
                                    String push_id= current_user_push.getKey();
                                       // String current_user_ref="Posts/"+currentuseruid;
                                    //creatig a map to store the image and thumbimage  url to the database
                                    Map update=new HashMap();
                                    update.put("user_post",desc);
                                   // update.put("image",download_url);
                                    update.put("thumbimage",thumbimage_url);
                                    update.put("User_id",currentuseruid);
                                    update.put("Time_of_post",Current_date);
                                    update.put("pushid",push_id);


                                    //bining the push key to the userpost,thumbimae,userid and time of post
                                    Map updates=new HashMap();
                                    updates.put("/"+push_id,update);






                                    //creating the database and addig th value of the map into it
                                    mPostDatabase.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){
                                                progressDialog.dismiss();
                                                medittext.setText("");
                                                Toast.makeText(PostActivity.this, "Post Added", Toast.LENGTH_LONG).show();

                                            }else{
                                                progressDialog.dismiss();
                                                String g =task.getException().getMessage().toString();
                                                Toast.makeText(PostActivity.this, g, Toast.LENGTH_LONG).show();

                                            }

                                        }
                                    });

                                }
                            });//end of uploadtask



                        }
                    });//end of filepath method


                }else {
                        if (TextUtils.isEmpty(desc) && imageuri != null) {
                            //Toast.makeText(PostActivity.this, "Description field empty", Toast.LENGTH_LONG).show();
                            // progressDialog.dismiss();

                            // desc="";

                            progressDialog.setTitle("Uploading Content");
                            progressDialog.setMessage("please wait");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();

                            try {
                                thumb_bitmap = new Compressor(PostActivity.this)
                                        .setMaxWidth(200)
                                        .setMaxHeight(200)
                                        .setQuality(70)
                                        .compressToBitmap(thumb_filepath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                            // final byte[] thumb_byte = baos.toByteArray();
                            thumb_byte = baos.toByteArray();

                            StorageReference filepath = mimagestorage.child("PostImages").child(currentuseruid + ".jpg");
                            final StorageReference thumbfilepath = mimagestorage.child("PostImages").child("thumbs").child(currentuseruid + "jpg");
                            filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    final String download_url = task.getResult().getDownloadUrl().toString();

                                    //upload task for uploading bytes to storage
                                    UploadTask uploadTask = thumbfilepath.putBytes(thumb_byte);
                                    final String thumbimage_url = task.getResult().getDownloadUrl().toString();
                                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            //creating a pushkey to enable user put
                                            //multiple post in database
                                            DatabaseReference current_user_push = mPostDatabase.push();
                                            String push_id = current_user_push.getKey();
                                            // String current_user_ref="Posts/"+currentuseruid;
                                            //creatig a map to store the image and thumbimage  url to the database
                                            Map update = new HashMap();
                                            update.put("user_post", desc);
                                            // update.put("image",download_url);
                                            update.put("thumbimage", thumbimage_url);
                                            update.put("User_id", currentuseruid);
                                            update.put("Time_of_post", Current_date);
                                            update.put("pushid", push_id);


                                            //bining the push key to the userpost,thumbimae,userid and time of post
                                            Map updates = new HashMap();
                                            updates.put("/" + push_id, update);


                                            //creating the database and addig th value of the map into it
                                            mPostDatabase.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(PostActivity.this, "Post Added", Toast.LENGTH_LONG).show();


                                                    } else {
                                                        String n = task.getException().getMessage().toString();
                                                        Toast.makeText(PostActivity.this, n, Toast.LENGTH_LONG).show();
                                                        progressDialog.dismiss();
                                                    }

                                                }
                                            });

                                        }
                                    });//end of uploadtask


                                }
                            });//end of filepath method

                        } else if (!TextUtils.isEmpty(desc) && imageuri == null) {

                            progressDialog.setTitle("Uploading Content");
                            progressDialog.setMessage("please wait");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            //creating a pushkey to enable user put
                            //multiple post in database
                            DatabaseReference current_user_push = mPostDatabase.push();
                            String push_id = current_user_push.getKey();
                            // String current_user_ref="Posts/"+currentuseruid;
                            //creatig a map to store the image and thumbimage  url to the database
                            Map update = new HashMap();
                            update.put("user_post", desc);
                            // update.put("image",download_url);
                            update.put("thumbimage", "default");
                            update.put("User_id", currentuseruid);
                            update.put("Time_of_post", Current_date);
                            update.put("pushid", push_id);


                            //bining the push key to the userpost,thumbimae,userid and time of post
                            Map updates = new HashMap();
                            updates.put("/" + push_id, update);


                            //creating the database and addig th value of the map into it
                            mPostDatabase.updateChildren(updates).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "Post Added", Toast.LENGTH_LONG).show();

                                    } else {
                                        String n = task.getException().getMessage().toString();
                                        Toast.makeText(PostActivity.this, n, Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        } else if (!TextUtils.isEmpty(desc) && imageuri == null) {
                            // progressDialog.dismiss();
                            Toast.makeText(PostActivity.this, "Text field is Empty", Toast.LENGTH_LONG).show();

                        }
                    }

                    }


        });
    }//end of oncreaate



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      /*  progressDialog.setTitle("uploadig image");
        progressDialog.setMessage("please wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();*/
          /*  if(imageuri!=null) {
                imageuri = data.getData();
                postimage.setImageURI(imageuri);
            }*/

        /*  if(imageuri!=null) {
             Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
          }else if(imageuri==null){
              imageuri = data.getData();
              postimage.setImageURI(imageuri);
          }*/
         if(data == null){
             //Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();

         }else if(data!=null){
             imageuri = data.getData();
             postimage.setImageURI(imageuri);

         }


        /*if (imageuri != null ){

            final File thumb_filepath = FileUtils.getFile(this, imageuri);
            //  String current_user_id = mCurrentuser.getUid();

            //pre-checking i case for error during compression
            try {
                thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(70)
                        .compressToBitmap(thumb_filepath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            final byte[] thumb_byte = baos.toByteArray();

            //storagereference to fiepath
            StorageReference filepath=mimagestorage.child("PostImages").child(currentuseruid +".jpg");
            final StorageReference thumbfilepath=mimagestorage.child("PostImages").child("thumbs").child(currentuseruid+"jpg");
                filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        final String download_url=task.getResult().getDownloadUrl().toString();
                        //upload task for uploading bytes to storage
                        UploadTask uploadTask=thumbfilepath.putBytes(thumb_byte);
                        final String thumbimage_url=task.getResult().getDownloadUrl().toString();
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    //creatig a map to store the image and thumbimage  url to the database
                                    HashMap<String,Object> update=new HashMap();
                                    update.put("image",download_url);
                                    update.put("thumbimage",thumbimage_url);
                                }
                            }
                        });//end of uploadtask oncompelete listener
                    }
                });//end of file path oncompletelistener
        }//end of first if staement*/

    }//end of onActivityresult

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
}//end of class



