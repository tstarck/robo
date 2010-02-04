/* Mario.java
 * Trying to play Mario theme song.
 */

import lejos.nxt.Button;
import lejos.nxt.Sound;

public class Mario {
	private final static int C4 = 262;
	private final static int D4 = 294;
	private final static int E4 = 330;
	private final static int F4 = 349;
	private final static int G4 = 392;
	private final static int A4 = 440;
	private final static int As4 = 466;
	private final static int B4 = 494;

	private final static int C5 = 523;
	private final static int Cs5 = 554;
	private final static int D5 = 587;
	private final static int Ds5 = 622;
	private final static int E5 = 659;
	private final static int F5 = 698;
	private final static int Fs5 = 740;
	private final static int G5 = 784;
	private final static int A5 = 880;

	private final static int C6 = 1047;

	public static void main(String[] argh) {
		System.out.println("Mario!");

		play(E5, 100, 150);
		play(E5, 100, 300);
		play(E5, 100, 300);
		play(C5, 100, 100);
		play(E5, 100, 300);
		play(G5, 100, 550);
		play(G4, 100, 575);

		for (int i=0; i<2; i++) {
			play(C5, 100, 450);
			play(G4, 100, 400);
			play(E4, 100, 500);
			play(A4, 100, 300);
			play(B4, 80, 330);
			play(As4, 100, 150);
			play(A4, 100, 300);
			play(G4, 100, 200);
			play(E5, 80, 200);
			play(G5, 50, 150);
			play(A5, 100, 300);
			play(F5, 80, 150);
			play(G5, 50, 350);
			play(E5, 80, 300);
			play(C5, 80, 150);
			play(D5, 80, 150);
			play(B4, 80, 500);
		}

		play(B4, 100, 300); //F

		while (false) {
		play(G5, 100, 100);
		play(Fs5, 100, 150);
		play(E5, 100, 150);
		play(Ds5, 150, 300);

		play(E5, 150, 300);
		play(G4, 100, 150);
		play(A4, 100, 150);

		play(B4, 100, 300);
		play(A4, 100, 150);
		play(B4, 100, 100);
		play(D5, 100, 225);

		play(B4, 100, 300);

		play(G5, 100, 100);
		play(Fs5, 100, 150);
		play(E5, 100, 150);
		play(Ds5, 150, 300);

		play(E5, 200, 300);

		play(C6, 80, 300);
		play(C6, 80, 150);
		play(C6, 80, 300);

		play(G4, 100, 300);
		play(B4, 100, 300);

		// Chorus 2:
		play(G5, 100, 100);
		play(Fs5, 100, 150);
		play(E5, 100, 150);
		play(Ds5, 150, 300);

		play(E5, 150, 300);
		play(G4, 100, 150);
		play(A4, 100, 150);

		play(B4, 100, 300);
		play(A4, 100, 150);
		play(B4, 100, 100);
		play(D5, 100, 425);
		// End 2

		play(D5, 100, 450);

		play(Cs5, 100, 425);

		play(B4, 100, 350);

		play(G4, 100, 300);
		play(B4, 100, 300);
		play(B4, 100, 150);
		play(B4, 100, 300);

		play(B4, 100, 300);

		// Insert 2
		play(G5, 100, 100);
		play(Fs5, 100, 150);
		play(E5, 100, 150);
		play(Ds5, 150, 300);

		play(E5, 150, 300);
		play(G4, 100, 150);
		play(A4, 100, 150);

		play(B4, 100, 300);
		play(A4, 100, 150);
		play(B4, 100, 100);
		play(D5, 100, 425);
		// End Insert 2
		}

		System.out.println("End.");
		Button.ESCAPE.waitForPressAndRelease();
	}

	private static void play(int freq, int dur, int delay) {
		if (100 <= freq  && freq  <= 12000 &&
			10  <= dur   && dur   <= 10000 &&
			10  <= delay && delay <= 10000) {
			Sound.playTone(freq, dur);
			try {
				Thread.sleep(delay);
			}
			catch (Exception e) {}
		}
	}
}
