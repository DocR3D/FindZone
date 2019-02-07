package Presentation;

import java.io.IOException;

import Menu.MenuController;
import Menu.Theme;
import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PresentationController {

	public  Stage myPrimaryStage;

	@FXML
	BorderPane leBorderPane;
	@FXML
	Label unAuteur;
	@FXML
	Text duTexte;
	@FXML
	Text seconde;
	
	@FXML
	private Button Jouer;
	
	@FXML
	private HBox info;

	@FXML
	private Button RetourMenu;

	@FXML
	private Label auteur;

	@FXML
	private Label nomTheme;
	
	@FXML 
	private VBox image;
	
	@FXML 
	private ImageView imageTheme;

	private static Theme theme;

	public void initialize(){
		info.setSpacing(30);
		image.setSpacing(10);	
	}
	
	@FXML
	private void jouer() throws IOException {
		
		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/application/Jeu/JeuInterface.fxml"));
		BorderPane root = rootLoader.load();
		Scene scene = new Scene(root, 800, 600);
		Main.myPrimaryStage.setScene(scene);

	}
	public void setThemeAndlnit(Theme theme){
		PresentationController.theme = theme;
		nomTheme.setText(theme.getNom());
		imageTheme.setImage(new Image("File:./Images/"+theme.getId() + ".jpg"));
		auteur.setText(theme.getAuteur());
		MenuController.parametre(auteur);
		MenuController.parametre(nomTheme);
		MenuController.parametre(duTexte);
		MenuController.parametre(seconde);
		MenuController.parametre(unAuteur);
		MenuController.parametre(leBorderPane);
		MenuController.parametre(Jouer);
		MenuController.parametre(RetourMenu);

		
	}
	

	public static String getAuteur(){
		return theme.getAuteur();
	}
	
	@FXML
	private void LancerMenu() throws IOException {
		
		FXMLLoader rootLoader = new FXMLLoader(
				getClass().getResource("/Menu/InterfaceMenu.fxml"));
		BorderPane root = rootLoader.load();
		Scene scene = new Scene(root, 800, 600);

		Main.myPrimaryStage.setScene(scene);

	}

}