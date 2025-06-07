package lk.ijse.alphamodificationstore.controller;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardPageController implements Initializable {

    public AnchorPane ancDashboard;
    public Label alpha;
    public Label alpha1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //animateLabelTyping1();
       // animateLabelTyping2();
        animateLabelZoomIn();
        animateLabelZoomIn2();

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

//    private void animateLabelTyping1(){
//        String loginText = (alpha).getText();
//        int animationDuration = 250;
//
//        (alpha).setText("");
//        Timeline typingAnimation = new Timeline();// Duration of animation in milliseconds
//
//        for (int i = 0; i <= loginText.length(); i++) {
//            int finalI = i;
//            KeyFrame keyFrame = new KeyFrame(
//                    Duration.millis(animationDuration * i),
//                    event -> (alpha).setText(loginText.substring(0, finalI))
//            );
//            typingAnimation.getKeyFrames().add(keyFrame);
//        }
//        typingAnimation.play();
//    }
//   private void animateLabelTyping2(){
//       /* String loginText = (alpha1).getText();
//        int animationDuration = 250;
//
//        (alpha1).setText("");
//        Timeline typingAnimation = new Timeline();// Duration of animation in milliseconds
//
//        for (int i = 0; i <= loginText.length(); i++) {
//            int finalI = i;
//            KeyFrame keyFrame = new KeyFrame(
//                    Duration.millis(animationDuration * i),
//                    event -> (alpha1).setText(loginText.substring(0, finalI))
//            );
//            typingAnimation.getKeyFrames().add(keyFrame);
//        }
//        typingAnimation.play();
//    }

    private void animateLabelZoomIn() {
        String loginText = alpha.getText();
        alpha.setText(loginText);

        // Set initial scale and opacity
        alpha.setScaleX(0.5);
        alpha.setScaleY(0.5);
        alpha.setOpacity(0);

        // Create scale transition (zoom effect)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1500), alpha);
        scaleTransition.setFromX(0.5);
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);

        // Create fade-in transition
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1500), alpha);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        // Play both transitions together
        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.play();
    }

    private void animateLabelZoomIn2() {
        String loginText = alpha1.getText();
        alpha1.setText(loginText);

        // Set initial scale and opacity
        alpha1.setScaleX(0.5);
        alpha1.setScaleY(0.5);
        alpha1.setOpacity(0);

        // Create scale transition (zoom effect)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1500), alpha1);
        scaleTransition.setFromX(0.5);
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);

        // Create fade-in transition
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1500), alpha1);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);

        // Play both transitions together
        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
        parallelTransition.play();
    }

    public void btnGoToSupplierOrderPageOnAction(ActionEvent event) {
        navigateTo("/view/SupplyOrderPage.fxml");
    }
}

