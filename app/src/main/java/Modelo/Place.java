package Modelo;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;
import org.jgrapht.traverse.*;

import java.util.ArrayList;

public class Place {
    private int id;
    private String nombre;
    private Boolean ocupado;
    private double xmin;
    private double xmax;
    private double ymin;
    private double ymax;
    public Place(int id){
        ocupado=false;
        this.id=id;
        nombre="";
        xmin=0;
        ymin=0;
        xmax=0;
        ymax=0;

    }
    public void AsignarSubGrafo(){
        if(this.id==0){

        }

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

    public double getXmin() {
        return xmin;
    }

    public void setXmin(double xmin) {
        this.xmin = xmin;
    }

    public double getXmax() {
        return xmax;
    }

    public void setXmax(double xmax) {
        this.xmax = xmax;
    }

    public double getYmin() {
        return ymin;
    }

    public void setYmin(double ymin) {
        this.ymin = ymin;
    }

    public double getYmax() {
        return ymax;
    }

    public void setYmax(double ymax) {
        this.ymax = ymax;
    }

}
