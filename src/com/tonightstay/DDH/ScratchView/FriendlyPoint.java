package com.tonightstay.DDH.ScratchView;

import android.graphics.Canvas;
import android.graphics.Paint;

public class FriendlyPoint extends Point {
	
	private final Point neighbour;
	
	public FriendlyPoint(final float x, final float y, final Point neighbour, final int width) {
		super(x, y, width);
		this.neighbour = neighbour;
	}
	
	@Override
	public void draw(final Canvas canvas, final Paint paint) {
		canvas.drawLine(x, y, neighbour.x, neighbour.y, paint);
		canvas.drawCircle(x, y, width, paint);
	}


}
