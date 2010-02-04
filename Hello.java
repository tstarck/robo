/* Hello.java
 * Hello world for LeJOS.
 */

import lejos.nxt.Button;

public class Hello {
	public static void main(String[] argh) {
		System.out.println("Helo wrld!");
		Button.ESCAPE.waitForPressAndRelease();
	}
}
