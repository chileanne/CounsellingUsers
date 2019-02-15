package com.gboron.ekpei.counsellingusers.Modelclass;

/**
 * Created by EKPEI on 11/23/2018.
 */

public class podcastacommentmodelclass {
    private  String comments,current_admin_userid;

    public podcastacommentmodelclass(String comments, String current_admin_userid) {
        this.comments = comments;
        this.current_admin_userid = current_admin_userid;
    }

    public String getComments() {
        return comments;
    }

    public String getCurrent_admin_userid() {
        return current_admin_userid;
    }
}
