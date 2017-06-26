package objectMap;

public class Boisson {
	private String nomBoisson;
	private  boolean chaude;
	private float prix;
	private int quantiter;
	
	public Boisson(String nomBoisson, boolean chaude, float prix, int quantiter) {
		this.nomBoisson = nomBoisson;
		this.chaude = chaude;
		this.prix = prix;
		this.quantiter = quantiter;
	}
	public String getNomBoisson() {
		return nomBoisson;
	}
	public void setNomBoisson(String nomBoisson) {
		this.nomBoisson = nomBoisson;
	}
	public boolean isChaude() {
		return chaude;
	}
	public void setChaude(boolean chaude) {
		this.chaude = chaude;
	}
	public float getPrix() {
		return prix;
	}
	public void setPrix(float prix) {
		this.prix = prix;
	}
	public int getQuantiter() {
		return quantiter;
	}
	public void setQuantiter(int quantiter) {
		this.quantiter = quantiter;
	}
	

}
