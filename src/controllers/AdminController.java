package controllers;
//package controllers;
import dao.SimpleCourseDAO;
import dao.SimpleFacultyDAO;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Course;
import model.Faculty;
import model.Student;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for Admin Dashboard
 */
public class AdminController implements Initializable {
    // Users Management Controls
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> userIdColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> userFullNameColumn;
    @FXML private TableColumn<User, String> userEmailColumn;
    @FXML private TableColumn<User, String> userPhoneColumn;
    @FXML private TableColumn<User, String> userDepartmentColumn;
    @FXML private TableColumn<User, String> userPasswordColumn;
    @FXML private TableColumn<User, String> userRoleColumn;
    @FXML private TableColumn<User, String> userStatusColumn;
    @FXML private TableColumn<User, Void> userActionsColumn;
    @FXML private Button addUserButton;
    @FXML private Button refreshUserButton;
    // User DAO
    private dao.UserDAO userDAO;
    private ObservableList<User> userList;

    // Header Controls
    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;

    // Faculty Management Controls
    @FXML private TableView<Faculty> facultyTable;
    @FXML private TableColumn<Faculty, Integer> facultyIdColumn;
    @FXML private TableColumn<Faculty, String> facultyNameColumn;
    @FXML private TableColumn<Faculty, String> facultyEmailColumn;
    @FXML private TableColumn<Faculty, String> facultyDesignationColumn;
    @FXML private TableColumn<Faculty, String> facultySpecializationColumn;
    @FXML private TableColumn<Faculty, String> facultyDepartmentColumn;
    @FXML private TableColumn<Faculty, String> facultyStatusColumn;
    @FXML private TableColumn<Faculty, Void> facultyActionsColumn;
    @FXML private Button addFacultyButton;
    @FXML private Button refreshFacultyButton;

    // Student Management Controls
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> studentIdColumn;
    @FXML private TableColumn<Student, String> studentNameColumn;
    @FXML private TableColumn<Student, String> studentEmailColumn;
    @FXML private TableColumn<Student, String> studentBatchColumn;
    @FXML private TableColumn<Student, String> studentSemesterColumn;
    @FXML private TableColumn<Student, Double> studentCgpaColumn;
    @FXML private TableColumn<Student, String> studentStatusColumn;
    @FXML private TableColumn<Student, Void> studentActionsColumn;
    @FXML private ComboBox<String> batchFilterCombo;
    @FXML private ComboBox<String> semesterFilterCombo;
    @FXML private Button addStudentButton;
    @FXML private Button refreshStudentButton;

    // Course Management Controls
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> courseCodeColumn;
    @FXML private TableColumn<Course, String> courseNameColumn;
    @FXML private TableColumn<Course, Integer> courseCreditsColumn;
    @FXML private TableColumn<Course, String> courseSemesterColumn;
    @FXML private TableColumn<Course, String> courseBatchColumn;
    @FXML private TableColumn<Course, String> courseSessionColumn;
    @FXML private TableColumn<Course, String> courseFacultyColumn;
    @FXML private TableColumn<Course, String> courseDepartmentColumn;
    @FXML private TableColumn<Course, Void> courseActionsColumn;
    @FXML private ComboBox<String> courseBatchFilterCombo;
    @FXML private ComboBox<String> courseSemesterFilterCombo;
    @FXML private Button addCourseButton;
    @FXML private Button refreshCourseButton;

    // Reports Controls
    @FXML private Label totalFacultyLabel;
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalCoursesLabel;
    @FXML private Button generateFacultyReportButton;
    @FXML private Button generateStudentReportButton;
    @FXML private Button generateCourseReportButton;

    // DAOs
    private SimpleFacultyDAO facultyDAO;
    private SimpleStudentDAO studentDAO;
    private SimpleCourseDAO courseDAO;

    // Current user
    private User currentUser;

    // Observable Lists
    private ObservableList<Faculty> facultyList;
    private ObservableList<Student> studentList;
    private ObservableList<Course> courseList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize User DAO and list
        userDAO = new dao.UserDAO();
        userList = FXCollections.observableArrayList();
        // Initialize DAOs
        facultyDAO = new SimpleFacultyDAO();
        studentDAO = new SimpleStudentDAO();
        courseDAO = new SimpleCourseDAO();

