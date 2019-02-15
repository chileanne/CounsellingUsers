package com.gboron.ekpei.counsellingusers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gboron.ekpei.counsellingusers.Adapterclass.chatmessagesadapter;
import com.gboron.ekpei.counsellingusers.Modelclass.chat_messages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static com.gboron.ekpei.counsellingusers.R.id.postimage;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView mrecyclerview;
    private List<chat_messages> postlist;
    private chat_messages mchatmodelclass;
    private chatmessagesadapter mpostAdapter;
    private DatabaseReference mfirebasedatase,mnotification;
    private FirebaseUser mCurrentuser;
    private FirebaseAuth firebaseAuth;
    private String currentuseruid;
    private ImageButton msendbutton,mimagebtn;
    private EditText medittext;
    private String otheruser,otherusernme;
    private Toolbar mtoolbar;
    private   String Current_user_ref;
    private DatabaseReference mdatabasereference,mcheck;
    private static final int GALLERY_PICK = 1;
    private StorageReference mimagestorage;
   private  Bitmap thumb_bitmap;
   // private ProgressDialog mprogressbar;
    private ProgressBar mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        mtoolbar =(Toolbar) findViewById(R.id.chatappbared);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("chat");


        firebaseAuth =FirebaseAuth.getInstance();
        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        currentuseruid = mCurrentuser.getUid();

        mdatabasereference= FirebaseDatabase.getInstance().getReference().child("Users").child(currentuseruid);
        //to store the path of imagi strorage
        mimagestorage = FirebaseStorage.getInstance().getReference();

        //getting string extra
        otheruser=getIntent().getStringExtra("admin_id");
        otherusernme=getIntent().getStringExtra("name");

      //  Typeface face= Typeface.createFromAsset(getApplicationContext().getAssets(), "font/Roboto-MediumItalic.ttf");
     // otheruser.setTypeface();

        //initializig edittext
        mrecyclerview=(RecyclerView) findViewById(R.id.chatrecy);
        medittext=(EditText) findViewById(R.id.message);
        msendbutton=(ImageButton) findViewById(R.id.snding);
        mimagebtn=(ImageButton) findViewById(R.id.imgbtn);
        mp=(ProgressBar) findViewById(R.id.mps);

        //disable edittext ,image and send btn
        medittext.setEnabled(false);
        msendbutton.setEnabled(false);
        mimagebtn.setEnabled(false);

        //hide recyclerview
        mrecyclerview.setVisibility(View.INVISIBLE);
        mp.setVisibility(View.VISIBLE);

        //initializing arrylist
        postlist=new ArrayList<>();

        //recyclevview set up

        mrecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mpostAdapter=new chatmessagesadapter(getApplicationContext(),postlist);
        mrecyclerview.setAdapter(mpostAdapter);






        Current_user_ref=  otheruser  +"/" +currentuseruid;
        mnotification=FirebaseDatabase.getInstance().getReference().child("chat_notofication").child(otheruser);
        mfirebasedatase= FirebaseDatabase.getInstance().getReference().child("messages").child(Current_user_ref);
        mcheck=FirebaseDatabase.getInstance().getReference().child("messages").child(Current_user_ref);

        mcheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    postlist.clear();
                    mfirebasedatase.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String chats=dataSnapshot.child("messages").getValue().toString();
                            String from= dataSnapshot.child("from").getValue().toString();
                            String thumbimage=dataSnapshot.child("messagetype").getValue().toString();
                            String admin=otherusernme;

                            //remove progressbar and enable btns


                            mchatmodelclass =new chat_messages(chats,from,thumbimage,admin);
                            postlist.add(mchatmodelclass);
                            mpostAdapter=new chatmessagesadapter(getApplicationContext(),postlist);
                            mrecyclerview.setAdapter(mpostAdapter);
                            mpostAdapter.notifyDataSetChanged();
                            mrecyclerview.scrollToPosition(postlist.size()-1);

                            mp.setVisibility(View.GONE);
                            mrecyclerview.setVisibility(View.VISIBLE);
                            medittext.setEnabled(true);
                            msendbutton.setEnabled(true);
                            mimagebtn.setEnabled(true);
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
                }else if(!dataSnapshot.exists()){
                    mp.setVisibility(View.GONE);
                    mrecyclerview.setVisibility(View.VISIBLE);
                    medittext.setEnabled(true);
                    msendbutton.setEnabled(true);
                    mimagebtn.setEnabled(true);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



            //sending image message
        mimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });


        //sending message
        msendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadmessage();

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {



            Uri imageuri = data.getData();



            if (imageuri != null && firebaseAuth.getCurrentUser() !=null) {


                final File thumb_filepath = FileUtils.getFile(this, imageuri);
                //  String current_user_id = mCurrentuser.getUid();

                //pre-checking i case for error during compression

                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(20)
                            .compressToBitmap(thumb_filepath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                final byte[] thumb_byte = baos.toByteArray();



                DatabaseReference user_message_push=mfirebasedatase.push();
                final String push_id=user_message_push.getKey();



                StorageReference filepath=mimagestorage.child("chatmessgeimage").child(currentuseruid +".jpg");
                final StorageReference thumbfilepath=mimagestorage.child("chatmessgeimage").child("thumbs").child(currentuseruid+"jpg");
                filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            final String download_url= task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask=thumbfilepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    String thumbimage_url=task.getResult().getDownloadUrl().toString();
                                    if(task.isSuccessful()){
                                        Map a= new HashMap();
                                        a.put("messages", thumbimage_url);
                                        a.put("from", currentuseruid);
                                        a.put("seen", false);
                                        a.put("images", download_url);
                                        a.put("messagetype", "images");
                                        a.put("time" , ServerValue.TIMESTAMP);

                                        Map b =new HashMap();
                                        b.put("/" +push_id,a);


                                        mfirebasedatase.updateChildren(b).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
                                                    medittext.setText("");
                                                }
                                            }
                                        });
                                    }
                                }
                            });


                        }
                    }
                });



            }

        }
    }//end of onActivity method

    private void loadmessage() {
        String message=medittext.getText().toString();
        if(!TextUtils.isEmpty(message)){
            //  String Current_user_ref= currentuseruid +"/" +otheruser;
            // String other_user_ref= otheruser +"/" +currentuseruid;

            DatabaseReference user_message_push=mfirebasedatase.push();
            String push_id=user_message_push.getKey();


            //setting notification for chat database
            final Map d= new HashMap();
            d.put("messages", message);
            d.put("from", currentuseruid);
            d.put("to", otheruser);


            Map a= new HashMap();
            a.put("messages", message);
            a.put("from", currentuseruid);
            a.put("seen", false);
          //  a.put("images", "default");
            a.put("messagetype", "text");
            a.put("time" , ServerValue.TIMESTAMP);

            Map b =new HashMap();
            b.put("/" +push_id,a);


            mfirebasedatase.updateChildren(b).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()) {
                        mnotification.updateChildren(d).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){
                                    medittext.setText("");
                                }
                            }
                        });

                    }
                }
            });
        }
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

