package objectMap;
import java.util.Random;

import map.Position;
public class Client {
	

	private int motivation;
	private Boisson envie;
	private Position pos;
	private float X;
	private float Y;
	final private float minX = 50.0f;
	final private float maxX = 200.0f;
	final private float minY= 50.0f;
	final private float maxY = 200.0f;

	Random rand = new Random();
 
	public Client ()
	{
		this.X = rand.nextFloat() * (maxX - minX) + minX;
		this.Y = rand.nextFloat() * (maxY - minY) + minY;
		this.motivation = 100;
		this.setPos(new Position(this.X,this.Y));
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
}