package improvedgames;
/*Created By Kelby
Jan 18, 2018
 */
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
public class ImprovedGames 
{
    public static void main(String[] args) 
    {
        //set up JFrame basics
        JFrame frame = new JFrame();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame = setFrameLook(frame);
        
        //show start screen
        new ImprovedGames().StartScreen(frame);
    }
    
    public static Timer timer;
    
    public static JFrame setFrameLook(JFrame frame){
        //<editor-fold defaultstate="collapsed" desc="Look and Feel - From JFrameForm">   /*
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        //</editor-fold>*/
        
        //set up menubar for jframe
        JMenuBar Menu = new JMenuBar();
        frame.setJMenuBar(Menu);
        JMenu File = new JMenu("File");
        Menu.add(File);
        JMenuItem Home = new JMenuItem("Home");
        Home.addActionListener((ActionEvent e) -> {
            new ImprovedGames().StartScreen(frame);
        });
        File.add(Home);
        JMenuItem About = new JMenuItem("About");
        About.addActionListener((ActionEvent e) -> {
            JDialog info = new JDialog();
            info.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(10,10,0,10);
            c.gridx = 0;
            c.gridy = 0;
            info.add(new JLabel("Created By Kelby Morrison"), c);
            c.insets = new Insets(0,10,10,10);
            c.gridy = 1;
            info.add(new JLabel("January 18, 2018"), c);
            info.setTitle("About");
            info.pack();
            info.setVisible(true);
            info.setResizable(false);
            info.setLocationRelativeTo(frame);
            //<editor-fold defaultstate="collapsed" desc="Auto Close Function">
            EventQueue.invokeLater(() -> {
                Toolkit.getDefaultToolkit().addAWTEventListener((AWTEvent event) -> {
                    Object source = event.getSource();
                    if (source instanceof Component) {
                        Component comp = (Component) source;
                        Window win = null;
                        if (comp instanceof Window) {
                            win = (Window) comp;
                        } else {
                            win = SwingUtilities.windowForComponent(comp);
                        }
                        if (win == info) {
                            timer.restart();
                        }
                    }
                }, AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK);

                info.addComponentListener(new ComponentListener(){
                    @Override public void componentHidden(ComponentEvent e) {}
                    @Override public void componentShown(ComponentEvent e) {}
                    @Override public void componentResized(ComponentEvent e) {}
                    @Override public void componentMoved(ComponentEvent e) {
                        timer.restart();
                    }
                });

                timer = new Timer(10000, (ActionEvent ev) -> {
                    info.dispose();
                });
                timer.start();
            });
            //</editor-fold>
        });
        File.add(About);
        File.add(new JSeparator());
        JMenuItem Close = new JMenuItem("Close");
        Close.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        Close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        File.add(Close);
        
        return frame;
    }

    public void StartScreen(JFrame frame) {
        frame.setTitle("Games");
        JPanel message = new JPanel();
        frame.setContentPane(message);
        message.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.insets = new Insets(10,10,5,10);
        c.gridx = 0;
        c.gridy = 0;
        message.add(new JLabel("What would you like to play?"), c);
        c.gridy = 1;
        c.insets = new Insets(5,10,10,10);
        JPanel options = new JPanel();
        message.add(options, c);
        options.setLayout(new FlowLayout());
        JButton TTT = new JButton("Tic Tac Toe");
        TTT.addActionListener((ActionEvent e) -> {
            new TicTacToe().Game(frame);
        });
        options.add(TTT);
        JButton SOL = new JButton("One Handed Solitaire");
        SOL.addActionListener((ActionEvent e) -> {
            new Solitaire().OneHanded(frame);
        });
        options.add(SOL);
        JButton BLK = new JButton("Blackjack");
        BLK.addActionListener((ActionEvent e) -> {
            new Blackjack().SetUp(frame);
        });
        options.add(BLK);
        JButton MINE = new JButton("Minesweeper");
        MINE.addActionListener((ActionEvent e) -> {
            new Minesweeper().SetUp(frame);
        });
        options.add(MINE);
        frame.pack();
    }
}