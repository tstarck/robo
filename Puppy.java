/* Puppy.java
 * Code for the listening part in connection between two NXTs.
 */

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.comm.*;
import lejos.robotics.subsumption.*;

public class Puppy implements Behavior {
	private boolean enabled;

	private DataInputStream in;
	private DataOutputStream out;

	private void bluetoothInit() {
		NXTConnection yhteys = Bluetooth.waitForConnection();

		if (yhteys == null) {
			System.out.println("[bt] :-(");
			return;
		}
		else {
			System.out.println("[bt] \\o/");
		}

		this.in = yhteys.openDataInputStream();
		this.out = yhteys.openDataOutputStream();

		this.enabled = true;
	}

	public Puppy() {
		this.enabled = false;
		this.bluetoothInit();
	}

	public boolean takeControl() {
		return this.enabled;
	}

	public void suppress() {
	}

	public void action() {
		int tmp = 0;

		try {
			tmp = this.in.readInt();
			System.out.println("[" + tmp + "]");
			tmp = 0;
		}
		catch (IOException e) {
			System.out.println(":-/");
		}
	}
}
