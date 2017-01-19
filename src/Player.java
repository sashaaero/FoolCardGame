public class Player extends BasePlayer {
    public Player(Deck deck) {
        super(deck);
    }

    void printCards(){
        System.out.println("Player have");
        super.printCards();
    }
}
