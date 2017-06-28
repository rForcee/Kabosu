package Com;

import java.util.ArrayList;

public class DataTrameItemJoueur{
	
	String joueur;
	protected ArrayList<DataItemJoueur> item= new ArrayList<DataItemJoueur>();

	public ArrayList<DataItemJoueur> getItem() {
		return item;
	}

	public void setItem(ArrayList<DataItemJoueur> item) {
		this.item = item;
	}
}
