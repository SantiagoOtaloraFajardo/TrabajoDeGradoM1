package com.example.androidopencv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
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
import static org.opencv.imgproc.Imgproc.cvtColor;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraBridgeViewBase = (JavaCameraView)findViewById(R.id.CameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);
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

    protected Mat segmentarVisajes(Mat frame) {

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
                Utils.bitmapToMat(gallBM ,baseMat );

                String sceneName = getSceneName();
                //------
                // sceneName, recibe el texto del nombre
                // baseMat, mat de la img cargada
                // logData, string contiene el texto a mostrar
                String logData = sceneName;

                //-----------

                toOutViewLog( logData );
                evokeMat(baseMat);


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
