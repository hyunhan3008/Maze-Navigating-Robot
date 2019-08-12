import java.sql.Time;
import java.util.Scanner;
import java.time.LocalTime;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import ShefRobot.*;

/**
 *
 * @author sdn
 */
public class Q8 {
	
    public static void main(String[] args) {
    	Robot myRobot = new Robot("dia-lego-b4");

        Motor leftMotor = myRobot.getLargeMotor(Motor.Port.B);
        Motor rightMotor = myRobot.getLargeMotor(Motor.Port.C);
        UltrasonicSensor sensor = myRobot.getUltrasonicSensor(Sensor.Port.S2);
        TouchSensor touch = myRobot.getTouchSensor(Sensor.Port.S3);

        final float KU = 8.75f; // KP when the robot oscilates continuosly
        final float TU = 1.4f; // Oscilation period
        
        float KP = 0;
        float KD = 0;
        float KI = 0;
        
        double lastTime = System.nanoTime() / 1000000000.0; // Time in seconds
        double time = 0;
        float deltaTime =  0;
        float startTime = 0;
        
        float setPoint = 150f; // 30 cm in mm
        
        float previousError = 0f;
        float integral = 0f;
		float derivative = 0f;
        while (true) {
        	time = System.nanoTime() / 1000000000.0; // Current time in seconds
        	deltaTime = (float) (time - lastTime); // Time between last frame and current frame
        	startTime += deltaTime; // Time since starting the program
        	
			KP = 0.6f * KU;
			KI = 2f * KP * deltaTime / TU;
			KD = KP * TU / 8f * deltaTime;

        	float currentDistance = sensor.getDistance() * 1000f; // Current distance in mm        	
        	float error = setPoint - currentDistance;
        	integral = integral + error;
        	derivative = error - previousError;
        	float output = KP * error + KI * integral + KD * derivative;
        	leftMotor.setSpeed(Math.round(Math.abs(output)));
        	rightMotor.setSpeed(Math.round(Math.abs(output)));
        	
        	if (output < 0) {
        		leftMotor.forward();
        		rightMotor.forward();
        	}
        	else if (output > 0) {
        		leftMotor.backward();
        		rightMotor.backward();
        	}

			try {
				Thread.sleep(10);
			} catch (Exception e)
			{
				System.out.println(e);
			}

        	if (touch.isTouched() || startTime > 60)
        		break;
        	
        	previousError = error;
        	lastTime = time;
        }
        
        leftMotor.stop();
        rightMotor.stop();
        myRobot.close();
    	System.out.println("FINISHED");
    }
}