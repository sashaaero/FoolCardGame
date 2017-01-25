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
            int currValue = card.suit == Game.getInstance().trump ?
                    card.value.ordinal() + Value.values().length : card.value.ordinal();
            if (currValue < min.value.ordinal()){
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

    void attack(){
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
                if (c.suit == attackCard.suit && c.value.ordinal() > attackCard.value.ordinal()
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
        } else {
            // Выбираем меньшую
            int size = filtered.size();
            int index = 0;
            int value = cards.get(index).suit == Game.getInstance().trump ?
                    filtered.get(index).value.ordinal() + Value.values().length : filtered.get(index).value.ordinal();
            for (int i = 1; i < size; i++){
                int currValue = cards.get(i).suit == Game.getInstance().trump ?
                        cards.get(i).value.ordinal() + Value.values().length : cards.get(i).value.ordinal();
            }
        }
    }
}
