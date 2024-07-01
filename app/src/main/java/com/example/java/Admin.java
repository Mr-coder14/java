package com.example.java;

public class Admin {
    String usennameadmin,useridadmin,profileuriadmin,emailadmin,phonoadmin,collegenameadmin;

    public Admin(String usennameadmin, String useridadmin, String profileuriadmin, String emailadmin, String phonoadmin, String collegenameadmin) {
        this.usennameadmin = usennameadmin;
        this.useridadmin = useridadmin;
        this.profileuriadmin = profileuriadmin;
        this.emailadmin = emailadmin;
        this.phonoadmin = phonoadmin;
        this.collegenameadmin = collegenameadmin;
    }

    public String getUsennameadmin() {
        return usennameadmin;
    }

    public void setUsennameadmin(String usennameadmin) {
        this.usennameadmin = usennameadmin;
    }

    public String getUseridadmin() {
        return useridadmin;
    }

    public void setUseridadmin(String useridadmin) {
        this.useridadmin = useridadmin;
    }

    public String getProfileuriadmin() {
        return profileuriadmin;
    }

    public void setProfileuriadmin(String profileuriadmin) {
        this.profileuriadmin = profileuriadmin;
    }

    public String getEmailadmin() {
        return emailadmin;
    }

    public void setEmailadmin(String emailadmin) {
        this.emailadmin = emailadmin;
    }

    public String getPhonoadmin() {
        return phonoadmin;
    }

    public void setPhonoadmin(String phonoadmin) {
        this.phonoadmin = phonoadmin;
    }

    public String getCollegenameadmin() {
        return collegenameadmin;
    }

    public void setCollegenameadmin(String collegenameadmin) {
        this.collegenameadmin = collegenameadmin;
    }
}
