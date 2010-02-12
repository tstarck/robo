/* Simo.java
 * Wiimote controlled vehicle with an middle axis.
 */

import lejos.nxt.LCD;
import lejos.nxt.Button;
import lejos.nxt.Motor;

public class Simo {
	/*
	public static Motor keula = Motor.A;
	public static Motor akseli = Motor.B;
	public static Motor taka = Motor.C;

	public static void vruum(int vauhti) {
		if (vauhti == 0) { keula.flt(); taka.flt(); }

		keula.setSpeed(vauhti); taka.setSpeed(vauhti);

		keula.backward(); taka.backward();
	}

	public static void kurvi(int kulma) {
		int nyt = akseli.getTachoCount();

		akseli.setSpeed(15);

		if (nyt < kulma) {
			akseli.forward();
			while (nyt < kulma) {
				nyt = akseli.getTachoCount();
				System.out.println("Fwrd > " + akseli.getTachoCount());
			}
			akseli.stop();
		}

		if (nyt > kulma) {
			akseli.backward();
			while (nyt > kulma) {
				nyt = akseli.getTachoCount();
				System.out.println("Bckwrd > " + akseli.getTachoCount());
			}
			akseli.stop();
		}

		akseli.lock(99);
	}
	*/

	public static void main(String[] limbo) {
		WiiNXT ohjain = WiiNXT.getInstance();

		ohjain.setMode(WiiNXT.RECVMOTION | WiiNXT.RECVBUTTONS | WiiNXT.MOTIONON);

		while (!Button.ENTER.isPressed()) {
			ohjain.read();
			System.out.println("Pitch::" + ohjain.pitch());
			if (Button.ESCAPE.isPressed()) ohjain.setRumble(true);
			else ohjain.setRumble(false);
		}

		/*
		System.out.println("Vruum?");
		Button.ENTER.waitForPressAndRelease();
		akseli.resetTachoCount();
		LCD.clear();

		vruum(720);
		try { Thread.sleep (3000); } catch (Exception e) {}
		vruum(360);
		kurvi(15);
		try { Thread.sleep (2000); } catch (Exception e) {}
		kurvi(-30);
		try { Thread.sleep (4000); } catch (Exception e) {}
		kurvi(0);
		vruum(0);

		System.out.println("End.");
		Button.ESCAPE.waitForPressAndRelease();
		*/
	}
}
