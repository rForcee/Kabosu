package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import map.Map;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//BorderPane root = new BorderPane();
			
			Map test = new Map(250,250, 200,0);
			double rayon = test.calculeRayon(test.centre.getCoordX(), test.centre.getCoordY(), test.pointSpand.getCoordX(), test.pointSpand.getCoordY());
			System.out.println(test.centre.getCoordX());
			System.out.println(test.centre.getCoordY());
			System.out.println(test.pointSpand.getCoordX());
			System.out.println(test.pointSpand.getCoordY());
			System.out.println(rayon);
			//Drawing a Circle 
		      Circle circle = new Circle() ; 
		      Image img = new Image("carte1.png");
		      //Setting the properties of the circle 
		      circle.setCenterX(test.centre.getCoordX()) ; 
		      circle.setCenterY(test.centre.getCoordY()) ; 
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
