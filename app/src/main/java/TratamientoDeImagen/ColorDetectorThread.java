package TratamientoDeImagen;

import org.opencv.core.Mat;

import java.util.ArrayList;

public class ColorDetectorThread extends Thread{
    private ArrayList<ColorDetector> rubicCube = new ArrayList<ColorDetector>();
    private int inicioX;
    private int inicioY;
    private int finX;
    private int finY;
    private Mat src;
    public ColorDetectorThread(ArrayList<ColorDetector> rubicCube, int inicioX, int inicioY, int finX, int finY, Mat src) {
        this.rubicCube = rubicCube;
        this.inicioX = inicioX;
        this.inicioY = inicioY;
        this.finX = finX;
        this.finY = finY;
        this.src=src;
    }

    @Override
    public void run()
    {
        for (int i =inicioX; i<finX;i++)
        {
            for (int j=inicioY;j<finY;j++)
            {
                double menor = 100000;
                String masParecido="";
                for(ColorDetector evaluador:rubicCube)
                {
                    if(evaluador.getEtiqueta().equals("rojo")||evaluador.getEtiqueta().equals("magenta"))
                    {
                        if(evaluador.calcularDistEuclidiana(src.get(i,j))<menor)
                        {
                            menor= evaluador.calcularDistEuclidiana(src.get(i,j));
                            masParecido=evaluador.getEtiqueta();

                        }
                    }
                }
                for (int k=0; k<rubicCube.size();k++)
                {
                    if(rubicCube.get(k).getEtiqueta().equals(masParecido))
                    {
                        rubicCube.get(k).asignarPixel(src.get(i,j),i,j);
                    }
                }
            }
        }
    }

}
