package com.cfbb.android.commom.utils.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.cfbb.android.R;
import com.cfbb.android.commom.utils.image.GlideCircleTransform;

import java.io.File;

/**
 * @author MrChang
 *         created  at  2016/3/1.
 * @description 对Glide库的二次封装
 */
public class ImageWithGlideUtils {

    public static void lodeFromUrl(String url, ImageView tartgetView, Context context) {
        try {
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(tartgetView);
        } catch (Exception e) {
        }

    }

    public static void lodeFromUrl(String url, ImageView tartgetView, Context context, RequestListener requestListener) {
        try {
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(requestListener)
                    .into(tartgetView);
        } catch (Exception e) {
        }
    }

    public static void lodeFromUrl(String url, int placeholder, ImageView tartgetView, Context context) {
        try {
            Glide.with(context)
                    .load(url)
                    .placeholder(placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(tartgetView);
        } catch (Exception e) {
        }
    }

    public static void lodeFromUrl(String url, int placeholder, int erroholder, ImageView tartgetView, Context context) {
        try {
            Glide.with(context)
                    .load(url)
                    .placeholder(placeholder)
                    .error(erroholder)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(tartgetView);
        } catch (Exception e) {
        }
    }

    public static void lodeFromFile(String filePath, ImageView tartgetView, Context context) {
        try {
            File file = new File(filePath);
            if (file != null && file.isFile()) {
                Glide.with(context)
                        .load(file)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(tartgetView);
            }
            file = null;
        } catch (Exception e) {
        }
    }

    public static void lodeFromRes(Integer filePath, ImageView tartgetView, Context context) {
        try {
            Glide.with(context)
                    .load(filePath)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .crossFade()
                    .into(tartgetView);
        } catch (Exception e) {
        }
    }

    public static void lodeFromFile(String filePath, int placeholder, ImageView tartgetView, Context context) {
        try {
            File file = new File(filePath);
            if (file != null && file.isFile()) {
                Glide.with(context)
                        .load(file)
                        .placeholder(placeholder)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(tartgetView);
            }
            file = null;
        } catch (Exception e) {
        }
    }

    public static void lodeFromFile(String filePath, int placeholder, int erroholder, ImageView tartgetView, Context context) {
        try {
            File file = new File(filePath);
            if (file != null && file.isFile()) {
                Glide.with(context)
                        .load(file)
                        .placeholder(placeholder)
                        .error(erroholder)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(tartgetView);
            }
            file = null;
        } catch (Exception e) {
        }
    }

    public static void lodeFromUrlRoundTransform(String url, ImageView tartgetView, Context context) {
        try {
            Glide.with(context)
                    .load(url)
                    .transform(new GlideCircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(tartgetView);
        } catch (Exception e) {
        }
    }
    public static void lodeFromUrlRoundTransformImg(String url, ImageView tartgetView, Context context){
        try {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .override(200,120)
                    .placeholder(R.drawable.sfz)
                    .into(tartgetView);
        } catch (Exception e) {
        }
    }
    public static void lodeFromUrlRoundTransform(String url, int placeholder, ImageView tartgetView, Context context) {
        try {
            Glide.with(context)
                    .load(url)
                    .placeholder(placeholder)
                    .transform(new GlideCircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(tartgetView);
        } catch (Exception e) {
        }
    }

}
