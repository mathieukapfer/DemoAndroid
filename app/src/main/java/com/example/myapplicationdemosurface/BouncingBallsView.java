package com.example.myapplicationdemosurface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Vector;
import java.util.concurrent.Semaphore;


class BouncingBallsView implements Runnable {

	private Thread mThread = null;
	private SurfaceView mSurf;
	private SurfaceHolder mSurfaceHolder;
	private volatile boolean running = false;

	private Paint mPaint;
    private Vector<BouncingBall> mBouncingBallArray;
	private Semaphore mSemaphore = new Semaphore(1, true);

	public BouncingBallsView(Context context, SurfaceView surf) {
		//super(context);

		mSurf = surf;
		mSurfaceHolder = surf.getHolder();

		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);

		mBouncingBallArray = new Vector<BouncingBall>();
		mBouncingBallArray.add(new BouncingBall());

		// add on touch event handler to the bouncing ball surface
		View.OnTouchListener listener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				try {
					mSemaphore.acquire();
					if (motionEvent.getX() < mSurf.getWidth() / 2) {
						// touch left part of the screen
						// => add a new ball
						mBouncingBallArray.add(new BouncingBall());
					} else {
						// touch right part of the screen
						// => remove the first one
						if (mBouncingBallArray.size() > 0) {
							mBouncingBallArray.remove(0);
						}
					}
					mSemaphore.release();
				} catch (Exception e) {
					// ingore all
				}
				return false;
			}
		};

		mSurf.setOnTouchListener(listener);


	}

	@Override
	public void run() {

		while (running) {
			if (mSurfaceHolder.getSurface().isValid()) {

				int sum =0;
				// prepare the drawing surface
				Canvas canvas = mSurfaceHolder.lockCanvas();
				canvas.drawColor(Color.BLACK);

                // update ball
				try {
					mSemaphore.acquire();
					for (BouncingBall b : mBouncingBallArray) {
						b.Move(mSurf.getWidth(), mSurf.getHeight(), canvas);
					}
					mSemaphore.release();
				} catch(Exception e) {
				}


				// Unlock the canvas and update the screen
				mSurfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

	public void resume() {
		running = true;
		mThread = new Thread(this);
		mThread.start();
	}

	public void pause() {
		running = false;
		while (true) {
			try {
				mThread.join();
				break;
			} catch (InterruptedException e) {
				// retry
			}
		}
	}
}
