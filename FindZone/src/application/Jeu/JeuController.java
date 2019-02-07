package application.Jeu;

import java.awt.Polygon;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Menu.MenuController;
import application.Main;
import bdd.BaseDeDonnee;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class JeuController {
	// Base de donn�es
	BaseDeDonnee uneBDD = new BaseDeDonnee("bddfindzone");

	// Des tests à supprimer
	private static int scorePoint;

	private double numeroQuestion = 0;

	// Variables des coordoonées
	private double x;
	private double y;

	// Variables
	private int nombreDeClique = 0;
	private int tempsRestant = 3;
	private boolean isClique = false;
	static int idThemeQuestion = 0;
	private static int nbSeconde = 0;
	private boolean gameOver = false;
	private boolean pause = false;
	private int nbSecondeParQuestion=0;
	// Variable à recuperer sur la BDD
	private ArrayList<String> listeQuestion = new ArrayList<String>();
	private ArrayList<Polygon> listePolygone = new ArrayList<Polygon>();
	private ArrayList<double[]> listeCoordonnerX = new ArrayList<double[]>();
	private ArrayList<double[]> listeCoordonnerY = new ArrayList<double[]>();
	private int[] tab = new int[100];
	private static File fichierImage = new File("../EditFindZone/images/" + idThemeQuestion + ".jpg");
	private static Image imageTheme;

	@FXML
	BorderPane leBorderPane;
	@FXML
	private ProgressBar progression;
	@FXML
	private Text temps;
	@FXML
	private ImageView erreur1;
	@FXML
	private ImageView erreur2;
	@FXML
	private ImageView erreur3;
	@FXML
	private Label question;
	@FXML
	private ImageView image;
	@FXML
	private Canvas zoneReponse;

	@FXML
	private Label scoreTexte;

	@FXML
	private Label progressionTexte;
	@FXML
	private Button questionSuivante;

	@FXML
	private Text tempsChrono;

	ResultSet rsQuestion;
	ResultSet rsCoordonner;

	public void initialize() throws InterruptedException, SQLException, IOException {
		MenuController.parametre(questionSuivante);
		MenuController.parametre(leBorderPane);
		MenuController.parametre(progressionTexte);
		MenuController.parametre(tempsChrono);
		MenuController.parametre(scoreTexte);
		MenuController.parametre(question);
		MenuController.parametre(temps);
		nbSeconde = 0;
		idThemeQuestion = Main.getIdTheme();
		

		erreur1.setVisible(false);
		erreur2.setVisible(false);
		erreur3.setVisible(false);
		
		ChargerBDD();
		progressionTexte.setText("Progression : " + ((int) numeroQuestion+1) + "/" +
				 listeQuestion.size());
		progression.setProgress((numeroQuestion+1)/
				 listeQuestion.size());
		
		question.setText(listeQuestion.get((int) numeroQuestion ));
		scoreTexte.setText("Score : 0 Pts");

		if (fichierImage.exists()) {
			imageTheme = new Image("File:" + fichierImage.getCanonicalPath());
		} else {
			fichierImage = new File("../EditFindZone/images/" + idThemeQuestion + ".jpg");
			if (fichierImage.exists()) {
				imageTheme = new Image("File:" + fichierImage.getCanonicalPath());
			} else
				imageTheme = new Image("File:../EditFindZone/images/404.jpg");

		}
		image.setImage(imageTheme);

		scorePoint = 0;
	
		activerChrono();

	}

	private void activerChrono() {
		if (pause == false) {
			nbSeconde++;
			nbSecondeParQuestion++;
			tempsChrono.setText(nbSeconde + " Secs");
		}
		if (gameOver == false) {
			new Timeline(new KeyFrame(Duration.seconds(1), event -> activerChrono())).play();
		}

	}

	private void ChargerBDD() throws SQLException {
		rsQuestion = uneBDD.envoyeUneRequetteDeLecture("SELECT * FROM Question where id_the_que=" + idThemeQuestion);
		int NumQuestion = 0;
		while (rsQuestion.next()) {
			
			listeQuestion.add(rsQuestion.getString("Nom_Que"));
			rsCoordonner = uneBDD.envoyeUneRequetteDeLecture(
					"SELECT * FROM COORDONNER WHERE Id_que_Co= " + rsQuestion.getInt("Id_que") + " ORDER BY Id_co ASC");
			Polygon unPolygonTemporaire = new Polygon();
			double[] uneListeXTemporaire = new double[100];
			double[] uneListeYTemporaire = new double[100];
			int i = 0;
			while (rsCoordonner.next()) {
				unPolygonTemporaire.addPoint(rsCoordonner.getInt("Ab_co"), rsCoordonner.getInt("Or_co"));
				uneListeXTemporaire[i] = rsCoordonner.getInt("Ab_co");
				uneListeYTemporaire[i] = rsCoordonner.getInt("Or_co");
				i++;
			}
			tab[NumQuestion] = i;
			listePolygone.add(unPolygonTemporaire);
			listeCoordonnerX.add(uneListeXTemporaire);
			listeCoordonnerY.add(uneListeYTemporaire);
			NumQuestion++;
		}
	}

	public void questionSuivante() throws InterruptedException, IOException, SQLException {
		
		progression.setProgress((numeroQuestion+1)/listeQuestion.size());
		erreur1.setVisible(false);
		erreur2.setVisible(false);
		erreur3.setVisible(false);

		pause = false;
		if (numeroQuestion < listeQuestion.size()) {
			isClique = false;
			question.setText(listeQuestion.get((int) numeroQuestion));
			question.setTextFill(ParametreController.getCouleurTexteVar());
			scoreTexte.setText("Score : " + scorePoint + " Pts");
			 progressionTexte.setText("Progression : " + ((int)numeroQuestion +1) + "/" +
			 listeQuestion.size());
		} else {
			gameOver = true;
			FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("ScoreInterface.fxml"));
			BorderPane root = rootLoader.load();
			Scene scoreScene = new Scene(root, 800, 600);

			Main.myPrimaryStage.setScene(scoreScene);
		}
	}

	@FXML
	public void btnQuestionSuivante() throws InterruptedException, IOException, SQLException {
		if (isClique == false) {
			nombreDeClique = 100;
			isClique = true;
			colorierLaZoneEtDecolorier(20);
			updateChrono();
			Timeline timer = new Timeline(new KeyFrame(Duration.seconds(0), event -> updateChrono()));
			timer.play();
			Timeline timer2 = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateChrono()));
			timer2.play();
			Timeline timer3 = new Timeline(new KeyFrame(Duration.seconds(2), event -> updateChrono()));
			timer3.play();
			Timeline timer4 = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
				try {
					finChrono(0);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}));
			timer4.play();

		}
	}

	@FXML
	public void repondreQuestion(MouseEvent e) throws InterruptedException, IOException {
		if (isClique == false) {
			pause = true;
			nombreDeClique++;
			x = e.getX();
			y = e.getY();
			if (listePolygone.get((int) numeroQuestion).contains(x, y)) {
				isClique = true;
				colorierLaZoneEtDecolorier(10);
				updateChrono();
				Timeline timer = new Timeline(new KeyFrame(Duration.seconds(0), event -> updateChrono()));
				timer.play();
				Timeline timer2 = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateChrono()));
				timer2.play();
				Timeline timer3 = new Timeline(new KeyFrame(Duration.seconds(2), event -> updateChrono()));
				timer3.play();
				Timeline timer4 = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
					try {
						finChrono(0);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}));
				timer4.play();
			} else {
				switch (nombreDeClique) {
				case 1:
					erreur1.setVisible(true);
					break;
				case 2:
					erreur2.setVisible(true);
					break;
				case 3:
					erreur3.setVisible(true);
					break;
				}
			}
		}
	}

	private Object finChrono(int i) throws InterruptedException, IOException, SQLException {
		temps.setText("");
		tempsRestant = 3;
		colorierLaZoneEtDecolorier(i);
		ajoutPoint();
		questionSuivante();
		return null;
	}

	private Object updateChrono() {
		temps.setText("Prochaine question dans : " + tempsRestant);
		tempsRestant--;
		return null;
	}

	public void ajoutPoint() {
		if (nombreDeClique == 1)
			scorePoint = scorePoint + 100;
		if (nombreDeClique == 2)
			scorePoint = scorePoint + 50;
		if (nombreDeClique == 3)
			scorePoint = scorePoint + 25;
		if(nbSecondeParQuestion<=2) scorePoint += 10;
		if(nbSecondeParQuestion<=4 && nbSecondeParQuestion > 2) scorePoint += 5;
		nbSecondeParQuestion=0;
		numeroQuestion++;
		nombreDeClique = 0;

	}

	public void colorierLaZoneEtDecolorier(int i) throws InterruptedException {
		GraphicsContext g = zoneReponse.getGraphicsContext2D();
		g.setLineWidth(4);
		if (i == 10) {
			g.setStroke(Color.GREEN);
			question.setTextFill(Color.GREEN);
		} else if (i == 20) {
			g.setStroke(Color.RED);
			question.setTextFill(Color.RED);
		}

		if (i == 0) {
			g.clearRect(0, 0, 500, 500);
		} else {
			g.strokePolygon(listeCoordonnerX.get((int) numeroQuestion), listeCoordonnerY.get((int) numeroQuestion),
					tab[(int) numeroQuestion]);
		}
	}

	public static int getScorePoint() {
		return scorePoint;
	}

	public static int getTemps() {
		return nbSeconde;
	}

	public static Image getImageTheme() {
		return imageTheme;
	}

}
