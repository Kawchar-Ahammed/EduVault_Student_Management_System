package controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Student;

public class StudentDialog extends Dialog<Student> {
    private final TextField nameField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField batchField = new TextField();
    private final TextField semesterField = new TextField();
    private final TextField cgpaField = new TextField();
    private final TextField statusField = new TextField();

    public StudentDialog(Student student, boolean isEdit) {
        setTitle(isEdit ? "Edit Student" : "Add Student");
        setHeaderText(isEdit ? "Edit Student Information" : "Add New Student");
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
        grid.add(new Label("Batch:"), 0, 2);
        grid.add(batchField, 1, 2);
        grid.add(new Label("Semester:"), 0, 3);
        grid.add(semesterField, 1, 3);
        grid.add(new Label("CGPA:"), 0, 4);
        grid.add(cgpaField, 1, 4);
        grid.add(new Label("Status:"), 0, 5);
        grid.add(statusField, 1, 5);

        if (student != null) {
            nameField.setText(student.getFullName());
            emailField.setText(student.getEmail());
            batchField.setText(String.valueOf(student.getBatch()));
            semesterField.setText(String.valueOf(student.getSemester()));
            cgpaField.setText(String.valueOf(student.getCgpa()));
            statusField.setText(student.getStatus());
        }

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Student s = student != null ? student : new Student();
                s.setFullName(nameField.getText());
                s.setEmail(emailField.getText());
                try {
                    s.setBatch(Integer.parseInt(batchField.getText()));
                } catch (NumberFormatException e) {
                    s.setBatch(0);
                }
                try {
                    s.setSemester(Integer.parseInt(semesterField.getText()));
                } catch (NumberFormatException e) {
                    s.setSemester(0);
                }
                try {
                    s.setCgpa(Double.parseDouble(cgpaField.getText()));
                } catch (NumberFormatException e) {
                    s.setCgpa(0.0);
                }
                s.setStatus(statusField.getText());
                return s;
            }
            return null;
        });
    }
}
