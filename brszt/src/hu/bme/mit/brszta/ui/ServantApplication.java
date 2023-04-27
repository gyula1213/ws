package hu.bme.mit.brszta.ui;

import hu.bme.mit.brszta.networking.ClientSocketHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServantApplication {
    private JPanel contentPane;
    private JLabel connectionStatusLabel;
    private MyCanvas canvas;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Servant application");
        ServantApplication app = new ServantApplication();
        frame.setContentPane(app.contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ablak középreigazítása (first hit on stackoverflow)
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

        frame.pack();
        frame.setVisible(true);

        ClientSocketHandler handler = new ClientSocketHandler(app.canvas, app.connectionStatusLabel);
        Thread socketThread = new Thread(handler);
        socketThread.start();

        // a socket-et kezelő háttérszál folyamatosan fut és nem tud arról, hogy mikor zárult be (ha egyáltalán) a UI
        // ablak. Ezért ha az ablak épp "closing" állapotban van, állítsuk le a háttérszálat is.
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);

                handler.setRunning(false);
                try {
                    socketThread.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

}
