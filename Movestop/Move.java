/* Move.java */

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.*;

public class Move implements Behavior {
	private int x;
	private TouchSensor nappi;
	private static Motor oikea = Motor.A;
	private static Motor vasen = Motor.B;

	public Move() {
		this.x = 1000;
		this.nappi = new TouchSensor(SensorPort.S1);
	}

	public boolean takeControl() {
		return nappi.isPressed();
	}

	public void action() {
		System.out.println("> move " + this.x);
		Delay.now(this.x);
		this.x += 100;
	}

	public void suppress() {
	}
}
