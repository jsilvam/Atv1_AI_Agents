package AgentesIA;
import java.util.*;

//Clase Nodo que almacena las intersecciones y distancia entre ellas
public class Nodo{
    int longitud;
    int id;
    String calle1;
    String calle2;


    public Nodo(){}

    public Nodo(String calle1, String calle2, int longitud, int id){
        this.calle1 = calle1;
        this.calle2 = calle2;
        this.longitud = longitud;
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }


    public String getCalle1(){
        return calle1;
    }

    public void setCalle1(String calle1){
        this.calle1=calle1;
    }

    public String getCalle2(){
        return calle2;
    }

    public void setCalle2(String calle2){
        this.calle2=calle2;
    }
}