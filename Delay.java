/* Delay.java */

public class Delay {
	public static void now(int ms) {
		try {
			Thread.sleep(ms);
		}
		catch (Exception e) {
		}
	}
}
