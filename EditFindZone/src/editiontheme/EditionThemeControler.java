package editiontheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import editionquestion.EditionQuestionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import listtheme.Main;

public class EditionThemeControler {

	@FXML
	public TextField titreTextField = new TextField();
	@FXML
	public TextField auteurTextField = new TextField();
	@FXML
	public ComboBox<String> choisirLaDifficulte = new ComboBox<String>();
	@FXML
	public static ImageView uneImageView2 = new ImageView();
	@FXML
	public ImageView uneImageView = new ImageView();
	@FXML
	public VBox vboxLesQuestions = new VBox();

	@FXML
	public Label messageErreue = new Label();
	@FXML
    private Button btnImporterImage = new Button();
	@FXML
	public ImageView imageDeTheme;
	@FXML
	public Label titrePage = new Label();

	public static int ID;
	public static String auteur;
	public static String titre;
	public static String cheminImageTheme = null;
	public static String valeurDifficulte;
	public static Stage unStage = new Stage();
	public static File imageAcopier;
	public static boolean modifier = false;

	@FXML
	private void initialize() throws SQLException, IOException {
		if (modifier==false) {
			titrePage.setText("Création d'un thème");
		}else
			titrePage.setText("Modification d'un thème");
		if (titre != null) {
			chargementDesElementDuTheme();
			ObservableList<String> difficulter = FXCollections.observableArrayList("Très difficile", "Difficile",
					"Moyen", "Facile");
			choisirLaDifficulte.setItems(difficulter);
		} else {
			ObservableList<String> difficulter = FXCollections.observableArrayList("Très difficile", "Difficile",
					"Moyen", "Facile", "Trés facile");
			choisirLaDifficulte.setValue("Moyen");
			choisirLaDifficulte.setItems(difficulter);
		}
		affichageQuestion();
		if (cheminImageTheme != null) {
			this.chargerImage(cheminImageTheme);
		}
		this.netoyageDeLaBDD();
	}

	@FXML
	private void ajouterUneQuestion() throws IOException, SQLException {
		this.sauvegardeDesElementEntrer();
		if (verificationChampsRemplie()) {
			ResultSet res = null;
			res = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT MAX(ID_QUE) AS MAX FROM QUESTION");
			while (res.next()) {
				EditionQuestionController.idQuestion = res.getInt("MAX");
			}
			EditionQuestionController.idQuestion++;
			EditionQuestionController.modifier=false;
			FXMLLoader rootLoader = new FXMLLoader(
					getClass().getResource("/editionquestion/InterfaceEditionQuestion.fxml"));
			BorderPane root = rootLoader.load();
			Scene sceneEditionTheme = new Scene(root, 800, 600);
			Main.unPrimaryStage.setScene(sceneEditionTheme);
		}
	}

	@FXML
	private void retourAuMenu() throws IOException, SQLException {
		if ((modifier == false) && (cheminImageTheme != null)) {
			Path lienImagesASuprimer = Paths.get(".\\images\\" + EditionThemeControler.ID + ".jpg");
			Files.delete(lienImagesASuprimer);
		}
		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/listtheme/InterfaceListTheme.fxml"));
		BorderPane root = rootLoader.load();
		Scene sceneEditionTheme = new Scene(root, 800, 600);
		Main.unPrimaryStage.setScene(sceneEditionTheme);
		this.remiseAZeroDesVariable();
	}

	@FXML
    private void valider() throws IOException, SQLException {
        sauvegardeDesElementEntrer();
        if (verificationChampsRemplie() == true) {
        	
        	 Main.findZoneBdd.envoyeUneRequetteDEcriture("DELETE FROM THEME WHERE ID_The = " + EditionThemeControler.ID + ";");
        	 
              if (verifieTitreUnique() == true) {
                String titreTempo = titre.replaceAll("'", "''");
                String auteurTempo = auteur.replaceAll("'", "''");
                Main.findZoneBdd.envoyeUneRequetteDEcriture("INSERT INTO THEME VALUES (" + EditionThemeControler.ID + ",'" + titreTempo
                        + "','" + auteurTempo + "','.\\images\\" + EditionThemeControler.ID + ".jpg','" + valeurDifficulte + "');");
                FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/listtheme/InterfaceListTheme.fxml"));
                BorderPane root = rootLoader.load();
                Scene sceneEditionTheme = new Scene(root, 800, 600);
                Main.unPrimaryStage.setScene(sceneEditionTheme);
                this.remiseAZeroDesVariable();
            }
        }
    }

	@FXML
	private void importeUneImage() throws IOException {
		FileChooser image = new FileChooser();
		image.setTitle("Image de fond");
		cheminImageTheme = image.showOpenDialog(Main.unPrimaryStage).getAbsoluteFile().getAbsolutePath();
		this.copieDeLimage(cheminImageTheme);
		this.chargerImage(cheminImageTheme);		
	}

	@FXML
	private void closeButtonAction() {
		Main.unPrimaryStage.close();
	}

	public ArrayList<interactionQuestion> lesQuestions = new ArrayList<interactionQuestion>();

