package mozog;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import java.awt.Color;
 
class Kocka extends JFrame
{
	JButton MehetGomb = new JButton("Mehet");
	JButton VisszaGomb = new JButton("Vissza");
	SajatPanel panel1 = new SajatPanel();
	int x, y;
	Timer mehetIdozito;
	Timer visszaIdozito;
 
	Kocka()
	{
		mehetIdozito = new Timer (100, new MehetIdozito_Time());
		visszaIdozito = new Timer (100, new VisszaIdozito_Time());
 
		x = 5;
		y = 5;
		MehetGomb.addActionListener(new MehetGomb_Click());
		MehetGomb.setBounds(200, 300, 100, 30);
		VisszaGomb.addActionListener(new VisszaGomb_Click());
		VisszaGomb.setBounds(200, 340, 100, 30);
 
		panel1.setBounds(0,0, 200, 200);
 
		add(MehetGomb);
		add(VisszaGomb);
		add(panel1);
		setSize(800, 600);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	class MehetIdozito_Time implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(x<100)
				x++;
			else
				mehetIdozito.stop();
 
			repaint();
		}
	}
 
	class VisszaIdozito_Time implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(x>5)
				x--;
			else
				visszaIdozito.stop();
 
			repaint();
		}
	}
 
 
	class SajatPanel extends JPanel
	{
		public void paint(Graphics g)
		{
			g.setColor (Color.red);
			g.fillRect(x, y, 50, 50);
		}
	}
 
	class MehetGomb_Click implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			for(int i=0; i<30; i++)
			{
				mehetIdozito.start();
			}
		}
	}
	class VisszaGomb_Click implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			for(int i=0; i<30; i++)
			{
				visszaIdozito.start();
			}
		}
	}
 
	public static void main(String args[])
	{
		new Kocka();
	}
 
}