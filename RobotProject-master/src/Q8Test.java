import java.sql.Time;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner6;

import java.time.LocalTime;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import ShefRobot.*;

/**
 *
 * @author sdn
 */
public class Q8Test {
	
    public static void main(String[] args) {
    	Robot myRobot = new Robot();

        Motor leftMotor = myRobot.getLargeMotor(Motor.Port.C);
        Motor rightMotor = myRobot.getLargeMotor(Motor.Port.B);
        UltrasonicSensor leftSensor = myRobot.getUltrasonicSensor(Sensor.Port.S4);
        UltrasonicSensor rightSensor = myRobot.getUltrasonicSensor(Sensor.Port.S1);
        TouchSensor touch = myRobot.getTouchSensor(Sensor.Port.S2);

        final float KU = 0.43f; // 0.4 good, 0.43 risky
        final float TU = 0f; // Oscilation period
        
        final float KP = KU;//0.6f * KU;
        final float KD = 0;//(3f * KU * TU) / 40f;
        final float KI = 0;//(1.2f * KU) / TU;

        final float THRESHOLD = 150; // 125 good

        final float SPEED = 400; // 300 good, 350 risky
        
        double lastTime = System.nanoTime() / 1000000000.0; // Time in seconds
        double time = 0;
        float deltaTime = 0;
        float startTime = 0;
        
        float previousError = 0f;
        float integral = 0f;

        float previousLeft = 0;
        float previousRight = 0;
        float previousOutput = 0;
        
        while (true) {
        	time = System.nanoTime() / 1000000000.0; // Current time in seconds
        	deltaTime = (float) (time - lastTime); // Time between last frame and current frame
        	startTime += deltaTime; // Time since starting the program
            
            float leftDistance = leftSensor.getDistance();
            float rightDistance = rightSensor.getDistance();

            if (leftDistance >= Float.POSITIVE_INFINITY || leftDistance == Float.NaN || leftDistance <= Float.NEGATIVE_INFINITY)
            {
                leftDistance = previousLeft;
            } else {
                previousLeft = leftDistance;
            }

            if (rightDistance >= Float.POSITIVE_INFINITY || rightDistance == Float.NaN || rightDistance <= Float.NEGATIVE_INFINITY)
            {
                rightDistance = previousRight;
            } else {
                previousRight = rightDistance;
            }
            float error = (rightDistance - leftDistance) * 1000;

        	// integral = integral + (error * deltaTime);
        	// float derivative = (error - previousError) / deltaTime;
            float output = KP * error;// + KI * integral + KD * derivative;

            if (output > THRESHOLD)
                output = THRESHOLD;

            if (output < -THRESHOLD)
                output = -THRESHOLD;

            // if (output >= Float.POSITIVE_INFINITY || output == Float.NaN || output <= Float.NEGATIVE_INFINITY)
            // {
            //     output = previousOutput;
            // }

            System.out.println(previousLeft + ":" + leftDistance + ":" + previousRight + ":" + rightDistance + ":" + output);

            if (output > 0)
                {
                    System.out.println("RIGHT");
                    leftMotor.setSpeed(Math.round(SPEED + Math.abs(output)));
                    rightMotor.setSpeed(Math.round(Math.max(0, SPEED - Math.abs(output))));
                } else if (output < 0)
                {
                    System.out.println("LEFT");
                    leftMotor.setSpeed(Math.round(Math.max(0, SPEED - Math.abs(output))));
                    rightMotor.setSpeed(Math.round(SPEED + Math.abs(output)));
                }

                leftMotor.forward();
                rightMotor.forward();
        	
        	if (touch.isTouched() || startTime > 200)
        		break;
        	
        	previousError = error;
            lastTime = time;
            
            previousOutput = output;

            try {
				Thread.sleep(0);
			} catch (Exception e)
			{
				System.out.println(e);
			}
        }
        
        leftMotor.stop();
        rightMotor.stop();
        myRobot.close();
    	System.out.println("FINISHED");
    }
}