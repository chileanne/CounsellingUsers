package com.gboron.ekpei.counsellingusers.myfragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gboron.ekpei.counsellingusers.Adapterclass.PostAdapter;
import com.gboron.ekpei.counsellingusers.Adapterclass.podcastadapter;
import com.gboron.ekpei.counsellingusers.Modelclass.podcastmodelclass;
import com.gboron.ekpei.counsellingusers.R;
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
public class PodcastFragment extends Fragment {
    private List<podcastmodelclass> mlist;
    private podcastmodelclass mpodcastmodel;
    private DatabaseReference mpodcastdatabase,mcheck;
    private RecyclerView mrecyclerview;
    private podcastadapter mpostAdapter;
    private ProgressDialog mprogressbar;
    private  LinearLayoutManager mlinear;


    public PodcastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_podcast, container, false);
        mprogressbar=new ProgressDialog(getActivity());
        mprogressbar.setTitle("Loading Content");
        mprogressbar.setMessage("please wait");
        mprogressbar.setCanceledOnTouchOutside(true);
        mprogressbar.show();


        mlist=new ArrayList<>();
        mpodcastdatabase= FirebaseDatabase.getInstance().getReference().child("Articles");
        mcheck=FirebaseDatabase.getInstance().getReference().child("Articles");

        //recyclevview set up
        mrecyclerview=(RecyclerView) v.findViewById(R.id.recypodcast);
       // mrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mlinear=new LinearLayoutManager(getActivity());
        mlinear.setReverseLayout(true);
        mlinear.setStackFromEnd(true);
        mrecyclerview.setLayoutManager(mlinear);
        mpostAdapter=new podcastadapter(getActivity(),mlist);
        mrecyclerview.setAdapter(mpostAdapter);

        mlist.clear();

    mcheck.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                Query messagequery=mpodcastdatabase.limitToLast(1000000);
                messagequery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if(dataSnapshot.exists()){
                            String admin_article= dataSnapshot.child("admin_article").getValue().toString();
                            String admin_article_title= dataSnapshot.child("admin_article_title").getValue().toString();
                            String thumbimage=dataSnapshot.child("thumbimage").getValue().toString();
                            String pushid=dataSnapshot.child("pushid").getValue().toString();
                            String admin_id=dataSnapshot.child("admin_id").getValue().toString();

                            mprogressbar.dismiss();

                            mpodcastmodel= new podcastmodelclass(admin_article, admin_article_title,thumbimage,pushid,admin_id);
                            mlist.add(mpodcastmodel);
                            mpostAdapter=new podcastadapter(getActivity(),mlist);
                            mrecyclerview.setAdapter(mpostAdapter);
                            mpostAdapter.notifyDataSetChanged();
                        }else if(!dataSnapshot.exists()) {
                            mprogressbar.dismiss();
                            Toast.makeText(getActivity(),"No Current Articles",Toast.LENGTH_SHORT).show();

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
                Toast.makeText(getActivity(),"No Articles",Toast.LENGTH_LONG).show();

            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

        return  v;
    }

}
