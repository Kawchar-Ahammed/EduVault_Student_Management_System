package dao;

import model.Course;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple CourseDAO that matches the actual database structure
 */
public class SimpleCourseDAO {
    // Overloaded: Get course by name and facultyId (int userId or String courseCode)
    public Course getCourseByNameAndFaculty(String courseName, int facultyId) {
        // Try to match by facultyId as int (new data)
        Course course = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Courses WHERE courseName = ? AND (facultyId = ? OR facultyId = ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, courseName);
            pstmt.setString(2, String.valueOf(facultyId)); // int as string
            pstmt.setString(3, getLegacyFacultyCode(facultyId)); // legacy code
            rs = pstmt.executeQuery();
            if (rs.next()) {
                course = new Course();
                course.setCourseId(rs.getInt("courseId"));
                course.setCourseName(rs.getString("courseName"));
                course.setCourseCode(rs.getString("courseCode"));
                course.setCredits(rs.getInt("credits"));
                course.setDescription(rs.getString("description"));
                course.setSemester(rs.getInt("semester"));
                course.setBatch(rs.getInt("batch"));
                course.setSession(rs.getString("session"));
                course.setProgramId(rs.getInt("programId"));
                course.setFacultyId(rs.getString("facultyId"));
                course.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching course by name and faculty: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        return course;
    }

    // Legacy support: get by courseName and facultyId as String (courseCode)
    public Course getCourseByNameAndFaculty(String courseName, String facultyId) {
        Course course = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Courses WHERE courseName = ? AND facultyId = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, courseName);
            pstmt.setString(2, facultyId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                course = new Course();
                course.setCourseId(rs.getInt("courseId"));
                course.setCourseName(rs.getString("courseName"));
                course.setCourseCode(rs.getString("courseCode"));
                course.setCredits(rs.getInt("credits"));
                course.setDescription(rs.getString("description"));
                course.setSemester(rs.getInt("semester"));
                course.setBatch(rs.getInt("batch"));
                course.setSession(rs.getString("session"));
                course.setProgramId(rs.getInt("programId"));
                course.setFacultyId(rs.getString("facultyId"));
                course.setStatus(rs.getString("status"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching course by name and faculty (legacy): " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        return course;
    }

    // Helper: Map facultyId (int) to legacy code if needed (stub, update as needed)
    private String getLegacyFacultyCode(int facultyId) {
        // TODO: Map facultyId to legacy code, e.g., from a lookup table or hardcoded mapping
        // For now, return "" to avoid null
        return "";
    }

    public boolean addCourse(model.Course course) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Courses (courseId, courseName, courseCode, credits, description, semester, batch, session, programId, facultyId, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getCourseCode());
            pstmt.setInt(4, course.getCredits());
            pstmt.setString(5, course.getDescription());
            pstmt.setInt(6, course.getSemester());
            pstmt.setInt(7, course.getBatch());
            pstmt.setString(8, course.getSession());
            pstmt.setInt(9, course.getProgramId());
            pstmt.setString(10, course.getFacultyId()); // FIXED: String type
            pstmt.setString(11, course.getStatus());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(null, pstmt, conn);
        }
    }

    public boolean updateCourse(model.Course course) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE Courses SET courseName=?, courseCode=?, credits=?, description=?, semester=?, batch=?, session=?, programId=?, facultyId=?, status=? WHERE courseId=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getCourseCode());
            pstmt.setInt(3, course.getCredits());
            pstmt.setString(4, course.getDescription());
            pstmt.setInt(5, course.getSemester());
            pstmt.setInt(6, course.getBatch());
            pstmt.setString(7, course.getSession());
            pstmt.setInt(8, course.getProgramId());
            pstmt.setString(9, course.getFacultyId()); // FIXED: String type
            pstmt.setString(10, course.getStatus());
            pstmt.setInt(11, course.getCourseId());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(null, pstmt, conn);
        }
    }

    public boolean deleteCourse(int courseId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM Courses WHERE courseId=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(null, pstmt, conn);
        }
    }
    
    public List<Course> getAllCourses() {
        List<Course> courseList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT c.courseId, c.courseName, c.courseCode, c.credits, " +
                    "c.description, c.semester, c.batch, c.session, c.programId, " +
                    "c.facultyId, c.status, p.programName, " +
                    "u.fullName as facultyName, d.departmentName " +
                    "FROM Courses c " +
                    "LEFT JOIN Programs p ON c.programId = p.programId " +
                    "LEFT JOIN Faculty f ON c.facultyId = f.facultyId " +
                    "LEFT JOIN Users u ON f.userId = u.userId " +
                    "LEFT JOIN Departments d ON f.departmentId = d.departmentId " +
                    "ORDER BY c.semester, c.batch, c.courseName";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("courseId"));
                course.setCourseName(rs.getString("courseName"));
                course.setCourseCode(rs.getString("courseCode"));
                course.setCredits(rs.getInt("credits"));
                course.setDescription(rs.getString("description"));
                course.setSemester(rs.getInt("semester"));
                course.setBatch(rs.getInt("batch"));
                course.setSession(rs.getString("session"));
                course.setProgramId(rs.getInt("programId"));
                course.setFacultyId(rs.getString("facultyId"));
                course.setStatus(rs.getString("status"));
                course.setProgramName(rs.getString("programName"));
                course.setFacultyName(rs.getString("facultyName"));
                course.setDepartmentName(rs.getString("departmentName"));
                courseList.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching courses: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        return courseList;
    }
    
    public List<Course> getCoursesByBatch(String batch) {
        List<Course> courseList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "SELECT c.courseId, c.courseName, c.courseCode, c.credits, " +
                          "c.description, c.semester, c.batch, c.session, c.programId, " +
                          "c.facultyId, c.status, p.programName, " +
                          "u.fullName as facultyName, d.departmentName " +
                          "FROM Courses c " +
                          "LEFT JOIN Programs p ON c.programId = p.programId " +
                          "LEFT JOIN Faculty f ON c.facultyId = f.facultyId " +
                          "LEFT JOIN Users u ON f.userId = u.userId " +
                          "LEFT JOIN Departments d ON f.departmentId = d.departmentId " +
                          "WHERE c.batch = ? " +
                          "ORDER BY c.semester, c.courseName";
            
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(batch));
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("courseId"));
                course.setCourseName(rs.getString("courseName"));
                course.setCourseCode(rs.getString("courseCode"));
                course.setCredits(rs.getInt("credits"));
                course.setDescription(rs.getString("description"));
                course.setSemester(rs.getInt("semester"));
                course.setBatch(rs.getInt("batch"));
                course.setSession(rs.getString("session"));
                course.setProgramId(rs.getInt("programId"));
                course.setFacultyId(rs.getString("facultyId")); // FIXED: String type
                course.setStatus(rs.getString("status"));
                course.setProgramName(rs.getString("programName"));
                course.setFacultyName(rs.getString("facultyName"));
                course.setDepartmentName(rs.getString("departmentName"));
                courseList.add(course);
            }
            
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error fetching courses by batch: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return courseList;
    }
    
    public List<Course> getCoursesBySemester(String semester) {
        List<Course> courseList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "SELECT c.courseId, c.courseName, c.courseCode, c.credits, " +
                          "c.description, c.semester, c.batch, c.session, c.programId, " +
                          "c.facultyId, c.status, p.programName, " +
                          "u.fullName as facultyName " +
                          "FROM Courses c " +
                          "LEFT JOIN Programs p ON c.programId = p.programId " +
                          "LEFT JOIN Faculty f ON c.facultyId = f.facultyId " +
                          "LEFT JOIN Users u ON f.userId = u.userId " +
                          "WHERE c.semester = ? " +
                          "ORDER BY c.batch, c.courseName";
            
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(semester));
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("courseId"));
                course.setCourseName(rs.getString("courseName"));
                course.setCourseCode(rs.getString("courseCode"));
                course.setCredits(rs.getInt("credits"));
                course.setDescription(rs.getString("description"));
                course.setSemester(rs.getInt("semester"));
                course.setBatch(rs.getInt("batch"));
                course.setSession(rs.getString("session"));
                course.setProgramId(rs.getInt("programId"));
                course.setFacultyId(rs.getString("facultyId")); // FIXED: String type
                course.setStatus(rs.getString("status"));
                course.setProgramName(rs.getString("programName"));
                course.setFacultyName(rs.getString("facultyName"));
                courseList.add(course);
            }
            
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error fetching courses by semester: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return courseList;
    }
    
    public List<Course> getCoursesByBatchAndSemester(String batch, String semester) {
        List<Course> courseList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "SELECT c.courseId, c.courseName, c.courseCode, c.credits, " +
                          "c.description, c.semester, c.batch, c.session, c.programId, " +
                          "c.facultyId, c.status, p.programName, " +
                          "u.fullName as facultyName " +
                          "FROM Courses c " +
                          "LEFT JOIN Programs p ON c.programId = p.programId " +
                          "LEFT JOIN Faculty f ON c.facultyId = f.facultyId " +
                          "LEFT JOIN Users u ON f.userId = u.userId " +
                          "WHERE c.batch = ? AND c.semester = ? " +
                          "ORDER BY c.courseName";
            
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(batch));
            pstmt.setInt(2, Integer.parseInt(semester));
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("courseId"));
                course.setCourseName(rs.getString("courseName"));
                course.setCourseCode(rs.getString("courseCode"));
                course.setCredits(rs.getInt("credits"));
                course.setDescription(rs.getString("description"));
                course.setSemester(rs.getInt("semester"));
                course.setBatch(rs.getInt("batch"));
                course.setSession(rs.getString("session"));
                course.setProgramId(rs.getInt("programId"));
                course.setFacultyId(rs.getString("facultyId")); // FIXED: String type
                course.setStatus(rs.getString("status"));
                course.setProgramName(rs.getString("programName"));
                course.setFacultyName(rs.getString("facultyName"));
                courseList.add(course);
            }
            
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error fetching courses by batch and semester: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return courseList;
    }
}