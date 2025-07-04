package views;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
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
import models.Patient;
import utils.DBConnection;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class PatientSignUp extends Scene {
    private TextField nomField, prenomField, sexeField, numDossierField;
    private DatePicker dateNaissancePicker;
    private TextField adresseField, telField, emailField;
    private PasswordField passwordField;
    private TextArea allergiesArea, antecedentsArea;
    private Button submitBtn;
    private ImageView medicalImageView;
    private ScrollPane formScrollPane;

    public PatientSignUp(Stage primaryStage) {
        super(new StackPane(), 800, 600);

        StackPane root = (StackPane) getRoot();
        setupBackground(root);
        setupMainContent(root);
        startAnimations();
    }

    private void setupBackground(StackPane root) {
        // Gradient de fond moderne
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#f8f9fa")),
                new Stop(0.5, Color.web("#e9ecef")),
                new Stop(1, Color.web("#dee2e6")));

        Rectangle background = new Rectangle(1000, 700);
        background.setFill(gradient);

        // Formes décoratives subtiles
        createFloatingShapes(root);

        root.getChildren().add(background);
    }

    private void createFloatingShapes(StackPane root) {
        // Création de formes géométriques flottantes subtiles
        for (int i = 0; i < 3; i++) {
            Rectangle shape = new Rectangle(40 + i * 15, 40 + i * 15);
            shape.setFill(Color.rgb(11, 203, 149, 0.05));
            shape.setRotate(45);

            // Position dans les coins
            shape.setTranslateX(-400 + i * 200);
            shape.setTranslateY(-250 + i * 150);

            // Animation de rotation lente
            RotateTransition rotate = new RotateTransition(Duration.seconds(15 + i * 5), shape);
            rotate.setFromAngle(0);
            rotate.setToAngle(360);
            rotate.setCycleCount(Timeline.INDEFINITE);
            rotate.play();

            root.getChildren().add(shape);
        }
    }

    private void setupMainContent(StackPane root) {
        HBox mainContainer = new HBox(40);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setMaxWidth(900);

        // Section gauche - Image avec lévitation
        VBox leftSection = setupLeftSection();

        // Section droite - Formulaire
        VBox rightSection = setupRightSection();

        mainContainer.getChildren().addAll(leftSection, rightSection);
        root.getChildren().add(mainContainer);
    }

    private VBox setupLeftSection() {
        VBox leftSection = new VBox(20);
        leftSection.setAlignment(Pos.CENTER);
        leftSection.setPrefWidth(300);

        // Titre de bienvenue
        Label welcomeTitle = new Label("Rejoignez-nous");
        welcomeTitle.setStyle(
                "-fx-text-fill: #0bcb95; " +
                        "-fx-font-size: 28px; " +
                        "-fx-font-weight: bold;");

        Label welcomeSubtitle = new Label("Créez votre profil patient en quelques étapes simples");
        welcomeSubtitle.setStyle(
                "-fx-text-fill: #6c757d; " +
                        "-fx-font-size: 14px; " +
                        "-fx-text-alignment: center;");
        welcomeSubtitle.setWrapText(true);
        welcomeSubtitle.setMaxWidth(280);

        // Image médicale avec lévitation
        medicalImageView = new ImageView();
        setupMedicalImage();

        leftSection.getChildren().addAll(welcomeTitle, welcomeSubtitle, medicalImageView);
        return leftSection;
    }

    private void setupMedicalImage() {
        try {
            // Vous pouvez remplacer par votre image médicale
            Image medicalImage = new Image(getClass().getResourceAsStream("/assets/medical-icon.png"));
            medicalImageView.setImage(medicalImage);
        } catch (Exception e) {
            System.err.println("Erreur de chargement de l'image médicale: " + e.getMessage());
            medicalImageView = createMedicalPlaceholder();
        }

        medicalImageView.setFitWidth(200);
        medicalImageView.setFitHeight(200);
        medicalImageView.setPreserveRatio(true);

        // Effet d'ombre pour la lévitation
        DropShadow levitationShadow = new DropShadow();
        levitationShadow.setColor(Color.rgb(11, 203, 149, 0.3));
        levitationShadow.setRadius(20);
        levitationShadow.setOffsetX(0);
        levitationShadow.setOffsetY(10);
        medicalImageView.setEffect(levitationShadow);
    }

    private ImageView createMedicalPlaceholder() {
        ImageView placeholder = new ImageView();
        placeholder.setFitWidth(200);
        placeholder.setFitHeight(200);

        // Création d'un placeholder médical stylisé
        Rectangle rect = new Rectangle(200, 200);
        rect.setFill(Color.rgb(11, 203, 149, 0.1));
        rect.setStroke(Color.web("#0bcb95"));
        rect.setStrokeWidth(2);
        rect.setArcWidth(20);
        rect.setArcHeight(20);

        return placeholder;
    }

    private VBox setupRightSection() {
        VBox rightSection = new VBox(20);
        rightSection.setPrefWidth(500);

        // Titre du formulaire
        Label formTitle = new Label("Inscription Patient");
        formTitle.setStyle(
                "-fx-text-fill: #212529; " +
                        "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold;");

        // Bouton Retour
        Button backButton = new Button("Retour");
        backButton.setStyle(
                "-fx-background-color: #6c757d; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-radius: 5; " +
                        "-fx-cursor: hand;");
        backButton.setOnAction(e -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new ChoiceInscriptionView(stage));
        });

        // Container du formulaire avec scroll
        VBox formContainer = setupFormContainer();

        formScrollPane = new ScrollPane(formContainer);
        formScrollPane.setFitToWidth(true);
        formScrollPane.setFitToHeight(true);
        formScrollPane.setPrefHeight(500);
        formScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        formScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        formScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        rightSection.getChildren().addAll(formTitle, backButton, formScrollPane);
        return rightSection;
    }

    private VBox setupFormContainer() {
        VBox formContainer = new VBox(20);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Section 1: Informations personnelles
        VBox personalSection = createFormSection("Informations Personnelles", createPersonalFields());

        // Section 2: Contact
        VBox contactSection = createFormSection("Informations de Contact", createContactFields());

        // Section 3: Informations médicales
        VBox medicalSection = createFormSection("Informations Médicales", createMedicalFields());

        // Bouton de soumission
        setupSubmitButton();

        formContainer.getChildren().addAll(personalSection, contactSection, medicalSection, submitBtn);
        return formContainer;
    }

    private VBox createFormSection(String title, VBox fields) {
        VBox section = new VBox(15);

        Label sectionTitle = new Label(title);
        sectionTitle.setStyle(
                "-fx-text-fill: #0bcb95; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold;");

        section.getChildren().addAll(sectionTitle, fields);
        return section;
    }

    private VBox createPersonalFields() {
        VBox fields = new VBox(10);

        nomField = createStyledTextField("Nom");
        prenomField = createStyledTextField("Prénom");

        dateNaissancePicker = new DatePicker();
        dateNaissancePicker.setValue(LocalDate.now().minusYears(20));
        dateNaissancePicker.setPromptText("Date de naissance");
        styleControl(dateNaissancePicker);

        sexeField = createStyledTextField("Sexe");
        numDossierField = createStyledTextField("Numéro de dossier");

        fields.getChildren().addAll(
                createFieldWithLabel("Nom *", nomField),
                createFieldWithLabel("Prénom *", prenomField),
                createFieldWithLabel("Date de naissance *", dateNaissancePicker),
                createFieldWithLabel("Sexe", sexeField),
                createFieldWithLabel("Numéro de dossier", numDossierField));

        return fields;
    }

    private VBox createContactFields() {
        VBox fields = new VBox(10);

        adresseField = createStyledTextField("Adresse");
        telField = createStyledTextField("Téléphone");
        emailField = createStyledTextField("Email");
        passwordField = createStyledPasswordField("Mot de passe");

        fields.getChildren().addAll(
                createFieldWithLabel("Adresse", adresseField),
                createFieldWithLabel("Téléphone", telField),
                createFieldWithLabel("Email *", emailField),
                createFieldWithLabel("Mot de passe *", passwordField));

        return fields;
    }

    private VBox createMedicalFields() {
        VBox fields = new VBox(10);

        allergiesArea = createStyledTextArea("Allergies connues");
        antecedentsArea = createStyledTextArea("Antécédents médicaux");

        fields.getChildren().addAll(
                createFieldWithLabel("Allergies", allergiesArea),
                createFieldWithLabel("Antécédents médicaux", antecedentsArea));

        return fields;
    }

    private VBox createFieldWithLabel(String labelText, Control field) {
        VBox fieldContainer = new VBox(5);

        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: #495057; -fx-font-size: 14px; -fx-font-weight: 500;");

        fieldContainer.getChildren().addAll(label, field);
        return fieldContainer;
    }

    private TextField createStyledTextField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        styleControl(field);
        return field;
    }

    private PasswordField createStyledPasswordField(String promptText) {
        PasswordField field = new PasswordField();
        field.setPromptText(promptText);
        styleControl(field);
        return field;
    }

    private TextArea createStyledTextArea(String promptText) {
        TextArea area = new TextArea();
        area.setPromptText(promptText);
        area.setPrefRowCount(3);
        styleControl(area);
        return area;
    }

    private void styleControl(Control control) {
        control.setStyle(
                "-fx-background-color: #f8f9fa; " +
                        "-fx-border-color: #dee2e6; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 10; " +
                        "-fx-font-size: 14px;");

        // Effet focus
        control.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                control.setStyle(
                        "-fx-background-color: white; " +
                                "-fx-border-color: #0bcb95; " +
                                "-fx-border-width: 2; " +
                                "-fx-border-radius: 8; " +
                                "-fx-background-radius: 8; " +
                                "-fx-padding: 10; " +
                                "-fx-font-size: 14px;");
            } else {
                control.setStyle(
                        "-fx-background-color: #f8f9fa; " +
                                "-fx-border-color: #dee2e6; " +
                                "-fx-border-width: 1; " +
                                "-fx-border-radius: 8; " +
                                "-fx-background-radius: 8; " +
                                "-fx-padding: 10; " +
                                "-fx-font-size: 14px;");
            }
        });
    }

    private void setupSubmitButton() {
        submitBtn = new Button("Créer mon profil");
        submitBtn.setStyle(
                "-fx-background-color: #0bcb95; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 30 15 30; " +
                        "-fx-background-radius: 25; " +
                        "-fx-cursor: hand;");

        submitBtn.setMaxWidth(Double.MAX_VALUE);

        // Effet d'ombre
        DropShadow buttonShadow = new DropShadow();
        buttonShadow.setColor(Color.rgb(11, 203, 149, 0.3));
        buttonShadow.setRadius(8);
        buttonShadow.setOffsetX(0);
        buttonShadow.setOffsetY(4);
        submitBtn.setEffect(buttonShadow);

        // Animations hover
        submitBtn.setOnMouseEntered(e -> {
            submitBtn.setStyle(
                    "-fx-background-color: #0aa884; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 16px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 15 30 15 30; " +
                            "-fx-background-radius: 25; " +
                            "-fx-cursor: hand;");

            ScaleTransition scale = new ScaleTransition(Duration.millis(150), submitBtn);
            scale.setToX(1.02);
            scale.setToY(1.02);
            scale.play();
        });

        submitBtn.setOnMouseExited(e -> {
            submitBtn.setStyle(
                    "-fx-background-color: #0bcb95; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 16px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 15 30 15 30; " +
                            "-fx-background-radius: 25; " +
                            "-fx-cursor: hand;");

            ScaleTransition scale = new ScaleTransition(Duration.millis(150), submitBtn);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        submitBtn.setOnAction(e -> {
            if (validateForm()) {
                Patient patient = createPatient();
                String password = passwordField.getText();

                if (savePatientToDatabase(patient, password)) {
                    System.out.println("✅ Patient enregistré avec succès");
                    showSuccessAnimation();
                } else {
                    System.out.println("❌ Échec de l'enregistrement du patient");
                    showDatabaseError();
                }
            } else {
                showValidationError();
            }
        });
    }

    private boolean savePatientToDatabase(Patient patient, String password) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Démarrer une transaction

            String hashedPassword = hashPassword(password);
            if (hashedPassword.length() > 45) {
                hashedPassword = hashedPassword.substring(0, 45);
            }

            // 1. Insérer dans la table utilisateur
            String sqlUser = "INSERT INTO utilisateur (email, mot_de_passe, role) VALUES (?, ?, ?)";

            try (PreparedStatement pstmtUser = conn.prepareStatement(sqlUser,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmtUser.setString(1, patient.getEmail());
                pstmtUser.setString(2, hashedPassword);
                pstmtUser.setString(3, "patient");
                int userAffected = pstmtUser.executeUpdate();

                if (userAffected == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // 2. Insérer dans la table patient
            String sqlPatient = "INSERT INTO patient ("
                    + "nom, prenom, date_naissance, numero_dossier, adresse, telephone, "
                    + "email, allergies, antecedents, mot_passe) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmtPatient = conn.prepareStatement(sqlPatient)) {
                pstmtPatient.setString(1, patient.getNom());
                pstmtPatient.setString(2, patient.getPrenom());
                pstmtPatient.setDate(3, java.sql.Date.valueOf(patient.getDateNaissance()));
                pstmtPatient.setString(4, patient.getNumeroDossier());
                pstmtPatient.setString(5, patient.getAdresse());
                pstmtPatient.setString(6, patient.getTelephone());
                pstmtPatient.setString(7, patient.getEmail());
                pstmtPatient.setString(8, patient.getAllergies());
                pstmtPatient.setString(9, patient.getAntecedents());
                pstmtPatient.setString(10, hashedPassword);

                int patientAffected = pstmtPatient.executeUpdate();
                if (patientAffected == 0) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit(); // Valider la transaction
            return true;

        } catch (SQLException ex) {
            System.err.println("Erreur SQL: " + ex.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Annuler en cas d'erreur
                } catch (SQLException ex1) {
                    System.err.println("Erreur de rollback: " + ex1.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Rétablir le mode auto-commit
                    conn.close();
                } catch (SQLException ex) {
                    System.err.println("Erreur de fermeture: " + ex.getMessage());
                }
            }
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return password; // Fallback (non sécurisé)
        }
    }

    private void startAnimations() {
        // Animation de lévitation pour l'image
        TranslateTransition levitation = new TranslateTransition(Duration.seconds(3), medicalImageView);
        levitation.setFromY(0);
        levitation.setToY(-15);
        levitation.setCycleCount(Timeline.INDEFINITE);
        levitation.setAutoReverse(true);
        levitation.play();

        // Animation d'entrée pour le formulaire
        formScrollPane.setOpacity(0);
        formScrollPane.setTranslateX(50);

        FadeTransition formFade = new FadeTransition(Duration.millis(800), formScrollPane);
        formFade.setToValue(1);

        TranslateTransition formSlide = new TranslateTransition(Duration.millis(800), formScrollPane);
        formSlide.setToX(0);

        ParallelTransition formEntry = new ParallelTransition(formFade, formSlide);
        formEntry.setDelay(Duration.millis(400));
        formEntry.play();
    }

    private boolean validateForm() {
        return !nomField.getText().trim().isEmpty() &&
                !prenomField.getText().trim().isEmpty() &&
                dateNaissancePicker.getValue() != null &&
                !emailField.getText().trim().isEmpty() &&
                !passwordField.getText().isEmpty();
    }

    private void showValidationError() {
        // Animation de secousse pour indiquer l'erreur
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), submitBtn);
        shake.setFromX(0);
        shake.setToX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.play();

        // Changer temporairement la couleur du bouton
        submitBtn.setStyle(
                "-fx-background-color: #dc3545; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 30 15 30; " +
                        "-fx-background-radius: 25; " +
                        "-fx-cursor: hand;");

        Timeline resetColor = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            submitBtn.setStyle(
                    "-fx-background-color: #0bcb95; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 16px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 15 30 15 30; " +
                            "-fx-background-radius: 25; " +
                            "-fx-cursor: hand;");
        }));
        resetColor.play();
    }

    private void showDatabaseError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de base de données");
        alert.setHeaderText("Échec de l'enregistrement");
        alert.setContentText("Une erreur est survenue lors de l'enregistrement. Veuillez réessayer.");
        alert.showAndWait();
    }

    private void showSuccessAnimation() {
        // Animation de succès
        ScaleTransition success = new ScaleTransition(Duration.millis(200), submitBtn);
        success.setFromX(1.0);
        success.setFromY(1.0);
        success.setToX(1.1);
        success.setToY(1.1);
        success.setAutoReverse(true);
        success.setCycleCount(2);
        success.play();

        // Redirection personnalisée vers la prise de rendez-vous avec le patient
        // connecté
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            Stage stage = (Stage) submitBtn.getScene().getWindow();
            // Rediriger vers AppointmentManagement avec le patient connecté
            stage.setScene(new AppointmentManagement(createPatient()));
        }));
        timeline.play();
    }

    private Patient createPatient() {
        Patient patient = new Patient();
        patient.setNom(nomField.getText().trim());
        patient.setPrenom(prenomField.getText().trim());
        patient.setDateNaissance(dateNaissancePicker.getValue());
        patient.setSexe(sexeField.getText().trim());
        patient.setNumeroDossier(numDossierField.getText().trim());
        patient.setAdresse(adresseField.getText().trim());
        patient.setTelephone(telField.getText().trim());
        patient.setEmail(emailField.getText().trim());
        patient.setAllergies(allergiesArea.getText().trim());
        patient.setAntecedents(antecedentsArea.getText().trim());
        return patient;
    }
}