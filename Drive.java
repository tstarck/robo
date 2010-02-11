/* Drive.java
 * Simple test to drive two motors.
 */

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class Drive {
	public static Motor kone = Motor.A;
	public static Motor koje = Motor.C;

	public static void main(String[] argh) {
		System.out.println("Vruum!");
		Button.ENTER.waitForPressAndRelease();

		kone.setSpeed(360);
		koje.setSpeed(360);
		kone.forward();
		koje.forward();

		try { Thread.sleep (1000); } catch (Exception e) {}

		kone.setSpeed(720);
		koje.setSpeed(720);

		try { Thread.sleep (2000); } catch (Exception e) {}

		kone.setSpeed(400);
		koje.setSpeed(300);

		try { Thread.sleep (2000); } catch (Exception e) {}

		kone.setSpeed(720);
		koje.setSpeed(720);
		kone.backward();
		koje.backward();

		try { Thread.sleep (1000); } catch (Exception e) {}

		kone.stop();
		koje.stop();

		System.out.println("End.");
		Button.ESCAPE.waitForPressAndRelease();
	}
}
