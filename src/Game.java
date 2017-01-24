import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

class Game extends JFrame{
    private Deck deck;
    private Player player;
    private Bot bot;
    private Suit trump;
    private Turn turn = Turn.player;
    private State state = State.attack;
    private List<Card> attackCards = new LinkedList<>();
    private List<Card> defenceCards = new LinkedList<>();

    /* GUI */
    private Background background;

    /* Singleton */
    private static Game instance = null;

    private Game(){
        deck = new Deck();
        player = new Player(deck);
        bot = new Bot(deck);
        pickTrump();
        setFirstTurn();
        initGUI();
    }

    public static Game getInstance(){
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    private void initGUI(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1024, 768);
        this.setLocationRelativeTo(null);
        this.setTitle("Игра «Дурак» - Курсовая работа Гетмана Антона");
        this.setLayout(new GridLayout(1, 1));
        background = new Background();
        this.add(background);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
             super.mouseReleased(e);
             int x = e.getX();
             int y = e.getY();
             if (turn == Turn.player && state == State.attack){
                 // ПОЕХАЛИ
             } else if (turn == Turn.bot && state == State.defence) {
                 // Защита!!!
             }

                    for (int i = 0; i < player.getCards().size(); i++) {
                        if (player.getCards().get(i).isClicked(x, y)) {
                            attack(player.getCards().get(i));
                            break;
                        }
                    }

            }
        });

        this.setVisible(true);
    }

    private void pickTrump(){
        Card next = deck.getCard(); // Берем следующую
        trump = next.suit; // Делаем ее масть козырем
        deck.add(next); // Кладем в конец
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

        if (botsMin == null && playersMin == null){ // Ни у кого нет козыря
            turn = Turn.player;
        } else if(botsMin != null && playersMin != null){ // У обоих есть
            if (botsMin.ordinal() < playersMin.ordinal()){
                turn = Turn.bot;
                bot.attack();
            } else {
                turn = Turn.player;
            }
        } else if (botsMin == null && playersMin != null){
            turn = Turn.player;
        } else {
            turn = Turn.bot;
            bot.attack();
        }
    }

    Set<Value> valuesOnTable(){
        Set<Value> values = new HashSet<>();
        for(Card card1: attackCards){
            values.add(card1.value);
        }
        for(Card card1: defenceCards){
            values.add(card1.value);
        }
        return values;
    }

    boolean ableToAttack(Turn turn){
        Set<Value> values = valuesOnTable();
        if (turn == Turn.player){
            for(Card card: player.getCards()){
                if(values.contains(card.value)){
                    return true;
                }
            }
        } else {
            for(Card card: bot.getCards()){
                if(values.contains(card.value)){
                    return true;
                }
            }
        }
        return false;
    }

    void attack(Card card){
        if(attackCards.isEmpty()){
            // Первый ход, фильтр пустой
            attackCards.add(card);
            if(turn == Turn.player){
                player.getCards().remove(card);
            } else {
                bot.getCards().remove(card);
            }
        } else {
            Set<Value> values = valuesOnTable();
            if (values.contains(card.value)){
                attackCards.add(card);
                if(turn == Turn.player){
                    player.getCards().remove(card);
                } else {
                    bot.getCards().remove(card);
                }
                state = State.defence;
            }
        }
        background.repaint();
    }

    void defend(Card card){

    }

    void play(){
        while (true){

        }
    }

    class Background extends JPanel{
        double cardHeightCf = 0.75;
        BufferedImage bg = Resources.getImage("background");
        protected void paintComponent(Graphics g){
            for(int x = 0; x < getWidth(); x += bg.getWidth()){
                for(int y = 0; y < getHeight(); y += bg.getHeight()){
                    g.drawImage(bg, x, y, null);
                }
            }
        }

        protected void paintBorder(Graphics g) {
            /* Рисуем карты игрока */
            int maxWidth = (int) (getWidth() * 0.8);
            int size = player.getCards().size();
            int imageWidth = Resources.getImage("spades/six").getWidth();
            int imageHeight = Resources.getImage("spades/six").getHeight();
            int actualWidth = size * imageWidth;

            if (actualWidth <= maxWidth) {
                /* Без перекрытия */
                int startPosition = (getWidth() - actualWidth) / 2;

                for (int i = 0; i < size; i++) {
                    Card card = player.getCards().get(i);
                    int leftBorder = startPosition + i * imageWidth;
                    int rightBorder = leftBorder + imageWidth - 1;
                    int topBorder = getHeight() - (int) (imageHeight * cardHeightCf);
                    card.setBorders(leftBorder, rightBorder, topBorder, this.getHeight());
                    g.drawImage(card.image(), leftBorder, topBorder, null);
                }
            } else {
                /* Приходится перерасчитывать и перекрывать */
                double shift = (double) maxWidth / (double) actualWidth;
                int startPosition = (getWidth() - maxWidth) / 2;

                for (int i = 0; i < size; i++) {
                    Card card = player.getCards().get(i);
                    int leftBorder = (int) (startPosition + (i * imageWidth * shift));
                    int rightBorder = leftBorder + (int) (imageWidth * shift) - 1;
                    int topBorder = getHeight() - (int) (imageHeight * cardHeightCf);
                    card.setBorders(leftBorder, rightBorder, topBorder, this.getHeight());
                    g.drawImage(card.image(), leftBorder, topBorder, null);
                }
            }

            /* Рисуем карты бота */
            size = bot.getCards().size();
            actualWidth = size * imageWidth;
            if (actualWidth <= maxWidth) {
                int startPosition = (getWidth() - actualWidth) / 2;
                for (int i = 0; i < size; i++) {
                    g.drawImage(
                            Resources.getImage("back"),
                            startPosition + i * imageWidth,
                            -(int) (imageHeight * (1.0 - cardHeightCf)),
                            null
                    );
                }
            } else {
                double shift = (double) maxWidth / (double) actualWidth;
                int startPosition = (getWidth() - maxWidth) / 2;
                for (int i = 0; i < size; i++) {
                    g.drawImage(
                            Resources.getImage("back"),
                            (int) (startPosition + (i * imageWidth * shift)),
                            -(int) (imageHeight * (1.0 - cardHeightCf)),
                            null
                    );
                }
            }
        }

        protected void paintChildren(Graphics g){
            int size = attackCards.size();
            int imageWidth = Resources.getImage("spades/six").getWidth();
            int imageHeight = Resources.getImage("spades/six").getHeight();
            /* Тут не перерасчета и сдвигов по ширине т.к. не больше 6 карт */
            int startPosition = (getWidth() - size * imageWidth) / 2 - (int) (imageWidth * 0.1);
            for (int i = 0; i < size; i++){
                if(turn == Turn.player){
                    g.drawImage(
                            attackCards.get(i).image(),
                            startPosition + i * (imageWidth + 10),
                            (int) ((getHeight() - imageHeight) / 2 + imageHeight * 0.5),
                            null
                    );
                } else {
                    g.drawImage(
                            attackCards.get(i).image(),
                            startPosition + i * (imageWidth + 10),
                            (int) ((getHeight() - imageHeight) / 2 - imageHeight * 0.5),
                            null
                    );
                }
            }

            size = defenceCards.size();
            startPosition = (getWidth() - size * imageWidth) / 2 + (int) (imageWidth * 0.1);

            for (int i = 0; i < size; i++){
                if (turn == Turn.player){
                    g.drawImage(
                            defenceCards.get(i).image(),
                            startPosition + i * (imageWidth + 10),
                            (int) ((getHeight() - imageHeight) / 2 + imageHeight * 0.5),
                            null
                    );
                } else {
                    g.drawImage(
                            defenceCards.get(i).image(),
                            startPosition + i * (imageWidth + 10),
                            (int) ((getHeight() - imageHeight) / 2 - imageHeight * 0.5),
                            null
                    );
                }
            }
        }
    }
}
