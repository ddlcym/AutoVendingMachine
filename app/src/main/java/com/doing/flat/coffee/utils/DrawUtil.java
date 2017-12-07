package com.doing.flat.coffee.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

public class DrawUtil {
    public static double getTextHeight(float frontSize) {
        Paint paint = new Paint();
        paint.setTextSize(frontSize);
        FontMetrics fm = paint.getFontMetrics();
        return Math.ceil(fm.descent - fm.top) + 2;
    }

    public static float getTextWidth(float frontSize, String text) {
        Paint paint = new Paint();
        paint.setTextSize(frontSize);
        return paint.measureText(text);
    }

    public static int[] getScreenSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int displayWidth = dm.widthPixels;
        int displayHeight = dm.heightPixels;
        return new int[]{displayWidth, displayHeight};
    }

    public static int getStatusBarHeight(Context context) {
        Rect frame = new Rect();
        ((Activity) context).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        return statusBarHeight;
    }

    public static int getTtitleBarHeight(Context context) {
        int contentTop = ((Activity) context).getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // statusBarHeight�����������״̬���ĸ߶�
        int titleBarHeight = contentTop - getStatusBarHeight(context);
        return titleBarHeight;
    }

    public static int sp2px(Context context, float spValue) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (spValue * dm.scaledDensity + 0.5f);
    }


    public static int[] getViewSize(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        return new int[]{width, height};
    }

    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (px - 0.5f) / scale;
    }

    public static int getActionBarHeight(Context context) {
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize,
                tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data, context.getResources().getDisplayMetrics());
            return actionBarHeight;
        }
        return 0;
    }

    public static float getScaleTextWidth(TextPaint itemsPaint, String string) {
        // TODO Auto-generated method stub
        return itemsPaint.measureText(string);
    }

    public static Bitmap convertViewToBitmap(View view) {

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        //利用bitmap生成画布
        Canvas canvas = new Canvas(bitmap);

        //把view中的内容绘制在画布上
        view.draw(canvas);

        return bitmap;
    }

    private static int QR_WIDTH = 800, QR_HEIGHT = 800;

    /**
     * 生成QR图
     */
    public static Bitmap createImage(String url) {
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();

            String text = url;

            if (text == null || "".equals(text) || text.length() < 1) {
                return null;
            }

            // 把输入的文本转为二维码
            BitMatrix martix = null;
            try {
                martix = writer.encode(text, BarcodeFormat.QR_CODE,
                        200, 200);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            System.out.println("w:" + martix.getWidth() + "h:"
                    + martix.getHeight());

//            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); //编码
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
//                        pixels[y * QR_WIDTH + x] = 0x00ffffff;
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static final int IMAGE_HALFWIDTH = 50;//宽度值，影响中间图片大小

    /**
     * 生成二维码
     *
     * @param string  二维码中包含的文本信息
     * @param mBitmap logo图片
     * @return Bitmap 位图
     * @throws WriterException
     */
    public static Bitmap createCode(String string, Bitmap mBitmap) {
        try {
            Matrix m = new Matrix();
            float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
            float sy = (float) 2 * IMAGE_HALFWIDTH
                    / mBitmap.getHeight();
            m.setScale(sx, sy);//设置缩放信息
            //将logo图片按martix设置的信息缩放
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                    mBitmap.getWidth(), mBitmap.getHeight(), m, false);
            MultiFormatWriter writer = new MultiFormatWriter();
            Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
            hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");//设置字符编码
            BitMatrix matrix = writer.encode(string, BarcodeFormat.QR_CODE, 400, 400, hst);//生成二维码矩阵信息
            int width = matrix.getWidth();//矩阵高度
            int height = matrix.getHeight();//矩阵宽度
            int halfW = width / 2;
            int halfH = height / 2;
            int[] pixels = new int[width * height];//定义数组长度为矩阵高度*矩阵宽度，用于记录矩阵中像素信息
            for (int y = 0; y < height; y++) {//从行开始迭代矩阵
                for (int x = 0; x < width; x++) {//迭代列
                    if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                            && y > halfH - IMAGE_HALFWIDTH
                            && y < halfH + IMAGE_HALFWIDTH) {//该位置用于存放图片信息
//记录图片每个像素信息
                        pixels[y * width + x] = mBitmap.getPixel(x - halfW
                                + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                    } else {
                        if (matrix.get(x, y)) {//如果有黑块点，记录信息
                            pixels[y * width + x] = 0xff000000;//记录黑块信息
                        }
                    }

                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            // 通过像素数组生成bitmap
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 保存头像图片
     *
     * @param bitmap
     */
    public static String saveHeadBitmap(Bitmap bitmap, int options, String fex_id) {
        String imagePath = "";
        String path = "";
        if (bitmap != null) {
            imagePath = Environment.getExternalStorageDirectory()
                    + "/slab/";
            File tempFile = new File(imagePath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            path = imagePath + "" + fex_id + ".jpg";
            File tempFile2 = new File(path);
            if (tempFile2.isFile()) {
                tempFile2.delete();
            }
            if (!tempFile2.exists()) {
                try {
                    tempFile2.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(tempFile2, true));
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, bos);
                bos.flush();
                bos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return path;
    }


    public static Bitmap scaleImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float ww, hh;
        ww = 640f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = (int) (w / ww);// be=1表示不缩放

        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 压缩好比例大小后再进行质量压缩
        int degree = readPictureDegree(srcPath);
        if (degree == 0) {
            return bitmap;
        } else {
            return adjustPhotoRotation(bitmap, degree);// 处理旋转
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
//            Utils.LogE("获取角度失败！");
            e.printStackTrace();
        }
        return degree;
    }
    /**
     * 旋转bitmap
     *
     * @param bm                要旋转的bitmap
     * @param orientationDegree 要旋转的角度
     * @return
     */
    public static Bitmap adjustPhotoRotation(Bitmap bm,
                                             final int orientationDegree) {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2,
                (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }

        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        m.postTranslate(targetX - x1, targetY - y1);

        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(),
                Bitmap.Config.RGB_565);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);

        bm.recycle();
        bm = null;
        return bm1;
    }
}
