import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage stage;
    private static FXMLLoader loader;

    public static Stage getPrimaryStage() {
        return stage;
    }

    public static FXMLLoader getLoader() {
        return loader;
    }


    public static void main(String[]args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            //ResourceBundle bundle = ResourceBundle.getBundle("resources.bundles");
            loader = new FXMLLoader(this.getClass().getResource("Main.fxml"));
            stage = primaryStage;
            //loader.setResources(bundle);
            Parent root = loader.load();
            Scene scene = new Scene(root,860,645);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);
            Image image = new Image("icons/icon.png");
            primaryStage.getIcons().add(image);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
