import java.util.LinkedList;
import java.util.List;


class BasePlayer {
    List<Card> cards = new LinkedList<>();

    BasePlayer(Deck deck){
        for(int i = 0; i < Main.defaultCardsAmount; i++){
            cards.add(deck.getCard());
        }
    }

    void addCard(Card c){
        cards.add(c);
    }

    void printCards(){
        for(Card card: cards)
            System.out.println(card);
    }

    List<Card> getCards(){
        return this.cards;
    }
}
