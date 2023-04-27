package alkesz;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Menu {
	
	private String highscorepath;
	private int communicationID;
	private int communicationRole;
	
	//TODO status enum
	//TODO functions
	private int gametime;
	//TODO player12,drinks
	
	
	public static void main(String[] args) {
		
		System.out.println("Hello World!");
//		Game game = new Game();
		
		JFrame window = new JFrame("Alkesz");		
		JFrame startScreen = new JFrame();
		
		JButton start = new JButton("Start");
		
		start.addActionListener(listener -> {
			startScreen.setVisible(false);
			GameScreen gs = new GameScreen(); 
			//window.setVisible(true);			
		});
		
		startScreen.getContentPane().add(start);		
		startScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startScreen.setTitle("ALKESZ MENU");		
		startScreen.setSize(490,600);
		startScreen.setResizable(false);		
		startScreen.setVisible(true);
	}
	
	
	

}
