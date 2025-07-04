package views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ConsultationForm extends Scene {

    private ComboBox<String> patientComboBox;
    private TextArea diagnosticArea;
    private TextArea traitementArea;
    private TextArea notesArea;
    private DatePicker dateConsultation;
    private ComboBox<String> typeConsultation;
    private Button submitButton;
    private Button cancelButton;
    private Stage stage;

    public ConsultationForm(Stage stage) {
        super(new BorderPane(), 800, 600);
        this.stage = stage; // Initialisation de la référence au Stage

        BorderPane root = (BorderPane) getRoot();
        root.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        VBox header = createHeader();
        root.setTop(header);

        // Main content
        ScrollPane mainContent = createMainContent();
        root.setCenter(mainContent);
        // Charger les patients depuis la base de données
        loadPatientsFromDatabase();

        // Footer with buttons
        HBox footer = createFooter();
        root.setBottom(footer);
    }

    // Enregistrement de la consultation en base de données
    private boolean saveConsultationToDatabase() {
        // Récupérer les valeurs du formulaire
        String patient = patientComboBox.getValue();
        String diagnostic = diagnosticArea.getText().trim();
        String traitement = traitementArea.getText().trim();
        String notes = notesArea.getText().trim();
        java.time.LocalDate date = (dateConsultation.getValue() != null) ? dateConsultation.getValue()
                : java.time.LocalDate.now();
        String type = typeConsultation.getValue();

        if (patient == null || patient.isEmpty() || diagnostic.isEmpty() || traitement.isEmpty() || type == null
                || type.isEmpty()) {
            showAlert("Champs obligatoires",
                    "Veuillez remplir tous les champs obligatoires (patient, diagnostic, traitement, type).");
            return false;
        }

        // Extraire l'ID du patient depuis la chaîne (ex: "Jean Dupont (15 ans) - ID:
        // 001")
        String patientId = "";
        if (patient.contains("ID:")) {
            String[] parts = patient.split("ID:");
            if (parts.length > 1)
                patientId = parts[1].trim();
        }

        try (java.sql.Connection conn = utils.DBConnection.getConnection()) {
            // Création de la table si elle n'existe pas
            String createTableSQL = "CREATE TABLE IF NOT EXISTS consultation (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "patient_id VARCHAR(50)," +
                    "date_consultation DATE," +
                    "type VARCHAR(100)," +
                    "diagnostic TEXT," +
                    "traitement TEXT," +
                    "notes TEXT" +
                    ")";
            try (java.sql.Statement st = conn.createStatement()) {
                st.executeUpdate(createTableSQL);
            }

            String sql = "INSERT INTO consultation (patient_id, date_consultation, type, diagnostic, traitement, notes) VALUES (?, ?, ?, ?, ?, ?)";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, patientId);
                stmt.setDate(2, java.sql.Date.valueOf(date));
                stmt.setString(3, type);
                stmt.setString(4, diagnostic);
                stmt.setString(5, traitement);
                stmt.setString(6, notes);
                stmt.executeUpdate();
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Erreur SQL", "Erreur lors de l'enregistrement de la consultation: " + ex.getMessage());
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private VBox createHeader() {
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle(
                "-fx-background-color: #0bcb95; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        Label title = new Label("Nouvelle Consultation Médicale");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label subtitle = new Label("Saisissez les informations de consultation");
        subtitle.setTextFill(Color.web("#e8e8e8"));
        subtitle.setFont(Font.font("System", 14));

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private ScrollPane createMainContent() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));
        mainContent.setMaxWidth(800);
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Section Patient
        VBox patientSection = createPatientSection();

        // Section Consultation
        VBox consultationSection = createConsultationSection();

        // Section Diagnostic
        VBox diagnosticSection = createDiagnosticSection();

        // Section Traitement
        VBox traitementSection = createTraitementSection();

        // Section Notes
        VBox notesSection = createNotesSection();

        mainContent.getChildren().addAll(
                patientSection,
                consultationSection,
                diagnosticSection,
                traitementSection,
                notesSection);

        scrollPane.setContent(mainContent);
        return scrollPane;
    }

    private VBox createPatientSection() {
        VBox section = new VBox(10);
        section.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-padding: 20;");

        Label sectionTitle = new Label("👤 Informations Patient");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        Label patientLabel = new Label("Sélectionner le patient:");
        patientLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));

        patientComboBox = new ComboBox<>();
        patientComboBox.setPromptText("Choisir un patient...");
        patientComboBox.setPrefWidth(400);
        patientComboBox.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-border-radius: 5;");

        // Les patients seront chargés dynamiquement depuis la base

        section.getChildren().addAll(sectionTitle, patientLabel, patientComboBox);
        return section;
    }

    private VBox createConsultationSection() {
        VBox section = new VBox(10);
        section.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-padding: 20;");

        Label sectionTitle = new Label("📅 Détails de la Consultation");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        HBox consultationDetails = new HBox(20);
        consultationDetails.setAlignment(Pos.CENTER_LEFT);

        VBox dateBox = new VBox(5);
        Label dateLabel = new Label("Date de consultation:");
        dateLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));

        dateConsultation = new DatePicker();
        dateConsultation.setPrefWidth(200);
        dateConsultation.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-border-radius: 5;");

        dateBox.getChildren().addAll(dateLabel, dateConsultation);

        VBox typeBox = new VBox(5);
        Label typeLabel = new Label("Type de consultation:");
        typeLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));

        typeConsultation = new ComboBox<>();
        typeConsultation.setPrefWidth(200);
        typeConsultation.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-border-radius: 5;");
        typeConsultation.getItems().addAll(
                "Consultation générale",
                "Consultation de suivi",
                "Consultation d'urgence",
                "Consultation spécialisée");

        typeBox.getChildren().addAll(typeLabel, typeConsultation);

        consultationDetails.getChildren().addAll(dateBox, typeBox);
        section.getChildren().addAll(sectionTitle, consultationDetails);

        return section;
    }

    private VBox createDiagnosticSection() {
        VBox section = new VBox(10);
        section.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-padding: 20;");

        Label sectionTitle = new Label("🔍 Diagnostic");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        diagnosticArea = new TextArea();
        diagnosticArea.setPromptText("Saisir le diagnostic médical...");
        diagnosticArea.setPrefRowCount(4);
        diagnosticArea.setWrapText(true);
        diagnosticArea.setStyle(
                "-fx-font-size: 14px; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #dee2e6;");

        section.getChildren().addAll(sectionTitle, diagnosticArea);
        return section;
    }

    private VBox createTraitementSection() {
        VBox section = new VBox(10);
        section.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-padding: 20;");

        Label sectionTitle = new Label("💊 Traitement Prescrit");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        traitementArea = new TextArea();
        traitementArea.setPromptText("Saisir le traitement prescrit (médicaments, posologie, durée...)");
        traitementArea.setPrefRowCount(4);
        traitementArea.setWrapText(true);
        traitementArea.setStyle(
                "-fx-font-size: 14px; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #dee2e6;");

        section.getChildren().addAll(sectionTitle, traitementArea);
        return section;
    }

    private VBox createNotesSection() {
        VBox section = new VBox(10);
        section.setStyle(
                "-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-padding: 20;");

        Label sectionTitle = new Label("📝 Notes Complémentaires");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        notesArea = new TextArea();
        notesArea.setPromptText("Notes additionnelles, observations, recommandations...");
        notesArea.setPrefRowCount(3);
        notesArea.setWrapText(true);
        notesArea.setStyle(
                "-fx-font-size: 14px; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #dee2e6;");

        section.getChildren().addAll(sectionTitle, notesArea);
        return section;
    }

    private HBox createFooter() {
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20));
        footer.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 1 0 0 0;");

        cancelButton = new Button("Annuler");
        cancelButton.setPrefWidth(120);
        cancelButton.setPrefHeight(40);
        cancelButton.setStyle(
                "-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5; -fx-cursor: hand;");
        // Utilisation de la référence stockée au Stage
        cancelButton.setOnAction(e -> {
            stage.setScene(new DoctorInterface(stage));
        });

        submitButton = new Button("Enregistrer Consultation");
        submitButton.setPrefWidth(200);
        submitButton.setPrefHeight(40);
        submitButton.setStyle(
                "-fx-background-color: #0bcb95; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
        submitButton.setOnAction(e -> {
            if (saveConsultationToDatabase()) {
                // Retour à l'interface du docteur après succès
                stage.setScene(new DoctorInterface(stage));
            }
        });

        // Effets au survol (inchangés)
        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(
                "-fx-background-color: #5a6268; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5; -fx-cursor: hand;"));
        cancelButton.setOnMouseExited(e -> cancelButton.setStyle(
                "-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5; -fx-cursor: hand;"));

        submitButton.setOnMouseEntered(e -> submitButton.setStyle(
                "-fx-background-color: #0aa082; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;"));
        submitButton.setOnMouseExited(e -> submitButton.setStyle(
                "-fx-background-color: #0bcb95; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;"));

        footer.getChildren().addAll(cancelButton, submitButton);
        return footer;
    }

    public TextArea getDiagnosticArea() {
        return diagnosticArea;
    }

    public TextArea getTraitementArea() {
        return traitementArea;
    }

    public TextArea getNotesArea() {
        return notesArea;
    }

    public DatePicker getDateConsultation() {
        return dateConsultation;
    }

    public ComboBox<String> getTypeConsultation() {
        return typeConsultation;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    // Charge les patients depuis la table patient et les ajoute à la ComboBox
    private void loadPatientsFromDatabase() {
        try (java.sql.Connection conn = utils.DBConnection.getConnection()) {
            // Création de la table patient si elle n'existe pas (sécurise le premier
            // lancement)
            String createTableSQL = "CREATE TABLE IF NOT EXISTS patient (" +
                    "id INT NOT NULL AUTO_INCREMENT," +
                    "utilisateur_id INT NOT NULL," +
                    "nom VARCHAR(50) NOT NULL," +
                    "prenom VARCHAR(50) NOT NULL," +
                    "date_naissance DATE NOT NULL," +
                    "numero_dossier VARCHAR(20) NOT NULL," +
                    "adresse TEXT," +
                    "telephone VARCHAR(20) DEFAULT NULL," +
                    "allergies TEXT," +
                    "antecedents TEXT," +
                    "mot_passe VARCHAR(100) NOT NULL," +
                    "PRIMARY KEY (id)," +
                    "UNIQUE KEY utilisateur_id (utilisateur_id)," +
                    "UNIQUE KEY numero_dossier (numero_dossier)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
            try (java.sql.Statement st = conn.createStatement()) {
                st.executeUpdate(createTableSQL);
            }

            String sql = "SELECT id, nom, prenom, date_naissance, numero_dossier FROM patient";
            try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (java.sql.ResultSet rs = stmt.executeQuery()) {
                    patientComboBox.getItems().clear();
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String nom = rs.getString("nom");
                        String prenom = rs.getString("prenom");
                        java.sql.Date dateNaissance = rs.getDate("date_naissance");
                        String numeroDossier = rs.getString("numero_dossier");
                        // Calcul de l'âge
                        int age = 0;
                        if (dateNaissance != null) {
                            java.time.LocalDate birth = dateNaissance.toLocalDate();
                            java.time.LocalDate now = java.time.LocalDate.now();
                            age = java.time.Period.between(birth, now).getYears();
                        }
                        String display = nom + " " + prenom + " (" + age + " ans) - Dossier: " + numeroDossier
                                + " - ID: " + id;
                        patientComboBox.getItems().add(display);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Erreur SQL", "Impossible de charger les patients: " + ex.getMessage());
        }
    }

    // Getters pour accéder aux composants depuis l'extérieur
    public ComboBox<String> getPatientComboBox() {
        return patientComboBox;
    }
}
