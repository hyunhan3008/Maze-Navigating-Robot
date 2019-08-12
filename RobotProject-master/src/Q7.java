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
public class Q7 {
	
    public static void main(String[] args) {

        //Create a robot object to use and connect to it
    	Scanner reader = new Scanner(System.in);
    	String input;
    	Robot myRobot = new Robot("dia-lego-b4");
       
        //The robot is made of components which are themselves objects.
        //Create references to them as useful shortcuts
        Motor leftMotor = myRobot.getLargeMotor(Motor.Port.B);
        Motor rightMotor = myRobot.getLargeMotor(Motor.Port.C);
        UltrasonicSensor sensor = myRobot.getUltrasonicSensor(Sensor.Port.S2);
        TouchSensor touch = myRobot.getTouchSensor(Sensor.Port.S3);

        final float KP = 6;
        final float KD = 0;
        final float KI = 0;
        final float TRESHOLD = 0;
        
        long lastTime = System.currentTimeMillis() / 1000;
        long time = 0;
        long deltaTime = 0;
        long timeSince10 = 0;
        long startTime = 0;
        
        float lowerSetPoint = 200f;
        float upperSetPoint = 200f;
        float setPoint = lowerSetPoint;
        boolean lower = true;
        
        float previousError = 0f;
        float integral = 0f;
        while (true) {
        	time = System.currentTimeMillis() / 1000;
        	deltaTime = time - lastTime;
        	timeSince10 += deltaTime;
        	startTime += deltaTime;
        	
        	float currentDistance = sensor.getDistance() * 1000f;
        	float error = setPoint - currentDistance;
        	integral = integral + error * deltaTime;
        	float derivative = (error - previousError) / deltaTime;
        	float output = KP * error + KI * integral + KD * derivative;
        	leftMotor.setSpeed(Math.round(Math.abs(output)));
        	rightMotor.setSpeed(Math.round(Math.abs(output)));
        	
        	if (output < TRESHOLD) {
        		leftMotor.forward();
        		rightMotor.forward();
        	}
        	else if (output > TRESHOLD) {
        		leftMotor.backward();
        		rightMotor.backward();
        	}
        	
        	System.out.println(currentDistance + ":" + setPoint);
        	
        	if (timeSince10 > 10) {
        		if (lower) {
        			setPoint = upperSetPoint;
        			lower = false;
        			System.out.println("TARGET -> UPPER POINT");
        		}
        		else {
        			setPoint = lowerSetPoint;
        			lower = true;
        			System.out.println("TARGET -> LOWER POINT");
        		}
        		timeSince10 = 0;
        	}
        	
        	if (touch.isTouched())
        		break;
        	
        	lastTime = time;
        }
    	System.out.println("FINISHED");
    }
}