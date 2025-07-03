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
import models.Medecin;

import java.util.HashSet;
import java.util.Set;

public class MedecinSignUp extends Scene {
    private TextField nomField, prenomField, specialiteField, telField, emailField;
    private ToggleButton lundiBtn, mardiBtn, mercrediBtn, jeudiBtn, vendrediBtn, samediBtn;
    private CheckBox estActifCheck;
    private Button submitBtn;
    private ImageView doctorImageView;
    private ScrollPane formScrollPane;

    public MedecinSignUp(Stage primaryStage) {
        super(new StackPane(), 800, 600);

        StackPane root = (StackPane) getRoot();
        setupBackground(root);
        setupMainContent(root);
        startAnimations();
    }

    private void setupBackground(StackPane root) {
        // Gradient de fond professionnel
        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#f1f3f4")),
                new Stop(0.5, Color.web("#e8eaed")),
                new Stop(1, Color.web("#dadce0"))
        );

        Rectangle background = new Rectangle(1000, 700);
        background.setFill(gradient);

        // Formes décoratives médicales
        createMedicalShapes(root);

        root.getChildren().add(background);
    }

    private void createMedicalShapes(StackPane root) {
        // Création de croix médicales flottantes
        for (int i = 0; i < 4; i++) {
            VBox cross = createMedicalCross(20 + i * 5);
            cross.setOpacity(0.05);

            // Position aléatoire
            cross.setTranslateX(-400 + i * 250);
            cross.setTranslateY(-250 + i * 120);

            // Animation de rotation lente
            RotateTransition rotate = new RotateTransition(Duration.seconds(20 + i * 5), cross);
            rotate.setFromAngle(0);
            rotate.setToAngle(360);
            rotate.setCycleCount(Timeline.INDEFINITE);
            rotate.play();

            // Animation de flottement
            TranslateTransition float1 = new TranslateTransition(Duration.seconds(6 + i * 2), cross);
            float1.setFromY(cross.getTranslateY());
            float1.setToY(cross.getTranslateY() + 30);
            float1.setCycleCount(Timeline.INDEFINITE);
            float1.setAutoReverse(true);
            float1.play();

            root.getChildren().add(cross);
        }
    }

    private VBox createMedicalCross(double size) {
        VBox cross = new VBox();
        cross.setAlignment(Pos.CENTER);

        Rectangle vertical = new Rectangle(size/3, size);
        vertical.setFill(Color.web("#0bcb95"));

        Rectangle horizontal = new Rectangle(size, size/3);
        horizontal.setFill(Color.web("#0bcb95"));

        StackPane crossShape = new StackPane();
        crossShape.getChildren().addAll(vertical, horizontal);

        cross.getChildren().add(crossShape);
        return cross;
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
        Label welcomeTitle = new Label("Espace Médecin");
        welcomeTitle.setStyle(
                "-fx-text-fill: #0bcb95; " +
                        "-fx-font-size: 28px; " +
                        "-fx-font-weight: bold;"
        );

        Label welcomeSubtitle = new Label("Rejoignez notre équipe médicale et contribuez à la santé de nos patients");
        welcomeSubtitle.setStyle(
                "-fx-text-fill: #6c757d; " +
                        "-fx-font-size: 14px; " +
                        "-fx-text-alignment: center;"
        );
        welcomeSubtitle.setWrapText(true);
        welcomeSubtitle.setMaxWidth(280);

        // Image de médecin avec lévitation
        doctorImageView = new ImageView();
        setupDoctorImage();

        leftSection.getChildren().addAll(welcomeTitle, welcomeSubtitle, doctorImageView);
        return leftSection;
    }

    private void setupDoctorImage() {
        try {
            // Vous pouvez remplacer par votre image de médecin
            Image doctorImage = new Image(getClass().getResourceAsStream("/assets/doctor-icon.png"));
            doctorImageView.setImage(doctorImage);
        } catch (Exception e) {
            System.err.println("Erreur de chargement de l'image médecin: " + e.getMessage());
            doctorImageView = createDoctorPlaceholder();
        }

        doctorImageView.setFitWidth(220);
        doctorImageView.setFitHeight(220);
        doctorImageView.setPreserveRatio(true);

        // Effet d'ombre pour la lévitation
        DropShadow levitationShadow = new DropShadow();
        levitationShadow.setColor(Color.rgb(11, 203, 149, 0.4));
        levitationShadow.setRadius(25);
        levitationShadow.setOffsetX(0);
        levitationShadow.setOffsetY(12);
        doctorImageView.setEffect(levitationShadow);
    }

    private ImageView createDoctorPlaceholder() {
        ImageView placeholder = new ImageView();
        placeholder.setFitWidth(220);
        placeholder.setFitHeight(220);

        // Création d'un placeholder médical stylisé
        Rectangle rect = new Rectangle(220, 220);
        rect.setFill(Color.rgb(11, 203, 149, 0.1));
        rect.setStroke(Color.web("#0bcb95"));
        rect.setStrokeWidth(3);
        rect.setArcWidth(25);
        rect.setArcHeight(25);

        return placeholder;
    }

    private VBox setupRightSection() {
        VBox rightSection = new VBox(20);
        rightSection.setPrefWidth(500);

        // Titre du formulaire
        Label formTitle = new Label("Inscription Médecin");
        formTitle.setStyle(
                "-fx-text-fill: #212529; " +
                        "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold;"
        );

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
        backButton.setOnAction(e -> {
            Stage stage = (Stage) getWindow();
            stage.setScene(new ChoiceInscriptionView(stage));
        });

        // Container du formulaire avec scroll
        VBox formContainer = setupFormContainer();

        formScrollPane = new ScrollPane(formContainer);
        formScrollPane.setFitToWidth(true);
        formScrollPane.setFitToHeight(true);
        formScrollPane.setPrefHeight(520);
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
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        // Section 1: Informations professionnelles
        VBox professionalSection = createFormSection("Informations Professionnelles", createProfessionalFields());

        // Section 2: Contact
        VBox contactSection = createFormSection("Informations de Contact", createContactFields());

        // Section 3: Planning
        VBox scheduleSection = createFormSection("Planning de Travail", createScheduleFields());

        // Section 4: Statut
        VBox statusSection = createFormSection("Statut", createStatusFields());

        // Bouton de soumission
        setupSubmitButton();

        formContainer.getChildren().addAll(professionalSection, contactSection, scheduleSection, statusSection, submitBtn);
        return formContainer;
    }

    private VBox createFormSection(String title, VBox fields) {
        VBox section = new VBox(15);

        Label sectionTitle = new Label(title);
        sectionTitle.setStyle(
                "-fx-text-fill: #0bcb95; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold;"
        );

        section.getChildren().addAll(sectionTitle, fields);
        return section;
    }

    private VBox createProfessionalFields() {
        VBox fields = new VBox(10);

        nomField = createStyledTextField("Nom");
        prenomField = createStyledTextField("Prénom");
        specialiteField = createStyledTextField("Spécialité");

        fields.getChildren().addAll(
                createFieldWithLabel("Nom *", nomField),
                createFieldWithLabel("Prénom *", prenomField),
                createFieldWithLabel("Spécialité *", specialiteField)
        );

        return fields;
    }

    private VBox createContactFields() {
        VBox fields = new VBox(10);

        telField = createStyledTextField("Téléphone");
        emailField = createStyledTextField("Email");

        fields.getChildren().addAll(
                createFieldWithLabel("Téléphone", telField),
                createFieldWithLabel("Email", emailField)
        );

        return fields;
    }

    private VBox createScheduleFields() {
        VBox fields = new VBox(15);

        Label scheduleLabel = new Label("Sélectionnez vos jours de travail *");
        scheduleLabel.setStyle("-fx-text-fill: #495057; -fx-font-size: 14px; -fx-font-weight: 500;");

        // Container pour les jours avec disposition moderne
        GridPane daysGrid = new GridPane();
        daysGrid.setHgap(10);
        daysGrid.setVgap(10);
        daysGrid.setAlignment(Pos.CENTER);

        // Création des boutons toggle stylisés pour les jours
        lundiBtn = createDayToggleButton("Lundi", "LUN");
        mardiBtn = createDayToggleButton("Mardi", "MAR");
        mercrediBtn = createDayToggleButton("Mercredi", "MER");
        jeudiBtn = createDayToggleButton("Jeudi", "JEU");
        vendrediBtn = createDayToggleButton("Vendredi", "VEN");
        samediBtn = createDayToggleButton("Samedi", "SAM");

        // Disposition en grille 3x2
        daysGrid.add(lundiBtn, 0, 0);
        daysGrid.add(mardiBtn, 1, 0);
        daysGrid.add(mercrediBtn, 2, 0);
        daysGrid.add(jeudiBtn, 0, 1);
        daysGrid.add(vendrediBtn, 1, 1);
        daysGrid.add(samediBtn, 2, 1);

        fields.getChildren().addAll(scheduleLabel, daysGrid);
        return fields;
    }

    private ToggleButton createDayToggleButton(String fullName, String shortName) {
        ToggleButton dayBtn = new ToggleButton(shortName);
        dayBtn.setPrefSize(80, 60);
        dayBtn.setStyle(
                "-fx-background-color: #f8f9fa; " +
                        "-fx-border-color: #dee2e6; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 12; " +
                        "-fx-background-radius: 12; " +
                        "-fx-text-fill: #6c757d; " +
                        "-fx-font-size: 12px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand;"
        );

        // Tooltip pour le nom complet
        dayBtn.setTooltip(new Tooltip(fullName));

        // Animation de sélection
        dayBtn.setOnAction(e -> {
            if (dayBtn.isSelected()) {
                dayBtn.setStyle(
                        "-fx-background-color: #0bcb95; " +
                                "-fx-border-color: #0bcb95; " +
                                "-fx-border-width: 2; " +
                                "-fx-border-radius: 12; " +
                                "-fx-background-radius: 12; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-cursor: hand;"
                );

                // Animation de sélection
                ScaleTransition scale = new ScaleTransition(Duration.millis(150), dayBtn);
                scale.setFromX(1.0);
                scale.setFromY(1.0);
                scale.setToX(1.1);
                scale.setToY(1.1);
                scale.setAutoReverse(true);
                scale.setCycleCount(2);
                scale.play();
            } else {
                dayBtn.setStyle(
                        "-fx-background-color: #f8f9fa; " +
                                "-fx-border-color: #dee2e6; " +
                                "-fx-border-width: 2; " +
                                "-fx-border-radius: 12; " +
                                "-fx-background-radius: 12; " +
                                "-fx-text-fill: #6c757d; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-cursor: hand;"
                );
            }
        });

        // Effet hover
        dayBtn.setOnMouseEntered(e -> {
            if (!dayBtn.isSelected()) {
                dayBtn.setStyle(
                        "-fx-background-color: #e9ecef; " +
                                "-fx-border-color: #0bcb95; " +
                                "-fx-border-width: 2; " +
                                "-fx-border-radius: 12; " +
                                "-fx-background-radius: 12; " +
                                "-fx-text-fill: #0bcb95; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-cursor: hand;"
                );
            }
        });

        dayBtn.setOnMouseExited(e -> {
            if (!dayBtn.isSelected()) {
                dayBtn.setStyle(
                        "-fx-background-color: #f8f9fa; " +
                                "-fx-border-color: #dee2e6; " +
                                "-fx-border-width: 2; " +
                                "-fx-border-radius: 12; " +
                                "-fx-background-radius: 12; " +
                                "-fx-text-fill: #6c757d; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-cursor: hand;"
                );
            }
        });

        return dayBtn;
    }

    private VBox createStatusFields() {
        VBox fields = new VBox(10);

        estActifCheck = new CheckBox("Médecin actif");
        estActifCheck.setSelected(true);
        estActifCheck.setStyle(
                "-fx-text-fill: #495057; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: 500;"
        );

        fields.getChildren().add(estActifCheck);
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

    private void styleControl(Control control) {
        control.setStyle(
                "-fx-background-color: #f8f9fa; " +
                        "-fx-border-color: #dee2e6; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 8; " +
                        "-fx-background-radius: 8; " +
                        "-fx-padding: 10; " +
                        "-fx-font-size: 14px;"
        );

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
                                "-fx-font-size: 14px;"
                );
            } else {
                control.setStyle(
                        "-fx-background-color: #f8f9fa; " +
                                "-fx-border-color: #dee2e6; " +
                                "-fx-border-width: 1; " +
                                "-fx-border-radius: 8; " +
                                "-fx-background-radius: 8; " +
                                "-fx-padding: 10; " +
                                "-fx-font-size: 14px;"
                );
            }
        });
    }

    private void setupSubmitButton() {
        submitBtn = new Button("Valider l'inscription");
        submitBtn.setStyle(
                "-fx-background-color: #0bcb95; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15 30 15 30; " +
                        "-fx-background-radius: 25; " +
                        "-fx-cursor: hand;"
        );

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
                            "-fx-cursor: hand;"
            );

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
                            "-fx-cursor: hand;"
            );

            ScaleTransition scale = new ScaleTransition(Duration.millis(150), submitBtn);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        submitBtn.setOnAction(e -> {
            if (validateForm()) {
                Medecin medecin = createMedecin();
                showSuccessAnimation();
                showSuccessAlert(medecin);
            } else {
                showValidationError();
            }
        });
    }

    private void startAnimations() {
        // Animation de lévitation pour l'image
        TranslateTransition levitation = new TranslateTransition(Duration.seconds(3.5), doctorImageView);
        levitation.setFromY(0);
        levitation.setToY(-20);
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
        boolean joursSelected = lundiBtn.isSelected() || mardiBtn.isSelected() ||
                mercrediBtn.isSelected() || jeudiBtn.isSelected() ||
                vendrediBtn.isSelected() || samediBtn.isSelected();

        return !nomField.getText().trim().isEmpty() &&
                !prenomField.getText().trim().isEmpty() &&
                !specialiteField.getText().trim().isEmpty() &&
                joursSelected;
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
                        "-fx-cursor: hand;"
        );

        Timeline resetColor = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            submitBtn.setStyle(
                    "-fx-background-color: #0bcb95; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 16px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-padding: 15 30 15 30; " +
                            "-fx-background-radius: 25; " +
                            "-fx-cursor: hand;"
            );
        }));
        resetColor.play();

        showAlert("Champs obligatoires", "Veuillez remplir tous les champs obligatoires (*) et sélectionner au moins un jour de travail.");
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
    }

    private Medecin createMedecin() {
        Medecin medecin = new Medecin();
        medecin.setNom(nomField.getText().trim());
        medecin.setPrenom(prenomField.getText().trim());
        medecin.setSpecialite(specialiteField.getText().trim());
        medecin.setTelephone(telField.getText().trim());
        medecin.setEmail(emailField.getText().trim());

        Set<String> joursTravail = new HashSet<>();
        if (lundiBtn.isSelected()) joursTravail.add("Lundi");
        if (mardiBtn.isSelected()) joursTravail.add("Mardi");
        if (mercrediBtn.isSelected()) joursTravail.add("Mercredi");
        if (jeudiBtn.isSelected()) joursTravail.add("Jeudi");
        if (vendrediBtn.isSelected()) joursTravail.add("Vendredi");
        if (samediBtn.isSelected()) joursTravail.add("Samedi");

        medecin.setJoursTravail(joursTravail);
        medecin.setEstActif(estActifCheck.isSelected());

        return medecin;
    }

    private void showSuccessAlert(Medecin medecin) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Inscription réussie");
        alert.setHeaderText("Médecin enregistré avec succès");
        alert.setContentText("Dr. " + medecin.getNom() + " " + medecin.getPrenom() +
                "\nSpécialité: " + medecin.getSpecialite() +
                "\nJours de travail: " + String.join(", ", medecin.getJoursTravail()));
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}