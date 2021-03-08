package com.yun.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.yun.common.R;

import java.security.MessageDigest;

/**
 * Created by Lovelin on 2019/4/16
 * <p>
 * Describe:
 */
public class GlideUtils {
    /**
     * 加载网络图片
     *
     * @param mContext
     * @param path
     * @param imageview
     */
    public static void LoadImage(Context mContext, String path,
                                 ImageView imageview) {
//        Glide.with(mContext).load(path).centerCrop().placeholder(R.drawable.image_default)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);
    }

    /**
     * 加载带尺寸的图片
     *
     * @param mContext
     * @param path
     * @param Width
     * @param Height
     * @param imageview
     */
    public static void LoadImageWithSize(Context mContext, String path,
                                         int Width, int Height, ImageView imageview) {
//        Glide.with(mContext).load(path).override(Width, Height)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);
    }

    /**
     * 加载本地图片
     *
     * @param mContext
     * @param path
     * @param imageview
     */
    public static void LoadImageWithLocation(Context mContext, Integer path,
                                             ImageView imageview) {
//        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .into(imageview);
    }

    /**
     * 加载自定义圆形imageview 或者 加载圆形imageview
     *
     * @param mContext
     * @param path
     * @param imageview
     */
    public static void LogadCustomCircleImage(final Context mContext, String path, final ImageView imageview) {

//        Glide.with(mContext)
//                .load(path)
//                .asBitmap()
//                .centerCrop()
//                .error(R.drawable.icon_mine_pic)
//                .placeholder(R.drawable.icon_mine_pic)
//                .into(new BitmapImageViewTarget(imageview) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        super.setResource(resource);
//                        RoundedBitmapDrawable roundedBitmapDrawable =
//                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
//                        roundedBitmapDrawable.setCircular(true);
//                        imageview.setImageDrawable(roundedBitmapDrawable);
//                    }
//                });
//        RoundedBitmapDrawable roundedBitmapDrawable =
//                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
//        roundedBitmapDrawable.setCircular(true);
//        imageview.setImageDrawable(roundedBitmapDrawable);


//        RequestOptions options = RequestOptions.circleCropTransform();//圆形图片  好多的图片形式都是这么设置的
//        options.placeholder(R.drawable.icon_mine_pic);//占位图
//        options.error(R.drawable.icon_mine_pic);//占位图
//        Glide.with(mContext).load(path)
//                .apply(options).into(imageview);

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.icon_mine_pic)//图片加载出来前，显示的图片
                .fallback(R.drawable.icon_mine_pic) //url为空的时候,显示的图片
                .error(R.drawable.icon_mine_pic);//图片加载失败后，显示的图片

        Glide.with(mContext).load(path)
                .apply(options).into(imageview);


    }

    /**
     * 加载正常的imageview
     *
     * @param mContext
     * @param path
     * @param imageview
     */
    public static void LoadUserImageView(final Context mContext, String path, final ImageView imageview) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.icon_baiyang)//图片加载出来前，显示的图片
                .fallback(R.mipmap.icon_baiyang) //url为空的时候,显示的图片
                .error(R.mipmap.icon_baiyang);//图片加载失败后，显示的图片

        Glide.with(mContext).load(path)
                .apply(options).into(imageview);
//        Glide.with(mContext)
//                .load(path)
//                .into(imageview);
//        Glide.with(mContext).asBitmap().load(path).into(imageview);

    }

    public static void LogadUserCircleImage(final Context mContext, String path, final ImageView imageview) {


        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.icon_baiyang)//图片加载出来前，显示的图片
                .fallback(R.mipmap.icon_baiyang) //url为空的时候,显示的图片
                .error(R.mipmap.icon_baiyang);//图片加载失败后，显示的图片

        Glide.with(mContext).load(path)
                .apply(options).into(imageview);


    }


    /**
     * 加载正常的imageview
     *
     * @param mContext
     * @param path
     * @param imageview
     */
    public static void LoadUrlImageView(final Context mContext, String path, final ImageView imageview) {
//        Glide.with(mContext)
//                .load(path)
//                .error(R.drawable.icon_mine_pic)
//                .placeholder(R.drawable.icon_mine_pic)
//                .into(new SimpleTarget<GlideDrawable>() {
//                    @Override
//                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                        imageview.setImageDrawable(resource);
//                    }
//                });
//        GlideUtils.LogadCustomCircleImage(getActivity(), HttpConstants.Common + inforBean.getData().getHeadimg(), currentPic);
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.item_new_iv)//图片加载出来前，显示的图片
                .fallback(R.mipmap.item_new_iv) //url为空的时候,显示的图片
                .error(R.mipmap.item_new_iv);//图片加载失败后，显示的图片
//        RequestOptions options = new RequestOptions()
//                .placeholder(R.mipmap.item_new_iv)//图片加载出来前，显示的图片
//                .fallback(R.mipmap.item_new_iv) //url为空的时候,显示的图片
//                .error(R.mipmap.item_new_iv);//图片加载失败后，显示的图片

        Glide.with(mContext).load(path)
                .apply(options).into(imageview);


//        ImageLoader.with(this)
//                .load(data.getIcon())
//                .circle()
//                .into(mLogoView);

//        Glide.with(mContext)
//                .load(path)
//                .into(imageview);
//        Glide.with(mContext).asBitmap().load(path).into(imageview);

    }

    /**
     * 圆形加载
     *
     * @param mContext
     * @param path
     * @param
     */
    public static void LoadCircleImage(Context mContext, int path, ImageView imageview) {
//        Glide.with(mContext).load(path).centerCrop().placeholder(R.drawable.image_default)
//                .transform(new GlideCircleTransform(mContext, 2, mContext.getResources().getColor(R.color.color_ffffff)))
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageview);

    }

    static class GlideCircleTransform extends BitmapTransformation {

        private Paint mBorderPaint;
        private float mBorderWidth;

        public GlideCircleTransform(Context context) {
            super(context);
        }

        public GlideCircleTransform(Context context, int borderWidth, int borderColor) {
            super(context);
            mBorderWidth = Resources.getSystem().getDisplayMetrics().density * borderWidth;

            mBorderPaint = new Paint();
            mBorderPaint.setDither(true);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }


        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderWidth / 2));
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            if (mBorderPaint != null) {
                float borderRadius = r - mBorderWidth / 2;
                canvas.drawCircle(r, r, borderRadius, mBorderPaint);
            }
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }

}
