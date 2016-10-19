import java.util.*;

public class Deck {
    List<Card> deck = new LinkedList<>();

    public Deck(int trump){
        for(int i = Suit.heart; i < Suit.spade + 1; i++){
            for(int j = Value.six; j < Value.ace + 1; j++){
                deck.add(new Card(i, j, (i == trump)));
            }
        }
        Collections.shuffle(deck);
    }

    Card get(){
        Card ret = deck.get(deck.size() - 1);
        deck.remove(ret);
        return ret;
    }

    int cardsLeft(){
        return deck.size();
    }
}
