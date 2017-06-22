#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <string.h>

int main() {
    char buffer[500];// buffer qui lit le port
    char trameRecu[20]; // chaine qui contiendra la trame reçu
    char finTrame = '</deb>'; // caractère de fin de trame
    char debutTrame = '<deb>'; // caractère de debut de trame
    int indice = 0; // indice du while
    int finLecture = 1; // boolean de fin de lecture
    int enregistre = 0; // boolean enregistrement trame

    int fd = open("/dev/ttyACM2", O_RDWR);


    if (fd) {
        while (1) {
            int nb = read(fd, buffer, 500);
            buffer[nb] = '\0';

            printf("%s", buffer);
        }
    }

    fclose(fd);
}


    /*
    FILE* fichier = NULL;
    fichier = fopen("/dev/ttyACM5","r"); // ouverture fichier
    while(finLecture !=0){

        if (fichier != NULL)
        {
            fread(buffer,sizeof(buffer[0]), sizeof(buffer)/sizeof(buffer[0]), fichier); // lecture fichier
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
        printf("trame :");
        printf("%s\n", trameRecu);
    }


    fclose(fichier);// fermeture fichier
}
*/