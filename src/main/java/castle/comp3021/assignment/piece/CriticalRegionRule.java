package castle.comp3021.assignment.piece;

import castle.comp3021.assignment.protocol.*;

public class CriticalRegionRule implements Rule {


    /**
     * Validate whether the proposed  move will violate the critical region rule
     * I.e., there are no more than {@link Configuration#getCriticalRegionCapacity()} in the critical region.
     * Determine whether the move is in critical region, using {@link this#isInCriticalRegion(Game, Place)}
     * @param game the current game object
     * @param move the move to be validated
     * @return whether the given move is valid or not
     */
    @Override
    public boolean validate(Game game, Move move) {
        boolean isInBefore = isInCriticalRegion(game, move.getSource());
        boolean isInAfter = isInCriticalRegion(game, move.getDestination());
        if(isInAfter && isInBefore || !isInAfter)
            return true;
        return game.getConfiguration().getCriticalRegionCapacity() > getCurrentNumberInCriticalRegion(game, move);
    }

    private int getCurrentNumberInCriticalRegion(Game game, Move move) {
        int size = game.getConfiguration().getCriticalRegionSize();
        int start = (game.getConfiguration().getSize() - size) / 2;
        int stop = (game.getConfiguration().getSize() + size) / 2;
        int count = 0;
        for(int i = start; i != stop; ++i) {
            for(int j = 0; j < game.getConfiguration().getSize(); ++j) {
                if(game.getPiece(j, i) != null && !move.getDestination().equals(new Place(j,i)))
                    ++count;
            }
        }
        return count;
    }

    /**
     * Check whether the given move is in critical region
     * Critical region is {@link Configuration#getCriticalRegionSize()} of rows, centered around center place
     * Example:
     *      In a 5 * 5 board, which center place lies in the 3rd row
     *      Suppose critical region size = 3, then for row 1-5, the critical region include row 2-4.
     * @param game the current game object
     * @param place the move to be validated
     * @return whether the given move is in critical region
     */
    private boolean isInCriticalRegion(Game game, Place place) {
        int size = game.getConfiguration().getCriticalRegionSize();
        return place.y() >= (game.getConfiguration().getSize() - size) / 2 &&
                place.y() < (game.getConfiguration().getSize() + size) / 2;
    }

    @Override
    public String getDescription() {
        return "critical region is full";
    }
}
