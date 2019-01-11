import java.util.ArrayList;
import java.io.*;
import java.util.*;

public class SeamCarving {

	/**
	 * Lecture d'un fichier de format pgm
	 * @param fn nom du fichier
	 * @return tableau representant la valeur de la couleur des pixels
	 * Format pgm : pixel en niveau de gris de 0 à 255
	 */
  public static int[][] readpgm(String fn) {		
    try {
		InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
		BufferedReader d = new BufferedReader(new InputStreamReader(f));
		String magic = d.readLine();
		String line = d.readLine();
		while (line.startsWith("#")) {
			line = d.readLine();
		}
		Scanner s = new Scanner(line);
		int width = s.nextInt();
		int height = s.nextInt();
		line = d.readLine();
		s = new Scanner(line);
		int maxVal = s.nextInt();
		int[][] im = new int[height][width];
		s = new Scanner(d);
		int count = 0;
		while (count < height*width) {
			im[count / width][count % width] = s.nextInt();
			count++;
		}
		return im;
    }
    catch(Throwable t) {
		t.printStackTrace(System.err) ;
		return null;
    }
  }

	/**
	 * Ecrit un fichier en format pgm
	 * @param image tableau representant la valeur de la couleur des pixels
	 * @param filename nom du fichier
	 */
  public static void writepgm(int[][] image, String filename) {
    File file = new File(filename);
    	try {
	    if (!file.exists()) {
			file.createNewFile();	// Création du fichier s'il n'existe pas
	     }
		  int nb_lignes = image.length;
		  int nb_colonnes = image[0].length;
		  FileWriter fw = new FileWriter(file);
		  BufferedWriter buffer = new BufferedWriter(fw);
		  buffer.write("P2\n");
		  buffer.write(nb_colonnes + " " + nb_lignes + "\n");
		  buffer.write("255\n");		// valeur maximale de gris
		  int pixel;
		  for(int i = 0 ; i < nb_lignes ; i++) {
		  	for (int j = 0; j < nb_colonnes ; j++) {
				pixel = image[i][j];	// représente la valeur de la couleur du pixel en i,j
				buffer.write(pixel + "");	// écriture de la valeur de la couleur du pixel
				if (j < nb_colonnes-1) {	// le pixel n'est pas en fin de ligne, donc on ajoute un espace
					buffer.write(" ");
				}
			}
			buffer.newLine();
		  }
		  buffer.close();
	  }
	  catch (Exception e) {

	}
  }
	
	/**
		Construit le tableau d'intérêt de pixels à partir d'une image donnée 
	*/
	public static int[][] interest(int[][] image) {
		// Récupération de la longueur de largeur de l'image
		int nb_lignes = image.length;
		int nb_colonnes = image[0].length;
		 // Création du tableau d'intérêt des pixels
		int[][] pixelsInterest;
		pixelsInterest = new int[nb_lignes][nb_colonnes];
		int pixelInterestSingle = 0;
		for (int i = 0 ; i < nb_lignes ; i++) {
			for (int j = 0 ; j < nb_colonnes ; j++) {
				if (j == 0) { // On est en début de ligne
					pixelInterestSingle = Math.abs(image[i][j]-image[i][j+1]);
				}
				else if (j == nb_colonnes - 1) { // On est en fin de ligne
					pixelInterestSingle = Math.abs(image[i][j]-image[i][j-1]);
				}
				else { // On est ni en début de ligne, ni en fin de ligne
					pixelInterestSingle = Math.abs(image[i][j]-((image[i][j-1]+image[i][j+1])/2));
				}
				pixelsInterest[i][j] = pixelInterestSingle;
			}
		}
		return pixelsInterest;
	}
	
	public static Graph toGraph(int[][] itr) {
		// Récupération de la longueur de largeur de l'image
		int nb_lignes = itr.length;
		int nb_colonnes = itr[0].length;
		// Création du graphe vide
		// Nombre de sommets = longueur * largeur de l'image +2 pour les premier et dernier sommets
		GraphArrayList graph = new GraphArrayList(nb_lignes * nb_colonnes + 3);
		Edge edge;
		int noVertice = 2; // Compteur indiquant quel sommet manipuler
		// Construction du graphe via double itération
		for (int i = 0 ; i < nb_lignes ; i++) {
		    for (int j = 0 ; j < nb_colonnes ; j++) {
			if (i == 0) { // On est au sommet du graphe
			    edge = new Edge(1, noVertice, 0);
			    graph.addEdge(edge);
			}
			else if (i == nb_lignes - 1) { // On est au pied du graphe
			    edge = new Edge(noVertice, nb_lignes * nb_colonnes + 2, itr[i][j]);
			    graph.addEdge(edge);
			}
			else if (j == 0) { // On est tout à gauche du graphe
			    edge = new Edge(noVertice, noVertice + nb_colonnes, itr[i][j]);
			    graph.addEdge(edge);
			    edge = new Edge(noVertice, noVertice + nb_colonnes + 1, itr[i][j]);
			    graph.addEdge(edge);
			}
			else if (j == nb_colonnes - 1) { // On est tout à droite du graphe
			    edge = new Edge(noVertice, noVertice + nb_colonnes - 1, itr[i][j]);
			    graph.addEdge(edge);
			    edge = new Edge(noVertice, noVertice + nb_colonnes, itr[i][j]);
			    graph.addEdge(edge);
			}
			else { // On est quelque aprt dans le graphe sauf à un des bords
			    edge = new Edge(noVertice, noVertice + nb_colonnes - 1, itr[i][j]);
			    graph.addEdge(edge);
			    edge = new Edge(noVertice, noVertice + nb_colonnes, itr[i][j]);
			    graph.addEdge(edge);
			    edge = new Edge(noVertice, noVertice + nb_colonnes + 1, itr[i][j]);
			    graph.addEdge(edge);
			}
			noVertice++;
		    }
		    if (i == 0) {
			noVertice = 2;
		    }	
		}
		return graph;
	}
}



/* Structure d'une image pgm
P2
Commentaire (?)
nb_colonnes nb_lignes
nb_max_valeur_gris (255)
image
 */
