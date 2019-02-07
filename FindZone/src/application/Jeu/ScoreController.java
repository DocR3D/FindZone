package application.Jeu;

import java.io.IOException;

import Menu.MenuController;
import Presentation.PresentationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ScoreController {

	@FXML
	BorderPane leBorderPane;
	@FXML
	Label score;
	@FXML
	Label question;
	@FXML
	Label auteur;
	@FXML
	ImageView image;
	@FXML
	Button menu2;
	@FXML
	Button recommencer;
	@FXML
	Text tempsChrono;

	public void initialize() throws InterruptedException {
		MenuController.parametre(menu2);
		MenuController.parametre(recommencer);


		image.setImage(JeuController.getImageTheme());

		score.setText("Votre score est : " + JeuController.getScorePoint() + " points");
		MenuController.parametre(score);
		MenuController.parametre(tempsChrono);
		MenuController.parametre(auteur);
		MenuController.parametre(leBorderPane);
		tempsChrono.setText("Temps : " + JeuController.getTemps() + " Sec");
		auteur.setText("Auteur : " + PresentationController.getAuteur());
	}

	@FXML
	public void recommencer() throws IOException {

		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("JeuInterface.fxml"));
		@SuppressWarnings("unused")
		Parent root = rootLoader.load();
		Stage stage = (Stage) recommencer.getScene().getWindow();
		Scene scene = new Scene(rootLoader.getRoot());
		stage.setScene(scene);
	}

	@FXML
	public void menu() throws IOException {

		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/Menu/InterfaceMenu.fxml"));
		@SuppressWarnings("unused")
		Parent root = rootLoader.load();
		Stage stage = (Stage) recommencer.getScene().getWindow();
		Scene scene = new Scene(rootLoader.getRoot());
		stage.setScene(scene);
	}
}
