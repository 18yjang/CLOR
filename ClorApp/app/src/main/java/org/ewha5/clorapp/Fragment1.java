package org.ewha5.clorapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.Handler;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

/*윤주*/
import java.net.URL;

import android.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.Interpreter;

public class Fragment1 extends AppCompatActivity {
    private static final String TAG = "Fragment1";

    //Context context;
    ImageView pictureImageView;
    //boolean isPhotoCaptured;

    Uri uri;
    File file;
    public static Bitmap resultPhotoBitmap;
    boolean received;

    /*윤주*/
    public static String URL = "http://146.56.166.169:5000/receive";


    //추가
    Handler handler = new Handler();

    ProgressDialog dialog;
    String picturePath;
    String mCurrentPath;
    public static String[] result_color = new String[7];

    private File tempFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment1);

        //사진 미리보기
        pictureImageView = findViewById(R.id.pictureImageView);

        //사진 촬영 버튼
        Button addButton1 = findViewById(R.id.addButton1);
        addButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showPhotoCaptureActivity();
            }
        });

        //갤러리에서 선택 버튼
        Button addButton2 = (Button) findViewById(R.id.addButton2);
        addButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhotoSelectionActivity();
            }
        });

        //저장 버튼 - 서버와 다음 단계로 연결
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //웹 서버로 연결
                //showMessage();
                Interpreter lite = getTfliteInterpreter("converted_model.tflite");
                //lite.run(resultPhotoBitmap, resultPhotoBitmap);

                Intent intent = new Intent(getApplicationContext(), ShowResult.class);


                //startActivityForResult(intent, AppConstants.REQ_SHOW_COMBINATION);

                request();
                //ProgressDialog pdialog
                dialog = ProgressDialog.show(Fragment1.this, "", "서버에서 정보를 가져오는 중입니다.");
                dialog.setCancelable(true);
                //dialog.setMax((int) 50000);
                //dialog.setProgress((int) 50000);

                //pdialog.show();
                //Log.e(TAG, "Here");

                //while(!received){
                    //Log.e(TAG,"NOOO");
                    /*if(received){
                        Log.e(TAG, "YESS");
                        pdialog.dismiss();
                        startActivityForResult(intent, AppConstants.REQ_SHOW_COMBINATION);
                        break;
                    }*/
                    //continue;
                //}

                Log.e(TAG, "여기여기여기여기여기여ㅣ겨이겨이겨이겨이");

            }

        });
    }

    //서버로 연결
    private void request() {
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                if(Build.VERSION.SDK_INT > 8){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }

                post(URL);
            }
            //서버 연결
        }, 1000);
    }

    public void post(String url){
        received = false;
        try{
            JSONObject obj = new JSONObject();
            try {
                obj.put("image", getStringFromBitmap(resultPhotoBitmap));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            URL url1 = new URL(url);

            HttpURLConnection conn = null;

            conn = (HttpURLConnection) url1.openConnection();
            conn.setRequestProperty("Accept-Encoding", "identity");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bufferedWriter.write(obj.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
            Log.e(TAG, "SENT");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            in.readLine();      // '[' 가 들어옴 (무시)
            for (int i = 0; i<7; i++){
                result_color[i] = in.readLine();
                result_color[i] = result_color[i].replace(" ", "");
                result_color[i] = result_color[i].replace(",", "");
                result_color[i] = result_color[i].replace("\"", "");
                //System.out.println("The string after removing character: " + result_color[i]);
            }
            //in.readLine();
                           // 전송결과를 전역 변수에 저장

            //응답코드 수신
            int responseCode = conn.getResponseCode();
            if(responseCode ==400){
                System.out.println("400: 실행오류");
            } else if (responseCode ==500){
                System.out.println("500: 서버오류");
            } else{
                System.out.println(responseCode+":응답코드");
            }
            Intent intent = new Intent(getApplicationContext(), ShowResult.class);
            dialog.dismiss();
            intent.putExtra("path", picturePath);
            startActivityForResult(intent, AppConstants.REQ_SHOW_COMBINATION);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public static String getStringFromBitmap(Bitmap bitmapPicture) {
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        Log.e(TAG, "0");
        return encodedImage;
    }


    //서버 연결 후, 다음 화면 넘어가기 위한 메시지
    /*private void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("안내");
        builder.setMessage("조합을 확인할까요?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);


        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //결과 보여주기 - ShowResult class로 연결

                //dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), ShowResult.class);

                //사진 경로 함께 보내기
                //intent.putExtra("path", picturePath);
               // startActivityForResult(intent, AppConstants.REQ_SHOW_COMBINATION);
                //사진 경로 확인용 메시지
                //Toast.makeText(getApplicationContext(), picturePath+" 파일 경로", Toast.LENGTH_SHORT).show();
                //AlertDialog dialogs = builder.create();
                //dialogs.show();
                //dialogs.setCancelable(false);
                //프로그레스 바 - 서버 후 수정 필요

                ProgressDialog pdialog = new ProgressDialog(Fragment1.this);
                pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pdialog.setMessage("서버에서 정보를 받아오는 중입니다.");
                //dialogc.setMax((int) 50000);
                //dialogc.setProgress((int) 50000);
                pdialog.show();
                Log.e(TAG, "Here");
                //request();


                //받아오고 나면,,

                while(!received){
                    //Log.e(TAG,"NOOO");
                    if(received){
                        Log.e(TAG, "YESS");
                        pdialog.dismiss();
                        break;
                    }
                    //continue;
                }
                //dialogs.dismiss();
                Log.e(TAG, "여기여기여기여기여기여ㅣ겨이겨이겨이겨이");
                startActivityForResult(intent, AppConstants.REQ_SHOW_COMBINATION);
                //Log.e(TAG, "저저기저깆거직저기적지겆기");
                //
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //설정하기
                //dialog.dismiss();

            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();


    }
      */

    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(Fragment1.this, modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public void showPhotoCaptureActivity() {

        try {
            file = createFile();
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            Log.e(TAG, file.getAbsolutePath());
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
        } else {
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(intent, AppConstants.REQ_PHOTO_CAPTURE);
        /*if(intent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;

            try{
                photoFile = createImageFile();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                Log.e(TAG, "사진오류");
            }
            if(photoFile!=null){
                Uri photoURI = FileProvider.getUriForFile(this, "org.ewha5.clorapp", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.e(TAG, String.valueOf(intent));
                startActivityForResult(intent, AppConstants.REQ_PHOTO_CAPTURE);
            }
            else{
                Log.e(TAG, "photoFile = NULL");
            }
        }*/
        /*
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(isIntentAvailable(getApplicationContext(),MediaStore.ACTION_IMAGE_CAPTURE)){
            try{
                Log.e(TAG, "여기있어요");
                File f = createImageFile();
                picturePath = f.getAbsolutePath();
                Log.e(TAG, "여기있어요22");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                Log.e(TAG, "여기있어요333");

            }catch(IOException e){
                e.printStackTrace();
            }
        }*/

    }


    //파일 경로 수정

/*
    private File createFile() {
        String filename = createFilename();
        //File outFile = new File(getFilesDir(), filename);
        File outFile = new File("/sdcard/Android/data/org.ewha5.clorapp/files", filename);
        Log.d("Main", "File path : " + outFile.getAbsolutePath());

        return outFile;
    }
*/

    private File createFile() {
        String filename = createFilename();
        File outFile = new File(getExternalFilesDir(null), filename);
        picturePath = outFile.toString();
        Log.d("Main", "File path : " + outFile.getAbsolutePath());
        return outFile;
    }

/*
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File image = File.createTempFile( imageFileName, ".png", storageDir );
        picturePath = image.getAbsolutePath();
        return image;
    }
*/
    //사진 촬영 끝...

    //
    public void showPhotoSelectionActivity() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, AppConstants.REQ_PHOTO_SELECTION);
    }
    //

    /**
     * 다른 액티비티로부터의 응답 처리
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode,  @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent != null) {
            switch (requestCode) {
                case AppConstants.REQ_PHOTO_CAPTURE:  // 사진 찍는 경우
                    Log.e(TAG, "onActivityResult() for REQ_PHOTO_CAPTURE.");
                    Log.e(TAG, "resultCode : " + resultCode);       // 확인완료


                    /*
                    ContentResolver resolver = context.getContentResolver();

                    try {
                        InputStream instream = resolver.openInputStream(fileUri);
                        resultPhotoBitmap = BitmapFactory.decodeStream(instream);
                        pictureImageView.setImageBitmap(resultPhotoBitmap);

                        instream.close();

                        isPhotoCaptured = true;
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                     */

                    /*
                    Cursor cursor = null;

                    try{
                        String[] proj = { MediaStore.Images.Media.DATA };
                        assert fileUri != null;
                        cursor = getContentResolver().query(fileUri, proj, null, null, null);

                        assert cursor != null;
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        tempFile = new File(cursor.getString(column_index));

                    }finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }

                    setImage();

                     */


                    //setPicture(file.getAbsolutePath(), 8);
                    resultPhotoBitmap = decodeSampledBitmapFromResource(file, pictureImageView.getWidth(), pictureImageView.getHeight());
                    /*윤주*/ //  이미지 -> 바이트 변환 -> Base64 인코딩 -> Json value 전달
                    pictureImageView.setImageBitmap(resultPhotoBitmap);

                    /*
                    if (intent != null && resultCode == RESULT_OK ) {


                        String[] filePathColumn = {picturePath};
                        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);

                        if(cursor == null || cursor.getCount() <1){
                            return;
                        }

                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                        if(columnIndex < 0)
                            return;

                        mCurrentPath = cursor.getString(columnIndex);
                        tempFile = new File(mCurrentPath);

                        cursor.close();

                        setImage();
                    }

                     */

                    Toast.makeText(getApplicationContext(), "사진 업로드에 성공했습니다!", Toast.LENGTH_SHORT).show();
                    break;

                case AppConstants.REQ_PHOTO_SELECTION:  // 사진을 앨범에서 선택하는 경우
                    Log.d(TAG, "onActivityResult() for REQ_PHOTO_SELECTION.");

                    Uri fileUri = intent.getData();


                    Log.d(TAG, "fileUri : " + fileUri);

                    /*
                    ContentResolver resolver = context.getContentResolver();

                    try {
                        InputStream instream = resolver.openInputStream(fileUri);
                        resultPhotoBitmap = BitmapFactory.decodeStream(instream);
                        pictureImageView.setImageBitmap(resultPhotoBitmap);

                        instream.close();

                        isPhotoCaptured = true;
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                     */

                    /*
                    Cursor cursor = null;

                    try{
                        String[] proj = { MediaStore.Images.Media.DATA };
                        assert fileUri != null;
                        cursor = getContentResolver().query(fileUri, proj, null, null, null);

                        assert cursor != null;
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        tempFile = new File(cursor.getString(column_index));

                    }finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }

                    setImage();

                     */

                    if (intent != null && resultCode == RESULT_OK ) {

                        try {
                            resultPhotoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(fileUri, filePathColumn, null, null, null);

                        if(cursor == null || cursor.getCount() <1){
                            return;
                        }

                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                        if(columnIndex < 0)
                            return;

                        picturePath = cursor.getString(columnIndex);
                        tempFile = new File(picturePath);


                        cursor.close();

                        setImage();
                    }
                    Toast.makeText(getApplicationContext(), "사진 업로드에 성공했습니다!", Toast.LENGTH_SHORT).show();

                    Bitmap resizeBitmap = null;
                    int width = resultPhotoBitmap.getWidth();
                    int height = resultPhotoBitmap.getHeight();
                    int newWidth = width;
                    int newHeight = height;
                    float rate = 0.0f;

                    if(width > height){
                        if(1000 < width){
                            rate = 1000 / (float)width;
                            newHeight = (int) (height*rate);
                            newWidth = 1000;
                        }
                    }
                    else{
                        if(1000 < height){
                            rate = 1000 / (float) height;
                            newWidth = (int)(width*rate);
                            newHeight = 1000;
                        }
                    }
                    resultPhotoBitmap = Bitmap.createScaledBitmap(resultPhotoBitmap, newWidth, newHeight, true);

                    break;

            }
        } else{
            Log.e(TAG, String.valueOf(intent));
        }
    }
    //onActivitybyresult 끝.

    private void setImage() {

        ImageView imageView = findViewById(R.id.pictureImageView);

        /*
        ExifInterface exif = null;
        int exifOrientation;
        int exifDegree;
        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else { exifDegree = 0; }
        imageView.setImageBitmap(rotate(originalBm,exifDegree));
         */


        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Log.e(TAG,"Im here~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        imageView.setImageBitmap(originalBm);

    }

    public static Bitmap decodeSampledBitmapFromResource(File res, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(res.getAbsolutePath(),options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(res.getAbsolutePath(),options);
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height;
            final int halfWidth = width;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private String createFilename() {
        Date curDate = new Date();
        String curDateStr = String.valueOf(curDate.getTime()) + ".png"; //확장자로 저장
        return curDateStr;
    }


    /*
    public String savePicture() {
        if (resultPhotoBitmap == null) {
            AppConstants.println("No picture to be saved.");
            return "";
        }

        File photoFolder = new File(AppConstants.FOLDER_PHOTO);

        if(!photoFolder.isDirectory()) {
            Log.d(TAG, "creating photo folder : " + photoFolder);
            photoFolder.mkdirs();
        }

        String photoFilename = createFilename();
        String picturePath = photoFolder + File.separator + photoFilename;

        try {
            FileOutputStream outstream = new FileOutputStream(picturePath);
            resultPhotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return picturePath;
    }
     */

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } return 0;

    }

    // 이미지를 회전시키는 메서드 선언
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }




}
