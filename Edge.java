// Arête
public class Edge {

   int from;	// Origine
   int to;	// Arrivée
   int cost;	// Coût

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
   
}
