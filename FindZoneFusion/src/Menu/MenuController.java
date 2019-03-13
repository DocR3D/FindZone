package Menu;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Presentation.PresentationController;
import application.Main;
import application.Jeu.ParametreController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MenuController {

	public Stage myPrimaryStage;

	@FXML
	private Button btnQuitter;

	@FXML
	private BorderPane leborderPane;

	@FXML
	private Button parametre;

	@FXML
	private Button commentJouer;
	
	@FXML
	private Button image;

	int sizeLigne;
	@FXML
	private GridPane photo;

	@FXML
	private ImageView imageParametre;
	@FXML
	private Label titre;

	@FXML
	private void quitter() {
		Main.myPrimaryStage.close();
	}

	@FXML
	private void modifParametre() throws IOException {
		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/application/Jeu/parametreInterface.fxml"));
		BorderPane root = rootLoader.load();
		Scene scenePresentation = new Scene(root, 800, 600);
		Main.myPrimaryStage.setScene(scenePresentation);

	}

	public void initGame(int sizeX, int sizeY, List<Theme> listTheme , int nb) throws SQLException, IOException {
		int numElement =0 ;
		photo.setHgap(20);
		photo.setVgap(20);
		GameElement.setController(this);
		for (int i = 0; i < sizeX; i++) {
			RowConstraints row = new RowConstraints(200);
			photo.getRowConstraints().add(row);
		}

		for (int i = 0; i < sizeY; i++) {
			ColumnConstraints column = new ColumnConstraints(150);
			photo.getColumnConstraints().add(column);
		}
		for (int i = 0; i < sizeX; i++) {
			for (int j = 0; j < sizeY; j++) {
				if ( nb > numElement) {
					photo.add(GameElement.generateElement(listTheme.get(numElement)), j, i);
					numElement ++;
				}
			}
		}
	

	}

	@FXML
	private void Image() throws SQLException, IOException {
		ResultSet res = null;
		res = Main.findZoneBdd.envoyeUneRequetteDeLecture("SELECT IMA_THE FROM THEME");
		while (res.next()) {
			@SuppressWarnings("unused")
			String ID = res.getString("IMA_THE");
		}
	}

	public void initialize() throws SQLException, IOException {
		parametre(btnQuitter);
		parametre(parametre);
		parametre(titre);
		parametre(leborderPane);
		parametre(photo);
		int nb=0;
		ResultSet res1 = null;
		File file = new File("IMAGE/gear-option.png");
		System.out.println(file.toURI().toString());
        Image image = new Image(file.toURI().toString());
		imageParametre.setImage(image);
		res1 = Main.findZoneBdd
				.envoyeUneRequetteDeLecture("SELECT TI_THE ,IMA_THE, AUT_THE, ID_THE FROM THEME ORDER BY TI_THE ASC ");
		List<Theme> listTheme = new ArrayList<>();
		while (res1.next()) {
			Theme theme = new Theme(res1.getString("TI_THE"), res1.getString("IMA_THE"), res1.getString("AUT_THE"),
					res1.getString("ID_THE"));
			
			listTheme.add(theme);
			nb ++;
		}
		int modulo = (nb % 3);

		if (modulo == 0) {
			sizeLigne = nb / 3;
		} else {
			sizeLigne = nb / 3 + 1;
		}
		initGame(sizeLigne, 3, listTheme, nb);
	}

	public void LancePresentation(Theme theme) throws IOException, SQLException {
		Main.setIdTheme(theme.getId());
		FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/Presentation/InterfacePresentation.fxml"));
		BorderPane root = rootLoader.load();
		((PresentationController) rootLoader.getController()).setThemeAndlnit(theme);

		Scene scenePresentation = new Scene(root, 800, 600);

		Main.myPrimaryStage.setScene(scenePresentation);

	}

	@FXML
	private void pageCommentJouer() {
		Main.myPrimaryStage.close();

	}

	public static void parametre(Label label) {
		label.setTextFill(ParametreController.getCouleurTexteVar());
		label.setFont(Font.font(ParametreController.getChoixPoliceVar(), FontWeight.NORMAL,
				Integer.parseInt(label.getFont().toString().substring(label.getFont().toString().length() - 5,
						label.getFont().toString().length() - 3))));

	}

	public static void parametre(Text text) {
		text.setFill(ParametreController.getCouleurTexteVar());
		text.setFont(Font.font(ParametreController.getChoixPoliceVar(), FontWeight.NORMAL,
				Integer.parseInt(text.getFont().toString().substring(text.getFont().toString().length() - 5,
						text.getFont().toString().length() - 3))));

	}

	public static void parametre(BorderPane borderPane) {
		borderPane.setStyle("-fx-background-color : " + ParametreController.getCouleurFondVar());
	}

	public static void parametre(GridPane unGridPane) {
		unGridPane.setStyle("-fx-background-color : " + ParametreController.getCouleurFondVar() + "; -fx-border-color: "
				+ ParametreController.getCouleurFondVar());

	}
	public static void parametre(Button button){
		button.setStyle("-fx-background-color : " + ParametreController.getCouleurBouton());
	}

}