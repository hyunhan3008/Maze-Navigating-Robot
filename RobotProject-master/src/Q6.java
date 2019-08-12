import java.sql.Time;
import java.time.LocalTime;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import ShefRobot.*;

/**
 *
 * @author sdn
 */
public class Q6 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Create a robot object to use and connect to it
    	Robot myRobot = new Robot("dia-lego-b4");
       
        //The robot is made of components which are themselves objects.
        //Create references to them as useful shortcuts
        Motor leftMotor = myRobot.getLargeMotor(Motor.Port.B);
        Motor rightMotor = myRobot.getLargeMotor(Motor.Port.C);
        Speaker speaker = myRobot.getSpeaker();
        UltrasonicSensor sensor = myRobot.getUltrasonicSensor(Sensor.Port.S2);
        try {
			PrintWriter pw = new PrintWriter(new File("test-test.csv"));
	        StringBuilder sb = new StringBuilder();
	        
	        int counter = 0;
	        float distance;
	        float totalDistance = 0;
	        final char DEFAULT_SEPARATOR = ',';
	        
	        
	        Time time;
	        Time targetTime = Time.valueOf(LocalTime.now().plusSeconds(10));
	        while (true) {
	        	
	        	time = Time.valueOf(LocalTime.now()); 
	        	if (time.after(targetTime))
	        		break;
	        	
	        	counter+=1;
	        	
	        	distance = (float) sensor.getDistance();
	        	totalDistance+=distance;
		        System.out.println(counter + " : " + distance + " : " + totalDistance);
		        sb.append(counter + "," + distance + "\n");
	        }
	        
	        System.out.println("finished");
	        
	        System.out.println(totalDistance/counter);
	        pw.write(sb.toString());
	        pw.close();
	        System.out.println("done!");
	        myRobot.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        /*
        //Go Forwards
        leftMotor.setSpeed(150);
        rightMotor.setSpeed(150);
        leftMotor.forward();
        rightMotor.forward();

        //Keep going for 5 seconds
        myRobot.sleep(5000);

        //Stop
        leftMotor.stop();
        rightMotor.stop();

        //Beep at 1000Hz for half a second
        speaker.playTone(1000, 500);

        //Go Backwards
        leftMotor.backward();
        rightMotor.backward();

        //Keep going for 5 seconds
        myRobot.sleep(5000);

        //Stop
        leftMotor.stop();
        rightMotor.stop();
        
        */
        //Disconnect from the Robot
        //myRobot.close();

    }

}