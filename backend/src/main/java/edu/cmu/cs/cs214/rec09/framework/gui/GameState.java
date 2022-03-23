package edu.cmu.cs.cs214.rec09.framework.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.cmu.cs.cs214.rec09.framework.core.GameFrameworkImpl;

public class GameState {

    private final String name;
    private final String footer;
    private final Cell[] cells;
    private final Plugin[] plugins;
    private final String numColStyle;
    private final String currentPlayer;
    private final String gameOverMsg;

    private GameState(String name, String footer, Cell[] cells, Plugin[] plugins, String numColStyle, String currentPlayer, String gameOverMsg) {
        this.name = name;
        this.footer = footer;
        this.cells = cells;
        this.plugins = plugins;
        this.numColStyle = numColStyle;
        this.currentPlayer = currentPlayer;
        this.gameOverMsg = gameOverMsg;
    }

    public static GameState forGame(GameFrameworkImpl game) {
        String name = game.getGameName();
        String footer = game.getFooter();
        Cell[] cells = getCells(game);
        Plugin[] plugins = getPlugins(game);
        String numColStyle = getNumColStyle(game);
        String currentPlayer = game.getCurrentPlayerName();
        String gameOverMsg = game.getGameOverMsg();
        return new GameState(name,footer,cells,plugins,numColStyle, currentPlayer, gameOverMsg);
    }

    private static String getNumColStyle(GameFrameworkImpl game) {
        int numCols = game.getGridWidth();
        List<String> style = new ArrayList<String>();
        for (int i=0; i<numCols; i++){
            style.add("auto");
        }
        return String.join(" ", style);

    }

    private static Plugin[] getPlugins(GameFrameworkImpl game) {
        List<String> gamePlugins = game.getRegisteredPluginName();
        Plugin[] plugins = new Plugin[gamePlugins.size()];
        for (int i=0; i<gamePlugins.size(); i++){
            plugins[i] = new Plugin(gamePlugins.get(i));
        }
        return plugins;
    }

    private static Cell[] getCells(GameFrameworkImpl game) {
        int height = game.getGridHeight();
        int width = game.getGridWidth();
        Cell[] cells = new Cell[height*width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                String text = game.getSquare(x,y);
                boolean playable  = game.isSquarePlayable(x,y);
                cells[width * y + x] = new Cell(x, y, text, playable);
            }
        }
        return cells;
    }

    public Cell[] getCells() {
        return this.cells;
    }

    public String getName() {
        return name;
    }

    public String getFooter() {
        return footer;
    }

    public Plugin[] getPlugins() {
        return plugins;
    }

    public String getNumColStyle() {
        return numColStyle;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getGameOverMsg() {
        return gameOverMsg;
    }

    @Override
    public String toString() {
        return ("{ \"name\": \"" + this.name + "\"," +
                " \"footer\": \"" + this.footer + "\"," +
                " \"cells\": " +  Arrays.toString(cells) + "," +
                " \"plugins\": " +  Arrays.toString(plugins) + "," +
                " \"numColStyle\": \"" + this.numColStyle + "\"," +
                " \"currentPlayer\": \"" + this.currentPlayer + "\"," +
                " \"gameOverMsg\": \"" + this.gameOverMsg + "\"}").replace("null", "");

    }
}

