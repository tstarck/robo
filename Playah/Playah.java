/* Playah.java */

import lejos.nxt.Motor;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

public class Playah {
	public static void main(String[] argh) {
		LightSensor valo = new LightSensor(SensorPort.S2);

		System.out.println("> enter?");
		Button.ENTER.waitForPressAndRelease();

		while (!Button.ESCAPE.isPressed()) {
			System.out.println(valo.readValue());
			Delay.now(333);
		}
	}
}
