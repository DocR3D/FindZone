package application;
	

import Menu.MenuController;
import bdd.BaseDeDonnee;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	public static BaseDeDonnee findZoneBdd = new BaseDeDonnee("bddfindzone");
	public static Stage myPrimaryStage;
	private static int idTheme;

	@Override
	public void start(Stage primaryStage) {
		try {
			myPrimaryStage=primaryStage;
			primaryStage.setTitle("FindZone");
			FXMLLoader rootLoader=new FXMLLoader(getClass().getResource("/Menu/InterfaceMenu.fxml"));
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
	
	public static int getIdTheme() {
		return idTheme;
	}


	public static void setIdTheme(int unidTheme) {
		idTheme = unidTheme;
	}

}
