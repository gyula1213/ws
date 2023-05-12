package screen;

/**
 * Ahhoz, hogy egy játékot (kockát?) ki tudjunk tenni a kéepernyőre, ezekre a függvényekre van szűkség
 * @author slenk
 *
 */
public interface Cubeable
{
	public void initGame();
	public void init(String ... commands );
	public void restart();
	public int getCnt();
	public int getSize();
	public int getSec();
	public void setSec(int sec);
	public boolean checkActTable();
	public void setTarget( int [] target);
	public int [] getActTable();
    public String [] getColors();
    public void forgat( String name );
    public int [] getOriginal();
    public void setActTable(int n);
    
    	// Egy tábla (kirakási cél) jellemző adatai
    public String getName(int n);
    public int [] getTarget(int n);
    public String [] getCommands(int n);
    public int [] getOrig(int n);
    public int [] getEnd(int n);
    public String [] getResult(int n);
}
