package objectMap;

import Com.DataTrameItemJoueur;
import Com.DataTramePlayerInfo;

public class Joueur {
	private int Rang;
	private int nbPub;
	protected DataTramePlayerInfo infojoueur = new DataTramePlayerInfo();
	protected DataTrameItemJoueur infoItem = new DataTrameItemJoueur();
	
	public int getNbPub() {
		return nbPub;
	}
	public void setNbPub(int nbPub) {
		this.nbPub = nbPub;
	}
	public int getRang() {
		return Rang;
	}
	public void setRang(int rang) {
		Rang = rang;
	}
	public DataTramePlayerInfo getInfojoueur() {
		return infojoueur;
	}
	public void setInfojoueur(DataTramePlayerInfo infojoueur) {
		this.infojoueur = infojoueur;
	}
	public DataTrameItemJoueur getInfoItem() {
		return infoItem;
	}
	public void setInfoItem(DataTrameItemJoueur infoItem) {
		this.infoItem = infoItem;
	}
}
