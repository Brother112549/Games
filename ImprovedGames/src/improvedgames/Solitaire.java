package improvedgames;
/*Created By Kelby
Jan 29, 2018
*/
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;
public class Solitaire 
{

    private ArrayList <Card> Deck;
    private Card[] Active;
    private int Discard;
    private ArrayList <Card> DiscardPile;
    private int a;
    private JFrame frame;
    private Timer time;
    
    public Solitaire()
    {
        
    }
    
    public void OneHanded(JFrame jframe){
        Deck = new DeckOfCards().NewShuffledDeck();
        Active = new Card[4];
        for(int i = 0; i<4; i++){
            Active[i] = Deck.get(i);
        }
        Discard = 0;
        DiscardPile = new ArrayList();
        a = 4;
        frame = jframe;
        OneHandedHand();
    }
    
    private void OneHandedHand(){
        ActionListener Listen = (ActionEvent e) -> {
            if(a<Deck.size()){
                if(Active[0].Rank==Active[3].Rank){
                    if(DiscardPile.size()>2){
                        for(int b = 2; b > -1; b--){
                            Active[b] = DiscardPile.get(0);
                            DiscardPile.remove(0);
                        }
                        for(int b = 3; b < Active.length; b++){
                            Active[b] = Deck.get(a);
                            a++;
                        }
                    }else if(DiscardPile.size()>1){
                        for(int b = 1; b > -1; b--){
                            Active[b] = DiscardPile.get(0);
                            DiscardPile.remove(0);
                        }
                        for(int b = 2; b < Active.length; b++){
                            Active[b] = Deck.get(a);
                            a++;
                        }
                    }else if(DiscardPile.size()>0){
                        for(int b = 0; b > -1; b--){
                            Active[b] = DiscardPile.get(0);
                            DiscardPile.remove(0);
                        }
                        for(int b = 1; b < Active.length; b++){
                            Active[b] = Deck.get(a);
                            a++;
                        }
                    }else{
                        for(int b = 0; b < Active.length; b++){
                            Active[b] = Deck.get(a);
                            a++;
                        }
                    }
                    Discard += 4;
                }else if(Active[0].Suit==Active[3].Suit){
                    if(DiscardPile.size()>1){
                        Active[2]=Active[0];
                        for(int b = 1; b > -1; b--){
                            Active[b] = DiscardPile.get(0);
                            DiscardPile.remove(0);
                        }
                    }else if(DiscardPile.size()>0){
                        Active[1]=Active[0];
                        Active[2]=Active[3];
                        for(int b = 0; b > -1; b--){
                            Active[b] = DiscardPile.get(0);
                            DiscardPile.remove(0);
                        }
                        Active[3]=Deck.get(a);
                        a++;
                    }else{
                        Active[1]=Active[3];
                        for(int b = 2; b < Active.length; b++){
                            Active[b] = Deck.get(a);
                            a++;
                        }
                    }
                    Discard += 2;
                }else{
                    DiscardPile.add(0, Active[0]);
                    for(int b = 0; b < 3; b ++){
                        Active[b]=Active[b+1];
                    }
                    Active[3] = Deck.get(a);
                    a++;
                }

                frame.getContentPane().removeAll();
                JTextArea Hand = new JTextArea();
                Hand.setText(Active[0].getRank()+Active[0].getSuit()+"\n"+Active[1].getRank()+Active[1].getSuit()+
                        "\n"+Active[2].getRank()+Active[2].getSuit()+"\n"+Active[3].getRank()+Active[3].getSuit());
                JPanel Handle = new JPanel();
                Handle.setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.BOTH;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.insets = new Insets(1,5,1,5);
                c.gridx = 0;
                c.gridy = 0;
                Handle.add(new JLabel("Discarded: "), c);
                c.gridx = 3;
                Handle.add(new JLabel(Integer.toString(Discard)+" Cards"), c);
                c.gridx = 0;
                c.gridy = 1;
                Handle.add(new JLabel("To Draw: "), c);
                c.gridx = 3;
                Handle.add(new JLabel(Integer.toString(52-a)+" Cards"), c);
                c.gridx = 0;
                c.gridy = 2;
                Handle.add(new JLabel("In Hand: "), c);
                c.gridx = 3;
                Handle.add(new JLabel(Integer.toString(DiscardPile.size()+4)+" Cards"), c);

                c.gridx = 0;
                c.insets = new Insets(1,5,5,5);
                c.gridy = 3;
                Handle.add(new JLabel(new ImageIcon(Active[0].showCardT())), c);
                c.gridx = 1;
                Handle.add(new JLabel(new ImageIcon(Active[1].showCardT())), c);
                c.gridx = 2;
                Handle.add(new JLabel(new ImageIcon(Active[2].showCardT())), c);
                c.gridx = 3;
                Handle.add(new JLabel(new ImageIcon(Active[3].showCardT())), c);

                c.gridx = 0;
                c.gridy = 4;
                c.gridwidth = 4;
                Handle.add(Hand, c);
                Handle.setVisible(true);
                
                //Pause Button
                final JButton Pause = new JButton("Pause");
                if(!time.isRunning()){
                    Pause.setText("Resume");
                }
                c.fill = GridBagConstraints.NONE;
                c.gridy = 5;
                c.anchor = GridBagConstraints.CENTER;
                Handle.add(Pause, c);
                Pause.addActionListener((ActionEvent g) -> {
                    if(time.isRunning()){
                        time.stop();
                        Pause.setText("Resume");
                    }else{
                        time.start();
                        Pause.setText("Pause");
                    }
                });
                
                frame.setContentPane(Handle);
                frame.pack();
            }else{
                time.stop();
                End(frame, Discard==Deck.size());
            }
        };
        
        time = new Timer(500, Listen);
        time.setRepeats(true);
        time.start();
    }
    
    private void End(JFrame frame, boolean win){
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
            new Solitaire().OneHanded(frame);
        });
        options.add(Yes, c);
        c.gridx = 2;
        JButton No = new JButton("No");
        No.addActionListener((ActionEvent e) -> {
            new ImprovedGames().StartScreen(frame);
        });
        options.add(No, c);
        frame.setContentPane(options);
        frame.pack();
    }

}