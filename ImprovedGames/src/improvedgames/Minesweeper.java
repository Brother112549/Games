package improvedgames;
/*Created By Kelby
Jan 28, 2018
*/

//TO DO: add something for losing

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;
public class Minesweeper 
{

    //selection options
    private int height;
    private int width;
    private int bombs;
    //active variables when playing
    private JLabel BmBs;        //label for numbmbs and numRevd
    private TimerLabel Time;    //time taken to solve
    private int numclks;        //used to ensure valid first action only
    private int numbmbs;        //number of unflagged bombs
    private int numRevd;        //number of revealed cells
    private Cell[][] Cells;
    private boolean win = true;
    JFrame frame;
    
    public Minesweeper()
    {
        
    }
    
    public void SetUp(JFrame frame){
        frame.setContentPane(new JPanel());
        height = 0;
        width = 0;
        bombs = 0;
        
        frame.setTitle("Games - Minesweeper");
        final String ht = "Board Height: ";
        JSlider Height = new JSlider(10, 50, 10);
        JLabel cHeight = new JLabel(ht + Integer.toString(Height.getValue()));
        final String wd = "Board Width: ";
        JSlider Width = new JSlider(10, 100, 10);
        JLabel cWidth = new JLabel(wd + Integer.toString(Width.getValue()));
        final String bb = "Bombs: ";
        JSlider Bombs = new JSlider(10, 100, 10);
        JLabel cBombs = new JLabel(bb + Integer.toString(Bombs.getValue()));
        JButton Confirm = new JButton("Confirm");
        JPanel options = new JPanel();
        options.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        options.add(cHeight, c);
        c.gridx = 1;
        options.add(Height, c);
        c.gridy = 1;
        c.gridx = 0;
        options.add(cWidth, c);
        c.gridx = 1;
        options.add(Width, c);
        c.gridy = 2;
        c.gridx = 0;
        options.add(cBombs, c);
        c.gridx = 1;
        options.add(Bombs, c);
        c.gridy = 3;
        c.gridx = 0;
        c.gridwidth = 2;
        options.add(Confirm, c);
        
        //sliders actively display values
        Height.addChangeListener((ChangeEvent e) -> {
            cHeight.setText(ht + Integer.toString(Height.getValue()));
            Bombs.setMaximum((Height.getValue()*Width.getValue()*6/10));    //maximum of 60% bombs
            cBombs.setText(bb + Integer.toString(Bombs.getValue()));
            frame.pack();
        });
        Width.addChangeListener((ChangeEvent e) -> {
            cWidth.setText(wd + Integer.toString(Width.getValue()));
            Bombs.setMaximum((Height.getValue()*Width.getValue()*6/10));    //maximum of 60% bombs
            cBombs.setText(bb + Integer.toString(Bombs.getValue()));
            frame.pack();
        });
        Bombs.addChangeListener((ChangeEvent e) -> {
            cBombs.setText(bb + Integer.toString(Bombs.getValue()));
            frame.pack();
        });
        Confirm.addActionListener((ActionEvent e) -> {
            width = Width.getValue();
            height = Height.getValue();
            bombs = Bombs.getValue();
            Game(frame);
        });
        frame.add(options);
        frame.pack();
    }
    
