package objectIhm;
// class pour recupere et calculer l'heure
public class Date {
	

	private int heureServeur;
	private int HeureJeux;
	private int jour;
	final private int dureUneJournee = 24;
	
//Methode
	public int jourJeux(int heureServeur){
		return jour = (heureServeur/dureUneJournee);
	}
		public int heureJeux(int heureServeur){
		return HeureJeux = (heureServeur-(jour*dureUneJournee));
		
	}
	
// setter
	public void setHeureServeur(int heureServeur) {
		this.heureServeur = heureServeur;
	}
	
	public void setJour(int jour) {
		this.jour = jour;
	}
	
	public void setHeureJeux(int heureJeux) {
		HeureJeux = heureJeux;
	}
//getter	
	public int getJour() {
		return jour;
	}
	public float getHeureServeur() {
		return heureServeur;
	}
	public float getHeureJeux() {
		return HeureJeux;
	}
	public int getDureJour() {
		return dureUneJournee;
	}
	

}
