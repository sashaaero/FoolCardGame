import java.awt.image.BufferedImage;

class Card {
    Suit suit;
    Value value;

    /* Ограничивающие карту линии */
    int leftBorder, rightBorder, topBorder, bottomBorder;

    Card(Suit suit, Value value){
        this.suit = suit;
        this.value = value;
    }

    public String toString(){
        return suit.toString() + "/" + value.toString();
    }

    BufferedImage image(){
        return Resources.getImage(toString());
    }

    void setBorders(int leftBorder, int rightBorder, int topBorder, int bottomBorder){
        this.leftBorder = leftBorder + 8; // Количество прозрачных пикселей на спрайте
        this.rightBorder = rightBorder + 8;
        this.topBorder = topBorder;
        this.bottomBorder = bottomBorder;
    }

    boolean isClicked(int x, int y){
        return x >= leftBorder && x <= rightBorder && y >= topBorder && y <= bottomBorder;
    }
}