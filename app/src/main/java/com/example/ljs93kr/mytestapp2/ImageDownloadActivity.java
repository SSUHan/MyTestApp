package com.example.ljs93kr.mytestapp2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by ljs93kr on 2015-08-24.
 */
public class ImageDownloadActivity extends Activity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image_download);

        mImageView = (ImageView)findViewById(R.id.image_down_view);

    }

    public void DoDownload(View v){
        final ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(mImageView,R.drawable.default_image,R.drawable.error_image);

        ImageLoader imageLoader = MyVolley.getInstance(getApplicationContext()).getImageLoader();
        imageLoader.get(SystemMain.SERVER_ROOT_URL+ "/client_data/image.jpg",
                imageListener);
    }


}
