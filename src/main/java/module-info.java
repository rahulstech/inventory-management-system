module javafx.starter.project.main {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens ucf.assignments to com.google.gson;

    exports ucf.assignments;
}