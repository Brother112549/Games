package improvedgames;
/*Created By Kelby
Jan 29, 2018
*/
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
public class Blackjack
{
    //For main game
    private JFrame frame = new JFrame();
    private final JTextArea DealerCards = new JTextArea();
    private final JTextArea PlayerCards = new JTextArea();
    private final ArrayList <Card> Dealer = new ArrayList();
    private final ArrayList <Card> Player = new ArrayList();
    private ArrayList <Card> Deck = new DeckOfCards().NewDeck();
    private String PC = null;
    private String DC = null;
    private int PT = 0;
    private int DT = 0;
    private boolean bust = false;
    private boolean win = false;
        //For split hand
    private final JTextArea PlayerCards1 = new JTextArea();
    private final ArrayList <Card> Player1 = new ArrayList();
    private String PC1 = null;
    private int PT1 = 0;
    private boolean bust1 = false;
    private boolean win1 = false;
    private int hand = 0;

    public Blackjack(){
        
    }

    public void SetUp(JFrame jframe){
        frame = jframe;
        NDeck[] NumDecks = new NDeck[8];
        for(int cnt = 0; cnt < NumDecks.length;cnt ++){
            NumDecks[cnt] = new NDeck();
            NumDecks[cnt].setPos(cnt+1);
            NumDecks[cnt].but.setText(Integer.toString(NumDecks[cnt].Pos));
            NumDecks[cnt].addAL();
        }
        JLabel decks = new JLabel("How many Decks do you want to use?");
        JPanel opt = new JPanel();
        frame.setContentPane(opt);
        opt.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 10;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 5, 10);
        opt.add(decks, c);
        c.gridwidth = 1;
        c.gridy = 1;
        for(int i = 0; i < NumDecks.length; i++){
            if(i == 0){
                c.insets = new Insets(5, 10, 10, 5);
            }else if(i == NumDecks.length-1){
                c.insets = new Insets(5, 5, 10, 10);
            }else{
                c.insets = new Insets(5, 5, 10, 5);
            }
            c.gridx = i;
            opt.add(NumDecks[i].getButton(), c);
        }
        frame.setTitle("Games - Blackjack");
        frame.pack();
    }
    
    private class NDeck{
        
        private int Pos = 0;
        private JButton but;
        
        private NDeck(){
            but = new JButton();
        }
        
        private void setPos(int npos){
            Pos = npos;
        }
        
        private JButton getButton(){
            return but;
        }
        
        private void addAL(){
            but.addActionListener((ActionEvent e) -> {
                int decks = Pos;
                while(decks>0){
                    Deck = new DeckOfCards().BiggerDeck(Deck);
                    decks --;
                }
                Deck = new DeckOfCards().ShuffledDeck(Deck);
                Player.add(Deck.get(0));
                Deck.remove(0);
                Dealer.add(Deck.get(0));
                Deck.remove(0);
                Player.add(Deck.get(0));
                Deck.remove(0);
                Dealer.add(Deck.get(0));
                Deck.remove(0);
                PC = (Player.get(0).getRank()+Player.get(0).getSuit()+"\n"+Player.get(1).getRank()+Player.get(1).getSuit());
                for(Card P1: Player){
                    if(P1.Rank<11){
                        PT += P1.Rank;
                    }if(P1.isFaceCard()){
                        PT += 10;
                    }
                }
                DC = (Dealer.get(0).getRank()+Dealer.get(0).getSuit()+"\none face down card");
                for(Card D1: Dealer){
                    if(D1.Rank<11){
                        DT += D1.Rank;
                    }if(D1.isFaceCard()){
                        DT += 10;
                    }
                }
                Play(frame);
            });
        }
        
    }
    
    private void Play(JFrame jframe){
        frame = jframe;
        JPanel options = new JPanel();
        options.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        DealerCards.setEditable(false);
        DealerCards.setCursor(null);
        DealerCards.setOpaque(false);
        DealerCards.setFocusable(false);
        DealerCards.setText(DC);
        PlayerCards.setEditable(false);
        PlayerCards.setCursor(null);
        PlayerCards.setOpaque(false);
        PlayerCards.setFocusable(false);
        PlayerCards.setText(PC);
        JLabel DealerCardsL = new JLabel("The Dealer has: ", JLabel.CENTER);
        JLabel PlayerCardsL = new JLabel("You have: ", JLabel.CENTER);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.insets = new Insets(10,10,10,10);
        c.gridx = 0;
        c.gridy = 0;
        options.add(DealerCardsL, c);
        c.gridy = 1;
        options.add(PlayerCardsL, c);
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 0;
        options.add(DealerCards, c);
        c.gridy = 1;
        options.add(PlayerCards, c);
        frame.pack();
        
        if(Player.get(0).Rank==Player.get(1).Rank){
            checkSplit(options, c);
        }else{
            noSplit(options, c);
        }
    }
    
    private void checkSplit(JPanel options, GridBagConstraints c){
        JPanel Options = options;
        GridBagConstraints C = c;
        JLabel OptionsL = new JLabel("Would you like to split your hand?");
        JButton OP1 = new JButton("Yes");
        JButton OP2 = new JButton("No");

        c.gridwidth = 1;
        c.gridy = 2;
        c.gridx = 0;
        options.add(OptionsL, c);
        c.gridx = 1;
        options.add(OP1, c);
        c.gridx = 2;
        options.add(OP2, c);

        OP1.addActionListener((ActionEvent e) -> {
            doSplit(Options, C);
        });
        OP2.addActionListener((ActionEvent e) -> {
            noSplit(Options, C);
        });
        frame.setContentPane(options);
        frame.pack();
    }
    
    private void doSplit(JPanel options, GridBagConstraints c){
        frame.remove(frame.getContentPane());
        Player1.add(Player.get(1));
        PT = 0;
        Player.remove(1);
        Player.add(Deck.get(0));
        Deck.remove(0);
        Player1.add(Deck.get(0));
        Deck.remove(0);
        PC = (Player.get(0).getRank()+Player.get(0).getSuit()+"\n"+Player.get(1).getRank()+Player.get(1).getSuit());
        PC1 = (Player1.get(0).getRank()+Player1.get(0).getSuit()+"\n"+Player1.get(1).getRank()+Player1.get(1).getSuit());
        for(Card P1: Player){
            if(P1.Rank<11){
                PT += P1.Rank;
            }if(P1.isFaceCard()){
                PT += 10;
            }
        }
        for(Card P1: Player1){
            if(P1.Rank<11){
                PT1 += P1.Rank;
            }if(P1.isFaceCard()){
                PT1 += 10;
            }
        }

        PlayerCards.setText(PC);
        PlayerCards1.setEditable(false);
        PlayerCards1.setCursor(null);
        PlayerCards1.setOpaque(false);
        PlayerCards1.setFocusable(false);
        PlayerCards1.setText(PC1);
        JLabel PlayerCardsR = new JLabel("Your Second Hand: ", JLabel.CENTER);
        JLabel OptionsL = new JLabel("Would you like another card for your first hand?");
        JButton OP1 = new JButton("Yes");
        JButton OP2 = new JButton("No");


        c.gridy = 2;
        c.gridx = 0;
        options.remove(4);
        options.remove(4);
        options.remove(4);
        options.add(PlayerCardsR, c);
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 2;
        options.add(PlayerCards1, c);
        c.gridwidth = 1;
        c.gridy = 3;
        c.gridx = 0;
        options.add(OptionsL, c);
        c.gridx = 1;
        options.add(OP1, c);
        c.gridx = 2;
        options.add(OP2, c);


        OP1.addActionListener((ActionEvent e) -> {
            if(hand==0){
                Player.add(Deck.get(0));
                Deck.remove(0);
                if(Player.get(Player.size()-1).Rank<11){
                    PT += Player.get(Player.size()-1).Rank;
                }if(Player.get(Player.size()-1).isFaceCard()){
                    PT += 10;
                }
                PC = PC + "\n" + Player.get(Player.size()-1).getRank()+Player.get(Player.size()-1).getSuit();
                PlayerCards.setText(PC);
                frame.pack();
                bust = checkbust(PT, Player);
                if(bust){
                    hand ++;
                    OptionsL.setText("Would you like another card for your second hand?");
                    frame.pack();
                }
            }else{
                Player1.add(Deck.get(0));
                Deck.remove(0);
                if(Player1.get(Player1.size()-1).Rank<11){
                    PT1 += Player1.get(Player1.size()-1).Rank;
                }if(Player1.get(Player1.size()-1).isFaceCard()){
                    PT1 += 10;
                }
                PC1 = PC1 + "\n" + Player1.get(Player1.size()-1).getRank()+Player1.get(Player1.size()-1).getSuit();
                PlayerCards1.setText(PC1);
                frame.pack();
                bust1 = checkbust(PT1, Player1);
                if(bust1){
                    DC = (Dealer.get(0).getRank()+Dealer.get(0).getSuit()+"\n"+Dealer.get(1).getRank()+Dealer.get(1).getSuit());
                    DealerCards.setText(DC);
                    frame.pack();
                    SplitEnd();
                }
            }

        });
        OP2.addActionListener((ActionEvent e) -> {
            if(hand==0){
                OptionsL.setText("Would you like another card for your second hand?");
                frame.pack();
                hand++;
            }else if(hand==1){
                DC = (Dealer.get(0).getRank()+Dealer.get(0).getSuit()+"\n"+Dealer.get(1).getRank()+Dealer.get(1).getSuit());
                DealerCards.setText(DC);
                frame.pack();
                win = checkwin();
                if(DT>21){
                    win1 = true;
                }else{
                    if(Player1.get(0).Rank==Player1.get(1).Rank&&Player1.get(0).Rank==1&&Player1.size()==2){
                        PT1 = 12;
                    }
                    win1 = PT1>DT;
                }
                SplitEnd();
            }
        });
        
        frame.setContentPane(options);
        frame.pack();
    }
    
    private void noSplit(JPanel options, GridBagConstraints c){
        JLabel OptionsL = new JLabel("Would you like another card?");
        JButton OP1 = new JButton("Yes");
        JButton OP2 = new JButton("No");

        c.gridwidth = 1;
        c.gridy = 2;
        c.gridx = 0;
        options.add(OptionsL, c);
        c.gridx = 1;
        options.add(OP1, c);
        c.gridx = 2;
        options.add(OP2, c);

        OP1.addActionListener((ActionEvent e) -> {
            Player.add(Deck.get(0));
            Deck.remove(0);
            if(Player.get(Player.size()-1).Rank<11){
                PT += Player.get(Player.size()-1).Rank;
            }if(Player.get(Player.size()-1).isFaceCard()){
                PT += 10;
            }
            PC = PC + "\n" + Player.get(Player.size()-1).getRank()+Player.get(Player.size()-1).getSuit();
            PlayerCards.setText(PC);
            frame.pack();
            bust = checkbust(PT, Player);
            if(bust){
                DC = (Dealer.get(0).getRank()+Dealer.get(0).getSuit()+"\n"+Dealer.get(1).getRank()+Dealer.get(1).getSuit());
                DealerCards.setText(DC);
                OP1.setEnabled(false);
                frame.pack();
                OneEnd();
            }
        });
        OP2.addActionListener((ActionEvent e) -> {
            DC = (Dealer.get(0).getRank()+Dealer.get(0).getSuit()+"\n"+Dealer.get(1).getRank()+Dealer.get(1).getSuit());
            DealerCards.setText(DC);
            frame.pack();
            win = checkwin();
            OneEnd();
        });

        frame.setContentPane(options);
        frame.pack();
    }
    
    private void OneEnd(){
        ActionListener Listen = (ActionEvent e) -> {
            if(bust){
                frame = Bust(frame, "you Busted");
            }else if(win){
                if(DT>21){
                    frame = DealerBust(frame,"You Won ");
                }else{
                    frame = Win(frame, "you had a Better Hand");
                }
            }else{
                frame = Loss(frame,"You Lost ");
            }
            rePlay();
        };
        
        Timer time = new Timer(1500, Listen);
        time.setRepeats(false);
        time.start();
    }
    
    private void SplitEnd(){
        ActionListener Listen = (ActionEvent e) -> {
            if(bust==bust1&&win==win1){
                if(bust){
                    frame = Bust(frame, "you Busted Both Hands");
                }else if(win){
                    if(DT>21){
                        frame = DealerBust(frame,"You Won Both Hands ");
                    }else{
                        frame = Win(frame,"Both of your Hands were Better");
                    }
                }else{
                    frame = Loss(frame,"You Lost Both Hands ");
                }
            }else{
                frame = OneNOne(frame);
            }
            rePlay();
        };
        
        Timer time = new Timer(1500, Listen);
        time.setRepeats(false);
        time.start();
    }
    
    private void rePlay(){
        ActionListener Listen = (ActionEvent e) -> {
            JPanel pane = new JPanel();
            pane.setLayout(new GridBagLayout());
            frame.setContentPane(pane);
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(10, 10, 10, 10);
            c.gridwidth = 2;
            c.gridx = 0;
            c.gridy = 0;
            JLabel test = new JLabel("Do you want to play again?");
            test.setHorizontalTextPosition(SwingConstants.CENTER);
            pane.add(test, c);
            c.weightx = 1;
            c.gridwidth = 1;
            c.gridy = 1;
            JButton yes = new JButton("Yes");
            yes.addActionListener((ActionEvent f) -> {
                new Blackjack().SetUp(frame);
            });
            pane.add(yes, c);
            c.gridx = 1;
            JButton no = new JButton("No");
            no.addActionListener((ActionEvent f) -> {
                new ImprovedGames().StartScreen(frame);
            });
            pane.add(no, c);
            pane.setPreferredSize(new Dimension(275,100));
            frame.pack();
        };
        Timer time = new Timer(3000, Listen);
        time.setRepeats(false);
        time.start();
    }

    private boolean checkbust(int PT, ArrayList <Card> Player){
        if(PT>21){
            int PA = 0;
            for(Card players: Player){
                if(PT>21&&players.Rank==1){
                    PT-=10;
                    PA++;
                }
            }
            boolean busted = (PT>21);
            for(Card players: Player){
                if(PA>0&&players.Rank==1){
                    PT+=10;
                    PA--;
                }
            }
            return busted;
        }else
            return false;
    }

    private boolean checkwin() {
        int counter = 0;
        int aces = 0;
        if(DT>21){
            for(Card dealers: Dealer){
                if(DT>21&&dealers.Rank==1){
                    DT-=10;
                    aces ++;
                }
            }
        }
        while(DT<17){
            if(counter>0){
                for(Card dealers: Dealer){
                    if(aces>0&&dealers.Rank==1){
                        DT+=10;
                        aces --;
                    }
                }
            }
            Dealer.add(Deck.get(0));
            Deck.remove(0);
            if(Dealer.get(Dealer.size()-1).Rank<11){
                DT += Dealer.get(Dealer.size()-1).Rank;
            }if(Dealer.get(Dealer.size()-1).isFaceCard()){
                DT += 10;
            }
            DC = DC + "\n" + Dealer.get(Dealer.size()-1).getRank()+Dealer.get(Dealer.size()-1).getSuit();
            DealerCards.setText(DC);
            frame.pack();
            if(DT>21){
                for(Card dealers: Dealer){
                    if(DT>21&&dealers.Rank==1){
                        DT-=10;
                        aces ++;
                    }
                }
            }
            counter ++;
        }
        
        
        if(DT>21){
            return true;
        }else{
            if(Player.get(0).Rank==Player.get(1).Rank&&Player.get(0).Rank==1&&Player.size()==2){
                PT = 12;
            }
            return PT>DT;
        }
    }

    private JFrame Bust(JFrame frame, String numhands) {
        frame.getContentPane().removeAll();
        JLabel Bust = new JLabel("You Lost Because "+numhands, JLabel.CENTER);
        Bust.setFont(new Font(Bust.getFont().getName(), Font.PLAIN, 30));
        JLabel text = new JLabel("Waiting...", JLabel.CENTER);
        JPanel options = new JPanel();
        
        options.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.insets = new Insets(10,10,10,10);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        options.add(Bust, c);
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        options.add(text, c);
        
        frame.add(options);
        frame.pack();
        return frame;
    }

    private JFrame Loss(JFrame frame, String numhands) {
        frame.getContentPane().removeAll();
        JLabel Bust = new JLabel(numhands+"Because the Dealer had a Better Hand", JLabel.CENTER);
        Bust.setFont(new Font(Bust.getFont().getName(), Font.PLAIN, 30));
        JLabel text = new JLabel("Waiting...", JLabel.CENTER);
        JPanel options = new JPanel();
        
        options.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.insets = new Insets(10,10,10,10);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        options.add(Bust, c);
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        options.add(text, c);
        
        frame.add(options);
        frame.pack();
        return frame;
    }

    private JFrame DealerBust(JFrame frame, String numhands) {
        frame.getContentPane().removeAll();
        JLabel Bust = new JLabel(numhands+"Because the Dealer Busted", JLabel.CENTER);
        Bust.setFont(new Font(Bust.getFont().getName(), Font.PLAIN, 30));
        JLabel text = new JLabel("Waiting...", JLabel.CENTER);
        JPanel options = new JPanel();
        
        options.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.insets = new Insets(10,10,10,10);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        options.add(Bust, c);
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        options.add(text, c);
        
        frame.add(options);
        frame.pack();
        return frame;
    }

    private JFrame Win(JFrame frame, String numhands) {
        frame.getContentPane().removeAll();
        JLabel Bust = new JLabel("You Won Because "+numhands+" than the Dealer", JLabel.CENTER);
        Bust.setFont(new Font(Bust.getFont().getName(), Font.PLAIN, 30));
        JLabel text = new JLabel("Waiting...", JLabel.CENTER);
        JPanel options = new JPanel();
        
        options.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.insets = new Insets(10,10,10,10);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        options.add(Bust, c);
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        options.add(text, c);
        
        frame.add(options);
        frame.pack();
        return frame;
    }

    private JFrame OneNOne(JFrame frame) {
        frame.getContentPane().removeAll();
        JLabel Bust = new JLabel("You Won One Hand and Lost Your Other Hand", JLabel.CENTER);
        Bust.setFont(new Font(Bust.getFont().getName(), Font.PLAIN, 30));
        JLabel text = new JLabel("Waiting...", JLabel.CENTER);
        JPanel options = new JPanel();
        
        options.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.insets = new Insets(10,10,10,10);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        options.add(Bust, c);
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        options.add(text, c);
        
        frame.add(options);
        frame.pack();
        return frame;
    }

}