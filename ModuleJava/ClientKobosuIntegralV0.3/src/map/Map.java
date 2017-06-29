package map;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
	public HashMap<String, Rectangle> generationPopulationClient(Group root, int nbClient, Meteo meteo, ArrayList<Joueur> al_joueur) throws Exception{
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
			//System.out.println(mymapClient.get("client"+i).motivation(meteo));
			System.out.println("client"+i);

			if(mymapClient.get("client"+i).motivation(meteo)[0] == true){
				

				ArrayList<String> jpositif = new ArrayList<String>() ;
				jpositif.addAll(rayonInfluence(mymapClient.get("client"+i).getX(), mymapClient.get("client"+i).getY(), al_joueur));
				if(jpositif.size() > 0){
					System.out.println("CLIENT NUMBER IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
					if(jpositif.size() == 1)
					{
						mymapClient.get("client"+i).vente(mymapClient.get("client"+i).motivation(meteo), jpositif.get(i));
					}
					else
					{

						//todo boucle list
						Random rand = new Random();
						int goFurther = (int) (rand.nextFloat() * (100 - 0) + 0);

						for(int j = 0 ; j <= al_joueur.size(); j++)
						{
							for(int p = 0; p < jpositif.size(); p++)
							{

								//if(al_joueur.get(j).getInfojoueur().getPseudo().equals(jpositif.get(j)) )
								//{
								float leastExpensive = al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice(); //todo int boission voulu 

								switch(meteo.getMeteo()){
								case 1:
									
									if(goFurther > 0 && goFurther <= 10)
									{

										if(al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice() <= leastExpensive)//si le prix du joueur suivant est le moins cher
										{


											leastExpensive = al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice();
											JmoinCher = al_joueur.get(j).getInfojoueur().getPseudo();
											mymapClient.get("client"+i).vente(mymapClient.get("client"+i).motivation(meteo),JmoinCher);
											System.out.println("vente");

										}

									}else
									{
										for(int it = 0; it< al_joueur.get(j).getInfoItem().getItem().size(); it++)
										{

											double distanceMin = calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude());

											if( (calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude()))
													<= distanceMin)//si la distance suivante est la plus courte
											{
												distanceMin = calculeRayon(mymapClient.get("client"+i).getX(),mymapClient.get("client"+i).getY(),al_joueur.get(j).getInfoItem().getItem().get(it).getLatitude(),al_joueur.get(j).getInfoItem().getItem().get(it).getLongitude());//TO
												//TODO methode check rayon
												JmoinLoin = al_joueur.get(j).getInfojoueur().getPseudo();
												mymapClient.get("client"+i).vente(mymapClient.get("client"+i).motivation(meteo),JmoinLoin  );
												System.out.println("vente");
											}

										}


									}


									break;
								case 2:
									System.out.println("22222222222222222222222222222222222222");
									if(goFurther >= 0 && goFurther <= 40)
									{

										if(al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice() <= leastExpensive)//si le prix du joueur suivant est le moins cher
										{


											leastExpensive = al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice();//TODO methode boissoin;
											JmoinCher = al_joueur.get(j).getInfojoueur().getPseudo();
											mymapClient.get("client"+i).vente(mymapClient.get("client"+i).motivation(meteo),JmoinCher  );
											System.out.println("vente");

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
												mymapClient.get("client"+i).vente(mymapClient.get("client"+i).motivation(meteo),JmoinLoin );
												System.out.println("vente");

											}
										}

									}//fin else

									break;
								case 3:
									System.out.println("33333333333333333333333333333333");
									if(goFurther >= 0 && goFurther <= 50)
									{
										if(al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice() <= leastExpensive)//si le prix du joueur suivant est le moins cher
										{


											leastExpensive = al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice();
											JmoinCher = al_joueur.get(j).getInfojoueur().getPseudo();
											mymapClient.get("client"+i).vente(mymapClient.get("client"+i).motivation(meteo),JmoinCher );
											System.out.println("vente");

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
												mymapClient.get("client"+i).vente(mymapClient.get("client"+i).motivation(meteo),JmoinLoin );
												System.out.println("vente");

											}


										}

									}//fin else

									break;
								case 4:
									System.out.println("4444444444444444444444444444444444444444444");
									if(goFurther >= 0 && goFurther <= 20)
									{

										if(al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice() <= leastExpensive)//si le prix du joueur suivant est le moins cher
										{


											leastExpensive = al_joueur.get(j).getInfojoueur().getDrink().get(boisson).getPrice();
											JmoinCher = al_joueur.get(j).getInfojoueur().getPseudo();
											mymapClient.get("client"+i).vente(mymapClient.get("client"+i).motivation(meteo),JmoinCher );
											System.out.println("vente");
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
												mymapClient.get("client"+i).vente(mymapClient.get("client"+i).motivation(meteo),JmoinLoin );
												System.out.println("vente");

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
											mymapClient.get("client"+i).vente(mymapClient.get("client"+i).motivation(meteo),JmoinLoin );
											System.out.println("vente");

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
		//}
		return mymapRec;
	}
	public HashMap<String, Rectangle> generationPopulationStand(Group root) throws IOException, Exception{
		HashMap<String, StandJoueur> mymapStand = new HashMap<String, StandJoueur>();
		URL urlGet = new URL("https://kabosu.herokuapp.com/map");
		TestHttp get = new TestHttp();
		DataTrameRank  data = new DataTrameRank ();
		DataTrameItemJoueur  dataItem = new DataTrameItemJoueur();

		HashMap<String,  Rectangle> mymapRec = new HashMap<String,  Rectangle>();
		for (int i=0; i<TestHttp.traitementTrameRank(TestHttp.getMap(urlGet)).getRank().size(); i++)
		{
			data.getRank().add((TestHttp.traitementTrameRank(TestHttp.getMap(urlGet)).getRank().get(i)));

			for(int l = 0; l<(get.traitementTrameItem(TestHttp.getMap(urlGet),data.getRank().get(i)) ).getItem().size();l++)
			{			
				dataItem.getItem().add(l, ((get.traitementTrameItem(TestHttp.getMap(urlGet),data.getRank().get(i)) ).getItem().get(l)));			

			}
if(dataItem.getItem().get(i).getTypeItem().equals("stand"))
{
			mymapStand.put("Stand"+i, new StandJoueur(dataItem.getItem().get(i).getNomjoueur(), dataItem.getItem().get(i).getInfluence(), dataItem.getItem().get(i).getLatitude(),  dataItem.getItem().get(i).getLongitude(),10));
			mymapRec.put("rectangle"+i, new Rectangle());
			mymapRec.get("rectangle"+i).setX(mymapStand.get("Stand"+i).getItemJoueur().getLatitude());
			mymapRec.get("rectangle"+i).setY(mymapStand.get("Stand"+i).getItemJoueur().getLongitude());
			mymapRec.get("rectangle"+i).setWidth(30);
			mymapRec.get("rectangle"+i).setHeight(30);
			mymapRec.get("rectangle"+i).setFill(new ImagePattern(mymapStand.get("Stand"+i).getSkinStand()));
			// System.out.println("client"+i+": "+mymapClient.get("client"+i).getX());
			//System.out.println(mymapClient.get("client"+i).getY());
			root.getChildren().add(mymapRec.get("rectangle"+i));
}
			// System.out.println(mymapClient.get("client"+i).motivation(meteo));
			System.out.println("Stand"+i);


		}
		return mymapRec;
	}

	public HashMap<String, Rectangle> generationPopulationPub(Group root, int nbPub) throws IOException, Exception{
		HashMap<String, Publicite> mymapPub = new HashMap<String, Publicite>();
		URL urlGet = new URL("https://kabosu.herokuapp.com/map");
		TestHttp get = new TestHttp();
		DataTrameItemJoueur  dataItem = new DataTrameItemJoueur();
		DataTrameRank  data = new DataTrameRank ();
		data = TestHttp.traitementTrameRank(TestHttp.getMap(urlGet));
		HashMap<String,  Rectangle> mymapRec = new HashMap<String,  Rectangle>();
		for (int i=0; i<data.getRank().size(); i++)
		{
			for(int l = 0; l<(get.traitementTrameItem(TestHttp.getMap(urlGet),data.getRank().get(i)) ).getItem().size();l++)
			{			
				dataItem.getItem().add(l, ((get.traitementTrameItem(TestHttp.getMap(urlGet),data.getRank().get(i)) ).getItem().get(l)));			

			}
			if(dataItem.getItem().get(i).getTypeItem().equals( "ad"))
			{
				mymapPub.put("Pub"+i, new Publicite(dataItem.getItem().get(i).getNomjoueur(), dataItem.getItem().get(i).getInfluence(), dataItem.getItem().get(i).getLatitude(), dataItem.getItem().get(i).getLongitude(), 40));
				mymapRec.put("rectangle"+i, new Rectangle());
				mymapRec.get("rectangle"+i).setX(mymapPub.get("Pub"+i).getItemJoueur().getLatitude());
				mymapRec.get("rectangle"+i).setY(mymapPub.get("Pub"+i).getItemJoueur().getLongitude());
				mymapRec.get("rectangle"+i).setWidth(30);
				mymapRec.get("rectangle"+i).setHeight(30);
				mymapRec.get("rectangle"+i).setFill(new ImagePattern(mymapPub.get("Pub"+i).getSkinPub()));
				// System.out.println("client"+i+": "+mymapClient.get("client"+i).getX());
				//System.out.println(mymapClient.get("client"+i).getY());
				root.getChildren().add(mymapRec.get("rectangle"+i));
				// System.out.println(mymapClient.get("client"+i).motivation(meteo));
				System.out.println("Pub"+i);
			}
		}
		return mymapRec;
	}


	public ArrayList<String> rayonInfluence(float ClientCoordX, float ClientCoordY, ArrayList<Joueur> joueur)//Prend en param�tre les coordonn�es d'un client ainsi que la list de tout les stand de la map
	{

		ArrayList<String> joueurPositif = new ArrayList<String>();//mon array list de stands positif
		
		for (int i = 0; i < joueur.size(); i++) //pour tout les element de ma list d'item
		{
			for(int j = 0; j <joueur.get(i).getInfoItem().getItem().size();j++ )
			{System.out.println("tototototo");
				float distance = (float) Math.sqrt(Math.pow(joueur.get(i).getInfoItem().getItem().get(j).getLatitude()-ClientCoordX, 2)
						+Math.pow(joueur.get(i).getInfoItem().getItem().get(j).getLongitude()-ClientCoordY, 2));//petite formule pour calculer la distance entre mon item et mon client
				if (distance <= joueur.get(i).getInfoItem().getItem().get(j).getInfluence()) //Si la distance client/item et inf�rieur ou egale au rayon d'influence le stand passe en positif
				{
					joueurPositif.add(joueur.get(i).getInfoItem().getItem().get(j).getNomjoueur());//je met le stand positif dans l'array list	
				}

			}
		}
		return joueurPositif;
	}
	public ArrayList<Joueur> initlistJoueur() throws IOException, Exception
	{	
		ArrayList<Joueur> listJoueur = new ArrayList<Joueur>();
		URL urlGet = new URL("https://kabosu.herokuapp.com/map");
		DataTrameRank  data = new DataTrameRank ();
		TestHttp.traitementTrameRank(TestHttp.getMap(urlGet));
		for(int i =0; i<(TestHttp.traitementTrameRank(TestHttp.getMap(urlGet)).getRank().size());i++)
		{
			data.getRank().add((TestHttp.traitementTrameRank(TestHttp.getMap(urlGet)).getRank().get(i)));
			DataTramePlayerInfo playerInfo = new DataTramePlayerInfo();
			DataTrameItemJoueur item = new DataTrameItemJoueur();
			playerInfo.setBudget(TestHttp.traitementTramePlayerInfo(TestHttp.getMap(urlGet), data.getRank().get(i)).getBudget());
			playerInfo.setProfit(TestHttp.traitementTramePlayerInfo(TestHttp.getMap(urlGet), data.getRank().get(i)).getProfit());
			playerInfo.setVente(TestHttp.traitementTramePlayerInfo(TestHttp.getMap(urlGet), data.getRank().get(i)).getVente());
			playerInfo.setPseudo(TestHttp.traitementTramePlayerInfo(TestHttp.getMap(urlGet), data.getRank().get(i)).getPseudo());
			
			for(int j = 0; j<TestHttp.traitementTramePlayerInfo(TestHttp.getMap(urlGet), data.getRank().get(i)).getDrink().size(); j++)
			{
				playerInfo.getDrink().addAll(j, TestHttp.traitementTramePlayerInfo(TestHttp.getMap(urlGet), data.getRank().get(i)).getDrink());
				
			}
			for(int j = 0; j<TestHttp.traitementTrameItem(TestHttp.getMap(urlGet), data.getRank().get(i)).getItem().size(); j++)
			{
				item.getItem().addAll(j,TestHttp.traitementTrameItem(TestHttp.getMap(urlGet), data.getRank().get(i)).getItem());
				
			}
			Joueur joueur = new Joueur(playerInfo.getBudget(), playerInfo.getDrink(), playerInfo.getProfit(), playerInfo.getPseudo(), playerInfo.getVente(),item.getItem());
			listJoueur.add(joueur);
		}



		return listJoueur;

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