        // Initialize Observable Lists
        facultyList = FXCollections.observableArrayList();
        studentList = FXCollections.observableArrayList();
        courseList = FXCollections.observableArrayList();

        // Setup tables
        setupFacultyTable();
        setupStudentTable();
        setupCourseTable();
        setupUserTable();

        // Load initial data
        loadAllData();
        updateStatistics();
        loadUserData();
    }

    /**
     * Set current user for the dashboard
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + user.getFullName());
    }

    /**
     * Setup Faculty Table
     */
    private void setupFacultyTable() {
        facultyIdColumn.setCellValueFactory(new PropertyValueFactory<>("facultyId"));
        facultyNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        facultyEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        facultyDesignationColumn.setCellValueFactory(new PropertyValueFactory<>("designation"));
        facultySpecializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        facultyDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        
        facultyStatusColumn.setCellValueFactory(cellData -> {
            String status = cellData.getValue().getStatus();
            return new javafx.beans.property.SimpleStringProperty(status);
        });

        // Add action buttons
        facultyActionsColumn.setCellFactory(column -> {
            TableCell<Faculty, Void> cell = new TableCell<Faculty, Void>() {
                private final Button editButton = new Button("Edit");
                private final Button deleteButton = new Button("Delete");
                private final HBox buttons = new HBox(5, editButton, deleteButton);

                {
                    editButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-size: 10px;");
                    deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
                    
                    editButton.setOnAction(event -> {
                        Faculty faculty = getTableView().getItems().get(getIndex());
                        handleEditFaculty(faculty);
                    });
                    
                    deleteButton.setOnAction(event -> {
                        Faculty faculty = getTableView().getItems().get(getIndex());
                        handleDeleteFaculty(faculty);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : buttons);
                }
            };
            return cell;
        });

        facultyTable.setItems(facultyList);
    }

    /**
     * Setup Student Table
     */
    private void setupStudentTable() {
    studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
    studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    studentEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    studentBatchColumn.setCellValueFactory(new PropertyValueFactory<>("batch"));
    studentSemesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
    studentCgpaColumn.setCellValueFactory(new PropertyValueFactory<>("cgpa"));
    studentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Add action buttons
        studentActionsColumn.setCellFactory(column -> {
            TableCell<Student, Void> cell = new TableCell<Student, Void>() {
                private final Button editButton = new Button("Edit");
                private final Button deleteButton = new Button("Delete");
                private final HBox buttons = new HBox(5, editButton, deleteButton);

                {
                    editButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-size: 10px;");
                    deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
                    
                    editButton.setOnAction(event -> {
                        Student student = getTableView().getItems().get(getIndex());
                        handleEditStudent(student);
                    });
                    
                    deleteButton.setOnAction(event -> {
                        Student student = getTableView().getItems().get(getIndex());
                        handleDeleteStudent(student);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : buttons);
                }
            };
            return cell;
        });

        studentTable.setItems(studentList);
    }

    /**
     * Setup Course Table
     */
    private void setupCourseTable() {
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseCreditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));
        courseSemesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        courseBatchColumn.setCellValueFactory(new PropertyValueFactory<>("batch"));
        courseSessionColumn.setCellValueFactory(new PropertyValueFactory<>("session"));
        courseFacultyColumn.setCellValueFactory(new PropertyValueFactory<>("facultyName"));
        courseDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));

        // Add action buttons
        courseActionsColumn.setCellFactory(column -> {
            TableCell<Course, Void> cell = new TableCell<Course, Void>() {
                private final Button editButton = new Button("Edit");
                private final Button deleteButton = new Button("Delete");
                private final HBox buttons = new HBox(5, editButton, deleteButton);

                {
                    editButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-size: 10px;");
                    deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
                    
                    editButton.setOnAction(event -> {
                        Course course = getTableView().getItems().get(getIndex());
                        handleEditCourse(course);
                    });
                    
                    deleteButton.setOnAction(event -> {
                        Course course = getTableView().getItems().get(getIndex());
                        handleDeleteCourse(course);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : buttons);
                }
            };
            return cell;
        });

        courseTable.setItems(courseList);
    }

    /**
     * Load all data
     */
    private void loadAllData() {
        loadFacultyData();
        loadStudentData();
        loadCourseData();
        loadFilterOptions();
        loadUserData();
    }

    /**
     * Setup User Table
     */
    private void setupUserTable() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        userFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        userEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    userDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("roleName"));
        userStatusColumn.setCellValueFactory(cellData -> {
            boolean active = cellData.getValue().isActive();
            return new javafx.beans.property.SimpleStringProperty(active ? "Active" : "Inactive");
        });

        // Add action buttons
        userActionsColumn.setCellFactory(column -> {
            TableCell<User, Void> cell = new TableCell<User, Void>() {
                private final Button editButton = new Button("Edit");
                private final Button deleteButton = new Button("Delete");
                private final HBox buttons = new HBox(5, editButton, deleteButton);
                {
                    editButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-size: 10px;");
                    deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px;");
                    editButton.setOnAction(event -> handleEditUser(getTableView().getItems().get(getIndex())));
                    deleteButton.setOnAction(event -> handleDeleteUser(getTableView().getItems().get(getIndex())));
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(buttons);
                    }
                }
            };
            return cell;
        });
        userTable.setItems(userList);
    }

    /**
     * Load user data
     */
    private void loadUserData() {
    // Load all users, not just by role
    List<User> users = userDAO.getAllUsers();
    userList.clear();
    userList.addAll(users);
    }

    // User Management Event Handlers
    @FXML
    private void handleAddUser(ActionEvent event) {
        UserDialog dialog = new UserDialog(null, false);
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(newUser -> {
            // You may want to add role and department selection logic here
            if (userDAO.addUser(newUser, newUser.getPassword(), newUser.getRoleName())) {
                showInfo("Success", "User added successfully.");
                loadUserData();
            } else {
                showError("Error", "Failed to add user.");
            }
        });
    }

    @FXML
    private void handleRefreshUser(ActionEvent event) {
        loadUserData();
        showInfo("Refresh", "User data refreshed successfully.");
    }

    private void handleEditUser(User user) {
        if (user == null) return;
        UserDialog dialog = new UserDialog(user, true);
        Optional<User> result = dialog.showAndWait();
        result.ifPresent(updatedUser -> {
            if (userDAO.updateUser(updatedUser)) {
                showInfo("Success", "User updated successfully.");
                loadUserData();
            } else {
                showError("Error", "Failed to update user.");
            }
        });
    }

    private void handleDeleteUser(User user) {
        if (user == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Are you sure you want to delete this user?");
        alert.setContentText("User: " + user.getFullName() + "\nThis action cannot be undone.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (userDAO.deleteUser(user.getUserId())) {
                showInfo("Success", "User deleted successfully.");
                loadUserData();
            } else {
                showError("Error", "Failed to delete user.");
            }
        }
    }

    /**
     * Load faculty data
     */
    private void loadFacultyData() {
        List<Faculty> faculties = facultyDAO.getAllFaculty();
        facultyList.clear();
        facultyList.addAll(faculties);
    }

    /**
     * Load student data
     */
    private void loadStudentData() {
        List<Student> students = studentDAO.getAllStudents();
        studentList.clear();
        studentList.addAll(students);
    }

    /**
     * Load course data
     */
    private void loadCourseData() {
        List<Course> courses = courseDAO.getAllCourses();
        courseList.clear();
        courseList.addAll(courses);
    }

    /**
     * Load filter options from database
     */
    private void loadFilterOptions() {
        // Load batch options from database
        List<String> batches = studentDAO.getAllBatches();
        batchFilterCombo.setItems(FXCollections.observableArrayList(batches));
        courseBatchFilterCombo.setItems(FXCollections.observableArrayList(batches));

        // Load semester options from database
        List<String> semesters = studentDAO.getAllSemesters();
        semesterFilterCombo.setItems(FXCollections.observableArrayList(semesters));
        courseSemesterFilterCombo.setItems(FXCollections.observableArrayList(semesters));
    }

    /**
     * Update statistics
     */
    private void updateStatistics() {
        totalFacultyLabel.setText(String.valueOf(facultyList.size()));
        totalStudentsLabel.setText(String.valueOf(studentList.size()));
        totalCoursesLabel.setText(String.valueOf(courseList.size()));
    }

    // Event Handlers
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
                stage.setTitle("Academic Management System - Login");
            }
        } catch (IOException e) {
            showError("Error", "Failed to logout: " + e.getMessage());
        }
    }

    // Faculty Management Event Handlers
    @FXML
    private void handleAddFaculty(ActionEvent event) {
        FacultyDialog dialog = new FacultyDialog(null, false);
        Optional<Faculty> result = dialog.showAndWait();
        result.ifPresent(newFaculty -> {
            if (facultyDAO.addFaculty(newFaculty)) {
                showInfo("Success", "Faculty added successfully.");
                loadFacultyData();
                updateStatistics();
            } else {
                showError("Error", "Failed to add faculty.");
            }
        });
    }

    @FXML
    private void handleRefreshFaculty(ActionEvent event) {
        loadFacultyData();
        updateStatistics();
        showInfo("Refresh", "Faculty data refreshed successfully.");
    }

    private void handleEditFaculty(Faculty faculty) {
        if (faculty == null) return;
        FacultyDialog dialog = new FacultyDialog(faculty, true);
        Optional<Faculty> result = dialog.showAndWait();
        result.ifPresent(updatedFaculty -> {
            if (facultyDAO.updateFaculty(updatedFaculty)) {
                showInfo("Success", "Faculty updated successfully.");
                loadFacultyData();
                updateStatistics();
            } else {
                showError("Error", "Failed to update faculty.");
            }
        });
    }

    private void handleDeleteFaculty(Faculty faculty) {
        if (faculty == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Faculty");
        alert.setHeaderText("Are you sure you want to delete this faculty member?");
        alert.setContentText("Faculty: " + faculty.getFullName() + "\nThis action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (facultyDAO.deleteFaculty(faculty.getFacultyId())) {
                showInfo("Success", "Faculty member deleted successfully.");
                loadFacultyData();
                updateStatistics();
            } else {
                showError("Error", "Failed to delete faculty member.");
            }
        }
    }

    // Student Management Event Handlers
    @FXML
    private void handleAddStudent(ActionEvent event) {
        StudentDialog dialog = new StudentDialog(null, false);
        Optional<Student> result = dialog.showAndWait();
        result.ifPresent(newStudent -> {
            if (studentDAO.addStudent(newStudent)) {
                showInfo("Success", "Student added successfully.");
                loadStudentData();
                updateStatistics();
            } else {
                showError("Error", "Failed to add student.");
            }
        });
    }

    @FXML
    private void handleRefreshStudent(ActionEvent event) {
        loadStudentData();
        updateStatistics();
        showInfo("Refresh", "Student data refreshed successfully.");
    }

    @FXML
    private void handleBatchFilter(ActionEvent event) {
        String selectedBatch = batchFilterCombo.getValue();
        String selectedSemester = semesterFilterCombo.getValue();
        
        List<Student> filteredStudents;
        
        if (selectedBatch != null && selectedSemester != null) {
            filteredStudents = studentDAO.getStudentsByBatchAndSemester(selectedBatch, selectedSemester);
        } else if (selectedBatch != null) {
            filteredStudents = studentDAO.getStudentsByBatch(selectedBatch);
        } else if (selectedSemester != null) {
            filteredStudents = studentDAO.getStudentsBySemester(selectedSemester);
        } else {
            filteredStudents = studentDAO.getAllStudents();
        }
        
        studentList.clear();
        studentList.addAll(filteredStudents);
    }

    @FXML
    private void handleSemesterFilter(ActionEvent event) {
        handleBatchFilter(event); // Same logic
    }

    private void handleEditStudent(Student student) {
        if (student == null) return;
        StudentDialog dialog = new StudentDialog(student, true);
        Optional<Student> result = dialog.showAndWait();
        result.ifPresent(updatedStudent -> {
            if (studentDAO.updateStudent(updatedStudent)) {
                showInfo("Success", "Student updated successfully.");
                loadStudentData();
                updateStatistics();
            } else {
                showError("Error", "Failed to update student.");
            }
        });
    }

    private void handleDeleteStudent(Student student) {
        if (student == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Student");
        alert.setHeaderText("Are you sure you want to delete this student?");
        alert.setContentText("Student: " + student.getFullName() + "\nThis action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (studentDAO.deleteStudent(student.getStudentId())) {
                showInfo("Success", "Student deleted successfully.");
                loadStudentData();
                updateStatistics();
            } else {
                showError("Error", "Failed to delete student.");
            }
        }
    }

    // Course Management Event Handlers
    @FXML
    private void handleAddCourse(ActionEvent event) {
        CourseDialog dialog = new CourseDialog(null, false);
        Optional<Course> result = dialog.showAndWait();
        result.ifPresent(newCourse -> {
            if (courseDAO.addCourse(newCourse)) {
                showInfo("Success", "Course added successfully.");
                loadCourseData();
                updateStatistics();
            } else {
                showError("Error", "Failed to add course.");
            }
        });
    }

    @FXML
    private void handleRefreshCourse(ActionEvent event) {
        loadCourseData();
        updateStatistics();
        showInfo("Refresh", "Course data refreshed successfully.");
    }

    @FXML
    private void handleCourseBatchFilter(ActionEvent event) {
        String selectedBatch = courseBatchFilterCombo.getValue();
        String selectedSemester = courseSemesterFilterCombo.getValue();
        
        List<Course> filteredCourses;
        
        if (selectedBatch != null && selectedSemester != null) {
            filteredCourses = courseDAO.getCoursesByBatchAndSemester(selectedBatch, selectedSemester);
        } else if (selectedBatch != null) {
            filteredCourses = courseDAO.getCoursesByBatch(selectedBatch);
        } else if (selectedSemester != null) {
            filteredCourses = courseDAO.getCoursesBySemester(selectedSemester);
        } else {
            filteredCourses = courseDAO.getAllCourses();
        }
        
        courseList.clear();
        courseList.addAll(filteredCourses);
    }

    @FXML
    private void handleCourseSemesterFilter(ActionEvent event) {
        handleCourseBatchFilter(event); // Same logic
    }

    private void handleEditCourse(Course course) {
        if (course == null) return;
        CourseDialog dialog = new CourseDialog(course, true);
        Optional<Course> result = dialog.showAndWait();
        result.ifPresent(updatedCourse -> {
            if (courseDAO.updateCourse(updatedCourse)) {
                showInfo("Success", "Course updated successfully.");
                loadCourseData();
                updateStatistics();
            } else {
                showError("Error", "Failed to update course.");
            }
        });
    }

    private void handleDeleteCourse(Course course) {
        if (course == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Course");
        alert.setHeaderText("Are you sure you want to delete this course?");
        alert.setContentText("Course: " + course.getCourseName() + "\nThis action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (courseDAO.deleteCourse(course.getCourseId())) {
                showInfo("Success", "Course deleted successfully.");
                loadCourseData();
                updateStatistics();
            } else {
                showError("Error", "Failed to delete course.");
            }
        }
    }

    // Report Generation Event Handlers
    @FXML
    private void handleGenerateFacultyReport(ActionEvent event) {
        showInfo("Faculty Report", "Faculty report generation will be implemented in the next phase.");
    }

    @FXML
    private void handleGenerateStudentReport(ActionEvent event) {
        showInfo("Student Report", "Student report generation will be implemented in the next phase.");
    }

    @FXML
    private void handleGenerateCourseReport(ActionEvent event) {
        showInfo("Course Report", "Course report generation will be implemented in the next phase.");
    }

    // Utility Methods
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
