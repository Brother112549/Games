package improvedgames;
/*Created By Kelby
Jan 28, 2018
*/
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
public class TicTacToe 
{

    private JFrame frame;
    private final JLabel Turn = new JLabel();
    private boolean y = true;
    private int trn = 0;
    private int win = 0;
    private Block[][] Board;
    
    public void Game(JFrame jframe){
        //set up the ContentPane and internal frame
        frame = jframe;
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
        Board = new Block[3][3];
        for (int h = 0; h < 3; h++){
            for (int w = 0; w < 3; w++){
                Block ablock = new Block();
                Board[h][w] = ablock;
                board.add(ablock.getButton());
            }
        }
        cont.add(board, c);
        Back.pack();
        Desk.setPreferredSize(Back.getSize());
        frame.pack();
        
        //set up game
        trn ++;
        if(trn%2!=0){
            y=false;
        }
        frame.setTitle("Games - Tic Tac Toe");
    }
    
    private void winner(){
        //horizontal
        if(Board[0][0].mark==Board[0][1].mark&&Board[0][0].mark==Board[0][2].mark&&Board[0][0].mark!=0){
            if(y){
                win = 1;
            }else{
                win = 2;
            }
        }else if(Board[1][0].mark==Board[1][1].mark&&Board[1][0].mark==Board[1][2].mark&&Board[1][0].mark!=0){
            if(y){
                win = 1;
            }else{
                win = 2;
            }
        }else if(Board[2][0].mark==Board[2][1].mark&&Board[2][0].mark==Board[2][2].mark&&Board[2][0].mark!=0){
            if(y){
                win = 1;
            }else{
                win = 2;
            }
        }
        //vertical
        if(Board[0][0].mark==Board[1][0].mark&&Board[1][0].mark==Board[2][0].mark&&Board[2][0].mark!=0){
            if(y){
                win = 1;
            }else{
                win = 2;
            }
        }else if(Board[0][1].mark==Board[1][1].mark&&Board[1][1].mark==Board[2][1].mark&&Board[2][1].mark!=0){
            if(y){
                win = 1;
            }else{
                win = 2;
            }
        }else if(Board[0][2].mark==Board[1][2].mark&&Board[1][2].mark==Board[2][2].mark&&Board[2][2].mark!=0){
            if(y){
                win = 1;
            }else{
                win = 2;
            }
        }
        
        //diagonal
        if(Board[0][0].mark==Board[1][1].mark&&Board[1][1].mark==Board[2][2].mark&&Board[0][0].mark!=0){
            if(y){
                win = 1;
            }else{
                win = 2;
            }
        }else if(Board[0][2].mark==Board[1][1].mark&&Board[1][1].mark==Board[2][0].mark&&Board[2][0].mark!=0){
            if(y){
                win = 1;
            }else{
                win = 2;
            }
        }
        
        //cat game
        if(win==0&&Board[0][0].mark!=0&&Board[0][1].mark!=0&&Board[0][2].mark!=0
                &&Board[1][0].mark!=0&&Board[1][1].mark!=0&&Board[1][2].mark!=0
                &&Board[2][0].mark!=0&&Board[2][1].mark!=0&&Board[2][2].mark!=0){
            win = 3;
        }
    }
    
    //end Screen
    private void End(){
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
        if(win==1){
            End.setText("X is the Winner");
        }else if(win==2){
            End.setText("O is the Winner");
        }else if(win==3){
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
    
    
    private class Block{
        
        private JButton but;
        private int mark = 0;
        
        private JButton getButton(){
            but = new JButton();
            but.setFont(new Font("Arial", Font.PLAIN, 60));
            but.setPreferredSize(new Dimension(100,100));
            but.addActionListener((ActionEvent e) -> {
                //set as x or o
                if(mark == 0){
                    if(!y){
                        but.setText("X");
                        mark = 1;
                        trn ++;
                        y = trn%2 == 0;
                    }else if(y){
                        but.setText("O");
                        mark = 2;
                        trn ++;
                        y = trn%2 == 0;
                    }
                }
                    
                //check for a winner
                winner();
                if(win != 0){
                    End();
                }else{      //if no winner set whose turn it is
                    if(y){
                        Turn.setText("It is O's Turn");
                    }else{
                        Turn.setText("It is X's Turn");
                    }
                }
            });
            return but;
        }
        
    }
    
}