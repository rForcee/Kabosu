package objectMap;

public class Publicite extends ItemJoueur {

	private float prix;
	private int id;
	final private int  prixBasePub = 50;
	
	public Publicite(String nomJoueur, float rayon, float x, float y, int id) {
		super(nomJoueur, rayon, x, y);
		prix = prixBasePub*rayon;
		this.id = id;
	}
	
	public float getPrix() {
		return prix;
	}
	public void setPrix(float prix) {
		this.prix = prix;
	}
	public int getPrixBasePub() {
		return prixBasePub;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
