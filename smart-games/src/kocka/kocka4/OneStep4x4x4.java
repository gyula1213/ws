package kocka.kocka4;

import kocka.Kocka;
import kocka.OneStep;

/**
 * Kocka kirakása több lépésben
 * Ez az osztály az egy lépést leíró jellemzőket tartalmazza, illetve egy lépést hajt végre (próbál meg végrehajtani)
 * @author slenk
 *
 */
public class OneStep4x4x4 extends OneStep
{
	public OneStep4x4x4(String name, int[] target, String... commands) {
		super(name, target, commands);
	}

	@Override
	protected Kocka newKocka(int []act) {
		return new Kocka4x4x4(act);
	}
}
