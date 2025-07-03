package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentManagement extends Scene {

    private ComboBox<String> medecinComboBox;
    private VBox disponibilitesContainer;
    private VBox heuresContainer;
    private ComboBox<String> patientComboBox;
    private TextArea motifArea;
    private Button confirmerButton;
    private Button annulerButton;
    private Label selectedMedecinInfo;
    private Label selectedDateTimeInfo;

    // Données simulées des médecins et leurs disponibilités
    private Map<String, Map<String, List<String>>> disponibilitesMedecins;

    public AppointmentManagement() {
        super(new BorderPane(), 800, 600);

        initializeData();

        BorderPane root = (BorderPane) getRoot();
        root.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        VBox header = createHeader();
        root.setTop(header);

        // Main content
        ScrollPane mainContent = createMainContent();
        root.setCenter(mainContent);

        // Footer
        HBox footer = createFooter();
        root.setBottom(footer);

        // Événements
        setupEventHandlers();
    }

    private void initializeData() {
        disponibilitesMedecins = new HashMap<>();

        // Dr. Martin - Médecin généraliste
        Map<String, List<String>> martinDispo = new HashMap<>();
        martinDispo.put("Lundi", Arrays.asList("08:00", "09:00", "10:00", "14:00", "15:00", "16:00"));
        martinDispo.put("Mardi", Arrays.asList("08:00", "09:00", "10:00", "11:00", "14:00", "15:00"));
        martinDispo.put("Mercredi", Arrays.asList("08:00", "09:00", "10:00"));
        martinDispo.put("Jeudi", Arrays.asList("08:00", "09:00", "10:00", "14:00", "15:00", "16:00"));
        martinDispo.put("Vendredi", Arrays.asList("08:00", "09:00", "10:00", "11:00"));
        disponibilitesMedecins.put("Dr. Martin - Médecin généraliste", martinDispo);

        // Dr. Dubois - Pédiatre
        Map<String, List<String>> duboisDispo = new HashMap<>();
        duboisDispo.put("Lundi", Arrays.asList("09:00", "10:00", "11:00", "14:00", "15:00"));
        duboisDispo.put("Mardi", Arrays.asList("08:00", "09:00", "10:00", "14:00", "15:00", "16:00"));
        duboisDispo.put("Jeudi", Arrays.asList("08:00", "09:00", "10:00", "11:00", "14:00"));
        duboisDispo.put("Vendredi", Arrays.asList("09:00", "10:00", "11:00", "14:00", "15:00", "16:00"));
        disponibilitesMedecins.put("Dr. Dubois - Pédiatre", duboisDispo);

        // Dr. Leroy - Infirmier scolaire
        Map<String, List<String>> leroyDispo = new HashMap<>();
        leroyDispo.put("Lundi", Arrays.asList("08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00"));
        leroyDispo.put("Mardi", Arrays.asList("08:00", "09:00", "10:00", "11:00", "14:00", "15:00"));
        leroyDispo.put("Mercredi", Arrays.asList("08:00", "09:00", "10:00", "11:00"));
        leroyDispo.put("Jeudi", Arrays.asList("08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00"));
        leroyDispo.put("Vendredi", Arrays.asList("08:00", "09:00", "10:00", "11:00", "14:00"));
        disponibilitesMedecins.put("Dr. Leroy - Infirmier scolaire", leroyDispo);
    }

    private VBox createHeader() {
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25));
        header.setStyle("-fx-background-color: #0bcb95; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label title = new Label("Prise de Rendez-vous");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("System", FontWeight.BOLD, 32));

        Label subtitle = new Label("Infirmerie Scolaire - Sélectionnez votre médecin et créneaux disponibles");
        subtitle.setTextFill(Color.web("#e8f5f3"));
        subtitle.setFont(Font.font("System", 16));

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private ScrollPane createMainContent() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox mainContent = new VBox(25);
        mainContent.setPadding(new Insets(30));
        mainContent.setMaxWidth(900);
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Section Patient
        VBox patientSection = createPatientSection();

        // Section Médecin
        VBox medecinSection = createMedecinSection();

        // Section Disponibilités
        VBox disponibilitesSection = createDisponibilitesSection();

        // Section Motif
        VBox motifSection = createMotifSection();

        // Section Récapitulatif
        VBox recapSection = createRecapSection();

        mainContent.getChildren().addAll(
                patientSection,
                medecinSection,
                disponibilitesSection,
                motifSection,
                recapSection
        );

        scrollPane.setContent(mainContent);
        return scrollPane;
    }

    private VBox createPatientSection() {
        VBox section = new VBox(12);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2); -fx-padding: 25;");

        Label sectionTitle = new Label("👤 Patient");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        Label patientLabel = new Label("Sélectionner le patient:");
        patientLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));

        patientComboBox = new ComboBox<>();
        patientComboBox.setPromptText("Choisir un patient...");
        patientComboBox.setPrefWidth(400);
        patientComboBox.setStyle("-fx-font-size: 14px; -fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 8;");

        patientComboBox.getItems().addAll(
                "Jean Dupont (15 ans) - Classe: 3èmeA",
                "Marie Martin (14 ans) - Classe: 4èmeB",
                "Pierre Durand (16 ans) - Classe: 2ndeC",
                "Sophie Moreau (13 ans) - Classe: 5èmeA",
                "Antoine Petit (17 ans) - Classe: 1èreS"
        );

        section.getChildren().addAll(sectionTitle, patientLabel, patientComboBox);
        return section;
    }

    private VBox createMedecinSection() {
        VBox section = new VBox(12);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2); -fx-padding: 25;");

        Label sectionTitle = new Label("👨‍⚕️ Médecin");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        Label medecinLabel = new Label("Choisir le médecin:");
        medecinLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));

        medecinComboBox = new ComboBox<>();
        medecinComboBox.setPromptText("Sélectionner un médecin...");
        medecinComboBox.setPrefWidth(400);
        medecinComboBox.setStyle("-fx-font-size: 14px; -fx-background-radius: 8; -fx-border-radius: 8; -fx-padding: 8;");

        medecinComboBox.getItems().addAll(disponibilitesMedecins.keySet());

        selectedMedecinInfo = new Label("");
        selectedMedecinInfo.setFont(Font.font("System", FontWeight.NORMAL, 12));
        selectedMedecinInfo.setTextFill(Color.web("#6c757d"));
        selectedMedecinInfo.setWrapText(true);

        section.getChildren().addAll(sectionTitle, medecinLabel, medecinComboBox, selectedMedecinInfo);
        return section;
    }

    private VBox createDisponibilitesSection() {
        VBox section = new VBox(15);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2); -fx-padding: 25;");

        Label sectionTitle = new Label("📅 Créneaux Disponibles");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        Label infoLabel = new Label("Sélectionnez d'abord un médecin pour voir ses disponibilités");
        infoLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        infoLabel.setTextFill(Color.web("#6c757d"));

        disponibilitesContainer = new VBox(15);
        disponibilitesContainer.getChildren().add(infoLabel);

        heuresContainer = new VBox(10);

        section.getChildren().addAll(sectionTitle, disponibilitesContainer, heuresContainer);
        return section;
    }

    private VBox createMotifSection() {
        VBox section = new VBox(12);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 2); -fx-padding: 25;");

        Label sectionTitle = new Label("📝 Motif de la Consultation");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        motifArea = new TextArea();
        motifArea.setPromptText("Décrivez brièvement le motif de votre consultation...");
        motifArea.setPrefRowCount(3);
        motifArea.setWrapText(true);
        motifArea.setStyle("-fx-font-size: 14px; -fx-background-radius: 8; -fx-border-radius: 8; -fx-border-color: #dee2e6; -fx-padding: 10;");

        section.getChildren().addAll(sectionTitle, motifArea);
        return section;
    }

    private VBox createRecapSection() {
        VBox section = new VBox(12);
        section.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 12; -fx-border-color: #0bcb95; -fx-border-width: 2; -fx-border-radius: 12; -fx-padding: 25;");

        Label sectionTitle = new Label("📋 Récapitulatif");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        selectedDateTimeInfo = new Label("Veuillez sélectionner un médecin et un créneau");
        selectedDateTimeInfo.setFont(Font.font("System", FontWeight.NORMAL, 14));
        selectedDateTimeInfo.setTextFill(Color.web("#495057"));
        selectedDateTimeInfo.setWrapText(true);

        section.getChildren().addAll(sectionTitle, selectedDateTimeInfo);
        return section;
    }

    private void setupEventHandlers() {
        medecinComboBox.setOnAction(e -> {
            String selectedMedecin = medecinComboBox.getValue();
            if (selectedMedecin != null) {
                updateMedecinInfo(selectedMedecin);
                updateDisponibilites(selectedMedecin);
            }
        });
    }

    private void updateMedecinInfo(String medecin) {
        String info = "";
        if (medecin.contains("généraliste")) {
            info = "Consultations générales, examens de routine, certificats médicaux";
        } else if (medecin.contains("Pédiatre")) {
            info = "Spécialiste des enfants et adolescents, vaccinations, suivi de croissance";
        } else if (medecin.contains("Infirmier")) {
            info = "Soins infirmiers, premiers secours, suivi des traitements";
        }
        selectedMedecinInfo.setText("Spécialités: " + info);
    }

    private void updateDisponibilites(String medecin) {
        disponibilitesContainer.getChildren().clear();
        heuresContainer.getChildren().clear();

        Map<String, List<String>> disponibilites = disponibilitesMedecins.get(medecin);
        if (disponibilites != null) {
            Label instructionLabel = new Label("Cliquez sur un jour pour voir les créneaux horaires disponibles:");
            instructionLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
            instructionLabel.setTextFill(Color.web("#495057"));

            disponibilitesContainer.getChildren().add(instructionLabel);

            HBox joursContainer = new HBox(10);
            joursContainer.setAlignment(Pos.CENTER_LEFT);

            for (String jour : disponibilites.keySet()) {
                Button jourButton = new Button(jour);
                jourButton.setPrefWidth(100);
                jourButton.setPrefHeight(35);
                jourButton.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-font-size: 12px; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;");

                jourButton.setOnAction(e -> updateHeures(jour, disponibilites.get(jour)));

                jourButton.setOnMouseEntered(e -> jourButton.setStyle("-fx-background-color: #0bcb95; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;"));
                jourButton.setOnMouseExited(e -> jourButton.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-font-size: 12px; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;"));

                joursContainer.getChildren().add(jourButton);
            }

            disponibilitesContainer.getChildren().add(joursContainer);
        }
    }

    private void updateHeures(String jour, List<String> heures) {
        heuresContainer.getChildren().clear();

        Label heureLabel = new Label("Créneaux disponibles pour " + jour + ":");
        heureLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        heureLabel.setTextFill(Color.web("#0bcb95"));

        FlowPane heuresFlow = new FlowPane();
        heuresFlow.setHgap(8);
        heuresFlow.setVgap(8);

        for (String heure : heures) {
            Button heureButton = new Button(heure);
            heureButton.setPrefWidth(80);
            heureButton.setPrefHeight(30);
            heureButton.setStyle("-fx-background-color: white; -fx-text-fill: #0bcb95; -fx-font-size: 12px; -fx-background-radius: 6; -fx-border-color: #0bcb95; -fx-border-width: 1; -fx-border-radius: 6; -fx-cursor: hand;");

            heureButton.setOnAction(e -> selectCreneau(jour, heure));

            heureButton.setOnMouseEntered(e -> heureButton.setStyle("-fx-background-color: #0bcb95; -fx-text-fill: white; -fx-font-size: 12px; -fx-background-radius: 6; -fx-border-radius: 6; -fx-cursor: hand;"));
            heureButton.setOnMouseExited(e -> heureButton.setStyle("-fx-background-color: white; -fx-text-fill: #0bcb95; -fx-font-size: 12px; -fx-background-radius: 6; -fx-border-color: #0bcb95; -fx-border-width: 1; -fx-border-radius: 6; -fx-cursor: hand;"));

            heuresFlow.getChildren().add(heureButton);
        }

        heuresContainer.getChildren().addAll(heureLabel, heuresFlow);
    }

    private void selectCreneau(String jour, String heure) {
        String medecin = medecinComboBox.getValue();
        String patient = patientComboBox.getValue();

        StringBuilder recap = new StringBuilder();
        recap.append("✅ Rendez-vous sélectionné:\n\n");

        if (patient != null) {
            recap.append("👤 Patient: ").append(patient).append("\n");
        }

        if (medecin != null) {
            recap.append("👨‍⚕️ Médecin: ").append(medecin).append("\n");
        }

        recap.append("📅 Date: ").append(jour).append("\n");
        recap.append("🕐 Heure: ").append(heure).append("\n\n");
        recap.append("Veuillez confirmer votre rendez-vous.");

        selectedDateTimeInfo.setText(recap.toString());

        // Activer le bouton de confirmation
        if (confirmerButton != null) {
            confirmerButton.setDisable(false);
        }
    }

    private HBox createFooter() {
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(25));
        footer.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1 0 0 0;");

        annulerButton = new Button("Annuler");
        annulerButton.setPrefWidth(130);
        annulerButton.setPrefHeight(45);
        annulerButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8; -fx-cursor: hand;");

        confirmerButton = new Button("Confirmer le Rendez-vous");
        confirmerButton.setPrefWidth(220);
        confirmerButton.setPrefHeight(45);
        confirmerButton.setStyle("-fx-background-color: #0bcb95; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
        confirmerButton.setDisable(true);

        // Effets de survol
        annulerButton.setOnMouseEntered(e -> annulerButton.setStyle("-fx-background-color: #5a6268; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8; -fx-cursor: hand;"));
        annulerButton.setOnMouseExited(e -> annulerButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8; -fx-cursor: hand;"));

        confirmerButton.setOnMouseEntered(e -> {
            if (!confirmerButton.isDisabled()) {
                confirmerButton.setStyle("-fx-background-color: #0aa082; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
            }
        });
        confirmerButton.setOnMouseExited(e -> {
            if (!confirmerButton.isDisabled()) {
                confirmerButton.setStyle("-fx-background-color: #0bcb95; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");
            }
        });

        footer.getChildren().addAll(annulerButton, confirmerButton);
        return footer;
    }

    // Getters
    public ComboBox<String> getMedecinComboBox() {
        return medecinComboBox;
    }

    public ComboBox<String> getPatientComboBox() {
        return patientComboBox;
    }

    public TextArea getMotifArea() {
        return motifArea;
    }

    public Button getConfirmerButton() {
        return confirmerButton;
    }

    public Button getAnnulerButton() {
        return annulerButton;
    }
}