    private void Game(JFrame jframe){
        //Set up Game Board
        frame = jframe;
        frame.setContentPane(new JPanel());
        numbmbs = bombs;
        numclks = 0;
        numRevd = 0;
        JPanel board = new JPanel();
        board.setLayout(new GridLayout(height, width));
        Cells = new Cell[height][width];
        for (int h = 0; h < height; h++){
            for (int w = 0; w < width; w++){
                Cell acell = new Cell();
                acell.setPos(h, w);
                Cells[h][w] = acell;
                board.add(acell.getButton());
            }
        }
        board.setPreferredSize(new Dimension(42*width, 42*height));
        
        //Header for timer, reset button and bomb counter
        JPanel Head = new JPanel();
        Head.setLayout(new GridBagLayout());
        GridBagConstraints Hc = new GridBagConstraints();
        Hc.fill = GridBagConstraints.NONE;
        Hc.insets = new Insets(10,10,0,10);
        Hc.anchor = GridBagConstraints.LINE_START;
        Hc.weightx = 1;
        Hc.gridx = 0;
        Hc.gridy = 0;
        BmBs = new JLabel("Mines: " + numbmbs + ", Revealed: " + numRevd);
        Head.add(BmBs, Hc);
        Hc.anchor = GridBagConstraints.LINE_END;
        Hc.gridx = 2;
        Time = new TimerLabel();
        Time.setString("Time Taken: ");
        Head.add(Time, Hc);
        JButton CoolGuy = new JButton("Reset and Refresh");
        CoolGuy.setPreferredSize(new Dimension(150, 40));
        CoolGuy.addActionListener((ActionEvent e) -> {
            Game(frame);
        });
        Hc.anchor = GridBagConstraints.CENTER;
        Hc.gridx = 1;
        Head.add(CoolGuy, Hc);
        
        //add Game board and Header to Frame
        JPanel pan = new JPanel();
        pan.setLayout(new BorderLayout(0, 10));
        pan.add(board, BorderLayout.CENTER);
        pan.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        JScrollPane Scroll = new JScrollPane();
        Scroll.setColumnHeaderView(Head);
        Scroll.setViewportView(pan);
        frame.add(Scroll);
        frame.setResizable(true);
        frame.pack();
        
        //sizing for scroll bars
        if(frame.getHeight() >= Toolkit.getDefaultToolkit().getScreenSize().height || frame.getWidth() >= Toolkit.getDefaultToolkit().getScreenSize().width){
            frame.setSize(frame.getWidth()+25, frame.getHeight()+25);
        } else {
            frame.setSize(frame.getWidth()+10, frame.getHeight()+10);
        }
        frame.setMaximizedBounds(null);
        
        //make frame into a layered type pane
        JDesktopPane Desk = new JDesktopPane();
        JInternalFrame Int = new JInternalFrame();
        Desk.setPreferredSize(frame.getSize());
        Desk.setOpaque(false);
        Int.setContentPane(Scroll);
        Int.setVisible(true);
        Desk.add(Int, JLayeredPane.DEFAULT_LAYER);
        frame.setContentPane(Desk);
        Int.setBorder(null);
        BasicInternalFrameUI bi = (BasicInternalFrameUI)Int.getUI();
        bi.setNorthPane(null);
        try {
            Int.setMaximum(true);
        } catch (PropertyVetoException ex) {
        }
        
        //Rules mines up
        for (int i = 0; i < bombs; i++) {
            Random rand = new Random();
            int row = rand.nextInt(height);
            int col = rand.nextInt(width);
            while(Cells[row][col].isMine) { 
                row = rand.nextInt(height);
                col = rand.nextInt(width);
            }
            Cells[row][col].setMine();
        }
    }
    
