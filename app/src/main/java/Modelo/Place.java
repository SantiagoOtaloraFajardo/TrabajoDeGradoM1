package Modelo;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;
import org.jgrapht.traverse.*;
public class Place {
    private Graph<SubPlace,DefaultEdge> subPlaces;
    private int id;
    private String nombre;
    private Boolean ocupado;
    public Place(int id){
        ocupado=false;
        this.id=id;
        nombre="";
    }
    public Graph<SubPlace, DefaultEdge> getSubPlaces() {
        return subPlaces;
    }

    public void setSubPlaces(Graph<SubPlace, DefaultEdge> subPlaces) {
        this.subPlaces = subPlaces;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getOcupado() {
        return ocupado;
    }

    public void setOcupado(Boolean ocupado) {
        this.ocupado = ocupado;
    }
}
