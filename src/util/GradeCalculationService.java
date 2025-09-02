package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Service class for automatic grade calculation
 * Implements comprehensive grade calculation logic based on:
 * - Quiz marks (10%)
 * - Midterm marks (30%) 
 * - Final marks (40%)
 * - Assignment marks (10%)
 * - Attendance marks (10%)
 */
public class GradeCalculationService {
    
    // Grade weightages based on assessment types
    private static final double QUIZ_WEIGHTAGE = 0.10;
    private static final double MIDTERM_WEIGHTAGE = 0.30;
    private static final double FINAL_WEIGHTAGE = 0.40;
    private static final double ASSIGNMENT_WEIGHTAGE = 0.10;
    private static final double ATTENDANCE_WEIGHTAGE = 0.10;
    
    // Grade boundaries
    private static final double A_PLUS_THRESHOLD = 80.0;
    private static final double A_THRESHOLD = 75.0;
    private static final double A_MINUS_THRESHOLD = 70.0;
    private static final double B_PLUS_THRESHOLD = 65.0;
    private static final double B_THRESHOLD = 60.0;
    private static final double B_MINUS_THRESHOLD = 55.0;
    private static final double C_PLUS_THRESHOLD = 50.0;
    private static final double C_THRESHOLD = 45.0;
    private static final double D_THRESHOLD = 40.0;
    
    /**
     * Calculate final grade for a student in a specific course
     */
    public static GradeResult calculateFinalGrade(String studentId, String courseId, String session) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            // Get all assessments for this student and course
            Map<String, Double> assessmentMarks = getAssessmentMarks(conn, studentId, courseId);
            
            // Get attendance percentage
            double attendancePercentage = getAttendancePercentage(conn, studentId, courseId, session);
            
            // Calculate weighted total
            double finalMarks = calculateWeightedTotal(assessmentMarks, attendancePercentage);
            
            // Determine letter grade
            String letterGrade = getLetterGrade(finalMarks);
            
            // Determine GPA
            double gpa = getGPA(finalMarks);
            
            conn.close();
            
