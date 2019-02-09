package in.ac.nitc.eyyauto.models;

public class User {
    private String Name;
    private String Contact;

    // Required according to firebase documentation https://firebase.google.com/docs/database/android/read-and-write
    public User() {
    }

    public User(String name, String contact) {
        Name = name;
        Contact = contact;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getName() {
        return Name;
    }

    public String getContact() {
        return Contact;
    }
}
