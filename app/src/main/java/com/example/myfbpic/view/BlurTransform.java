package com.example.myfbpic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

import com.squareup.picasso.Transformation;

/**
 * From https://github.com/Sefford/AndroidInMotion/blob/master/src/com/sefford/animations/ui/transformations/BlurTransformation.java
 * With changes to android.support.v8.renderscript library
 */

public class BlurTransform implements Transformation {

    private final RenderScript renderScript;

    public BlurTransform(Context context) {
        super();
        renderScript = RenderScript.create(context);
    }

    @Override
    public Bitmap transform(Bitmap originalBitmap) {
        Bitmap blurredBitmap = Bitmap.createBitmap(originalBitmap);

        Allocation input = Allocation.createFromBitmap(renderScript, originalBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
        Allocation output = Allocation.createTyped(renderScript, input.getType());

        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        script.setInput(input);
        script.setRadius(20);
        script.forEach(output);
        output.copyTo(blurredBitmap);
        return blurredBitmap;
    }

    @Override
    public String key() {
        return "blur";
    }

}