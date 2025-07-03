package views;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import views.MedecinSignUp;
import views.PatientSignUp;

public class ChoiceInscriptionView extends Scene {
    private static final String DOCTOR_ICON = "/assets/doctor-icon.png";
    private static final String PATIENT_ICON = "/assets/medical-icon.png";
    private final Stage primaryStage;

    public ChoiceInscriptionView(Stage primaryStage) {
        super(new StackPane(), 800, 600);
        this.primaryStage = primaryStage;

        StackPane root = (StackPane) getRoot();
        root.setStyle("-fx-background-color: white;");

        initializeUI(root);
    }

    private void initializeUI(StackPane root) {
        // Titre
        Text title = new Text("Choisissez votre profil");
        title.setFont(Font.font(24));
        title.setFill(Color.web("#0bcb95"));

        // Boutons de choix
        Button doctorBtn = createProfileButton("Médecin", DOCTOR_ICON);
        Button patientBtn = createProfileButton("Patient", PATIENT_ICON);

        doctorBtn.setOnAction(e -> navigateTo(new MedecinSignUp(primaryStage), "Inscription Médecin"));
        patientBtn.setOnAction(e -> navigateTo(new PatientSignUp(primaryStage), "Inscription Patient"));

        HBox choicesBox = new HBox(40, doctorBtn, patientBtn);
        choicesBox.setAlignment(Pos.CENTER);

        // Bouton Retour
        Button backButton = new Button("Retour");
        backButton.setStyle(
                "-fx-background-color: #6c757d; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;"
        );
        backButton.setOnAction(e -> navigateTo(new LoginPage(), "Connexion"));

        VBox mainLayout = new VBox(40, title, choicesBox, backButton);
        mainLayout.setAlignment(Pos.CENTER);

        root.getChildren().add(mainLayout);
    }

    private Button createProfileButton(String text, String iconPath) {
        ImageView icon = loadImage(iconPath, 150);
        Button btn = new Button(text, icon);

        btn.setStyle("-fx-background-color: white; -fx-text-fill: #0bcb95; -fx-font-size: 18px; "
                + "-fx-border-color: #0bcb95; -fx-border-width: 2px; -fx-border-radius: 5px; "
                + "-fx-padding: 20px; -fx-content-display: top;");
        btn.setMinSize(250, 300);

        // Animation de survol
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle() + "-fx-effect: dropshadow(gaussian, rgba(11,203,149,0.5), 10, 0, 0, 0);"));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace("-fx-effect: dropshadow(gaussian, rgba(11,203,149,0.5), 10, 0, 0, 0);", "")));

        return btn;
    }

    private ImageView loadImage(String resourcePath, double size) {
        try {
            Image image = new Image(getClass().getResourceAsStream(resourcePath));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            imageView.setPreserveRatio(true);
            return imageView;
        } catch (Exception e) {
            System.err.println("Erreur de chargement de l'image: " + resourcePath);
            return createPlaceholder(size);
        }
    }

    private ImageView createPlaceholder(double size) {
        ImageView placeholder = new ImageView();
        placeholder.setFitWidth(size);
        placeholder.setFitHeight(size);
        placeholder.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #0bcb95;");
        return placeholder;
    }

    private void navigateTo(Scene nextScene, String title) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            primaryStage.setTitle(title);
            primaryStage.setScene(nextScene);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), nextScene.getRoot());
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
}