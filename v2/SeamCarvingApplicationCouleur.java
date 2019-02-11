import java.io.File;
import java.util.ArrayList;
import src.*;

public class SeamCarvingApplicationCouleur {

    /**
     * Main de l'application
     * @param args Arguments
     * 1ier argument : nom de l'image source
     */
    public static void main(String[] args) {
        if(args.length == 3) {
            String source = args[0];    // Nom de l'image source
            File fSource = new File(source);    // Fichier représentant l'image source
            if(fSource.exists()) {  // Si le fichier source n'existe pas, on ne peut pas continuer
                String[] split = source.split("\\.");   // On sépare le nom et l'extension
                String ext = split[1];  // Extension du fichier
                if(ext.equals("ppm")) { // On vérifie que l'extension est correcte
                    String nomSansExt = split[0];   // On récupère le nom sans extension
		    int[][][] image = SeamCarving.readppm(args[0]);  // Mise en tableau de l'image 
		    image = suppression(image, Integer.parseInt(args[1])); // Suppression de n colonnes (donné en deuxième argument)
		    image = flipImage(image); // Inversion des lignes et des colonnes de l'image afin d'effectuer une suppression par lignes
		    image = suppression(image, Integer.parseInt(args[2])); // Suppression de m lignes (donné en troisième argument)
		    image = flipImage(image); // Reinversion pour retrouver l'image dans le bon sens
		    String nomCarving = nomSansExt + "_seamCarving.ppm";   // Nouveau nom du fichier
		    SeamCarving.writeppm(image, nomCarving); // On ne remplace pas le fichier d'origine
		    System.out.println("Le fichier a été créé sous " + nomCarving);
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
            System.out.println("Nombre d'arguments incorrect. <image src> <Nombre de colonnes à supprimer> <Nombre de lignes à supprimer>\n");
        }
    }

    /**
     * Supprime n colonnes d'une image en suivant un seam carving
     * @param nom nom de l'image sans extension
     * @param nbIter nombre de colonnes à supprimer
     */
    private static int[][][] suppression(int[][][] image, int nbIter) {
        int[][] interest;
        Graph graph;
        ArrayList<Integer> arl;
        ArrayList<Integer> bm;
	int nbIte = nbIter;	// Nombre de colonnes à supprimer
	int picture_width = image[0].length;
	int[][][] newImage;
	int newImageWidthInd;
	int newImageHeightInd;
	while (picture_width > 0 && nbIte > 0) { // On gère le cas nb_colonnes < nbIter
            interest = SeamCarving.interestRGB(image); // On vérifie les coefficients d'interet des pixels
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
		image[row][column][0] = -1;	// On place à -1 les pixels à supprimer
            }
	    picture_width--;
	    nbIte--;
	    newImage = new int[image.length][picture_width][3]; // Création d'une image avec 1 colonne en moins
	    newImageWidthInd = 0;
	    newImageHeightInd = 0;
	    // On place les bons pixels dans la nouvelle image
	    for (int i = 0 ; i < image.length-1 ; i++) {
		for (int j = 0 ; j < image[0].length-1 ; j++) {
		    if (image[i][j][0] >= 0) {
			newImage[newImageHeightInd][newImageWidthInd][0] = image[i][j][0];
			newImage[newImageHeightInd][newImageWidthInd][1] = image[i][j][1];
			newImage[newImageHeightInd][newImageWidthInd][2] = image[i][j][2];		
			newImageWidthInd++;
		    }	// On ignore les -1
		}
		newImageWidthInd = 0;
		newImageHeightInd++;
	    }
	    image = newImage;
	}
	return image;
    }

    private static int[][][] flipImage(int[][][] image) {
	int width = image.length;
	int height = image[0].length;
	int[][][] imageFlip = new int[height][width][3];
	for (int i = 0 ; i < width ; i++) {
	    for (int j = 0 ; j < height ; j++) {
		imageFlip[j][i] = image[i][j];
	    }
	}	
	return imageFlip;
    }
}
