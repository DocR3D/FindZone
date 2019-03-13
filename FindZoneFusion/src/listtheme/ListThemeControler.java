package listtheme;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import editiontheme.EditionThemeControler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ListThemeControler {

	@FXML
	public ListView<String> ensembleDesTheme;
	public static Stage unStage = new Stage();

	@FXML
	private void initialize() throws SQLException, IOException {
		this.verificationDeLaBDD();
		afficherTheme();
	}

	@FXML
	public void ajouterUnTheme() throws IOException, SQLException {
		EditionThemeControler.cheminImageTheme=null;
		ResultSet res = null;
		res = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT MAX(ID_THE) AS MAX FROM THEME");
		while (res.next()) {
			EditionThemeControler.ID  = res.getInt("MAX");
		}
		EditionThemeControler.ID++;
		System.out.println(EditionThemeControler.ID);
		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/editiontheme/InterfaceEditionTheme.fxml"));
		BorderPane root = rootLoader.load();
		Scene sceneEditionTheme = new Scene(root, 800, 600);
		Main.unPrimaryStage.setScene(sceneEditionTheme);

	}

	@FXML
	public void verificationDeLaBDD() throws SQLException {
		int idThemeDeLaQuestion;
		int idTheme;
		boolean verif = true;
		ResultSet res = null;
		res = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT * FROM QUESTION");
		while (res.next()) {
			idThemeDeLaQuestion = res.getInt("ID_THE_QUE");
			ResultSet res2 = null;
			res2 = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT * FROM THEME");
			while (res2.next()) {
				idTheme = res2.getInt("ID_THE");
				if(idThemeDeLaQuestion==idTheme) {
					verif=false;
				}
			}
			if(verif==true) {
				System.err.println("Un netoyage a été fait");
				Main.findZoneBdd.envoyeUneRequetteDEcriture("DELETE FROM QUESTION WHERE ID_THE_QUE = " + idThemeDeLaQuestion);
				Main.findZoneBdd.envoyeUneRequetteDEcriture("DELETE FROM COORDONNER WHERE ID_THE_CO = " + idThemeDeLaQuestion);
				
			}
		verif = true;
		}
		
	}

	@FXML
	public void suprimerUnTheme() throws IOException {
		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/modifiertheme/InterfaceModifierTheme.fxml"));
		BorderPane root = rootLoader.load();
		Scene sceneSuprimerTheme = new Scene(root, 250, 300);
		unStage.setScene(sceneSuprimerTheme);
		unStage.show();
		unStage.setResizable(false);
	}

	@FXML
	GridPane gridPaneTheme = new GridPane();

	@FXML
	public void afficherTheme() throws SQLException, IOException {
		gridPaneTheme.setHgap(20);
		gridPaneTheme.setVgap(20);
		int compteurDeTheme = 0;
		ResultSet res = null;
		res = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT * FROM THEME ORDER BY TI_THE ASC");
		while (res.next()) {
			compteurDeTheme++;

			interactionTheme unTheme = new interactionTheme(compteurDeTheme, res.getString("TI_THE"),res.getString("Ima_the"));
			lesThemes.add(unTheme);

		}
		int nbLigne = 0;
		if (compteurDeTheme % 3 == 0) {
			nbLigne = compteurDeTheme / 3;
		} else {
			nbLigne = (compteurDeTheme / 3) + 1;
		}

		for (int i = 0; i < nbLigne; i++) {
			RowConstraints row = new RowConstraints(150);
			gridPaneTheme.getRowConstraints().add(row);
		}

		for (int i = 0; i < 3; i++) {
			ColumnConstraints column = new ColumnConstraints(200);
			gridPaneTheme.getColumnConstraints().add(column);
		}
		int compteur = 0, ligne = 0, colonne = 1;
		boutonAjouter();
		while (compteur != compteurDeTheme) {
			gridPaneTheme.add(lesThemes.get(compteur).getvTheme(), colonne, ligne);
			if (colonne < 2) {
				colonne++;
			} else {
				colonne = 0;
				ligne++;
			}
			compteur++;
		}
	}
	@FXML
	private void boutonAjouter() throws FileNotFoundException {
		VBox uneVbox = new VBox();
		uneVbox.setAlignment(Pos.CENTER);
		uneVbox.setStyle("-fx-border-color: BLACK;");
		Button btnText = new Button();
		btnText.setText("Ajouter Thème");
		btnText.setPrefHeight(35);
		btnText.setPrefWidth(200);
		btnText.setStyle("-fx-border-color: transparent;");
		btnText.setStyle("-fx-background-color: #FF8207;");
		btnText.setTextFill(Color.WHITE);
		btnText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					ajouterUnTheme();
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		Button btnImage = new Button();
		ImageView imgPlus = new ImageView();
		imgPlus.setFitWidth(100.0);
		imgPlus.setFitHeight(100.0);
		imgPlus.setImage(new Image(new FileInputStream(".\\images\\plus.png")));
		btnImage.setGraphic(imgPlus);
		btnImage.setStyle("-fx-border-color: transparent;");
		btnImage.setStyle("-fx-background-color: transparent;");
		btnImage.setPrefHeight(100);
		btnImage.setPrefWidth(200);
		btnImage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					ajouterUnTheme();
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		uneVbox.getChildren().add(btnImage);
		uneVbox.getChildren().add(btnText);
		gridPaneTheme.add(uneVbox, 0, 0);
	}

	ArrayList<interactionTheme> lesThemes = new ArrayList<interactionTheme>();

}