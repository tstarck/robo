/* Drive.java
 * Simple test to drive a motor.
 */

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class Drive {
	public static void main(String[] argh) {
		System.out.println("Vruum!");
		Motor kone = Motor.A;
		kone.setSpeed(360);
		kone.forward();
		try {
			Thread.sleep (1000);
		}
		catch (Exception e) {}
		kone.flt();
		System.out.println("End.");
		Button.ESCAPE.waitForPressAndRelease();
	}
}
