/* Stop.java
 * Simple ... :>>
 */

import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.*;

public class Stop implements Behavior {
	private TouchSensor nappi;

	public Stop() {
		this.nappi = new TouchSensor(SensorPort.S1);
	}

	public boolean takeControl() {
		return false;
	}

	public void action() {
		// peruuta moottoreilla pikkasen ja jää siihen
		System.out.println("[stop]");
	}

	public void suppress() {
	}
}
