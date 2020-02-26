package Modelo;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;
import org.jgrapht.traverse.*;
import org.opencv.android.Utils;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

import java.util.*;
import java.util.Vector;

import TratamientoDeImagen.ImageTreater;

public class Escenario {
    private Graph<Place, DefaultEdge> places;
    private String nombre;
    private Graph<SubPlace, DefaultEdge> subPlaces;
    public Escenario(String nombre){
        places= new DefaultUndirectedGraph<>(DefaultEdge.class);
        Place place0=new Place(0);
        Place place1= new Place(1);
        Place place2= new Place(2);
        Place place3= new Place(3);
        Place place4= new Place(4);
        Place place5= new Place(5);
        Place place6= new Place(6);
        Place place7= new Place(7);
        Place place8= new Place(8);
        //add the vertices
        places.addVertex(place0);
        places.addVertex(place1);
        places.addVertex(place2);
        places.addVertex(place3);
        places.addVertex(place4);
        places.addVertex(place5);
        places.addVertex(place6);
        places.addVertex(place7);
        places.addVertex(place8);
        //add the edges
        places.addEdge(place0,place1);
        places.addEdge(place0,place3);
        places.addEdge(place0,place4);
        //edge places1
        places.addEdge(place1,place2);
        places.addEdge(place1,place3);
        places.addEdge(place1,place4);
        places.addEdge(place1,place5);
        //edge place2
        places.addEdge(place2,place4);
        places.addEdge(place2,place5);
        //edge place3
        places.addEdge(place3,place4);
        places.addEdge(place3,place7);
        places.addEdge(place3,place6);
        //edge place4
        places.addEdge(place4,place5);
        places.addEdge(place4,place6);
        places.addEdge(place4,place7);
        places.addEdge(place4,place8);
        //edge place5
        places.addEdge(place5,place7);
        places.addEdge(place5,place8);
        //edge place6
        places.addEdge(place6,place7);
        //edge place7
        places.addEdge(place7,place8);
        subPlaces= new DefaultUndirectedGraph<SubPlace, DefaultEdge>(DefaultEdge.class);
        //create vertex
        SubPlace subPlace1=new SubPlace(1);
        SubPlace subPlace2= new SubPlace(2);
        SubPlace subPlace3= new SubPlace(3);
        SubPlace subPlace4= new SubPlace(4);
        SubPlace subPlace5= new SubPlace(5);
        SubPlace subPlace6= new SubPlace(6);
        SubPlace subPlace7= new SubPlace(7);
        SubPlace subPlace8= new SubPlace(8);
        SubPlace subPlace9= new SubPlace(9);
        SubPlace subPlace10= new SubPlace(10);
        SubPlace subPlace11= new SubPlace(11);
        SubPlace subPlace12= new SubPlace(12);
        SubPlace subPlace13= new SubPlace(13);
        SubPlace subPlace14= new SubPlace(14);
        SubPlace subPlace15= new SubPlace(15);
        SubPlace subPlace16= new SubPlace(16);
        //agregar Vertices
        subPlaces.addVertex(subPlace1);
        subPlaces.addVertex(subPlace2);
        subPlaces.addVertex(subPlace3);
        subPlaces.addVertex(subPlace4);
        subPlaces.addVertex(subPlace5);
        subPlaces.addVertex(subPlace6);
        subPlaces.addVertex(subPlace7);
        subPlaces.addVertex(subPlace8);
        subPlaces.addVertex(subPlace9);
        subPlaces.addVertex(subPlace10);
        subPlaces.addVertex(subPlace11);
        subPlaces.addVertex(subPlace12);
        subPlaces.addVertex(subPlace13);
        subPlaces.addVertex(subPlace14);
        subPlaces.addVertex(subPlace15);
        subPlaces.addVertex(subPlace16);
        //create edges
        subPlaces.addEdge(subPlace1,subPlace2);
        subPlaces.addEdge(subPlace1,subPlace5);
        subPlaces.addEdge(subPlace2,subPlace3);
        subPlaces.addEdge(subPlace2,subPlace6);
        subPlaces.addEdge(subPlace3,subPlace4);
        subPlaces.addEdge(subPlace3,subPlace7);
        subPlaces.addEdge(subPlace4,subPlace8);
        subPlaces.addEdge(subPlace5,subPlace6);
        subPlaces.addEdge(subPlace5,subPlace9);
        subPlaces.addEdge(subPlace6,subPlace7);
        subPlaces.addEdge(subPlace6,subPlace10);
        subPlaces.addEdge(subPlace7,subPlace8);
        subPlaces.addEdge(subPlace7,subPlace11);
        subPlaces.addEdge(subPlace8,subPlace12);
        subPlaces.addEdge(subPlace9,subPlace10);
        subPlaces.addEdge(subPlace9,subPlace13);
        subPlaces.addEdge(subPlace10,subPlace11);
        subPlaces.addEdge(subPlace10,subPlace14);
        subPlaces.addEdge(subPlace11,subPlace12);
        subPlaces.addEdge(subPlace11,subPlace15);
        subPlaces.addEdge(subPlace12,subPlace16);
        subPlaces.addEdge(subPlace13,subPlace14);
        subPlaces.addEdge(subPlace14,subPlace15);
        subPlaces.addEdge(subPlace15,subPlace16);


    }
    public void detectarPlace(Mat src ){
        Mat bN = Mat.zeros(src.size(), CvType.CV_8U);
        Scalar rojoHigh=new Scalar(255,229,229);
        Scalar rojoLow= new Scalar(51,25,25);
        double x1;
        double y1;
        double x2;
        double y2;
        int lineasH;
        int lineasV;
        int nodo;
        Place p;
        Imgproc.cvtColor(src,bN, Imgproc.COLOR_RGB2GRAY);
        Mat rojos=Mat.zeros(src.size(), CvType.CV_8U);
        //rojos=ImageTreater.detectarColor(src,rojoLow,rojoHigh);
        Mat wLocMat = Mat.zeros(rojos.size(), rojos.channels());
        Core.findNonZero(rojos, wLocMat);
        Imgproc.adaptiveThreshold(bN, bN, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 30);

        for(int i=0;i<wLocMat.total();i++){
            double[] puntico=wLocMat.get(0, i);
            x1=puntico[0];
            y1=puntico[1];
            Log.d("Largo:",""+x1);
            Log.d("Ancho",""+y1);
            lineasV=contarLineasVerticales(bN,(int)x1,(int)y1);
            lineasH=contarLineasHorizontales(bN,(int)x1,(int)y1);
            nodo=encontrarNodo(lineasH,lineasV);
            if(nodo!=-1) {
                p = getVertice(nodo);
                //places.get
                if(!p.getOcupado()) {
                    int code=asignarSubPlaces(p);
                    if(code==0){
                        p.setOcupado(true);
                        Log.d("DetectarPlace","Se creó el place");
                    }
                }
            }
            else{
                Log.d("DetectarPlace","No se pudo crear el place");
            }

        }
        int FilaPlace;
        int columPlace;

    }
    public int contarLineasVerticales(Mat src, int x, int y){
        Scalar negro= new Scalar(0,0,0);
        double[] zero= {0};
        int contadorVerticales=0;
        int numLineas=0;
        int grosor=0;
        int inicio =y;
        Log.d("Valor1:",""+src.get(x,inicio)[0]);
        while(zero[0] == src.get(x,inicio)[0])
            inicio++;
        for(int i=inicio;i<src.rows();i++)
        {
            Log.d("TamFilas=",""+src.rows());
            Log.d("Posición=",""+i);
            Log.d("Filas",""+src.get(x,i)[0]);
            if(zero[0] == src.get(x,i)[0]) {
                grosor++;
                if(zero[0] != src.get(x,i++)[0])
                    break;
            }
        }
        Log.d("Grosor:",""+grosor);
        for (int i=inicio;i<src.rows();i++){
            if(zero[0] == src.get(x,i)[0]){
                contadorVerticales++;
            }
        }
        Log.d("ContadorVerticales:",""+contadorVerticales);

        numLineas=contadorVerticales/grosor;

        return numLineas;
    }
    public int contarLineasHorizontales(Mat src, int x, int y){
        Scalar negro= new Scalar(0,0,0);
        double[] zero= {0};
        int contadorHorizontal=0;
        int numLineas;
        int grosor=0;
        Boolean blanco=false;
        Boolean negroB= true;
        int inicio =x;
        while(zero == src.get(inicio,y))
            inicio++;
        for(int i=inicio;i<src.rows();i++)
        {
            if(zero == src.get(i,y)) {
                grosor++;
                if(zero != src.get(i++,y))
                    break;
            }
        }
        for (int i=inicio;i<src.rows();i++){
            if(zero == src.get(i,y)){
                contadorHorizontal++;

            }

        }
        Log.d("Grosor",""+grosor);
        numLineas=contadorHorizontal/grosor;
        return numLineas;
    }
    public int encontrarNodo(int lineasH,int lineasV){
        int id;
        if(lineasH==3 && lineasV==3){
            return 0;
        }
        else if(lineasH==3 && lineasV==2){
            return 1;
        }
        else if(lineasH==3 && lineasV==1){
            return 2;
        }
        else if(lineasH==2 && lineasV==3){
            return 3;
        }
        else if(lineasH==2 && lineasV==2){
            return 4;
        }
        else if(lineasH==2 && lineasV==1){
            return 5;
        }
        else if(lineasH==1 &&lineasV==3){
            return 6;
        }
        else if(lineasH==1 && lineasV==2){
            return 7;
        }
        else if(lineasH==1 && lineasV==1){
            return 8;
        }
        return -1;
    }
    public Place getVertice(int id){
        Set<Place> setPlaces= places.vertexSet();
        for (Iterator<Place> it = setPlaces.iterator(); it.hasNext(); ) {
            Place p = it.next();
            if (p.equals(new Place(id)))
                return p;
        }
        return null;
    }
    public SubPlace getSubPlace(int id){
        Set<SubPlace> setSubPlaces= subPlaces.vertexSet();
        for (Iterator<SubPlace> it = setSubPlaces.iterator(); it.hasNext(); ) {
            SubPlace sp = it.next();
            if (sp.equals(new Place(id)))
                return sp;
        }
        return null;
    }
    public int asignarSubPlaces(Place p){
        SubPlace subplace1;
        SubPlace subplace2;
        SubPlace subPlace3;
        SubPlace subPlace4;
        if(p.getId()==0){
            subplace1=getSubPlace(1);
            subplace2=getSubPlace(2);
            subPlace3=getSubPlace(5);
            subPlace4=getSubPlace(6);
            if(subplace1.getPlace()==null && subplace2.getPlace()==null && subPlace3.getPlace()==null &&
            subPlace4.getPlace()==null){
                subplace1.setPlace(p);
                subplace2.setPlace(p);
                subPlace3.setPlace(p);
                subPlace4.setPlace(p);
                return 0;
            }
            else{
                return -1;
            }
        }
        else if(p.getId()==1){
            subplace1=getSubPlace(2);
            subplace2=getSubPlace(3);
            subPlace3=getSubPlace(6);
            subPlace4=getSubPlace(7);
            if(subplace1.getPlace()==null && subplace2.getPlace()==null && subPlace3.getPlace()==null &&
                    subPlace4.getPlace()==null){
                subplace1.setPlace(p);
                subplace2.setPlace(p);
                subPlace3.setPlace(p);
                subPlace4.setPlace(p);
                return 0;
            }
            else{
                return -1;
            }
        }
        else if(p.getId()==2){
            subplace1=getSubPlace(3);
            subplace2=getSubPlace(4);
            subPlace3=getSubPlace(7);
            subPlace4=getSubPlace(8);
            if(subplace1.getPlace()==null && subplace2.getPlace()==null && subPlace3.getPlace()==null &&
                    subPlace4.getPlace()==null){
                subplace1.setPlace(p);
                subplace2.setPlace(p);
                subPlace3.setPlace(p);
                subPlace4.setPlace(p);
                return 0;
            }
            else{
                return -1;
            }
        }
        else if(p.getId()==3){
            subplace1=getSubPlace(5);
            subplace2=getSubPlace(6);
            subPlace3=getSubPlace(9);
            subPlace4=getSubPlace(10);
            if(subplace1.getPlace()==null && subplace2.getPlace()==null && subPlace3.getPlace()==null &&
                    subPlace4.getPlace()==null){
                subplace1.setPlace(p);
                subplace2.setPlace(p);
                subPlace3.setPlace(p);
                subPlace4.setPlace(p);
                return 0;
            }
            else{
                return -1;
            }
        }
        else if(p.getId()==4){
            subplace1=getSubPlace(6);
            subplace2=getSubPlace(7);
            subPlace3=getSubPlace(10);
            subPlace4=getSubPlace(11);
            if(subplace1.getPlace()==null && subplace2.getPlace()==null && subPlace3.getPlace()==null &&
                    subPlace4.getPlace()==null){
                subplace1.setPlace(p);
                subplace2.setPlace(p);
                subPlace3.setPlace(p);
                subPlace4.setPlace(p);
                return 0;
            }
            else{
                return -1;
            }
        }
        else if(p.getId()==5){
            subplace1=getSubPlace(7);
            subplace2=getSubPlace(8);
            subPlace3=getSubPlace(11);
            subPlace4=getSubPlace(12);
            if(subplace1.getPlace()==null && subplace2.getPlace()==null && subPlace3.getPlace()==null &&
                    subPlace4.getPlace()==null){
                subplace1.setPlace(p);
                subplace2.setPlace(p);
                subPlace3.setPlace(p);
                subPlace4.setPlace(p);
                return 0;
            }
            else{
                return -1;
            }
        }
        else if(p.getId()==6){
            subplace1=getSubPlace(9);
            subplace2=getSubPlace(10);
            subPlace3=getSubPlace(13);
            subPlace4=getSubPlace(14);
            if(subplace1.getPlace()==null && subplace2.getPlace()==null && subPlace3.getPlace()==null &&
                    subPlace4.getPlace()==null){
                subplace1.setPlace(p);
                subplace2.setPlace(p);
                subPlace3.setPlace(p);
                subPlace4.setPlace(p);
                return 0;
            }
            else{
                return -1;
            }
        }
        else if(p.getId()==7){
            subplace1=getSubPlace(10);
            subplace2=getSubPlace(11);
            subPlace3=getSubPlace(14);
            subPlace4=getSubPlace(15);
            if(subplace1.getPlace()==null && subplace2.getPlace()==null && subPlace3.getPlace()==null &&
                    subPlace4.getPlace()==null){
                subplace1.setPlace(p);
                subplace2.setPlace(p);
                subPlace3.setPlace(p);
                subPlace4.setPlace(p);
                return 0;
            }
            else{
                return -1;
            }
        }
        else if(p.getId()==8){
            subplace1=getSubPlace(11);
            subplace2=getSubPlace(12);
            subPlace3=getSubPlace(15);
            subPlace4=getSubPlace(16);
            if(subplace1.getPlace()==null && subplace2.getPlace()==null && subPlace3.getPlace()==null &&
                    subPlace4.getPlace()==null){
                subplace1.setPlace(p);
                subplace2.setPlace(p);
                subPlace3.setPlace(p);
                subPlace4.setPlace(p);
                return 0;
            }
            else{
                return -1;
            }
        }

        return -1;
    }
    public Boolean verificarDisponibilidad(Place p){
        Set<DefaultEdge>aristas=places.edgesOf(p);
        for (Iterator<DefaultEdge> it = aristas.iterator(); it.hasNext(); ) {
            DefaultEdge dE = it.next();
        }
        return false;
    }
}
