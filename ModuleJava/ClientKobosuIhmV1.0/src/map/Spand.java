package map;

public class Spand {
	private float coordX;
	private float coordY;
	public Spand()
	{
		this.coordX = 0;
		this.coordY = 0;
	}
	public Spand(float X, float Y)
	{
		this.coordX = X;
		this.coordY = Y;
	}
	public float getCoordX() {
		return coordX;
	}
	public void setCoordX(float coordX) {
		this.coordX = coordX;
	}
	public float getCoordY() {
		return coordY;
	}
	public void setCoordY(float coordY) {
		this.coordY = coordY;
	}
}
