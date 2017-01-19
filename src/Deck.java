import java.util.*;

class Deck {
    public static List<Card> cards = new ArrayList<>();

    Deck(){
        for(Suit suit: Suit.values()){
            for(Value value: Value.values()){
                cards.add(new Card(suit, value));
            }
        }
        Collections.shuffle(cards);
    }

    Card getCard(){
        return cards.remove(0);
    }

    void add(Card c){
        cards.add(c);
    }

    int size(){
        return cards.size();
    }
}
