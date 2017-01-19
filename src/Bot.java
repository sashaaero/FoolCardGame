class Bot extends BasePlayer {
    public Bot(Deck deck){
        super(deck);
    }

    void printCards(){
        System.out.println("Bot have");
        super.printCards();
    }
}
