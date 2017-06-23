package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import map.*;
import objectMap.*;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//BorderPane root = new BorderPane();
			
			Map test = new Map(300,300, 250,0);
			double rayon = test.calculeRayon(test.getCentre().getCoordX(), test.getCentre().getCoordY(), test.getPointSpand().getCoordX(), test.getPointSpand().getCoordY());
			System.out.println(test.getCentre().getCoordX());
			System.out.println(test.getCentre().getCoordY());
			System.out.println(test.getPointSpand().getCoordX());
			System.out.println(test.getPointSpand().getCoordY());
			System.out.println(rayon);
			//Drawing a Circle 
		      Circle circle = new Circle() ; 
		      Image img = new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIhmV1.0/src/carte1.png");
		      //Setting the properties of the circle 
		      circle.setCenterX(test.getCentre().getCoordX()) ; 
		      circle.setCenterY(test.getCentre().getCoordY()) ; 
		      circle.setRadius(rayon) ; 
		     circle.setFill(new ImagePattern(img));
			 Group root = new Group(circle) ;
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
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
