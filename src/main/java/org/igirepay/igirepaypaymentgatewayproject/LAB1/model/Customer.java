package org.igirepay.igirepaypaymentgatewayproject.LAB1.model;

public class Customer {

    private int id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String pin;
    private String role;       // "admin" or "customer"
    private boolean locked;
    private int failedAttempts;

    public Customer() {}

    public Customer(int id, String fullName, String email, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Customer(int id) { this.id = id; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }

    public int getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }

    @Override
    public String toString() {
        return "Customer{id=" + id + ", fullName='" + fullName + "', email='" + email +
                "', phoneNumber='" + phoneNumber + "', role='" + role + "'}";
    }
}
