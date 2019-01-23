package src;

import java.util.Stack;
import java.util.ArrayList;
import java.util.Iterator;

// Parcours en profondeur d'abord
public class DFS {
    
    public static void botched_dfs1(Graph g, int s) {
		Stack<Integer> stack = new Stack<Integer>();
		boolean visited[] = new boolean[g.vertices()];
		stack.push(s);
		visited[s] = true;
		while (!stack.empty()) {
			int u = stack.pop();
			System.out.println(u);
			for (Edge e: g.next(u)) {
				if (!visited[e.getVerticeDestination()]) {
					visited[e.getVerticeDestination()] = true;
					stack.push(e.getVerticeDestination());
				}
			}
		}
    }

    public static void botched_dfs2(Graph g, int s) {
		Stack<Integer> stack = new Stack<Integer>();
		boolean visited[] = new boolean[g.vertices()];
		stack.push(s);
		System.out.println(s);
		visited[s] = true;
		while (!stack.empty()) {
			int u = stack.pop();
			for (Edge e: g.next(u)) {
				if (!visited[e.getVerticeDestination()]) {
					System.out.println(e.getVerticeDestination());
					visited[e.getVerticeDestination()] = true;
					stack.push(e.getVerticeDestination());
				}
			}
		}
    }
    
    public static void botched_dfs3(Graph g, int s) {
		Stack<Integer> stack = new Stack<Integer>();
		boolean visited[] = new boolean[g.vertices()];
		stack.push(s);
		while (!stack.empty()) {
			int u = stack.pop();
			if (!visited[u]){
				visited[u] = true;
				System.out.println(u);
				for (Edge e: g.next(u)) {
					if (!visited[e.getVerticeDestination()]) {
						stack.push(e.getVerticeDestination());
					}
				}
			}
		}
    }

    
    public static void botched_dfs4(Graph g, int s) {
		Stack<Integer> stack = new Stack<Integer>();
		boolean visited[] = new boolean[g.vertices()];
		stack.push(s);
		visited[s] = true;
		System.out.println(s);
		while (!stack.empty()) {
			boolean end = true;
			/* (a) Soit u le sommet en haut de la pile */
			/* (b) Si u a un voisin non visité, alors */
			/*     (c) on le visite et on l'ajoute sur la pile */
			/* Sinon */
			/*     (d) on enlève u de la pile */

			/* (a) */
			int u = stack.peek();
			for (Edge e: g.next(u))
			if (!visited[e.getVerticeDestination()]) /* (b) */ {
				visited[e.getVerticeDestination()] = true;
				System.out.println(e.getVerticeDestination());
				stack.push(e.getVerticeDestination()); /*(c) */
				end = false;
				break;
			}
			if (end) /*(d)*/
			stack.pop();
		}
		System.out.println(stack.capacity());
    }


    


    
    
    public static void testGraph()
    {
	int n = 5;
	int i,j;
	GraphArrayList g = new GraphArrayList(6);
	g.addEdge(new Edge(0, 1, 1));
	g.addEdge(new Edge(0, 2, 1));
	g.addEdge(new Edge(0, 3, 1));
	g.addEdge(new Edge(1, 4, 1));
	g.addEdge(new Edge(4, 3, 1));
	g.addEdge(new Edge(3, 5, 1));
	g.addEdge(new Edge(5, 1, 1));
	botched_dfs1(g, 0);
	botched_dfs2(g, 0);
	botched_dfs3(g, 0);
	botched_dfs4(g, 0);


    }
    
    public static ArrayList<Integer> iterativeDfs(Graph g, int u) {
	ArrayList<Integer> verticesEnd = new ArrayList<>();
	boolean visite[] = new boolean[g.vertices()];
	Stack<Integer> stackVertices = new Stack<Integer>();
	Stack<Iterator<Edge>> stackVerticesIterators = new Stack<Iterator<Edge>>();
	stackVertices.push(u);
	stackVerticesIterators.push(g.next(u).iterator());
	visite[u] = true;
	int vertice;
	while (!stackVerticesIterators.empty()) {
	    if (stackVerticesIterators.peek().hasNext()) {
		vertice = stackVerticesIterators.peek().next().getVerticeDestination();
		if (visite[vertice] == false) {
		    stackVerticesIterators.push(g.next(vertice).iterator());
		    stackVertices.push(vertice);
		    visite[vertice] = true;
		}
	    }
	    else {
	        verticesEnd.add(stackVertices.pop());
		stackVerticesIterators.pop();
	    }
	}
	return verticesEnd;
    }

    public static void main(String[] args)
    {
	testGraph();
    }

}
