/* Katapultti.java
 * Pieni ja yksinkertainen linnoitustenmurskaaja.
 */

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class Katapultti {
	public static void main(String[] argh) {
		System.out.println("Prss any key");
		System.out.println(" to SHOOT !");

		Button.ENTER.waitForPressAndRelease();

		Motor a = Motor.A;
		Motor b = Motor.B;
		Motor c = Motor.C;

		a.resetTachoCount();
		b.resetTachoCount();
		c.resetTachoCount();

		a.setSpeed(900);
		b.setSpeed(900);
		c.setSpeed(900);

		System.out.println("Tacho A: " + a.getTachoCount());

		a.backward();
		b.backward();
		c.backward();

		while (-60 < a.getTachoCount()) {
			System.out.println("Tacho A: " + a.getTachoCount());
		}

		a.stop();
		b.stop();
		c.stop();

		a.flt();
		b.flt();
		c.flt();

		System.out.println("Done.");
		Button.ESCAPE.waitForPressAndRelease();
	}
}
