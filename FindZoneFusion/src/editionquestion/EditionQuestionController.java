
package editionquestion;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import editiontheme.EditionThemeControler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import listtheme.Main;

public class EditionQuestionController {

	@FXML
	public Button btnSelection;
	@FXML
	public TextField intituleDeLaQuestionTextField;
	@FXML
	public static String intituleDeLaQuestion = "";
	@FXML
	public ImageView imageDuTheme = new ImageView();
	@FXML
	public Label messageErreur = new Label();
	@FXML
	public Label lbltitrePage = new Label();
	@FXML
	public AnchorPane anchorImage = new AnchorPane();
	@FXML 
	public AnchorPane anchorZone = new AnchorPane();
	@FXML
	public Polyline zonePolyline ;

	public static ArrayList<Double> listePointPolygoneX = new ArrayList<Double>();
	public static ArrayList<Double> listePointPolygoneY = new ArrayList<Double>();
	public ArrayList<Circle> listCircle = new ArrayList<Circle>();
	public static int idQuestion;
	public static int idCoordonner;
	private boolean valider;
	public static int nbrPoint;
	public static boolean modifier=false;

	@FXML
	private void initialize() throws SQLException {
		if (modifier==false) {
			
			lbltitrePage.setText("Création d'une question");
			System.out.println("creation une question");
		}else {
			System.out.println("modification une question");
			lbltitrePage.setText("Modification d'une question");
		}
		imageDuTheme.setImage(EditionThemeControler.uneImageView2.getImage());
		valider = false;
		ResultSet res = null;
		res = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT MAX(ID_CO) AS MAX FROM COORDONNER");
		while (res.next()) {
			idCoordonner = res.getInt("MAX");
		}
		intituleDeLaQuestionTextField.setText(intituleDeLaQuestion);
		for (Double unDouble : listePointPolygoneX) {
			System.out.println(unDouble);
		}
		this.tracerLaZone();
	}

	public boolean champsRemplie() {
		if ((intituleDeLaQuestion.equals("")) && (nbrPoint == 0)) {
			messageErreur.setText("Saisir la zone de réponse et la question");
			return false;
		}
		if (nbrPoint == 0) {
			messageErreur.setText("La zone de réponse n'est pas Séléctionné");
			return false;
		}
		if (nbrPoint < 3) {
			messageErreur.setText("La zone de réponse : 3 points minimun");
			return false;
		}
		if (intituleDeLaQuestion.equals("")) {
			messageErreur.setText("La question n'a pas était saisie");
			return false;
		}
		return true;
	}

	public boolean verifieQuestionUnique() throws SQLException {
		boolean questionUnique = true;
		ResultSet res = null;
		res = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT Nom_QUE FROM QUESTION");
		while (res.next()) {
			String questionTest;
			questionTest = res.getString(1);
			if (questionTest.equals(intituleDeLaQuestion)) {
				questionUnique = false;
			}
		}
		if (questionUnique == true) {
			return true;
		}
		messageErreur.setText("Cette question existe déjà");
		return false;
	}

	@FXML
	public void validerQuestion() throws IOException, SQLException {
		intituleDeLaQuestion = intituleDeLaQuestionTextField.getText();
		if ((champsRemplie() == true)) {

			Main.findZoneBdd.envoyeUneRequetteDEcriture(
					"DELETE FROM QUESTION WHERE ID_Que=" + EditionQuestionController.idQuestion + ";");
			if (verifieQuestionUnique() == true) {
				String tempo = intituleDeLaQuestion.replace("'", "''");
				Main.findZoneBdd.envoyeUneRequetteDEcriture(
						"INSERT INTO QUESTION VALUES ('" + EditionQuestionController.idQuestion + "','"
								+ EditionThemeControler.ID + "','" + tempo + "');");
				FXMLLoader rootLoader = new FXMLLoader(
						getClass().getResource("/editiontheme/InterfaceEditionTheme.fxml"));
				BorderPane root = rootLoader.load();
				Scene sceneEditionTheme = new Scene(root, 800, 600);
				Main.unPrimaryStage.setScene(sceneEditionTheme);
				this.remiseAZero();
			}
		}

	}

	@FXML
	private void buttonselection() throws SQLException {
		if (valider == false) {
			anchorZone.getChildren().clear();
			zonePolyline = new Polyline();
			zonePolyline.setStrokeWidth(2);
			zonePolyline.setStroke(Color.RED);
			zonePolyline.setFill(Color.rgb(173, 173, 173, 0.35));
			anchorZone.getChildren().add(zonePolyline);
			zonePolyline.getPoints().clear();
			listePointPolygoneX.clear();
			listePointPolygoneY.clear();
			Main.findZoneBdd.envoyeUneRequetteDEcriture("DELETE FROM COORDONNER WHERE ID_QUE_CO = " + idQuestion);
			nbrPoint = 0;
			btnSelection.setText("Valider la zone de réponse");
			valider = true;
		} else {
			anchorZone.getChildren().clear();
			btnSelection.setText("Changer la zone de réponse");
			valider = false;
			this.tracerLaZone();
		}
	}

	@FXML
	private void selectionPolygone(MouseEvent e) throws SQLException {

		if (valider == true) {
			nbrPoint++;
			int abscice = (int) e.getX();
			int ordonner = (int) e.getY();
			listePointPolygoneX.add((double) abscice);
			listePointPolygoneY.add((double) ordonner);
			zonePolyline.getPoints().add((double) abscice);
			zonePolyline.getPoints().add((double) ordonner);
			idCoordonner++;
			Main.findZoneBdd.envoyeUneRequetteDEcriture("INSERT INTO COORDONNER VALUES (" + idCoordonner + ","
					+ idQuestion + "," + EditionThemeControler.ID + "," + abscice + "," + ordonner + ");");
		} else {
			messageErreur.setText("Veuillez cliquer sur le bouton \"Séléctionner Zone\"");
		}
	}

	@FXML
	public void retourPage() throws IOException, SQLException {
		this.remiseAZero();
		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/editiontheme/InterfaceEditionTheme.fxml"));
		BorderPane root = rootLoader.load();
		Scene sceneEditionTheme = new Scene(root, 800, 600);
		Main.unPrimaryStage.setScene(sceneEditionTheme);
	}
	

	@FXML
	public void tracerLaZone() {
		Polygon zonePolygone= new Polygon();
		for (int i = 0; i < nbrPoint; i++) {
			zonePolygone.getPoints().add(listePointPolygoneX.get(i));
			zonePolygone.getPoints().add(listePointPolygoneY.get(i));
		}
		zonePolygone.setFill(Color.rgb(173, 173, 173, 0.35));
		zonePolygone.setStroke(Color.RED);
		zonePolygone.setStrokeWidth(3);
		anchorZone.getChildren().add(zonePolygone);
		supprimerCercleRouge();
	}

	public void supprimerCercleRouge() {
		for (Circle unPoint : listCircle) {
			unPoint.setVisible(false);
		}
	}

	@FXML
	public void pointRouge(MouseEvent e) {
		if (valider == true) {
			Circle point = new Circle(3, Color.RED);
			anchorImage.getChildren().add(point);
			double x, y;
			x = (double) Math.round(e.getX() * 100) / 100;
			y = (double) Math.round(e.getY() * 100) / 100;
			point.setTranslateX(x);
			point.setTranslateY(y);
			listCircle.add(point);
		}

	}

	@FXML
	public void remiseAZero() {
		nbrPoint = 0;
		intituleDeLaQuestion = "";
		imageDuTheme.setImage(null);
		listePointPolygoneX.clear();
		listePointPolygoneY.clear();
	}
}