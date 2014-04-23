package com.randalltower605.lucky;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by eli on 3/12/14.
 */
public class SelectorView extends View {
    Paint paint = new Paint();
    public SelectorView(Context context) {
        super(context);
    }
    @Override
    public void onDraw(Canvas canvas)
    {
        paint.setColor(Color.WHITE);
        paint.setAlpha(30);
        canvas.drawRect(0, 0, 220f, 220f, paint);
    }
}
