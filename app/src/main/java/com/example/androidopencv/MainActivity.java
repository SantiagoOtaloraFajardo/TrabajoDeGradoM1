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
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

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

    //----------------------------UI methods:> //rather stuff i've done
    //wrapper to invoke cam app
    Mat baseMat;

    static final int REQ_NOOD=1, REQ_GALL = 2, REQ_CROP = 10;


    public void invokeCamera(View view){
        Intent sendNoodsInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(sendNoodsInt.resolveActivity(getPackageManager()) != null){
            startActivityForResult( sendNoodsInt , REQ_NOOD );
        }
    }

    //doing thumbnail stuff for now, too sleepy
    @Override
    protected void onActivityResult( int REQ_COD, int RES_COD, Intent data ){
        ImageView IV = findViewById(R.id.picZone);

        if (RES_COD == RESULT_OK) {

            if (REQ_COD == REQ_NOOD) {
                Bundle extras = data.getExtras();
                Bitmap picBM = (Bitmap) extras.get("data");
                baseMat = new Mat(picBM.getHeight(), picBM.getWidth(), CvType.CV_32SC3);
                Utils.bitmapToMat(picBM, baseMat);
                cameraBridgeViewBase.enableView();
                IV.setImageBitmap(picBM);


            }
            if (REQ_COD == REQ_GALL) {
                Uri selectedImage = data.getData();
                try {
                    //ImageDecoder.Source imgSrc = ImageDecoder.createSource(  getContentResolver() , selectedImage );
                    Bitmap gallBM = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    //Bitmap gallBM = ImageDecoder.decodeBitmap(imgSrc );


                    //IV.setImageBitmap(gallBM);

                    baseMat = new Mat();
                    Utils.bitmapToMat(gallBM, baseMat);

                    //------
                    // sceneName, recibe el texto del nombre
                    // baseMat, mat de la img cargada
                    // logData, string con texto a mostrar
                    String sceneName = getSceneName();
                    String logData = sceneName;

                    //-----------
                    logData += "opencv version :: " + Core.getVersionString();
                    toOutViewLog(logData);
                    evokeMat(baseMat);


                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }
            }
            if (REQ_COD == REQ_CROP){
                Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    getContentResolver().delete(uri, null, null);
                    IV.setImageBitmap(bitmap);
                    baseMat = new Mat();
                    Utils.bitmapToMat(bitmap,baseMat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    public void testBtnMethod(View v){

        Toast.makeText (this,"shiny",Toast.LENGTH_SHORT  );

        Intent openTestAct = new Intent( this, testActiv.class );

        startActivity(openTestAct);
        //startActivityForResult(<intent>);

    }

    public void invokeCrop(View view){
        Intent cropIntent = new Intent(this, ScanActivity.class );
        cropIntent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_MEDIA);
        startActivityForResult(cropIntent, REQ_CROP);

    }
}