    private void Ender(){
        //make cells final
        for(Cell[] cella: Cells){
            for(Cell cell: cella){
                cell.EndGame();
            }
        }
        
        //ending screen
        JPanel options = new JPanel();
        options.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 3;
        c.insets = new Insets(10,10,10,10);
        c.gridx = 0;
        c.gridy = 0;
        JLabel End = new JLabel();
        if(win){
            End.setText("You Won!");
        }else{
            End.setText("You Lost");
        }
        End.setFont(new Font("Arial", Font.PLAIN, 60));
        End.setForeground(Color.red);
        options.add(End, c);
        c.gridwidth = 1;
        c.gridy = 1;
        options.add(new JLabel("Do you want to play again?"), c);
        c.gridx = 1;
        JButton Yes = new JButton("Yes");
        Yes.addActionListener((ActionEvent e) -> {
            SetUp(frame);
        });
        options.add(Yes, c);
        c.gridx = 2;
        JButton No = new JButton("No");
        No.addActionListener((ActionEvent e) -> {
            new ImprovedGames().StartScreen(frame);
        });
        options.add(No, c);
        
        JInternalFrame JInF = new JInternalFrame();
        JInF.add(options);
        JInF.pack();
        JInF.setLocation(frame.getWidth()/2-JInF.getWidth()/2, frame.getHeight()/2-JInF.getHeight()/2);
        JInF.setVisible(true);
        BasicInternalFrameUI Bi = (BasicInternalFrameUI)JInF.getUI();
        Bi.setNorthPane(null);
        frame.getContentPane().add(JInF, JLayeredPane.DRAG_LAYER);
        
        try {
            JInF.setSelected(true);
        } catch (PropertyVetoException ex) {
        }
    }
    
    
    private class Cell {
        
        private JButton buton;
        private int Hpos;                   //height position
        private int Wpos;                   //width position
        private boolean isMine = false;
        private boolean isRevled = false;
        private boolean isFlaged = false;
        private int Near = 0;               //number of bombs in adjacent cells
        private MouseListener Listen;

        public Cell() {
            
        }
        
        private void setPos(int h, int w) {
            Hpos = h;
            Wpos = w;
        }

        private void setMine() {
            isMine = true;
        }
        
