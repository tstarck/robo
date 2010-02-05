/* Katapultti.java
 * Pieni ja yksinkertainen linnoitustenmurskaaja.
 */

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class Katapultti {
	public static void main(String[] argh) {
		System.out.println("Prss any key");

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

		a.forward();
		b.forward();
		c.forward();

		while (90 < a.getTachoCount() || 90 < b.getTachoCount() || 90 < c.getTachoCount()) {
			a.flt();
			b.flt();
			c.flt();
		}

		// try { Thread.sleep (1000); } catch (Exception e) {}

		// a.lock(99);
		// b.lock(99);
		// c.lock(99);

		// a.flt();
		// b.flt();
		// c.flt();

		System.out.println("End.");
		Button.ESCAPE.waitForPressAndRelease();
	}
}
