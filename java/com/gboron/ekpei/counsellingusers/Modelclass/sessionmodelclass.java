package com.gboron.ekpei.counsellingusers.Modelclass;

/**
 * Created by EKPEI on 12/1/2018.
 */

public class sessionmodelclass {
    private  String thumbimage,username,admin_id,online;
    public sessionmodelclass(String thumbimage, String username, String admin_id, String online) {
        this.thumbimage=thumbimage;
        this.username=username;
        this.admin_id=admin_id;
        this.online=online;
    }

  /*  public sessionmodelclass( String username) {

        this.username=username;
    }*/

    public String getThumbimage() {
        return thumbimage;
    }

    public String getUsername() {
        return username;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public String getOnline() {
        return online;
    }
}
