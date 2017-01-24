public class Player extends BasePlayer {
    public Player(Deck deck) {
        super(deck);
    }

    void printCards(){
        System.out.println("Player have");
        super.printCards();
    }

    Card attack(Card card){
        return null;
    }

    Card defend(Card card){

    }
}
