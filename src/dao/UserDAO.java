package dao;

import model.User;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User operations
 */
public class UserDAO {

    /**
     * Get departmentId by departmentName
     */
    public Integer getDepartmentIdByName(String departmentName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT departmentId FROM Departments WHERE departmentName = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, departmentName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("departmentId");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching departmentId by name: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        return null;
    }

    /**
     * Get all users (for admin management)
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT u.userId, u.username, u.password, u.email, u.fullName, u.phoneNumber, u.departmentId, d.departmentName, u.isActive, r.roleName " +
                          "FROM Users u " +
                          "INNER JOIN UserRoles ur ON u.userId = ur.userId " +
                          "INNER JOIN Roles r ON ur.roleId = r.roleId " +
                          "LEFT JOIN Departments d ON u.departmentId = d.departmentId " +
                          "ORDER BY u.fullName";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("fullName"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setDepartmentId(rs.getInt("departmentId"));
                user.setDepartmentName(rs.getString("departmentName"));
                user.setActive(rs.getBoolean("isActive"));
                user.setRoleName(rs.getString("roleName"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        return users;
    }
    
    /**
     * Authenticate user login
     */
    public User authenticateUser(String username, String password, String role) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "SELECT u.userId, u.username, u.email, u.fullName, u.phoneNumber, " +
                          "u.departmentId, u.isActive, r.roleName " +
                          "FROM Users u " +
                          "INNER JOIN UserRoles ur ON u.userId = ur.userId " +
                          "INNER JOIN Roles r ON ur.roleId = r.roleId " +
                          "WHERE u.username = ? AND u.password = ? AND r.roleName = ? AND u.isActive = 1";
            
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In production, use hashed passwords
            pstmt.setString(3, role);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("fullName"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setDepartmentId(rs.getInt("departmentId"));
                user.setActive(rs.getBoolean("isActive"));
                user.setRoleName(rs.getString("roleName"));
                return user;
            }
            
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return null; // Authentication failed
    }
    
    /**
     * Get all users by role
     */
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "SELECT u.userId, u.username, u.email, u.fullName, u.phoneNumber, " +
                          "u.departmentId, u.isActive, r.roleName " +
                          "FROM Users u " +
                          "INNER JOIN UserRoles ur ON u.userId = ur.userId " +
                          "INNER JOIN Roles r ON ur.roleId = r.roleId " +
                          "WHERE r.roleName = ? ORDER BY u.fullName";
            
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, role);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("fullName"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setDepartmentId(rs.getInt("departmentId"));
                user.setActive(rs.getBoolean("isActive"));
                user.setRoleName(rs.getString("roleName"));
                users.add(user);
            }
            
        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return users;
    }
    
    /**
     * Add new user
     */
    public boolean addUser(User user, String password, String role) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Insert user
            String insertUserQuery = "INSERT INTO Users (username, password, email, fullName, phoneNumber, departmentId, isActive) " +
                                   "VALUES (?, ?, ?, ?, ?, ?, 1)";
            
            pstmt = conn.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, password);
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getFullName());
            pstmt.setString(5, user.getPhoneNumber());
            pstmt.setObject(6, user.getDepartmentId());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get generated user ID
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int userId = rs.getInt(1);
                    user.setUserId(userId);
                    
                    // Get role ID and assign role
                    DatabaseConnection.closeResources(rs, pstmt);
                    
                    String getRoleIdQuery = "SELECT roleId FROM Roles WHERE roleName = ?";
                    pstmt = conn.prepareStatement(getRoleIdQuery);
                    pstmt.setString(1, role);
                    rs = pstmt.executeQuery();
                    
                    if (rs.next()) {
                        int roleId = rs.getInt("roleId");
                        
                        DatabaseConnection.closeResources(rs, pstmt);
                        
                        // Assign role to user
                        String assignRoleQuery = "INSERT INTO UserRoles (userId, roleId) VALUES (?, ?)";
                        pstmt = conn.prepareStatement(assignRoleQuery);
                        pstmt.setInt(1, userId);
                        pstmt.setInt(2, roleId);
                        
                        pstmt.executeUpdate();
                        
                        conn.commit(); // Commit transaction
                        return true;
                    }
                }
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Rollback error: " + rollbackEx.getMessage());
            }
            System.err.println("Error adding user: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
            DatabaseConnection.closeResources(rs, pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Update user information
     */
    public boolean updateUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "UPDATE Users SET email = ?, fullName = ?, phoneNumber = ?, " +
                          "departmentId = ?, isActive = ? WHERE userId = ?";
            
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getFullName());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setObject(4, user.getDepartmentId());
            pstmt.setBoolean(5, user.isActive());
            pstmt.setInt(6, user.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Delete user (soft delete - set inactive)
     */
    public boolean deleteUser(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            String query = "UPDATE Users SET isActive = 0 WHERE userId = ?";
            
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
        } finally {
            DatabaseConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
}
