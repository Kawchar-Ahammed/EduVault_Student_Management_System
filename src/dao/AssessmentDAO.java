package dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class AssessmentDAO {
    // Save assessment marks for a student
    public static boolean saveAssessment(String studentId, int courseId, String facultyId, String assessmentName, String assessmentType, double marks, double totalMarks, double weightage, String remarks) {
        String sql = "INSERT INTO Assessments (studentId, courseId, facultyId, assessmentName, assessmentType, marks, totalMarks, weightage, assessmentDate, remarks) VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), ?)";
        try (Connection conn = util.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, studentId);
            stmt.setInt(2, courseId);
            stmt.setString(3, facultyId);
            stmt.setString(4, assessmentName);
            stmt.setString(5, assessmentType);
            stmt.setDouble(6, marks);
            stmt.setDouble(7, totalMarks);
            stmt.setDouble(8, weightage);
            stmt.setString(9, remarks);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get assessments for a course
    public static List<Assessment> getAssessmentsByCourse(int courseId) {
        List<Assessment> assessments = new ArrayList<>();
        String sql = "SELECT * FROM Assessments WHERE courseId = ?";
        try (Connection conn = util.DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Assessment a = new Assessment(
                    rs.getInt("assessmentId"),
                    rs.getString("studentId"),
                    rs.getInt("courseId"),
                    rs.getString("facultyId"),
                    rs.getString("assessmentName"),
                    rs.getString("assessmentType"),
                    rs.getDouble("marks"),
                    rs.getDouble("totalMarks"),
                    rs.getDouble("weightage"),
                    rs.getString("remarks")
                );
                assessments.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assessments;
    }

    // Assessment model for TableView
    public static class Assessment {
        private int assessmentId;
        private String studentId;
        private int courseId;
        private String facultyId;
        private String assessmentName;
        private String assessmentType;
        private double marks;
        private double totalMarks;
        private double weightage;
        private String remarks;

        public Assessment(int assessmentId, String studentId, int courseId, String facultyId, String assessmentName, String assessmentType, double marks, double totalMarks, double weightage, String remarks) {
            this.assessmentId = assessmentId;
            this.studentId = studentId;
            this.courseId = courseId;
            this.facultyId = facultyId;
            this.assessmentName = assessmentName;
            this.assessmentType = assessmentType;
            this.marks = marks;
            this.totalMarks = totalMarks;
            this.weightage = weightage;
            this.remarks = remarks;
        }
        public int getAssessmentId() { return assessmentId; }
        public String getStudentId() { return studentId; }
        public int getCourseId() { return courseId; }
        public String getFacultyId() { return facultyId; }
        public String getAssessmentName() { return assessmentName; }
        public String getAssessmentType() { return assessmentType; }
        public double getMarks() { return marks; }
        public double getTotalMarks() { return totalMarks; }
        public double getWeightage() { return weightage; }
        public String getRemarks() { return remarks; }
    }
}
