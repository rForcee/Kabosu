package application;

import java.net.URL;
import java.util.HashMap;

import Com.DataTrameMap;
import Com.DataTrameMeteo;
import Com.TestHttp;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import map.*;
import objectMap.*;
import objectIhm.*;
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
	
		//TODO faire des methodes des elemnt dans le maine tfaire une class Ui
		try {
			Group root = new Group();
			Scene scene = new Scene(root,1000,1000,Color.GREY);
			DataTrameMap data1 = new DataTrameMap ();
			DataTrameMeteo data2 = new DataTrameMeteo();
			Date date = new Date();
			TestHttp reseau = new TestHttp();
			try {
				URL urlPost = new URL("https://kabosu.herokuapp.com/sales");
				URL urlGet = new URL("https://kabosu.herokuapp.com/map"); 
				URL urlGet2 = new URL("https://kabosu.herokuapp.com/metrology"); 
				//data1 = TestHttp.traitementTrameMap(TestHttp.getMap(urlGet)); // get vers https://kabosu.herokuapp.com/map
				data2 = TestHttp.traitementTrameMetrology(TestHttp.getMap(urlGet2));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int nbClient =24;
			Meteo IcoMeteo = new Meteo();
			IcoMeteo.setMeteo(data2.getWeather()); // selectionne la l'icone en fonction de la m�t�o
			Map test = new Map(nbClient,data1);
			HashMap<String,  Rectangle> mymapRec; 
			GridPane gridpane = new GridPane();
			gridpane.setPadding(new Insets(5));
			gridpane.setHgap(10);
			gridpane.setVgap(10);

			VBox vbox = new VBox();
			vbox.setLayoutX(150);
			vbox.setLayoutY(90);
			
			
			Label labelJour = new Label("jour:");
			GridPane.setHalignment(labelJour, HPos.LEFT);
			gridpane.add(labelJour, 0, 0);
			TextField jour = new TextField ();
			jour.setText(String.valueOf(date.jourJeux(data2.getHeure()))); // affectation jour
			gridpane.add(jour, 1, 0);
			Label labelHeure = new Label("Heure:");
			GridPane.setHalignment(labelHeure, HPos.LEFT);
			gridpane.add(labelHeure, 0, 1);
			TextField heure = new TextField ();
			heure.setText(String.valueOf(date.heureJeux(data2.getHeure()))); // affectation heure de jeux
			gridpane.add(heure, 1, 1);
		
			test.generationMap(root);
		
			mymapRec = test.generationPopulationClient(root,50, IcoMeteo); 
			IcoMeteo.afficheMeteo(root, scene);
			
			
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			((Group) scene.getRoot()).getChildren().add(vbox);
			root.getChildren().add(gridpane);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
