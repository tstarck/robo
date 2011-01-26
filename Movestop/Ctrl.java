/* Ctrl.java */

import lejos.nxt.Button;
import lejos.robotics.subsumption.*;

public class Ctrl {
	public static void main(String[] argh) {
		Behavior[] toiminnot = {
			new Move();
			new Stop();
		};

		Arbitrator toimintamalli = new Arbitrator(toiminnot, true);

		toimintamalli.start();

		System.out.println(".");
		Button.waitForPress();

		System.exit(0);
	}
}
