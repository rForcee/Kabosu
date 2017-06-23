package objectMap;

public class StandJoueur extends ItemJoueur {
	
	
		public StandJoueur(String nomJoueur, float standInfluence){
			this.setProprietaire(nomJoueur);
			this.setRayonInfluence(standInfluence);
			
		}
}
