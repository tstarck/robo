/* Move.java */

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.*;

public class Move implements Behavior {
	private int delta;
	private int speed;
	private TouchSensor nappi;
	private static Motor oikea = Motor.A;
	private static Motor vasen = Motor.B;

	public Move() {
		this.speed = 0;
		this.delta = 10;
		this.nappi = new TouchSensor(SensorPort.S1);
	}

	public boolean takeControl() {
		return nappi.isPressed();
	}

	public void action() {
		this.speed += this.delta;

		oikea.setSpeed(this.speed);
		vasen.setSpeed(this.speed);

		oikea.backward();
		vasen.backward();
	}

	public void suppress() {}
}
