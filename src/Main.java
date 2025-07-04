import javafx.application.Application;
import javafx.stage.Stage;
import views.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // ChoiceInscriptionView est une Scene, on peut l'utiliser directement
            //ChoiceInscriptionView choiceScene = new ChoiceInscriptionView(primaryStage);
            LandingPage  choiceScene = new LandingPage();
            primaryStage.setScene(choiceScene);
            primaryStage.setTitle("Infirmerie Scolaire");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}