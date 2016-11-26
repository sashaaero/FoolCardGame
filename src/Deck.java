import java.util.*;

class Deck {
    public static Stack<Card> cards = new Stack<>();
    private Suit trump;

    Deck(){
        trump = pickTrump();
        for(Suit suit: Suit.values()){
            for(Value value: Value.values()){
                cards.add(new Card(suit, value, (suit == trump)));
            }
        }
        Collections.shuffle(cards);
    }

    private Suit pickTrump(){
        return Suit.values()[(int) (Math.random() * Suit.values().length)];
    }

    Card getCard(){
        return cards.pop();
    }

    int size(){
        return cards.size();
    }
}
