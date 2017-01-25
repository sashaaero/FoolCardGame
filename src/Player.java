import javax.swing.*;
import java.util.Set;

public class Player extends BasePlayer {
    public Player(Deck deck) {
        super(deck);
    }

    void printCards(){
        System.out.println("Player have");
        super.printCards();
    }

    Card clickedCard(int x, int y){
        for (Card c: cards){
            if (c.isClicked(x, y)){
                return c;
            }
        }
        return null;
    }

    void sendAttack(Card card){
        Game.getInstance().attackCards.add(card);
        Game.getInstance().state = State.defence;
        cards.remove(card);
        Game.getInstance().repaint();
        Game.getInstance().bot.defend();
    }

    void sendDefend(Card card){
        System.out.print("Отбиваем используя ");
        System.out.println(card);
        Game.getInstance().defenceCards.add(card);
        Game.getInstance().state = State.attack;
        cards.remove(card);
        Game.getInstance().bot.attack();
        Game.getInstance().repaint();
    }

    void attack(int x, int y){
        Card card = clickedCard(x, y);
        if (card == null)
            return;

        Set<Value> filter = Game.getInstance().valuesOnTable();
        if(filter.isEmpty()){
            sendAttack(card);
        } else {
            if (filter.contains(card.value)){
                sendAttack(card);
            }
        }
    }

    void defend(Card attackCard, int x, int y){
        System.out.print("Надо отбить ");
        System.out.println(attackCard);
        Card card = clickedCard(x, y);
        if (card == null)
            return;

        if(attackCard.suit == Game.getInstance().trump){
            // Отбиваться только козырем
            if (card.suit == attackCard.suit && card.value.ordinal() > attackCard.value.ordinal()){
                sendDefend(card);
            }
        } else {
            if (card.suit == Game.getInstance().trump){
                sendDefend(card);
            } else if (card.suit == attackCard.suit && card.value.ordinal() > attackCard.value.ordinal()){
                sendDefend(card);
            }
        }
        if (Game.getInstance().deck.size() == 0 && cards.size() == 0){
            JOptionPane.showMessageDialog(Game.getInstance(), "Победа!");
        }
    }
}
