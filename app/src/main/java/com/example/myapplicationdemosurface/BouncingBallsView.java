package com.example.myapplicationdemosurface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;
import java.util.concurrent.Semaphore;


class BouncingBallsView implements Runnable {

	private Thread thread = null;
	private SurfaceView surf;
	private SurfaceHolder surfaceHolder;
	private volatile boolean running = false;

	private Paint paint;
    private Vector<BouncingBall> bouncingBallArray;
	private Semaphore semaphore = new Semaphore(1, true);

	public BouncingBallsView(Context p_context, SurfaceView p_surf) {
		//super(context);

		surf = p_surf;
		surfaceHolder = p_surf.getHolder();

		paint = new Paint();
		paint.setColor(Color.BLACK);

        bouncingBallArray = new Vector<BouncingBall>();
		bouncingBallArray.add(new BouncingBall());

		// add on touch event handler to the bouncing ball surface
		View.OnTouchListener listener = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				try {
					semaphore.acquire();
					if (motionEvent.getX() < surf.getWidth() / 2) {
						// touch left part of the screen
						// => add a new ball
						bouncingBallArray.add(new BouncingBall());
					} else {
						// touch right part of the screen
						// => remove the first one
						if (bouncingBallArray.size() > 0) {
							bouncingBallArray.remove(0);
						}
					}
					semaphore.release();
				} catch (Exception e) {}
				return false;
			}
		};
		surf.setOnTouchListener(listener);


	}

	@Override
	public void run() {

		while (running) {
			if (surfaceHolder.getSurface().isValid()) {

				int sum =0;
				// prepare the drawing surface
				Canvas canvas = surfaceHolder.lockCanvas();
				canvas.drawColor(Color.BLACK);

                // update ball
				try {
					semaphore.acquire();
					for (BouncingBall b : bouncingBallArray) {
						b.Move(surf.getWidth(), surf.getHeight(), canvas);
					}
					semaphore.release();
				} catch(Exception e) {
				}


				// Unlock the canvas and update the screen
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}

	public void resume() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void pause() {
		running = false;
		while (true) {
			try {
				thread.join();
				break;
			} catch (InterruptedException e) {
				// retry
			}
		}
	}
}
