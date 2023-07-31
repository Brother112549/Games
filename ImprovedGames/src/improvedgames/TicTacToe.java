package improvedgames;
/*Created By Kelby
Jan 28, 2018
New Version Created on Oct 14, 2022
*/
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class TicTacToe 
{

    private final JLabel Turn = new JLabel();   //Displays whose turn it is
    private JFrame frame;
    private JButton[][] Board;
    private static int mode;
    private int Depth = 9;                      //number of open spaces remaining on Board
    private boolean turn = false;               //true for O's turn and false for X's turn
    private ActionListener HvH;                 //humans are both X and O
    private ActionListener HvC;                 //human is X, Computer is O
    private ActionListener CvH;                 //Computer is X, human is O
    
    TicTacToe(){
        HvH = (ActionEvent e) -> {
            //set as x or o, so long as not already set
            if("".equals(((JButton) e.getSource()).getText())){
                if(!turn){
                    ((JButton) e.getSource()).setText("X");
                    turn = !turn;
                }else if(turn){
                    ((JButton) e.getSource()).setText("O");
                    turn = !turn;
                }
                
                //check for a winner
                int[][] State = new int[3][3];
                for(int r = 0; r < 3; r++){
                    for(int c = 0; c < 3; c++){
                        if(Board[r][c].getText().equals("X")){
                            State[r][c] = 1;
                        }else if(Board[r][c].getText().equals("O")){
                            State[r][c] = -1;
                        }
                    }
                }
                int win = winner(State);
                if(win != 0){
                    End(win);
                }else{      //if no winner set whose turn it is
                    if(turn){
                        Turn.setText("It is O's Turn");
                    }else{
                        Turn.setText("It is X's Turn");
                    }
                }
            }
        };
        
        HvC = (ActionEvent e) -> {
            if("".equals(((JButton) e.getSource()).getText())){
                if(!turn){
                    //Human's Turn
                    ((JButton) e.getSource()).setText("X");
                    turn = true;
                    Depth--;
                    
                    //check for a winner
                    int[][] State = new int[3][3];
                    for(int r = 0; r < 3; r++){
                        for(int c = 0; c < 3; c++){
                            if(Board[r][c].getText().equals("X")){
                                State[r][c] = 1;
                            }else if(Board[r][c].getText().equals("O")){
                                State[r][c] = -1;
                            }
                        }
                    }
                    int win = winner(State);
                    if(win != 0){
                        End(win);
                    }else{      //if no winner set whose turn it is
                        if(turn){
                            Turn.setText("It is O's Turn");
                        }else{
                            Turn.setText("It is X's Turn");
                        }
                        Turn.repaint();
                       
                        //Computer's Turn playing Os
                        int[] pos;
                        if(mode == 1){
                            pos = minimax(State, Depth, -1);
                        }else{
                            pos = alphabeta(State, Depth, -1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                        }
                        Board[pos[0]][pos[1]].setText("O");
                        Depth--;
                        turn = false;

                        //check for a winner
                        State = new int[3][3];
                        for(int r = 0; r < 3; r++){
                            for(int c = 0; c < 3; c++){
                                if(Board[r][c].getText().equals("X")){
                                    State[r][c] = 1;
                                }else if(Board[r][c].getText().equals("O")){
                                    State[r][c] = -1;
                                }
                            }
                        }
                        win = winner(State);
                        if(win != 0){
                            End(win);
                        }else{      //if no winner set whose turn it is
                            if(turn){
                                Turn.setText("It is O's Turn");
                            }else{
                                Turn.setText("It is X's Turn");
                            }
                            Turn.repaint();
                        }
                    }
                }else if(turn){
                    //currently Computer's Turn, so do nothing
                }
            }
        };
        
        CvH = (ActionEvent e) -> {
            if("".equals(((JButton) e.getSource()).getText())){
                if(!turn){
                    //currently Computer's Turn, so do nothing
                }else if(turn){
                    //Human's Turn
                    ((JButton) e.getSource()).setText("O");
                    turn = false;
                    Depth--;
                    
                    //check for a winner
                    int[][] State = new int[3][3];
                    for(int r = 0; r < 3; r++){
                        for(int c = 0; c < 3; c++){
                            if(Board[r][c].getText().equals("X")){
                                State[r][c] = 1;
                            }else if(Board[r][c].getText().equals("O")){
                                State[r][c] = -1;
                            }
                        }
                    }
                    int win = winner(State);
                    if(win != 0){
                        End(win);
                    }else{      //if no winner set whose turn it is
                        if(turn){
                            Turn.setText("It is O's Turn");
                        }else{
                            Turn.setText("It is X's Turn");
                        }
                        Turn.repaint();
                        
                        //Computer's Turn playing Os
                        int[] pos;
                        if(mode == 3){
                            pos = minimax(State, Depth, -1);
                        }else{
                            pos = alphabeta(State, Depth, -1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                        }
                        Board[pos[0]][pos[1]].setText("X");
                        Depth--;
                        turn = true;

                        //check for a winner
                        State = new int[3][3];
                        for(int r = 0; r < 3; r++){
                            for(int c = 0; c < 3; c++){
                                if(Board[r][c].getText().equals("X")){
                                    State[r][c] = 1;
                                }else if(Board[r][c].getText().equals("O")){
                                    State[r][c] = -1;
                                }
                            }
                        }
                        win = winner(State);
                        if(win != 0){
                            End(win);
                        }else{      //if no winner set whose turn it is
                            if(turn){
                                Turn.setText("It is O's Turn");
                            }else{
                                Turn.setText("It is X's Turn");
                            }
                            Turn.repaint();
                        }
                    }
                }
            }
        };
    }
    
    public void Game(JFrame jframe){
        //Ask player to select game mode
        frame = jframe;
        frame.setTitle("Games - Tic Tac Toe - Mode Selection");
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
        message.add(new JLabel("What method should the computer use?"), c);
        c.gridy = 1;
        c.insets = new Insets(5,10,10,10);
        JPanel options = new JPanel();
        message.add(options, c);
        options.setLayout(new FlowLayout());
        JButton hvh = new JButton("Human vs Human");
        hvh.addActionListener((ActionEvent e) -> {
            mode = 0;
            Setup();
        });
        options.add(hvh);
        JButton hvmmc = new JButton("Human vs Minimax Computer");
        hvmmc.addActionListener((ActionEvent e) -> {
            mode = 1;
            Setup();
        });
        options.add(hvmmc);
        JButton hvabc = new JButton("Human vs Alpha-Beta Computer");
        hvabc.addActionListener((ActionEvent e) -> {
            mode = 2;
            Setup();
        });
        options.add(hvabc);
        JButton mmcvh = new JButton("Minimax Computer vs Human");
        mmcvh.addActionListener((ActionEvent e) -> {
            mode = 3;
            Setup();
        });
        options.add(mmcvh);
        JButton abcvh = new JButton("Alpha-Beta Computer vs Human");
        abcvh.addActionListener((ActionEvent e) -> {
            mode = 4;
            Setup();
        });
        options.add(abcvh);
        frame.pack();
    }
    
    private void Setup() {
        //set up the ContentPane and internal frame
        JDesktopPane Desk = new JDesktopPane();
        frame.setContentPane(Desk);
        Desk.setOpaque(false);
        JInternalFrame Back = new JInternalFrame();
        Back.setVisible(true);
        Back.setBorder(null);
        BasicInternalFrameUI bi = (BasicInternalFrameUI)Back.getUI();
        bi.setNorthPane(null);
        Desk.add(Back, JLayeredPane.DEFAULT_LAYER);
        try {
            Back.setMaximum(true);
        } catch (PropertyVetoException ex) {
        }
        
        //set up header
        Container cont = Back.getContentPane();
        cont.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10,10,5,10);
        c.gridx = 0;
        c.gridy = 0;
        Turn.setText("It is X's Turn");
        cont.add(Turn, c);
        
        //set up board
        c.gridy = 1;
        c.insets = new Insets(5,10,10,10);
        JPanel board = new JPanel();
        board.setLayout(new GridLayout(3, 3, 10, 10));
        Board = new JButton[3][3];
        for (int h = 0; h < 3; h++){
            for (int w = 0; w < 3; w++){
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.PLAIN, 60));
                button.setPreferredSize(new Dimension(100,100));
                if(mode == 0){
                    button.addActionListener(HvH);
                }else if(mode <= 2){
                    button.addActionListener(HvC);
                }else if(mode > 2){
                    button.addActionListener(CvH);
                }
                Board[h][w] = button;
                board.add(button);
            }
        }
        cont.add(board, c);
        Back.pack();
        Desk.setPreferredSize(Back.getSize());
        frame.pack();
        frame.setTitle("Games - Tic Tac Toe");
        
        if(mode > 2){   //Computer goes first
            //starts with random choice
            int x = (int) (Math.random() * 3);
            int y = (int) (Math.random() * 3);
            Board[x][y].setText("X");
            Depth--;
            turn = true;
            Turn.setText("It is O's Turn");
        }
    }
    
    private int winner(int[][] state){
        //horizontal
        for(int r = 0; r < 3; r++){
            if(state[r][0] + state[r][1] + state[r][2] == 3){
                return 1;
            }else if(state[r][0] + state[r][1] + state[r][2] == -3){
                return 2;
            }
        }
        
        //vertical
        for(int c = 0; c < 3; c++){
            if(state[0][c] + state[1][c] + state[2][c] == 3){
                return 1;
            }else if(state[0][c] + state[1][c] + state[2][c] == -3){
                return 2;
            }
        }
        
        //diagonal
        if(state[0][0] + state[1][1] + state[2][2] == 3){
            return 1;
        }else if(state[0][0] + state[1][1] + state[2][2] == -3){
            return 2;
        }else if(state[0][2] + state[1][1] + state[2][0] == 3){
            return 1;
        }else if(state[0][2] + state[1][1] + state[2][0] == -3){
            return 2;
        }
        
        //cat scratch
        for(int r = 0; r < 3; r++){
            for(int c = 0; c < 3; c++){
                if(state[r][c] == 0){
                    return 0;
                }
            }
        }
        return 3;
    }
    
    //end Screen
    private void End(int winner) {
        //turn buttons off
        for (int h = 0; h < 3; h++){
            for (int w = 0; w < 3; w++){
                Board[h][w].setEnabled(false);
            }
        }
        
        //end message
        JPanel options = new JPanel();
        options.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.insets = new Insets(10,10,10,10);
        c.gridx = 0;
        c.gridy = 0;
        JLabel End = new JLabel();
        if(winner==1){
            End.setText("X is the Winner");
        }else if(winner==2){
            End.setText("O is the Winner");
        }else if(winner==3){
            End.setText("Cat's Scratch");
        }
        End.setFont(new Font("Arial", Font.PLAIN, 40));
        End.setForeground(Color.red);
        options.add(End, c);
        c.gridy = 1;
        options.add(new JLabel("Do you want to play Tic Tac Toe again?"), c);
        c.gridwidth = 1;
        c.weightx = 1;
        c.gridy = 2;
        JButton Yes = new JButton("Yes");
        Yes.addActionListener((ActionEvent e) -> {
            new TicTacToe().Game(frame);
        });
        options.add(Yes, c);
        c.gridx = 1;
        JButton No = new JButton("No");
        No.addActionListener((ActionEvent e) -> {
            new ImprovedGames().StartScreen(frame);
        });
        options.add(No, c);
        
        JInternalFrame POPUP = new JInternalFrame();
        POPUP.add(options);
        POPUP.pack();
        POPUP.setLocation(frame.getWidth()/2-POPUP.getWidth()/2-8, frame.getHeight()/2-POPUP.getHeight()/2-30);
        POPUP.setVisible(true);
        BasicInternalFrameUI Bi = (BasicInternalFrameUI)POPUP.getUI();
        Bi.setNorthPane(null);
        frame.getContentPane().add(POPUP, JLayeredPane.DRAG_LAYER);
        try {
            POPUP.setSelected(true);
        } catch (PropertyVetoException ex) {
        }
    }
    
    private int[] minimax(int[][] state, int depth, int player) {
        int[] best = new int[3];
        best[0] = -1;
        best[1] = -1;
        if(mode > 2){   //computer plays Xs
            if(player == 1){                //find max value for computer's turn
                best[2] = Integer.MIN_VALUE;
            }else{                          //find min value for human's turn
                best[2] = Integer.MAX_VALUE;
            }
            
            //Evaluate
            int win = winner(state);
            if(win == 1){
                int[] XWin = {-1, -1, (depth * 1) + 10};
                return XWin;
            }else if(win == 2){
                int[] OWin = {-1, -1, (depth * -1) - 10};
                return OWin;
            }else if(win == 3){
                int[] NoWin = {-1, -1, depth * -1};
                return NoWin;
            }
            
            //for each empty cell
            for(int r = 0; r < 3; r++){
                for(int c = 0; c < 3; c++){
                    if(state[r][c] == 0){
                        state[r][c] = player;
                        int[] score = minimax(state, depth - 1, -player);
                        state[r][c] = 0;
                        score[0] = r;
                        score[1] = c;
                    
                        if(player == 1){    //find max value for computer's turn
                            if(score[2] > best[2]){
                                best = Arrays.copyOf(score, 3);
                            }
                        }else{              //find min value for human's turn
                            if(score[2] < best[2]){
                                best = Arrays.copyOf(score, 3);
                            }
                        }
                    }
                }
            }
        }else{          //computer plays Os
            if(player == -1){               //find max value for computer's turn
                best[2] = Integer.MIN_VALUE;
            }else{                          //find min value for human's turn
                best[2] = Integer.MAX_VALUE;
            }
            
            //Evaluate
            int win = winner(state);
            if(win == 2){
                int[] OWin = {-1, -1, (depth * 1) + 10};
                return OWin;
            }else if(win == 1){
                int[] XWin = {-1, -1, (depth * -1) - 10};
                return XWin;
            }else if(win == 3){
                int[] NoWin = {-1, -1, depth * -1};
                return NoWin;
            }
            
            //for each empty cell
            for(int r = 0; r < 3; r++){
                for(int c = 0; c < 3; c++){
                    if(state[r][c] == 0){
                        state[r][c] = player;
                        int[] score = minimax(state, depth - 1, -player);
                        state[r][c] = 0;
                        score[0] = r;
                        score[1] = c;

                        if(player == -1){   //find max value for computer's turn
                            if(score[2] > best[2]){
                                best = Arrays.copyOf(score, 3);
                            }
                        }else{              //find min value for human's turn
                            if(score[2] < best[2]){
                                best = Arrays.copyOf(score, 3);
                            }
                        }
                    }
                }
            }
        }
        
        return best;
    }

    private int[] alphabeta(int[][] state, int depth, int player, int alphaMax, int betaMin) {
        int[] best = new int[3];
        best[0] = -1;
        best[1] = -1;
        if(mode > 2){   //computer plays Xs
            if(player == 1){                //find max value for computer's turn
                best[2] = Integer.MIN_VALUE;
            }else{                          //find min value for human's turn
                best[2] = Integer.MAX_VALUE;
            }
            
            //Evaluate
            int win = winner(state);
            if(win == 1){
                int[] XWin = {-1, -1, (depth * 1) + 10};
                return XWin;
            }else if(win == 2){
                int[] OWin = {-1, -1, (depth * -1) - 10};
                return OWin;
            }else if(win == 3){
                int[] NoWin = {-1, -1, depth * -1};
                return NoWin;
            }
            
            //for each empty cell
            for(int r = 0; r < 3; r++){
                for(int c = 0; c < 3; c++){
                    if(state[r][c] == 0){
                        state[r][c] = player;
                        int[] score = alphabeta(state, depth - 1, -player, alphaMax, betaMin);
                        state[r][c] = 0;
                        score[0] = r;
                        score[1] = c;
                    
                        if(player == 1){    //find max value for computer's turn
                            if(score[2] > best[2]){
                                best = Arrays.copyOf(score, 3);
                            }
                            if(best[2] >= betaMin){
                                return best;
                            }
                            if(alphaMax < best[2]){
                                alphaMax = best[2];
                            }
                        }else{              //find min value for human's turn
                            if(score[2] < best[2]){
                                best = Arrays.copyOf(score, 3);
                            }
                            if(best[2] <= alphaMax){
                                return best;
                            }
                            if(betaMin > best[2]){
                                betaMin = best[2];
                            }
                        }
                    }
                }
            }
        }else{          //computer plays Os
            if(player == -1){               //find max value for computer's turn
                best[2] = Integer.MIN_VALUE;
            }else{                          //find min value for human's turn
                best[2] = Integer.MAX_VALUE;
            }
            
            //Evaluate
            int win = winner(state);
            if(win == 2){
                int[] OWin = {-1, -1, (depth * 1) + 10};
                return OWin;
            }else if(win == 1){
                int[] XWin = {-1, -1, (depth * -1) - 10};
                return XWin;
            }else if(win == 3){
                int[] NoWin = {-1, -1, depth * -1};
                return NoWin;
            }
            
            //for each empty cell
            for(int r = 0; r < 3; r++){
                for(int c = 0; c < 3; c++){
                    if(state[r][c] == 0){
                        state[r][c] = player;
                        int[] score = minimax(state, depth - 1, -player);
                        state[r][c] = 0;
                        score[0] = r;
                        score[1] = c;

                        if(player == -1){   //find max value for computer's turn
                            if(score[2] > best[2]){
                                best = Arrays.copyOf(score, 3);
                            }
                            if(best[2] >= betaMin){
                                return best;
                            }
                            if(alphaMax < best[2]){
                                alphaMax = best[2];
                            }
                        }else{              //find min value for human's turn
                            if(score[2] < best[2]){
                                best = Arrays.copyOf(score, 3);
                            }
                            if(best[2] <= alphaMax){
                                return best;
                            }
                            if(betaMin > best[2]){
                                betaMin = best[2];
                            }
                        }
                    }
                }
            }
        }
        
        return best;
    }
    
}