package dao;

import model.Faculty;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple FacultyDAO that matches the actual database structure
 */
public class SimpleFacultyDAO {

    public boolean addFaculty(Faculty faculty) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Faculty (facultyId, userId, departmentId, designation, specialization, joinDate, officeRoom, officeHours, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, faculty.getFacultyId());
            pstmt.setInt(2, faculty.getUserId());
            pstmt.setInt(3, faculty.getDepartmentId());
            pstmt.setString(4, faculty.getDesignation());
            pstmt.setString(5, faculty.getSpecialization());
            pstmt.setDate(6, faculty.getJoinDate() != null ? new java.sql.Date(faculty.getJoinDate().getTime()) : null);
            pstmt.setString(7, faculty.getOfficeRoom());
            pstmt.setString(8, faculty.getOfficeHours());
            pstmt.setString(9, faculty.getStatus());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding faculty: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(null, pstmt, conn);
        }
    }

    public boolean updateFaculty(Faculty faculty) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "UPDATE Faculty SET departmentId=?, designation=?, specialization=?, joinDate=?, officeRoom=?, officeHours=?, status=? WHERE facultyId=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, faculty.getDepartmentId());
            pstmt.setString(2, faculty.getDesignation());
            pstmt.setString(3, faculty.getSpecialization());
            pstmt.setDate(4, faculty.getJoinDate() != null ? new java.sql.Date(faculty.getJoinDate().getTime()) : null);
            pstmt.setString(5, faculty.getOfficeRoom());
            pstmt.setString(6, faculty.getOfficeHours());
            pstmt.setString(7, faculty.getStatus());
            pstmt.setString(8, faculty.getFacultyId());
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating faculty: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(null, pstmt, conn);
        }
    }

    public boolean deleteFaculty(String facultyId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM Faculty WHERE facultyId=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, facultyId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting faculty: " + e.getMessage());
            return false;
        } finally {
            DatabaseConnection.closeResources(null, pstmt, conn);
        }
    }
    
    public List<Faculty> getAllFaculty() {
        List<Faculty> facultyList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "SELECT f.facultyId, f.userId, f.departmentId, f.designation, " +
                          "f.specialization, f.joinDate, f.officeRoom, f.officeHours, " +
                          "f.status, u.fullName, u.email, u.phoneNumber, d.departmentName " +
                          "FROM Faculty f " +
                          "INNER JOIN Users u ON f.userId = u.userId " +
                          "LEFT JOIN Departments d ON f.departmentId = d.departmentId " +
                          "WHERE f.userId NOT IN (SELECT userId FROM Students) " +
                          "ORDER BY u.fullName";
            
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Faculty faculty = new Faculty();
                faculty.setFacultyId(rs.getString("facultyId"));
                faculty.setUserId(rs.getInt("userId"));
                faculty.setDepartmentId(rs.getInt("departmentId"));
                faculty.setDesignation(rs.getString("designation"));
                faculty.setSpecialization(rs.getString("specialization"));
                faculty.setJoinDate(rs.getDate("joinDate"));
                faculty.setOfficeRoom(rs.getString("officeRoom"));
                faculty.setOfficeHours(rs.getString("officeHours"));
                faculty.setStatus(rs.getString("status"));
                faculty.setFullName(rs.getString("fullName"));
                faculty.setEmail(rs.getString("email"));
                faculty.setPhoneNumber(rs.getString("phoneNumber"));
                faculty.setDepartmentName(rs.getString("departmentName"));
                facultyList.add(faculty);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching faculty: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return facultyList;
    }
}
