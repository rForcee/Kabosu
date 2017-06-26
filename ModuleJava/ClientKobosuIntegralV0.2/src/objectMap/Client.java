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
		setSkinClient(new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIntegralV0.2/src/client.png"));
	}
	public boolean motivation(Meteo meteo){
		int ChanceAchat = rand.nextInt() * (100- 0) + 0;
		boolean Achat = false;
		switch(meteo.getMeteo()){
			case 1: //"rainy":
				if(ChanceAchat >= 0 && ChanceAchat >= 15){
					Achat = true;
				}else{
					Achat = false;
				}	
				break;
			case 2: //"cloudy":
				if(ChanceAchat >= 0 && ChanceAchat >= 30){
					Achat = true;
				}else{
					Achat = false;
				}
				break;
			case 3: //"sunny":
				if(ChanceAchat >= 0 && ChanceAchat >= 75){
					Achat = true;
				}else{
					Achat = false;
				}
				break;
			case 4://"heatwave":
				Achat = true;
				break;
			case 5:// "thunderstorm":
				Achat = false;
				break;
		}
		return Achat;
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
