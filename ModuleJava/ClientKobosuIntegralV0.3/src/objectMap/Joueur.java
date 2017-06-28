package objectMap;

public class Joueur {
	
	private String speudo;
	private float budget;
	private int vente;
	private StandJoueur monStand;
	private int nbPub;

	public Joueur(String speudo, float budget, int vente, StandJoueur monStand) {
		this.speudo = speudo;
		this.budget = budget;
		this.vente = vente;
		this.monStand = monStand;
		nbPub = 0;
	}
	public void ajoutPub(){
		
	}
	public StandJoueur getMonStand() {
		return monStand;
	}
	public void setMonStand(StandJoueur monStand) {
		this.monStand = monStand;
	}
	public String getSpeudo() {
		return speudo;
	}
	public void setSpeudo(String speudo) {
		this.speudo = speudo;
	}
	public float getBudget() {
		return budget;
	}
	public void setBudget(float budget) {
		this.budget = budget;
	}
	public int getVente() {
		return vente;
	}
	public void setVente(int vente) {
		this.vente = vente;
	}
	public int getNbPub() {
		return nbPub;
	}
	public void setNbPub(int nbPub) {
		this.nbPub = nbPub;
	}
	
}
