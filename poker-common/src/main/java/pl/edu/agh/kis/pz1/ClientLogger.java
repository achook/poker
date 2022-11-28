package pl.edu.agh.kis.pz1;

public class ClientLogger extends Log {
    @Override
    protected void logln(String message) {
        // TODO
        super.logln(message);
    }

    public void displayHand(Hand hand) {
        logln("Your hand");
        logln(hand.toString());
    }

    public void displayID(int id) {
        logln("Your player number is " + id);
    }

    public void displayAnte(int ante) {
        logln("Ante is $" + ante);
    }

    public void announceTurn() {
        logln("It's your turn");
    }

    public void announceWinner(int id) {
        logln("Player " + id + " won");
    }

    public void askForCardsToDiscard() {
        logln("Which cards do you want to discard? (0/1/2/3/4)");
    }

    public void displayMoney(int money) {
        logln("You have $" + money);
    }

    public void announceDealer(int id) {
        logln("Player " + id + " is the dealer");
    }

    public void askForMove() {
        logln("What do you want to do? (fold/check/call/raise)");
    }

    public void askForContinue() {
        logln("Do you want to continue? (y/n)");
    }

    public void announceBet(String rawBet) {
        var betMakerID = Utilities.getArgument(rawBet);
        var betType = rawBet.split(" ")[2];

        switch (betType) {
            case "CHECK" -> logln("Player " + betMakerID + " checked");
            case "CALL" -> logln("Player " + betMakerID + " called");
            case "RAISE" -> {
                var betAmount = Integer.parseInt(rawBet.split(" ")[3]);
                logln("Player " + betMakerID + " raised by $" + betAmount);
            }
            case "FOLD" -> logln("Player " + betMakerID + " folded");
            default -> throw new IllegalStateException("Unexpected value: " + betType);
        }
    }
}
