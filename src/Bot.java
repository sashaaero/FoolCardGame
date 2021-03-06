import javax.swing.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

class Bot extends BasePlayer{
    Bot(Deck deck){
        super(deck);
    }

    void printCards(){
        System.out.println("Bot have");
        super.printCards();
    }

    private static Card pickMin(List<Card> cards){
        Card min = cards.get(0);
        for (Card card: cards){
            /* Если козырь, добавим кол-во значений, чтобы карта была больше любой некозырной */
            int currValue = extractValue(card);
            if (currValue < extractValue(min)){
                min = card;
            }
        }
        return min;
    }

    private Card pickCardToAttack(){
        Set<Value> filter = Game.getInstance().valuesOnTable();
        if (filter.isEmpty()){
            return pickMin(cards);
        } else {
            List<Card> filtered = new LinkedList<>();
            for (Card card: cards){
                if (filter.contains(card.value)){
                    filtered.add(card);
                }
            }
            if (filtered.size() > 0)
                return pickMin(filtered);
            else
                return null;
        }
    }

    private static int extractValue(Card card){
        return card.suit == Game.getInstance().trump ?
                card.value.ordinal() + Value.values().length : card.value.ordinal();
    }

    void attack(){
        if(Game.getInstance().deck.size() == 0 && cards.size() == 0){
            JOptionPane.showMessageDialog(Game.getInstance(), "Победил бот");
            Game.reinit();
            return;
        }
        Card cardToAttack = pickCardToAttack();
        if (cardToAttack == null){ // Нечем атаковать, ход закончен
            Game.getInstance().clearTable();
            Game.getInstance().turn = Turn.player;
            Game.getInstance().state = State.attack;
        } else {
            Game.getInstance().attackCards.add(cardToAttack);
            cards.remove(cardToAttack);
            Game.getInstance().state = State.defence;
        }
    }

    void defend(){
        if(Game.getInstance().attackCards.size() == Game.getInstance().defenceCards.size())
            return;

        Card attackCard = Game.getInstance().attackCards.get(
                Game.getInstance().attackCards.size() - 1
        );
        List<Card> filtered = new LinkedList<>();
        if (attackCard.suit == Game.getInstance().trump){
            for(Card c: cards){
                if (c.suit == attackCard.suit && c.value.ordinal() > attackCard.value.ordinal()){
                    filtered.add(c);
                }
            }
        } else {
            for (Card c: cards){
                if ((c.suit == attackCard.suit && c.value.ordinal() > attackCard.value.ordinal())
                        || c.suit == Game.getInstance().trump){
                    filtered.add(c);
                }
            }
        }

        if (filtered.size() == 0){
            // Берем, нечем бить
            for (Card c1: Game.getInstance().attackCards){
                cards.add(c1);
            }
            for (Card c2: Game.getInstance().defenceCards){
                cards.add(c2);
            }
            Game.getInstance().clearTable();
            Game.getInstance().turn = Turn.player;
            Game.getInstance().state = State.attack;
        } else if (filtered.size() == 1){
            // Отбиваемся этой шмалью
            Game.getInstance().defenceCards.add(filtered.get(0));
            Game.getInstance().state = State.attack;
            cards.remove(filtered.get(0));
        } else {
            // Выбираем меньшую
            int size = filtered.size();
            int index = 0;
            int value = extractValue(filtered.get(index));
            for (int i = 1; i < size; i++){
                int currValue = extractValue(filtered.get(i));
                if (currValue < value){
                    index = i;
                    value = currValue;
                }
            }
            Card c = filtered.get(index);
            Game.getInstance().defenceCards.add(c);
            Game.getInstance().state = State.attack;
            cards.remove(c);
        }
        if (Game.getInstance().deck.size() == 0 && cards.size() == 0){
            JOptionPane.showMessageDialog(Game.getInstance(), "Победил бот!");
            Game.reinit();
        }
    }
}
