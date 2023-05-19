package com.example.myapplicationdemosurface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.myapplicationdemosurface.BouncingBall;

import java.net.BindException;
import java.util.Vector;

class BouncingBallsView implements Runnable {

	private Thread thread = null;
	private SurfaceView surf;
	private SurfaceHolder surfaceHolder;
	private volatile boolean running = false;

	private Paint paint;
    private Vector<BouncingBall> bouncingBallArray;

	public BouncingBallsView(Context p_context, SurfaceView p_surf) {
		//super(context);

		surf = p_surf;
		surfaceHolder = p_surf.getHolder();

		paint = new Paint();
		paint.setColor(Color.BLACK);

        bouncingBallArray = new Vector<BouncingBall>();

		for (int index=0;index<2;index++) {
			bouncingBallArray.add(new BouncingBall());
		}
	}

	@Override
	public void run() {

		while (running) {
			if (surfaceHolder.getSurface().isValid()) {

				int sum =0;
				// prepare the drawing surface
				Canvas canvas = surfaceHolder.lockCanvas();
				canvas.drawColor(0xFFBBBBBB);

                // update ball
				for(BouncingBall b: bouncingBallArray) {
					sum += b.Move(surf.getWidth(), surf.getHeight(), canvas);
				}

				// no more ball in the air then add new ball
				if (bouncingBallArray.size() == 0 ||
						sum / bouncingBallArray.size() < 0.2 * surf.getHeight()) {
					bouncingBallArray.add(new BouncingBall());
				}

				if (bouncingBallArray.size() > 10) {
					bouncingBallArray.remove(0);
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
