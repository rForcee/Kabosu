package objectMap;

import java.util.ArrayList;

import Com.DataTrameItemJoueur;
import Com.DataTramePlayerInfo;
import Com.DatadrinksOffered;

public class Joueur {

	private int Rang;
	private int nbPub;
	protected DataTramePlayerInfo infojoueur = new DataTramePlayerInfo();
	protected DataTrameItemJoueur infoItem = new DataTrameItemJoueur();
	public Joueur(float budget,ArrayList<DatadrinksOffered>drink, float profit, String pseudo, int vente) 
	{
		this.infojoueur.setBudget(budget);
		this.infojoueur.setDrink(drink);
		this.infojoueur.setProfit(profit);
		this.infojoueur.setPseudo(pseudo);
		this.infojoueur.setVente(vente);
	}
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
