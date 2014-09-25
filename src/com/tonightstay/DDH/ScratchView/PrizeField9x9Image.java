package com.tonightstay.DDH.ScratchView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.Log;

public class PrizeField9x9Image {
	
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
	
	

	Bitmap[] mResBitmap=new Bitmap[prizeMsgCount];
	
	int mStrokeWidth = 1;
	
	int mImageSize = 0;
	
	public PrizeField9x9Image()
	{
		PrizeBG.setColor(Color.argb(200, 255, 255, 255));
		WordPaint.setColor(Color.BLACK);
		
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
		LinePaint.setStrokeWidth(mStrokeWidth);
//		 PathEffect effects = new DashPathEffect(
//                 new float[]{mStrokeWidth,mStrokeWidth}, 1);
//		 LinePaint.setPathEffect(effects);
	}

	public void setRect(Rect rect,int imgSize)
	{
		if (rect!=null)
		{
			mImageSize = imgSize;
			
			mPrizeRect = rect;
			
			paddingTB = (int) (mPrizeRect.height()*paddingTopBottomPercent);
			prizeHeight =(int)((mPrizeRect.height() - (prizeRowCount+1)*paddingTB)/prizeRowCount);
			paddingLR = (int) (mPrizeRect.width()*paddingLeftRightPercent);
			prizeWidth =(int)((mPrizeRect.width() - (prizeColCount+1)*paddingLR)/prizeRowCount);
			setImagePosition(imgSize);
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
	
	public void setMessage(Bitmap [] arResBitmaps )
	{
		if (arResBitmaps!=null && arResBitmaps.length == prizeMsgCount)
		{
			mResBitmap = arResBitmaps;
		}
	}
	
	int[] mTextX=new int[prizeMsgCount];
	int[] mTextY=new int[prizeMsgCount];
	
	
	
	void setImagePosition(int imgSize)
	{
		int msgIdx =0;
		
		for (int y=0;y<prizeRowCount;++y)
		{
			for (int x=0;x<prizeColCount;++x)
			{
				mTextX[msgIdx] = mPrizeRect.left+ (x+1)*paddingLR+prizeWidth*x+prizeWidth/2 - imgSize/ 2;
				mTextY[msgIdx] = mPrizeRect.top+ (y+1)*paddingTB+prizeHeight*y+prizeHeight/2 - imgSize / 2;
				msgIdx++;
			}
		}
	}
	
	public void onDrawPrize(Canvas canvas)
	{
		canvas.drawRect(mPrizeRect, PrizeBG);

		int msgIdx =0;
		int imgX = 0;
		int imgY = 0;
		Bitmap img=null;
		for (int y=0;y<prizeRowCount;++y)
		{
			for (int x=0;x<prizeColCount;++x)
			{
				img = mResBitmap[msgIdx];
				
				imgX = mTextX[msgIdx];
				imgY = mTextY[msgIdx];
				if (img!=null && img.isRecycled() == false)
				    canvas.drawBitmap(img, new Rect(0, 0, mImageSize, mImageSize), new Rect(imgX, imgY,imgX+ mImageSize, imgY+mImageSize), WordPaint);
				msgIdx++;
			}
		}
	}
	
	public void onDrawLine(Canvas canvas)
	{
		//canvas.drawRoundRect(new RectF(mPrizeRect),5,5, LinePaint);
	}


}
