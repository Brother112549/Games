package improvedgames;
/*Created By Kelby
Jan 29, 2018
*/
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
class Card 
{
    public int Rank;
    public int Suit;
    
    public Card()
    {
        
    }
    
    public void setRank(int b){
        Rank = b;
    }

    public void setSuit(int a) {
        Suit = a;
    }
    
    public String getSuit(){
        String suit = null;
        if(Suit==1){
            suit = "Spades";
        }else if(Suit==2){
            suit = "Hearts";
        }else if(Suit==3){
            suit = "Clubs";
        }else if(Suit==4){
            suit = "Diamonds";
        }
        return suit;
    }
    
    public String getRank(){
        String rank = null;
        if(Rank==1){
            rank = "Ace of ";
        }else if(Rank==11){
            rank = "Jack of ";
        }else if(Rank==12){
            rank = "Queen of ";
        }else if(Rank==13){
            rank = "King of ";
        }else{
            rank = Integer.toString(Rank) + " of ";
        }
        return rank;
    }
    
    public String getColor(){
        String color = null;
        if(Suit==1||Suit==3){
            color = "Black";
        }else if(Suit==2||Suit==4){
            color = "Red";
        }
        return color;
    }
    
    public boolean isFaceCard(){
        boolean x = Rank==1||Rank==11||Rank==12||Rank==13;
        return x;
    }
    
    //shows card Thumbnail (i.e. K<> is King of Diamonds)
    public Image showCardT(){
        Color cardColor = null;
        String suit = "Blank ";
        if(Suit==1){
            suit += "Spade";
            cardColor = Color.BLACK;
        }else if(Suit==2){
            suit += "Heart";
            cardColor = Color.RED;
        }else if(Suit==3){
            suit += "Club";
            cardColor = Color.BLACK;
        }else if(Suit==4){
            suit += "Diamond";
            cardColor = Color.RED;
        }
        
        String rank = null;
        if(Rank==1){
            rank = "A";
        }else if(Rank==11){
            rank = "J";
        }else if(Rank==12){
            rank = "Q";
        }else if(Rank==13){
            rank = "K";
        }else{
            rank = Integer.toString(Rank);
        }
        
        Image image = null;
        try {
            image = (ImageIO.read(getClass().getResource("/improvedgames/Images/"+suit+".jpg")));
        } catch (IOException ex) {
            Logger.getLogger(Card.class.getName()).log(Level.SEVERE, null, ex);
        }
        Graphics g = image.getGraphics();
        g.setFont(g.getFont().deriveFont(60f));
        g.setColor(cardColor);
        g.drawString(rank, 10, 75);
        g.dispose();
        
        Image SImage = image.getScaledInstance(60, 40, Image.SCALE_DEFAULT);
        
        return SImage;
    }

}