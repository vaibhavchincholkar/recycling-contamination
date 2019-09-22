package edu.bigredhack.recyclecontamination;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImage;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

public class MainActivity extends AppCompatActivity implements TaskCompleted {
    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    Button camera;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera= findViewById(R.id.camera);
        imageView= findViewById(R.id.imageView);


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }
    @Override
    public void onTaskComplete(String[] texts){
        Intent i= new Intent();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(this,"Please select an Image", Toast.LENGTH_LONG);
                return;
            }
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream);
                new RetrieveFeedTask(this).execute(bitmap1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            File mypath=new File("/storage/emulated/0/Np/","trash.jpg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                photo.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imageView.setImageBitmap(photo);
            new RetrieveFeedTask(this).execute(photo);

        }
    }

}

class RetrieveFeedTask extends AsyncTask<Bitmap, Void, String[]> {

    private Context callback;
    public RetrieveFeedTask(Context context){
        callback=context;

    }

    protected String[] doInBackground(Bitmap... image) {
        String[] texts= new String[2];
        try {
            IamOptions options = new IamOptions.Builder()
                    .apiKey("kZeFXb75vqGPka7LBNGe08QfVihFHFAZkjwpOzS5eQVM")
                    .build();
            VisualRecognition visualRecognition = new VisualRecognition("2018-03-19", options);
            //VisualRecognition vs= new VisualRecognition("",options);
            InputStream is= new FileInputStream("/storage/emulated/0/Np/trash.jpg");

            float th= (float) 0.5;
            ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                    .imagesFile(is)
                    .imagesFilename("bt.jpg")
                    //.url("https://www.betootaadvocate.com/wp-content/uploads/2018/10/handbag.png")
                    .classifierIds(Arrays.asList("DefaultCustomModel_1955313798"))
                    .threshold(th)
                    .build();
            //Collections.singletonList()
            //  visualRecognition.setEndPoint("https://gateway.watsonplatform.net/visual-recognition/api");
            ClassifiedImages classifiedImages = visualRecognition.classify(classifyOptions).execute();
            List<ClassifiedImage> lst= classifiedImages.getImages();

            Iterator<ClassifierResult> i= lst.get(0).getClassifiers().listIterator();
            if(i.hasNext()){
                String classname=i.next().getClasses().get(0).getClassName();
                if(classname.equals("bottle")){
                        texts[0]="bottle";
                        texts[1]="1.Remove the cap as it is not recyclable. \n " +
                                "2. Remove the brand sticker if any.\n" +
                                "3. Wash and dry the bottle.\n";
                }
                else if(classname.equals("cardboard")){
                    texts[0]="Cardboard";
                    texts[1]="1. Cannot be recycled if in contact with food items. \n" +
                            "2. Cut out any oil stains before recycling.\n" +
                            "3. Make sure that the cardboard is not wet.\n";
                }
                else if(classname.equals("aerosol_can")){
                    texts[0]="Aerosole Cans";
                    texts[1]="1.Remove the cap as it is not recyclable. \n " +
                            "2. Remove the brand sticker if any.\n" +
                            "3. Clean the bottle.\n";
                }
                else if(classname.equals("milkcarton")){
                    texts[0]="Milk Carton";
                    texts[1]="1. Wash and dry the container\n" +
                            "2. Remove the cap since it cannot be recycled\n" +
                            "3. Remove the label, if any\n";
                }
                else if(classname.equals("yogurt")){
                    texts[0]="Yogurt Cup";
                    texts[1]="1. Throw the plastic lid\n" +
                            "2. Wash and dry the cup \n" +
                            "3. Cannot be recycled if there are remains left.\n";
                }
            }
            else{
            //not recyclable
                texts[0]="Not Recyclable";
                texts[1]="================";
            }

            Log.d("List of images",""+lst);
            Log.d("Test result",""+classifiedImages);

        } catch (Exception e) {

        }
        return texts;
    }

    @Override
    protected void onPostExecute(String[] texts) {
        super.onPostExecute(texts);
        Intent intent= new Intent(callback,result.class);
        intent.putExtra("TITLE",texts[0]);
        intent.putExtra("STEPS",texts[1]);
        callback.startActivity(intent);
    }
}
