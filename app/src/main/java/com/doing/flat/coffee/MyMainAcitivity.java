package com.doing.flat.coffee;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.doing.flat.coffee.utils.Utils;


/**
 * Created by doing_hqg on 2016/8/28.
 */
public class MyMainAcitivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_banner);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setVisibility(View.GONE);
//        VideoView videoView = (VideoView) findViewById(R.id.videoView);
//        videoView.setVisibility(View.VISIBLE);
//        videoView.setVideoPath( "file://" +"/mnt/internal_sd/wenj.mp4");
//        videoView.setVideoPath( "file://" +"/内部储存/wenj.mp4");
//        videoView.setVideoURI(Uri.parse("android.resource://com.doing.flat.coffee/" + R.raw.wenj));
//       String url= Environment.getExternalStorageDirectory()+"/VID_20130215_125614.mp4";
//       String url= "/storage/emulated/0/Android/data/com.ourdoing.office.exhibition/files/7e471f88abc5c4f22c43ab3c3e4429e7.mp4";
        //
//        Utils.LogE("url="+url);
//        videoView.setVideoPath(url);
//        videoView.start();
//        "file://" +
//        videoView.start();
//        videoView.requestFocus();
    }

}
