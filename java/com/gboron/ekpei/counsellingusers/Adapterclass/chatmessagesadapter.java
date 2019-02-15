package com.gboron.ekpei.counsellingusers.Adapterclass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gboron.ekpei.counsellingusers.Modelclass.chat_messages;
import com.gboron.ekpei.counsellingusers.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by EKPEI on 12/13/2018.
 */

public class chatmessagesadapter  extends RecyclerView.Adapter<chatmessagesadapter.Myholder> {
    private List<chat_messages> postlist;
    private Context mcontext;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mdataref;
    private String user;


    public chatmessagesadapter(Context applicationContext, List<chat_messages> postlist) {
        this.postlist=postlist;
        this.mcontext=applicationContext;
    }

    @Override
    public chatmessagesadapter.Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_singlelayout,parent,false);
        return new  Myholder(view);
    }

    @Override
    public void onBindViewHolder(chatmessagesadapter.Myholder holder, int position) {
        holder.setIsRecyclable(false);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        String currentuseruid = mCurrentuser.getUid();
        chat_messages v =postlist.get(position);

        String input=v.getMessaged();
        String from= v.getFrom();
        String messgetype=v.getThumbimage();
        String adminname=v.getAdmin();


      /*  mdataref= FirebaseDatabase.getInstance().getReference().child("Users").child(from);
        mdataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("username").getValue().toString();

                user=name;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


       /* if(from.equals(currentuseruid)){
            if(messgetype.equals("text")){
                holder.ll.removeView(holder.mimage);
                holder.mes.setText("me");
                holder.mtext.setText(input);
            }else{
                holder.mtext.setText("Loading image....");
                holder.mtext.setTextColor(Color.RED);
                holder.mes.setText("me");
                Glide.with(mcontext).load(input).placeholder(R.drawable.images).into(holder.mimage);
                holder.ll.removeView(holder.mtext);
            }
        } else  if(!from.equals(currentuseruid)){
            if(messgetype.equals("text")){
                holder.ll.removeView(holder.mimage);
                holder.mes.setText(adminname);
                holder.mtext.setText(input);
            }else{
               holder.mtext.setText("Loading image....");
                holder.mtext.setTextColor(Color.RED);
                holder.mes.setText(adminname);
                Glide.with(mcontext).load(input).placeholder(R.drawable.images).into(holder.mimage);
                holder.ll.removeView(holder.mtext);
            }
        }*/

            if(from.equals(currentuseruid)){
                if(messgetype.equals("text")){
                  //lets make the sendercardview invisible
                    holder.mrecievercard.setVisibility(View.GONE);
                    //just b visible
                    holder.msendercad.setVisibility(View.VISIBLE);


                    //lets make senderimage and check invisible
                    holder.msenderimage.setVisibility(View.GONE);
                    holder.mcheckimage.setVisibility(View.GONE);
                    //lets displays sender card
                    holder.msendermessage.setVisibility(View.VISIBLE);
                    holder.msendermessage.setText(input);
                    holder.mchecktext.setVisibility(View.VISIBLE);
                }else if(messgetype.equals("images")){
                    //lets make the recievercardview invisible
                    holder.mrecievercard.setVisibility(View.GONE);
                    //just b visible
                    holder.msendercad.setVisibility(View.VISIBLE);

                    //lets make sendermessage and sendercheck to invisible
                    holder.msendermessage.setVisibility(View.GONE);
                    holder.mchecktext.setVisibility(View.GONE);

                    //lets display senderimage and check
                    holder.msenderimage.setVisibility(View.VISIBLE);
                    Glide.with(mcontext).load(input).placeholder(R.drawable.ava).into(holder.msenderimage);
                    holder.mcheckimage.setVisibility(View.VISIBLE);

                }
            }else  if(!from.equals(currentuseruid)){
                if(messgetype.equals("text")){
                    //lets make the sendercardview invisible
                    holder.msendercad.setVisibility(View.GONE);
                    //just b visible
                    holder.mrecievercard.setVisibility(View.VISIBLE);
                  //  holder.circleImageView.setVisibility(View.VISIBLE);

                    //lets make the reciverimage
                    holder.mrecieverimage.setVisibility(View.GONE);

                    //set the other visiible
                   // holder.mrecieverrname.setVisibility(View.GONE);
                    holder.mrecievermessage.setVisibility(View.VISIBLE);

                    //holder.mrecieverrname.setText(adminname);
                    holder.mrecievermessage.setText(input);


                }else if(messgetype.equals("images")) {
                    //lets make the sendercardview invisible
                    holder.msendercad.setVisibility(View.GONE);
                    //just b visible
                    holder.mrecievercard.setVisibility(View.VISIBLE);

                    //lets make the reciverimage
                    holder.mrecievermessage.setVisibility(View.GONE);


                    //set the other visiible
                   // holder.mrecieverrname.setVisibility(View.VISIBLE);
                    holder.mrecieverimage.setVisibility(View.VISIBLE);

                    //holder.mrecieverrname.setText(adminname);
                    Glide.with(mcontext).load(input).placeholder(R.drawable.ava).into(holder.mrecieverimage);


                }

            }









    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
      //  private ImageView circleImageView;
        private TextView msendermessage,mrecieverrname,mrecievermessage;
        private CardView msendercad,mrecievercard;
        private ImageView msenderimage,mrecieverimage,mchecktext,mcheckimage;
        private RelativeLayout ll;
        public Myholder(View v) {
            super(v);
            ll=v.findViewById(R.id.ij);
         //   circleImageView=v.findViewById(R.id.circleImageView);
            msendermessage=v.findViewById(R.id.chat_textsender);
          // mrecieverrname=v.findViewById(R.id.recievername);
            mrecievermessage=v.findViewById(R.id.chat_textreciever);
           msendercad=v.findViewById(R.id.sendercard);
            mrecievercard=v.findViewById(R.id.recievercard);
          msenderimage=v.findViewById(R.id.senderimage);
           mrecieverimage=v.findViewById(R.id.imgreciver);
           mchecktext=v.findViewById(R.id.sendercheck);
            mcheckimage=v.findViewById(R.id.sendercheckimg);


         Typeface face= Typeface.createFromAsset(mcontext.getAssets(), "font/Roboto-MediumItalic.ttf");
            mrecievermessage.setTypeface(face);

            Typeface face1= Typeface.createFromAsset(mcontext.getAssets(), "font/Roboto-Regular.ttf");
           msendermessage.setTypeface(face1);
        }
    }
}
