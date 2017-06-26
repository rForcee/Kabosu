package objectMap;
import map.Position;
public class ItemJoueur {
	
	private float rayonInfluence;
	private String proprietaire;
	private Position posItem;
	public ItemJoueur(String nomJoueur, float rayon, float x, float y){
		proprietaire = nomJoueur;
		rayonInfluence = rayon;
		setPosItem(new Position(x,y));
	}
	public float getRayonInfluence() {
		return rayonInfluence;
	}
	public void setRayonInfluence(float rayonInfluence) {
		this.rayonInfluence = rayonInfluence;
	}
	public String getProprietaire() {
		return proprietaire;
	}
	public void setProprietaire(String proprietaire) {
		this.proprietaire = proprietaire;
	}
	public Position getPosItem() {
		return posItem;
	}
	public void setPosItem(Position posItem) {
		this.posItem = posItem;
	}
	
	

}
