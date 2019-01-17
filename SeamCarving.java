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
	 * Construit le tableau d'interet de pixels a partir d'une image donnee
	 * @param image tableau de pixel de l'image source
	 * @return tableau d'interet de l'image
	 */
	public static int[][] interest(int[][] image) {
		// Récupération de la longueur et de la largeur de l'image
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

	/**
	 * Mise en graphe d'une image
	 * @param itr tableau de pixels de l'image
	 * @return graphe de l'image
	 */
	public static Graph toGraph(int[][] itr) {
		// Récupération de la longueur et de la largeur de l'image
		int nb_lignes = itr.length;
		int nb_colonnes = itr[0].length;
		// Création du graphe vide
		// Nombre de sommets = longueur * largeur de l'image +2 pour les premier et dernier sommets
		GraphArrayList graph = new GraphArrayList(nb_lignes * nb_colonnes + 3);
		Edge edge;
		int noVertice = 2; // Compteur indiquant quel sommet manipuler
		// Construction du graphe via double itération
		boolean top = false;
		for (int i = 0 ; i < nb_lignes ; i++) {
		    for (int j = 0 ; j < nb_colonnes ; j++) {
			if (i == 0) { // On est au sommet du graphe
			    edge = new Edge(1, noVertice, 0);
			    graph.addEdge(edge);
			    if (j == 0) { // On est tout en haut à gauche du graphe
				edge = new Edge(noVertice, noVertice + nb_colonnes, itr[i][j]);
				graph.addEdge(edge);
				edge = new Edge(noVertice, noVertice + nb_colonnes + 1, itr[i][j]);
				graph.addEdge(edge);
			    }
			    else if (j == nb_colonnes - 1) { // On est tout en haut à droite du graphe
				edge = new Edge(noVertice, noVertice + nb_colonnes - 1, itr[i][j]);
				graph.addEdge(edge);
				edge = new Edge(noVertice, noVertice + nb_colonnes, itr[i][j]);
				graph.addEdge(edge);
			    }
			    else { // On est tout en haut quelque part au milieu du graphe
				edge = new Edge(noVertice, noVertice + nb_colonnes - 1, itr[i][j]);
				graph.addEdge(edge);
				edge = new Edge(noVertice, noVertice + nb_colonnes, itr[i][j]);
				graph.addEdge(edge);
				edge = new Edge(noVertice, noVertice + nb_colonnes + 1, itr[i][j]);
				graph.addEdge(edge);
			    }
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
			else { // On est quelque part dans le graphe sauf à un des bords
			    edge = new Edge(noVertice, noVertice + nb_colonnes - 1, itr[i][j]);
			    graph.addEdge(edge);
			    edge = new Edge(noVertice, noVertice + nb_colonnes, itr[i][j]);
			    graph.addEdge(edge);
			    edge = new Edge(noVertice, noVertice + nb_colonnes + 1, itr[i][j]);
			    graph.addEdge(edge);
			}
			noVertice++;
		    }
		}
		return graph;
	}

    public static ArrayList<Integer> tritopo(Graph g) {
	// verticesEnd : Liste qui indique l'ordre dans lequel on a fini de visiter les sommets
	ArrayList<Integer> verticesEnd = new ArrayList<>();
	int n = g.vertices();
        boolean visite[] = new boolean[n*n+2] ;
	dfs(g, 1, visite, verticesEnd); // Execution du parcours en profondeur avec remplissage de verticesEnd
	// Inversion de la Liste verticesEnd afin d'obtenir le tri topologique
	int verticesStart = 0;
	int verticesFinish = verticesEnd.size() - 1;
	int temp;
	while (verticesStart < verticesFinish) {
	    temp = verticesEnd.get(verticesStart);
	    verticesEnd.set(verticesStart, verticesEnd.get(verticesFinish));
	    verticesEnd.set(verticesFinish, temp);
	    verticesStart++;
	    verticesFinish--;
	}
	return verticesEnd;
    }
    
    private static void dfs(Graph g, int u,  boolean[] visite, ArrayList<Integer> verticesEnd) {
		visite[u] = true;
		for (Edge e: g.next(u)) {
		    if (!visite[e.to]) {
			dfs(g,e.to, visite, verticesEnd);
		    }
		}
		verticesEnd.add(u);
	 }

    public static ArrayList<Integer> bellman(Graph g, int s, int t, ArrayList<Integer> order) {
	// val : Liste qui contient les coûts minimaux pour chaque sommet
	ArrayList<Integer> val = new ArrayList<>(g.vertices());
	// valA : Liste qui contient le sommet d'origine du chemin minimal pour chaque sommet 
	ArrayList<Integer> valA = new ArrayList<>();
	// ccm : Liste qui contient le chemin de coût minimal depuis s vers t
	ArrayList<Integer> ccm = new ArrayList<>();
	int costOld; // ancien cout minimal calculé pour un sommet u
	int costNew; // nouveau cout minimal calculé pour un sommet u
	val.add(0); // initialisation du 1er sommet à 0 (ne coute rien pour aller à lui-meme)
	// initialisation de tous les autres sommets à "plus infini"
	for (int i = 1 ; i < g.vertices() ; i++) {
	    val.add(999999999);
	}
	// initialisation des sommets d'origine du plus court chemin pur chaque sommet
	for (int i = 1 ; i < g.vertices() ; i++) {
	    valA.add(1);
	}
	// evaluation du ccm pour chaque sommet dans l'ordre du tri topologique
	for (int v : order) {
	    for (Edge e: g.next(v)) {
		costNew = val.get(e.from-1) + e.cost;
		costOld = val.get(e.to-1);
		if (costNew < costOld) {
		    val.set(e.to-1, costNew);
		    valA.set(e.to-1, e.from);
		}
	    }
	}
	// construction du chemin de cout minimal depuis s vers t en partant de t
    int currentVertice = t;
	while (currentVertice != s) {
	    ccm.add(0, currentVertice);
	    currentVertice = valA.get(currentVertice - 1);
	}
	return valA;
    }

}

/* Structure d'une image pgm
P2
Commentaire (?)
nb_colonnes nb_lignes
nb_max_valeur_gris (255)
image
 */
