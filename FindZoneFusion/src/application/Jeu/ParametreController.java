package application.Jeu;

import java.io.IOException;

import Menu.MenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ParametreController {

	@FXML
	BorderPane leBorderPane;
	@FXML 
	ColorPicker couleurTexte;
	
	@FXML
	Label unTroisiemeLabel;
	@FXML
	Label unQuatriemeLabel;
	@FXML
	Label unDeuxiemeLabel;
	@FXML
	Label unAutreLabel;
	@FXML
	Label unCinquiemeLabel;
	
	
	@FXML
	private Button valider;
	@FXML
	private Button reset;
	
	@FXML
	ColorPicker couleurFond;
	
	@FXML
	ColorPicker couleurFond3;
	
	@FXML
	ComboBox<String> choixPolice;
	
	private static Color couleurTexteVar=Color.WHITE;
	private static String couleurFondVar="#1090BB";
	private static String choixPoliceVar="Arial";
	java.util.List<String> familiesList = Font.getFamilies();
	private static String couleurBouton="#FF8207";
	
	@FXML
	public void initialize(){
		MenuController.parametre(leBorderPane);
		MenuController.parametre(unAutreLabel);
		MenuController.parametre(reset);
		MenuController.parametre(unTroisiemeLabel);
		MenuController.parametre(unQuatriemeLabel);
		MenuController.parametre(unCinquiemeLabel);
		MenuController.parametre(valider);
		MenuController.parametre(unDeuxiemeLabel);

		
		choixPolice.setPromptText(choixPoliceVar);
		choixPolice.getItems().addAll(familiesList);
		couleurTexte.setValue(couleurTexteVar); 
		couleurFond.setValue(Color.valueOf(couleurFondVar));
		couleurFond3.setValue(Color.valueOf(couleurBouton));
		
	}
	
	@FXML
	public void validerEtQuitter() throws IOException{
		couleurTexteVar = couleurTexte.getValue();
		couleurFondVar =  couleurFond.getValue().toString();
		couleurFondVar = couleurFondVar.substring(2, 8);
		couleurFondVar = "#" + couleurFondVar;
		choixPoliceVar = choixPolice.getValue();
		couleurBouton = couleurFond3.getValue().toString();
		couleurBouton = "#" +couleurBouton.substring(2, 8);
		
		//Changement de fênetre
		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/Menu/InterfaceMenu.fxml"));
		Stage stage = (Stage) choixPolice.getScene().getWindow();
		@SuppressWarnings("unused")
		Parent root = rootLoader.load();
		Scene scene = new Scene(rootLoader.getRoot());
		stage.setScene(scene);
		
	}
	


	@FXML
	public void reset() {
		couleurTexteVar=Color.WHITE;
		couleurFondVar="#1090BB";
		couleurBouton="#FF8207";

		couleurTexte.setValue(couleurTexteVar); 
		couleurFond.setValue(Color.valueOf(couleurFondVar));
		couleurFond3.setValue(Color.valueOf(couleurBouton));
		
	}
	public static Color getCouleurTexteVar() {
		return couleurTexteVar;
	}

	public static String getCouleurFondVar() {
		return couleurFondVar;
	}

	public  static String getChoixPoliceVar() {
		return choixPoliceVar;
	}
	public static String getCouleurBouton(){
		return couleurBouton;
	}
}
