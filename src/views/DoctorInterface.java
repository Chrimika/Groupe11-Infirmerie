package views;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import models.Medecin;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Statement;

public class DoctorInterface extends Scene {
    private Label timeLabel;
    private VBox appointmentsList;
    private VBox consultationsList;
    private Stage stage;
    private Medecin medecin;
    private List<String[]> appointments; // [nom, heure, raison, icone, couleur]
    private int nbPatients;
    private int nbRdv;
    private int nbUrgences;
    private String tempsMoyen;

    // Nouveau constructeur pour personnalisation
    public DoctorInterface(Stage stage, Medecin medecin, List<String[]> appointments, int nbPatients, int nbRdv,
            int nbUrgences, String tempsMoyen) {
        super(new StackPane(), 1200, 600);
        this.stage = stage;
        this.medecin = medecin;
        this.appointments = appointments;
        this.nbPatients = nbPatients;
        this.nbRdv = nbRdv;
        this.nbUrgences = nbUrgences;
        this.tempsMoyen = tempsMoyen;
        StackPane root = (StackPane) getRoot();
        createGradientBackground(root);
        BorderPane mainLayout = createMainLayout();
        root.getChildren().add(mainLayout);
        playEntranceAnimations();
        startClock();
    }

    // Ancien constructeur pour compatibilité (données démo)
    public DoctorInterface(Stage stage) {
        this(stage,
                new Medecin(),
                List.of(
                        new String[] { "Jean Dupont", "10:00", "Consultation générale", "🩺", "#e3f2fd" },
                        new String[] { "Marie Curie", "11:30", "Contrôle tension", "❤️", "#f3e5f5" },
                        new String[] { "Pierre Martin", "14:00", "Vaccination", "💉", "#e8f5e8" },
                        new String[] { "Sophie Bernard", "15:30", "Consultation pédiatrique", "👶", "#fff3e0" },
                        new String[] { "Lucas Moreau", "16:45", "Bilan de santé", "📋", "#f1f8e9" }),
                12, 5, 2, "25min");
    }

    private void createGradientBackground(StackPane root) {
        Rectangle background = new Rectangle(1024, 768);

        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, null,
                new Stop(0, Color.web("#0bcb95")),
                new Stop(0.6, Color.web("#7dd3c0")),
                new Stop(1, Color.web("#f8fffe")));
        background.setFill(gradient);

        GaussianBlur blur = new GaussianBlur(1);
        background.setEffect(blur);

