package controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.User;

public class UserDialog extends Dialog<User> {
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final TextField fullNameField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField phoneField = new TextField();
    private final TextField departmentField = new TextField();
    private final TextField roleField = new TextField();

    public UserDialog(User user, boolean isEdit) {
        setTitle(isEdit ? "Edit User" : "Add User");
        setHeaderText(isEdit ? "Edit User Information" : "Add New User");
        ButtonType okButtonType = new ButtonType(isEdit ? "Update" : "Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Full Name:"), 0, 2);
        grid.add(fullNameField, 1, 2);
        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(new Label("Phone:"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(new Label("Department Name:"), 0, 5);
        grid.add(departmentField, 1, 5);
        grid.add(new Label("Role:"), 0, 6);
        grid.add(roleField, 1, 6);

        if (user != null) {
            usernameField.setText(user.getUsername());
            passwordField.setText(user.getPassword());
            fullNameField.setText(user.getFullName());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhoneNumber());
            departmentField.setText(user.getDepartmentName());
            roleField.setText(user.getRoleName());
        }

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                User u = user != null ? user : new User();
                u.setUsername(usernameField.getText());
                u.setPassword(passwordField.getText());
                u.setFullName(fullNameField.getText());
                u.setEmail(emailField.getText());
                u.setPhoneNumber(phoneField.getText());
                u.setDepartmentName(departmentField.getText());
                u.setRoleName(roleField.getText());
                return u;
            }
            return null;
        });
    }
}
