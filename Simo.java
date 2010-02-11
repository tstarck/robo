/* Simo.java
 * Simple test of consept with a vehicle with an middle axis.
 */

import lejos.nxt.Button;
import lejos.nxt.Motor;

public class Drive {
	public static Motor keula = Motor.A;
	public static Motor akseli = Motor.B;
	public static Motor taka = Motor.C;

	public void vruum(int vauhti) {
		if (vauhti == 0) { keula.flt(); taka.flt(); }

		keula.setSpeed(vauhti); taka.setSpeed(vauhti);

		keula.backward(); taka.backward();
	}

	public void kurvi(int kulma) {
		int nyt = akseli.getTachoCount();

		akseli.setSpeed(30);

		while (nyt < kulma) {
			System.out.println("Akseli frwd");
			akseli.forward();
		}

		akseli.stop();

		while (nyt > kulma) {
			System.out.println("Akseli bckwrd");
			akseli.backward();
		}

		akseli.stop();
		akseli.lock(99);
	}

	public static void main(String[] limbo) {
		System.out.println("Vruum?");
		Button.ENTER.waitForPressAndRelease();
		akseli.resetTachoCount();
		LCD.clear();

		this.vruum(720);
		try { Thread.sleep (2000); } catch (Exception e) {}
		this.vruum(360);
		kurvi(15);
		try { Thread.sleep (2000); } catch (Exception e) {}
		kurvi(0);

		System.out.println("End.");
		Button.ESCAPE.waitForPressAndRelease();
	}
}
