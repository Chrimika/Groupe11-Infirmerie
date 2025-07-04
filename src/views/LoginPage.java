package views;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginPage extends Scene {
    private VBox loginForm;
    private ImageView loginImage;

    public LoginPage(Stage stage) {
        super(new StackPane(), 800, 600);
        StackPane root = (StackPane) getRoot();

        // Fond dégradé moderne
        createGradientBackground(root);

        // Container principal avec effet de glassmorphisme
        HBox mainContainer = createMainContainer();

        // Partie gauche - Image
        VBox leftSection = createImageSection();

        // Partie droite - Formulaire
        VBox rightSection = createFormSection(stage);

        mainContainer.getChildren().addAll(leftSection, rightSection);
        root.getChildren().add(mainContainer);

        // Animations d'entrée
        playEntranceAnimations();
    }

    private void createGradientBackground(StackPane root) {
        Rectangle background = new Rectangle(1200, 800);

        // Dégradé moderne vert-blanc
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, null,
                new Stop(0, Color.web("#0bcb95")),
                new Stop(0.7, Color.web("#7dd3c0")),
                new Stop(1, Color.web("#ffffff")));
        background.setFill(gradient);

        // Effet de flou pour le fond
        GaussianBlur blur = new GaussianBlur(2);
        background.setEffect(blur);

        root.getChildren().add(background);
    }

    private HBox createMainContainer() {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(0);
        container.setPrefSize(900, 550);
        container.setMaxSize(900, 550);

        // Effet glassmorphisme
        container.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.1);" +
                        "-fx-background-radius: 20;" +
                        "-fx-border-radius: 20;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-border-width: 1;");

        // Ombre portée élégante
        DropShadow shadow = new DropShadow();
        shadow.setRadius(30);
        shadow.setOffsetX(0);
        shadow.setOffsetY(10);
        shadow.setColor(Color.color(0, 0, 0, 0.1));
        container.setEffect(shadow);

        return container;
    }

    private VBox createImageSection() {
        VBox imageSection = new VBox();
        imageSection.setAlignment(Pos.CENTER);
        imageSection.setPrefSize(450, 550);
        imageSection.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.05);" +
                        "-fx-background-radius: 20 0 0 20;");

        try {
            // Image de connexion
            Image image = new Image("assets/login.png");
            loginImage = new ImageView(image);
            loginImage.setFitWidth(300);
            loginImage.setFitHeight(300);
            loginImage.setPreserveRatio(true);
            loginImage.setSmooth(true);

            // Effet d'ombre sur l'image
            DropShadow imageShadow = new DropShadow();
            imageShadow.setRadius(15);
            imageShadow.setColor(Color.color(0, 0, 0, 0.2));
            loginImage.setEffect(imageShadow);

            imageSection.getChildren().add(loginImage);
        } catch (Exception e) {
            // Placeholder si l'image n'est pas trouvée
            Label placeholder = new Label("🖼️");
            placeholder.setFont(Font.font("Arial", FontWeight.NORMAL, 80));
            placeholder.setTextFill(Color.WHITE);
            imageSection.getChildren().add(placeholder);
        }

        return imageSection;
    }

    private VBox createFormSection(Stage stage) {
        VBox formSection = new VBox();
        formSection.setAlignment(Pos.CENTER);
        formSection.setPrefSize(450, 550);
        formSection.setPadding(new Insets(60));
        formSection.setStyle("-fx-background-color: rgba(255, 255, 255, 0.95); -fx-background-radius: 0 20 20 0;");

        // Titre élégant
        Label title = new Label("Connexion");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#0bcb95"));
        title.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 2, 0, 0, 1);");

        Label subtitle = new Label("Connectez-vous à votre compte");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 16));
        subtitle.setTextFill(Color.web("#2c3e50"));

        // Champs de saisie modernes
        TextField username = createModernTextField("Nom d'utilisateur", "👤");
        PasswordField password = createModernPasswordField("Mot de passe", "🔒");

        // Bouton de connexion avec animation
        Button loginButton = createModernButton("Se connecter");

        // Ajout de l'action de connexion
        loginButton.setOnAction(e -> {
            String user = username.getText().trim();
            String pass = password.getText();
            if (user.isEmpty() || pass.isEmpty()) {
                showLoginError(loginButton, "Veuillez remplir tous les champs.");
                return;
            }
            // Connexion à la base pour vérifier l'utilisateur (patient ou medecin)
            java.sql.Connection conn = null;
            try {
                conn = utils.DBConnection.getConnection();
                // Vérifier d'abord si c'est un médecin
                String sqlMed = "SELECT * FROM medecin WHERE email = ? OR nom = ?";
                try (java.sql.PreparedStatement ps = conn.prepareStatement(sqlMed)) {
                    ps.setString(1, user);
                    ps.setString(2, user);
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String hashed = rs.getString("motdepasse");
                            if (hashed != null && hashed.equals(hashPassword(pass))) {
                                // Créer l'objet Medecin et rediriger
                                models.Medecin med = new models.Medecin();
                                med.setId(rs.getInt("id"));
                                med.setNom(rs.getString("nom"));
                                med.setPrenom(rs.getString("prenom"));
                                med.setSpecialite(rs.getString("specialite"));
                                med.setTelephone(rs.getString("telephone"));
                                med.setEmail(rs.getString("email"));
                                // Charger les rendez-vous du médecin
                                java.util.List<String[]> appointments = new java.util.ArrayList<>();
                                int nbPatients = 0;
                                int nbRdv = 0;
                                int nbUrgences = 0;
                                String tempsMoyen = "-";
                                try {
                                    // Charger les rendez-vous depuis la table rendezvous
                                    String sqlRdv = "SELECT patient_nom, jour, heure FROM rendezvous WHERE medecin_id = ? ORDER BY jour, heure";
                                    try (java.sql.PreparedStatement psRdv = conn.prepareStatement(sqlRdv)) {
                                        psRdv.setInt(1, med.getId());
                                        try (java.sql.ResultSet rsRdv = psRdv.executeQuery()) {
                                            while (rsRdv.next()) {
                                                String patient = rsRdv.getString("patient_nom");
                                                String jour = rsRdv.getString("jour");
                                                String heure = rsRdv.getString("heure");
                                                appointments.add(new String[] { patient, jour, heure });
                                                nbRdv++;
                                            }
                                        }
                                    }
                                    // Nombre de patients distincts
                                    String sqlPatients = "SELECT COUNT(DISTINCT patient_nom) as nb FROM rendezvous WHERE medecin_id = ?";
                                    try (java.sql.PreparedStatement psPat = conn.prepareStatement(sqlPatients)) {
                                        psPat.setInt(1, med.getId());
                                        try (java.sql.ResultSet rsPat = psPat.executeQuery()) {
                                            if (rsPat.next()) {
                                                nbPatients = rsPat.getInt("nb");
                                            }
                                        }
                                    }
                                    // Nombre d'urgences (exemple : état = 'urgence')
                                    String sqlUrg = "SELECT COUNT(*) as nb FROM rendezvous WHERE medecin_id = ? AND etat = 'urgence'";
                                    try (java.sql.PreparedStatement psUrg = conn.prepareStatement(sqlUrg)) {
                                        psUrg.setInt(1, med.getId());
                                        try (java.sql.ResultSet rsUrg = psUrg.executeQuery()) {
                                            if (rsUrg.next()) {
                                                nbUrgences = rsUrg.getInt("nb");
                                            }
                                        }
                                    }
                                    // Temps moyen (exemple fictif)
                                    tempsMoyen = "25min";
                                } catch (Exception ex) {
                                    // Valeurs par défaut si erreur
                                }
                                stage.setScene(new DoctorInterface(stage, med, appointments, nbPatients, nbRdv,
                                        nbUrgences, tempsMoyen));
                                return;
                            } else {
                                showLoginError(loginButton, "Mot de passe incorrect pour le médecin.");
                                return;
                            }
                        }
                    }
                }
                // Sinon, vérifier si c'est un patient
                String sqlPat = "SELECT * FROM patient WHERE email = ? OR nom = ?";
                try (java.sql.PreparedStatement ps = conn.prepareStatement(sqlPat)) {
                    ps.setString(1, user);
                    ps.setString(2, user);
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String hashed = rs.getString("motdepasse");
                            if (hashed != null && hashed.equals(hashPassword(pass))) {
                                // Créer l'objet Patient et rediriger
                                models.Patient pat = new models.Patient();
                                pat.setId(rs.getInt("id"));
                                pat.setNom(rs.getString("nom"));
                                pat.setPrenom(rs.getString("prenom"));
                                pat.setEmail(rs.getString("email"));
                                // ... autres champs si besoin
                                stage.setScene(new AppointmentManagement(pat));
                                return;
                            } else {
                                showLoginError(loginButton, "Mot de passe incorrect pour le patient.");
                                return;
                            }
                        }
                    }
                }
                // Aucun utilisateur trouvé
                showLoginError(loginButton, "Utilisateur non trouvé.");
            } catch (java.sql.SQLException sqlEx) {
                System.err.println("❌ ERREUR: Connexion à la base de données impossible : " + sqlEx.getMessage());
                showLoginError(loginButton,
                        "Impossible de se connecter à la base de données. Vérifiez votre connexion ou contactez l'administrateur.");
            } catch (Exception ex) {
                System.err.println("❌ ERREUR inattendue lors de la connexion : " + ex.getMessage());
                showLoginError(loginButton, "Erreur inattendue lors de la connexion. Veuillez réessayer.");
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Exception ignore) {
                    }
                }
            }
        });
        // Méthode utilitaire pour le hashage SHA-256 (identique à l'inscription)
        // (À placer dans la classe LoginPage)
        // Copié depuis PatientSignUp/MedecinSignUp pour cohérence
        // (Peut être factorisé dans un utilitaire si besoin)
        //
        // Affichage d'une erreur de connexion avec animation
        // (À placer dans la classe LoginPage)

        // Texte et bouton pour inscription
        Label noAccountLabel = new Label("Pas encore de compte ?");
        noAccountLabel.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 14));
        noAccountLabel.setTextFill(Color.web("#2c3e50"));

        Button signUpButton = new Button("S'inscrire");
        signUpButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        signUpButton.setTextFill(Color.web("#0bcb95"));
        signUpButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        signUpButton.setOnAction(e -> {
            stage.setScene(new ChoiceInscriptionView(stage));
        });

        // Espacement et arrangement
        VBox signUpSection = new VBox(5, noAccountLabel, signUpButton);
        signUpSection.setAlignment(Pos.CENTER);

        loginForm = new VBox(25);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.getChildren().addAll(
                new VBox(5, title, subtitle),
                new VBox(20, username, password),
                loginButton,
                signUpSection);

        formSection.getChildren().add(loginForm);
        return formSection;
    }

    // Méthode utilitaire pour le hashage SHA-256 (identique à l'inscription)
    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return null;
        }
    }

    // Affichage d'une erreur de connexion avec animation
    private void showLoginError(Button loginButton, String message) {
        javafx.animation.TranslateTransition shake = new javafx.animation.TranslateTransition(
                javafx.util.Duration.millis(100), loginButton);
        shake.setFromX(0);
        shake.setToX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();

        loginButton.setStyle(
                "-fx-background-color: #dc3545; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 30 15 30; " +
                        "-fx-background-radius: 25; " +
                        "-fx-cursor: hand;");

        javafx.animation.Timeline resetColor = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(2), ev -> {
                    loginButton.setStyle(
                            "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                                    "-fx-text-fill: white;" +
                                    "-fx-background-radius: 25;" +
                                    "-fx-border-radius: 25;" +
                                    "-fx-cursor: hand;" +
                                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);");
                }));
        resetColor.play();

        // Affichage d'une alerte
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    private TextField createModernTextField(String placeholder, String icon) {
        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setPrefHeight(50);
        field.setFont(Font.font("Segoe UI", 14));

        field.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-border-color: #e9ecef;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 0 20 0 20;" +
                        "-fx-text-fill: #2c3e50;" +
                        "-fx-prompt-text-fill: #95a5a6;");

        // Effet de focus
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                        "-fx-background-color: #ffffff;" +
                                "-fx-border-color: #0bcb95;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-radius: 10;" +
                                "-fx-background-radius: 10;" +
                                "-fx-padding: 0 20 0 20;" +
                                "-fx-text-fill: #2c3e50;" +
                                "-fx-prompt-text-fill: #95a5a6;" +
                                "-fx-effect: dropshadow(gaussian, rgba(11, 203, 149, 0.3), 10, 0, 0, 0);");
            } else {
                field.setStyle(
                        "-fx-background-color: #f8f9fa;" +
                                "-fx-border-color: #e9ecef;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-radius: 10;" +
                                "-fx-background-radius: 10;" +
                                "-fx-padding: 0 20 0 20;" +
                                "-fx-text-fill: #2c3e50;" +
                                "-fx-prompt-text-fill: #95a5a6;");
            }
        });

        return field;
    }

    private PasswordField createModernPasswordField(String placeholder, String icon) {
        PasswordField field = new PasswordField();
        field.setPromptText(placeholder);
        field.setPrefHeight(50);
        field.setFont(Font.font("Segoe UI", 14));

        field.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-border-color: #e9ecef;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 0 20 0 20;" +
                        "-fx-text-fill: #2c3e50;" +
                        "-fx-prompt-text-fill: #95a5a6;");

        // Effet de focus identique au TextField
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                        "-fx-background-color: #ffffff;" +
                                "-fx-border-color: #0bcb95;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-radius: 10;" +
                                "-fx-background-radius: 10;" +
                                "-fx-padding: 0 20 0 20;" +
                                "-fx-text-fill: #2c3e50;" +
                                "-fx-prompt-text-fill: #95a5a6;" +
                                "-fx-effect: dropshadow(gaussian, rgba(11, 203, 149, 0.3), 10, 0, 0, 0);");
            } else {
                field.setStyle(
                        "-fx-background-color: #f8f9fa;" +
                                "-fx-border-color: #e9ecef;" +
                                "-fx-border-width: 2;" +
                                "-fx-border-radius: 10;" +
                                "-fx-background-radius: 10;" +
                                "-fx-padding: 0 20 0 20;" +
                                "-fx-text-fill: #2c3e50;" +
                                "-fx-prompt-text-fill: #95a5a6;");
            }
        });

        return field;
    }

    private Button createModernButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(200, 50);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));

        // Style dégradé moderne
        button.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 25;" +
                        "-fx-border-radius: 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);");

        // Animations sur hover et clic
        button.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();

            button.setStyle(
                    "-fx-background-color: linear-gradient(to right, #5a67d8, #6b46c1);" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 25;" +
                            "-fx-border-radius: 25;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 12, 0, 0, 4);");
        });

        button.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            button.setStyle(
                    "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 25;" +
                            "-fx-border-radius: 25;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);");
        });

        button.setOnMousePressed(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(50), button);
            scale.setToX(0.95);
            scale.setToY(0.95);
            scale.play();
        });

        button.setOnMouseReleased(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(50), button);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });

        return button;
    }

    private void playEntranceAnimations() {
        // Animation de l'image (glissement depuis la gauche)
        if (loginImage != null) {
            TranslateTransition imageSlide = new TranslateTransition(Duration.millis(800), loginImage);
            imageSlide.setFromX(-300);
            imageSlide.setToX(0);

            FadeTransition imageFade = new FadeTransition(Duration.millis(800), loginImage);
            imageFade.setFromValue(0);
            imageFade.setToValue(1);

            imageSlide.play();
            imageFade.play();
        }

        // Animation du formulaire (glissement depuis la droite)
        TranslateTransition formSlide = new TranslateTransition(Duration.millis(800), loginForm);
        formSlide.setFromX(300);
        formSlide.setToX(0);

        FadeTransition formFade = new FadeTransition(Duration.millis(800), loginForm);
        formFade.setFromValue(0);
        formFade.setToValue(1);

        formSlide.play();
        formFade.play();
    }
}