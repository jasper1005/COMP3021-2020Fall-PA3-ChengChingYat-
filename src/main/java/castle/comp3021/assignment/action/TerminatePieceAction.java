package castle.comp3021.assignment.action;

import castle.comp3021.assignment.player.ComputerPlayer;
import castle.comp3021.assignment.player.ConsolePlayer;
import castle.comp3021.assignment.protocol.Action;
import castle.comp3021.assignment.protocol.Game;
import castle.comp3021.assignment.protocol.Piece;
import castle.comp3021.assignment.protocol.Place;
import castle.comp3021.assignment.protocol.exception.ActionException;

/**
 * The action to terminate a piece.
 * <p>
 * The piece must belong to {@link ComputerPlayer}.
 * After terminated, the piece cannot be paused or resumed.
 */
public class TerminatePieceAction extends Action {
    /**
     * @param game the current {@link Game} object
     * @param args the arguments input by users in the console
     */
    public TerminatePieceAction(Game game, String[] args) {
        super(game, args);
    }

    /**
     * Terminate the piece according to {@link this#args}
     * Expected {@link this#args}: "a1"
     * Hint:
     * Consider corner cases (e.g., invalid {@link this#args})
     * Throw {@link ActionException} when exception happens.
     * <p>
     * Related meethods:
     * - {@link Piece#terminate()}
     * - {@link Thread#interrupt()}
     * <p>
     * - The piece thread can be get by
     * {@link castle.comp3021.assignment.protocol.Configuration#getPieceThread(Piece)}
     */
    @Override
    public void perform() throws ActionException {
        if(args.length < 1)
            throw new ActionException("empty args");
        var place = ConsolePlayer.parsePlace(args[0]);
        if(place == null)
            throw new ActionException("invalid place: can not parse place");
        if(place.y() < 0 || place.y() >= game.getConfiguration().getSize() ||
                place.x() < 0 || place.x() >= game.getConfiguration().getSize())
            throw new ActionException("invalid place: not legal position in board");
        var piece = game.getPiece(place);
        if(piece == null)
            throw new ActionException("can not terminate an empty piece");
        if(!(piece.getPlayer() instanceof ComputerPlayer))
            throw new ActionException("the piece is not belonging to ComputerPlayer");
        piece.terminate();
    }

    @Override
    public String toString() {
        return "Action[Terminate piece]";
    }
}