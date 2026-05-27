module org.igirepay.igirepaypaymentgatewayproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.igirepay.igirepaypaymentgatewayproject to javafx.fxml;
    exports org.igirepay.igirepaypaymentgatewayproject;
}