	@FXML
	private void affichageQuestion() throws SQLException {
		ResultSet res = null;
		res = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT * FROM QUESTION WHERE ID_THE_QUE = " + ID);
		while (res.next()) {
			interactionQuestion uneQuestion = new interactionQuestion(res.getString("NOM_QUE"));
			Button unbtn = new Button();
			unbtn.setText(res.getString("NOM_QUE"));
			vboxLesQuestions.getChildren().add(uneQuestion.gethQuestion());
		}
	}

	@FXML
	public void chargementDesElementDuTheme() {
		titreTextField.setText(titre);
		auteurTextField.setText(auteur);
		choisirLaDifficulte.setValue(valeurDifficulte);
	}

	@FXML
	public void sauvegardeDesElementEntrer() {
		titre = titreTextField.getText();
		auteur = auteurTextField.getText();
		valeurDifficulte = choisirLaDifficulte.getValue();
	}

	@FXML
	public void remiseAZeroDesVariable() {
		titre = null;
		auteur = null;
		cheminImageTheme = null;
		valeurDifficulte = null;
		modifier=false;
	}

	@FXML
	public void modifierUneQuestion() throws IOException {
		FXMLLoader rootLoader = new FXMLLoader(
				getClass().getResource("/modifierquestion/InterfaceModifierQuestion.fxml"));
		BorderPane root = rootLoader.load();
		Scene sceneModificationQuestion = new Scene(root, 250, 300);
		unStage.setScene(sceneModificationQuestion);
		unStage.show();
		unStage.setResizable(false);
	}

	public void chargerImage(String cheminImageTheme) throws IOException {
		FileInputStream image = new FileInputStream(cheminImageTheme);
		imageDeTheme.setImage(new Image(image));
		image.close();
		FileInputStream image2 = new FileInputStream(cheminImageTheme);
		uneImageView.setImage(new Image(image2));
		uneImageView2.setImage(uneImageView.getImage());
		image2.close();
		
	}

	public void netoyageDeLaBDD() throws SQLException {
		int idQuestionDeCoordonner;
		int idQuestion;
		boolean verif = true;
		ResultSet resCoordonner = null;
		resCoordonner = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT * FROM COORDONNER");
		while (resCoordonner.next()) {
			idQuestionDeCoordonner = resCoordonner.getInt("ID_QUE_CO");
			ResultSet resQuestion = null;
			resQuestion = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT * FROM QUESTION");
			while (resQuestion.next()) {
				idQuestion = resQuestion.getInt("ID_QUE");
				if (idQuestionDeCoordonner == idQuestion) {
					verif = false;
				}
			}
			if (verif == true) {
				Main.findZoneBdd.envoyeUneRequetteDEcriture(
						"DELETE FROM COORDONNER WHERE ID_QUE_CO = " + idQuestionDeCoordonner);
			}
			verif = true;
		}
	}

	public void copieDeLimage(String chemin) throws IOException {
		Path source = Paths.get(chemin);
		Path destination = Paths.get(".\\images\\" + EditionThemeControler.ID + ".jpg");
		Files.copy(source, destination,StandardCopyOption.REPLACE_EXISTING);
		cheminImageTheme = ".\\images\\" + EditionThemeControler.ID + ".jpg";
		
	}

	public boolean verificationChampsRemplie() throws SQLException {
        if (titre.equals("")) {
            messageErreue.setText("Veuillez saisir un nom de thème");
            titreTextField.setStyle("-fx-border-color: RED;");
            return false;
        }
        if (titre.length() > 24) {
            titreTextField.setStyle("-fx-border-color: RED;");
            messageErreue.setText("Maximun 24 caractére pour le titre");
            return false;
        }
        if (auteur.equals("")) {
            messageErreue.setText("Veuillez saisir un auteur");
            titreTextField.setStyle("-fx-border-color: NOIR;");
            auteurTextField.setStyle("-fx-border-color: RED;");
            return false;
        }
        if (auteur.length() > 24) {
            titreTextField.setStyle("-fx-border-color: NOIR;");
            auteurTextField.setStyle("-fx-border-color: RED;");
            messageErreue.setText("Maximun 24 caractére pour l'auteur");
            return false;
        }
        if (cheminImageTheme == null) {
            auteurTextField.setStyle("-fx-border-color: NOIR;");
            btnImporterImage.setStyle("-fx-border-color: RED;");
            messageErreue.setText("Veuillez importer une image");
            return false;
        }
        return true;
    }
	
    public boolean verifieTitreUnique() throws SQLException {
        boolean titreUnique = true;

        ResultSet res = null;
        res = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT Ti_THE FROM THEME");
        while (res.next()) {
            String questionTest;
            questionTest = res.getString(1);
            if (questionTest.equals(titre)) {
                titreUnique = false;
            }
        }
        if (titreUnique == true) {
            return true;
        }
        messageErreue.setText("Ce nom de thème existe déjà");
        titreTextField.setStyle("-fx-border-color: RED;");
        return false;
    }

}
