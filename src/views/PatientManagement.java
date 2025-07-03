package views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Patient;

public class PatientManagement extends Scene {
    public PatientManagement() {
        super(new VBox(), 1024, 768);
        VBox root = (VBox) getRoot();

        // Tableau des patients (fake data)
        TableView<Patient> table = new TableView<>();
        TableColumn<Patient, String> nomCol = new TableColumn<>("Nom");
        TableColumn<Patient, String> prenomCol = new TableColumn<>("Prénom");
        table.getColumns().addAll(nomCol, prenomCol);

        // Ajouter des données fictives
        ObservableList<Patient> data = FXCollections.observableArrayList(
//                new Patient("Dupont", "Jean"),
//                new Patient("Martin", "Sophie")
        );
        table.setItems(data);

        // Boutons d'action
        HBox actions = new HBox(10);
        Button addBtn = new Button("Ajouter");
        Button editBtn = new Button("Modifier");
        addBtn.setStyle("-fx-background-color: #0bcb95; -fx-text-fill: white;");

        root.getChildren().addAll(table, actions);
    }
}