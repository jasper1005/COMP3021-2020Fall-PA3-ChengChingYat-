package castle.comp3021.assignment.protocol;

import java.util.Random;

public class MakeMoveByStrategy {
    private final Strategy strategy;
    private final Game game;
    private final Move[] availableMoves;

    private static final double CAPTUREWEIGHT = 1;
    private static final double DISTANCEWEIGHT = 1;

    public MakeMoveByStrategy(Game game, Move[] availableMoves, Strategy strategy){
        this.game = game;
        this.availableMoves = availableMoves;
        this.strategy = strategy;
    }

    /**
     * Return next move according to different strategies made by {@link castle.comp3021.assignment.player.ComputerPlayer}
     * You can add helper method if needed, as long as this method returns a next move.
     * - {@link Strategy#RANDOM}: select a random move from the proposed moves by all pieces
     * - {@link Strategy#SMART}: come up with some strategy to select a next move from the proposed moves by all pieces
     *
     * @return a next move
     */
    public Move getNextMove(){
        if(availableMoves == null || availableMoves.length == 0)
            return null;
        if(strategy == Strategy.RANDOM) {
            Random random = new Random();
            return availableMoves[random.nextInt(availableMoves.length)];
        } else {
            Move best = availableMoves[0];
            for(int i = 1; i < availableMoves.length; ++i) {
                if(h(availableMoves[i]) > h(best))
                    best = availableMoves[i];
            }
            return best;
        }
    }

    private double h(Move availableMove) {
        double distance = MakeMoveByBehavior.getDistance(availableMove.getDestination(),
                game.getCentralPlace()) * DISTANCEWEIGHT;
        double canCapture = (game.getPiece(availableMove.getDestination()) != null ? CAPTUREWEIGHT : 0);
        return distance + canCapture;
    }
}
