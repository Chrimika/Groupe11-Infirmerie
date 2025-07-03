package views;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AdminDashboard extends Scene {
    public AdminDashboard() {
        super(new BorderPane(), 1024, 768);
        BorderPane root = (BorderPane) getRoot();

        // Menu latéral
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: #0bcb95; -fx-padding: 20;");

        Button patientsBtn = new Button("Gestion Patients");
        Button medecinsBtn = new Button("Gestion Médecins");
        Button statsBtn = new Button("Statistiques");

        sidebar.getChildren().addAll(patientsBtn, medecinsBtn, statsBtn);

        // Contenu principal
        Label welcome = new Label("Bienvenue, Admin");
        VBox content = new VBox(welcome);

        root.setLeft(sidebar);
        root.setCenter(content);
    }
}