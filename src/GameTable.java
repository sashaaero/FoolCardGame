public class GameTable {
    Deck deck;
    Player player;
    Bot bot;

    public GameTable(){
        deck = new Deck();
        player = new Player(deck);
        bot = new Bot(deck);

        System.out.print("cards left ");
        System.out.println(deck.size());
    }
}
