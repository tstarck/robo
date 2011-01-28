/* Ctrl.java */

import lejos.nxt.Button;
import lejos.robotics.subsumption.*;

/**
 */
public class Ctrl {
	public static void main(String[] argh) {
		Behavior[] toiminnot = {
			new Move(),
			new Avoid(),
			new Stop()
		};

		Arbitrator toimintamalli = new Arbitrator(toiminnot);

		toimintamalli.start();

		System.exit(0);
	}
}
