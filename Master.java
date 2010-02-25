/* Master.java
 * Code for the active part making connection between two NXTs.
 */

import java.util.Vector;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.bluetooth.RemoteDevice;

import lejos.nxt.Button;
import lejos.nxt.comm.*;
import lejos.robotics.subsumption.*;

public class Master implements Behavior {
	private boolean enabled;

	private DataInputStream in;
	private DataOutputStream out;

	private void bluetoothInit(String name) {
		RemoteDevice laite = Bluetooth.getKnownDevice(name);
		NXTConnection yhteys = Bluetooth.connect(laite);

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

	public Master(String name) {
		this.enabled = false;
		this.bluetoothInit(name);
	}

	public boolean takeControl() {
		return this.enabled;
	}

	public void suppress() {
	}

	public void action() {
		try {
			this.out.writeInt(0);
			this.out.flush();
			Delay.now(10000);
		}
		catch (IOException e) {
			System.out.println(":-/");
			System.exit(1);
		}
	}
}
