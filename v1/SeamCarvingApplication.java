import java.io.File;
import java.util.ArrayList;
import src.*;

public class SeamCarvingApplication {

    /**
     * Main de l'application
     * @param args Arguments
     * 1ier argument : nom de l'image source
     */
    public static void main(String[] args) {
        if(args.length == 1) {
            String source = args[0];    // Nom de l'image source
            File fSource = new File(source);    // Fichier représentant l'image source
            if(fSource.exists()) {  // Si le fichier source n'existe pas, on ne peut pas continuer
                String[] split = source.split("\\.");   // On sépare le nom et l'extension
                String ext = split[1];  // Extension du fichier
                if(ext.equals("pgm")) { // On vérifie que l'extension est correcte
                    String nomSansExt = split[0];   // On récupère le nom sans extension
                    supression50(nomSansExt);
                }
                else {
                    System.out.println("Mauvaise extension de fichier !\n");
                }
            }
            else {
                System.out.println("Le fichier n'existe pas !\n");
            }
        }
        else {
            System.out.println("Nombre d'arguments incorrect. <image src>\n");
        }
    }

    /**
     * Supprime 50 colonnes d'une image en suivant un seam carving
     * @param nom nom de l'image sans extension
     */
    private static void supression50(String nom) {
        int[][] image;
        int[][] interest;
        Graph graph;
        ArrayList<Integer> arl;
        ArrayList<Integer> bm;
        image = SeamCarving.readpgm(nom + ".pgm");  // Mise en tableau de l'image
	int nbIte = 50;	// Nombre de colonnes à supprimer
	int picture_width = image[0].length;
	int[][] newImage;
	int newImageWidthInd;
	int newImageHeightInd;
	while (picture_width > 0 && nbIte > 0) { // On gère le cas nb_colonnes < 50
            interest = SeamCarving.interest(image); // On vérifie les coefficients d'interet des pixels
            graph = SeamCarving.toGraph(interest);    // Mise en graphe du tableau d'interet
            arl = SeamCarving.tritopo(graph);               // Tri topologique sur le graphe
            bm = SeamCarving.bellman(graph, 1, graph.vertices() - 1, arl);    // Application de Bellman
	    bm.remove(bm.size()-1); // On enlève également le dernier sommet dans le chemin, puisque celui-ci n'est pas un pixel dans l'image
            for (Integer in : bm) {   // On parcourt les sommets constituant le plus court chemin et on supprime le pixel correspondant
		int row = (in-1) / picture_width;
		int column = ((in-1) % picture_width);
		if (row > image.length-1) {	// Pour gérer le nombre de colonnes impair
		    row = image.length-1;
		}
		image[row][column] = -1;	// On place à -1 les pixels à supprimer
            }
	    picture_width--;
	    nbIte--;
	    newImage = new int[image.length][picture_width]; // Création d'une image avec 1 colonne en moins
	    newImageWidthInd = 0;
	    newImageHeightInd = 0;
	    // On place les bons pixels dans la nouvelle image
	    for (int i = 0 ; i < image.length-1 ; i++) {
		for (int j = 0 ; j < image[0].length-1 ; j++) {
		    if (image[i][j] >= 0) {
			newImage[newImageHeightInd][newImageWidthInd] = image[i][j];
			newImageWidthInd++;
		    }	// On ignore les -1
		}
		newImageWidthInd = 0;
		newImageHeightInd++;
	    }
	    image = newImage;
	}
        String nomCarving = nom + "_seamCarving.pgm";   // Nouveau nom du fichier
        SeamCarving.writepgm(image, nomCarving); // On ne remplace pas le fichier d'origine
        System.out.println("Le fichier a été créé sous " + nomCarving);
    }

}
