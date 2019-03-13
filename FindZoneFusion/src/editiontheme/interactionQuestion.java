package editiontheme;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import editionquestion.EditionQuestionController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import listtheme.Main;

public class interactionQuestion {

	private HBox hQuestion = new HBox();
	private Button btnSupprimer = new Button();
	private Button btnModifier = new Button();
	private Label lblNomQuestion = new Label();
	private String nomQuestion;

	public interactionQuestion(String nomQuestion) {
		super();
		this.nomQuestion = nomQuestion;

		btnSupprimer.setText("Supprimer");
		btnSupprimer.setPrefWidth(100);
		btnSupprimer.setStyle("-fx-border-color: transparent;");
		btnSupprimer.setStyle("-fx-background-color: RED;");
		btnSupprimer.setPrefHeight(35);
		btnSupprimer.setTextFill(Color.WHITE);
		btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					supprimerQuestion();
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		btnModifier.setText("Modifier");
		btnModifier.setPrefWidth(100);
		btnModifier.setStyle("-fx-border-color: transparent;");
		btnModifier.setStyle("-fx-background-color: GREEN;");
		btnModifier.setPrefHeight(35);
		btnModifier.setTextFill(Color.WHITE);
		btnModifier.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					modifierQuestion();
				} catch (SQLException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
		lblNomQuestion.setText(nomQuestion);
		lblNomQuestion.setPrefWidth(535);
		lblNomQuestion.setFont(Font.font(15));
		
		Label espace = new Label();
		espace.setPrefWidth(10);
		
		hQuestion.setSpacing(10);
		hQuestion.setAlignment(Pos.CENTER_RIGHT);
		hQuestion.setStyle("-fx-border-color: BLACK;");
		hQuestion.getChildren().add(espace);
		hQuestion.getChildren().add(lblNomQuestion);
		hQuestion.getChildren().add(btnModifier);
		hQuestion.getChildren().add(btnSupprimer);
	}

	private void supprimerQuestion() throws SQLException, IOException {
		ResultSet res = null;

		String nomQuestionTempo = nomQuestion.replaceAll("'", "''");
		res = Main.findZoneBdd
				.envoyeUneRequetteDeLecture("SELECT ID_QUE FROM QUESTION WHERE NOM_QUE = '" + nomQuestionTempo + "';");
		int id = 0;
		while (res.next()) {
			id = res.getInt("ID_QUE");
		}
		Main.findZoneBdd.envoyeUneRequetteDEcriture("DELETE FROM QUESTION WHERE ID_QUE=" + id + ";");
		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/editiontheme/InterfaceEditionTheme.fxml"));
		BorderPane root = rootLoader.load();
		Scene sceneEditionTheme = new Scene(root, 800, 600);
		Main.unPrimaryStage.setScene(sceneEditionTheme);
	}

	private void modifierQuestion() throws SQLException, IOException {
		int idDeLaQuestion = 0;
		ResultSet res = null;
		String nomQuestionTempo = nomQuestion.replaceAll("'", "''");
		res = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT * FROM QUESTION WHERE Nom_Que = '" + nomQuestionTempo + "';");
		// On cherche quel est la question que l'utilisateur souhaite selectioner
		while (res.next()) {
			idDeLaQuestion = res.getInt("Id_que");
		}
		EditionQuestionController.intituleDeLaQuestion = nomQuestion;

		res = Main.findZoneBdd
				.envoyeUneRequetteDeLecture("SELECT * FROM COORDONNER WHERE Id_Que_Co =" + idDeLaQuestion + ";");
		int compteur = 0;
		while (res.next()) {
			EditionQuestionController.listePointPolygoneX.add(res.getDouble("Ab_CO"));
			EditionQuestionController.listePointPolygoneY.add(res.getDouble("OR_CO"));
			compteur++;
		}
		EditionQuestionController.modifier=true;
		EditionQuestionController.nbrPoint = compteur;
		EditionQuestionController.idQuestion = idDeLaQuestion;
		FXMLLoader rootLoader = new FXMLLoader(
				getClass().getResource("/editionquestion/InterfaceEditionQuestion.fxml"));
		BorderPane root = rootLoader.load();
		Scene sceneEditionQuestion = new Scene(root, 800, 600);
		Main.unPrimaryStage.setScene(sceneEditionQuestion);
		EditionThemeControler.unStage.close();
	}

	public HBox gethQuestion() {
		return hQuestion;
	}
	
	

}
