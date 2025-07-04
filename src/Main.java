import javafx.application.Application;
import javafx.stage.Stage;
import views.LandingPage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            LandingPage landingPage = new LandingPage();
            primaryStage.setScene(landingPage);
            primaryStage.setTitle("Infirmerie Scolaire");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}