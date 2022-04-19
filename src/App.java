import jadex.base.PlatformConfiguration;
import jadex.base.Starter;
import java.util.*;
import AgentesIA.Graph;
import AgentesIA.Nodo;

public class App {
    public static void main(String[] args) throws Exception {
        PlatformConfiguration config = PlatformConfiguration.getDefaultNoGui();
        config.addComponent("AgentesIA.AsistenteGPSBDI.class"); 
        Starter.createPlatform(config).get(); 
        
    }
}
