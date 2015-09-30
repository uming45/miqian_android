package com.miqian.mq.views;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author sunyong
 */

public class CustomTextView extends TextView {

  // Default constructor when inflating from XML file
  public CustomTextView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  // Default constructor override
  public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs);
    setIncludeFontPadding(false); //remove the font padding
    //setGravity(getGravity() | Gravity.TOP ); //make sure that the gravity is set to the top
    setGravity(getGravity() | Gravity.TOP | Gravity.LEFT); //make sure that the gravity is set to the top
  }

  /*This is where the magic happens*/
  @Override
  protected void onDraw(Canvas canvas){
    TextPaint textPaint = getPaint();
    textPaint.setColor(getCurrentTextColor());
    textPaint.drawableState = getDrawableState();
    canvas.save();

    //converts 5dip into pixels
    int additionalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
        getContext().getResources().getDisplayMetrics());

    //subtracts the additional padding from the top of the canvas that textview draws to in order to align it with the top.
    canvas.translate(0, -additionalPadding);
    if(getLayout() != null)
      getLayout().draw(canvas);
    canvas.restore();
  }
}