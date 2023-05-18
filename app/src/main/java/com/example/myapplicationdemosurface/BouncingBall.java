package com.example.myapplicationdemosurface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class BouncingBall {

    private Paint paint;

 	private int screen_width = 0;
	private int screen_height = 0;

	private int ball_radius = 20;
	private int ball_x = 50;
	private int ball_y = 0;
	private float ball_speed_x = 5;
	private float ball_speed_y = 5;
	private float gravity = 0.8f;

    Canvas canvas;

    public BouncingBall() {
        paint = new Paint();
		paint.setColor(Color.BLACK);
    }

    public void Move(int p_screen_width, int p_screen_height, Canvas p_canvas) {

        // update sceen size & canvas
        screen_width = p_screen_width;
        screen_height = p_screen_height;
        canvas = p_canvas;

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

    }
}
