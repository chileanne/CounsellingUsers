package com.gboron.ekpei.counsellingusers.myfragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.gboron.ekpei.counsellingusers.Adapterclass.Sessionadapter;
import com.gboron.ekpei.counsellingusers.Adapterclass.podcastadapter;
import com.gboron.ekpei.counsellingusers.Modelclass.podcastmodelclass;
import com.gboron.ekpei.counsellingusers.Modelclass.sessionmodelclass;
import com.gboron.ekpei.counsellingusers.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SessionFragment extends Fragment {
    private List<sessionmodelclass> mlist;
    private sessionmodelclass msessionmodel;
    private DatabaseReference msessiondatabase,mcheck;
    private RecyclerView mrecyclerview;
    private Sessionadapter mpostAdapter;
    private ProgressDialog mprogressbar;


    public SessionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_session, container, false);
      mprogressbar=new ProgressDialog(getActivity());
        mprogressbar.setTitle("Loading Content");
        mprogressbar.setCanceledOnTouchOutside(true);
        mprogressbar.show();


            mlist= new ArrayList<>();
        msessiondatabase= FirebaseDatabase.getInstance().getReference().child("Admin_Users");
        mcheck=FirebaseDatabase.getInstance().getReference().child("Admin_Users");

        //recyclevview set up
        mrecyclerview=(RecyclerView) v.findViewById(R.id.recysession);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mpostAdapter=new Sessionadapter(getActivity(),mlist);
        mrecyclerview.setAdapter(mpostAdapter);

        mcheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    msessiondatabase.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                           // mlist.clear();
                            String username=dataSnapshot.child("username").getValue().toString();
                            String thumbimage=dataSnapshot.child("thumbimage").getValue().toString();
                            String online=dataSnapshot.child("online").getValue().toString();
                            String admin_id= dataSnapshot.getKey();

                            mprogressbar.dismiss();

                            // String key= msessiondatabase.getKey();

                            msessionmodel= new sessionmodelclass(thumbimage,username,admin_id,online);
                            mlist.add(msessionmodel);
                            mpostAdapter=new Sessionadapter(getActivity(),mlist);
                            mrecyclerview.setAdapter(mpostAdapter);
                            mpostAdapter.notifyDataSetChanged();
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
                    Toast.makeText(getActivity(),"Admin Unavailable",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return v;
    }

}
