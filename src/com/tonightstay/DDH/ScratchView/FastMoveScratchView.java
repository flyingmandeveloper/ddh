package com.tonightstay.DDH.ScratchView;

import com.example.com.tonightstay.ddh.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FastMoveScratchView extends  SurfaceView implements SurfaceHolder.Callback {

	Bitmap mStartBitmap1;
	Bitmap mStartBitmap2;
	Bitmap mBackgroundBitmap;
	Paint mBgPaint = new Paint();
	FastMoveScratchViewThread mThread;
	double mOffY = 0;
	double mSpeed = 50;
	
	final double  mSlowToFastSpeedDefault = 0.1;
	double mMillsDiffTime = 0;
	double mSlowToFastOffY1 = 0;
	double mSlowToFastOffY2 = 0;
	double mSlowToFastSpeed = mSlowToFastSpeedDefault;
	double mSlowToFastAcceleration = 0.005;
	double mSlowDiffDistance = 0;
	
	final double  mFastToSlowSpeedDefault = 1.3;
	double mFastToSlowOffY = 0;
	double mFastToSlowDiffDistance = 0;
	double mFastToSlowDuring = 0;
	double mFastToSlowAcceleration = 0;
	double mFastToSlowNow = 0;
	
	public interface OnFastMoveScratchListener
	{
		public void OnPickScratchFinish();
	};
	
	OnFastMoveScratchListener mOnFastMoveScratchListener;
	
	public void setOnFastMoveScratchListener(OnFastMoveScratchListener listener)
	{
		mOnFastMoveScratchListener = listener;
	}
	
	private Handler FinishPickScratch =  new Handler()
	{
		@Override
		 public void handleMessage(Message msg) 
		{
		  super.handleMessage(msg);
		  String MsgString = (String)msg.obj;
		  if (MsgString.equals("Finish"))
		  {
			 if (mOnFastMoveScratchListener!=null)
				 mOnFastMoveScratchListener.OnPickScratchFinish();
		  } 
		}
	};
	
	public FastMoveScratchView(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		init(ctx, attrs);
	}

	public FastMoveScratchView(Context context) {
		super(context);
		init(context, null);
	}
	
	private void init(Context context, AttributeSet attrs) 
	{
		setZOrderOnTop(true);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		holder.setFormat(PixelFormat.TRANSPARENT);
		
		mBackgroundBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.fastmove_scratch_bg);
		
		mStartBitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.scratch_default_1);
		mStartBitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.scratch_default_2);
		
		mBgPaint.setAntiAlias(true);
		
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		if (mThread == null)
			return false;
		
			
		switch(me.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				switch(mThread.aniState)
				{
				case VeryFast:
					mFastToSlowDuring = getWidth()*2/mFastToSlowSpeedDefault;
					mFastToSlowAcceleration = - mFastToSlowSpeedDefault/mFastToSlowDuring;
					mThread.aniState = eAnimState.FastToSlow;
					break;
				}
				break;
		}
		
		return false;
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mThread = new FastMoveScratchViewThread(getHolder(), this);
		mThread.setRunning(true);
		mThread.start();
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
	
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		mThread.setRunning(false);
		while (retry) {
			try {
				mThread.join();
				retry = false;
			} catch (InterruptedException e) {
				// do nothing but keep retry
			}
		}
		
	}
	
	public void onSlowToFastDraw(Canvas canvas,long offsetTime) {

		mMillsDiffTime = ((double)offsetTime/1000000);
		int wholeWidth = getWidth();
		int leftImageX1 = wholeWidth;
		int leftImageX2 = wholeWidth+wholeWidth;
		
		
		mSlowToFastSpeed += mMillsDiffTime*mSlowToFastAcceleration;
		mSlowDiffDistance = mMillsDiffTime*mSlowToFastSpeed;
		
		mSlowToFastOffY1-=mSlowDiffDistance;
		mSlowToFastOffY2-=mSlowDiffDistance;
				
		canvas.drawBitmap(
				mStartBitmap1, 
				new Rect(0,0,mStartBitmap1.getWidth(),mStartBitmap1.getHeight()),
				new Rect((int)mSlowToFastOffY1,0,(int)mSlowToFastOffY1+wholeWidth,getHeight()),
				mBgPaint);

		canvas.drawBitmap(
				mStartBitmap2, 
				new Rect(0,0,mStartBitmap2.getWidth(),mStartBitmap2.getHeight()),
				new Rect((int)mSlowToFastOffY2+leftImageX1,0,(int)mSlowToFastOffY2+leftImageX2,getHeight()),
				mBgPaint);
		
		if (mSlowToFastOffY1<=-leftImageX1)
		{
			mSlowToFastOffY1 = leftImageX2 + mSlowToFastOffY1;
			mThread.aniState = eAnimState.VeryFast;
		}
		
		if (mSlowToFastOffY2<=-leftImageX2)
		{
			mSlowToFastOffY2 = leftImageX2 + mSlowToFastOffY2;
			
		}

	}
	
	public void onSDraw(Canvas canvas,long offsetTime) {

		mMillsDiffTime = ((double)offsetTime/1000000);
		int wholeWidth = getWidth()*3;
		
		canvas.drawBitmap(
				mBackgroundBitmap, 
				new Rect(0,0,mBackgroundBitmap.getWidth(),mBackgroundBitmap.getHeight()),
				new Rect((int)mOffY,0,(int)mOffY+wholeWidth,getHeight()),
				mBgPaint);
		
		canvas.drawBitmap(
				mBackgroundBitmap, 
				new Rect(0,0,mBackgroundBitmap.getWidth(),mBackgroundBitmap.getHeight()),
				new Rect((int)mOffY+wholeWidth,0,(int)mOffY+wholeWidth+wholeWidth,getHeight()),
				mBgPaint);
		
	
		
		mOffY-=mMillsDiffTime*mSpeed;

		
		if (mOffY<=-wholeWidth)
			mOffY = mOffY%(-wholeWidth);
	}
	
	public void onFastToSlowDraw(Canvas canvas,long offsetTime) {

		mMillsDiffTime = ((double)offsetTime/1000000);
		int wholeWidth = getWidth();
		int wholeWidthX2 = wholeWidth+wholeWidth;
		
		mFastToSlowNow +=mMillsDiffTime;

		if (mFastToSlowNow<=mFastToSlowDuring)
			mFastToSlowOffY = -(mFastToSlowSpeedDefault*mFastToSlowNow+(mFastToSlowAcceleration*(mFastToSlowNow*mFastToSlowNow))/2);
		else
		{
			mFastToSlowOffY = -wholeWidth;
			if (mThread.finishPick == false)
			{
				mThread.finishPick = true;
				Message message = FinishPickScratch.obtainMessage(1,"Finish");
				FinishPickScratch.sendMessage(message);
			}
		}
		
				
		canvas.drawBitmap(
				mStartBitmap1, 
				new Rect(0,0,mStartBitmap1.getWidth(),mStartBitmap1.getHeight()),
				new Rect((int)mFastToSlowOffY,0,(int)mFastToSlowOffY+wholeWidth,getHeight()),
				mBgPaint);

		canvas.drawBitmap(
				mStartBitmap2, 
				new Rect(0,0,mStartBitmap2.getWidth(),mStartBitmap2.getHeight()),
				new Rect((int)mFastToSlowOffY+wholeWidth,0,(int)mFastToSlowOffY+wholeWidthX2,getHeight()),
				mBgPaint);
		
		
	}

	public enum eAnimState
	{
		SlowToFast,
		VeryFast,
		FastToSlow
	}
	
	class FastMoveScratchViewThread extends Thread {
		private SurfaceHolder mSurfaceHolder;
		private FastMoveScratchView mView;
		private boolean mRun = false;
		boolean finishPick = false;
		
		eAnimState aniState = eAnimState.SlowToFast;

		public FastMoveScratchViewThread(SurfaceHolder surfaceHolder, FastMoveScratchView view) {
			mSurfaceHolder = surfaceHolder;
			mView = view;
		}

		public void setRunning(boolean run) {
			mRun = run;
		}

		public SurfaceHolder getSurfaceHolder() {
			return mSurfaceHolder;
		}
		

		@Override
		public void run() 
		{
			long nowTime = System.nanoTime();
			long startTm = 0L;
			long sleepTime = 0L;
			long drawTime = 0L;
			
			while (mRun) 
			{
				Canvas c = null;
				try 
				{
					startTm = System.nanoTime();
					
					c = mSurfaceHolder.lockCanvas();
		
					if (c != null)
					{
						switch(aniState)
						{
							case SlowToFast:
								mView.onSlowToFastDraw(c,System.nanoTime() - nowTime);
							break;
							case VeryFast:
								mView.onSDraw(c,System.nanoTime() - nowTime);
							break;
							case FastToSlow:
								mView.onFastToSlowDraw(c,System.nanoTime() - nowTime);
							break;
							
						}
					}
					nowTime = System.nanoTime();
					drawTime = nowTime - startTm;
					startTm = nowTime;
	
				} finally {
					if (c != null)
						mSurfaceHolder.unlockCanvasAndPost(c);
				}
				
				
				sleepTime = 33 -(drawTime/1000000L);
				if (sleepTime<0L)
					sleepTime = 0L;
				
				
				try {

					this.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
