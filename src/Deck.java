import java.util.*;

class Deck {
    List<Card> cards = new ArrayList<>();
    Card trump;

    Deck(){
        for(Suit suit: Suit.values()){
            for(Value value: Value.values()){
                cards.add(new Card(suit, value));
            }
        }
        Collections.shuffle(cards);
    }

    void setTrump(Card card){
        trump = card;
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
