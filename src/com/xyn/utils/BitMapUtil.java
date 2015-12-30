package com.xyn.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

public class BitMapUtil {
	
	private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩        
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

	public Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

public Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();        
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出    
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

/////////////////////////////////////////////////////////////////////////////////////////

/**
 * get the orientation of the bitmap {@link android.media.ExifInterface}
 * @param path
 * @return
 */
public final static int getDegress(String path) {
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
        e.printStackTrace();
    }
    return degree;
}
 
/**
 * rotate the bitmap 
 * @param bitmap
 * @param degress
 * @return
 */
public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
    if (bitmap != null) {
        Matrix m = new Matrix();
        m.postRotate(degress); 
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        return bitmap;
    }
    return bitmap;
}
 
/**
 * 压缩指定路径的图片，并得到图片对象
 * @param context
 * @param path bitmap source path
 * @return Bitmap {@link android.graphics.Bitmap}
 */
public final static Bitmap compress_path_length(String path, int rqsW, int rqsH) {
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, options);
    options.inSampleSize = caculateInSampleSize_length(options, rqsW, rqsH);
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(path, options);
}
 
/**
 * 计算压缩比例并返回压缩倍率
 * @param path
 * @return
 */
private static int caculateInSampleSize_length(BitmapFactory.Options options, int rqsW, int rqsH) {
    if (rqsW == 0 || rqsH == 0) return 1;
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;
    if (height > rqsH || width > rqsW) {
        final int heightRatio = Math.round((float) height/ (float) rqsH);
        final int widthRatio = Math.round((float) width / (float) rqsW);
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }
    return inSampleSize;
}
 
/**
 * 压缩字节流图片的长宽值，并返回压缩后的图像
 * @param byte
 * @param reqsW
 * @param reqsH
 * @return BitMap
 */
public final static Bitmap compress_byte_length(byte[] bts, int reqsW, int reqsH) {
    final Options options = new Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
    options.inSampleSize = caculateInSampleSize_length(options, reqsW, reqsH);
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
}
 
/**
 * 获取输入流图片的字节流，压缩图片的长宽和质量，并返回Bitmap
 * @return Bitmap {@link android.graphics.Bitmap}
 */
public final static Bitmap decodeStream_compress(InputStream is, int reqsW, int reqsH, long maxBytes) {
    try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ReadableByteChannel channel = Channels.newChannel(is);
        ByteBuffer buffer = ByteBuffer.allocate(64*1024);
        while (channel.read(buffer) != -1) {
            buffer.flip();
            while (buffer.hasRemaining()) 
            	baos.write(buffer.get());
            buffer.clear();
        }
        byte[] bts = baos.toByteArray();
        Bitmap bitmap = compress_byte_length(bts, reqsW, reqsH);
    	Log.e("BitmapBytes", "compressBitmap_inSampleSize_bytes:"+bts.length);
    	while (bts.length > maxBytes) {
            int quality = 90;
    		if(bts.length > 3*maxBytes)
            	quality = quality/2;
            else if(bts.length > 2*maxBytes)
            	quality = (int) (quality/1.5);
            else {
                if(quality > 60)
                	quality -= 15;
                else if(quality > 20)
                	quality -= 10;
                else if(quality > 10)
                	quality -= 5;
                else if(quality > 5)
                	quality -= 3;
                else if(quality > 2)
                	quality -= 2;
                else
                	break;
            }
    		baos.reset();
        	Log.e("BitmapBytes", "quality:"+quality);
            bitmap.compress(CompressFormat.JPEG, quality, baos);
            bts = baos.toByteArray();
    	}
        bitmap = BitmapFactory.decodeByteArray(bts, 0, bts.length);
    	Log.e("BitmapBytes", "compressBitmap_quality_bytes:"+bts.length);
        is.close();
        channel.close();
        baos.close();
        return bitmap;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

/**
 * 对Bitmap压缩质量，图片长宽不变
 * <br> 可先调用适当长宽压缩处理图片后，再调用此方法压缩图片质量
 * @param bts
 * @param maxBytes 压缩后的图像最大大小 单位为byte
 */
public final static Bitmap compress_BitMap_JPEG_byte(Bitmap bitmap, long maxBytes) {
    try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 90;
        bitmap.compress(CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length > maxBytes) {
            if(baos.toByteArray().length > 3*maxBytes)
            	quality = quality/2;
            else if(baos.toByteArray().length > 2*maxBytes)
            	quality = (int) (quality/1.5);
            else {
                if(quality > 60)
                	quality -= 15;
                else if(quality > 20)
                	quality -= 10;
                else if(quality > 10)
                	quality -= 5;
                else if(quality > 5)
                	quality -= 3;
                else if(quality > 2)
                	quality -= 2;
                else
                	break;
            }
            baos.reset();
        	Log.e("BitmapBytes", "quality:"+quality);
            bitmap.compress(CompressFormat.JPEG, quality, baos);
        }
        byte[] bts = baos.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(bts, 0, bts.length);
    	Log.e("BitmapBytes", "compressBitmap_JPEG_byte:"+bts.length);
        baos.close();
        return bmp;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
}

/**
 * 压缩已存在的图片对象，并返回压缩后的图片,使字节流不大于256k并限制最大长宽值
 * 字节流大小不代表实际图片的大小，字节流有内存和文件两种形式
 * @param bitmap
 * @param reqsW
 * @param reqsH
 */
public final static Bitmap compress_Bitmap_JPEG_length(Bitmap bitmap, int reqsW, int reqsH) {
    try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 90;
        bitmap.compress(CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length /1024 > 256) {
            baos.reset();
            quality -= 10;
            bitmap.compress(CompressFormat.JPEG, quality, baos);
        }
        byte[] bts = baos.toByteArray();
        Bitmap Bitmap = compress_byte_length(bts, reqsW, reqsH);
        baos.close();
        return Bitmap;
    } catch (IOException e) {
        e.printStackTrace();
        return bitmap;
    }
}

