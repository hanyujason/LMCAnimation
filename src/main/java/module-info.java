module com.example.lmcanimation {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lmcanimation to javafx.fxml;
    exports com.example.lmcanimation;
}