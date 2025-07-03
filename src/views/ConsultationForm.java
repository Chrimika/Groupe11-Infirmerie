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

    public ConsultationForm() {
        super(new BorderPane(), 800, 600);

        BorderPane root = (BorderPane) getRoot();
        root.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        VBox header = createHeader();
        root.setTop(header);

        // Main content
        ScrollPane mainContent = createMainContent();
        root.setCenter(mainContent);

        // Footer with buttons
        HBox footer = createFooter();
        root.setBottom(footer);
    }

    private VBox createHeader() {
        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #0bcb95; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");

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
                notesSection
        );

        scrollPane.setContent(mainContent);
        return scrollPane;
    }

    private VBox createPatientSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-padding: 20;");

        Label sectionTitle = new Label("👤 Informations Patient");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        Label patientLabel = new Label("Sélectionner le patient:");
        patientLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));

        patientComboBox = new ComboBox<>();
        patientComboBox.setPromptText("Choisir un patient...");
        patientComboBox.setPrefWidth(400);
        patientComboBox.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-border-radius: 5;");

        // Données exemple
        patientComboBox.getItems().addAll(
                "Jean Dupont (15 ans) - ID: 001",
                "Marie Martin (28 ans) - ID: 002",
                "Pierre Durand (45 ans) - ID: 003",
                "Sophie Moreau (32 ans) - ID: 004",
                "Antoine Petit (67 ans) - ID: 005"
        );

        section.getChildren().addAll(sectionTitle, patientLabel, patientComboBox);
        return section;
    }

    private VBox createConsultationSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-padding: 20;");

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
                "Consultation spécialisée"
        );

        typeBox.getChildren().addAll(typeLabel, typeConsultation);

        consultationDetails.getChildren().addAll(dateBox, typeBox);
        section.getChildren().addAll(sectionTitle, consultationDetails);

        return section;
    }

    private VBox createDiagnosticSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-padding: 20;");

        Label sectionTitle = new Label("🔍 Diagnostic");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        diagnosticArea = new TextArea();
        diagnosticArea.setPromptText("Saisir le diagnostic médical...");
        diagnosticArea.setPrefRowCount(4);
        diagnosticArea.setWrapText(true);
        diagnosticArea.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #dee2e6;");

        section.getChildren().addAll(sectionTitle, diagnosticArea);
        return section;
    }

    private VBox createTraitementSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-padding: 20;");

        Label sectionTitle = new Label("💊 Traitement Prescrit");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        traitementArea = new TextArea();
        traitementArea.setPromptText("Saisir le traitement prescrit (médicaments, posologie, durée...)");
        traitementArea.setPrefRowCount(4);
        traitementArea.setWrapText(true);
        traitementArea.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #dee2e6;");

        section.getChildren().addAll(sectionTitle, traitementArea);
        return section;
    }

    private VBox createNotesSection() {
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-padding: 20;");

        Label sectionTitle = new Label("📝 Notes Complémentaires");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web("#0bcb95"));

        notesArea = new TextArea();
        notesArea.setPromptText("Notes additionnelles, observations, recommandations...");
        notesArea.setPrefRowCount(3);
        notesArea.setWrapText(true);
        notesArea.setStyle("-fx-font-size: 14px; -fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #dee2e6;");

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
        cancelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5; -fx-cursor: hand;");
        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) getWindow();
            stage.setScene(new DoctorInterface());
        });

        submitButton = new Button("Enregistrer Consultation");
        submitButton.setPrefWidth(200);
        submitButton.setPrefHeight(40);
        submitButton.setStyle("-fx-background-color: #0bcb95; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;");
        submitButton.setOnAction(e -> {
            Stage stage = (Stage) getWindow();
            stage.setScene(new ConsultationForm());
        });

        // Effets au survol
        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle("-fx-background-color: #5a6268; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5; -fx-cursor: hand;"));
        cancelButton.setOnMouseExited(e -> cancelButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5; -fx-cursor: hand;"));

        submitButton.setOnMouseEntered(e -> submitButton.setStyle("-fx-background-color: #0aa082; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;"));
        submitButton.setOnMouseExited(e -> submitButton.setStyle("-fx-background-color: #0bcb95; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; -fx-cursor: hand;"));

        footer.getChildren().addAll(cancelButton, submitButton);
        return footer;
    }

    // Getters pour accéder aux composants depuis l'extérieur
    public ComboBox<String> getPatientComboBox() {
        return patientComboBox;
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
}