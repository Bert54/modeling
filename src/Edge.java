package src;

// Arête
public class Edge {

   private int from;	// Origine
   private int to;	// Arrivée
   private int cost;	// Coût

	/**
	 * Constructeur d'une arête
	 * @param x origine de l'arete
	 * @param y extremite de l'arete
	 * @param cost cout de l'arete
	 */
   public Edge(int x, int y, int cost) {
		this.from = x;
		this.to = y;
		this.cost = cost;
	 }
    /**
     * Retourne le sommet source de l'arete
     */
    public int getVerticeOrigin() {
	return this.from;
    }

    /**
     * Retourne le sommet de destination de l'arete
     */
    public int getVerticeDestination() {
	return this.to;
    }

    /**
     * Retourne le poids de l'arete
     */
    public int getEdgeCost() {
	return this.cost;
    }

}
