package com.gboron.ekpei.counsellingusers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gboron.ekpei.counsellingusers.myfragments.ForumFragment;
import com.gboron.ekpei.counsellingusers.myfragments.PodcastFragment;
import com.gboron.ekpei.counsellingusers.myfragments.SessionFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private ForumFragment forumFragment;
    private SessionFragment sessionFragment;
    private PodcastFragment podcastFragment;
    private TextView mTextMessage;
    private FirebaseAuth  mfirebaseauth;
    private DatabaseReference mdatabasereference;
    private  String currentuserid;
    private int storage_pemission=23;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   // mTextMessage.setText(R.string.title_home);
                    mtoolbar.setTitle("Forum");
                   loadfragment(forumFragment);
                    return true;
                case R.id.navigation_dashboard:
                   // mTextMessage.setText(R.string.title_dashboard);
                    mtoolbar.setTitle("Articles");
                   loadfragment(podcastFragment);
                    return true;
                case R.id.navigation_notifications:
                  //  mTextMessage.setText(R.string.title_notifications);
                    mtoolbar.setTitle("Session");
                    loadfragment(sessionFragment);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            //setting toolbar in MainActivity
        mtoolbar =(Toolbar) findViewById(R.id.appbars);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Forum");

        if(isReadStorageAllowed()){
           // Toast.makeText(getApplicationContext(),"permission already approved",Toast.LENGTH_SHORT).show();
        }
        //if the app dosent have the permission ,request for permission
        requestStoragePermission();

        mfirebaseauth=FirebaseAuth.getInstance();
        FirebaseUser currentuser=mfirebaseauth.getCurrentUser();
         currentuserid=currentuser.getUid();

        if(mfirebaseauth==null){
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        }


        mdatabasereference= FirebaseDatabase.getInstance().getReference().child("Users").child(currentuserid);



       // mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Iniatilizing fragment
        forumFragment=new ForumFragment();
        podcastFragment=new PodcastFragment();
        sessionFragment=new SessionFragment();

        loadfragment(forumFragment);
       // mtoolbar.setTitle("Forum");
    }

    private Boolean isReadStorageAllowed(){
        //Getting the permission status
        int result= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //if permission is granted returning true
        if(result== PackageManager.PERMISSION_GRANTED)
            return  true;
        //if permission not granted return false
        return  false;

    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            //give a reason y
        }
        //ask for permission
        ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE},storage_pemission);
    }

    //this method will be called when the user clicks on allow or deny


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == storage_pemission) {
            //if permission is granted
            if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
               // Toast.makeText(getApplicationContext(),"permission approved",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //creating the main_menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

//click listener for items on main menu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId()==R.id.main_logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        }




        if (item.getItemId()==R.id.myprofile){

            startActivity(new Intent(this, ProfileActivity.class));
        }
        if(item.getItemId()==R.id.postinforum){
            startActivity(new Intent(this, PostActivity.class));
        }
        return  true;
    }

    //method to loa fragment
    private  void loadfragment(Fragment fragment){
        //load fragment
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content,fragment);
        transaction.commit();
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
