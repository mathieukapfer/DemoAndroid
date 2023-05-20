package com.example.myapplicationdemosurface;

import static java.lang.Math.abs;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class BouncingBall {

    private Paint mPaint;

    private Random mRand;

 	private int mScreenWidth = 0;
	private int mScreenHeight = 0;

    private int mBallRadius = 20;
	private int mBallX = 50;
	private int mBallY = 0;
	private float mBallSpeedX = 5;
	private float mBallSpeedy = 5;
	private float mGravity = 0.9f;

    Canvas canvas;

    public BouncingBall() {

        // define color of ball
        mPaint = new Paint();
		mPaint.setColor(Color.BLACK);

        // define random value.
        // Note that it can be outside the screen as 'Move' code will but it inside
        // when screen sizes will be defined
        mRand = new Random();
        mBallX = mRand.nextInt(500);
        mBallY = mRand.nextInt(500);
        mBallSpeedX += 10 + mRand.nextInt(10);
        mBallSpeedy += 10 + mRand.nextInt(20);

        // color
        boolean isRed =   true; //rand.nextBoolean();
        boolean isGreen = mRand.nextBoolean();
        boolean isBlue =  mRand.nextBoolean();
        int randomColor = Color.rgb(isRed?0xFF:0, isGreen?0xFF:0, isBlue?0xFF:0);
        mPaint.setColor(randomColor);
    }


    public int Move(int screenWidth, int screenHeight, Canvas canvas) {


        // update sceen size & canvas
        mScreenWidth = screenWidth;
        mScreenHeight = screenHeight;

        // Apply gravity to the ball
        mBallSpeedy += mGravity;

        // Move the ball
        mBallX += (int) mBallSpeedX;
        mBallY += (int) mBallSpeedy;

        // Bounce the ball off the walls
        if (mBallX + mBallRadius > mScreenWidth) {
            mBallX = mScreenWidth - mBallRadius;
            mBallSpeedX = -mBallSpeedX;
        } else if (mBallX - mBallRadius < 0) {
            mBallX = mBallRadius;
            mBallSpeedX = -mBallSpeedX;
        }
        if (mBallY + mBallRadius > mScreenHeight) {
            mBallY = mScreenHeight - mBallRadius;
            mBallSpeedy = -mBallSpeedy;
        } else if (mBallY - mBallRadius < 0) {
            mBallY = mBallRadius;
            mBallSpeedy = -mBallSpeedy;
        }

        // Draw the ball
        canvas.drawCircle(mBallX, mBallY, mBallRadius, mPaint);

        // reduce speed when ball is near ground
        // (speed y is already reduced by numerical rounding)
        int ball_height = (mScreenHeight - mBallY);

        if (abs(mBallSpeedy) < 2 && ball_height < 1.5 * mBallRadius) {
            mBallSpeedX *= 0.99;
            mBallSpeedy *= 0.99;
        }

        return (ball_height) ;
    }
}