        root.getChildren().add(background);
    }

    private BorderPane createMainLayout() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(20));

        // En-tête
        HBox header = createHeader();
        layout.setTop(header);

        // Contenu principal
        HBox centerContent = createCenterContent();
        layout.setCenter(centerContent);

        // Pied de page avec actions
        HBox footer = createFooter();
        layout.setBottom(footer);

        return layout;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 30, 0));
        header.setSpacing(20);

        // Conteneur avec effet glassmorphisme
        HBox headerContainer = new HBox();
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        headerContainer.setPadding(new Insets(20));
        headerContainer.setSpacing(20);
        headerContainer.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.3);" +
                        "-fx-border-width: 1;");

        // Ombre portée
        DropShadow headerShadow = new DropShadow();
        headerShadow.setRadius(15);
        headerShadow.setOffsetY(5);
        headerShadow.setColor(Color.color(0, 0, 0, 0.1));
        headerContainer.setEffect(headerShadow);

        // Avatar docteur (homme/femme selon prénom)
        String avatarEmoji = "👨‍⚕️";
        if (medecin != null && medecin.getPrenom() != null && !medecin.getPrenom().isEmpty()) {
            String prenom = medecin.getPrenom().toLowerCase();
            if (prenom.endsWith("e") || prenom.equals("marie") || prenom.equals("sophie") || prenom.equals("julie")) {
                avatarEmoji = "👩‍⚕️";
            }
        }
        Label avatar = new Label(avatarEmoji);
        avatar.setFont(Font.font("Arial", 40));

        // Informations docteur personnalisées
        VBox doctorInfo = new VBox(5);
        String nomAffiche = (medecin != null && medecin.getNom() != null && !medecin.getNom().isEmpty())
                ? medecin.getNom()
                : "Martin";
        String prenomAffiche = (medecin != null && medecin.getPrenom() != null) ? medecin.getPrenom() : "";
        Label welcome = new Label(
                "Bonjour, Dr. " + nomAffiche + (prenomAffiche.isEmpty() ? "" : (" " + prenomAffiche)));
        welcome.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        welcome.setTextFill(Color.web("#0bcb95"));

        String specialiteAffiche = (medecin != null && medecin.getSpecialite() != null
                && !medecin.getSpecialite().isEmpty()) ? medecin.getSpecialite() : "Médecine Générale";
        Label specialty = new Label(specialiteAffiche);
        specialty.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 14));
        specialty.setTextFill(Color.web("#666666"));

        doctorInfo.getChildren().addAll(welcome, specialty);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Horloge
        VBox clockBox = new VBox(5);
        clockBox.setAlignment(Pos.CENTER_RIGHT);

        timeLabel = new Label();
        timeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        timeLabel.setTextFill(Color.web("#0bcb95"));

        Label dateLabel = new Label(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
        dateLabel.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 12));
        dateLabel.setTextFill(Color.web("#666666"));

        clockBox.getChildren().addAll(timeLabel, dateLabel);

        headerContainer.getChildren().addAll(avatar, doctorInfo, spacer, clockBox);
        header.getChildren().add(headerContainer);

        return header;
    }

    private HBox createCenterContent() {
        HBox centerContent = new HBox(20);
        centerContent.setAlignment(Pos.TOP_CENTER);
        centerContent.setPadding(new Insets(0, 0, 20, 0));

        // Panel des rendez-vous (50% de la largeur)
        VBox appointmentsPanel = createAppointmentsPanel();
        appointmentsPanel.setPrefWidth(400);

        // Panel des consultations (30% de la largeur)
        VBox consultationsPanel = createConsultationsPanel();
        consultationsPanel.setPrefWidth(400);

        // Panel des statistiques (20% de la largeur)
        VBox statsPanel = createStatsPanel();
        statsPanel.setPrefWidth(280);

        centerContent.getChildren().addAll(appointmentsPanel, consultationsPanel, statsPanel);

        return centerContent;
    }

    // Panel pour afficher les consultations récentes
    private VBox createConsultationsPanel() {
        VBox panel = new VBox(15);
        panel.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.4);" +
                        "-fx-border-width: 1;");
        panel.setPadding(new Insets(25));

        // Ombre portée
        DropShadow panelShadow = new DropShadow();
        panelShadow.setRadius(20);
        panelShadow.setOffsetY(8);
        panelShadow.setColor(Color.color(0, 0, 0, 0.08));
        panel.setEffect(panelShadow);

        // En-tête du panel
        HBox panelHeader = new HBox();
        panelHeader.setAlignment(Pos.CENTER_LEFT);
        panelHeader.setSpacing(10);

        Label icon = new Label("📝");
        icon.setFont(Font.font("Arial", 24));

        Label title = new Label("Consultations récentes");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#2c3e50"));

        panelHeader.getChildren().addAll(icon, title);

        // Liste des consultations
        consultationsList = new VBox(10);
        List<String[]> consultations = loadConsultationsFromDatabase();
        if (consultations != null && !consultations.isEmpty()) {
            for (String[] c : consultations) {
                // c[0]=patient, c[1]=date, c[2]=type, c[3]=diagnostic, c[4]=traitement
                consultationsList.getChildren().add(createConsultationCard(c[0], c[1], c[2], c[3], c[4]));
            }
        } else {
            Label noConsult = new Label("Aucune consultation enregistrée.");
            noConsult.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 16));
            noConsult.setTextFill(Color.web("#888"));
            consultationsList.getChildren().add(noConsult);
        }

        ScrollPane scrollPane = new ScrollPane(consultationsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefHeight(300);

        panel.getChildren().addAll(panelHeader, scrollPane);
        return panel;
    }

    // Charge les consultations depuis la base de données
    private List<String[]> loadConsultationsFromDatabase() {
        List<String[]> consultations = new ArrayList<>();
        try (Connection conn = utils.DBConnection.getConnection()) {
            String sql = "SELECT c.date_consultation, c.type, c.diagnostic, c.traitement, p.nom, p.prenom FROM consultation c LEFT JOIN patient p ON c.patient_id = p.id ORDER BY c.date_consultation DESC LIMIT 10";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String patient = rs.getString("nom") + " " + rs.getString("prenom");
                        String date = (rs.getDate("date_consultation") != null)
                                ? rs.getDate("date_consultation").toString()
                                : "";
                        String type = rs.getString("type");
                        String diagnostic = rs.getString("diagnostic");
                        String traitement = rs.getString("traitement");
                        consultations.add(new String[] { patient, date, type, diagnostic, traitement });
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return consultations;
    }

    // Carte d'affichage pour une consultation
    private HBox createConsultationCard(String patient, String date, String type, String diagnostic,
            String traitement) {
        HBox card = new HBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(10));
        card.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1;");

        VBox info = new VBox(2);
        Label patientLabel = new Label(patient);
        patientLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        patientLabel.setTextFill(Color.web("#0bcb95"));

        Label dateLabel = new Label(date + " | " + type);
        dateLabel.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 12));
        dateLabel.setTextFill(Color.web("#666666"));

        Label diagLabel = new Label("Diagnostic: " + diagnostic);
        diagLabel.setFont(Font.font("Segoe UI", 12));
        diagLabel.setTextFill(Color.web("#2c3e50"));

        Label trtLabel = new Label("Traitement: " + traitement);
        trtLabel.setFont(Font.font("Segoe UI", 12));
        trtLabel.setTextFill(Color.web("#2c3e50"));

        info.getChildren().addAll(patientLabel, dateLabel, diagLabel, trtLabel);
        card.getChildren().add(info);
        return card;
    }

    private VBox createAppointmentsPanel() {
        VBox panel = new VBox(15);
        panel.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.4);" +
                        "-fx-border-width: 1;");
        panel.setPadding(new Insets(25));

        // Ombre portée
        DropShadow panelShadow = new DropShadow();
        panelShadow.setRadius(20);
        panelShadow.setOffsetY(8);
        panelShadow.setColor(Color.color(0, 0, 0, 0.08));
        panel.setEffect(panelShadow);

        // En-tête du panel
        HBox panelHeader = new HBox();
        panelHeader.setAlignment(Pos.CENTER_LEFT);
        panelHeader.setSpacing(10);

        Label calendarIcon = new Label("📅");
        calendarIcon.setFont(Font.font("Arial", 24));

        Label title = new Label("Mes rendez-vous aujourd'hui");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#2c3e50"));

        panelHeader.getChildren().addAll(calendarIcon, title);

        // Liste des rendez-vous depuis la base
        appointmentsList = new VBox(10);
        List<String[]> rdvs = loadRendezVousFromDatabase();
        if (rdvs != null && !rdvs.isEmpty()) {
            for (String[] rdv : rdvs) {
                // rdv[0]=patient, rdv[1]=jour, rdv[2]=heure
                appointmentsList.getChildren().add(createAppointmentCard(rdv[0], rdv[2], "", "🩺", "#e3f2fd"));
            }
        } else {
            Label noRdv = new Label("Aucun rendez-vous aujourd'hui.");
            noRdv.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 16));
            noRdv.setTextFill(Color.web("#888"));
            appointmentsList.getChildren().add(noRdv);
        }

        // ScrollPane pour la liste
        ScrollPane scrollPane = new ScrollPane(appointmentsList);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefHeight(300);

        panel.getChildren().addAll(panelHeader, scrollPane);

        return panel;
    }

    // Charge les rendez-vous du médecin connecté depuis la base
    private List<String[]> loadRendezVousFromDatabase() {
        List<String[]> rdvs = new ArrayList<>();
        try (Connection conn = utils.DBConnection.getConnection()) {
            // On suppose que le medecin est connecté et a un id
            int medecinId = (medecin != null && medecin.getId() > 0) ? medecin.getId() : 1; // fallback id=1
            String sql = "CREATE TABLE IF NOT EXISTS rendezvous (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "medecin_id INT NOT NULL," +
                    "patient_nom VARCHAR(100) NOT NULL," +
                    "jour VARCHAR(20) NOT NULL," +
                    "heure VARCHAR(10) NOT NULL," +
                    "etat VARCHAR(20) DEFAULT 'à venir'," +
                    "FOREIGN KEY (medecin_id) REFERENCES medecin(id)" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";
            try (Statement st = conn.createStatement()) {
                st.executeUpdate(sql);
            }
            String select = "SELECT patient_nom, jour, heure FROM rendezvous WHERE medecin_id = ? ORDER BY jour, heure";
            try (PreparedStatement stmt = conn.prepareStatement(select)) {
                stmt.setInt(1, medecinId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String patient = rs.getString("patient_nom");
                        String jour = rs.getString("jour");
                        String heure = rs.getString("heure");
                        rdvs.add(new String[] { patient, jour, heure });
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rdvs;
    }

    private void addSampleAppointments() {
        // Rendez-vous de démonstration
        appointmentsList.getChildren().addAll(
                createAppointmentCard("Jean Dupont", "10:00", "Consultation générale", "🩺", "#e3f2fd"),
                createAppointmentCard("Marie Curie", "11:30", "Contrôle tension", "❤️", "#f3e5f5"),
                createAppointmentCard("Pierre Martin", "14:00", "Vaccination", "💉", "#e8f5e8"),
                createAppointmentCard("Sophie Bernard", "15:30", "Consultation pédiatrique", "👶", "#fff3e0"),
                createAppointmentCard("Lucas Moreau", "16:45", "Bilan de santé", "📋", "#f1f8e9"));
    }

    private HBox createAppointmentCard(String patientName, String time, String reason, String icon, String bgColor) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15));
        card.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-color: rgba(0, 0, 0, 0.05);" +
                        "-fx-border-width: 1;");

        // Effet hover
        card.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), card);
            scale.setToX(1.02);
            scale.setToY(1.02);
            scale.play();

            card.setStyle(
                    "-fx-background-color: " + bgColor + ";" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-radius: 10;" +
                            "-fx-border-color: #0bcb95;" +
                            "-fx-border-width: 2;" +
                            "-fx-cursor: hand;");
        });

        card.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), card);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();

            card.setStyle(
                    "-fx-background-color: " + bgColor + ";" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-radius: 10;" +
                            "-fx-border-color: rgba(0, 0, 0, 0.05);" +
                            "-fx-border-width: 1;");
        });

        // Icône
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Arial", 24));

        // Informations
        VBox info = new VBox(3);

        Label nameLabel = new Label(patientName);
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#2c3e50"));

        Label reasonLabel = new Label(reason);
        reasonLabel.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 14));
        reasonLabel.setTextFill(Color.web("#666666"));

        info.getChildren().addAll(nameLabel, reasonLabel);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Heure
        Label timeLabel = new Label(time);
        timeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        timeLabel.setTextFill(Color.web("#0bcb95"));

        card.getChildren().addAll(iconLabel, info, spacer, timeLabel);

        return card;
    }

    private VBox createStatsPanel() {
        VBox panel = new VBox(15);
        panel.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.4);" +
                        "-fx-border-width: 1;");
        panel.setPadding(new Insets(25));

        // Ombre portée
        DropShadow panelShadow = new DropShadow();
        panelShadow.setRadius(20);
        panelShadow.setOffsetY(8);
        panelShadow.setColor(Color.color(0, 0, 0, 0.08));
        panel.setEffect(panelShadow);

        // Titre
        Label title = new Label("Statistiques du jour");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));

        // Cartes de statistiques
        VBox statsCards = new VBox(10);
        statsCards.getChildren().addAll(
                createStatCard("👥", "Patients vus", "12", "#e3f2fd"),
                createStatCard("📅", "RDV programmés", "5", "#f3e5f5"),
                createStatCard("🚨", "Urgences", "2", "#ffebee"),
                createStatCard("⏰", "Temps moyen", "25min", "#e8f5e8"));

        panel.getChildren().addAll(title, statsCards);

        return panel;
    }

    private HBox createStatCard(String icon, String label, String value, String bgColor) {
        HBox card = new HBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15));
        card.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;");

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Arial", 20));

        VBox info = new VBox(2);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        valueLabel.setTextFill(Color.web("#0bcb95"));

        Label labelText = new Label(label);
        labelText.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 12));
        labelText.setTextFill(Color.web("#666666"));

        info.getChildren().addAll(valueLabel, labelText);

        card.getChildren().addAll(iconLabel, info);

        return card;
    }

    private HBox createFooter() {
        HBox footer = new HBox(15);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(20, 0, 0, 0));

        // Bouton principal
        Button startConsultationBtn = createModernButton("🩺 Démarrer Consultation", "#0bcb95", true);
        startConsultationBtn.setOnAction(e -> {
            // Utilisation de la référence au Stage
            stage.setScene(new ConsultationForm(stage)); // Passer le stage si nécessaire
        });

        // Boutons secondaires
        Button emergencyBtn = createModernButton("🚨 Urgence", "#ff6b6b", false);
        Button breakBtn = createModernButton("☕ Pause", "#feca57", false);
        Button historyBtn = createModernButton("📊 Historique", "#74b9ff", false);

        footer.getChildren().addAll(startConsultationBtn, emergencyBtn, breakBtn, historyBtn);

        return footer;
    }

    private Button createModernButton(String text, String color, boolean isPrimary) {
        Button button = new Button(text);
        button.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

        if (isPrimary) {
            button.setPrefSize(220, 45);
            button.setStyle(
                    "-fx-background-color: " + color + ";" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 22;" +
                            "-fx-border-radius: 22;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 2);");
        } else {
            button.setPrefSize(160, 40);
            button.setStyle(
                    "-fx-background-color: " + color + ";" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 20;" +
                            "-fx-border-radius: 20;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 5, 0, 0, 1);");
        }

        // Animations
        button.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });

        button.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });

        return button;
    }

    private void playEntranceAnimations() {
        // Animation de fade-in pour toute l'interface
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), getRoot());
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private void startClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }
}