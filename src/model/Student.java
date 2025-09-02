package model;

import java.util.Date;

/**
 * Student model class representing the Students table
 */
public class Student {
    private String studentId;  // nvarchar(20) in database
    private String studentNumber; // new field for real student number
    private int userId;
    private int batch;          // int in database
    private int semester;       // int in database
    private String session;
    private int programId;      // programId instead of departmentId
    private double cgpa;
    private String status;
    private int totalCredits;
    private Date enrollmentDate;
    private String guardianName;
    private String guardianPhone;
    private String emergencyContact;
    private String bloodGroup;
    
    // Additional fields from joined tables
    private String fullName;
    private String email;
    private String phoneNumber;
    private String programName;
    
    // Default constructor
    public Student() {}
    public Student(String studentId, String studentNumber, int userId, int batch, int semester,
                  String session, int programId, double cgpa, String status) {
        this.studentId = studentId;
        this.studentNumber = studentNumber;
        this.userId = userId;
        this.batch = batch;
        this.semester = semester;
        this.session = session;
        this.programId = programId;
        this.cgpa = cgpa;
        this.status = status;
    }
    
    // Parameterized constructor
    public Student(String studentId, int userId, int batch, int semester,
                  String session, int programId, double cgpa, String status) {
        this.studentId = studentId;
        this.userId = userId;
        this.batch = batch;
        this.semester = semester;
        this.session = session;
        this.programId = programId;
        this.cgpa = cgpa;
        this.status = status;
    }
    
    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }
    
    // Alias method for table display
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getBatch() {
        return batch;
    }
    
    public void setBatch(int batch) {
        this.batch = batch;
    }
    
    public int getSemester() {
        return semester;
    }
    
    public void setSemester(int semester) {
        this.semester = semester;
    }
    
    public String getSession() {
        return session;
    }
    
    public void setSession(String session) {
        this.session = session;
    }
    
    public int getProgramId() {
        return programId;
    }
    
    public void setProgramId(int programId) {
        this.programId = programId;
    }
    
    public double getCgpa() {
        return cgpa;
    }
    
    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getTotalCredits() {
        return totalCredits;
    }
    
    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }
    
    public Date getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    public String getGuardianName() {
        return guardianName;
    }
    
    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }
    
    public String getGuardianPhone() {
        return guardianPhone;
    }
    
    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }
    
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    
    public String getBloodGroup() {
        return bloodGroup;
    }
    
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
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
    
    public String getProgramName() {
        return programName;
    }
    
    public void setProgramName(String programName) {
        this.programName = programName;
    }
    
    @Override
    public String toString() {
    return "Student{" +
        "studentId='" + studentId + '\'' +
        ", studentNumber='" + studentNumber + '\'' +
        ", userId=" + userId +
        ", batch=" + batch +
        ", semester=" + semester +
        ", session='" + session + '\'' +
        ", programId=" + programId +
        ", cgpa=" + cgpa +
        ", status='" + status + '\'' +
        ", fullName='" + fullName + '\'' +
        '}';
    }
}
