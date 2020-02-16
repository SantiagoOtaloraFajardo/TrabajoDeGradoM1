package Modelo;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;
import org.jgrapht.traverse.*;
import org.opencv.android.Utils;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
public class Escenario {
    private Graph<Place, DefaultEdge> places;
    private String nombre;
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


    }
    public void detectarPlace(Mat src ){
        Mat bN = Mat.zeros(src.size(), CvType.CV_8U);
        src.convertTo(bN, CvType.CV_8UC1);
        Mat rojos;
        Mat verdes;
        for (int i =0;i<rojos.rows;i++){
            for(int j=0;j<rojos.cols;j++){
                src.get(i,j);

            }

        }
    }
}
