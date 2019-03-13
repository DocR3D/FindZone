package listtheme;
	
import bdd.BaseDeDonnee;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	public static BaseDeDonnee findZoneBdd = new BaseDeDonnee("bddfindzone");
	public static Stage unPrimaryStage;
	@Override
	public void start(Stage primaryStage) {
		try {
			
			unPrimaryStage=primaryStage;
			FXMLLoader rootLoader=new FXMLLoader(getClass().getResource("InterfaceListTheme.fxml"));
			BorderPane root = rootLoader.load();
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	

	
	
	
}
