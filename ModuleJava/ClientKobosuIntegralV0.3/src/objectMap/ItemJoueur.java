package objectMap;
import Com.DataItemJoueur;
public class ItemJoueur {
	
	protected DataItemJoueur itemJoueur = new DataItemJoueur();	

	public ItemJoueur(String nomJoueur, float rayon, float x, float y){
		itemJoueur.setNomjoueur(nomJoueur);
		itemJoueur.setInfluence(rayon);
		itemJoueur.setLatitude(x);
		itemJoueur.setLongitude(y);
	}
	
	public DataItemJoueur getItemJoueur() {
		return itemJoueur;
	}

	public void setItemJoueur(DataItemJoueur itemJoueur) {
		this.itemJoueur = itemJoueur;
	}
	

}
