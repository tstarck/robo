/* Stop.java */

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.subsumption.*;

public class Stop implements Behavior {
	private boolean on;
	private TouchSensor nappi;

	public Stop() {
		this.on = true;
		this.nappi = new TouchSensor(SensorPort.S1);
	}

	public boolean takeControl() {
		if (nappi.isPressed()) return false;
		return this.on;
	}

	public void action() {
		this.on = false;
		System.out.println("> peruutus");
		Delay.now(2000);
	}

	public void suppress() {
	}
}
