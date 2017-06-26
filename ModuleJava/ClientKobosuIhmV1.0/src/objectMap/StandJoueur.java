package objectMap;

import javafx.scene.image.Image;

public class StandJoueur extends ItemJoueur {
	
	private float argent;
	private Image skinStand;
	public StandJoueur(String nomJoueur, float standInfluence, float x, float y, float argentDepart)
	{
			super(nomJoueur, standInfluence, x, y);	
			this.argent = argentDepart;
			skinStand = new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIhmV1.0/src/stand.png");
	}
	public float getargent() {
		return argent;
	}
	public void setargent(float argent) {
		this.argent = argent;
	}
	public Image getSkinStand() {
		return skinStand;
	}
	public void setSkinStand(Image skinStand) {
		this.skinStand = skinStand;
	}
}
