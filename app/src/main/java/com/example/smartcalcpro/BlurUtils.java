package com.example.smartcalcpro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;

public class BlurUtils {
    private static final float MAX_BLUR_RADIUS = 25f;
    private static final float SCALE_FACTOR = 0.1f;

    @RequiresApi(api = Build.VERSION_CODES.S)
    public static void applyBlurEffect(View view, float blurRadius) {
        if (blurRadius <= 0) {
            view.setRenderEffect(null);
            return;
        }

        // Create a bitmap from the view
        Bitmap bitmap = createBitmapFromView(view);
        if (bitmap == null) return;

        // Apply blur effect
        RenderEffect blurEffect = RenderEffect.createBlurEffect(
                blurRadius,
                blurRadius,
                Shader.TileMode.DECAL
        );
        view.setRenderEffect(blurEffect);
    }

    public static void applyLegacyBlurEffect(View view, float blurRadius) {
        if (blurRadius <= 0) {
            view.setBackground(null);
            return;
        }

        // Create a bitmap from the view
        Bitmap bitmap = createBitmapFromView(view);
        if (bitmap == null) return;

        // Apply blur using RenderScript
        Context context = view.getContext();
        Bitmap blurredBitmap = blurBitmap(context, bitmap, blurRadius);

        // Set the blurred bitmap as background
        Drawable drawable = new BitmapDrawable(context.getResources(), blurredBitmap);
        view.setBackground(drawable);
    }

    private static Bitmap createBitmapFromView(View view) {
        if (view.getWidth() <= 0 || view.getHeight() <= 0) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(
                view.getWidth(),
                view.getHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private static Bitmap blurBitmap(Context context, Bitmap bitmap, float radius) {
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap);
        RenderScript rs = RenderScript.create(context);

        Allocation input = Allocation.createFromBitmap(rs, bitmap);
        Allocation output = Allocation.createFromBitmap(rs, outputBitmap);

        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);
        script.setRadius(radius);
        script.forEach(output);

        output.copyTo(outputBitmap);
        rs.destroy();

        return outputBitmap;
    }

    public static void applyBlurToViewGroup(ViewGroup viewGroup, float blurRadius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            applyBlurEffect(viewGroup, blurRadius);
        } else {
            applyLegacyBlurEffect(viewGroup, blurRadius);
        }
    }
} 