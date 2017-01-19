class Card {
    Suit suit;
    Value value;
    boolean trump;

    Card(Suit suit, Value value){
        this.suit = suit;
        this.value = value;
    }

    public String toString(){
        return suit.toString() + ": " + value.toString();
    }
}