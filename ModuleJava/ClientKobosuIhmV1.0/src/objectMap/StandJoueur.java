package objectMap;

import javafx.scene.image.Image;

public class StandJoueur extends ItemJoueur {
	
	private float budget;
	private Image skinStand = new Image("file:b:/projet1A/git/Kabosu/ModuleJava/ClientKobosuIhmV1.0/src/stand.png");
	public StandJoueur(String nomJoueur, float standInfluence, float x, float y, float budgetDepart)
	{
			super(nomJoueur, standInfluence, x, y);	
			this.budget = budgetDepart;
	}
	public float getBudget() {
		return budget;
	}
	public void setBudget(float budget) {
		this.budget = budget;
	}
	public Image getSkinStand() {
		return skinStand;
	}
	public void setSkinStand(Image skinStand) {
		this.skinStand = skinStand;
	}
}
