package com.tonightstay.DDH.ScratchView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Paint.Style;
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
	
	float filledPercent = 0.8f;
	float paddingTopBottomPercent = 0.0f;
	float paddingLeftRightPercent = 0.0f;
	
	
	String[] mMsg=new String[prizeMsgCount];
	Rect[]  mMsgBounds= new Rect[prizeMsgCount];
	
	public PrizeField9x9()
	{
		PrizeBG.setColor(Color.WHITE);
		WordPaint.setColor(Color.BLACK);
		
		LinePaint.setColor(Color.WHITE);
		LinePaint.setStyle(Style.STROKE);
		 PathEffect effects = new DashPathEffect(
                 new float[]{5,5,5,5}, 1);
		 LinePaint.setPathEffect(effects);
		 
		for (int i=0;i<prizeMsgCount;++i)
			mMsgBounds[i] = new Rect(0,0,0,0);
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
				
				canvas.drawText(msg,mPrizeRect.left+ (x+1)*paddingLR+prizeWidth*x+prizeWidth/2 - msgBounds.width()
					/ 2,mPrizeRect.top+ (y+1)*paddingTB+prizeHeight*y+prizeHeight/2 + msgBounds.height() / 2, WordPaint);
				
				msgIdx++;
			}
		}
	}
	
	public void onDrawLine(Canvas canvas)
	{
		canvas.drawRect(mPrizeRect, LinePaint);
	}


}
