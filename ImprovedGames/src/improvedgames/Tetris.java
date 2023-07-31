package improvedgames;
/*Created By Kelby
Apr 16, 2018
*/
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;
public class Tetris 
{

    private JFrame frame;
    private int OCntr = 0;
    private int ICntr = 0;
    private int TCntr = 0;
    private int LCntr = 0;
    private int ZCntr = 0;
    private int JCntr = 0;
    private int SCntr = 0;
    private JLabel Oc;
    private JLabel Ic;
    private JLabel Tc;
    private JLabel Lc;
    private JLabel Zc;
    private JLabel Jc;
    private JLabel Sc;
    private int Score = 0;
    private JLabel ScoreCntr;
    private ArrayList<JLabel[]> board;
    private ArrayList<JLabel[]> next;
    private final String[][][] Options = {{
            {" ", "x", "x", " "},
            {" ", "x", "x", " "},
            {" ", " ", " ", " "},
            {" ", " ", " ", " "}}, 
        {
            {" ", " ", " ", " "},
            {"x", "x", "x", "x"},
            {" ", " ", " ", " "},
            {" ", " ", " ", " "}}, 
        {
            {" ", "x", " ", " "},
            {"x", "x", "x", " "},
            {" ", " ", " ", " "},
            {" ", " ", " ", " "}}, 
        {
            {" ", " ", "x", " "},
            {"x", "x", "x", " "},
            {" ", " ", " ", " "},
            {" ", " ", " ", " "}}, 
        {
            {"x", "x", " ", " "},
            {" ", "x", "x", " "},
            {" ", " ", " ", " "},
            {" ", " ", " ", " "}}, 
        {
            {"x", " ", " ", " "},
            {"x", "x", "x", " "},
            {" ", " ", " ", " "},
            {" ", " ", " ", " "}}, 
        {
            {" ", "x", "x", " "},
            {"x", "x", " ", " "},
            {" ", " ", " ", " "},
            {" ", " ", " ", " "}}};
    private int nxt;
    private Color nxtcolor;
    private int cntdwn = 3;
    private Timer start;
    private double delay = 1000;
    private ArrayList<JLabel[]> active;
    private int activeht;
    private KeyEventDispatcher Keys;
    private int activewt;
    private int actshp;
    
    public Tetris() {
        
    }

