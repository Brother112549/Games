package improvedgames;
/*Created By Kelby
Jan 29, 2018
*/
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
public class DeckOfCards 
{

    private final int StandardDeckSize = 52;
    public DeckOfCards()
    {
        
    }
    
    public ArrayList NewDeck(){
        ArrayList <Card> Deck = new ArrayList<>();
        for(int a = 0; a < StandardDeckSize; a++){
            Deck.add(new Card());
        }
        int c = 0;
        for(int a = 1;a<5;a++){
            for(int b = 1; b < 14; b++){
                Deck.get(c).setRank(b);
                Deck.get(c).setSuit(a);
                c++;
            }
        }
        return(Deck);
    }
    
    public ArrayList ShuffledDeck(ArrayList Deck){
        Collections.shuffle(Deck);
        return Deck;
    }
    
    public ArrayList NewShuffledDeck(){
        ArrayList <Card> Deck = new DeckOfCards().NewDeck();
        Collections.shuffle(Deck);
        return Deck;
    }
    
    public ArrayList BiggerDeck(ArrayList Deck){
        ArrayList <Card> Add = new DeckOfCards().NewDeck();
        for(Card Add1: Add){
            Deck.add(Add1);
        }
        return Deck;
    }
    
}