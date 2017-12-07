package com.doing.flat.coffee.utils;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.doing.flat.coffee.R;
import com.doing.flat.coffee.entity.ADInfo;
import com.doing.flat.nostra13.universalimageloader.core.ImageLoader;

/**
 * ImageView创建工厂
 */
public class ViewFactory {

    /**
     * 获取ImageView视图的同时加载显示url
     *
     * @return
     */
    public static View getImageView(Context context, ADInfo urlInt) {
        View bannerView = LayoutInflater.from(context).inflate(
                R.layout.view_banner, null);
//        ImageView imageView = (ImageView) bannerView.findViewById(R.id.imageView);
////        com.doing.flat.coffee.ui.VideoView
//        VideoView videoView = (VideoView) bannerView.findViewById(R.id.videoView);
//        imageView.setVisibility(View.GONE);
//        videoView.setVisibility(View.GONE);
//        Utils.LogE("url=" + urlInt.getPathUrl());
//        if (urlInt.getFile_type().equals("1")) {
//            imageView.setVisibility(View.VISIBLE);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            Utils.setCanReplyImage(urlInt.getPathUrl(), imageView);
//        } else {
////            videoView.setVisibility(View.VISIBLE);
////            videoView.setVideoPath(urlInt.getPathUrl());//            videoView.setVideoPath("file://" +"/mnt/internal_sd/wenj.mp4");
////            videoView.start();
////            videoView.requestFocus();
//
//        }
        return bannerView;
    }
}