        private JButton getButton(){
            buton = new JButton("x");
            buton.setBackground(Color.GRAY);
            Listen = new MouseListener() {      //Mouse Listener implementation
                @Override public void mousePressed(MouseEvent e) {}
                @Override public void mouseReleased(MouseEvent e) {}
                @Override public void mouseEntered(MouseEvent e) {}
                @Override public void mouseExited(MouseEvent e) {}
                @Override public void mouseClicked(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e)){
                        if(numclks == 0){
                            if(isMine){
                                FirstClick();
                            }
                            Time.Starter();
                            Reveal();
                            numclks ++;
                        }else if(isFlaged){
                            //do nothing
                        }else if(isRevled){
                            Checker();
                        }else if(isMine){
                            RevealedMine();
                        }else{
                            Reveal();
                        }
                        CheckWin();
                    }
                    if(SwingUtilities.isRightMouseButton(e)){
                        if(numclks == 0){
                            //do nothing
                        }else if(isFlaged){
                            unFlag();
                        }else if(!isRevled){
                            try {
                                setFlag();
                            } catch (IOException ex) {
                            }
                        }
                    }
                }
            };
            buton.addMouseListener(Listen);
            return buton;
        }
        
        private void setFlag() throws IOException {
            isFlaged = true;
            numbmbs --;
            BmBs.setText("Mines: " + numbmbs + ", Revealed: " + numRevd);
            buton.setText(null);
            Image image = (ImageIO.read(getClass().getResource("/improvedgames/Images/Minesweeper Flag.jpg")));
            buton.setIcon(new ImageIcon(image.getScaledInstance(40, 40, Image.SCALE_DEFAULT)));                                                //sets Flag image on Cell
        }
        
        private void unFlag(){
            isFlaged = false;
            numbmbs ++;
            BmBs.setText("Mines: " + numbmbs + ", Revealed: " + numRevd);
            buton.setIcon(null);
            buton.setText("x");
        }
        
        //reveals number of mines touching cell
        private void Reveal(){
            buton.setBackground(Color.LIGHT_GRAY);
            isRevled = true;
            numRevd ++;
            BmBs.setText("Mines: " + numbmbs + ", Revealed: " + numRevd);
            for(int u = (Hpos - 1); u <= (Hpos + 1); u++){
                for(int s = (Wpos - 1); s <= (Wpos + 1); s++){
                    if(u != -1 && u != height && s != -1 && s != width){
                        if(Cells[u][s].isMine){
                            Near ++;
                        }
                    }
                }
            }
            
            if(Near==0){
                //chain reveals when cell is touching no mines
                for(int u = (Hpos - 1); u <= (Hpos + 1); u++){
                    for(int s = (Wpos - 1); s <= (Wpos + 1); s++){
                        if(u != -1 && u != height && s != -1 && s != width){
                            if(!Cells[u][s].isRevled){
                                Cells[u][s].Reveal();
                            }
                        }
                    }
                }
                buton.setText(" ");
            }else if(Near==1){
                buton.setForeground(Color.BLUE);
                buton.setText(String.valueOf(Near));
            }else if(Near==2){
                buton.setForeground(Color.GREEN);
                buton.setText(String.valueOf(Near));
            }else if(Near==3){
                buton.setForeground(Color.RED);
                buton.setText(String.valueOf(Near));
            }else if(Near==4){
                buton.setForeground(Color.BLACK);
                buton.setText(String.valueOf(Near));
            }else if(Near==5){
                buton.setForeground(Color.CYAN);
                buton.setText(String.valueOf(Near));
            }else if(Near==6){
                buton.setForeground(Color.DARK_GRAY);
                buton.setText(String.valueOf(Near));
            }else if(Near==7){
                buton.setForeground(Color.YELLOW);
                buton.setText(String.valueOf(Near));
            }else if(Near==8){
                buton.setForeground(Color.MAGENTA);
                buton.setText(String.valueOf(Near));
            }
        }
        
        private void FirstClick(){
            boolean Looking = true;
            for (int h = 0; h < height; h++){
                for (int w = 0; w < width; w++){
                    if((!Cells[h][w].isMine)&&Looking){
                        Cells[h][w].setMine();
                        Looking = false;
                    }
                }
            }
            isMine = false;
        }
        
        private void CheckWin(){
            if(!win){
                //lost
            }else if(((height*width)-numRevd) > bombs){
                //still playing
            }else{
                Time.Stopper();
                Ender();
            }
        }
        
        private void RevealedMine(){
            buton.setBackground(Color.RED);
                                                                //add something for losing
            win = false;
            Time.Stopper();
            Ender();
        }
        
        //reveals neighbors when correct number of mines are flagged
        private void Checker(){
            int flagged = 0;
            for(int u = (Hpos - 1); u <= (Hpos + 1); u++){
                for(int s = (Wpos - 1); s <= (Wpos + 1); s++){
                    if(u != -1 && u != height && s != -1 && s != width){
                        if(Cells[u][s].isFlaged){
                            flagged ++;
                        }
                    }
                }
            }
            if(flagged == Near){
                for(int u = (Hpos - 1); u <= (Hpos + 1); u++){
                    for(int s = (Wpos - 1); s <= (Wpos + 1); s++){
                        if(u != -1 && u != height && s != -1 && s != width){
                            if(Cells[u][s].isMine&&!Cells[u][s].isFlaged){
                                Cells[u][s].RevealedMine();
                            }else if(!Cells[u][s].isRevled&&!Cells[u][s].isFlaged){
                                Cells[u][s].Reveal();
                            }
                        }
                    }
                }
            }
        }

        private void EndGame() {
            buton.removeMouseListener(Listen);
            buton.setEnabled(false);
        }
        
    }
    
    
    private class TimerLabel extends JLabel implements ActionListener{
        
        private int time = 0;
        private String lead = null;
        private final Timer timer;
        
        
        private TimerLabel(){
            time = 0;
            timer = new Timer(1000, this);
            timer.setInitialDelay(0);
        }
        
        private void setString(String words){
            lead = words;
            setText(lead);
        }
        
        private void Starter(){
            timer.start();
        }
        
        private void Stopper(){
            timer.stop();
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            updateTimer();
        }

        private void updateTimer() {
            setText(lead + Integer.toString(time++));
        }
        
    }

}