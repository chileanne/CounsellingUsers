package com.gboron.ekpei.counsellingusers.Modelclass;

import static android.R.attr.thumb;

/**
 * Created by EKPEI on 12/13/2018.
 */

public class chat_messages {
    private String messaged,from,thumbimage,admin;
    public chat_messages(String chats, String from, String thumbimage, String admin) {
        this.messaged=chats;
        this.from=from;
        this.thumbimage=thumbimage;
        this.admin=admin;
    }

    public String getAdmin() {
        return admin;
    }

    public String getThumbimage() {
        return thumbimage;
    }

    public String getMessaged() {
        return messaged;
    }



    public String getFrom() {
        return from;
    }
}



