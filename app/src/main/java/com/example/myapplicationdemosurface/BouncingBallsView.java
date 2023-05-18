package com.example.myapplicationdemosurface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.myapplicationdemosurface.BouncingBall;

class BouncingBallsView implements Runnable {

	private Thread thread = null;
	private SurfaceView surf;
	private SurfaceHolder surfaceHolder;
	private volatile boolean running = false;

	private Paint paint;
    private BouncingBall bouncingBall;

	public BouncingBallsView(Context p_context, SurfaceView p_surf) {
		//super(context);

		surf = p_surf;
		surfaceHolder = p_surf.getHolder();

		paint = new Paint();
		paint.setColor(Color.BLACK);

        bouncingBall = new BouncingBall();
	}

	@Override
	public void run() {

		while (running) {
			if (surfaceHolder.getSurface().isValid()) {

				// prepare the drawing surface
				Canvas canvas = surfaceHolder.lockCanvas();
				canvas.drawColor(Color.WHITE);

                // update ball
                bouncingBall.Move(surf.getWidth(), surf.getHeight(), canvas);

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
