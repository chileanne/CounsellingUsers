package com.gboron.ekpei.counsellingusers.Adapterclass;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gboron.ekpei.counsellingusers.ChatActivity;
import com.gboron.ekpei.counsellingusers.Modelclass.sessionmodelclass;
import com.gboron.ekpei.counsellingusers.R;
import com.gboron.ekpei.counsellingusers.ReadmoreActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by EKPEI on 12/1/2018.
 */

public class Sessionadapter extends RecyclerView.Adapter<Sessionadapter.MyHolder> {
    private List<sessionmodelclass> mlist;
    private Context mcontext;
    private DatabaseReference mrequestsession;
    private DatabaseReference mnotificationrequest;
    private FirebaseAuth mfirebaseauth;
    private String request_type=null;
    private String requesttype;
    private String current_state;
    private String approved;
    public boolean state=false;

    public Sessionadapter(FragmentActivity activity, List<sessionmodelclass> mlist) {
        this.mcontext=activity;
        this.mlist=mlist;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.session_singlelayout,parent,false);
        return new  MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        current_state="not_sent";

        mfirebaseauth=FirebaseAuth.getInstance();
        FirebaseUser currentuser=mfirebaseauth.getCurrentUser();
        final String currentuserid=currentuser.getUid();

        sessionmodelclass m=mlist.get(position);
        final String adminname=m.getUsername();
        String thumbimage=m.getThumbimage();
        String online=m.getOnline();
        final String id=m.getAdmin_id();

        //seetting to views
        holder.madminname.setText(adminname);
        Glide.with(mcontext).load(thumbimage).placeholder(R.mipmap.person).into(holder.mimage);


            //check to see if the admin is online
        if(online.equals("true")){
            holder.monline.setText("online");

        }

        if(online.equals("false")){
            holder.monline.setText("Not online");
        }



   holder.mrequet.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           if(current_state.equals("not_sent")){
               mrequestsession= FirebaseDatabase.getInstance().getReference().child("Request_Session").child(currentuserid);
               Map update=new HashMap();
               update.put("currentuserid", currentuserid);
               update.put("request_type", "sent");
               //  update.put("currentuserid", currentuserid);
               mrequestsession.setValue(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           current_state = "sent";
                       //setting up notificatio request
                       mnotificationrequest = FirebaseDatabase.getInstance().getReference().child("Request_Notification").child(id);
                       HashMap<String, String> usermap = new HashMap<>();
                       usermap.put("from", currentuserid);
                       usermap.put("request_type", "sent");
                       mnotificationrequest.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {
                                   Toast.makeText(mcontext, "Request sent", Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
                   }

                   }
               });
           }

       }
   });

        mrequestsession= FirebaseDatabase.getInstance().getReference().child("Request_Session").child(currentuserid);
        mrequestsession.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                   // state=true;
                    requesttype=dataSnapshot.child("request_type").getValue().toString();
                    request_type=requesttype;
                    if(request_type.equals("sent")) {
                        holder.mrequet.setText("Request Sent");
                        current_state="sent";
                        //state=true;
                    }else if(request_type.equals("approved")) {
                        holder.mrequet.setText("Request Approved");
                        current_state="sent";

                    }
                }else if(!dataSnapshot.exists()){
                    holder.mrequet.setText("Request");
                  //  Toast.makeText(mcontext,"no resut on database",Toast.LENGTH_SHORT).show();
                    current_state="not_sent";
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.mcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(request_type == null){
                    Toast.makeText(mcontext,"Make a Request",Toast.LENGTH_SHORT).show();
                }else if(request_type.equals("approved")){
                    Intent podcast= new Intent(mcontext,ChatActivity.class);
                    podcast.putExtra("admin_id",id);
                    podcast.putExtra("name",adminname);

                    mcontext.startActivity(podcast);

                }else{
                    Toast.makeText(mcontext,"Request not approved",Toast.LENGTH_SHORT).show();
                }

            }
        });




    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        private Button mrequet;
        private CircleImageView mimage;
        private TextView madminname,monline;
        private CardView mcard;
        public MyHolder(View v) {
            super(v);
            mrequet=v.findViewById(R.id.request);
            mimage= v.findViewById(R.id.adminimage);
           madminname= v.findViewById(R.id.adminname);
            monline= v.findViewById(R.id.online);
            mcard=v.findViewById(R.id.card);
        }
    }
}
