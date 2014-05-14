package com.tonightstay.DDH.ScratchView;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Point {
	public final float x, y;
	public final int width;
	
	public Point(final float x, final float y, final int width) {
		this.x = x;
		this.y = y;
		this.width = width;
	}
	
	public void draw(final Canvas canvas, final Paint paint) {
		//canvas.drawCircle(x, y, width/2, paint);
	}

}
