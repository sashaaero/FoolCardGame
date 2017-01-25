import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

class Game extends JFrame{
    Deck deck;
    Player player;
    Bot bot;
    Suit trump;
    Turn turn = Turn.player;
    State state = State.attack;
    List<Card> attackCards = new LinkedList<>();
    List<Card> defenceCards = new LinkedList<>();

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

    public static void init(){
        instance = new Game();
    }

    public static Game getInstance(){
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
             if (x > 10 && x < 100 && y >= getHeight() / 2 && y <= (getHeight() / 2) + 20){
                 buttonClicked();
             }

             if (turn == Turn.player && state == State.attack){
                 player.attack(x, y);
             } else if (turn == Turn.bot && state == State.defence) {
                 if(attackCards.size() > defenceCards.size()) {
                     Card attackCard = attackCards.get(attackCards.size() - 1);
                     player.defend(attackCard, x, y);
                 }
             }

            }
        });

        this.setVisible(true);
    }

    void buttonClicked(){
        if (turn == Turn.player && state == State.attack){
            clearTable(); // Бито
            turn = Turn.bot;
            bot.attack();
        } else if (turn == Turn.bot && state == State.defence) {
            for (Card c: attackCards){
                player.addCard(c);
            }
            for (Card c: defenceCards){
                player.addCard(c);
            }
            clearTable();
            state = State.attack;
            bot.attack();
        }
    }

    public void start(){
        if(turn == Turn.bot){
            bot.attack();
        }
    }

    public void clearTable(){
        attackCards.clear();
        defenceCards.clear();
        this.repaint();

        if (turn == Turn.bot){
            while (bot.getCards().size() < Main.defaultCardsAmount && deck.size() > 0){
                bot.addCard(deck.getCard());
            }

            while (player.getCards().size() < Main.defaultCardsAmount && deck.size() > 0){
                player.addCard(deck.getCard());
            }
        } else {
            while (player.getCards().size() < Main.defaultCardsAmount && deck.size() > 0){
                player.addCard(deck.getCard());
            }

            while (bot.getCards().size() < Main.defaultCardsAmount && deck.size() > 0){
                bot.addCard(deck.getCard());
            }
        }
    }

    private void pickTrump(){
        Card next = deck.getCard(); // Берем следующую
        trump = next.suit; // Делаем ее масть козырем
        deck.add(next); // Кладем в конец
        deck.setTrump(next);
    }

    private void setFirstTurn(){
        // Находим у кого меньший козырь
        Value botsMin = null, playersMin = null;
        for(Card card: bot.getCards()){
            if (card.suit == trump){
                if (botsMin == null){
                    botsMin = card.value;
                } else if (botsMin.ordinal() > card.value.ordinal()) {
                    botsMin = card.value;
                }
            }
        }
        for(Card card: player.getCards()){
            if (card.suit == trump){
                if (playersMin == null){
                    playersMin = card.value;
                } else if (playersMin.ordinal() > card.value.ordinal()){
                    playersMin = card.value;
                }
            }
        }

        if (botsMin == null && playersMin == null){ // Ни у кого нет козыря
            turn = Turn.player;
        } else if(botsMin != null && playersMin != null){ // У обоих есть
            if (botsMin.ordinal() < playersMin.ordinal()){
                turn = Turn.bot;
            } else {
                turn = Turn.player;
            }
        } else if (botsMin == null && playersMin != null){
            turn = Turn.player;
        } else {
            turn = Turn.bot;
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

        protected void paintBorder(Graphics g_) {
            Graphics2D g = (Graphics2D) g_.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.setColor(Color.white);
            /* Рисуем колоду */
            if (deck.size() > 0) {
                BufferedImage trump = deck.trump.image();
                int leftBorder = getWidth() - imageWidth / 2;
                int topBorder = (getHeight() - imageHeight) / 2;
                g.drawImage(trump, leftBorder, topBorder, null);

                if (deck.size() > 1) {
                    BufferedImage image = Resources.getImage("back_90");
                    leftBorder = getWidth() - (int) (image.getWidth() * 0.7);
                    topBorder = getHeight() / 2;
                    g.drawImage(image, leftBorder, topBorder, this);
                    g.setFont(new Font("Arial", Font.BOLD, 24));
                    g.setColor(Color.white);
                    g.drawString(Integer.toString(deck.size()), leftBorder, topBorder - image.getHeight() / 2);
                }
            } else {
                if (trump.toString().equals("clubs")){
                }
            }

            //for(int i = 0; i < deck.size(); i++){
                //g.drawImage(image, leftBorder + 1, topBorder + 1,this);
            //} // Эффект колоды (некрасиво)

            /* Кнопка бито/взять */
            if(turn == Turn.bot){
                g.drawString("Атака", 10, 20);
                g.drawString("Взять", 10, getHeight() / 2);
                g.drawString("Защита", 10, getHeight() - 10);

            } else if(turn == Turn.player){
                g.drawString("Защита", 10, 20);
                g.drawString("Бито", 10, getHeight() / 2);
                g.drawString("Атака", 10, getHeight() - 10);
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

/*
    Я петух...
 */