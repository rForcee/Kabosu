package map;
import java.util.HashMap;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import map.Position;
import map.Spand;
import objectMap.Client;
public class Map 
{
	private Position centre ;
	private Spand pointSpand ;
	private Image skinMap = new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIhmV1.0/src/skyrim.png");
	public Map(float X,float Y,float Sx,float Sy, int nbClient){
		
		this.setCentre(new Position(X,Y));
		this.setPointSpand(new Spand(Sx,Sy));

	}
	
	public void generationMap(Group root){
		double rayon = calculeRayon(getCentre().getCoordX(), getCentre().getCoordY(), getPointSpand().getCoordX(),getPointSpand().getCoordY());
		System.out.println(rayon);
		Circle circle = new Circle() ; 
		circle.setCenterX(getCentre().getCoordX()) ; 
		circle.setCenterY(getCentre().getCoordY()) ; 
		circle.setRadius(rayon) ; 
		circle.setFill(new ImagePattern(getSkinMap())); // ajoute le skins de la map dans le cercle
		root.getChildren().add(circle);
	}
	public HashMap<String, Rectangle> generationPopulationClient(Group root, int nbClient){
		HashMap<String, Client> mymapClient = new HashMap<String, Client>();
		HashMap<String,  Rectangle> mymapRec = new HashMap<String,  Rectangle>();
		for (int i=0; i<nbClient; i++)
		{
		     mymapClient.put("client"+i, new Client());
		     mymapRec.put("rectangle"+i, new Rectangle());
		     mymapRec.get("rectangle"+i).setX(mymapClient.get("client"+i).getX());
		     mymapRec.get("rectangle"+i).setY(mymapClient.get("client"+i).getY());
		     mymapRec.get("rectangle"+i).setWidth(15);
		     mymapRec.get("rectangle"+i).setHeight(15);
		     mymapRec.get("rectangle"+i).setFill(new ImagePattern(mymapClient.get("client"+i).getSkinClient()));
		     System.out.println("client"+i+": "+mymapClient.get("client"+i).getX());
		     System.out.println(mymapClient.get("client"+i).getY());
		     root.getChildren().add(mymapRec.get("rectangle"+i));
		}
		return mymapRec;
	}
	
	
	public Double calculeRayon(float X,float Y,float Sx,float Sy){
		double tmpCal = ((Y-X)*(Y-X))+((Sy-Sx)*(Sy-Sx));
		System.out.println("tmp: "+tmpCal);
		 return Math.sqrt(Math.abs(tmpCal));
	}


	public Position getCentre() {
		return centre;
	}


	public void setCentre(Position centre) {
		this.centre = centre;
	}


	public Spand getPointSpand() {
		return pointSpand;
	}


	public void setPointSpand(Spand pointSpand) {
		this.pointSpand = pointSpand;
	}


	public Image getSkinMap() {
		return skinMap;
	}


	public void setSkinMap(Image skinMap) {
		this.skinMap = skinMap;
	}

}