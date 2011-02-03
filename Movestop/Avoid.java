/* Avoid.java */

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.subsumption.*;

/**
 */
public class Avoid implements Behavior {
	private boolean ctrl;
	private TouchSensor nappi;
	private UltrasonicSensor silmae;
	private static Motor oikea = Motor.A;
	private static Motor vasen = Motor.B;

	public Avoid() {
		this.ctrl = false;
		this.nappi = new TouchSensor(SensorPort.S1);
		this.silmae = new UltrasonicSensor(SensorPort.S3);
	}

	public boolean takeControl() {
		if (this.ctrl) return true;
		this.ctrl = (nappi.isPressed()) && (silmae.getDistance() < 10);
		return this.ctrl;
	}

	public void action() {
		oikea.lock(100);
		vasen.lock(100);

		// System.out.println("> tuut tuut");

		oikea.resetTachoCount();
		vasen.resetTachoCount();

		oikea.setSpeed(360);
		vasen.setSpeed(360);

		oikea.rotate(-170, true);
		vasen.rotate(210, true);

		Delay.now(666);

		this.ctrl = false;
	}

	public void suppress() {}
}
