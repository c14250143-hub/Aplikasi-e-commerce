module com.example.aplikasi_ecommerce {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.aplikasi_ecommerce to javafx.fxml;
    opens com.example.aplikasi_ecommerce.model to javafx.base;
    exports com.example.aplikasi_ecommerce;
}