    public void Play(JFrame jframe) {
        frame = jframe;
        JPanel pane = new JPanel();
        frame.setContentPane(new JLayeredPane());
        frame.getContentPane().add(pane, JLayeredPane.DEFAULT_LAYER);
        frame.getContentPane().setPreferredSize(new Dimension(400, 580));
        pane.setBounds(0, 0, 400, 580);
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);
        
        
        //Score
        ScoreCntr = new JLabel();
        ScoreCntr.setText("Score: " + Integer.toString(Score));
        c.weightx = 5;
        c.fill = GridBagConstraints.BOTH;
        pane.add(ScoreCntr, c);
        
        
        //Pause Button
        final JButton Pause = new JButton("Pause");
        Pause.setPreferredSize(new Dimension(120,40));
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.weightx = 0;
        c.anchor = GridBagConstraints.EAST;
        pane.add(Pause, c);
        Pause.addActionListener((ActionEvent e) -> {
            if(start.isRunning()){
                start.stop();
                Pause.setText("Resume");
            }else{
                start.start();
                Pause.setText("Pause");
            }
        });
        
        
        //Piece That is up next
        JPanel UPNXT = new JPanel();
        UPNXT.setLayout(new GridLayout(4, 4, 1, 1));
        UPNXT.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2), "Next:", TitledBorder.LEFT, TitledBorder.ABOVE_TOP));
        next = new ArrayList<>();
        for(int a = 0; a < 4; a++){
            next.add(new JLabel[4]);
            for(int b = 0; b < 4; b++){
                next.get(a)[b] = new JLabel();
                next.get(a)[b].setPreferredSize(new Dimension(25,25));
                next.get(a)[b].setBackground(Color.BLACK);
                next.get(a)[b].setOpaque(true);
                UPNXT.add(next.get(a)[b]);
            }
        }
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        pane.add(UPNXT, c);
        
        
        
        //count of pieces box
        JPanel counter = new JPanel();
        counter.setLayout(new GridBagLayout());
        GridBagConstraints C = new GridBagConstraints();
        C.fill = GridBagConstraints.NONE;
        C.gridheight = 1;
        C.gridwidth = 2;
        C.insets = new Insets(3, 3, 3, 3);
        C.gridx = 0;
        C.gridy = 0;
        C.weightx = 1;
        C.anchor = GridBagConstraints.WEST;
        //JLabels need images of pieces
        JLabel OC = new JLabel("Os");
        counter.add(OC, C);
        C.gridy = 1;
        JLabel IC = new JLabel("Is");
        counter.add(IC, C);
        C.gridy = 2;
        JLabel TC = new JLabel("Ts");
        counter.add(TC, C);
        C.gridy = 3;
        JLabel LC = new JLabel("Ls");
        counter.add(LC, C);
        C.gridy = 4;
        JLabel JC = new JLabel("Zs");
        counter.add(JC, C);
        C.gridy = 5;
        JLabel ZC = new JLabel("Js");
        counter.add(ZC, C);
        C.gridy = 6;
        JLabel SC = new JLabel("Ss");
        counter.add(SC, C);
        //Active JLabels that count num of pieces
        C.gridwidth = 1;
        C.weightx = 0;
        C.gridx = 2;
        C.gridy = 0;
        Oc = new JLabel(" x"+OCntr);
        counter.add(Oc, C);
        C.gridy = 1;
        Ic = new JLabel(" x"+ICntr);
        counter.add(Ic, C);
        C.gridy = 2;
        Tc = new JLabel(" x"+TCntr);
        counter.add(Tc, C);
        C.gridy = 3;
        Lc = new JLabel(" x"+LCntr);
        counter.add(Lc, C);
        C.gridy = 4;
        Zc = new JLabel(" x"+ZCntr);
        counter.add(Zc, C);
        C.gridy = 5;
        Jc = new JLabel(" x"+JCntr);
        counter.add(Jc, C);
        C.gridy = 6;
        Sc = new JLabel(" x"+SCntr);
        counter.add(Sc, C);
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        pane.add(counter, c);
        c.fill = GridBagConstraints.NONE;
        
        
        //Board of 10col and 20rows
        JPanel Board = new JPanel();
        JLayeredPane Overlay = new JLayeredPane();
        Overlay.add(Board, JLayeredPane.DEFAULT_LAYER);
        Overlay.setPreferredSize(new Dimension(260,520));
        Overlay.setOpaque(false);
        Board.setSize(260, 510);
        Board.setLocation(0, 0);
        Board.setLayout(new GridLayout(20, 10, 1, 1));
        Board.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        board = new ArrayList<>();
        for(int a = 0; a < 20; a++){
            board.add(new JLabel[10]);
            for(int b = 0; b < 10; b++){
                board.get(a)[b] = new JLabel();
                board.get(a)[b].setPreferredSize(new Dimension(25,25));
                board.get(a)[b].setBackground(Color.BLACK);
                board.get(a)[b].setOpaque(true);
                Board.add(board.get(a)[b]);
            }
        }
        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 2;
        c.weightx = 5;
        pane.add(Overlay, c);
        
        
        frame.pack();
        
        
        //Set up Game
        SetNext();
        JPanel Over = new JPanel();
        Over.setOpaque(false);
        final JTextArea Text = new JTextArea();
        Text.setBackground(new Color(0,0,0,0));
        Text.setBorder(null);
        Text.setFont(new Font("Arial",Font.BOLD,30));
        Text.setForeground(Color.GREEN);
        Text.setText("\nGame Starts In\n\n"
                + "            " + cntdwn);
        Over.add(Text);
        Over.setPreferredSize(Board.getPreferredSize());
        Over.setBounds(0, 0, 260, 510);
        Overlay.add(Over, JLayeredPane.DRAG_LAYER);
        ActionListener advnce = (ActionEvent e) -> {        //Used to advance the game
            Advance();
        };
        Keys = (KeyEvent e) -> {
            if(e.getID() == KeyEvent.KEY_PRESSED){
                if(e.getKeyCode()==KeyEvent.VK_UP){
                    Rotate(true);
                }else if(e.getKeyCode()==KeyEvent.VK_DOWN){
                    Rotate(false);
                }else if(e.getKeyCode()==KeyEvent.VK_LEFT){
                    HorShift(-1);
                }else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                    HorShift(1);
                }else if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    Score ++;
                    Advance();
                }
            }
            return true;
        };
        ActionListener starter = new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if(cntdwn == 0){
                    start.removeActionListener(this);
                    start.addActionListener(advnce);
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(Keys);
                    Board.setFocusable(true);
                    Text.setText(null);
                    UseNext();
                }else{
                    cntdwn --;
                    Text.setText("\nGame Starts In\n\n"
                            + "            " + cntdwn);
                }
            }
        };
        start = new Timer((int) delay, starter);
        start.start();
    }
    
    private void SetNext(){
        nxt = (int) (Math.random() * 7);
        //set color
        if(nxt==0){
            nxtcolor = Color.CYAN;
        }else if(nxt==1){
            nxtcolor = Color.WHITE;
        }else if(nxt==2){
            nxtcolor = Color.GRAY;
        }else if(nxt==3){
            nxtcolor = Color.BLUE;
        }else if(nxt==4){
            nxtcolor = Color.GREEN;
        }else if(nxt==5){
            nxtcolor = Color.RED;
        }else if(nxt==6){
            nxtcolor = Color.YELLOW;
        }
        
        
        //Reset next box
        for(int a = 0; a < 4; a++){
            for(int b = 0; b < 4; b++){
                next.get(a)[b].setBackground(Color.BLACK);
            }
        }
        //Display next shape
        for(int a = 0; a < 4; a++){
            for(int b = 0; b < 4; b++){
                if("x".equals(Options[nxt][a][b])){
                    next.get(a)[b].setBackground(nxtcolor);
                }
            }
        }
    }
    
    private void UseNext(){
        //Count uses
        if(nxt==0){
            OCntr++;
            Oc.setText(" x"+OCntr);
        }else if(nxt==1){
            ICntr++;
            Ic.setText(" x"+ICntr);
        }else if(nxt==2){
            TCntr++;
            Tc.setText(" x"+TCntr);
        }else if(nxt==3){
            LCntr++;
            Lc.setText(" x"+LCntr);
        }else if(nxt==4){
            ZCntr++;
            Zc.setText(" x"+ZCntr);
        }else if(nxt==5){
            JCntr++;
            Jc.setText(" x"+JCntr);
        }else if(nxt==6){
            SCntr++;
            Sc.setText(" x"+SCntr);
        }
        
        active = new ArrayList<>();
        actshp = nxt;
        //move next to active
        for(int a = 0; a < 4; a++){
            active.add(new JLabel[4]);
            for(int b = 0; b < 4; b++){
                active.get(a)[b] = new JLabel();
                active.get(a)[b].setPreferredSize(new Dimension(25,25));
                active.get(a)[b].setOpaque(true);
                active.get(a)[b].setBackground(next.get(a)[b].getBackground());
            }
        }
        
        
        //move active onto top of board
        activeht = 0;
        activewt = 3;
        for(int a = 0; a < 4; a++){
            for(int b = 0; b < 4; b++){
                int B = b + activewt;
                if(board.get(a)[B].getBackground()==Color.BLACK){
                    board.get(a)[B].setBackground(active.get(a)[b].getBackground());
                }else if(active.get(a)[b].getBackground()==Color.BLACK){
                    //extra space so do nothing
                }else{
                    start.stop();
                    GameOver();
                }
            }
        }
        
        
        //set new next
        SetNext();
    }

    private void Advance() {
        boolean cgd = true;//can go down?
        cangodown:{
            for(int a = 0; a < 4; a++){
                int A = a + activeht + 1;
                for(int b = 0; b < 4; b++){
                    int B = b + activewt;
                    if(active.get(a)[b].getBackground()!=Color.BLACK){
                        int c;
                        for(c = 1; c + a < 4; c++){
                            if(active.get(a+c)[b].getBackground()!=Color.BLACK){
                                
                            }else{
                                c--;
                                break;
                            }
                        }
                        if(a+c>3){
                            c--;
                        }
                        if((A+c)<board.size()){
                            if(board.get(A+c)[B].getBackground()!=Color.BLACK){
                                cgd = false;
                                break cangodown;
                            }
                        }else if((A+c)==board.size()){
                            cgd = false;
                            break cangodown;
                        }
                    }
                }
            }
        }
        
        
        if(!cgd){//cannot go down
            //Check for line clear
            int lines = 0;
            int cleared = 0;
            for(int a = 0; a < board.size(); a++){
                boolean filled = true;
                for(int b = 0; b < 10; b++){
                    if(board.get(a)[b].getBackground()==Color.BLACK){
                        filled = false;
                        break;
                    }
                }
                if(filled){
                    lines ++;
                    cleared++;
                    Score += lines*100;
                    delay *= 0.995;
                    start.setDelay((int) delay);
                    for(int b = 0; b < 10; b++){
                        board.get(a)[b].setBackground(Color.BLACK);
                    }
                    for(; cleared > 0; cleared --){
                        for(int c = a; c > 0; c --){
                            for(int b = 0; b < 10; b++){
                                board.get(c)[b].setBackground(board.get(c-1)[b].getBackground());
                            }
                        }
                        for(int b = 0; b < 10; b++){
                            board.get(0)[b].setBackground(Color.BLACK);
                        }
                    }
                }
            }
            
            
            UseNext();
        }else{//go down
            Score ++;
            ScoreCntr.setText("Score: " + Integer.toString(Score));
            activeht ++;
            //Remove current
            for(int a = 0; a < 4; a++){
                int A = a + activeht;
                for(int b = 0; b < 4; b++){
                    int B = b + activewt;
                    if(active.get(a)[b].getBackground()!=Color.BLACK){
                        if(a>0 && active.get(a-1)[b].getBackground()!=Color.BLACK){
                            board.get(A-2)[B].setBackground(Color.BLACK);
                        }else if(a>1 && active.get(a-2)[b].getBackground()!=Color.BLACK){
                            board.get(A-3)[B].setBackground(Color.BLACK);
                        }else if(a>2 && active.get(a-3)[b].getBackground()!=Color.BLACK){
                            board.get(A-4)[B].setBackground(Color.BLACK);
                        }else{
                            board.get(A-1)[B].setBackground(Color.BLACK);
                        }
                    }
                }
            }
            
            
            //Add in next spot down
            for(int a = 0; a < 4; a++){
                int A = a + activeht;
                for(int b = 0; b < 4; b++){
                    int B = b + activewt;
                    if(A<board.size() && B>=0 && B<10){
                        if(board.get(A)[B].getBackground()==Color.BLACK){
                            board.get(A)[B].setBackground(active.get(a)[b].getBackground());
                        }
                    }
                }
            }
        }
    }
    
    private void HorShift(int LR){
        for(int a = 0; a < 4; a++){
            int A = a + activeht;
            for(int b = 0; b < 4; b++){
                int B = b + activewt;
                if(active.get(a)[b].getBackground()!=Color.BLACK){
                    board.get(A)[B].setBackground(Color.BLACK);
                }
            }
        }
        boolean Hor = true;
        Check:{
            for(int a = 0; a < 4; a++){
                int A = a + activeht;
                for(int b = 0; b < 4; b++){
                    int B = b + activewt + LR;
                    if(active.get(a)[b].getBackground()!=Color.BLACK){
                        if(B<0 || B>=10){
                            Hor = false;
                            break Check;
                        }else if(board.get(A)[B].getBackground()!=Color.BLACK){
                            Hor = false;
                            break Check;
                        }
                    }
                }
            }
        }
        if(Hor){
            activewt += LR;
        }
        for(int a = 0; a < 4; a++){
            int A = a + activeht;
            for(int b = 0; b < 4; b++){
                int B = b + activewt;
                if(active.get(a)[b].getBackground()!=Color.BLACK){
                    board.get(A)[B].setBackground(active.get(a)[b].getBackground());
                }
            }
        }
    }
    
    private void Rotate(boolean CW){
        //setup temp for rotation
        ArrayList<JLabel[]> Rotated = new ArrayList<>();
        for(int a = 0; a < 4; a++){
            Rotated.add(new JLabel[4]);
            for(int b = 0; b < 4; b++){
                Rotated.get(a)[b] = new JLabel();
                Rotated.get(a)[b].setPreferredSize(new Dimension(25,25));
                Rotated.get(a)[b].setBackground(Color.BLACK);
            }
        }
        if(actshp==0){
            //Boxes don't rotate
        }else if(actshp==1){
            //<editor-fold desc="lines rotate about 4x4 center">
            if(CW){
                for(int a = 0; a < 4; a++){
                    for(int b = 0; b < 4; b++){
                        Rotated.get(b)[3-a].setBackground(active.get(a)[b].getBackground());
                    }
                }
            }else{
                for(int a = 0; a < 4; a++){
                    for(int b = 3; b >= 0; b--){
                        Rotated.get(a)[b].setBackground(active.get(b)[3-a].getBackground());
                    }
                }
            }
            boolean canturn = true;
            boolean kick = true;
            //hide current for ease of checking
            for(int d = 0; d < 4; d++){
                int A = d + activeht;
                for(int e = 0; e < 4; e++){
                    int B = e + activewt;
                    if(active.get(d)[e].getBackground()!=Color.BLACK){
                        board.get(A)[B].setBackground(Color.BLACK);
                    }
                }
            }
            //check to see if it can turn
            Turn:{
                for(int a = 0; a < 4; a++){
                    int A = a + activeht;
                    for(int b = 0; b < 4; b++){
                        int B = b + activewt;
                        if(Rotated.get(a)[b].getBackground()!=Color.BLACK){
                            if(B<0 || A<0 || A>=board.size() || B>=board.get(A).length){
                                canturn = false;
                                break Turn;
                            }else if(board.get(A)[B].getBackground()!=Color.BLACK){
                                canturn = false;
                                break Turn;
                            }
                        }
                    }
                }
            }
            //if not check to see if it can turn and kick
            int kickx = 0;
            int kicky = 0;//negative is up on board
            if(!canturn){
                Kick:{
                    //kick cases and results modified from: http://tetris.wikia.com/wiki/SRS
                    for(int a = 0; a < 4; a++){
                        int A = a + activeht;
                        for(int b = 0; b < 4; b++){
                            int B = b + activewt;
                            if(Rotated.get(a)[b].getBackground()!=Color.BLACK){
                                if(A>=board.size()){//bottom obstruction
                                    kicky -= 1;
                                    if(B<0){//bottom left obstruction
                                        kickx += 1;
                                    }else if(B>=board.get(A-1).length){//bottom right obstruction
                                        kickx -= 1;
                                    }
                                }else if(B<0){
                                    kickx += 1;
                                    if(a>=2 && board.get(A)[0].getBackground()!=Color.BLACK){
                                        kicky -= 1;
                                    }
                                }else if(B>=board.get(A).length){
                                    kickx -= 1;
                                    if(a>=2 && board.get(A)[board.get(A).length-1].getBackground()!=Color.BLACK){
                                        kicky -= 1;
                                    }
                                }else if(b<=1 && board.get(A)[B].getBackground()!=Color.BLACK){//left edge obstruction
                                    kickx += 1;
                                    if(a>=2 && board.get(A)[B].getBackground()!=Color.BLACK){
                                        kicky -= 1;
                                    }
                                }else if(b>=2 && board.get(A)[B].getBackground()!=Color.BLACK){//right edge obstruction
                                    kickx -= 1;
                                    if(a>=2 && board.get(A)[B].getBackground()!=Color.BLACK){
                                        kicky -= 1;
                                    }
                                }
                            }
                        }
                    }
                    for(int a = 0; a < 4; a++){
                        int A = a + activeht + kicky;
                        for(int b = 0; b < 4; b++){
                            int B = b + activewt + kickx;
                            if(Rotated.get(a)[b].getBackground()!=Color.BLACK){
                                if(B<0 || A<0 || A>=board.size() || B>=board.get(A).length){
                                    kick = false;
                                    break Kick;
                                }else if(board.get(A)[B].getBackground()!=Color.BLACK){
                                    kick = false;
                                    break Kick;
                                }
                            }
                        }
                    }
                }
            }
            if(canturn){
                //if it can turn
                for(int a = 0; a < 4; a++){
                    for(int b = 0; b < 4; b++){
                        active.get(a)[b].setBackground(Rotated.get(a)[b].getBackground());
                    }
                }
                for(int a = 0; a < 4; a++){
                    int A = a + activeht;
                    for(int b = 0; b < 4; b++){
                        int B = b + activewt;
                        if(active.get(a)[b].getBackground()!=Color.BLACK){
                            board.get(A)[B].setBackground(active.get(a)[b].getBackground());
                        }
                    }
                }
            }else if(kick){
                //if it can kick
                for(int a = 0; a < 4; a++){
                    for(int b = 0; b < 4; b++){
                        active.get(a)[b].setBackground(Rotated.get(a)[b].getBackground());
                    }
                }
                activeht += kicky;
                activewt += kickx;
                for(int a = 0; a < 4; a++){
                    int A = a + activeht;
                    for(int b = 0; b < 4; b++){
                        int B = b + activewt;
                        if(active.get(a)[b].getBackground()!=Color.BLACK){
                            board.get(A)[B].setBackground(active.get(a)[b].getBackground());
                        }
                    }
                }
            }else{
                //if it can't turn or kick return to normal
                for(int d = 0; d < 4; d++){
                    int A = d + activeht;
                    for(int e = 0; e < 4; e++){
                        int B = e + activewt;
                        if(active.get(d)[e].getBackground()!=Color.BLACK){
                            board.get(A)[B].setBackground(active.get(d)[e].getBackground());
                        }
                    }
                }
            }//</editor-fold>
        }else{
            //<editor-fold desc="all other rotate about 3x3 center">
            if(CW){
                for(int a = 0; a < 3; a++){
                    for(int b = 0; b < 3; b++){
                        Rotated.get(b)[2-a].setBackground(active.get(a)[b].getBackground());
                    }
                }
            }else{
                for(int a = 0; a < 3; a++){
                    for(int b = 2; b >= 0; b--){
                        Rotated.get(a)[b].setBackground(active.get(b)[2-a].getBackground());
                    }
                }
            }
            boolean canturn = true;
            boolean kick = true;
            //hide current for ease of checking
            for(int a = 0; a < 4; a++){
                int A = a + activeht;
                for(int b = 0; b < 4; b++){
                    int B = b + activewt;
                    if(active.get(a)[b].getBackground()!=Color.BLACK){
                        board.get(A)[B].setBackground(Color.BLACK);
                    }
                }
            }
            //check to see if it can turn
            Turn:{
                for(int a = 0; a < 4; a++){
                    int A = a + activeht;
                    for(int b = 0; b < 4; b++){
                        int B = b + activewt;
                        if(Rotated.get(a)[b].getBackground()!=Color.BLACK){
                            if(B<0 || A<0 || A>=board.size() || B>=board.get(A).length){
                                canturn = false;
                                break Turn;
                            }else if(board.get(A)[B].getBackground()!=Color.BLACK){
                                canturn = false;
                                break Turn;
                            }
                        }
                    }
                }
            }
            //if not check to see if it can turn and kick
            int kickx = 0;
            int kicky = 0;//negative is up on board
            if(!canturn){
                Kick:{
                    //kick cases and results modified from: http://tetris.wikia.com/wiki/SRS
                    for(int a = 0; a < 3; a++){
                        int A = a + activeht;
                        for(int b = 0; b < 3; b++){
                            int B = b + activewt;
                            if(Rotated.get(a)[b].getBackground()!=Color.BLACK){
                                if(A>=board.size()){//bottom obstruction
                                    kicky = -1;
                                    if(B<0){//bottom left obstruction
                                        kickx = 1;
                                    }else if(B>=board.get(A-1).length){//bottom right obstruction
                                        kickx = -1;
                                    }
                                }else if(B<0){
                                    kickx = 1;
                                    if(a==2 && board.get(A)[B+1].getBackground()!=Color.BLACK){
                                        kicky = -1;
                                    }
                                }else if(B>=board.get(A).length){
                                    kickx = -1;
                                    if(a==2 && board.get(A)[B-1].getBackground()!=Color.BLACK){
                                        kicky = -1;
                                    }
                                }else if(b==0 && board.get(A)[B].getBackground()!=Color.BLACK){//left edge obstruction
                                    kickx = 1;
                                    if(a==2 && board.get(A)[B].getBackground()!=Color.BLACK){
                                        kicky = -1;
                                    }
                                }else if(b==2 && board.get(A)[B].getBackground()!=Color.BLACK){//right edge obstruction
                                    kickx = -1;
                                    if(a==2 && board.get(A)[B].getBackground()!=Color.BLACK){
                                        kicky = -1;
                                    }
                                }else if(b==1 && a==0 && board.get(A)[B].getBackground()!=Color.BLACK){//other obstruction
                                    kick = false;
                                    break Kick;
                                }
                            }
                        }
                    }
                    for(int a = 0; a < 3; a++){
                        int A = a + activeht + kicky;
                        for(int b = 0; b < 3; b++){
                            int B = b + activewt + kickx;
                            if(Rotated.get(a)[b].getBackground()!=Color.BLACK){
                                if(B<0 || A<0 || A>=board.size() || B>=board.get(A).length){
                                    kick = false;
                                    break Kick;
                                }else if(board.get(A)[B].getBackground()!=Color.BLACK){
                                    kick = false;
                                    break Kick;
                                }
                            }
                        }
                    }
                }
            }
            if(canturn){
                //if it can turn normally
                for(int a = 0; a < 4; a++){
                    for(int b = 0; b < 4; b++){
                        active.get(a)[b].setBackground(Rotated.get(a)[b].getBackground());
                    }
                }
                for(int a = 0; a < 4; a++){
                    int A = a + activeht;
                    for(int b = 0; b < 4; b++){
                        int B = b + activewt;
                        if(active.get(a)[b].getBackground()!=Color.BLACK){
                            board.get(A)[B].setBackground(active.get(a)[b].getBackground());
                        }
                    }
                }
            }else if(kick){
                //if it can kick and turn
                for(int a = 0; a < 4; a++){
                    for(int b = 0; b < 4; b++){
                        active.get(a)[b].setBackground(Rotated.get(a)[b].getBackground());
                    }
                }
                activeht += kicky;
                activewt += kickx;
                for(int a = 0; a < 4; a++){
                    int A = a + activeht;
                    for(int b = 0; b < 4; b++){
                        int B = b + activewt;
                        if(active.get(a)[b].getBackground()!=Color.BLACK){
                            board.get(A)[B].setBackground(active.get(a)[b].getBackground());
                        }
                    }
                }
            }else{
                //if it can't turn or kick, so return to normal
                for(int d = 0; d < 4; d++){
                    int A = d + activeht;
                    for(int e = 0; e < 4; e++){
                        int B = e + activewt;
                        if(active.get(d)[e].getBackground()!=Color.BLACK){
                            board.get(A)[B].setBackground(active.get(d)[e].getBackground());
                        }
                    }
                }
            }//</editor-fold>
        }
    }

    private void GameOver(){
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(Keys);
        JInternalFrame Over = new JInternalFrame();
        JPanel pane = new JPanel();
        Over.setContentPane(pane);
        pane.setLayout(new GridLayout(3, 1));
        JLabel Game = new JLabel("Game Over");
        Game.setFont(new Font("Arial",Font.BOLD,50));
        Game.setForeground(Color.RED);
        pane.add(Game);
        JLabel High = new JLabel("Score: " + Score);
        pane.add(High);
        JPanel playagain = new JPanel();
        playagain.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(5, 5, 5, 5);
        c.gridx = 0;
        c.gridy = 0;
        playagain.add(new JLabel("Do you want to play again?"), c);
        c.gridx = 1;
        JButton yes = new JButton("Yes");
        yes.addActionListener((ActionEvent e) -> {
            new Tetris().Play(frame);
        });
        playagain.add(yes, c);
        c.gridx = 2;
        JButton no = new JButton("No");
        no.addActionListener((ActionEvent e) -> {
            new ImprovedGames().StartScreen(frame);
        });
        playagain.add(no, c);
        Over.add(playagain);
        Over.pack();
        Over.setLocation(frame.getWidth()/2-Over.getWidth()/2, frame.getHeight()/2-Over.getHeight()/2);
        Over.setVisible(true);
        BasicInternalFrameUI Bi = (BasicInternalFrameUI)Over.getUI();
        Bi.setNorthPane(null);
        frame.getContentPane().add(Over, JLayeredPane.DRAG_LAYER);
        
        try {
            Over.setSelected(true);
        } catch (PropertyVetoException ex) {
        }
    }

}