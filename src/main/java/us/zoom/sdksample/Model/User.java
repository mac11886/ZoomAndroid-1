package us.zoom.sdksample.Model;

import java.io.Serializable;

public class User {
    private boolean isChecked = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id ;
    private String name;
    private String password;
    private String token_line;
    private String email;
    public User(String name, String email, String password, String token_line,int id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.token_line = token_line;
        this.id = id;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken_line() {
        return token_line;
    }

    public void setToken_line(String token_line) {
        this.token_line = token_line;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


}
