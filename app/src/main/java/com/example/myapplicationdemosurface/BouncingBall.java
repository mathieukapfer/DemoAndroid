package com.example.myapplicationdemosurface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class BouncingBall implements Runnable {

	private Thread thread = null;
	private SurfaceView surf;
	private SurfaceHolder surfaceHolder;
	private volatile boolean running = false;

	private int screen_width = 0;
	private int screen_height = 0;

	private int ball_radius = 20;
	private int ball_x = 50;
	private int ball_y = 0;
	private float ball_speed_x = 5;
	private float ball_speed_y = 5;
	private float gravity = 0.8f;
	private Paint paint;


	public BouncingBall(Context context, SurfaceView surf_) {
		//super(context);
		surf = surf_;
		surfaceHolder = surf.getHolder();

		paint = new Paint();
		paint.setColor(Color.BLACK);
	}

	@Override
	public void run() {

		while (running) {
			if (surfaceHolder.getSurface().isValid()) {

				// update size now to get the good one (when surface is drawn)
				screen_width = surf.getWidth();
				screen_height = surf.getHeight();

				// prepare the drawing surface
				Canvas canvas = surfaceHolder.lockCanvas();
				canvas.drawColor(Color.WHITE);

				// Apply gravity to the ball
				ball_speed_y += gravity;

				// Move the ball
				ball_x += (int) ball_speed_x;
				ball_y += (int) ball_speed_y;

				// Bounce the ball off the walls
				if (ball_x + ball_radius > screen_width) {
					ball_x = screen_width - ball_radius;
					ball_speed_x = -ball_speed_x;
				} else if (ball_x - ball_radius < 0) {
					ball_x = ball_radius;
					ball_speed_x = -ball_speed_x;
				}
				if (ball_y + ball_radius > screen_height) {
					ball_y = screen_height - ball_radius;
					ball_speed_y = -ball_speed_y;
				} else if (ball_y - ball_radius < 0) {
					ball_y = ball_radius;
					ball_speed_y = -ball_speed_y;
				}

				// Draw the ball
				canvas.drawCircle(ball_x, ball_y, ball_radius, paint);

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
