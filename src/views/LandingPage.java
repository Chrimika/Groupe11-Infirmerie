package views;

import java.io.IOException;
import java.io.InputStream;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LandingPage extends Scene {

    private ImageView logoView;
    private Label title;
    private Label subtitle;
    private Button loginBtn;
    private VBox mainContent;

    public LandingPage() {
        super(new StackPane(), 800, 600);

        StackPane root = (StackPane) getRoot();
        setupBackground(root);
        setupContent(root);
        startAnimations();
    }

    private void setupBackground(StackPane root) {
        // Gradient de fond moderne
        LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0bcb95")),
                new Stop(0.5, Color.web("#0aa884")),
                new Stop(1, Color.web("#088773")));

        Rectangle background = new Rectangle(800, 600);
        background.setFill(gradient);

        // Formes décoratives animées
        createFloatingShapes(root);

        root.getChildren().add(background);
    }

    private void createFloatingShapes(StackPane root) {
        // Création de formes géométriques flottantes
        for (int i = 0; i < 5; i++) {
            Rectangle shape = new Rectangle(30 + i * 10, 30 + i * 10);
            shape.setFill(Color.rgb(255, 255, 255, 0.1));
            shape.setRotate(45);

            // Position aléatoire
            shape.setTranslateX((Math.random() - 0.5) * 600);
            shape.setTranslateY((Math.random() - 0.5) * 400);

            // Animation de rotation continue
            RotateTransition rotate = new RotateTransition(Duration.seconds(8 + i * 2), shape);
            rotate.setFromAngle(0);
            rotate.setToAngle(360);
            rotate.setCycleCount(Timeline.INDEFINITE);
            rotate.play();

            // Animation de flottement
            TranslateTransition float1 = new TranslateTransition(Duration.seconds(4 + i), shape);
            float1.setFromY(shape.getTranslateY());
            float1.setToY(shape.getTranslateY() + 20);
            float1.setCycleCount(Timeline.INDEFINITE);
            float1.setAutoReverse(true);
            float1.play();

            root.getChildren().add(shape);
        }
    }

    private void setupContent(StackPane root) {
        mainContent = new VBox(30);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setPadding(new Insets(50));

        // Configuration du logo avec effet
        setupLogo();

        // Configuration du titre
        setupTitle();

        // Configuration du sous-titre
        setupSubtitle();

        // Configuration du bouton
        setupLoginButton();

        mainContent.getChildren().addAll(logoView, title, subtitle, loginBtn);
        root.getChildren().add(mainContent);
    }

    private void setupLogo() {
        logoView = new ImageView();

        try {
            // Chemin CORRECT (supposant que Piol.png est dans src/main/resources/assets)
            InputStream stream = getClass().getResourceAsStream("/assets/Piol.png");

            if (stream == null) {
                throw new IOException("Resource not found! Check path: /assets/Piol.png");
            }

            Image logo = new Image("assets/Piol.png");
            logoView.setImage(logo);
            logoView.setFitWidth(500);
            logoView.setFitHeight(350);
            logoView.setPreserveRatio(true);
        } catch (Exception e) {
            System.err.println("Erreur de chargement du logo: " + e.getMessage());
            logoView = createModernPlaceholder(200, 200); // Assurez-vous que cette méthode existe
        }

        // ... (le reste de votre code avec les effets reste inchangé)
        DropShadow logoShadow = new DropShadow();
        logoShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        logoShadow.setRadius(15);
        logoShadow.setOffsetX(0);
        logoShadow.setOffsetY(8);
        logoView.setEffect(logoShadow);

        logoView.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), logoView);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();
        });

        logoView.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), logoView);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
    }

    private void setupTitle() {
        title = new Label("Infirmerie Scolaire");
        title.setStyle(
                "-fx-text-fill: white; " +
                        "-fx-font-size: 36px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-family: 'Arial', sans-serif;");

        // Effet d'ombre sur le texte
        DropShadow textShadow = new DropShadow();
        textShadow.setColor(Color.rgb(0, 0, 0, 0.5));
        textShadow.setRadius(3);
        textShadow.setOffsetX(0);
        textShadow.setOffsetY(2);
        title.setEffect(textShadow);
    }

    private void setupSubtitle() {
        subtitle = new Label("Gestion moderne et efficace de votre santé scolaire");
        subtitle.setStyle(
                "-fx-text-fill: rgba(255, 255, 255, 0.9); " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-style: italic;");
        subtitle.setMaxWidth(400);
        subtitle.setWrapText(true);
        subtitle.setAlignment(Pos.CENTER);
    }

    private void setupLoginButton() {
        loginBtn = new Button("Se connecter");
        loginBtn.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: #0bcb95; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 40 15 40; " +
                        "-fx-background-radius: 25; " +
                        "-fx-cursor: hand;");

        // Effet d'ombre sur le bouton
        DropShadow buttonShadow = new DropShadow();
        buttonShadow.setColor(Color.rgb(0, 0, 0, 0.2));
        buttonShadow.setRadius(10);
        buttonShadow.setOffsetX(0);
        buttonShadow.setOffsetY(5);
        loginBtn.setEffect(buttonShadow);

        // Redirection vers LoginPage
        loginBtn.setOnAction(e -> {
            Stage stage = (Stage) getWindow();
            stage.setScene(new LoginPage(stage));
        });

        // Animations hover
        loginBtn.setOnMouseEntered(e -> {
            loginBtn.setStyle(
                    "-fx-background-color: #f8f9fa; " +
                            "-fx-text-fill: #0bcb95; " +
                            "-fx-font-size: 18px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 15 40 15 40; " +
                            "-fx-background-radius: 25; " +
                            "-fx-cursor: hand;");

            ScaleTransition scale = new ScaleTransition(Duration.millis(150), loginBtn);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });

        loginBtn.setOnMouseExited(e -> {
            loginBtn.setStyle(
                    "-fx-background-color: white; " +
                            "-fx-text-fill: #0bcb95; " +
                            "-fx-font-size: 18px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 15 40 15 40; " +
                            "-fx-background-radius: 25; " +
                            "-fx-cursor: hand;");

            ScaleTransition scale = new ScaleTransition(Duration.millis(150), loginBtn);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        // Animation de clic
        loginBtn.setOnMousePressed(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), loginBtn);
            scale.setToX(0.95);
            scale.setToY(0.95);
            scale.play();
        });

        loginBtn.setOnMouseReleased(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), loginBtn);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });
    }

    private ImageView createModernPlaceholder(double width, double height) {
        ImageView placeholder = new ImageView();
        placeholder.setFitWidth(width);
        placeholder.setFitHeight(height);

        // Création d'une image placeholder moderne
        Rectangle rect = new Rectangle(width, height);
        rect.setFill(Color.rgb(240, 240, 240, 0.8));
        rect.setStroke(Color.web("#0bcb95"));
        rect.setStrokeWidth(3);
        rect.setArcWidth(20);
        rect.setArcHeight(20);

        return placeholder;
    }

    private void startAnimations() {
        // Animation d'entrée pour le logo
        logoView.setOpacity(0);
        logoView.setScaleX(0.5);
        logoView.setScaleY(0.5);

        FadeTransition logoFade = new FadeTransition(Duration.millis(800), logoView);
        logoFade.setToValue(1);

        ScaleTransition logoScale = new ScaleTransition(Duration.millis(800), logoView);
        logoScale.setToX(1);
        logoScale.setToY(1);

        ParallelTransition logoEntry = new ParallelTransition(logoFade, logoScale);
        logoEntry.setDelay(Duration.millis(200));

        // Animation d'entrée pour le titre
        title.setOpacity(0);
        title.setTranslateY(30);

        FadeTransition titleFade = new FadeTransition(Duration.millis(600), title);
        titleFade.setToValue(1);

        TranslateTransition titleSlide = new TranslateTransition(Duration.millis(600), title);
        titleSlide.setToY(0);

        ParallelTransition titleEntry = new ParallelTransition(titleFade, titleSlide);
        titleEntry.setDelay(Duration.millis(600));

        // Animation d'entrée pour le sous-titre
        subtitle.setOpacity(0);
        subtitle.setTranslateY(20);

        FadeTransition subtitleFade = new FadeTransition(Duration.millis(600), subtitle);
        subtitleFade.setToValue(1);

        TranslateTransition subtitleSlide = new TranslateTransition(Duration.millis(600), subtitle);
        subtitleSlide.setToY(0);

        ParallelTransition subtitleEntry = new ParallelTransition(subtitleFade, subtitleSlide);
        subtitleEntry.setDelay(Duration.millis(900));

        // Animation d'entrée pour le bouton
        loginBtn.setOpacity(0);
        loginBtn.setTranslateY(20);

        FadeTransition buttonFade = new FadeTransition(Duration.millis(600), loginBtn);
        buttonFade.setToValue(1);

        TranslateTransition buttonSlide = new TranslateTransition(Duration.millis(600), loginBtn);
        buttonSlide.setToY(0);

        ParallelTransition buttonEntry = new ParallelTransition(buttonFade, buttonSlide);
        buttonEntry.setDelay(Duration.millis(1200));

        // Animation de pulsation continue pour le logo
        ScaleTransition logoPulse = new ScaleTransition(Duration.seconds(2), logoView);
        logoPulse.setFromX(1.0);
        logoPulse.setFromY(1.0);
        logoPulse.setToX(1.02);
        logoPulse.setToY(1.02);
        logoPulse.setCycleCount(Timeline.INDEFINITE);
        logoPulse.setAutoReverse(true);

        // Séquence d'animations
        SequentialTransition sequence = new SequentialTransition(
                logoEntry,
                new PauseTransition(Duration.millis(200)),
                titleEntry,
                new PauseTransition(Duration.millis(100)),
                subtitleEntry,
                new PauseTransition(Duration.millis(100)),
                buttonEntry);

        sequence.setOnFinished(e -> logoPulse.play());
        sequence.play();
    }
}