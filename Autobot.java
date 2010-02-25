/* Autobot.java */

import lejos.nxt.Button;
import lejos.robotics.subsumption.*;

public class Autobot {
	public static void main(String[] argh) {
		Behavior blutuut = null;

		System.out.println("Master-mode?");

		if (1 == Button.waitForPress()) {
			System.out.println("I am Master");
			blutuut = new Master("NXT");
		}
		else {
			System.out.println("I iz Puppy");
			blutuut = new Puppy();
		}

		Behavior[] toiminnot = {blutuut};

		Arbitrator toimintamalli = new Arbitrator(toiminnot, true);

		toimintamalli.start();

		System.out.println("The End");
		Button.waitForPress();
		System.exit(0);
	}
}