            return new GradeResult(finalMarks, letterGrade, gpa, assessmentMarks, attendancePercentage);
            
        } catch (SQLException e) {
            System.err.println("Error calculating grade: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get assessment marks for a student in a course
     */
    private static Map<String, Double> getAssessmentMarks(Connection conn, String studentId, String courseId) throws SQLException {
        Map<String, Double> assessmentMarks = new HashMap<>();
        
        String query = "SELECT assessmentType, AVG(marks/totalMarks * 100) as averagePercentage " +
                      "FROM Assessments " +
                      "WHERE studentId = ? AND courseId = ? " +
                      "GROUP BY assessmentType";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, studentId);
        stmt.setString(2, courseId);
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String assessmentType = rs.getString("assessmentType");
            double averagePercentage = rs.getDouble("averagePercentage");
            assessmentMarks.put(assessmentType, averagePercentage);
        }
        
        rs.close();
        stmt.close();
        
        return assessmentMarks;
    }
    
    /**
     * Get attendance percentage for a student in a course
     */
    private static double getAttendancePercentage(Connection conn, String studentId, String courseId, String session) throws SQLException {
        String query = "SELECT attendancePercentage FROM AttendanceReports " +
                      "WHERE studentId = ? AND courseId = ? AND academicYear = ?";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, studentId);
        stmt.setString(2, courseId);
        stmt.setString(3, session);
        
        ResultSet rs = stmt.executeQuery();
        
        double attendancePercentage = 0.0;
        if (rs.next()) {
            attendancePercentage = rs.getDouble("attendancePercentage");
        }
        
        rs.close();
        stmt.close();
        
        return attendancePercentage;
    }
    
    /**
     * Calculate weighted total based on assessment marks and attendance
     */
    private static double calculateWeightedTotal(Map<String, Double> assessmentMarks, double attendancePercentage) {
        double total = 0.0;
        
        // Quiz marks (10%)
        if (assessmentMarks.containsKey("Quiz")) {
            total += assessmentMarks.get("Quiz") * QUIZ_WEIGHTAGE;
        }
        
        // Midterm marks (30%)
        if (assessmentMarks.containsKey("Midterm")) {
            total += assessmentMarks.get("Midterm") * MIDTERM_WEIGHTAGE;
        }
        
        // Final marks (40%)
        if (assessmentMarks.containsKey("Final")) {
            total += assessmentMarks.get("Final") * FINAL_WEIGHTAGE;
        }
        
        // Assignment marks (10%)
        if (assessmentMarks.containsKey("Assignment")) {
            total += assessmentMarks.get("Assignment") * ASSIGNMENT_WEIGHTAGE;
        }
        
        // Attendance marks (10%)
        total += attendancePercentage * ATTENDANCE_WEIGHTAGE;
        
        return total;
    }
    
    /**
     * Get letter grade based on marks
     */
    private static String getLetterGrade(double marks) {
        if (marks >= A_PLUS_THRESHOLD) return "A+";
        if (marks >= A_THRESHOLD) return "A";
        if (marks >= A_MINUS_THRESHOLD) return "A-";
        if (marks >= B_PLUS_THRESHOLD) return "B+";
        if (marks >= B_THRESHOLD) return "B";
        if (marks >= B_MINUS_THRESHOLD) return "B-";
        if (marks >= C_PLUS_THRESHOLD) return "C+";
        if (marks >= C_THRESHOLD) return "C";
        if (marks >= D_THRESHOLD) return "D";
        return "F";
    }
    
    /**
     * Get GPA based on marks
     */
    private static double getGPA(double marks) {
        if (marks >= A_PLUS_THRESHOLD) return 4.00;
        if (marks >= A_THRESHOLD) return 3.75;
        if (marks >= A_MINUS_THRESHOLD) return 3.50;
        if (marks >= B_PLUS_THRESHOLD) return 3.25;
        if (marks >= B_THRESHOLD) return 3.00;
        if (marks >= B_MINUS_THRESHOLD) return 2.75;
        if (marks >= C_PLUS_THRESHOLD) return 2.50;
        if (marks >= C_THRESHOLD) return 2.25;
        if (marks >= D_THRESHOLD) return 2.00;
        return 0.00;
    }
    
    /**
     * Calculate semester GPA for a student
     */
    public static double calculateSemesterGPA(String studentId, String session, int semester) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            // Get all courses for this student in the given semester
            String courseQuery = "SELECT DISTINCT c.courseId, c.creditHours " +
                               "FROM Courses c " +
                               "JOIN CourseEnrollments ce ON c.courseId = ce.courseId " +
                               "WHERE ce.studentId = ? AND c.session = ? AND c.semester = ?";
            
            PreparedStatement stmt = conn.prepareStatement(courseQuery);
            stmt.setString(1, studentId);
            stmt.setString(2, session);
            stmt.setInt(3, semester);
            
            ResultSet rs = stmt.executeQuery();
            
            double totalGradePoints = 0.0;
            int totalCreditHours = 0;
            
            while (rs.next()) {
                String courseId = rs.getString("courseId");
                int creditHours = rs.getInt("creditHours");
                
                GradeResult gradeResult = calculateFinalGrade(studentId, courseId, session);
                if (gradeResult != null) {
                    totalGradePoints += gradeResult.getGpa() * creditHours;
                    totalCreditHours += creditHours;
                }
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            return totalCreditHours > 0 ? totalGradePoints / totalCreditHours : 0.0;
            
        } catch (SQLException e) {
            System.err.println("Error calculating semester GPA: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }
    
    /**
     * Calculate cumulative GPA for a student
     */
    public static double calculateCumulativeGPA(String studentId) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            // Get all courses for this student across all semesters
            String courseQuery = "SELECT DISTINCT c.courseId, c.creditHours, c.session, c.semester " +
                               "FROM Courses c " +
                               "JOIN CourseEnrollments ce ON c.courseId = ce.courseId " +
                               "WHERE ce.studentId = ? " +
                               "ORDER BY c.session, c.semester";
            
            PreparedStatement stmt = conn.prepareStatement(courseQuery);
            stmt.setString(1, studentId);
            
            ResultSet rs = stmt.executeQuery();
            
            double totalGradePoints = 0.0;
            int totalCreditHours = 0;
            
            while (rs.next()) {
                String courseId = rs.getString("courseId");
                int creditHours = rs.getInt("creditHours");
                String session = rs.getString("session");
                
                GradeResult gradeResult = calculateFinalGrade(studentId, courseId, session);
                if (gradeResult != null) {
                    totalGradePoints += gradeResult.getGpa() * creditHours;
                    totalCreditHours += creditHours;
                }
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            return totalCreditHours > 0 ? totalGradePoints / totalCreditHours : 0.0;
            
        } catch (SQLException e) {
            System.err.println("Error calculating cumulative GPA: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }
    
    /**
     * Generate grade report for a student
     */
    public static String generateGradeReport(String studentId, String session, int semester) {
        StringBuilder report = new StringBuilder();
        
        report.append("GRADE REPORT\n");
        report.append("============\n\n");
        report.append("Student ID: ").append(studentId).append("\n");
        report.append("Session: ").append(session).append("\n");
        report.append("Semester: ").append(semester).append("\n");
        report.append("Generated: ").append(java.time.LocalDateTime.now().toString()).append("\n\n");
        
        try {
            Connection conn = DatabaseConnection.getConnection();
            
            // Get student name
            String studentQuery = "SELECT fullName FROM Students WHERE studentId = ?";
            PreparedStatement studentStmt = conn.prepareStatement(studentQuery);
            studentStmt.setString(1, studentId);
            ResultSet studentRs = studentStmt.executeQuery();
            
            if (studentRs.next()) {
                report.append("Student Name: ").append(studentRs.getString("fullName")).append("\n\n");
            }
            
            studentRs.close();
            studentStmt.close();
            
            // Get courses and grades
            String courseQuery = "SELECT c.courseCode, c.courseName, c.creditHours " +
                               "FROM Courses c " +
                               "JOIN CourseEnrollments ce ON c.courseId = ce.courseId " +
                               "WHERE ce.studentId = ? AND c.session = ? AND c.semester = ?";
            
            PreparedStatement courseStmt = conn.prepareStatement(courseQuery);
            courseStmt.setString(1, studentId);
            courseStmt.setString(2, session);
            courseStmt.setInt(3, semester);
            
            ResultSet courseRs = courseStmt.executeQuery();
            
            report.append("Course Code\tCourse Name\t\tCredit Hours\tGrade\tGPA\n");
            report.append("================================================================\n");
            
            double totalGradePoints = 0.0;
            int totalCreditHours = 0;
            
            while (courseRs.next()) {
                String courseCode = courseRs.getString("courseCode");
                String courseName = courseRs.getString("courseName");
                int creditHours = courseRs.getInt("creditHours");
                
                // Get course ID for grade calculation
                String courseIdQuery = "SELECT courseId FROM Courses WHERE courseCode = ? AND session = ? AND semester = ?";
                PreparedStatement courseIdStmt = conn.prepareStatement(courseIdQuery);
                courseIdStmt.setString(1, courseCode);
                courseIdStmt.setString(2, session);
                courseIdStmt.setInt(3, semester);
                
                ResultSet courseIdRs = courseIdStmt.executeQuery();
                if (courseIdRs.next()) {
                    String courseId = courseIdRs.getString("courseId");
                    
                    GradeResult gradeResult = calculateFinalGrade(studentId, courseId, session);
                    if (gradeResult != null) {
                        report.append(courseCode).append("\t");
                        report.append(courseName.length() > 15 ? courseName.substring(0, 15) + "..." : courseName).append("\t\t");
                        report.append(creditHours).append("\t\t");
                        report.append(gradeResult.getLetterGrade()).append("\t");
                        report.append(String.format("%.2f", gradeResult.getGpa())).append("\n");
                        
                        totalGradePoints += gradeResult.getGpa() * creditHours;
                        totalCreditHours += creditHours;
                    }
                }
                
                courseIdRs.close();
                courseIdStmt.close();
            }
            
            courseRs.close();
            courseStmt.close();
            
            // Calculate and display GPA
            double semesterGPA = totalCreditHours > 0 ? totalGradePoints / totalCreditHours : 0.0;
            double cumulativeGPA = calculateCumulativeGPA(studentId);
            
            report.append("\n================================================================\n");
            report.append("Total Credit Hours: ").append(totalCreditHours).append("\n");
            report.append("Semester GPA: ").append(String.format("%.2f", semesterGPA)).append("\n");
            report.append("Cumulative GPA: ").append(String.format("%.2f", cumulativeGPA)).append("\n");
            
            conn.close();
            
        } catch (SQLException e) {
            report.append("Error generating grade report: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        
        return report.toString();
    }
    
    /**
     * Inner class to hold grade calculation results
     */
    public static class GradeResult {
        private final double finalMarks;
        private final String letterGrade;
        private final double gpa;
        private final Map<String, Double> assessmentMarks;
        private final double attendancePercentage;
        
        public GradeResult(double finalMarks, String letterGrade, double gpa, 
                          Map<String, Double> assessmentMarks, double attendancePercentage) {
            this.finalMarks = finalMarks;
            this.letterGrade = letterGrade;
            this.gpa = gpa;
            this.assessmentMarks = assessmentMarks;
            this.attendancePercentage = attendancePercentage;
        }
        
        public double getFinalMarks() { return finalMarks; }
        public String getLetterGrade() { return letterGrade; }
        public double getGpa() { return gpa; }
        public Map<String, Double> getAssessmentMarks() { return assessmentMarks; }
        public double getAttendancePercentage() { return attendancePercentage; }
        
        @Override
        public String toString() {
            return String.format("Grade: %s (%.2f%%) - GPA: %.2f", letterGrade, finalMarks, gpa);
        }
    }
}
