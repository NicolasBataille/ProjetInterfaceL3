module com.example.testjavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projetinterface to javafx.fxml;
    exports com.example.projetinterface;
}