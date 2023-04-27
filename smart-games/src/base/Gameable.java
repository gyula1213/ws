package base;

import java.util.Vector;

// Egy játéknak ezt az interfacet kell megvalósítania
public interface Gameable
{
    public boolean equals(Object game);	// Felül kell írni az Object függvényét, hogy ami logikailag egyforma az ugyanaz legyen
    public int hashCode();				// Felül kell írni az Object függvényét, hogy ami logikailag egyforma az ugyanaz legyen
	public int getId();
    public boolean isReady();
    public String getPrevStep();
    public Vector<Gameable> nextStages();
    /**
     * Megmondja, hogy ez az állapot a végső megfejtés szemőpontjából
     * sikerrel kecsegtet-e. megtartsuk-e a későbbiekre
     */
    public boolean isHopefulStage();
    /**
     * Megmondja, hogy az adott mélység kritikus-e
     * (általában empírikus úton derül ki)
     * Amikor ezt elérük, akkor megpróbálunk takarítani,
     * azaz csak az igéretes állapotokat tartjuk meg
     * @return
     */
    public boolean isCriticalDeep( int deep );
}
