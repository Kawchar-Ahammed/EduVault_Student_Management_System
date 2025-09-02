package model;

/**
 * User model representing system users (Admin, Faculty, Student)
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Integer departmentId;
    private boolean isActive;
    private String roleName;
    private String departmentName; // For department name display
    
    // Constructors
    public User() {}
    
    public User(int userId, String username, String email, String fullName, String roleName) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.roleName = roleName;
        this.isActive = true;
    }
    
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    
    @Override
    public String toString() {
        return fullName + " (" + username + ")";
    }
}