///**
// * 压缩已存在的图片对象，并返回压缩后的图片,注意！PNG方法未优化
// * @param bitmap
// * @param reqsW
// * @param reqsH
// * @return
// */
//public final static Bitmap compressBitmap_BitMapPNG_length(Bitmap bitmap, int reqsW, int reqsH) {
//    try {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(CompressFormat.PNG, 90, baos);
//        byte[] bts = baos.toByteArray();
//        Bitmap res = compressBitmap(bts, reqsW, reqsH);
//        baos.close();
//        return res;
//    } catch (IOException e) {
//        e.printStackTrace();
//        return bitmap;
//    }
//}
// 
///**
// * 基于质量的压缩算法， 此方法未 解决压缩后图像失真问题,注意！PNG方法未优化
// * <br> 可先调用比例压缩适当压缩图片后，再调用此方法可解决上述问题
// * @param bts
// * @param maxBytes 压缩后的图像最大大小 单位为byte
// * @return
// */
//public final static Bitmap compressBitmap_BitMapPNG_byte(Bitmap bitmap, long maxBytes) {
//    try {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int quality = 90;
//        bitmap.compress(CompressFormat.PNG, quality, baos);
//        while (baos.toByteArray().length > maxBytes) {
//            baos.reset();
//            bitmap.compress(CompressFormat.PNG, quality, baos);
//            quality -= 10;
//        }
//        byte[] bts = baos.toByteArray();
//        Bitmap bmp = BitmapFactory.decodeByteArray(bts, 0, bts.length);
//        baos.close();
//        return bmp;
//    } catch (IOException e) {
//        e.printStackTrace();
//        return null;
//    }
//}

/**
 * 压缩资源图片，并返回图片对象
 * @param res {@link android.content.res.Resources}
 * @param resID
 * @param reqsW
 * @param reqsH
 * @return
 */
public final static Bitmap compress_Resources_length(Resources res, int resID, int reqsW, int reqsH) {
    final Options options = new Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(res, resID, options);
    options.inSampleSize = caculateInSampleSize_length(options, reqsW, reqsH);
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeResource(res, resID, options);
}
 
/**
 * 得到指定路径图片的options
 * @param srcPath
 * @return Options {@link android.graphics.BitmapFactory.Options}
 */
public final static Options getBitmapOptions_path(String srcPath) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(srcPath, options);
    return options;
}
 
}
