package lk.ijse.alphamodificationstore.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPage2Controller implements Initializable {

    public Label lblAlpha;
    public Label lblModifications;
    public AnchorPane ancMainPage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        animateLabelFadeIn();
        animateLabelSlideIn();
    }

    private void animateLabelFadeIn() {
        /*String loginText = lblAlpha.getText();
        lblAlpha.setText(""); // Clear existing text

        // Set the full text first but make it invisible
        lblAlpha.setText(loginText);
        lblAlpha.setOpacity(0); // Fully transparent

        FadeTransition fadeIn = new FadeTransition();
        fadeIn.setNode(lblAlpha);
        fadeIn.setDuration(Duration.millis(3000)); // Total duration for fade-in
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();*/

        String loginText = lblAlpha.getText();
        lblAlpha.setText(loginText); // Make sure text is visible
        lblAlpha.setOpacity(0);      // Start invisible
        lblAlpha.setTranslateX(-50); // Start off-screen to the left

        // Slide-in animation
        TranslateTransition slide = new TranslateTransition(Duration.millis(2000), lblAlpha);
        slide.setFromX(100);
        slide.setToX(0);

        // Fade-in animation
        FadeTransition fade = new FadeTransition(Duration.millis(2000), lblAlpha);
        fade.setFromValue(0);
        fade.setToValue(1);

        // Play both at once
        ParallelTransition parallel = new ParallelTransition(slide, fade);
        parallel.play();
    }

    private void animateLabelSlideIn() {
        String loginText = lblModifications.getText();
        lblModifications.setText(loginText); // Make sure text is visible
        lblModifications.setOpacity(0);      // Start invisible
        lblModifications.setTranslateX(-50); // Start off-screen to the left

        // Slide-in animation
        TranslateTransition slide = new TranslateTransition(Duration.millis(2000), lblModifications);
        slide.setFromX(-100);
        slide.setToX(0);

        // Fade-in animation
        FadeTransition fade = new FadeTransition(Duration.millis(2000), lblModifications);
        fade.setFromValue(0);
        fade.setToValue(1);

        // Play both at once
        ParallelTransition parallel = new ParallelTransition(slide, fade);
        parallel.play();
    }


    public void btnsignInOnAction(ActionEvent event) {
        navigateTo("/view/LoginPage.fxml");
    }

    public void btnSignUpOnAction(ActionEvent event) {
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
