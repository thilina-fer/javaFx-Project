package lk.ijse.alphamodificationstore.controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    public ImageView myImage;
    public AnchorPane ancMainPage;

    public void startAnimation(MouseEvent mouseEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       /* TranslateTransition translate = new TranslateTransition();
        translate.setNode(myImage);
        translate.setDuration(Duration.millis(1000));
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.setByX(1400);
        translate.setByY(700);
        translate.setAutoReverse(true);
        translate.play();*/

        /*
        RotateTransition rotate = new RotateTransition();
        rotate.setNode(myImage);
        rotate.setDuration(Duration.millis(1000));
        rotate.setCycleCount(TranslateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.setByAngle(360);
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.play();*/

        FadeTransition fade = new FadeTransition();
        fade.setNode(myImage);
        fade.setDuration(Duration.millis(2000));
        fade.setCycleCount(TranslateTransition.INDEFINITE);
        fade.setInterpolator(Interpolator.LINEAR);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    public void btnSignInOnAction(ActionEvent actionEvent) {
        navigateTo("/view/LoginPage.fxml");
    }


    public void btnSignUpOnAction(ActionEvent actionEvent) {
        navigateTo("/view/SignUpPage.fxml");
    }

    private void navigateTo(String path) {
        try {
            ancMainPage.getChildren().clear();

            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));

            anchorPane.prefWidthProperty().bind(ancMainPage.widthProperty());
            anchorPane.prefHeightProperty().bind(ancMainPage.heightProperty());

            ancMainPage.getChildren().add(anchorPane);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
            e.printStackTrace();
        }
    }

}
