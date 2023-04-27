package event;

import javax.swing.*;

class Window extends JFrame {
	private int cnt;
    private JFrame frame;

    Window() {
	    super();
	    frame = new JFrame();
	    JButton button = new JButton("Exit");
	    button.setBounds(150, 100, 90, 25);
	    frame.add(button);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    button.addActionListener(event -> buttonAction());
	    Center center = new Center();
	    frame.setSize(400, 300);
	    frame.setTitle("PJP - Penzes Java Programming");
	    center.setCenter(frame);
	    frame.setLayout(null);
	    frame.setVisible(true);
	}
	
	private void buttonAction() {
		cnt++;
		frame.setTitle(cnt + ". katt");
	    //System.exit(0);
    }
}