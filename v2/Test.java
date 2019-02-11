import java.util.*;
import src.*;

public class Test {

   static boolean visite[];

   public static void dfs(Graph g, int u) {
		visite[u] = true;
		System.out.println("Je visite " + u);
		for (Edge e: g.next(u))
		  if (!visite[e.getVerticeDestination()])
			dfs(g,e.getVerticeDestination());
	 }
   
   public static void testGraph() {
		int n = 5;
		int i,j;
		GraphArrayList g = new GraphArrayList(n*n+2);
		
		for (i = 0; i < n-1; i++)
		  for (j = 0; j < n ; j++)
			g.addEdge(new Edge(n*i+j, n*(i+1)+j, 1664 - (i+j)));

		for (j = 0; j < n ; j++)		  
		  g.addEdge(new Edge(n*(n-1)+j, n*n, 666));
		
		for (j = 0; j < n ; j++)					
		  g.addEdge(new Edge(n*n+1, j, 0));
		
		g.addEdge(new Edge(13,17,1337));
		g.writeFile("test.dot");
		// dfs à partir du sommet 3
		visite = new boolean[n*n+2];
		dfs(g, 3);
	 }
   
   public static void main(String[] args) {
		//testGraph();
		int[][] image = SeamCarving.readpgm("test.pgm");
		int[][] interest = SeamCarving.interest(image);
		//SeamCarving.writepgm(image, "testCpy.pgm");
		SeamCarving.writepgm(interest, "interestTest.pgm");
		Graph graphe = SeamCarving.toGraph(interest);
		graphe.writeFile("gr_test");
		ArrayList<Integer> arl = new ArrayList<>();
		arl = SeamCarving.tritopo(graphe);
		ArrayList<Integer> ccm = new ArrayList<>();
		ccm = SeamCarving.bellman(graphe, 1, graphe.vertices() - 1, arl);
		for (int i = 0 ; i < interest.length ; i++) {
		    for (int j = 0 ; j < interest[0].length ; j++) {
			System.out.print(interest[i][j] + " ");
		    }
		    System.out.println();
		}
		//SeamCarving.writepgm(image, "waw.pgm");
	 }
}
