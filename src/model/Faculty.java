package model;

import java.util.Date;

/**
 * Faculty model class representing the Faculty table
 */
public class Faculty {
    private String facultyId;   // nvarchar(20) in database
    private int userId;
    private int departmentId;
    private String designation;
    private String specialization;
    private Date joinDate;      // joinDate instead of joiningDate
    private String officeRoom;
    private String officeHours;
    private String researchInterests;
    private String status;
    
    // Additional fields from joined tables
    private String fullName;
    private String email;
    private String phoneNumber;
    private String departmentName;
    
    // Default constructor
    public Faculty() {}
    
    // Parameterized constructor
    public Faculty(String facultyId, int userId, int departmentId, String designation,
                  String specialization, Date joinDate, String status) {
        this.facultyId = facultyId;
        this.userId = userId;
        this.departmentId = departmentId;
        this.designation = designation;
        this.specialization = specialization;
        this.joinDate = joinDate;
        this.status = status;
    }
    
    // Getters and Setters
    public String getFacultyId() {
        return facultyId;
    }
    
    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getDepartmentId() {
        return departmentId;
    }
    
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
    
    public String getDesignation() {
        return designation;
    }
    
    public void setDesignation(String designation) {
        this.designation = designation;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public Date getJoinDate() {
        return joinDate;
    }
    
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
    
    public String getOfficeRoom() {
        return officeRoom;
    }
    
    public void setOfficeRoom(String officeRoom) {
        this.officeRoom = officeRoom;
    }
    
    public String getOfficeHours() {
        return officeHours;
    }
    
    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }
    
    public String getResearchInterests() {
        return researchInterests;
    }
    
    public void setResearchInterests(String researchInterests) {
        this.researchInterests = researchInterests;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    @Override
    public String toString() {
        return "Faculty{" +
                "facultyId='" + facultyId + '\'' +
                ", userId=" + userId +
                ", departmentId=" + departmentId +
                ", designation='" + designation + '\'' +
                ", specialization='" + specialization + '\'' +
                ", joinDate=" + joinDate +
                ", status='" + status + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
