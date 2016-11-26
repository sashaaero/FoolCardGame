class Card {
    Suit suit;
    Value value;
    boolean trump;

    Card(Suit suit, Value value, boolean trump){
        this.suit = suit;
        this.value = value;
        this.trump = trump;
    }

    public String toString(){
        return suit.toString() + ": " + value.toString();
    }
}