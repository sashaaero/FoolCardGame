import java.awt.image.BufferedImage;

class Card {
    Suit suit;
    Value value;

    /* Ограничивающие карту линии?!?! */


    Card(Suit suit, Value value){
        this.suit = suit;
        this.value = value;
    }

    public String toString(){
        return suit.toString() + "/" + value.toString();
    }

    public BufferedImage image(){
        return Resources.getImage(toString());
    }
}