package AgentesIA;

import java.util.*;

public class Graph {

    // Agrega un vertice bidireccional entre el nodo d y s a la lista am
    public static void addEdge(ArrayList<ArrayList<Nodo>> am, Nodo s, Nodo d) {
        am.get(s.id).add(d);
        am.get(d.id).add(s);

    }

    //Devuelve una intersección seleccionada de forma aleatoria desde el nodo a
    public static Nodo siguienteRandom(ArrayList<ArrayList<Nodo>> am, Nodo a) {
        Random rand = new Random(); 
        int interseccionRandom = rand.nextInt(am.get(a.getId()).size());
        //System.out.println("siguienteRandom: " + interseccionRandom); 
        //System.out.println("siguienteRandom: " + am.get(a.id).get(interseccionRandom).calle1 + am.get(a.id).get(interseccionRandom).calle2);
        return am.get(a.id).get(interseccionRandom);
    }

    //Devuelve la lista de intersecciones del nodo a
    public static ArrayList<Nodo> siguientesIntersecciones(ArrayList<ArrayList<Nodo>> am, Nodo a) {
        return am.get(a.id);
    }

    //Imprimir el grafo
    public static void printGraph(ArrayList<ArrayList<Nodo>> am, ArrayList<Nodo> nombreIntersecciones ) {
        for (int i = 0; i < am.size(); i++) {
            System.out.println("\nIntersecciones adyacentes de " + nombreIntersecciones.get(i).calle1 + "-" + nombreIntersecciones.get(i).calle2 + ":");
            for (int j = 0; j < am.get(i).size(); j++) {
                System.out.print(" -> " + am.get(i).get(j).calle1 + "-" + am.get(i).get(j).calle2);
            }
            System.out.println();
        }
    }
}