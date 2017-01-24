import java.awt.*;
import java.util.Set;

class Bot extends BasePlayer{
    public Bot(Deck deck){
        super(deck);
    }

    void printCards(){
        System.out.println("Bot have");
        super.printCards();
    }

    /*void attack(){
        Set<Value> valueSet = Game.getInstance().valuesOnTable();
        if(valueSet.isEmpty()){
            // Ходим какой-то дешевой
        }
    }

    void defend(){

    }*/
}
