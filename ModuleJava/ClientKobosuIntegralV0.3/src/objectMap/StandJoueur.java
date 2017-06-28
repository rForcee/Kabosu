package objectMap;

import javafx.scene.image.Image;

public class StandJoueur extends ItemJoueur {
	

	private Image skinStand;
	
	public StandJoueur(String nomJoueur, float standInfluence, float x, float y, float argentDepart)
	{
			super(nomJoueur, standInfluence, x, y);	
			
			skinStand = new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIntegralV0.3/src/stand.png");
			
	}
	
	public Image getSkinStand() {
		return skinStand;
	}
	public void setSkinStand(Image skinStand) {
		this.skinStand = skinStand;
	}
}
