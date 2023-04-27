package main;

public class HighScore{
	public String name=null;
	public int Highscore=0;
	
	public HighScore(String pname, int pscore)
	{
		this.name=pname;
		this.Highscore=pscore;
	}
	
	public String print()
	{
		return (name+" "+String.valueOf(Highscore));
	}

}
