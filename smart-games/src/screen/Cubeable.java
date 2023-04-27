package screen;

import java.util.Vector;

// Egy játéknak ezt az interfacet kell megvalósítania
/**
 * Ahhoz, hogy egy játékot (kockát?) ki tudjunk tenni a kéepernyőre, ezekre a függvényekre van szűkség
 * @author slenk
 *
 */
public interface Cubeable
{
	public void init();
	public void restart();
	public int getCnt();
	public int getSize();
	public int getSec();
	public void setSec(int sec);
	public boolean checkActTable();
	public byte [] getActTable();
    public String [] getColors();
    public void forgat( String name );
}
