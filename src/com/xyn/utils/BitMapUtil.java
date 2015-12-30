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
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {    //ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��        
            baos.reset();//����baos�����baos
            options -= 10;//ÿ�ζ�����10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ��������ݴ�ŵ�baos��

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ���������baos��ŵ�ByteArrayInputStream��
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//��ByteArrayInputStream��������ͼƬ
        return bitmap;
    }

	public Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//��ʱ����bmΪ��

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ
        float hh = 800f;//�������ø߶�Ϊ800f
        float ww = 480f;//�������ÿ��Ϊ480f
        //���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
        int be = 1;//be=1��ʾ������
        if (w > h && w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//�������ű���
        //���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//ѹ���ñ�����С���ٽ�������ѹ��
    }

public Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();        
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//�ж����ͼƬ����1M,����ѹ������������ͼƬ��BitmapFactory.decodeStream��ʱ���    
            baos.reset();//����baos�����baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//����ѹ��50%����ѹ��������ݴ�ŵ�baos��
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ
        float hh = 800f;//�������ø߶�Ϊ800f
        float ww = 480f;//�������ÿ��Ϊ480f
        //���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
        int be = 1;//be=1��ʾ������
        if (w > h && w > ww) {//�����ȴ�Ļ����ݿ�ȹ̶���С����
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//����߶ȸߵĻ����ݿ�ȹ̶���С����
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//�������ű���
        newOpts.inPreferredConfig = Config.RGB_565;//����ͼƬ��ARGB888��RGB565
        //���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//ѹ���ñ�����С���ٽ�������ѹ��
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
 * ѹ��ָ��·����ͼƬ�����õ�ͼƬ����
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
 * ����ѹ������������ѹ������
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
 * ѹ���ֽ���ͼƬ�ĳ���ֵ��������ѹ�����ͼ��
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
 * ��ȡ������ͼƬ���ֽ�����ѹ��ͼƬ�ĳ����������������Bitmap
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
 * ��Bitmapѹ��������ͼƬ������
 * <br> ���ȵ����ʵ�����ѹ������ͼƬ���ٵ��ô˷���ѹ��ͼƬ����
 * @param bts
 * @param maxBytes ѹ�����ͼ������С ��λΪbyte
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
 * ѹ���Ѵ��ڵ�ͼƬ���󣬲�����ѹ�����ͼƬ,ʹ�ֽ���������256k��������󳤿�ֵ
 * �ֽ�����С������ʵ��ͼƬ�Ĵ�С���ֽ������ڴ���ļ�������ʽ
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
// * ѹ���Ѵ��ڵ�ͼƬ���󣬲�����ѹ�����ͼƬ,ע�⣡PNG����δ�Ż�
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
// * ����������ѹ���㷨�� �˷���δ ���ѹ����ͼ��ʧ������,ע�⣡PNG����δ�Ż�
// * <br> ���ȵ��ñ���ѹ���ʵ�ѹ��ͼƬ���ٵ��ô˷����ɽ����������
// * @param bts
// * @param maxBytes ѹ�����ͼ������С ��λΪbyte
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
 * ѹ����ԴͼƬ��������ͼƬ����
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
 * �õ�ָ��·��ͼƬ��options
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
