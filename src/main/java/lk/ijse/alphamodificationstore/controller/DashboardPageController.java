package lk.ijse.alphamodificationstore.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardPageController implements Initializable {

    public AnchorPane ancDashboard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    private void navigateTo(String path) {
        try {
            ancDashboard.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancDashboard.widthProperty());
            anchorPane.prefHeightProperty().bind(ancDashboard.heightProperty());

            ancDashboard.getChildren().add(anchorPane);

        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

    public void btnGoToSupplierPageOnAction(ActionEvent actionEvent) { navigateTo("/view/SupplierPage.fxml"); }

    public void btnCustomerOnAction(ActionEvent actionEvent) { navigateTo("/view/CustomerPage.fxml"); }

    public void btnGoToItemPageOnAction(ActionEvent actionEvent) { navigateTo("/view/ItemPage.fxml"); }

    public void btnGoToEmployeePageOnAction(ActionEvent actionEvent) { navigateTo("/view/EmployeePage.fxml");}

    public void btnGoToPurchaseOrderPageOnAction(ActionEvent actionEvent) { navigateTo("/view/PurchaseOrderPage.fxml");}

    public void btnGoToPurchaseReportPageOnAction(ActionEvent actionEvent) {navigateTo("/view/PurchaseReportPage.fxml");}

    public void btnGoToPreOrderPageOnAction(ActionEvent actionEvent) { navigateTo("/view/PreOrderPage.fxml");}

    public void btnGoToOrderPaymentsPageOnAction(ActionEvent actionEvent) { navigateTo("/view/OrderPaymentPage.fxml"); }

    public void btnGoToPurchasePaymentsPageOnAction(ActionEvent actionEvent) { navigateTo("/view/PurchasePaymentPage.fxml"); }


    public void btnGoToUserPageOnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/UserPasswordPopUpPage.fxml"));

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Change Password");

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();


    }

    public void btnGoToOrderPageOnAction(ActionEvent event) {
        navigateTo("/view/OrderPage.fxml");
    }

}
