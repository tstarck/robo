/* Stop.java */

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.*;

public class Stop implements Behavior {
	private boolean on;
	private TouchSensor nappi;
	private static Motor oikea = Motor.A;
	private static Motor vasen = Motor.B;

	public Stop() {
		this.on = true;
		this.nappi = new TouchSensor(SensorPort.S1);
	}

	public boolean takeControl() {
		if (nappi.isPressed()) {
			return false;
		}
		return this.on;
	}

	public void action() {
		this.on = false;
		System.out.println("> iik!");

		oikea.lock(100);
		vasen.lock(100);

		oikea.resetTachoCount();
		vasen.resetTachoCount();

		oikea.setSpeed(900);
		vasen.setSpeed(900);

		oikea.rotate(180, true);
		vasen.rotate(180, true);
	}

	public void suppress() {}
}
