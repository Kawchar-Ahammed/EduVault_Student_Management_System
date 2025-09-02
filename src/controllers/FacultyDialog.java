package controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Faculty;

public class FacultyDialog extends Dialog<Faculty> {
    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField designationField = new TextField();
    private final TextField specializationField = new TextField();
    private final TextField departmentField = new TextField();
    private final TextField statusField = new TextField();

    public FacultyDialog(Faculty faculty, boolean isEdit) {
        setTitle(isEdit ? "Edit Faculty" : "Add Faculty");
        setHeaderText(isEdit ? "Edit Faculty Information" : "Add New Faculty");
        ButtonType okButtonType = new ButtonType(isEdit ? "Update" : "Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Full Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Designation:"), 0, 2);
        grid.add(designationField, 1, 2);
        grid.add(new Label("Specialization:"), 0, 3);
        grid.add(specializationField, 1, 3);
        grid.add(new Label("Department Name:"), 0, 4);
        grid.add(departmentField, 1, 4);
        grid.add(new Label("Status:"), 0, 5);
        grid.add(statusField, 1, 5);

        if (faculty != null) {
            nameField.setText(faculty.getFullName());
            emailField.setText(faculty.getEmail());
            designationField.setText(faculty.getDesignation());
            specializationField.setText(faculty.getSpecialization());
            departmentField.setText(faculty.getDepartmentName());
            statusField.setText(faculty.getStatus());
        }

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Faculty f = faculty != null ? faculty : new Faculty();
                f.setFullName(nameField.getText());
                f.setEmail(emailField.getText());
                f.setDesignation(designationField.getText());
                f.setSpecialization(specializationField.getText());
                f.setDepartmentName(departmentField.getText());
                f.setStatus(statusField.getText());
                return f;
            }
            return null;
        });
    }
}
