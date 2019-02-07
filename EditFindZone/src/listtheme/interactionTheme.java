package listtheme;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

import editiontheme.EditionThemeControler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class interactionTheme {

	private Button unButtonModifier = new Button();
	private Button unButtonSupprimer = new Button();
	private String nomTheme;
	private Label lblNomTheme = new Label();
	private ImageView uneImageView = new ImageView();
	private VBox vTheme = new VBox();
	private HBox hTheme = new HBox();

	
	public Button getUnButtonModifier() {
		return unButtonModifier;
	}
	public Button getUnButtonSupprimer() {
		return unButtonSupprimer;
	}
	public String getNomTheme() {
		return nomTheme;
	}
	public ImageView getUneImageView() {
		return uneImageView;
	}
	
	
	public Label getLblNomTheme() {
		return lblNomTheme;
	}
	public interactionTheme(int num, String nomTheme, String cheminImage) throws IOException {
		super();
		this.nomTheme = nomTheme;
		lblNomTheme.setText(nomTheme);
		lblNomTheme.setFont(Font.font(15));
		lblNomTheme.setTextFill(Color.web("#FFF"));
		FileInputStream image = new FileInputStream(cheminImage);
		uneImageView.setImage(new Image(image));
		image.close();
		uneImageView.setFitWidth(150.0);
		uneImageView.setFitHeight(93.0);
		
		unButtonModifier.setText("Modifier");
		unButtonModifier.setPrefHeight(35);
		unButtonModifier.setPrefWidth(100);
		unButtonModifier.setStyle("-fx-border-color: transparent;");
		unButtonModifier.setStyle("-fx-background-color: GREEN;");
		unButtonModifier.setTextFill(Color.WHITE);
		unButtonModifier.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					modifier();
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		unButtonSupprimer.setText("Supprimer");
		unButtonSupprimer.setPrefHeight(35);
		unButtonSupprimer.setPrefWidth(100);
		unButtonSupprimer.setStyle("-fx-border-color: transparent;");
		unButtonSupprimer.setStyle("-fx-background-color: RED;");
		unButtonSupprimer.setTextFill(Color.WHITE);
		unButtonSupprimer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					supprimer();
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		Separator unSeparator = new Separator();
		unSeparator.setOrientation(Orientation.VERTICAL);
		vTheme.setAlignment(Pos.CENTER);
		vTheme.setStyle("-fx-border-color: BLACK;");
		vTheme.getChildren().add(uneImageView);
		vTheme.getChildren().add(lblNomTheme);
		vTheme.getChildren().add(hTheme);
		
		hTheme.setAlignment(Pos.CENTER);
		hTheme.setPrefHeight(50);
		hTheme.setStyle("-fx-border-color: BLACK;");
		hTheme.getChildren().add(unButtonModifier);
		hTheme.getChildren().add(unSeparator);
		hTheme.getChildren().add(unButtonSupprimer);
	}
	
	
	@FXML
	public void modifier() throws SQLException, IOException {
		EditionThemeControler.modifier=true;
		int idThemeAModifier = 0;
		String titreThemeAModifier = null;
		String auteurThemeAModifier = null;
		String imageThemeAModifier = null;
		String dificulteThemeAModifier = null;
		
		ResultSet res = null;
		String nomThemeTempo = nomTheme.replaceAll("'", "''");
		res = Main.findZoneBdd
				.envoyeUneRequetteDeLecture("SELECT * FROM THEME WHERE Ti_THE = '" + nomThemeTempo + "';");
		while(res.next()) {
			idThemeAModifier = res.getInt("ID_THE");
			titreThemeAModifier = res.getString("Ti_THE");
			auteurThemeAModifier = res.getString("Aut_THE");
			imageThemeAModifier = res.getString("Ima_THE");
			dificulteThemeAModifier = res.getString("Dif_THE");
		}
		EditionThemeControler.ID = idThemeAModifier;
		EditionThemeControler.titre = titreThemeAModifier;
		EditionThemeControler.auteur = auteurThemeAModifier;
		EditionThemeControler.cheminImageTheme = imageThemeAModifier;
		EditionThemeControler.valeurDifficulte = dificulteThemeAModifier;
		ListThemeControler.unStage.close();
		
		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/editiontheme/InterfaceEditionTheme.fxml"));
		BorderPane root = rootLoader.load();
		Scene sceneEditionTheme = new Scene(root, 800, 600);
		Main.unPrimaryStage.setScene(sceneEditionTheme);
	}

	@FXML
	public void supprimer()throws SQLException, IOException {
		String titreTempo = nomTheme.replaceAll("'", "''");
		ResultSet res = null; 
		res = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT * FROM THEME WHERE TI_THE ='"+titreTempo+"';");
		while(res.next()) {
			int idTheme = res.getInt("Id_THE");
			Path lienImagesASuprimer = Paths.get(".\\images\\"+idTheme+".jpg");
			Files.delete(lienImagesASuprimer);
		}
		Main.findZoneBdd.envoyeUneRequetteDEcriture("DELETE FROM THEME WHERE TI_THE='" + titreTempo + "';");
		
		Main.findZoneBdd.envoyeUneRequetteDEcriture("DELETE FROM THEME WHERE TI_THE='" + titreTempo + "';");
		
		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/listtheme/InterfaceListTheme.fxml"));
		BorderPane root = rootLoader.load();
		Scene sceneEditionTheme = new Scene(root, 800, 600);
		Main.unPrimaryStage.setScene(sceneEditionTheme);
	}
	
	public VBox getvTheme() {
		return vTheme;
	}
	public void setvTheme(VBox vTheme) {
		this.vTheme = vTheme;
	}
	public HBox gethTheme() {
		return hTheme;
	}
	public void sethTheme(HBox hTheme) {
		this.hTheme = hTheme;
	}
	
	
	

	
	
}
