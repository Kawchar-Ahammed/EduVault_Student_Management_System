package controllers;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for Login Screen
 */
public class LoginController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    // Controllers
    private UserDAO userDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize UserDAO
        userDAO = new UserDAO();
        
        // Populate role ComboBox
        roleComboBox.getItems().addAll("Admin", "Faculty", "Student");
        
        // Set default role
        roleComboBox.setValue("Admin");
        
        // Add enter key support for password field
        passwordField.setOnAction(this::handleLogin);
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || role == null) {
            showError("Please fill in all fields.");
            return;
        }

        try {
            // Database authentication - fetching credentials from database
            System.out.println("=== LOGIN ATTEMPT ===");
            System.out.println("Username: " + username);
            System.out.println("Role: " + role);
            System.out.println("Attempting database authentication...");
            
            // Convert role to match database format (uppercase)
            String dbRole = role.toUpperCase();
            
            // Authenticate against database
            User user = userDAO.authenticateUser(username, password, dbRole);
            
            if (user != null) {
                // Authentication successful
                System.out.println("✅ AUTHENTICATION SUCCESSFUL");
                System.out.println("User ID: " + user.getUserId());
                System.out.println("Full Name: " + user.getFullName());
                System.out.println("Role: " + user.getRoleName());
                
                hideError();
                
                // Route to appropriate dashboard based on role
                switch (role) {
                    case "Admin":
                        System.out.println("Redirecting to Admin Dashboard...");
                        loadAdminDashboard(user);
                        break;
                    case "Faculty":
                        System.out.println("Redirecting to Faculty Dashboard...");
                        loadFacultyDashboard(user);
                        break;
                    case "Student":
                        System.out.println("Redirecting to Student Dashboard...");
                        loadStudentDashboard(user);
                        break;
                    default:
                        System.err.println("❌ Invalid role selected: " + role);
                        showError("Invalid role selected.");
                }
            } else {
                // Authentication failed
                System.err.println("❌ AUTHENTICATION FAILED");
                System.err.println("Invalid credentials for user: " + username + " with role: " + role);
                showError("Invalid username, password, or role combination. Please check your credentials.");
            }
            
        } catch (Exception e) {
            System.err.println("❌ LOGIN ERROR:");
            System.err.println("Error Type: " + e.getClass().getSimpleName());
            System.err.println("Error Message: " + e.getMessage());
            e.printStackTrace();
            showError("Login failed: " + e.getMessage());
        }
    }

    /**
     * Load Admin Dashboard
     */
    private void loadAdminDashboard(User user) {
        try {
            System.out.println("=== ADMIN DASHBOARD LOADING ===");
            System.out.println("Current user: " + user.getFullName());
            System.out.println("Attempting to load: /fxml/AdminDashboard.fxml");
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminDashboard.fxml"));
            System.out.println("FXMLLoader created successfully");
            
            Parent root = loader.load();
            System.out.println("FXML loaded successfully");
            
            // Get the controller and set the current user
            AdminController adminController = loader.getController();
            System.out.println("AdminController obtained: " + adminController);
            
            adminController.setCurrentUser(user);
            System.out.println("Current user set in AdminController");
            
            // Get current stage and set new scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("EduVault - Admin Dashboard");
                stage.setMaximized(true); // Maximize the admin dashboard            System.out.println("✅ Admin Dashboard loaded successfully!");
            
        } catch (IOException e) {
            System.err.println("❌ FAILED TO LOAD ADMIN DASHBOARD:");
            System.err.println("Error Type: " + e.getClass().getSimpleName());
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("Full Error Details:");
            e.printStackTrace();
            
            // Also show in UI
            showError("Failed to load admin dashboard: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ UNEXPECTED ERROR LOADING ADMIN DASHBOARD:");
            System.err.println("Error Type: " + e.getClass().getSimpleName());
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("Full Error Details:");
            e.printStackTrace();
            
            // Also show in UI
            showError("Unexpected error loading admin dashboard: " + e.getMessage());
        }
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Hide error message
     */
    private void hideError() {
        errorLabel.setVisible(false);
    }

    /**
     * Load Faculty Dashboard
     */
    private void loadFacultyDashboard(model.User user) {
        try {
            System.out.println("=== FACULTY DASHBOARD LOADING ===");
            System.out.println("Current user: " + user.getFullName());
            System.out.println("Attempting to load: /fxml/FacultyDashboard_modern.fxml");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FacultyDashboard.fxml"));
            System.out.println("FXMLLoader created successfully");

            Parent root = loader.load();
            System.out.println("FXML loaded successfully");

            // Get the controller
            FacultyController facultyController = loader.getController();
            System.out.println("FacultyController obtained: " + facultyController);
            
            // Get faculty ID from database
            dao.SimpleFacultyDAO facultyDAO = new dao.SimpleFacultyDAO();
            List<model.Faculty> faculties = facultyDAO.getAllFaculty();
            String facultyId = null;
            
            for (model.Faculty faculty : faculties) {
                if (faculty.getUserId() == user.getUserId()) {
                    facultyId = faculty.getFacultyId();
                    System.out.println("Found faculty ID: " + facultyId + " for user ID: " + user.getUserId());
                    break;
                }
            }
            
            if (facultyId == null) {
                // If faculty ID not found, use user ID as fallback
                facultyId = String.valueOf(user.getUserId());
                System.out.println("Faculty ID not found, using user ID as fallback: " + facultyId);
            }
            
            // Set the faculty ID in the controller
            facultyController.setFacultyId(facultyId);
            System.out.println("Faculty ID set in FacultyController: " + facultyId);

            // Get current stage and set new scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            
            // Apply CSS
            scene.getStylesheets().add(getClass().getResource("/fxml/faculty_dashboard.css").toExternalForm());
            System.out.println("CSS applied to scene");
            
            stage.setScene(scene);
            stage.setTitle("EduVault - Faculty Dashboard");
            stage.setMaximized(true);

            System.out.println("✅ Faculty Dashboard loaded successfully!");

        } catch (IOException e) {
            System.err.println("❌ FAILED TO LOAD FACULTY DASHBOARD:");
            System.err.println("Error Type: " + e.getClass().getSimpleName());
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("Full Error Details:");
            e.printStackTrace();
            showError("Failed to load faculty dashboard: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ UNEXPECTED ERROR LOADING FACULTY DASHBOARD:");
            System.err.println("Error Type: " + e.getClass().getSimpleName());
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("Full Error Details:");
            e.printStackTrace();
            showError("Unexpected error loading faculty dashboard: " + e.getMessage());
        }
    }

    // ADD this method to your LoginController.java to properly set facultyId

private void handleFacultyLogin(String username, String password) {
    try {
        // Authenticate user
        User user = new dao.UserDAO().authenticateUser(username, password, "FACULTY");
        
        if (user != null) {
            System.out.println("Faculty login successful for: " + user.getFullName());
            
            // Get faculty record to retrieve facultyId
            dao.SimpleFacultyDAO facultyDAO = new dao.SimpleFacultyDAO();
            List<model.Faculty> allFaculty = facultyDAO.getAllFaculty();
            
            String facultyId = null;
            for (model.Faculty faculty : allFaculty) {
                if (faculty.getUserId() == user.getUserId()) {
                    facultyId = faculty.getFacultyId();
                    System.out.println("Found facultyId: " + facultyId + " for userId: " + user.getUserId());
                    break;
                }
            }
            
            if (facultyId != null) {
                // Load Faculty Dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FacultyDashboard.fxml"));
                Parent root = loader.load();
                
                // Get controller and set facultyId
                controllers.FacultyController facultyController = loader.getController();
                facultyController.setFacultyId(facultyId); // CRITICAL: Pass the facultyId
                
                // Show dashboard
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                
                // Apply CSS
                scene.getStylesheets().add(getClass().getResource("/fxml/faculty_dashboard.css").toExternalForm());
                
                stage.setScene(scene);
                stage.setTitle("Faculty Dashboard - " + user.getFullName());
                stage.setMaximized(true);
                stage.show();
                
            } else {
                showAlert("Error", "Faculty record not found for user: " + user.getFullName());
            }
        } else {
            showAlert("Login Failed", "Invalid credentials for faculty login.");
        }
        
    } catch (Exception e) {
        e.printStackTrace();
        showAlert("Error", "Login failed: " + e.getMessage());
    }
}

/**
 * Load Student Dashboard
 */
private void loadStudentDashboard(model.User user) {
    try {
        System.out.println("=== STUDENT DASHBOARD LOADING ===");
        System.out.println("Current user: " + user.getFullName());
        System.out.println("Attempting to load: /fxml/StudentDashboard.fxml");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StudentDashboard.fxml"));
        System.out.println("FXMLLoader created successfully");

        Parent root = loader.load();
        System.out.println("FXML loaded successfully");

        // Get the controller
        StudentController studentController = loader.getController();
        System.out.println("StudentController obtained: " + studentController);
        
        // Get student ID from database
        dao.SimpleStudentDAO studentDAO = new dao.SimpleStudentDAO();
        List<model.Student> students = studentDAO.getAllStudents();
        String studentId = null;
        
        for (model.Student student : students) {
            if (student.getUserId() == user.getUserId()) {
                studentId = student.getStudentId();
                System.out.println("Found student ID: " + studentId + " for user ID: " + user.getUserId());
                break;
            }
        }
        
        if (studentId == null) {
            // If student ID not found, use user ID as fallback
            studentId = String.valueOf(user.getUserId());
            System.out.println("Student ID not found, using user ID as fallback: " + studentId);
        }
        
        // Set the student ID in the controller
        studentController.setStudentId(studentId);
        System.out.println("Student ID set in StudentController: " + studentId);

        // Get current stage and set new scene
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Scene scene = new Scene(root);
        
        // Apply CSS
        scene.getStylesheets().add(getClass().getResource("/fxml/student_dashboard.css").toExternalForm());
        System.out.println("CSS applied to scene");
        
        stage.setScene(scene);
        stage.setTitle("EduVault - Student Dashboard");
        stage.setMaximized(true);

        System.out.println("✅ Student Dashboard loaded successfully!");

    } catch (IOException e) {
        System.err.println("❌ FAILED TO LOAD STUDENT DASHBOARD:");
        System.err.println("Error Type: " + e.getClass().getSimpleName());
        System.err.println("Error Message: " + e.getMessage());
        System.err.println("Full Error Details:");
        e.printStackTrace();
        showError("Failed to load student dashboard: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("❌ UNEXPECTED ERROR LOADING STUDENT DASHBOARD:");
        System.err.println("Error Type: " + e.getClass().getSimpleName());
        System.err.println("Error Message: " + e.getMessage());
        System.err.println("Full Error Details:");
        e.printStackTrace();
        showError("Unexpected error loading student dashboard: " + e.getMessage());
    }
}

// Helper method for alerts
private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}
}
