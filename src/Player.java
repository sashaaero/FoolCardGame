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

    void attack(int x, int y){
        //Card card =
    }

    void defend(Card card){

    }
}
