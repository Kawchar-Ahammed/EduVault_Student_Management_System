package dao;

import model.Student;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple StudentDAO that matches the actual database structure
 */
public class SimpleStudentDAO {
    // Get students enrolled in a specific course
    public List<Student> getEnrolledStudentsByCourse(int courseId) {
        List<Student> students = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT s.studentId, s.userId, u.fullName, u.email FROM Students s " +
                         "JOIN CourseEnrollments ce ON s.studentId = ce.studentId " +
                         "JOIN Users u ON s.userId = u.userId " +
                         "WHERE ce.courseId = ? AND ce.status = 'ENROLLED'";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getString("studentId"));
                student.setUserId(rs.getInt("userId"));
                student.setFullName(rs.getString("fullName"));
                student.setEmail(rs.getString("email"));
                students.add(student);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching enrolled students: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        return students;
    }

    public boolean addStudent(model.Student student) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Students (studentId, userId, batch, semester, session, programId, cgpa, status, enrollmentDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, student.getStudentId());
            pstmt.setInt(2, student.getUserId());
            pstmt.setInt(3, student.getBatch());
            pstmt.setInt(4, student.getSemester());
            pstmt.setString(5, student.getSession());
            pstmt.setInt(6, student.getProgramId());
            pstmt.setDouble(7, student.getCgpa());
            pstmt.setString(8, student.getStatus());
            pstmt.setDate(9, student.getEnrollmentDate() != null ? new java.sql.Date(student.getEnrollmentDate().getTime()) : null);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(null, pstmt, conn);
        }
    }

    public boolean updateStudent(model.Student student) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE Students SET batch=?, semester=?, session=?, programId=?, cgpa=?, status=?, enrollmentDate=? WHERE studentId=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, student.getBatch());
            pstmt.setInt(2, student.getSemester());
            pstmt.setString(3, student.getSession());
            pstmt.setInt(4, student.getProgramId());
            pstmt.setDouble(5, student.getCgpa());
            pstmt.setString(6, student.getStatus());
            pstmt.setDate(7, student.getEnrollmentDate() != null ? new java.sql.Date(student.getEnrollmentDate().getTime()) : null);
            pstmt.setString(8, student.getStudentId());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(null, pstmt, conn);
        }
    }

    public boolean deleteStudent(String studentId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM Students WHERE studentId=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(null, pstmt, conn);
        }
    }
    
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "SELECT s.studentId, s.userId, s.batch, s.semester, " +
                          "s.session, s.programId, s.cgpa, s.status, s.enrollmentDate, " +
                          "u.fullName, u.email, u.phoneNumber " +
                          "FROM Students s " +
                          "INNER JOIN Users u ON s.userId = u.userId " +
                          "WHERE s.userId NOT IN (SELECT userId FROM Faculty)" +
                          "ORDER BY s.batch, s.semester, u.fullName";
            
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getString("studentId"));
                student.setUserId(rs.getInt("userId"));
                student.setBatch(rs.getInt("batch"));
                student.setSemester(rs.getInt("semester"));
                student.setSession(rs.getString("session"));
                student.setProgramId(rs.getInt("programId"));
                student.setCgpa(rs.getDouble("cgpa"));
                student.setStatus(rs.getString("status"));
                student.setEnrollmentDate(rs.getDate("enrollmentDate"));
                student.setFullName(rs.getString("fullName"));
                student.setEmail(rs.getString("email"));
                student.setPhoneNumber(rs.getString("phoneNumber"));
                students.add(student);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching students: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return students;
    }
    
    public List<String> getAllBatches() {
        List<String> batches = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "SELECT DISTINCT batch FROM Students ORDER BY batch";
            
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                batches.add(String.valueOf(rs.getInt("batch")));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching batches: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return batches;
    }
    
    public List<String> getAllSemesters() {
        List<String> semesters = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "SELECT DISTINCT semester FROM Students ORDER BY semester";
            
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                semesters.add(String.valueOf(rs.getInt("semester")));
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching semesters: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return semesters;
    }
    
    public List<Student> getStudentsByBatch(String batch) {
        List<Student> students = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "SELECT s.studentId, s.userId, s.batch, s.semester, " +
                          "s.session, s.programId, s.cgpa, s.status, s.enrollmentDate, " +
                          "u.fullName, u.email, u.phoneNumber " +
                          "FROM Students s " +
                          "INNER JOIN Users u ON s.userId = u.userId " +
                          "WHERE s.batch = ? AND s.userId NOT IN (SELECT userId FROM Faculty) " +
                          "ORDER BY s.semester, u.fullName";
            
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(batch));
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getString("studentId"));
                student.setUserId(rs.getInt("userId"));
                student.setBatch(rs.getInt("batch"));
                student.setSemester(rs.getInt("semester"));
                student.setSession(rs.getString("session"));
                student.setProgramId(rs.getInt("programId"));
                student.setCgpa(rs.getDouble("cgpa"));
                student.setStatus(rs.getString("status"));
                student.setEnrollmentDate(rs.getDate("enrollmentDate"));
                student.setFullName(rs.getString("fullName"));
                student.setEmail(rs.getString("email"));
                student.setPhoneNumber(rs.getString("phoneNumber"));
                students.add(student);
            }
            
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error fetching students by batch: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return students;
    }
    
    public List<Student> getStudentsBySemester(String semester) {
        List<Student> students = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "SELECT s.studentId, s.userId, s.batch, s.semester, " +
                          "s.session, s.programId, s.cgpa, s.status, s.enrollmentDate, " +
                          "u.fullName, u.email, u.phoneNumber " +
                          "FROM Students s " +
                          "INNER JOIN Users u ON s.userId = u.userId " +
                          "WHERE s.semester = ? AND s.userId NOT IN (SELECT userId FROM Faculty) " +
                          "ORDER BY s.batch, u.fullName";
            
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(semester));
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getString("studentId"));
                student.setUserId(rs.getInt("userId"));
                student.setBatch(rs.getInt("batch"));
                student.setSemester(rs.getInt("semester"));
                student.setSession(rs.getString("session"));
                student.setProgramId(rs.getInt("programId"));
                student.setCgpa(rs.getDouble("cgpa"));
                student.setStatus(rs.getString("status"));
                student.setEnrollmentDate(rs.getDate("enrollmentDate"));
                student.setFullName(rs.getString("fullName"));
                student.setEmail(rs.getString("email"));
                student.setPhoneNumber(rs.getString("phoneNumber"));
                students.add(student);
            }
            
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error fetching students by semester: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return students;
    }
    
    public List<Student> getStudentsByBatchAndSemester(String batch, String semester) {
        List<Student> students = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "SELECT s.studentId, s.userId, s.batch, s.semester, " +
                          "s.session, s.programId, s.cgpa, s.status, s.enrollmentDate, " +
                          "u.fullName, u.email, u.phoneNumber " +
                          "FROM Students s " +
                          "INNER JOIN Users u ON s.userId = u.userId " +
                          "WHERE s.batch = ? AND s.semester = ? AND s.userId NOT IN (SELECT userId FROM Faculty) " +
                          "ORDER BY u.fullName";
            
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(batch));
            pstmt.setInt(2, Integer.parseInt(semester));
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getString("studentId"));
                student.setUserId(rs.getInt("userId"));
                student.setBatch(rs.getInt("batch"));
                student.setSemester(rs.getInt("semester"));
                student.setSession(rs.getString("session"));
                student.setProgramId(rs.getInt("programId"));
                student.setCgpa(rs.getDouble("cgpa"));
                student.setStatus(rs.getString("status"));
                student.setEnrollmentDate(rs.getDate("enrollmentDate"));
                student.setFullName(rs.getString("fullName"));
                student.setEmail(rs.getString("email"));
                student.setPhoneNumber(rs.getString("phoneNumber"));
                students.add(student);
            }
            
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error fetching students by batch and semester: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return students;
    }
}
