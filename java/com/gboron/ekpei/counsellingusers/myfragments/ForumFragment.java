package com.gboron.ekpei.counsellingusers.myfragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.gboron.ekpei.counsellingusers.Adapterclass.PostAdapter;
import com.gboron.ekpei.counsellingusers.MainActivity;
import com.gboron.ekpei.counsellingusers.Modelclass.Forummodelclass;
import com.gboron.ekpei.counsellingusers.PostActivity;
import com.gboron.ekpei.counsellingusers.R;
import com.gboron.ekpei.counsellingusers.SignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView mrecyclerview;
        private List<Forummodelclass> postlist;
    private Forummodelclass mforummodelclass;
    private PostAdapter mpostAdapter;
    private DatabaseReference mfirebasedatase,mcheck;
    private FirebaseUser mCurrentuser;
    private FirebaseAuth firebaseAuth;
    private String currentuseruid;
    private ProgressDialog mprogressbar;
    private  LinearLayoutManager mlinear;


    public ForumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_forum, container, false);



     mprogressbar=new ProgressDialog(getActivity());
        mprogressbar.setTitle("Loading Content....");
        mprogressbar.setMessage("please wait");
       mprogressbar.setCanceledOnTouchOutside(true);
        mprogressbar.show();


        //initializig firebase

        firebaseAuth =FirebaseAuth.getInstance();
        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        currentuseruid = mCurrentuser.getUid();


       // mfirebasedatase=FirebaseDatabase.getInstance().getReference().child("Posts").child(currentuseruid);
        mfirebasedatase=FirebaseDatabase.getInstance().getReference().child("Postsed");
        mcheck=FirebaseDatabase.getInstance().getReference().child("Postsed");
       // mforummodelclass=new Forummodelclass();

        //initializing arrylist
        postlist=new ArrayList<>();

        //recyclevview set up
        mrecyclerview=(RecyclerView) v.findViewById(R.id.forumrecycler);
      //  mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mlinear=new LinearLayoutManager(getActivity().getApplicationContext());
        mlinear.setReverseLayout(true);
        mlinear.setStackFromEnd(true);
        mrecyclerview.setLayoutManager(mlinear);
        mpostAdapter=new PostAdapter(getActivity().getApplicationContext(),postlist);
        mrecyclerview.setAdapter(mpostAdapter);

        postlist.clear();

        mcheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Query messagequery=mfirebasedatase.limitToLast(1000000);
                    messagequery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            // postlist.clear();

                            String post_text = dataSnapshot.child("user_post").getValue().toString();
                            String user_id = dataSnapshot.child("User_id").getValue().toString();
                            String thumbimage = dataSnapshot.child("thumbimage").getValue().toString();
                            String date = dataSnapshot.child("Time_of_post").getValue().toString();
                            String pushid = dataSnapshot.child("pushid").getValue().toString();


                            mprogressbar.dismiss();

                            if (post_text != null && user_id != null && thumbimage != null && date != null && pushid != null){

                                mforummodelclass = new Forummodelclass(post_text, user_id, thumbimage, date, pushid);
                            postlist.add(mforummodelclass);
                            mpostAdapter = new PostAdapter(getActivity(), postlist);
                            mrecyclerview.setAdapter(mpostAdapter);
                            mpostAdapter.notifyDataSetChanged();
                        }


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
                    mprogressbar.dismiss();
                    Toast.makeText(getActivity(),"No Post",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return v;
    }


}
