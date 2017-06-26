package map;
import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import map.Position;
import map.Spand;
import objectMap.Client;
public class Map 
{
	
	
	private Position centre ;
	private Spand pointSpand ;
	private Image skinMap = new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIhmV1.0/src/carte1.png");
	public Map(float X,float Y,float Sx,float Sy){
		
		this.setCentre(new Position(X,Y));
		this.setPointSpand(new Spand(Sx,Sy));
	}
	
	public HashMap generationPopulationClient(){
		HashMap<String, Client> mymapClient = new HashMap<String, Client>();
		HashMap<String,  Rectangle> mymapRec = new HashMap<String,  Rectangle>();
		for (int i=0; i<5; i++)
		{
		     mymapClient.put("client"+i, new Client());
		     mymapRec.put("rectangle"+i, new Rectangle());
		     mymapRec.get("rectangle"+i).setX(mymapClient.get("client"+i).getX());
		     mymapRec.get("rectangle"+i).setY(mymapClient.get("client"+i).getY());
		     mymapRec.get("rectangle"+i).setWidth(50);
		     mymapRec.get("rectangle"+i).setHeight(50);
		     mymapRec.get("rectangle"+i).setFill(new ImagePattern(mymapClient.get("client"+i).getSkinClient()));
		     System.out.println("client"+i+": "+mymapClient.get("client"+i).getX());
		     System.out.println(mymapClient.get("client"+i).getY());
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
