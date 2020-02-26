package TratamientoDeImagen;

import java.util.ArrayList;

public class ColorDetector {
    private ArrayList<Pixel> pixelesAsignados;
    private double[] valorColor;
    private String etiqueta;
    private double distEuclidiana;

    public ColorDetector(double[] valorColor, String etiqueta) {
        pixelesAsignados=new ArrayList<>();
        this.valorColor= new double[3];
        this.valorColor = valorColor;
        this.etiqueta=etiqueta;
        distEuclidiana = 0;
    }

    public ArrayList<Pixel> getPixelesAsignados() {
        return pixelesAsignados;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void asignarPixel(double[] valorPixel,int x, int y) {
        Pixel pixel = new Pixel(valorPixel,x,y);
        this.pixelesAsignados.add(pixel);
    }

    public double calcularDistEuclidiana(double[] valorColorSrc) {
        distEuclidiana=Math.sqrt(Math.pow((valorColor[0]-valorColorSrc[0]),2)+Math.pow((valorColor[1]-valorColorSrc[1]),2)+Math.pow((valorColor[2]-valorColorSrc[2]),2));
        return distEuclidiana;
    }
}
