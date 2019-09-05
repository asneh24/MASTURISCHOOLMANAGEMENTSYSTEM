package com.ikspl.masturischool;

import java.sql.Blob;

public class UserListModel {

    String stdadmisionid,stdname,stdfathername,stdmothername;

    Blob pic;

    public String getStdadmisionid() {
        return stdadmisionid;
    }

    public void setStdadmisionid(String stdadmisionid) {
        this.stdadmisionid = stdadmisionid;
    }

    public String getStdname() {
        return stdname;
    }

    public void setStdname(String stdname) {
        this.stdname = stdname;
    }

    public String getStdfathername() {
        return stdfathername;
    }

    public void setStdfathername(String stdfathername) {
        this.stdfathername = stdfathername;
    }

    public String getStdmothername() {
        return stdmothername;
    }

    public void setStdmothername(String stdmothername) {
        this.stdmothername = stdmothername;
    }

    public Blob getPic() {
        return pic;
    }

    public void setPic(Blob pic) {
        this.pic = pic;
    }
}
