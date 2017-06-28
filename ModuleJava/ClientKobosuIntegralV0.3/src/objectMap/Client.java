package objectMap;
import java.util.Random;

import javafx.scene.image.Image;
import map.Position;
import objectIhm.Meteo;
public class Client {
	

	private int motivation;
	private Boisson envie;
	private Position pos;
	private float X;
	private float Y;
	final private float minX = 330.0f;
	final private float maxX = 670.0f;
	final private float minY= 130.0f;
	final private float maxY = 470.0f;
	private Image skinClient;
	Random rand = new Random();
 
	public Client ()
	{
		this.X = rand.nextFloat() * (maxX - minX) + minX;
		this.Y = rand.nextFloat() * (maxY - minY) + minY;
		this.motivation = 100;
		this.setPos(new Position(this.X,this.Y));
		setSkinClient(new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIntegralV0.3/src/client.png"));
	}
	public boolean[] motivation(Meteo meteo){
		boolean chaud = false;
		boolean achat = false;
		int ChanceAchat = 0;
		switch(meteo.getMeteo()){
			case 1: //"rainy":
				ChanceAchat = rand.nextInt() * (100- 0) + 0;
				if(ChanceAchat >= 0 && ChanceAchat >= 15){
					achat = true;
					chaud = false;
				}
				ChanceAchat = rand.nextInt() * (100- 0) + 0;
				if(ChanceAchat >= 0 && ChanceAchat >= 85){
					achat = true;
					chaud = true;
				}else{
					achat = false;
				}
				break;
			case 2: //c"cloudy":
				ChanceAchat = rand.nextInt() * (100- 0) + 0;
				if(ChanceAchat >= 0 && ChanceAchat >= 30){
					achat = true;
					chaud = false;
				}
				ChanceAchat = rand.nextInt() * (100- 0) + 0;
				if(ChanceAchat >= 0 && ChanceAchat >= 70){
					achat = true;
					chaud = true;
				}else{
					achat = false;
				}
				break;
			case 3: //"sunny":
				ChanceAchat = rand.nextInt() * (100- 0) + 0;
				if(ChanceAchat >= 0 && ChanceAchat >= 25){
					achat = true;
					chaud = true;
				}
				ChanceAchat = rand.nextInt() * (100- 0) + 0;
				if(ChanceAchat >= 0 && ChanceAchat >= 75){
					achat = true;
					chaud = false;
				}else{
					achat = false;
				}
				break;
			case 4://"heatwave":
				chaud = false;
				achat = true;
				break;
			case 5:// "thunderstorm":
				chaud = true;
				achat = true;
				break;
		}
		boolean[] VenteBoisson = {chaud, achat};
		return VenteBoisson;
	}
	
	public int getMotivation() {
		return motivation;
	}
	public void setMotivation(int motivation) {
		this.motivation = motivation;
	}
	public Boisson getEnvie() {
		return envie;
	}
	public void setEnvie(Boisson envie) {
		this.envie = envie;
	}
	public float getX() {
		return X;
	}
	public void setX(float x) {
		X = x;
	}
	public float getY() {
		return Y;
	}
	public void setY(float y) {
		Y = y;
	}
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos = pos;
	}
	public Image getSkinClient() {
		return skinClient;
	}
	public void setSkinClient(Image skinClient) {
		this.skinClient = skinClient;
	}
}
