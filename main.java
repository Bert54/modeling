import java.io.File;
import java.util.ArrayList;

public class main {

    /**
     * 1ier argument : nom de l'image source
     * @param args
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
        int[][] image = SeamCarving.readpgm(nom + ".pgm");  // Mise en tableau de l'image
        int[][] interest = SeamCarving.interest(image); // On vérifie les coefficients d'interet des pixel
        Graph graph = SeamCarving.toGraph(interest);
        ArrayList<Integer> arl = new ArrayList<>();
        arl = SeamCarving.tritopo(graph);
        ArrayList<Integer> bm = new ArrayList<>();
        bm = SeamCarving.bellman(graph, 1, graph.vertices()-1, arl);
        String nomCarving = nom + "_seamCarving.pgm";   // Nouveau nom du fichier
        SeamCarving.writepgm(interest, nomCarving); // On ne remplace pas le fichier d'origine
    }

}
