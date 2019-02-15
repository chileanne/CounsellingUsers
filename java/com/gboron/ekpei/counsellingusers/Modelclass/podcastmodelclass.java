package com.gboron.ekpei.counsellingusers.Modelclass;

/**
 * Created by EKPEI on 11/22/2018.
 */

public class podcastmodelclass {
    private String admin_article,admin_article_title,thumbimage,pushid,admin_id;

    public podcastmodelclass (String admin_article, String admin_article_title, String thumbimage, String pushid, String admin_id) {
        this.admin_article = admin_article;
        this.admin_article_title = admin_article_title;
        this.thumbimage = thumbimage;
        this.pushid=pushid;
        this.admin_id=admin_id;
    }

    public String getAdmin_article() {
        return admin_article;
    }

    public String getAdmin_article_title() {
        return admin_article_title;
    }

    public String getThumbimage() {
        return thumbimage;
    }

    public String getPushid() {
        return pushid;
    }

    public String getAdmin_id() {
        return admin_id;
    }
}
