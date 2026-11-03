package entities;

public class User {
    private int id;
    private String email;
    private String password;
    private double balance;
    private String role;

    public User (int id, String email, String password, double balance, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public String getRole() {
        return role;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
