package tk.kiroshan.androidfirebasecrud;

public class User {

    private String uid;
    private String username;
    private String email;
    private String address;
    private String about;

    public User() {
    }

    public User(String uid, String username, String email, String address, String about) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.address = address;
        this.about = about;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getAbout() {
        return about;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
