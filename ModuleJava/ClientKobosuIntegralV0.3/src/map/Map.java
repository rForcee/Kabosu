package map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import map.Position;
import map.Spand;
import objectIhm.Meteo;
import objectMap.Client;
import objectMap.ItemJoueur;
import objectMap.Joueur;
import objectMap.Publicite;
import objectMap.StandJoueur;
import Com.*;
public class Map 
{
	private Position centre ;
	private Spand pointSpand ;
	private Image skinMap = new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIntegralV0.3/src/skyrim.png");
	public Map(int nbClient, DataTrameMap  data){
		this.setCentre(new Position(data.getMapX(),data.getMapY()));
		this.setPointSpand(new Spand(data.getMapSx(),data.getMapSy()));
	}
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
	public HashMap<String, Rectangle> generationPopulationClient(Group root, int nbClient, Meteo meteo, ArrayList<Joueur> al_joueur){
		int boisson = 0;
		String JmoinCher;
		String JmoinLoin;
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
			// System.out.println(mymapClient.get("client"+i).motivation(meteo));
			System.out.println("client"+i);

			if(mymapClient.get("client"+i).motivation(meteo)[1] == true){

				// System.out.println("todo :choix stand");
				ArrayList<String> jpositif = new ArrayList<String>() ;
				jpositif = rayonInfluence(mymapClient.get("client"+i).getX(), mymapClient.get("client"+i).getY(), al_joueur);
				if(jpositif.size() > 0){
					if(jpositif.size() == 1)
					{
						//Vente(ItemJoueur.getProprietaire(), BoissonVoulue());
					}else
					{

						//todo boucle list
						Random rand = new Random();
						int goFurther = (int) (rand.nextFloat() * (100 - 0) + 0);

						for(int j = 0 /*jpositif.size()*/; j <= al_joueur.size(); j++)
						{
							for(int p = 0; p < jpositif.size(); p++)
							{

								if(al_joueur.get(j).getInfojoueur().getPseudo() == jpositif.get(j) )
								{
									float leastExpensive = al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice(); //todo int boission voulu 

									switch(meteo.getMeteo()){
									case 1:
										if(goFurther > 0 && goFurther <= 10)
										{

											if(al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice() <= leastExpensive)//si le prix du joueur suivant est le moins cher
											{


												leastExpensive = al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice();
												JmoinCher = al_joueur.get(j).getInfojoueur().getPseudo();
											}

										}else
										{
											for(int it = 0; it< al_joueur.get(j).getInfoItem().getItem().size(); it++)
											{

												double distanceMin = calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude());

												if( (calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude()))
														<= distanceMin)//si la distance suivante est la plus courte
												{
													distanceMin = calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude());
													JmoinLoin = al_joueur.get(j).getInfojoueur().getPseudo();
												}


											}


										}

										//Vente(ItemJoueur.getProprietaire(), BoissonVoulue());
										break;
									case 2:
										if(goFurther >= 0 && goFurther <= 40)
										{

											if(al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice() <= leastExpensive)//si le prix du joueur suivant est le moins cher
											{


												leastExpensive = al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice();//TODO methode boissoin;
												JmoinCher = al_joueur.get(j).getInfojoueur().getPseudo();
											}

										}else
										{
											for(int it = 0; it< al_joueur.get(j).getInfoItem().getItem().size(); it++)
											{

												double distanceMin = calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude());

												if( (calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude()))
														<= distanceMin)//si la distance suivante est la plus courte
												{
													distanceMin = calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude());
													JmoinLoin = al_joueur.get(j).getInfojoueur().getPseudo();
												}


											}

										}//fin else
										//Vente(ItemJoueur.getProprietaire(), BoissonVoulue());
										break;
									case 3:
										if(goFurther >= 0 && goFurther <= 50)
										{
											if(al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice() <= leastExpensive)//si le prix du joueur suivant est le moins cher
											{


												leastExpensive = al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice();
												JmoinCher = al_joueur.get(j).getInfojoueur().getPseudo();
											}

										}else
										{
											for(int it = 0; it< al_joueur.get(j).getInfoItem().getItem().size(); it++)
											{

												double distanceMin = calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude());

												if( (calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude()))
														<= distanceMin)//si la distance suivante est la plus courte
												{
													distanceMin = calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude());
													JmoinLoin = al_joueur.get(j).getInfojoueur().getPseudo();
												}


											}

										}//fin else
										//Vente(ItemJoueur.getProprietaire(), BoissonVoulue());
										break;
									case 4:
										if(goFurther >= 0 && goFurther <= 20)
										{

											if(al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice() <= leastExpensive)//si le prix du joueur suivant est le moins cher
											{


												leastExpensive = al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice();
												JmoinCher = al_joueur.get(j).getInfojoueur().getPseudo();
											}

										}else
										{
											for(int it = 0; it< al_joueur.get(j).getInfoItem().getItem().size(); it++)
											{

												double distanceMin = calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude());

												if( (calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude()))
														<= distanceMin)//si la distance suivante est la plus courte
												{
													distanceMin = calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude());
													JmoinLoin = al_joueur.get(j).getInfojoueur().getPseudo();
												}
											}

										}//fin else

										//Vente(ItemJoueur.getProprietaire(), BoissonVoulue());
										break;
									case 5:
											for(int it = 0; it< al_joueur.get(j).getInfoItem().getItem().size(); it++)
											{

												double distanceMin = calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude());

												if( (calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude()))
														<= distanceMin)//si la distance suivante est la plus courte
												{
													distanceMin = calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude());
													JmoinLoin = al_joueur.get(j).getInfojoueur().getPseudo();
												}
											}
										

										//Vente(ItemJoueur.getProprietaire(), BoissonVoulue());
										break;
									}
								}

							}

						}

					}
				}//Fin algo Marvin
			}
		}
		return mymapRec;
	}
	public HashMap<String, Rectangle> generationPopulationStand(Group root, int nbJoueur){
		HashMap<String, StandJoueur> mymapStand = new HashMap<String, StandJoueur>();

		HashMap<String,  Rectangle> mymapRec = new HashMap<String,  Rectangle>();
		for (int i=0; i<nbJoueur; i++)
		{
			mymapStand.put("Stand"+i, new StandJoueur("toto", 40, 500, 500, 40));
			mymapRec.put("rectangle"+i, new Rectangle());
			mymapRec.get("rectangle"+i).setX(mymapStand.get("Stand"+i).getItemJoueur().getLatitude());
			mymapRec.get("rectangle"+i).setY(mymapStand.get("Stand"+i).getItemJoueur().getLongitude());
			mymapRec.get("rectangle"+i).setWidth(50);
			mymapRec.get("rectangle"+i).setHeight(50);
			mymapRec.get("rectangle"+i).setFill(new ImagePattern(mymapStand.get("Stand"+i).getSkinStand()));
			// System.out.println("client"+i+": "+mymapClient.get("client"+i).getX());
			//System.out.println(mymapClient.get("client"+i).getY());
			root.getChildren().add(mymapRec.get("rectangle"+i));
			// System.out.println(mymapClient.get("client"+i).motivation(meteo));
			System.out.println("Stand"+i);
		}
		return mymapRec;
	}

	public HashMap<String, Rectangle> generationPopulationPub(Group root, int nbPub){
		HashMap<String, Publicite> mymapPub = new HashMap<String, Publicite>();

		HashMap<String,  Rectangle> mymapRec = new HashMap<String,  Rectangle>();
		for (int i=0; i<nbPub; i++)
		{
			mymapPub.put("Pub"+i, new Publicite("toto", 40, 350, 500, 40));
			mymapRec.put("rectangle"+i, new Rectangle());
			mymapRec.get("rectangle"+i).setX(mymapPub.get("Pub"+i).getItemJoueur().getLatitude());
			mymapRec.get("rectangle"+i).setY(mymapPub.get("Pub"+i).getItemJoueur().getLongitude());
			mymapRec.get("rectangle"+i).setWidth(50);
			mymapRec.get("rectangle"+i).setHeight(50);
			mymapRec.get("rectangle"+i).setFill(new ImagePattern(mymapPub.get("Pub"+i).getSkinPub()));
			// System.out.println("client"+i+": "+mymapClient.get("client"+i).getX());
			//System.out.println(mymapClient.get("client"+i).getY());
			root.getChildren().add(mymapRec.get("rectangle"+i));
			// System.out.println(mymapClient.get("client"+i).motivation(meteo));
			System.out.println("Pub"+i);
		}
		return mymapRec;
	}


	public ArrayList<String> rayonInfluence(float ClientCoordX, float ClientCoordY, ArrayList<Joueur> joueur)//Prend en paramètre les coordonnées d'un client ainsi que la list de tout les stand de la map
	{

		ArrayList<String> joueurPositif = new ArrayList<String>();//mon array list de stands positif

		for (int i = 0; i < joueur.size(); i++) //pour tout les element de ma list d'item
		{
			for(int j = 0; j <joueur.get(i).getInfoItem().getItem().size();j++ )
			{
				float distance = (float) Math.sqrt(Math.pow(joueur.get(i).getInfoItem().getItem().get(j).getLatitude()-ClientCoordX, 2)
						+Math.pow(joueur.get(i).getInfoItem().getItem().get(j).getLatitude()-ClientCoordY, 2));//petite formule pour calculer la distance entre mon item et mon client
				if (distance <= joueur.get(i).getInfoItem().getItem().get(j).getInfluence()) //Si la distance client/item et inférieur ou egale au rayon d'influence le stand passe en positif
				{
					joueurPositif.add(joueur.get(i).getInfoItem().getItem().get(j).getNomjoueur());//je met le stand positif dans l'array list	
				}

			}
		}
		return joueurPositif;
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
