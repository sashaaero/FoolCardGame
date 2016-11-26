import java.util.LinkedList;
import java.util.List;

class Bot {
    List<Card> cards = new LinkedList<>();

    Bot(Deck deck){
        for(int i = 0; i < Main.defaultCardsAmount; i++){
            cards.add(deck.getCard());
        }


        System.out.println("Bot have");
        for(Card card: cards){
            System.out.println(card);
        }
    }
}
