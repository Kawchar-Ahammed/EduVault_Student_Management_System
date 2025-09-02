package controllers;

import dao.AssessmentDAO;
import dao.AttendanceDAO;
import dao.SimpleCourseDAO;
import dao.SimpleStudentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Course;
import model.Student;
import model.Faculty;
import util.EmailService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Faculty Dashboard
 */
public class FacultyController implements Initializable {
    // Header Controls
    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;
    
    // Academic Session Controls
    @FXML private ComboBox<String> sessionComboBox;
    @FXML private ComboBox<String> batchComboBox;
    @FXML private ComboBox<String> semesterComboBox;
    @FXML private ComboBox<Course> courseComboBox;
    
    // Mark Entry Controls
    @FXML private ComboBox<String> assessmentTypeComboBox;
    @FXML private Button viewStudentListButton;
    @FXML private TableView<StudentMarkEntry> markEntryTable;
    @FXML private TableColumn<StudentMarkEntry, String> studentIdColumn;
    @FXML private TableColumn<StudentMarkEntry, String> studentNameColumn;
    @FXML private TableColumn<StudentMarkEntry, TextField> marksColumn;
    @FXML private TableColumn<StudentMarkEntry, CheckBox> absentColumn;
    @FXML private Button submitMarksButton;
    
    // Attendance Controls
    @FXML private TableView<StudentAttendance> attendanceTable;
    @FXML private TableColumn<StudentAttendance, String> attendanceStudentIdColumn;
    @FXML private TableColumn<StudentAttendance, String> attendanceStudentNameColumn;
    @FXML private TableColumn<StudentAttendance, ComboBox<String>> attendanceStatusColumn;
    @FXML private Button submitAttendanceButton;
    
    // Email Controls
    @FXML private Tab sendMarksTab;
    @FXML private TableView<StudentMarkSummary> markSummaryTable;
    @FXML private TableColumn<StudentMarkSummary, String> summaryStudentIdColumn;
    @FXML private TableColumn<StudentMarkSummary, String> summaryStudentNameColumn;
    @FXML private TableColumn<StudentMarkSummary, String> summaryEmailColumn;
    @FXML private TableColumn<StudentMarkSummary, Double> summaryMarksColumn;
    @FXML private TableColumn<StudentMarkSummary, CheckBox> sendEmailColumn;
    @FXML private Button sendMarksEmailButton;
    @FXML private Button selectAllButton;
    @FXML private Button deselectAllButton;
    
    // Faculty ID
    private String facultyId;
    
    // DAOs
    private SimpleCourseDAO courseDAO;
    private SimpleStudentDAO studentDAO;
    
    // Observable Lists
    private ObservableList<Course> facultyCourses;
    private ObservableList<StudentMarkEntry> studentMarkEntries;
    private ObservableList<StudentAttendance> studentAttendances;
    private ObservableList<StudentMarkSummary> studentMarkSummaries;
    private ObservableList<AttendanceReportEntry> attendanceReportEntries;
    
    // Attendance Report Controls
    @FXML private TableView<AttendanceReportEntry> attendanceReportTable;
    @FXML private TableColumn<AttendanceReportEntry, String> reportStudentIdColumn;
    @FXML private TableColumn<AttendanceReportEntry, String> reportStudentNameColumn;
    @FXML private TableColumn<AttendanceReportEntry, Integer> totalClassesColumn;
    @FXML private TableColumn<AttendanceReportEntry, Integer> presentClassesColumn;
    @FXML private TableColumn<AttendanceReportEntry, Integer> absentClassesColumn;
    @FXML private TableColumn<AttendanceReportEntry, Double> attendancePercentColumn;
    @FXML private TableColumn<AttendanceReportEntry, Double> attendanceMarksColumn;
    @FXML private Button generateAttendanceReportButton;
    @FXML private Button exportAttendanceReportButton;
    @FXML private Button sendAttendanceEmailButton;
    
    // Reports & Analytics Controls
    @FXML private Label totalStudentsLabel;
    @FXML private Label averageAttendanceLabel;
    @FXML private Label averageMarksLabel;
    @FXML private BarChart<String, Number> performanceChart;
    @FXML private ComboBox<String> reportAssessmentTypeComboBox;
    @FXML private Button generateMarksReportButton;
    @FXML private Button generateCourseReportButton;
    
