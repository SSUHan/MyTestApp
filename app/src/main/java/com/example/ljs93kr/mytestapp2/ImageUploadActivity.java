package com.example.ljs93kr.mytestapp2;

import android.app.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ljs93kr on 2015-08-24.
 */
public class ImageUploadActivity extends Activity {

    final int REQ_CODE_SELECT_IMAGE=100;

    File mfile;
    private Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image_upload);
    }

    public void SearchPhoto(View v){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

    }

    public void DoUpload(View v){
        mfile = getImageFile(image_uri);
        if(mfile==null){
            Toast.makeText(getApplicationContext(),"이미지가 선택되지 않았습니다",Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("mfile",mfile.toString());

//        String sdString = Environment.getExternalStorageDirectory().getPath();
//        sdString += "/DCIM/Camera/20150818_130908.jpg";
//
//        mfile = new File(sdString);

        String url = SystemMain.SERVER_ROOT_URL+"/client_data/get_image.php";
        RequestQueue queue = MyVolley.getInstance(getApplicationContext()).getRequestQueue();
        MultipartRequest mRequest = new MultipartRequest(url,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"네트워크에 문제가 있습니다",Toast.LENGTH_SHORT).show();
                        Log.d("volley",error.getMessage());

                    }
                }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"이미지가 서버에 전송되었습니다",Toast.LENGTH_SHORT).show();
                Log.d("volley",response);

            }
        },mfile,null);
        queue.add(mRequest);

    }

    /**
     * 선택된 uri의 사진 Path를 가져온다
     * uri 가 없으면 널포인터를 반환한다.
     * @param uri
     * @return
     */
    private File getImageFile(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        if(uri == null){
           return null;
        }
        Cursor mCursor = getContentResolver().query(uri,projection,null,null,MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if(mCursor == null || mCursor.getCount()<1){
            return null;
        }
        int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();

        String path = mCursor.getString(column_index);
        if(mCursor!=null){
            mCursor.close();
            mCursor = null;
        }
        return new File(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_SHORT).show();

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode==Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    Log.d("image","data.getData() : "+data.getData());
                    image_uri = data.getData();

                    ImageView image = (ImageView)findViewById(R.id.image_upload_view);

                    //배치해놓은 ImageView에 set
                    image.setImageBitmap(image_bitmap);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public void test(View v){
        RequestQueue queue = MyVolley.getInstance(getApplicationContext()).getRequestQueue();
        String url = SystemMain.SERVER_ROOT_URL+"/test/test.php";
        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("test",response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.getMessage());
            }
        });
        queue.add(req);
    }


}
