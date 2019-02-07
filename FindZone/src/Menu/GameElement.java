package Menu;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameElement {

	private static MenuController controller;

	public static Pane generateElement(Theme theme) {
		
		ImageView image = new ImageView(new Image("File:../EditFindZone/Images/" +  theme.getId() +".jpg"));
		
		image.setFitHeight(100);
		image.setFitWidth(150);

		image.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					controller.LancePresentation(theme);
				} catch (IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		VBox box = new VBox();
		box.setStyle("-fx-border-color: black;\n");
		box.setSpacing(15);
		box.setAlignment(Pos.CENTER);
		
		Label label = new Label(theme.getNom());
		label.setFont(new Font("Arial", 12));
		label.setTextFill(Color.WHITE);
		box.getChildren().add(image);
		box.getChildren().add(label);
		return box;
	}

	public static void setController(MenuController menuController) {
		controller=menuController;
		
	}

}
