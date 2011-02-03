/* Stop.java */

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.*;

/**
 */
public class Stop implements Behavior {
	private boolean lukko;
	private TouchSensor nappi;
	private static Motor oikea = Motor.A;
	private static Motor vasen = Motor.B;

	public Stop() {
		this.lukko = false;
		this.nappi = new TouchSensor(SensorPort.S1);
	}

	public boolean takeControl() {
		if (nappi.isPressed()) {
			this.lukko = false;
			return false;
		}

		return true;
	}

	public void action() {
		if (this.lukko) return;

		oikea.lock(100);
		vasen.lock(100);

		oikea.resetTachoCount();
		vasen.resetTachoCount();

		oikea.setSpeed(900);
		vasen.setSpeed(900);

		oikea.rotate(180, true);
		vasen.rotate(180, true);

		Delay.now(666);

		oikea.flt();
		vasen.flt();

		this.lukko = true;
	}

	public void suppress() {}
}
