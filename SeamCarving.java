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
    if(!file.exists()) {
    	try {
			file.createNewFile();	// Création du fichier s'il n'existe pas
		}
		catch(Exception e) {

		}
	}
	  try {
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

}

/* Structure d'une image pgm
P2
Commentaire (?)
nb_colonnes nb_lignes
nb_max_valeur_gris (255)
image
 */
