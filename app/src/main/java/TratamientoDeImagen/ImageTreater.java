package TratamientoDeImagen;

import android.graphics.Color;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Math.*;

//Por el momento utilizara el camara view pero esto puede cambiar mas adelante
public class ImageTreater {



    private static ImageTreater instance = null;


    protected  ImageTreater()
    {

    }
    public static ImageTreater getInstance(){
        if(instance == null){
            instance = new ImageTreater();
        }
        return instance;
    }
    public static Mat detectarColor(Mat src)
    {
        ArrayList<ColorDetector> rubicCube = new ArrayList<ColorDetector>();
        double[] vectorRojo={255,0,0};
        double[] vectorVerde={0,255,0};
        double[] vectorAzul={0,0,255};
        double[] vectorAmarillo={255,255,0};
        double[] vectorCian={0,255,255};
        double[] vectorMagenta={255,0,255};
        double[] vectorNegro={0,0,0};
        double[] vectorBlanco={255,255,255};
        rubicCube.add(new ColorDetector(vectorRojo,"rojo"));
        rubicCube.add(new ColorDetector(vectorVerde,"verde"));
        rubicCube.add(new ColorDetector(vectorAzul,"azul"));
        rubicCube.add(new ColorDetector(vectorAmarillo,"amarillo"));
        rubicCube.add(new ColorDetector(vectorCian,"cian"));
        rubicCube.add(new ColorDetector(vectorMagenta,"Magenta"));
        rubicCube.add(new ColorDetector(vectorNegro,"negro"));
        rubicCube.add(new ColorDetector(vectorBlanco,"blanco"));
        for (int i =0; i<src.rows();i++)
        {
            for (int j=0;j<src.cols();i++)
            {
                double menor = 100000;
                String masParecido="";
                for(ColorDetector evaluador:rubicCube)
                {
                    if(evaluador.calcularDistEuclidiana(src.get(i,j))<menor)
                    {
                        menor= evaluador.calcularDistEuclidiana(src.get(i,j));
                        masParecido=evaluador.getEtiqueta();
                    }
                }
                for (ColorDetector ganador:rubicCube)
                {
                    if(ganador.getEtiqueta().equals(masParecido))
                    {
                        ganador.asignarPixel(src.get(i,j));
                    }
                }
            }
        }
        return null;
    }
    public static Mat segmentarVisajes(Mat frame) {

        Mat kernel = new Mat(3, 3, CvType.CV_32F);
        float[] kernelData = new float[(int) (kernel.total() * kernel.channels())];
        kernelData[0] = 1;
        kernelData[1] = 1;
        kernelData[2] = 1;
        kernelData[3] = 1;
        kernelData[4] = -8;
        kernelData[5] = 1;
        kernelData[6] = 1;
        kernelData[7] = 1;
        kernelData[8] = 1;
        kernel.put(0, 0, kernelData);
        Mat imgLaplacian = new Mat();
        Imgproc.filter2D(frame, imgLaplacian, CvType.CV_32F, kernel);
        Mat sharp = new Mat();
        frame.convertTo(sharp, CvType.CV_32F);
        Mat imgResult = new Mat();
        Core.subtract(sharp, imgLaplacian, imgResult);
        imgResult.convertTo(imgResult, CvType.CV_8UC3);
        imgLaplacian.convertTo(imgLaplacian, CvType.CV_8UC3);


        Mat bw = new Mat();
        Imgproc.cvtColor(imgResult, bw, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(bw, bw, 40, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);


        Mat dist = new Mat();
        Imgproc.distanceTransform(bw, dist, Imgproc.DIST_L2, 3);
        Core.normalize(dist, dist, 0.0, 1.0, Core.NORM_MINMAX);
        Mat distDisplayScaled = new Mat();
        Core.multiply(dist, new Scalar(255), distDisplayScaled);
        Mat distDisplay = new Mat();
        distDisplayScaled.convertTo(distDisplay, CvType.CV_8U);


        Imgproc.threshold(dist, dist, 0.4, 1.0, Imgproc.THRESH_BINARY);
        Mat kernel1 = Mat.ones(3, 3, CvType.CV_8U);
        Imgproc.dilate(dist, dist, kernel1);
        Mat distDisplay2 = new Mat();
        dist.convertTo(distDisplay2, CvType.CV_8U);
        Core.multiply(distDisplay2, new Scalar(255), distDisplay2);


        Mat dist_8u = new Mat();
        dist.convertTo(dist_8u, CvType.CV_8U);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dist_8u, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat markers = Mat.zeros(dist.size(), CvType.CV_32S);
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(markers, contours, i, new Scalar(i + 1), -1);
        }
        Mat markersScaled = new Mat();
        markers.convertTo(markersScaled, CvType.CV_32F);
        Core.normalize(markersScaled, markersScaled, 0.0, 255.0, Core.NORM_MINMAX);
        Imgproc.circle(markersScaled, new Point(5, 5), 3, new Scalar(255, 255, 255), -1);
        Mat markersDisplay = new Mat();
        markersScaled.convertTo(markersDisplay, CvType.CV_8U);
        Imgproc.circle(markers, new Point(5, 5), 3, new Scalar(255, 255, 255), -1);


        Imgproc.watershed(imgResult, markers);
        Mat mark = Mat.zeros(markers.size(), CvType.CV_8U);
        markers.convertTo(mark, CvType.CV_8UC1);
        Core.bitwise_not(mark, mark);
        Random rng = new Random(12345);
        List<Scalar> colors = new ArrayList<>(contours.size());
        for (int i = 0; i < contours.size(); i++) {
            int b = rng.nextInt(256);
            int g = rng.nextInt(256);
            int r = rng.nextInt(256);
            colors.add(new Scalar(b, g, r));
        }
        Mat dst = Mat.zeros(markers.size(), CvType.CV_8UC3);
        byte[] dstData = new byte[(int) (dst.total() * dst.channels())];
        dst.get(0, 0, dstData);
        int[] markersData = new int[(int) (markers.total() * markers.channels())];
        markers.get(0, 0, markersData);
        for (int i = 0; i < markers.rows(); i++) {
            for (int j = 0; j < markers.cols(); j++) {
                int index = markersData[i * markers.cols() + j];
                if (index > 0 && index <= contours.size()) {
                    dstData[(i * dst.cols() + j) * 3 + 0] = (byte) colors.get(index - 1).val[0];
                    dstData[(i * dst.cols() + j) * 3 + 1] = (byte) colors.get(index - 1).val[1];
                    dstData[(i * dst.cols() + j) * 3 + 2] = (byte) colors.get(index - 1).val[2];
                } else {
                    dstData[(i * dst.cols() + j) * 3 + 0] = 0;
                    dstData[(i * dst.cols() + j) * 3 + 1] = 0;
                    dstData[(i * dst.cols() + j) * 3 + 2] = 0;
                }
            }
        }
        dst.put(0, 0, dstData);
        return dst;
    }
}