    // Email Service
    private EmailService emailService;
    
    // Model classes for TableViews
    public static class StudentMarkEntry {
        private final String studentId;
        private final String studentName;
        private final TextField marksField;
        private final CheckBox absentCheckBox;
        private final String email;
        
        public StudentMarkEntry(String studentId, String studentName, String email) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.email = email;
            this.marksField = new TextField();
            this.marksField.setPrefWidth(80);
            this.absentCheckBox = new CheckBox();
            
            // When absent is checked, disable marks field and set to 0
            this.absentCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    this.marksField.setText("0");
                    this.marksField.setDisable(true);
                } else {
                    this.marksField.setText("");
                    this.marksField.setDisable(false);
                }
            });
        }
        
        public String getStudentId() { return studentId; }
        public String getStudentName() { return studentName; }
        public TextField getMarksField() { return marksField; }
        public CheckBox getAbsentCheckBox() { return absentCheckBox; }
        public String getEmail() { return email; }
        public double getMarks() {
            try {
                return Double.parseDouble(marksField.getText());
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        public boolean isAbsent() { return absentCheckBox.isSelected(); }
    }
    
    public static class StudentAttendance {
        private final String studentId;
        private final String studentName;
        private final ComboBox<String> statusComboBox;
        private final String email;
        
        public StudentAttendance(String studentId, String studentName, String email) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.email = email;
            this.statusComboBox = new ComboBox<>();
            this.statusComboBox.getItems().addAll("Present", "Absent", "Late", "Excused");
            this.statusComboBox.setValue("Present");
            this.statusComboBox.setPrefWidth(120);
        }
        
        public String getStudentId() { return studentId; }
        public String getStudentName() { return studentName; }
        public ComboBox<String> getStatusComboBox() { return statusComboBox; }
        public String getEmail() { return email; }
        public String getStatus() { return statusComboBox.getValue(); }
    }
    
    public static class StudentMarkSummary {
        private final String studentId;
        private final String studentName;
        private final String email;
        private final double marks;
        private final CheckBox sendEmailCheckBox;
        
        public StudentMarkSummary(String studentId, String studentName, String email, double marks) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.email = email;
            this.marks = marks;
            this.sendEmailCheckBox = new CheckBox();
            this.sendEmailCheckBox.setSelected(true);
        }
        
        public String getStudentId() { return studentId; }
        public String getStudentName() { return studentName; }
        public String getEmail() { return email; }
        public double getMarks() { return marks; }
        public CheckBox getSendEmailCheckBox() { return sendEmailCheckBox; }
        public boolean isSendEmail() { return sendEmailCheckBox.isSelected(); }
    }
    
    public static class AttendanceReportEntry {
        private final String studentId;
        private final String studentName;
        private final int totalClasses;
        private final int presentClasses;
        private final int absentClasses;
        private final double attendancePercentage;
        private final double attendanceMarks;
        private final String email;
        
        public AttendanceReportEntry(String studentId, String studentName, String email, 
                                    int totalClasses, int presentClasses, int absentClasses) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.email = email;
            this.totalClasses = totalClasses;
            this.presentClasses = presentClasses;
            this.absentClasses = absentClasses;
            this.attendancePercentage = totalClasses > 0 ? ((double) presentClasses / totalClasses) * 100 : 0;
            this.attendanceMarks = calculateAttendanceMarks(this.attendancePercentage);
        }
        
        private double calculateAttendanceMarks(double percentage) {
            // Assuming attendance is worth 10 marks maximum
            return Math.min(10.0, percentage / 10.0);
        }
        
        public String getStudentId() { return studentId; }
        public String getStudentName() { return studentName; }
        public String getEmail() { return email; }
        public int getTotalClasses() { return totalClasses; }
        public int getPresentClasses() { return presentClasses; }
        public int getAbsentClasses() { return absentClasses; }
        public double getAttendancePercentage() { return attendancePercentage; }
        public double getAttendanceMarks() { return attendanceMarks; }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize DAOs
        courseDAO = new SimpleCourseDAO();
        studentDAO = new SimpleStudentDAO();
        
        // Initialize Observable Lists
        facultyCourses = FXCollections.observableArrayList();
        studentMarkEntries = FXCollections.observableArrayList();
        studentAttendances = FXCollections.observableArrayList();
        studentMarkSummaries = FXCollections.observableArrayList();
        attendanceReportEntries = FXCollections.observableArrayList();
        
        // Initialize Email Service
        emailService = new EmailService();
        
        // Setup ComboBoxes
        setupComboBoxes();
        
        // Setup Tables
        setupMarkEntryTable();
        setupAttendanceTable();
        setupMarkSummaryTable();
        setupAttendanceReportTable();
        
        // Setup Event Handlers
        setupEventHandlers();
    }
    
    /**
     * Set faculty ID for the dashboard
     */
    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
        System.out.println("Faculty ID set: " + facultyId);
        
        // Load faculty courses
        loadFacultyCourses();
        
        // Set welcome label
        dao.SimpleFacultyDAO facultyDAO = new dao.SimpleFacultyDAO();
        List<Faculty> faculties = facultyDAO.getAllFaculty();
        for (Faculty faculty : faculties) {
            if (faculty.getFacultyId().equals(facultyId)) {
                welcomeLabel.setText("Welcome, " + faculty.getFullName());
                break;
            }
        }
    }
    
    /**
     * Setup ComboBoxes
     */
    private void setupComboBoxes() {
        // Setup Assessment Type ComboBox
        assessmentTypeComboBox.getItems().addAll("Quiz", "Midterm", "Final", "Assignment", "Project");
        assessmentTypeComboBox.setValue("Quiz");
        
        // Setup Session ComboBox
        sessionComboBox.getItems().addAll("2020-21", "2021-22", "2022-23", "203-24");
        sessionComboBox.setValue("2020-21");
        
        // Setup Course ComboBox
        courseComboBox.setConverter(new javafx.util.StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course != null ? course.getCourseCode() + " - " + course.getCourseName() : "";
            }
            
            @Override
            public Course fromString(String string) {
                return null; // Not needed for ComboBox
            }
        });
    }
    
    /**
     * Setup Mark Entry Table
     */
    private void setupMarkEntryTable() {
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        marksColumn.setCellValueFactory(new PropertyValueFactory<>("marksField"));
        absentColumn.setCellValueFactory(new PropertyValueFactory<>("absentCheckBox"));
        
        markEntryTable.setItems(studentMarkEntries);
    }
    
    /**
     * Setup Attendance Table
     */
    private void setupAttendanceTable() {
        attendanceStudentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        attendanceStudentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        attendanceStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statusComboBox"));
        
        attendanceTable.setItems(studentAttendances);
    }
    
    /**
     * Setup Mark Summary Table
     */
    private void setupMarkSummaryTable() {
        summaryStudentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        summaryStudentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        summaryEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        summaryMarksColumn.setCellValueFactory(new PropertyValueFactory<>("marks"));
        sendEmailColumn.setCellValueFactory(new PropertyValueFactory<>("sendEmailCheckBox"));
        
        markSummaryTable.setItems(studentMarkSummaries);
    }
    
    /**
     * Setup Attendance Report Table
     */
    private void setupAttendanceReportTable() {
        if (attendanceReportTable != null) {
            reportStudentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
            reportStudentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
            totalClassesColumn.setCellValueFactory(new PropertyValueFactory<>("totalClasses"));
            presentClassesColumn.setCellValueFactory(new PropertyValueFactory<>("presentClasses"));
            absentClassesColumn.setCellValueFactory(new PropertyValueFactory<>("absentClasses"));
            attendancePercentColumn.setCellValueFactory(new PropertyValueFactory<>("attendancePercentage"));
            attendanceMarksColumn.setCellValueFactory(new PropertyValueFactory<>("attendanceMarks"));
            
            attendanceReportTable.setItems(attendanceReportEntries);
            
            // Add event handlers for attendance report buttons
            if (generateAttendanceReportButton != null) {
                generateAttendanceReportButton.setOnAction(this::handleGenerateAttendanceReport);
            }
            
            if (exportAttendanceReportButton != null) {
                exportAttendanceReportButton.setOnAction(this::handleExportAttendanceReport);
            }
            
            if (sendAttendanceEmailButton != null) {
                sendAttendanceEmailButton.setOnAction(this::handleSendAttendanceEmail);
            }
        }
    }
    
    /**
     * Setup Event Handlers
     */
    private void setupEventHandlers() {
        // When batch or semester changes, update course list
        batchComboBox.setOnAction(e -> updateCourseList());
        semesterComboBox.setOnAction(e -> updateCourseList());
        
        // Select/Deselect All buttons
        selectAllButton.setOnAction(e -> {
            for (StudentMarkSummary summary : studentMarkSummaries) {
                summary.getSendEmailCheckBox().setSelected(true);
            }
        });
        
        deselectAllButton.setOnAction(e -> {
            for (StudentMarkSummary summary : studentMarkSummaries) {
                summary.getSendEmailCheckBox().setSelected(false);
            }
        });
    }
    
    /**
     * Load faculty courses
     */
    private void loadFacultyCourses() {
        if (facultyId == null || facultyId.isEmpty()) {
            showError("Error", "Faculty ID is not set.");
            return;
        }
        
        List<Course> courses = courseDAO.getAllCourses();
        List<Course> filteredCourses = new ArrayList<>();
        
        for (Course course : courses) {
            if (course.getFacultyId() != null && course.getFacultyId().equals(facultyId)) {
                filteredCourses.add(course);
            }
        }
        
        facultyCourses.clear();
        facultyCourses.addAll(filteredCourses);
        
        // Populate batch and semester ComboBoxes based on faculty courses
        populateBatchAndSemesterComboBoxes(filteredCourses);
    }
    
    /**
     * Populate batch and semester ComboBoxes based on faculty courses
     */
    private void populateBatchAndSemesterComboBoxes(List<Course> courses) {
        List<String> batches = new ArrayList<>();
        List<String> semesters = new ArrayList<>();
        
        for (Course course : courses) {
            String batch = String.valueOf(course.getBatch());
            String semester = String.valueOf(course.getSemester());
            
            if (!batches.contains(batch)) {
                batches.add(batch);
            }
            
            if (!semesters.contains(semester)) {
                semesters.add(semester);
            }
        }
        
        batchComboBox.getItems().clear();
        batchComboBox.getItems().addAll(batches);
        if (!batches.isEmpty()) {
            batchComboBox.setValue(batches.get(0));
        }
        
        semesterComboBox.getItems().clear();
        semesterComboBox.getItems().addAll(semesters);
        if (!semesters.isEmpty()) {
            semesterComboBox.setValue(semesters.get(0));
        }
        
        // Update course list
        updateCourseList();
    }
    
    /**
     * Update course list based on selected batch and semester
     */
    private void updateCourseList() {
        String batch = batchComboBox.getValue();
        String semester = semesterComboBox.getValue();
        
        if (batch == null || semester == null) {
            return;
        }
        
        List<Course> filteredCourses = new ArrayList<>();
        
        for (Course course : facultyCourses) {
            if (String.valueOf(course.getBatch()).equals(batch) && 
                String.valueOf(course.getSemester()).equals(semester)) {
                filteredCourses.add(course);
            }
        }
        
        courseComboBox.getItems().clear();
        courseComboBox.getItems().addAll(filteredCourses);
        if (!filteredCourses.isEmpty()) {
            courseComboBox.setValue(filteredCourses.get(0));
        }
    }
    
    /**
     * Handle View Student List button click
     */
    @FXML
    private void handleViewStudentList(ActionEvent event) {
        Course selectedCourse = courseComboBox.getValue();
        String assessmentType = assessmentTypeComboBox.getValue();
        
        if (selectedCourse == null) {
            showError("Error", "Please select a course.");
            return;
        }
        
        // Get students enrolled in the selected course
        List<Student> enrolledStudents = studentDAO.getEnrolledStudentsByCourse(selectedCourse.getCourseId());
        
        // Clear previous entries
        studentMarkEntries.clear();
        studentAttendances.clear();
        
        // Populate tables based on the selected action
        Tab selectedTab = ((TabPane) viewStudentListButton.getScene().lookup("#mainTabPane")).getSelectionModel().getSelectedItem();
        
        if (selectedTab.getText().equals("Mark Entry")) {
            // Populate mark entry table
            for (Student student : enrolledStudents) {
                studentMarkEntries.add(new StudentMarkEntry(
                    student.getStudentId(),
                    student.getFullName(),
                    student.getEmail()
                ));
            }
            
            // Enable submit button if there are students
            submitMarksButton.setDisable(enrolledStudents.isEmpty());
            
        } else if (selectedTab.getText().equals("Attendance")) {
            // Populate attendance table
            for (Student student : enrolledStudents) {
                studentAttendances.add(new StudentAttendance(
                    student.getStudentId(),
                    student.getFullName(),
                    student.getEmail()
                ));
            }
            
            // Enable submit button if there are students
            submitAttendanceButton.setDisable(enrolledStudents.isEmpty());
        }
    }
    
    /**
     * Handle Submit Marks button click
     */
    @FXML
    private void handleSubmitMarks(ActionEvent event) {
        Course selectedCourse = courseComboBox.getValue();
        String assessmentType = assessmentTypeComboBox.getValue();
        
        if (selectedCourse == null) {
            showError("Error", "Please select a course.");
            return;
        }
        
        // Validate marks
        boolean isValid = true;
        for (StudentMarkEntry entry : studentMarkEntries) {
            if (!entry.isAbsent()) {
                try {
                    double marks = Double.parseDouble(entry.getMarksField().getText());
                    if (marks < 0) {
                        isValid = false;
                        showError("Error", "Marks cannot be negative for student: " + entry.getStudentName());
                        break;
                    }
                } catch (NumberFormatException e) {
                    isValid = false;
                    showError("Error", "Invalid marks for student: " + entry.getStudentName());
                    break;
                }
            }
        }
        
        if (!isValid) {
            return;
        }
        
        // Save marks to database
        boolean allSaved = true;
        List<StudentMarkSummary> summaries = new ArrayList<>();
        
        for (StudentMarkEntry entry : studentMarkEntries) {
            double marks = entry.getMarks();
            String remarks = entry.isAbsent() ? "Absent" : "";
            
            boolean saved = AssessmentDAO.saveAssessment(
                entry.getStudentId(),
                selectedCourse.getCourseId(),
                facultyId,
                assessmentType,
                assessmentType,
                marks,
                100.0, // Assuming total marks is 100
                getWeightageForAssessmentType(assessmentType),
                remarks
            );
            
            if (!saved) {
                allSaved = false;
            }
            
            // Add to summaries for email sending
            summaries.add(new StudentMarkSummary(
                entry.getStudentId(),
                entry.getStudentName(),
                entry.getEmail(),
                marks
            ));
        }
        
        if (allSaved) {
            showInfo("Success", "Marks submitted successfully.");
            
            // Update mark summary table for email sending
            studentMarkSummaries.clear();
            studentMarkSummaries.addAll(summaries);
            
            // Switch to Send Marks tab
            TabPane tabPane = (TabPane) submitMarksButton.getScene().lookup("#mainTabPane");
            Tab sendMarksTab = tabPane.getTabs().stream()
                .filter(tab -> tab.getText().equals("Send Marks"))
                .findFirst()
                .orElse(null);
            
            if (sendMarksTab != null) {
                tabPane.getSelectionModel().select(sendMarksTab);
            }
        } else {
            showError("Error", "Failed to submit marks for some students.");
        }
    }
    
    /**
     * Handle Submit Attendance button click
     */
    @FXML
    private void handleSubmitAttendance(ActionEvent event) {
        Course selectedCourse = courseComboBox.getValue();
        
        if (selectedCourse == null) {
            showError("Error", "Please select a course.");
            return;
        }
        
        // Save attendance to database
        boolean allSaved = true;
        
        for (StudentAttendance attendance : studentAttendances) {
            String status = attendance.getStatus();
            
            boolean saved = AttendanceDAO.saveAttendance(
                attendance.getStudentId(),
                selectedCourse.getCourseId(),
                status
            );
            
            if (!saved) {
                allSaved = false;
            }
        }
        
        if (allSaved) {
            showInfo("Success", "Attendance submitted successfully.");
        } else {
            showError("Error", "Failed to submit attendance for some students.");
        }
    }
    
    /**
     * Handle Select All button click
     */
    @FXML
    private void handleSelectAll(ActionEvent event) {
        for (StudentMarkSummary summary : studentMarkSummaries) {
            summary.getSendEmailCheckBox().setSelected(true);
        }
    }
    
    /**
     * Handle Deselect All button click
     */
    @FXML
    private void handleDeselectAll(ActionEvent event) {
        for (StudentMarkSummary summary : studentMarkSummaries) {
            summary.getSendEmailCheckBox().setSelected(false);
        }
    }
    
    /**
     * Handle Send Marks Email button click
     */
    @FXML
    private void handleSendMarksEmail(ActionEvent event) {
        Course selectedCourse = courseComboBox.getValue();
        String assessmentType = assessmentTypeComboBox.getValue();
        
        if (selectedCourse == null) {
            showError("Error", "Please select a course.");
            return;
        }
        
        // Count selected students
        int selectedCount = 0;
        for (StudentMarkSummary summary : studentMarkSummaries) {
            if (summary.isSendEmail()) {
                selectedCount++;
            }
        }
        
        if (selectedCount == 0) {
            showError("Error", "Please select at least one student to send email.");
            return;
        }
        
        // Confirm sending emails
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Send Marks Email");
        alert.setHeaderText("Are you sure you want to send marks to " + selectedCount + " students?");
        alert.setContentText("This action will send emails to the selected students with their marks for " + 
                            assessmentType + " in " + selectedCourse.getCourseName() + ".");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Send emails
            int successCount = 0;
            
            for (StudentMarkSummary summary : studentMarkSummaries) {
                if (summary.isSendEmail()) {
                    String subject = assessmentType + " Marks for " + selectedCourse.getCourseCode() + " - " + selectedCourse.getCourseName();
                    String body = "Dear " + summary.getStudentName() + ",\n\n" +
                                "Your marks for " + assessmentType + " in " + selectedCourse.getCourseName() + " (" + selectedCourse.getCourseCode() + ") are as follows:\n\n" +
                                "Marks: " + summary.getMarks() + " out of 25\n\n" +
                                "Please contact your faculty if you have any questions.\n\n" +
                                "Regards,\nEduVault - Student Management System";
                    
                    boolean sent = emailService.sendEmail(summary.getEmail(), subject, body);
                    
                    if (sent) {
                        successCount++;
                    }
                }
            }
            
            if (successCount == selectedCount) {
                showInfo("Success", "Emails sent successfully to " + successCount + " students.");
            } else {
                showInfo("Partial Success", "Emails sent to " + successCount + " out of " + selectedCount + " students.");
            }
        }
    }
    
    /**
     * Handle Logout button click
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation");
            alert.setHeaderText("Are you sure you want to logout?");
            alert.setContentText("You will be redirected to the login screen.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Load login screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
                Parent root = loader.load();
                
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("EduVault - Login");
            }
        } catch (IOException e) {
            showError("Error", "Failed to logout: " + e.getMessage());
        }
    }
    
    /**
     * Get weightage for assessment type
     */
    private double getWeightageForAssessmentType(String assessmentType) {
        switch (assessmentType) {
            case "Quiz":
                return 10.0;
            case "Midterm":
                return 30.0;
            case "Final":
                return 40.0;
            case "Assignment":
                return 10.0;
            case "Project":
                return 10.0;
            default:
                return 0.0;
        }
    }
    
    /**
     * Show information alert
     */
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Show error alert
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Handle Generate Attendance Report button click
     */
    @FXML
    private void handleGenerateAttendanceReport(ActionEvent event) {
        Course selectedCourse = courseComboBox.getValue();
        
        if (selectedCourse == null) {
            showError("Error", "Please select a course.");
            return;
        }
        
        try {
            // Generate attendance report using ReportGenerationService
            String reportContent = util.ReportGenerationService.generateAttendanceReport(
                String.valueOf(selectedCourse.getCourseId()), 
                facultyId, 
                sessionComboBox.getValue()
            );
            
            // Show report in a dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Attendance Report Generated");
            alert.setHeaderText("Attendance Report for " + selectedCourse.getCourseCode());
            
            TextArea textArea = new TextArea(reportContent);
            textArea.setEditable(false);
            textArea.setPrefRowCount(25);
            textArea.setPrefColumnCount(80);
            textArea.setStyle("-fx-font-family: monospace");
            
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();
            
            // Also populate the attendance report table for display
            populateAttendanceReportTable(selectedCourse);
            
            showInfo("Success", "Attendance report generated successfully for " + selectedCourse.getCourseCode());
            
        } catch (Exception e) {
            showError("Error", "Failed to generate attendance report: " + e.getMessage());
        }
    }
    
    /**
     * Populate attendance report table for display in the UI
     */
    private void populateAttendanceReportTable(Course selectedCourse) {
        // Get students enrolled in the selected course
        List<Student> enrolledStudents = studentDAO.getEnrolledStudentsByCourse(selectedCourse.getCourseId());
        
        // Clear previous entries
        attendanceReportEntries.clear();
        
        // Populate attendance report table with actual or sample data
        for (Student student : enrolledStudents) {
            // In a real application, you would fetch actual attendance data from the database
            // For demonstration, we'll generate sample data
            int totalClasses = 15; // Assuming 15 classes in the semester
            int presentClasses = (int) (Math.random() * (totalClasses + 1)); // Random number between 0 and totalClasses
            int absentClasses = totalClasses - presentClasses;
            
            attendanceReportEntries.add(new AttendanceReportEntry(
                student.getStudentId(),
                student.getFullName(),
                student.getEmail(),
                totalClasses,
                presentClasses,
                absentClasses
            ));
        }
        
        // Update statistics
        updateAttendanceStatistics();
    }
    
    /**
     * Handle Export Attendance Report button click
     */
    @FXML
    private void handleExportAttendanceReport(ActionEvent event) {
        if (attendanceReportEntries.isEmpty()) {
            showError("Error", "No attendance data to export. Please generate the report first.");
            return;
        }
        
        Course selectedCourse = courseComboBox.getValue();
        
        if (selectedCourse == null) {
            showError("Error", "Please select a course.");
            return;
        }
        
        // In a real application, you would export the attendance report to a PDF or Excel file
        // For now, we'll just show a success message
        showInfo("Export Successful", "Attendance report exported successfully for " + selectedCourse.getCourseCode() + " - " + selectedCourse.getCourseName() + ".");
    }
    
    /**
     * Handle Send Attendance Email button click
     */
    @FXML
    private void handleSendAttendanceEmail(ActionEvent event) {
        if (attendanceReportEntries.isEmpty()) {
            showError("Error", "No attendance data to send. Please generate the report first.");
            return;
        }
        
        Course selectedCourse = courseComboBox.getValue();
        
        if (selectedCourse == null) {
            showError("Error", "Please select a course.");
            return;
        }
        
        // Confirm sending emails
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Send Attendance Report");
        alert.setHeaderText("Are you sure you want to send attendance reports to " + attendanceReportEntries.size() + " students?");
        alert.setContentText("This action will send emails to all students with their attendance reports for " + 
                            selectedCourse.getCourseName() + ".");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Send emails
            int successCount = 0;
            
            for (AttendanceReportEntry entry : attendanceReportEntries) {
                String subject = "Attendance Report for " + selectedCourse.getCourseCode() + " - " + selectedCourse.getCourseName();
                String body = "Dear " + entry.getStudentName() + ",\n\n" +
                            "Your attendance report for " + selectedCourse.getCourseName() + " (" + selectedCourse.getCourseCode() + ") is as follows:\n\n" +
                            "Total Classes: " + entry.getTotalClasses() + "\n" +
                            "Present: " + entry.getPresentClasses() + "\n" +
                            "Absent: " + entry.getAbsentClasses() + "\n" +
                            "Attendance Percentage: " + String.format("%.2f", entry.getAttendancePercentage()) + "%\n" +
                            "Attendance Marks: " + String.format("%.2f", entry.getAttendanceMarks()) + " out of 10\n\n" +
                            "Please contact your faculty if you have any questions.\n\n" +
                            "Regards,\nAcademic Management System";
                
                boolean sent = emailService.sendEmail(entry.getEmail(), subject, body);
                
                if (sent) {
                    successCount++;
                }
            }
            
            if (successCount == attendanceReportEntries.size()) {
                showInfo("Success", "Emails sent successfully to " + successCount + " students.");
            } else {
                showInfo("Partial Success", "Emails sent to " + successCount + " out of " + attendanceReportEntries.size() + " students.");
            }
        }
    }
    
    /**
     * Update attendance statistics
     */
    private void updateAttendanceStatistics() {
        if (attendanceReportEntries.isEmpty()) {
            return;
        }
        
        // Calculate average attendance percentage
        double totalPercentage = 0;
        for (AttendanceReportEntry entry : attendanceReportEntries) {
            totalPercentage += entry.getAttendancePercentage();
        }
        double averagePercentage = totalPercentage / attendanceReportEntries.size();
        
        // Update labels
        if (totalStudentsLabel != null) {
            totalStudentsLabel.setText(String.valueOf(attendanceReportEntries.size()));
        }
        
        if (averageAttendanceLabel != null) {
            averageAttendanceLabel.setText(String.format("%.2f%%", averagePercentage));
        }
    }
    
    /**
     * Handle Generate Marks Report button click
     */
    @FXML
    private void handleGenerateMarksReport(ActionEvent event) {
        Course selectedCourse = courseComboBox.getValue();
        String assessmentType = assessmentTypeComboBox.getValue();
        
        if (selectedCourse == null) {
            showError("Error", "Please select a course.");
            return;
        }
        
        if (assessmentType == null) {
            showError("Error", "Please select an assessment type.");
            return;
        }
        
        try {
            // Generate marks report using ReportGenerationService
            String reportContent = util.ReportGenerationService.generateMarksReport(
                String.valueOf(selectedCourse.getCourseId()), 
                assessmentType, 
                facultyId
            );
            
            // Show report in a dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Marks Report Generated");
            alert.setHeaderText("Marks Report for " + selectedCourse.getCourseCode() + " - " + assessmentType);
            
            TextArea textArea = new TextArea(reportContent);
            textArea.setEditable(false);
            textArea.setPrefRowCount(25);
            textArea.setPrefColumnCount(80);
            textArea.setStyle("-fx-font-family: monospace");
            
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();
            
            // Update statistics if marks data is available
            updateMarksStatistics(String.valueOf(selectedCourse.getCourseId()), assessmentType);
            
        } catch (Exception e) {
            showError("Error", "Failed to generate marks report: " + e.getMessage());
        }
    }
    
    /**
     * Handle Generate Course Report button click
     */
    @FXML
    private void handleGenerateCourseReport(ActionEvent event) {
        Course selectedCourse = courseComboBox.getValue();
        
        if (selectedCourse == null) {
            showError("Error", "Please select a course.");
            return;
        }
        
        try {
            // Generate course report using ReportGenerationService
            String reportContent = util.ReportGenerationService.generateCourseReport(
                String.valueOf(selectedCourse.getCourseId()), 
                facultyId
            );
            
            // Show report in a dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Course Report Generated");
            alert.setHeaderText("Comprehensive Course Report for " + selectedCourse.getCourseCode());
            
            TextArea textArea = new TextArea(reportContent);
            textArea.setEditable(false);
            textArea.setPrefRowCount(30);
            textArea.setPrefColumnCount(80);
            textArea.setStyle("-fx-font-family: monospace");
            
            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();
            
            showInfo("Success", "Course report generated successfully for " + selectedCourse.getCourseCode());
            
        } catch (Exception e) {
            showError("Error", "Failed to generate course report: " + e.getMessage());
        }
    }
    
    /**
     * Update marks statistics for the dashboard
     */
    private void updateMarksStatistics(String courseId, String assessmentType) {
        try {
            java.sql.Connection conn = util.DatabaseConnection.getConnection();
            
            // Calculate average marks for the selected course and assessment type
            String avgQuery = "SELECT AVG(marks/totalMarks * 100) as avgPercentage " +
                            "FROM Assessments " +
                            "WHERE courseId = ? AND assessmentType = ? AND marks > 0";
            
            java.sql.PreparedStatement stmt = conn.prepareStatement(avgQuery);
            stmt.setString(1, courseId);
            stmt.setString(2, assessmentType);
            
            java.sql.ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                double avgMarks = rs.getDouble("avgPercentage");
                if (averageMarksLabel != null) {
                    averageMarksLabel.setText(String.format("%.2f", avgMarks));
                }
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (java.sql.SQLException e) {
            System.err.println("Error updating marks statistics: " + e.getMessage());
        }
    }
}
