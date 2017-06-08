package testsdcard.cai.maiyu.picassodemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by maiyu on 2017/6/7.
 * 封装Picasso库
 */

public class PicassoUtils {

    /**
     * 指定图片宽高进行加载
     * @param context
     * @param path
     * @param width
     * @param height
     * @param imageView
     */
    public static void loadImageWithSize(Context context , String path , int width ,
                                     int height , ImageView imageView){
        Picasso.with(context).load(path).resize(width , height).centerCrop().into(imageView);
    }

    /**
     * 根据resID来加载图片
     * @param context
     * @param path
     * @param resID
     * @param imageView
     */
    public static void loadImageWithHolder(Context context , String path , int resID , ImageView imageView){
        Picasso.with(context).load(path).placeholder(resID).into(imageView);
    }


    /**
     * 自定义裁剪类来加载图片
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadImageWithCrop(Context context , String path , ImageView imageView){
        Picasso.with(context).load(path).transform(new CropSquareTransformation()).into(imageView);
    }

    /**
     * 实现对图片的自定义裁剪
     */
    public static class CropSquareTransformation implements Transformation{

        @Override
        public Bitmap transform(Bitmap source) {
            //获取图片的宽度和高度中的最小值
            int size = Math.min(source.getWidth() , source.getHeight());
            int x = (source.getWidth() - size)/2;
            int y = (source.getHeight() - size)/2;

            Bitmap result = Bitmap.createBitmap(source , x ,y ,size , size);

            if(result != null){
                source.recycle();//进行回收
            }
            return result;  //返回图片

        }

        @Override
        public String key() {
            return "square()";  //自己取一个名字
        }
    }

}
