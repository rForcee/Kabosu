package Com;

public class DataTramePlayerInfo {

	String pseudo;
	float budget;
	float profit;
	int vente;
	DatadrinksOffered drink = new DatadrinksOffered();
		public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public float getBudget() {
		return budget;
	}
	public void setBudget(float budget) {
		this.budget = budget;
	}
	public float getProfit() {
		return profit;
	}
	public void setProfit(float profit) {
		this.profit = profit;
	}
	public int getVente() {
		return vente;
	}
	public void setVente(int vente) {
		this.vente = vente;
	}
	public DatadrinksOffered getDrink() {
		return drink;
	}
	public void setDrink(DatadrinksOffered drink) {
		this.drink = drink;
	}
}
