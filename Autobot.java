/* Autobot.java
 */

import java.io.IOException;
import lejos.nxt.Button;
import lejos.nxt.comm.*;
import lejos.nxt.remote.*;

public class Autobot {
	public static void main(String[] argh) {
		RemoteNXT yhteys = null;

		System.out.print("Connect..");

		try {
			yhteys = new RemoteNXT("lego2", Bluetooth.getConnector());
			System.out.println(" done!");
		}
		catch (IOException e) {
			System.out.println(" failed!");
			Button.waitForPress();
			System.exit(1);
		}

		RemoteMotor kone = yhteys.A;
		kone.setSpeed(360);
		kone.forward();
		try { Thread.sleep(2000); } catch (Exception e) {}
		kone.flt();

		Button.ESCAPE.waitForPressAndRelease();
		System.exit(0);
	}
}
