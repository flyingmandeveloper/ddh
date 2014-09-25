package com.tonightstay.DDH.ScratchView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.Log;

public class PrizeField9x9 {
	
	Rect mPrizeRect = new Rect(0, 0, 0, 0);
	
	Paint PrizeBG = new Paint();
	Paint LinePaint = new Paint();
	
	Paint WordPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
			| Paint.ANTI_ALIAS_FLAG);
	
	final int prizeMsgCount = 9;
	final int prizeRowCount = 3;
	final int prizeColCount = prizeMsgCount/prizeRowCount;
	
	int paddingTB = 0;
	int prizeHeight = 0;
	int paddingLR = 0;
	int prizeWidth = 0;
	
	float filledPercent = 0.7f;
	float paddingTopBottomPercent = 0.0f;
	float paddingLeftRightPercent = 0.0f;
	
	
	String[] mMsg=new String[prizeMsgCount];
	Rect[]  mMsgBounds= new Rect[prizeMsgCount];
	
	int mStrokeWidth = 1;
	
	public PrizeField9x9()
	{
		PrizeBG.setColor(Color.argb(200, 255, 255, 255));
		WordPaint.setColor(Color.BLACK);
		
		LinePaint.setColor(Color.BLACK);
		LinePaint.setStyle(Style.STROKE);
		LinePaint.setStrokeWidth(mStrokeWidth);
		
		 PathEffect effects = new DashPathEffect(
                 new float[]{mStrokeWidth,mStrokeWidth}, 1);
		 LinePaint.setPathEffect(effects);
		 
		for (int i=0;i<prizeMsgCount;++i)
			mMsgBounds[i] = new Rect(0,0,0,0);
	}
	
	public void setStrokeWidth(int width)
	{
		mStrokeWidth = width;
		LinePaint.setStrokeWidth(mStrokeWidth);
		 PathEffect effects = new DashPathEffect(
                 new float[]{mStrokeWidth,mStrokeWidth}, 1);
		 LinePaint.setPathEffect(effects);
	}

	public void setRect(Rect rect)
	{
		if (rect!=null)
		{
			mPrizeRect = rect;
			
			paddingTB = (int) (mPrizeRect.height()*paddingTopBottomPercent);
			prizeHeight =(int)((mPrizeRect.height() - (prizeRowCount+1)*paddingTB)/prizeRowCount);
			paddingLR = (int) (mPrizeRect.width()*paddingLeftRightPercent);
			prizeWidth =(int)((mPrizeRect.width() - (prizeColCount+1)*paddingLR)/prizeRowCount);
			setTextPosition();
		}
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
		if (mMsg == null)
			return 0;
		int length = mMsg.length;
		
		
		int retTextSize = Integer.MAX_VALUE;
		int defaultTextSize = (int) (prizeHeight*filledPercent);
		int textSize = 0;
		
		String msg = null;
		Rect testWordsBounds = null;
		for (int i=0;i<prizeMsgCount;++i)
		{
			if (i<length)
			{
				textSize = defaultTextSize;
				testWordsBounds = mMsgBounds[i];
				
				WordPaint.setTextSize(textSize);
				
				msg = mMsg[i];
				if (msg!=null)
					WordPaint.getTextBounds(msg, 0, msg.length(), testWordsBounds);
				
				 
				
				int toFitWidth = (int) (prizeWidth*filledPercent);
				if (testWordsBounds.width()>toFitWidth)
				{
					textSize = (int) (textSize*((float) toFitWidth/(float)testWordsBounds.width()));
				}
				
				retTextSize=Math.min(retTextSize, textSize);
				
			}
		}
		
		
		return retTextSize;
	}
	
	public void setTextSize(int textSize)
	{
		
		WordPaint.setTextSize(textSize);
		
		if (mMsg == null)
			return;
		
		String msg = null;
		Rect testWordsBounds = null;
		
		for (int i=0;i<prizeMsgCount;++i)
		{
			msg = mMsg[i];
			testWordsBounds = mMsgBounds[i];
			if (msg!=null) 
				WordPaint.getTextBounds(msg, 0, msg.length(), testWordsBounds);
		}
		
		setTextPosition();
		
	}

	public void setMessage(String [] arMsg)
	{
		if (arMsg!=null && arMsg.length == prizeMsgCount)
		{
			mMsg = arMsg;
			
			String msg = null;
			Rect testWordsBounds = null;
			
			for (int i=0;i<prizeMsgCount;++i)
			{
				msg = mMsg[i];
				testWordsBounds = mMsgBounds[i];
				if (msg!=null) 
					WordPaint.getTextBounds(msg, 0, msg.length(), testWordsBounds);
			}
			
			setTextPosition();
		}
	}
	
	int[] mTextX=new int[prizeMsgCount];
	int[] mTextY=new int[prizeMsgCount];
	
	void setTextPosition()
	{
		Rect msgBounds = null;
		int msgIdx =0;
		
		for (int y=0;y<prizeRowCount;++y)
		{
			for (int x=0;x<prizeColCount;++x)
			{
				msgBounds = mMsgBounds[msgIdx];
				mTextX[msgIdx] = mPrizeRect.left+ (x+1)*paddingLR+prizeWidth*x+prizeWidth/2 - msgBounds.width()/ 2;
				mTextY[msgIdx] = mPrizeRect.top+ (y+1)*paddingTB+prizeHeight*y+prizeHeight/2 + msgBounds.height() / 2;
				msgIdx++;
			}
		}
	}
	
	public void onDrawPrize(Canvas canvas)
	{
		canvas.drawRect(mPrizeRect, PrizeBG);

		int msgIdx =0;
		String msg=null;
		Rect msgBounds = null;
		for (int y=0;y<prizeRowCount;++y)
		{
			for (int x=0;x<prizeColCount;++x)
			{
				msg = mMsg[msgIdx];
				msgBounds = mMsgBounds[msgIdx];
				
				canvas.drawText(msg,mTextX[msgIdx],mTextY[msgIdx], WordPaint);
				
				msgIdx++;
			}
		}
	}
	
	public void onDrawLine(Canvas canvas)
	{
		canvas.drawRoundRect(new RectF(mPrizeRect),5,5, LinePaint);
	}


}
