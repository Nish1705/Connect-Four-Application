module com.example.connectfour {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.intenshala.connectfour to javafx.fxml;
    exports com.intenshala.connectfour;

}