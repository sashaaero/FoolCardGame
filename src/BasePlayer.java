import java.util.LinkedList;
import java.util.List;


class BasePlayer {
    private List<Card> cards = new LinkedList<>();

    BasePlayer(Deck deck){
        for(int i = 0; i < Main.defaultCardsAmount; i++){
            cards.add(deck.getCard());
        }
    }

    void printCards(){
        for(Card card: cards)
            System.out.println(card);
    }

    List<Card> getCards(){
        return this.cards;
    }

    Card attack(){
        return null;
    }

    Card defend(Card card){
        return null;
    }
}
