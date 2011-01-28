/* Stop.java */

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.*;

/**
 */
public class Stop implements Behavior {
	private TouchSensor nappi;
	private static Motor oikea = Motor.A;
	private static Motor vasen = Motor.B;

	public Stop() {
		this.nappi = new TouchSensor(SensorPort.S1);
	}

	public boolean takeControl() {
		return !nappi.isPressed();
	}

	public void action() {
		oikea.lock(100);
		vasen.lock(100);

		oikea.resetTachoCount();
		vasen.resetTachoCount();

		oikea.setSpeed(900);
		vasen.setSpeed(900);

		oikea.rotate(180, true);
		vasen.rotate(180, true);

		oikea.flt();
		vasen.flt();
	}

	public void suppress() {}
}
