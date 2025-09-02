package controllers;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Course;

public class CourseDialog extends Dialog<Course> {
    private final TextField codeField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField creditsField = new TextField();
    private final TextField semesterField = new TextField();
    private final TextField batchField = new TextField();
    private final TextField sessionField = new TextField();
    private final TextField facultyField = new TextField();
    private final TextField departmentField = new TextField();

    public CourseDialog(Course course, boolean isEdit) {
        setTitle(isEdit ? "Edit Course" : "Add Course");
        setHeaderText(isEdit ? "Edit Course Information" : "Add New Course");
        ButtonType okButtonType = new ButtonType(isEdit ? "Update" : "Add", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Course Code:"), 0, 0);
        grid.add(codeField, 1, 0);
        grid.add(new Label("Course Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Credits:"), 0, 2);
        grid.add(creditsField, 1, 2);
        grid.add(new Label("Semester:"), 0, 3);
        grid.add(semesterField, 1, 3);
        grid.add(new Label("Batch:"), 0, 4);
        grid.add(batchField, 1, 4);
        grid.add(new Label("Session:"), 0, 5);
        grid.add(sessionField, 1, 5);
        grid.add(new Label("Faculty Name:"), 0, 6);
        grid.add(facultyField, 1, 6);
        grid.add(new Label("Department Name:"), 0, 7);
        grid.add(departmentField, 1, 7);

        if (course != null) {
            codeField.setText(course.getCourseCode());
            nameField.setText(course.getCourseName());
            creditsField.setText(String.valueOf(course.getCredits()));
            semesterField.setText(String.valueOf(course.getSemester()));
            batchField.setText(String.valueOf(course.getBatch()));
            sessionField.setText(course.getSession());
            facultyField.setText(course.getFacultyName());
            departmentField.setText(course.getDepartmentName());
        }

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                Course c = course != null ? course : new Course();
                c.setCourseCode(codeField.getText());
                c.setCourseName(nameField.getText());
                try {
                    c.setCredits(Integer.parseInt(creditsField.getText()));
                } catch (NumberFormatException e) {
                    c.setCredits(0);
                }
                try {
                    c.setSemester(Integer.parseInt(semesterField.getText()));
                } catch (NumberFormatException e) {
                    c.setSemester(0);
                }
                try {
                    c.setBatch(Integer.parseInt(batchField.getText()));
                } catch (NumberFormatException e) {
                    c.setBatch(0);
                }
                c.setSession(sessionField.getText());
                c.setFacultyName(facultyField.getText());
                c.setDepartmentName(departmentField.getText());
                return c;
            }
            return null;
        });
    }
}
