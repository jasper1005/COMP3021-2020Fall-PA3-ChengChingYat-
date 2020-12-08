package castle.comp3021.assignment.protocol;

import castle.comp3021.assignment.piece.Knight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MakeMoveByBehavior {
    private final Behavior behavior;
    private final Game game;
    private final Move[] availableMoves;

    public MakeMoveByBehavior(Game game, Move[] availableMoves, Behavior behavior){
        this.game = game;
        this.availableMoves = availableMoves;
        this.behavior = behavior;
    }

    /**
     * Return next move according to different strategies made by each piece.
     * You can add helper method if needed, as long as this method returns a next move.
     * - {@link Behavior#RANDOM}: return a random move from {@link this#availableMoves}
     * - {@link Behavior#GREEDY}: prefer the moves towards central place, the closer, the better
     * - {@link Behavior#CAPTURING}: prefer the moves that captures the enemies, killing the more, the better.
     *                               when there are many pieces that can captures, randomly select one of them
     * - {@link Behavior#BLOCKING}: prefer the moves that block enemy's {@link Knight}.
     *                              See how to block a knight here: https://en.wikipedia.org/wiki/Xiangqi (see `Horse`)
     *
     * @return a selected move adopting strategy specified by {@link this#behavior}
     */
    public Move getNextMove(){
        if(availableMoves.length == 0)
            return null;
        if(behavior == Behavior.RANDOM) {
            Random r = new Random();
            return availableMoves[r.nextInt(availableMoves.length)];
        } else if(behavior == Behavior.GREEDY) {
            return mostCloest(availableMoves);
        } else if(behavior == Behavior.CAPTURING) {
            return mostCapturing(availableMoves);
        } else if(behavior == Behavior.BLOCKING) {
            return block(availableMoves);
        }
        return null;
    }

    private Move block(Move[] availableMoves) {
        return mostCloest(availableMoves);
    }

    private Move mostCapturing(Move[] availableMoves) {
        List<Move> candinate = new ArrayList<Move>();
        for( var move : availableMoves) {
            if(game.getPiece(move.getDestination()) != null) {
                candinate.add(move);
            }
        }
        Move[] ret = candinate.toArray(Move[]::new);
        if(candinate == null || ret.length == 0)
            ret = availableMoves;
        return mostCloest(ret);
    }

    private Move mostCloest(Move[] availableMoves) {
        Place center = game.getCentralPlace();
        Move cloest = null;
        double minDitance = 2 * game.getConfiguration().getSize();
        for( var move : availableMoves) {
            if(cloest == null) {
                cloest = move;
                minDitance = getDistance(cloest.getDestination(), center);
            }else {
                double distance = getDistance(move.getDestination(), center);
                if(distance < minDitance) {
                    cloest = move;
                    minDitance = distance;
                } else if(distance == minDitance) {
                    double dist1 = getDistance(move.getSource(), center);
                    double dist2 = getDistance(cloest.getSource(), center);
                    if(dist1 > dist2) {
                        cloest = move;
                    }
                }
            }
        }
        return cloest;
    }

    public static double getDistance(Place a, Place b){
        return Math.sqrt((a.x() - b.x()) * (a.x() - b.x()) + (a.y() - b.y()) * (a.y() - b.y()));
    }
}

