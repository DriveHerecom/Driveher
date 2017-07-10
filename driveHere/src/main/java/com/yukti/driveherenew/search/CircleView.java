package com.yukti.driveherenew.search;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.yukti.driveherenew.R;

public class CircleView extends View{

	int colorId;
	Paint paint;

	public CircleView(Context context) {
		super(context);
		setFocusable(true);
		   colorId = getResources().getColor(R.color.black);
		   paint = new Paint();
	}
	
	  public CircleView(Context context, AttributeSet attrs) 
      { 
          super(context, attrs); 
          colorId = getResources().getColor(R.color.black);
		  paint = new Paint();
      } 

      public CircleView(Context context, AttributeSet attrs, int defStyle) 
      { 
          super(context, attrs, defStyle); 
          colorId = getResources().getColor(R.color.black);
  	      paint = new Paint();

      }

      public CircleView(Context context, String hex)
      {
          super(context);
          colorId = getColor(hex);
          paint = new Paint();

      }

	@Override
	protected void onDraw(Canvas canvas) {

		
		int w = getWidth();
        int h = getHeight();

        int pl = getPaddingLeft();
        int pr = getPaddingRight();
        int pt = getPaddingTop();
        int pb = getPaddingBottom();

        int usableWidth = w - (pl + pr);
        int usableHeight = h - (pt + pb);

        int radius = Math.min(usableWidth, usableHeight) / 2;
        int cx = pl + (usableWidth / 2);
        int cy = pt + (usableHeight / 2);

     
        paint.setAntiAlias(true);
        paint.setColor(colorId);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4.5f);
    
        canvas.drawCircle(cx, cy, radius, paint);
	}
	
	public void drawAgain(String hexColor){
		colorId = Color.parseColor(hexColor);
		invalidate();
	}

    public int getColor(String hexColor){
        int cid = Color.parseColor(hexColor);
        return cid;
    }
}
