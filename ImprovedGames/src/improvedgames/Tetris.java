package improvedgames;
/*Created By Kelby
Apr 16, 2018
*/
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
public class Tetris 
{

    int BoxCntr = 0;
    int LinCntr = 0;
    int TCntr = 0;
    int LCntr = 0;
    int ZCntr = 0;
    int RLCntr = 0;
    int RZCntr = 0;
    int Score = 0;
    
    public Tetris() {
        
    }

    void Play(JFrame frame) {
        JPanel pane = new JPanel();
        frame.setContentPane(pane);
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        
        //count of pieces box
        JPanel counter = new JPanel();
        counter.setLayout(new GridBagLayout());
        GridBagConstraints C = new GridBagConstraints();
        C.fill = GridBagConstraints.BOTH;
        C.gridheight = 1;
        C.gridwidth = 2;
        C.insets = new Insets(3, 3, 3, 3);
        C.gridx = 0;
        C.gridy = 0;
    //JLabels need images of pieces
        JLabel BoxC = new JLabel("Box");
        counter.add(BoxC, C);
        C.gridy = 1;
        JLabel LineC = new JLabel("Line");
        counter.add(LineC, C);
        C.gridy = 2;
        JLabel TC = new JLabel("Ts");
        counter.add(TC, C);
        C.gridy = 3;
        JLabel LC = new JLabel("Ls");
        counter.add(LC, C);
        C.gridy = 4;
        JLabel ZC = new JLabel("Zs");
        counter.add(ZC, C);
        C.gridy = 5;
        JLabel RLC = new JLabel("RLs");
        counter.add(RLC, C);
        C.gridy = 6;
        JLabel RZC = new JLabel("RZs");
        counter.add(RZC, C);
    //Active JLabels that count num of pieces
        C.gridwidth = 1;
        C.gridx = 1;
        C.gridy = 0;
        JLabel Boxc = new JLabel("x"+BoxCntr);
        counter.add(Boxc, C);
        C.gridy = 1;
        JLabel Linec = new JLabel("x"+LinCntr);
        counter.add(Linec, C);
        C.gridy = 2;
        JLabel Tc = new JLabel("x"+TCntr);
        counter.add(Tc, C);
        C.gridy = 3;
        JLabel Lc = new JLabel("x"+LCntr);
        counter.add(Lc, C);
        C.gridy = 4;
        JLabel Zc = new JLabel("x"+ZCntr);
        counter.add(Zc, C);
        C.gridy = 5;
        JLabel RLc = new JLabel("x"+RLCntr);
        counter.add(RLc, C);
        C.gridy = 6;
        JLabel RZc = new JLabel("x"+RZCntr);
        counter.add(RZc, C);
        
        
        //Piece That is up next
        JLabel UPNXT = new JLabel();
        
        
        //Score
        JLabel score = new JLabel();
        score.setText(Integer.toString(Score));
        
        
        //Board of 10w and 20h
        JPanel Board = new JPanel();
        
        
        //Pause Button
        JButton Pause = new JButton();
    }

}