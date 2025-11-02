package ioom_assi6;

public abstract class User {
    private final String name;
    private final String email_id;
    private String password;

    public User(String nameString, String emailString, String passwordString) {
        this.name = nameString;
        this.email_id = emailString;
        this.password = passwordString;
    }

    public abstract String change_password(String newpass);

    public String getname() {
        return name;
    }

    public String get_email() {
        return email_id;
    }

    public String get_password() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
