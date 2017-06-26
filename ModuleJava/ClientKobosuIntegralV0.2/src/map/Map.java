package map;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import map.Position;
import map.Spand;
import objectIhm.Meteo;
import objectMap.Client;
import objectMap.ItemJoueur;
public class Map 
{
	private Position centre ;
	private Spand pointSpand ;
	private Image skinMap = new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIntegralV0.2/src/skyrim.png");
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
	public HashMap<String, Rectangle> generationPopulationClient(Group root, int nbClient, Meteo meteo){
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
		    // System.out.println("client"+i+": "+mymapClient.get("client"+i).getX());
		     //System.out.println(mymapClient.get("client"+i).getY());
		     root.getChildren().add(mymapRec.get("rectangle"+i));
		     //System.out.println(mymapClient.get("client"+i).motivation(meteo));
		     if(mymapClient.get("client"+i).motivation(meteo) == true){
		    	 System.out.println("todo :choix stand");
		     }
		}
		return mymapRec;
	}
	
	 public int rayonInfluence(float ClientCoordX, float ClientCoordY, ArrayList<ItemJoueur> Items)//Prend en paramètre les coordonnées d'un client ainsi que la list de tout les stand de la map
		{
			int nb_area = 0;//mon compteur de stand positif
			ArrayList<ItemJoueur> Stands = new ArrayList();//mon array list de stands positif

			for (int i = 0; i < Items.size(); i++) {//pour tout les element de ma list d'item
				
				float distance = (float) Math.sqrt(Math.pow(Items.get(i).getPosItem().getCoordX()-ClientCoordX, 2)
										  +Math.pow(Items.get(i).getPosItem().getCoordY()-ClientCoordY, 2));//petite formule pour calculer la distance entre mon item et mon client
				if (distance <= Items.get(i).getRayonInfluence()) {//Si la distance client/item et inférieur ou egale au rayon d'influence le stand passe en positif
					
					//Stands.get(nb_area).getProprietaire() = Items.get(i).getProprietaire();//je met le stand positif dans l'array list
				
					nb_area++;//incrementation du compteur
				}
			}
			return nb_area;//retour du compteur, voir comment récupérer l'arret list par la suite
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
