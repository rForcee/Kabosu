package Calcul;

public class Calcul {

	//fonction retournant le nbre de zone d'influence touchant un client et placant les stands positifs dans un arraylist
	int rayonInfluence(float ClientCoordX, float ClientCoordY, ArrayList<ItemJoueur> Items)//Prend en paramètre les coordonnées d'un client ainsi que la list de tout les stand de la map
	{
		int nb_area = 0;//mon compteur de stand positif
		ArrayList<ItemJoueur> Stands = new ArrayList();//mon array list de stands positif

		for (int i = 0; i < Items.size(); i++) {//pour tout les element de ma list d'item
			
			float distance = Math.sqrt(Math.pow(Items.get(i).getPosItem().getCoordX()-ClientCoordX, 2)
									  +Math.pow(Items.get(i).getPosItem().getCoordY()-ClientCoordY, 2));//petite formule pour calculer la distance entre mon item et mon client
			if (distance <= Items.get(i).getRayonInfluence()) {//Si la distance client/item et inférieur ou egale au rayon d'influence le stand passe en positif
				Stands.get(nb_area) = Items.get(i).getProprietaire();//je met le stand positif dans l'array list
				nb_area++;//incrementation du compteur
			}
		}
		return nb_area;//retour du compteur, voir comment récupérer l'arret list par la suite
	}
	
	 
	
	
	
}
