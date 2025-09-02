package model;

/**
 * Course model class representing the Courses table
 */
public class Course {
    private int courseId;
    private String courseCode;
    private String courseName;
    private String description;
    private int credits;
    private int creditHours;
    private int semester;        // int in database
    private int batch;           // int in database
    private String session;
    private int programId;       // programId instead of departmentId
    private String facultyId;    // FIXED: Changed from int to String (NVARCHAR(20) in DB)
    private int maxStudents;
    private int enrolledStudents;
    private boolean isActive;
    private String status;
    private String classroom;
    private String syllabus;
    private double attendanceMarks;
    private double ctMarks;
    private double finalMarks;
    private int totalClasses;
    private double attendanceWeight;
    
    // Additional fields from joined tables
    private String facultyName;
    private String programName;
    private String departmentName;
    
    // Default constructor
    public Course() {}
    
    // Parameterized constructor - FIXED
    public Course(int courseId, String courseCode, String courseName, String description,
                 int credits, int creditHours, int semester, int batch, String session,
                 int programId, String facultyId) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
        this.creditHours = creditHours;
        this.semester = semester;
        this.batch = batch;
        this.session = session;
        this.programId = programId;
        this.facultyId = facultyId; // FIXED: Now String type
    }
    
    // Getters and Setters
    public int getCourseId() {
        return courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    public int getCreditHours() {
        return creditHours;
    }
    
    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }
    
    public int getSemester() {
        return semester;
    }
    
    public void setSemester(int semester) {
        this.semester = semester;
    }
    
    public int getBatch() {
        return batch;
    }
    
    public void setBatch(int batch) {
        this.batch = batch;
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
    
    // FIXED: facultyId getter and setter
    public String getFacultyId() {
        return facultyId;
    }
    
    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }
    
    public int getMaxStudents() {
        return maxStudents;
    }
    
    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }
    
    public int getEnrolledStudents() {
        return enrolledStudents;
    }
    
    public void setEnrolledStudents(int enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getClassroom() {
        return classroom;
    }
    
    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
    
    public String getSyllabus() {
        return syllabus;
    }
    
    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }
    
    public double getAttendanceMarks() {
        return attendanceMarks;
    }
    
    public void setAttendanceMarks(double attendanceMarks) {
        this.attendanceMarks = attendanceMarks;
    }
    
    public double getCtMarks() {
        return ctMarks;
    }
    
    public void setCtMarks(double ctMarks) {
        this.ctMarks = ctMarks;
    }
    
    public double getFinalMarks() {
        return finalMarks;
    }
    
    public void setFinalMarks(double finalMarks) {
        this.finalMarks = finalMarks;
    }
    
    public int getTotalClasses() {
        return totalClasses;
    }
    
    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }
    
    public double getAttendanceWeight() {
        return attendanceWeight;
    }
    
    public void setAttendanceWeight(double attendanceWeight) {
        this.attendanceWeight = attendanceWeight;
    }
    
    public String getFacultyName() {
        return facultyName;
    }
    
    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
    
    public String getProgramName() {
        return programName;
    }
    
    public void setProgramName(String programName) {
        this.programName = programName;
    }
    
    public String getDepartmentName() {
        return departmentName != null ? departmentName : programName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", credits=" + credits +
                ", semester=" + semester +
                ", batch=" + batch +
                ", session='" + session + '\'' +
                ", programId=" + programId +
                ", facultyId='" + facultyId + '\'' +
                ", facultyName='" + facultyName + '\'' +
                '}';
    }
}