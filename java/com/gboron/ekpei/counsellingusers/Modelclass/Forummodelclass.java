package com.gboron.ekpei.counsellingusers.Modelclass;

/**
 * Created by EKPEI on 11/5/2018.
 */

public class Forummodelclass {
    private String  post_text, user_id,thumbimage,date,pushid;


    public Forummodelclass(String  post_text, String user_id, String thumbimage, String date, String pushid) {
        this.post_text = post_text;
        this.user_id = user_id;
        this.thumbimage = thumbimage;
        this.date = date;
        this.pushid=pushid;
    }

   public String getPost_text() {
        return post_text;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getThumbimage() {
        return thumbimage;
    }

    public String getDate() {
        return date;
    }

    public String getPushid() {
        return pushid;
    }
}
