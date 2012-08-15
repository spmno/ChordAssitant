package com.seventeenweek;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GuitarView extends SurfaceView implements SurfaceHolder.Callback{
	//private static final int MAX_TOUCHPOINTS = 10;
	int width, height;
	private float scale = 1.0f;
	public GuitarView(Context context) {
		super(context);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		setFocusable(true);
		setFocusableInTouchMode(true);
		ChordGenerator.getInstance().setContext(context);
		ChordGenerator.getInstance().genChord();
	}
	
	public void drawChordText(Canvas canvas) {
		if (canvas != null) {
			int x = width - width/8;
			int y = height/8;
			String text = ChordGenerator.getInstance().getCurrentChordName();
			Paint chordPaint = new Paint();
			chordPaint.setColor(Color.RED);
			chordPaint.setTextSize(width/10);
			canvas.drawText(text, x, y, chordPaint);
		}
	}
	
	private void drawGuitar(Canvas canvas) {
		if (canvas != null) {
			canvas.drawColor(Color.BLACK);
			
			
			Paint chordPaint = new Paint();
			chordPaint.setStrokeWidth(5);
			chordPaint.setColor(Color.WHITE);
			
			Point[][] chordPoint = SeparatedPointContainer.getInstance().getSeparatedPoints();
			
			for (int i = 0; i < 6; ++i) {
				canvas.drawLine(chordPoint[i][0].x, chordPoint[i][0].y, chordPoint[i][1].x, chordPoint[i][1].y, chordPaint);
			}
			
			//draw vertical line 
			canvas.drawLine(width/3, chordPoint[0][0].y, width/3, chordPoint[5][0].y, chordPaint);
			canvas.drawLine(width*2/3, chordPoint[0][0].y, width*2/3, chordPoint[5][0].y, chordPaint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int pointerCount = event.getPointerCount();
		Point[] touchPoint = new Point[pointerCount];
		
		for (int i = 0; i < pointerCount; ++i) {
			touchPoint[i] = new Point();
		}
		Canvas canvas = getHolder().lockCanvas();
		Paint touchPaint = new Paint();
		touchPaint.setColor(Color.RED);
		if (canvas != null) {
			
			drawGuitar(canvas);
			drawChordText(canvas);
			if (event.getAction() == MotionEvent.ACTION_UP) {
				
			}else {
				for (int i = 0; i < pointerCount; ++i) {
					int x = touchPoint[i].x = (int) event.getX(i);
					int y = touchPoint[i].y = (int) event.getY(i);
					canvas.drawCircle(x, y, 40, touchPaint);
				}
			}
			getHolder().unlockCanvasAndPost(canvas);
		}
		
		ChordJudge judge = new ChordJudge();
		if (judge.judge(touchPoint)) {
			ChordGenerator.getInstance().genChord();
		}
		
		
		return true;
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		this.width = width;
		this.height = height;
		
		if (width > height) {
			this.scale = width / 480f;
			
		} else {
			this.scale = height / 480f;
		}
		
		Canvas canvas = getHolder().lockCanvas();
		if (canvas != null) {
			drawGuitar(canvas);
			drawChordText(canvas);
			getHolder().unlockCanvasAndPost(canvas);
		}
		
		
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
}
