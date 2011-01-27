/* Ctrl.java
 * Simple behaviour class for a robot, which
 * moves until it comes across a obsticle.
 * */

import lejos.nxt.Button;
import lejos.robotics.subsumption.*;

public class Ctrl {
	public static void main(String[] argh) {
		Behavior[] toiminnot = {
			new Move(),
			new Stop()
		};

		Arbitrator toimintamalli = new Arbitrator(toiminnot, true);

		System.out.println("> alkaa");

		toimintamalli.start();

		System.out.println("> loppu");

		Button.ENTER.waitForPressAndRelease();

		System.exit(0);
	}
}
