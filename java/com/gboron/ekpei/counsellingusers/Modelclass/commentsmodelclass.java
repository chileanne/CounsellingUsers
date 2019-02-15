package com.gboron.ekpei.counsellingusers.Modelclass;

/**
 * Created by EKPEI on 11/17/2018.
 */

public class commentsmodelclass {
    private String comments,currentuserid;

    public commentsmodelclass(String comments, String currentuserid) {
        this.comments = comments;
        this.currentuserid=currentuserid;
    }

    public String getComments() {
        return comments;
    }

    public String getCurrentuserid() {
        return currentuserid;
    }
}
