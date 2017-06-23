package map;
import javafx.scene.image.Image;

import map.Position;
import map.Spand;
public class Map 
{
	
	
	private Position centre ;
	private Spand pointSpand ;
	private Image skinMap = new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIhmV1.0/src/carte1.png");
	public Map(float X,float Y,float Sx,float Sy){
		
		this.setCentre(new Position(X,Y));
		this.setPointSpand(new Spand(Sx,Sy));
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
