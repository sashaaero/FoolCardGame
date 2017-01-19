import javax.swing.*;

class GameTable extends JFrame{
    private Deck deck;
    private Player player;
    private Bot bot;
    private Suit trump;

    private Constants turn;

    GameTable(){
        deck = new Deck();
        player = new Player(deck);
        bot = new Bot(deck);
        pickTrump();
        player.printCards();
        bot.printCards();

        initGUI();
    }

    private void initGUI(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1024, 768);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void pickTrump(){
        Card next = deck.getCard(); // Берем следующую
        trump = next.suit; // Делаем ее масть козырем
        deck.add(next); // Кладем в конец
    }

    public Suit getTrump(){
        return trump;
    }

    private void setFirstTurn(){
        // Находим у кого меньший козырь
        Value botsMin = null, playersMin = null;
        for(Card card: bot.getCards()){
            if (card.suit == trump){
                if (botsMin == null){
                    botsMin = card.value;
                } else if (botsMin.ordinal() < card.value.ordinal()) {
                    botsMin = card.value;
                }
            }
        }
        for(Card card: player.getCards()){
            if (card.suit == trump){
                if (playersMin == null){
                    playersMin = card.value;
                } else if (playersMin.ordinal() < card.value.ordinal()){
                    playersMin = card.value;
                }
            }
        }
        // TODO ВЫБРАТЬ ТОГО, У КОГО МЕНЬШЕ))) У ПАШИ МЕНЬШЕ)))
    }
}
