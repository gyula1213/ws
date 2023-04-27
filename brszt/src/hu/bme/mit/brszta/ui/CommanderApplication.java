package hu.bme.mit.brszta.ui;

import hu.bme.mit.brszta.networking.ServerSocketHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommanderApplication {
    private JPanel contentPane;
    private JButton drawCircleButton;
    private JButton drawRectangleButton;
    private JButton showTextButton;

    /**
     * Hogy a UI szál és a socket-en dolgozó háttérszál közötti szinkronizációval ne kelljen manuálisan foglalkozni,
     * egy egyszerű konkurrens-üzenetsorral küldünk át adatot a UI szálról a háttérszál számára. Ez az osztály
     * a metódusain belül megoldja a szinkronizálást
     */
    private ConcurrentLinkedQueue<Integer> messageQueue = new ConcurrentLinkedQueue<>();

    public CommanderApplication() {

        // Az első ActionListener-t még egy anoním osztállyal csináljuk: mint látható, ez sokkal szövegesebb, mint
        // egy lambda
        drawCircleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageQueue.add(0);
            }
        });

        // Második listener: lambda kifejezés. Ez ebben a formában egy olyan függvény, amely az ActionListener osztály
        // actionPerformed metódusának szignatúráját veszi fel, tehát void visszatérésű és egy ActionEvent típust kap
        // paraméterül
        drawRectangleButton.addActionListener((ActionEvent e) -> {
            messageQueue.add(1);
        });

        // Valójában így szokták használni a lambdákat, az e paraméter továbbra is ActionEvent típusú
        showTextButton.addActionListener(e -> {
            messageQueue.add(2);
        });

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Commander application");
        CommanderApplication app = new CommanderApplication();
        frame.setContentPane(app.contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // ablak középreigazítása (first hit on stackoverflow)
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);


        ServerSocketHandler handler = new ServerSocketHandler(app.messageQueue);
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

        frame.pack();
        frame.setVisible(true);
    }
}
