import java.util.ArrayList;

class GraphImplicit extends Graph {

    int N;

	/**
	 * Constructeur d'un graphe implicite
	 * @param N nombre de sommets
	 */
	@SuppressWarnings("unchecked")
    GraphImplicit(int N) {
	this.N = N;
    }

	/**
	 * Getter sur le nombre de sommets
	 * @return nombre de sommets du graphe
	 */
	public int vertices() {
		return N;
    }

	/**
	 * Iterateur sur la prochaine arete
	 * @param v numéro du sommet
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public Iterable<Edge> next(int v) {
	     ArrayList<Edge> edges = new ArrayList();
	     for (int i = v; i < N; i++) {
			 edges.add(new Edge(v, i, i));
		 }
	     return edges;
		      
	 }

@SuppressWarnings("unchecked")
   public Iterable<Edge> prev(int v) {
	     ArrayList<Edge> edges = new ArrayList();
	     for (int i = 0; i < v-1; i++) {
	     	edges.add(new Edge(i, v, v));
		 }
	     return edges;
		      
	 }

    
}
