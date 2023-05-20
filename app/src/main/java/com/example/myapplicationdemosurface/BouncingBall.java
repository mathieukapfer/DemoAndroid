package com.example.myapplicationdemosurface;

import static java.lang.Math.abs;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class BouncingBall {

    private Paint paint;
    int randomColor = Color.BLACK;
    boolean hasCrazyColor = false;
    int crazyColorCounter =0;
    boolean isColored = false;

    private Random rand;

 	private int screen_width = 0;
	private int screen_height = 0;

    private int ball_radius = 20;
	private int ball_x = 50;
	private int ball_y = 0;
	private float ball_speed_x = 5;
	private float ball_speed_y = 5;
	private float gravity = 0.9f;

    Canvas canvas;

    public BouncingBall() {

        // define color of ball
        paint = new Paint();
		paint.setColor(Color.BLACK);

        // define random value.
        // Note that it can be outside the screen as 'Move' code will but it inside
        // when screen sizes will be defined
        rand = new Random();
        ball_x = rand.nextInt(500);
        ball_y = rand.nextInt(500);
        ball_speed_x += 10 + rand.nextInt(10);
        ball_speed_y += 10 + rand.nextInt(20);

        // color
        boolean isRed =   true; //rand.nextBoolean();
        boolean isGreen = rand.nextBoolean();
        boolean isBlue =  rand.nextBoolean();
        randomColor = Color.rgb(isRed?0xFF:0, isGreen?0xFF:0, isBlue?0xFF:0);
        paint.setColor(randomColor);
    }


    public int Move(int p_screen_width, int p_screen_height, Canvas p_canvas) {


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

        // reduce speed when ball is near ground
        // (speed y is already reduced by numerical rounding)
        int ball_height = (screen_height - ball_y);

        if (abs(ball_speed_y) < 2 && ball_height < 1.5 * ball_radius) {
            ball_speed_x *= 0.99;
            ball_speed_y *= 0.99;
        }

        return (ball_height) ;
    }
}
