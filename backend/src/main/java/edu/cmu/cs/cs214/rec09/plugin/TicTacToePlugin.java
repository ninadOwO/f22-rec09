package edu.cmu.cs.cs214.rec09.plugin;

import edu.cmu.cs.cs214.rec09.framework.core.GameFramework;
import edu.cmu.cs.cs214.rec09.framework.core.GamePlugin;
import edu.cmu.cs.cs214.rec09.games.TicTacToe;

public class TicTacToePlugin implements GamePlugin<TicTacToe.Player> {
    private static final String GAME_START_FOOTER = "Let's play Tic Tac Toe!";
    private GameFramework framework;
    private TicTacToe game;

    @Override
    public String getGameName() {
        return "Tic Tac Toe";
    }

    @Override
    public int getGridWidth() {
        return TicTacToe.SIZE;
    }

    @Override
    public int getGridHeight() {
        return TicTacToe.SIZE;
    }

    @Override
    public void onRegister(GameFramework framework) {
        this.framework = framework;
    }

    @Override
    public void onNewGame() {
        framework.setFooterText(GAME_START_FOOTER);
        game = new TicTacToe();
    }

    @Override
    public void onNewMove() {
        // Nothing to do
    }

    @Override
    public boolean isMoveValid(int x, int y) {
        return game.isValidPlay(x, y);
    }

    @Override
    public boolean isMoveOver() {
        return true;
    }

    @Override
    public void onMovePlayed(int x, int y) {
        framework.setSquare(x, y, game.currentPlayer().toString());
        game.play(x, y);
    }

    @Override
    public boolean isGameOver() {
        return game.isOver();
    }

    @Override
    public String getGameOverMessage() {
        TicTacToe.Player possibleWinner = game.winner();
        if (possibleWinner == null) return "The game ended in a tie.";
        return String.format("%s won!", possibleWinner);
    }

    @Override
    public void onGameClosed() {
        // Nothing to do.
    }

    @Override
    public TicTacToe.Player currentPlayer() {
        return game.currentPlayer();
    }
}