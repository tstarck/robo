/* Katapultti.java
 * Pieni ja yksinkertainen linnoitustenmurskaaja.
 */

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.LCD;

public class Katapultti {
	public static void main(String[] argh) {
		Motor a = Motor.A;
		Motor b = Motor.B;
		Motor c = Motor.C;

		while (!Button.ESCAPE.isPressed()) {
			LCD.clear();

			System.out.println(" Ammu !!");

			a.resetTachoCount(); b.resetTachoCount(); c.resetTachoCount();

			a.setSpeed(900); b.setSpeed(900); c.setSpeed(900);

			Button.ENTER.waitForPressAndRelease();

			a.backward(); b.backward(); c.backward();

			while (-69 < a.getTachoCount()) {}

			c.stop(); b.stop(); a.stop();

			try { Thread.sleep (1000); } catch (Exception e) {}

			a.setSpeed(90); b.setSpeed(90); c.setSpeed(90);

			a.forward(); b.forward(); c.forward();

			while (0 > a.getTachoCount()) {}

			c.stop(); b.stop(); a.stop();

			a.flt(); b.flt(); c.flt();
		}
	}
}
