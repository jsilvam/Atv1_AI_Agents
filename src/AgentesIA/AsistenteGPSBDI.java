package AgentesIA;

import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.GoalMaintainCondition;
import jadex.bdiv3.annotation.GoalTargetCondition;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.model.MProcessableElement.ExcludeMode;
import jadex.bridge.component.IExecutionFeature;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.AgentCreated;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.Description;

import java.util.*;

//TODO: Añadir mas intersecciones, actividades más espaciadas y limpiar el proyecto
@Agent
@Description("Primer Agente BDI con Objteivos Persistentes")
public class AsistenteGPSBDI {
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    
    //Atributo para acceder al grafo e inicializarlo
    ArrayList<ArrayList<Nodo>> grafo;
    
    //Atributo de las intersecciones inicializadas para añadirlo al grafo (lista de adyacencia).
    protected ArrayList<Nodo> nombreIntersecciones;
    
    //Atributo de las interseccion actual en el grafo. 
    //Es un Belief para que el agente pueda percibir el movimiento.
    @Belief
    Nodo interseccionActual;
    
    //Atributo de las actividades a realizar. 
    //Es un Belief para que el agente pueda sugerir las actividades cuando hay movimiento.
    @Belief
    protected Map<String, String> actividades;


    @AgentCreated
    public void init(){
        // Crear Lista de Adyacencia
        int V = 8;
        //ArrayList<ArrayList<Nodo>> grafo = new ArrayList<ArrayList<Nodo>>(V);
        grafo = new ArrayList<ArrayList<Nodo>>(V);

        //ArrayList<Nodo> nombreIntersecciones = new ArrayList<Nodo>();
        nombreIntersecciones = new ArrayList<Nodo>();

        for (int i = 0; i < V; i++)
        grafo.add(new ArrayList<Nodo>());

        // Añadir intersecciones a la lista de adyacencia.
        Nodo interseccion0 = new Nodo("Independencia", "Victor Lira", 160,0);
        Nodo interseccion1 = new Nodo("Victor Lira", "Mayta Capac",160,1);
        Nodo interseccion2 = new Nodo("Independencia", "2 de Mayo",90,2);
        Nodo interseccion3 = new Nodo("2 de Mayo", "Mayta Capac",90,3);
        Nodo interseccion4 = new Nodo("9 de Diciembre", "Independencia",100,4);
        Nodo interseccion5 = new Nodo("9 de Diciembre", "Mayta Capac",100,5);
        Nodo interseccion6 = new Nodo("15 de Agosto", "Independencia",100,6);
        Nodo interseccion7 = new Nodo("15 de Agosto", "Mayta Capac",100,7);

        nombreIntersecciones.add(interseccion0);
        nombreIntersecciones.add(interseccion1);
        nombreIntersecciones.add(interseccion2);
        nombreIntersecciones.add(interseccion3);
        nombreIntersecciones.add(interseccion4);
        nombreIntersecciones.add(interseccion5);
        nombreIntersecciones.add(interseccion6);
        nombreIntersecciones.add(interseccion7);

        Graph.addEdge(grafo, interseccion0, interseccion1);
        Graph.addEdge(grafo, interseccion0, interseccion2);
        Graph.addEdge(grafo, interseccion1, interseccion3);
        Graph.addEdge(grafo, interseccion3, interseccion2);
        Graph.addEdge(grafo, interseccion3, interseccion5);
        Graph.addEdge(grafo, interseccion2, interseccion4);
        Graph.addEdge(grafo, interseccion4, interseccion6);
        Graph.addEdge(grafo, interseccion5, interseccion7);

        //Intersección inicial seleccionada de forma aleatoria.
        Random rand = new Random(); 
        int interseccionRandom = rand.nextInt(6); 
        
        switch (interseccionRandom){
            case 0: interseccionActual = interseccion0; break;
            case 1: interseccionActual = interseccion1; break;
            case 2: interseccionActual = interseccion2; break;
            case 3: interseccionActual = interseccion3; break;
            case 4: interseccionActual = interseccion4; break;
            case 5: interseccionActual = interseccion5; break;
            case 6: interseccionActual = interseccion6; break;
            case 7: interseccionActual = interseccion7; break;
        }
        System.out.println("Inicia posicion actual: ");
        System.out.println(interseccionActual.getCalle1() + " - " + interseccionActual.getCalle2());
 
        
        actividades = new HashMap<String, String>();
        actividades.put("2 de Mayo", "comprar la camisa formal");
        actividades.put("Mayta Capac", "comer anticuchos");
        actividades.put("Victor Lira", "comprar viveres");
        actividades.put("Independencia", "ir a la vidrieria");

    }

    //Maintaing Goal para realizar el movimiento mientras la lista de actividades no termine.
    @Goal(excludemode = ExcludeMode.Never)
    public class MaintainStorageGoal{
        @GoalMaintainCondition(beliefs="actividades")
        protected boolean maintain(){
            return actividades.size()==0;
        }
    }

    //Top level Goal para realizar actividades mientras la lista de actividades no termine.
    @Goal(excludemode = ExcludeMode.Never)
    public class TopLevelGoal{       
        @GoalTargetCondition(beliefs="actividades")
        protected boolean target(){
            return actividades.size()==0;
        }
    }

    //Plan para realizar el movimiento, lo inicializa el Maintaing Goal.
    @Plan(trigger = @Trigger(goals=MaintainStorageGoal.class))
    protected void randomMove(){
        //System.out.println(interseccionActual.getCalle1() + " - " + interseccionActual.getCalle2());
        interseccionActual = Graph.siguienteRandom(grafo, interseccionActual);
        System.out.println("Nueva posición detectada: " + interseccionActual.getCalle1() + " - " + interseccionActual.getCalle2());
        execFeature.waitForDelay(4000).get();
    }

    //Plan para sugerir actividades, lo inicializa el Top Level Goal.
    @Plan(trigger = @Trigger(goals=TopLevelGoal.class))
    protected void executeActivity(){
        ArrayList<Nodo> intersecciones = Graph.siguientesIntersecciones(grafo, interseccionActual);
        for (Nodo interseccion : intersecciones){
            if (actividades.containsKey(interseccion.getCalle1())){
                System.out.println("Realizar actividad: " + actividades.get(interseccion.getCalle1()) + " en " + interseccion.getCalle1());
                interseccionActual = interseccion;
                actividades.remove(interseccion.getCalle1());
                System.out.println("Nueva posición después de actividad: " + interseccionActual.getCalle1() + " - " + interseccionActual.getCalle2());
                break;
            } else if (actividades.containsKey(interseccion.getCalle2())){
                System.out.println("Realizar actividad: " + actividades.get(interseccion.getCalle2()) + " en " + interseccion.getCalle2());
                interseccionActual = interseccion;
                actividades.remove(interseccion.getCalle2());
                System.out.println("Nueva posición después de actividad: " + interseccionActual.getCalle1() + " - " + interseccionActual.getCalle2());
                break;
            } 
        }
        execFeature.waitForDelay(4000).get();
    }

    //Agente busca el objetivo realizando los planes asociados que son iniciados en base a los beliefs. 
    @AgentBody
    public void body(){
        bdiFeature.dispatchTopLevelGoal(new MaintainStorageGoal());
        bdiFeature.dispatchTopLevelGoal(new TopLevelGoal());
    }
    
}
