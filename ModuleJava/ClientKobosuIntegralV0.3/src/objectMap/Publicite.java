package objectMap;

import javafx.scene.image.Image;

public class Publicite extends ItemJoueur {

	private float prix;
	private int id;
	final private int  prixBasePub = 50;
	private Image skinPub;
	public Publicite(String nomJoueur, float rayon, float x, float y, int id) {
		super(nomJoueur, rayon, x, y);
		prix = prixBasePub*rayon;
		this.id = id;
		setSkinPub(new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIntegralV0.3/src/Pub.png"));
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

	public Image getSkinPub() {
		return skinPub;
	}

	public void setSkinPub(Image skinPub) {
		this.skinPub = skinPub;
	}

}
