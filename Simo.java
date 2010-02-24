/* Simo.java
 * Wiimote controlled vehicle with an middle axis.
 */

import lejos.nxt.LCD;
import lejos.nxt.Button;
import lejos.nxt.Motor;

public class Simo {
	public static void main(String[] limbo) {
		WiiNXT ohjain = WiiNXT.getInstance();

		ohjain.setMode(WiiNXT.RECVMOTION | WiiNXT.RECVBUTTONS | WiiNXT.MOTIONON);

		while (!Button.ENTER.isPressed()) {
			ohjain.read();
			System.out.println("pitch : " + ohjain.pitch());
			if (Button.ESCAPE.isPressed()) ohjain.setRumble(true);
			else ohjain.setRumble(false);
		}
	}
}
