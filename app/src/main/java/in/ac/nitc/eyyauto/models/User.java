package in.ac.nitc.eyyauto.models;

public class User {
    private String UID;
    private ShareableInfo info;

    // Required according to firebase documentation https://firebase.google.com/docs/database/android/read-and-write
    public User() {
    }

    public User(String UID, ShareableInfo info) {
        this.UID = UID;
        this.info = info;
    }

    public void setInfo(ShareableInfo info) {
        this.info = info;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public ShareableInfo getInfo() {
        return info;
    }
}
