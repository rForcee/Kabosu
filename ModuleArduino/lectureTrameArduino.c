
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
int main(){
   char buffer[5000];// buffer qui lit le port
   char trameRecu[20]; // chaine qui contiendra la trame reçu
   char finTrame = '@'; // caractère de fin de trame
   char debutTrame = '#'; // caractère de debut de trame
   int indice = 0; // indice du while
   int finLecture = 1; // boolean de fin de lecture
   int enregistre = 0; // boolean enregistrement trame
    FILE* fichier = NULL;
    fichier = fopen("/dev/ttyACM0","r+"); // ouverture fichier
  while(finLecture !=0){

    if (fichier != NULL)
    {
	
        fread(buffer,sizeof(buffer[0]) , sizeof(buffer)/sizeof(buffer[0]), fichier); // lecture fichier
	
	     
    }
    else
    {
        // On affiche un message d'erreur si on veut
        printf("Impossible d'ouvrir le fichier test.txt");
    }
    while(buffer[indice] !='\0' || buffer[indice] != finTrame)
    {
	     if(buffer[indice] == debutTrame)
	     {
		      enregistre = 1;
		      indice++;
	     }	
	     else if(buffer[indice] == finTrame && enregistre == 1)
	     {
		      printf("break\n");
		
		      finLecture = 0;		
		      break;
	     }else if(enregistre = 1)
	     {
			   trameRecu[indice] = buffer[indice];
			   indice++;
	     }
    }// fin while
	  printf("buffer :");
   	printf(" %s\n", buffer);
	  printf("trame : ");
		printf("%s\n", trameRecu);	
  }
    	
   fclose(fichier);// fermeture fichier

}