package com.example.androidopencv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;
import org.jgrapht.traverse.*;
import java.lang.Math.*;

import Modelo.Escenario;
import TratamientoDeImagen.ColorDetector;
import TratamientoDeImagen.ImageTreater;
import TratamientoDeImagen.Pixel;

import static org.opencv.imgproc.Imgproc.cvtColor;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    Mat srcDetectarColor, dstDetectarColor;
    Scalar verdeOscuro, rojoOscuro, verdeClaro, rojoClaro;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBridgeViewBase = (JavaCameraView)findViewById(R.id.CameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);
        //Oscuro = componente bajo y Claro = componente alto
        verdeOscuro= new Scalar(25,51,25);
        verdeClaro= new Scalar(229,255,229);
        rojoOscuro= new Scalar(51,25,25);
        rojoClaro= new Scalar(255,229,229);
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);

                switch (status) {

                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }


            }
        };

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame;
        frame=inputFrame.rgba();


        return frame;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        srcDetectarColor = new Mat(width,height, CvType.CV_16UC4);
        dstDetectarColor = new Mat(width,height, CvType.CV_16UC4);
    }

    @Override
    public void onCameraViewStopped() {

    }
    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"There's a problem, yo!", Toast.LENGTH_SHORT).show();
        }

        else
        {
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }



    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase!=null){

            cameraBridgeViewBase.disableView();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraBridgeViewBase!=null){
            cameraBridgeViewBase.disableView();
        }
    }



    //----------------------------UI methods:>
    //wrapper to invoke cam app
    Mat baseMat;
    static final int REQ_NOOD=1, REQ_GALL = 2;
    public void invokeCamera(View view){
        Intent sendNoodsInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(sendNoodsInt.resolveActivity(getPackageManager()) != null){
            startActivityForResult( sendNoodsInt , REQ_NOOD );
        }
    }

    //doing thumbnail stuff for now, too sleppy
    @Override
    protected void onActivityResult( int REQ_COD, int RES_COD, Intent data ){
        if ( REQ_COD == REQ_NOOD &&  RES_COD == RESULT_OK  ) {
            Bundle extras = data.getExtras();
            Bitmap picBM = (Bitmap) extras.get("data");
            baseMat = new Mat(picBM.getHeight() , picBM.getWidth() , CvType.CV_32SC3);
            Utils.bitmapToMat(picBM,baseMat);
            cameraBridgeViewBase.enableView();
            ImageView IV = findViewById(R.id.picZone);
            IV.setImageBitmap(picBM);



        }
        if ( REQ_COD == REQ_GALL &&  RES_COD == RESULT_OK  ) {
            Uri selectedImage = data.getData();
            try {
                //ImageDecoder.Source imgSrc = ImageDecoder.createSource(  getContentResolver() , selectedImage );
                Bitmap gallBM = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                //Bitmap gallBM = ImageDecoder.decodeBitmap(imgSrc );
                ImageView IV = findViewById(R.id.picZone);

                //IV.setImageBitmap(gallBM);

                baseMat = new Mat();

                String sceneName = getSceneName();
                //------
                // sceneName, reibe el texto del nombre
                // baseMat, mat de la img cargada
                //data, string que debe contener e log
                String logData = sceneName;

                //-----------

                Utils.bitmapToMat(gallBM ,baseMat );
                Escenario escenario = new Escenario(sceneName);
                //escenario.detectarPlace(baseMat);
                Mat bN = Mat.zeros(baseMat.size(), CvType.CV_8U);
                Imgproc.cvtColor(baseMat,bN, Imgproc.COLOR_RGB2GRAY);
                //A baseMat hacerle lo de los colores
                //Buscar donde encuentro colores primarios y antiprimarios (Negro y blanco no) y retornar el punto donde se encontraron
                    //Recibe Mat baseMat
                    //Retornar = ArrayList<Double[]>
                /*
                    1) la lista de pixeles encontrados contine los 3 colores primarios y los 3 colores antiprimarios
                    2) la prueba es simplemente una Mat de fondo negro con el mismo tamaño y formato del baseMat
                        2.1) La idea es colorear en este fondo negro los pixeles que se encontraron
                        2.2) Por el momento estaria coloreando todo lo que se asemeje a: rojo, verde, azul, amarillo, magenta y cian
                        2.3) La funcion de detectarColores siempre puede limitarse a contar; por ejemplo, los pixeles rojos y magentas
                    3) La lista contiene entonces un conjunto de pixeles asignados. Estos, tienen los valores de su posicion en x,y y el valor de color
                        3.1) De esta forma se puede pintar sobre otra mat usando la funcion "put"
                 */
                ArrayList<ColorDetector> pixelesDeColores=new ArrayList<ColorDetector>();
                Mat pruebaColorDetector = Mat.zeros(baseMat.size(),CvType.CV_32SC3);
                pixelesDeColores=ImageTreater.detectarColor(baseMat);
                for(ColorDetector aux: pixelesDeColores)
                {
                    for(Pixel auxPixel: aux.getPixelesAsignados())
                    {
                        pruebaColorDetector.put(auxPixel.getX(),auxPixel.getY(),auxPixel.getValorPixel());
                    }
                }
                evokeMat(pruebaColorDetector);
                //Imgproc.adaptiveThreshold(bN, bN, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 30);
                Imgproc.Canny(bN, bN, 80, 200, 3);
                Mat mRgba= new Mat();
                Imgproc.cvtColor(bN, mRgba, Imgproc.COLOR_GRAY2RGB, 4);
                Mat lines= new Mat();
                Imgproc.HoughLinesP( bN, lines, 1, Math.PI/180, 100, 5, 5 );

                for (int x = 0; x < lines.rows(); x++)
                {
                    double[] vec = lines.get(x, 0);
                    double x1 = vec[0],
                            y1 = vec[1],
                            x2 = vec[2],
                            y2 = vec[3];
                    Point start = new Point(x1, y1);
                    Point end = new Point(x2, y2);

                    Imgproc.line(mRgba, start, end, new Scalar(255,0,0), 3);

                }
                evokeMat(mRgba);
                //evokeMat(baseMat);
                toOutViewLog( logData );


            } catch (IOException e) {
                Log.i("TAG", "Some exception " + e);
            }
        }
    }

    //wrapper to fetch from gallery

    public void invokeGallery(View view){
        Intent ImgPickInt = new Intent( Intent.ACTION_PICK );
        ImgPickInt.setType("image/*");
        startActivityForResult(ImgPickInt, REQ_GALL );


    }


    public void evokeMat(Mat mat){
        ImageView IV = findViewById(R.id.picZone);
        Bitmap temp = Bitmap.createBitmap(mat.width(),mat.height(), Bitmap.Config.ARGB_8888 );
        Utils.matToBitmap( mat , temp);
        IV.setImageBitmap(temp);

    }

    public void toOutViewLog (String logOut){ //to on screen log output
        TextView logOutV =  findViewById(R.id.dbgOut);
        logOutV.setText( logOut );

    }

    public String getSceneName(){
        TextView nameView = findViewById( R.id.nameInp );
        return nameView.getText().toString();
    }
}
