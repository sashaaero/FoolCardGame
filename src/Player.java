import java.util.LinkedList;
import java.util.List;

public class Player {
    List<Card> cards = new LinkedList<>();

    public Player(Deck deck){
        for(int i = 0; i < Main.defaultCardsAmount; i++){
            cards.add(deck.getCard());
        }


        System.out.println("Player have");
        for(Card card: cards){
            System.out.println(card);
        }
    }
}
