package views;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import models.Consultation;

public class MedicalHistory extends Scene {
    public MedicalHistory() {
        super(new VBox(), 1024, 768);
        VBox root = (VBox) getRoot();

        // Tableau d'historique (fake data)
        TableView<Consultation> table = new TableView<>();
        TableColumn<Consultation, String> dateCol = new TableColumn<>("Date");
        TableColumn<Consultation, String> diagCol = new TableColumn<>("Diagnostic");

        table.getColumns().addAll(dateCol, diagCol);
        table.getItems().addAll(
//                new Consultation("12/05/2023", "Angine"),
//                new Consultation("03/02/2023", "Vaccination")
        );

        root.getChildren().addAll(
                new Label("Historique médical de Jean Dupont"),
                table
        );
    }
}