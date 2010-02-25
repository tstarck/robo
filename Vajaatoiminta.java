/* Vajaatoiminta.java
 * Simple action for subsumption testing.
 */

import lejos.robotics.subsumption.*;

public class Vajaatoiminta implements Behavior {
	private int nro;
	private boolean act;

	public Vajaatoiminta() {
		this.nro = 5;
		this.act = true;
	}

	public boolean takeControl() {
		return this.act;
	}

	public void suppress() {
	}

	public void action() {
		this.nro--;
		if (this.nro <3) this.act = false;
		System.out.println("[" + this.nro + "]");
	}
}
