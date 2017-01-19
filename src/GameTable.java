import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class GameTable extends JFrame{
    private Deck deck;
    private Player player;
    private Bot bot;
    private Suit trump;

    private Constants turn;

    /* GUI */
    private Background background;

    GameTable(){
        deck = new Deck();
        player = new Player(deck);
        bot = new Bot(deck);
        pickTrump();
        initGUI();
    }

    private void initGUI(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1024, 768);
        this.setLocationRelativeTo(null);
        this.setTitle("Игра «Дурак» - Курсовая работа Гетмана Антона");
        this.setLayout(new GridLayout(1, 1));
        background = new Background();
        this.add(background);

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

    class Background extends JPanel{
        BufferedImage bg = Resources.getImage("background");
        protected void paintComponent(Graphics g){
            for(int x = 0; x < getWidth(); x += bg.getWidth()){
                for(int y = 0; y < getHeight(); y += bg.getHeight()){
                    g.drawImage(bg, x, y, null);
                }
            }
        }

        protected void paintBorder(Graphics g){
            /* Рисуем карты игрока */
            int maxWidth = (int) (getWidth() * 0.9);
            int size = player.getCards().size();
            int imageWidth = Resources.getImage("spades/six").getWidth();
            int imageHeight = Resources.getImage("spades/six").getHeight();
            int actualWidth = size * imageWidth;
            if (actualWidth <= maxWidth){
                /* Без перекрытия */
                int startPosition = (getWidth() - actualWidth) / 2;
                for(int i = 0; i < size; i++){
                    g.drawImage(
                            player.getCards().get(i).image(),
                            startPosition + i * imageWidth,
                            getHeight() - (int) (imageHeight * 0.58),
                            null
                    );
                }
            } else {
                /* Приходится перерасчитывать и перекрывать */
            }

            /* Рисуем карты бота */
            size = bot.getCards().size();
            actualWidth = size * imageWidth;
            if (actualWidth <= maxWidth){ 
                int startPosition = (getWidth() - actualWidth) / 2;
                for(int i = 0; i < size; i++){
                    g.drawImage(
                            Resources.getImage("back"),
                            startPosition + i * imageWidth,
                             -(int) (imageHeight * 0.25),
                            null
                    );
                }
            }
        }
    }

    class CardLayout extends JPanel{

    }
}
