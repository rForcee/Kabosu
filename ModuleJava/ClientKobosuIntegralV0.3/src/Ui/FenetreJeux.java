package Ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import Com.DataTrameMap;
import Com.DataTrameMeteo;
import Com.TestHttp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import map.*;
import objectIhm.*;
import objectMap.Client;
import objectMap.Joueur;
public class FenetreJeux extends Application {
	@Override
	public void start(Stage primaryStage) {
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
				data1 = TestHttp.traitementTrameMap(TestHttp.getMap(urlGet)); // get vers https://kabosu.herokuapp.com/map
				data2 = TestHttp.traitementTrameMetrology(TestHttp.getMap(urlGet2));


			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int nbClient =24;
			Meteo IcoMeteo = new Meteo();
			IcoMeteo.setMeteo(data2.getWeather()); // selectionne la l'icone en fonction de la météo
			Map test = new Map(nbClient,data1);
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
			ArrayList<Joueur> joueur = new ArrayList<Joueur>();

			joueur.addAll(test.initlistJoueur());
			test.generationMap(root);
			test.generationPopulationStand(root);
			//test.generationPopulationPub(root, 1);
			IcoMeteo.afficheMeteo(root, scene);
			test.generationPopulationClient(root,50); 
			heure.setText(String.valueOf(date.heureJeux(data2.getHeure()))); // affectation heure de jeux
			jour.setText(String.valueOf(date.jourJeux(data2.getHeure()))); // affectation jour*/

			new Thread(new Runnable() {
				@Override public void run() {

					for (int i = 1; i <= 1000000; i++) {
						final int counter = i;
						Platform.runLater(new Runnable() {

							@Override public void run() {
								Platform.runLater(new Runnable() {

									@Override
									public void run() {

										HashMap<String, Client> client = new HashMap<String, Client>();
										DataTrameMap data1 = new DataTrameMap ();
										DataTrameMeteo data2 = new DataTrameMeteo();
										heure.setText(String.valueOf(date.heureJeux(data2.getHeure()))); // affectation heure de jeux
										jour.setText(String.valueOf(date.jourJeux(data2.getHeure()))); // affectation jour*/
										IcoMeteo.afficheMeteo(root, scene);
										Thread.interrupted();
										try {
											
											client = (HashMap<String, Client>) test.generationPopulationClient(root,50).clone();
											
											heure.setText(String.valueOf(date.heureJeux(data2.getHeure()))); // affectation heure de jeux
											jour.setText(String.valueOf(date.jourJeux(data2.getHeure()))); // affectation jour*/
											IcoMeteo.afficheMeteo(root, scene);
										} catch (Exception e2) {
											// TODO Auto-generated catch block
											e2.printStackTrace();
										} 

										try {
											URL urlPost = new URL("https://kabosu.herokuapp.com/sales");
											heure.setText(String.valueOf(date.heureJeux(data2.getHeure()))); // affectation heure de jeux
											jour.setText(String.valueOf(date.jourJeux(data2.getHeure()))); // affectation jour*/
											IcoMeteo.afficheMeteo(root, scene);
										} catch (MalformedURLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										URL urlGet = null;
										try {
											urlGet = new URL("https://kabosu.herokuapp.com/map");
											IcoMeteo.afficheMeteo(root, scene);

										} catch (MalformedURLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} 
										URL urlGet2 = null;
										try {
											urlGet2 = new URL("https://kabosu.herokuapp.com/metrology");
											
											heure.setText(String.valueOf(date.heureJeux(data2.getHeure()))); // affectation heure de jeux
											jour.setText(String.valueOf(date.jourJeux(data2.getHeure()))); // affectation jour*/
											IcoMeteo.afficheMeteo(root, scene);
											
										} catch (MalformedURLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} 
										try {
											data1 = TestHttp.traitementTrameMap(TestHttp.getMap(urlGet));
											Thread.sleep(50);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} // get vers https://kabosu.herokuapp.com/map
										try {
											Thread.sleep(50);
											data2 = TestHttp.traitementTrameMetrology(TestHttp.getMap(urlGet2));
											
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										Thread.interrupted();
										heure.setText(String.valueOf(date.heureJeux(data2.getHeure()))); // affectation heure de jeux
										jour.setText(String.valueOf(date.jourJeux(data2.getHeure()))); // affectation jour*/
										IcoMeteo.afficheMeteo(root, scene);
										test.simulation(client, joueur, IcoMeteo);
										
										heure.setText(String.valueOf(date.heureJeux(data2.getHeure()))); // affectation heure de jeux
										jour.setText(String.valueOf(date.jourJeux(data2.getHeure()))); // affectation jour*/
										IcoMeteo.afficheMeteo(root, scene);
										//root.getChildren().clear();


										System.out.println("reset");
										try {
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});

							}
						});
					}
					
				}
			}).start();

			((Group) scene.getRoot()).getChildren().add(vbox);
			root.getChildren().add(gridpane);
			//new Thread(task).start();
			primaryStage.setScene(scene);

			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}


}
