package com.tonightstay.DDH.ScratchView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.AvoidXfermode.Mode;

public class HintField {
	
	Rect mPrizeRect = new Rect(0, 0, 0, 0);
	Rect mMsgBounds = new Rect(0, 0, 0, 0);
	Paint PrizeBG = new Paint();
	
	Paint WordPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
			| Paint.ANTI_ALIAS_FLAG);
	
	Paint LinePaint = new Paint();
	
	String mMsg="";
	
	float filledPercent = 0.7f;
	int mStrokeWidth = 1;
	
	int mTextX = 0;
	int mTextY = 0;
	
	public HintField()
	{
		PrizeBG.setColor(Color.argb(200, 0, 0, 0));
		WordPaint.setColor(Color.WHITE);
		
		LinePaint.setColor(Color.BLACK);
		LinePaint.setStyle(Style.STROKE);
		LinePaint.setStrokeWidth(mStrokeWidth);
//		 PathEffect effects = new DashPathEffect(
//                 new float[]{mStrokeWidth,mStrokeWidth}, 1);
//		 LinePaint.setPathEffect(effects);
		
	}
	
	public void setStrokeWidth(int width)
	{
		mStrokeWidth = width;
//		LinePaint.setStrokeWidth(mStrokeWidth);
//		 PathEffect effects = new DashPathEffect(
//                 new float[]{mStrokeWidth,mStrokeWidth}, 1);
//		 LinePaint.setPathEffect(effects);
	}

	public void setRect(Rect rect)
	{
		if (rect!=null)
		{
			mPrizeRect = rect;
			setTextPosition();
		}
		
	}
	
	void setTextPosition()
	{
		mTextX = mPrizeRect.centerX() - mMsgBounds.width()/2;
		mTextY = mPrizeRect.centerY() + mMsgBounds.height()/2;
	}
	
	public int getX()
	{
		return mPrizeRect.left;
	}
	
	public int getY()
	{
		return mPrizeRect.top;
	}
	
	public int getWidth()
	{
		return mPrizeRect.width();
	}
	
	public int getHeight()
	{
		return mPrizeRect.height();
	}
	
	public int testTextSize()
	{
		int textSize = (int) (mPrizeRect.height()*filledPercent);
		WordPaint.setTextSize(textSize);
		WordPaint.getTextBounds(mMsg, 0, mMsg.length(), mMsgBounds);
		
		int toFitWidth = (int) (mPrizeRect.width()*filledPercent);
		if (mMsgBounds.width()>toFitWidth)
		{
			textSize = (int) (textSize*((float) toFitWidth/(float)mMsgBounds.width()));
			WordPaint.setTextSize(textSize);
			WordPaint.getTextBounds(mMsg, 0, mMsg.length(), mMsgBounds);
		}
		return textSize;
	}
	
	public void setTextSize(int textSize)
	{
		WordPaint.setTextSize(textSize);
		WordPaint.getTextBounds(mMsg, 0, mMsg.length(), mMsgBounds);
		
		setTextPosition();
	}
	
	public void setMessage(String msg)
	{
		mMsg = msg;
		WordPaint.getTextBounds(mMsg, 0, mMsg.length(), mMsgBounds);
		
		setTextPosition();
	}
	
	public void onDrawPrize(Canvas canvas)
	{
		canvas.drawRect(mPrizeRect, PrizeBG);
		canvas.drawText(mMsg, mTextX, mTextY, WordPaint);
	}

	public void onDrawLine(Canvas canvas)
	{
		//canvas.drawRoundRect(new RectF(mPrizeRect),5,5, LinePaint);
	}

}
