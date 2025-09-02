package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.DatabaseConnection;

public class AttendanceDAO {
    // Save attendance for a student
    public static boolean saveAttendance(String studentId, int courseId, String status) {
        String sql = "INSERT INTO Attendance (studentId, courseId, status, attendanceDate) VALUES (?, ?, ?, GETDATE())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.setInt(2, courseId);
            stmt.setString(3, status);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
