/* Move.java
 * Simple ... :>>
 */

import lejos.nxt.Motor;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.*;

public class Move implements Behavior {
	private TouchSensor nappi;
	private static Motor oikea = Motor.A;
	private static Motor vasen = Motor.B;

	public Move() {
		this.nappi = new TouchSensor(SensorPort.S1);
	}

	public boolean takeControl() {
		return nappi.isPressed();
	}

	public void action() {
		System.out.println("");
	}

	public void suppress() {
	}
}
