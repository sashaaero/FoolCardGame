public class Main {
    public static int defaultCardsAmount = 6;

    public static void main(String[] args) {
        Game.init();
        Game.getInstance().start();
    }
}
