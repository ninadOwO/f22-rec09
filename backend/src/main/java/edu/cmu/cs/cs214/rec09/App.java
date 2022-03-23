package edu.cmu.cs.cs214.rec09;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import edu.cmu.cs.cs214.rec09.framework.core.GameFrameworkImpl;
import edu.cmu.cs.cs214.rec09.framework.core.GamePlugin;
import edu.cmu.cs.cs214.rec09.framework.gui.GameState;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private GameFrameworkImpl game;
    private List<GamePlugin> plugins;

    /**
     * Start the server at :8080 port.
     * @throws IOException
     */
    public App() throws IOException {
        super(8080);

        this.game = new GameFrameworkImpl();
        plugins = loadPlugins();
        for (GamePlugin p: plugins){
            game.registerPlugin(p);
        }


        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();
        if (uri.equals("/plugin")) {
            // e.g., /plugin?i=0
            game.startNewGame(plugins.get(Integer.parseInt(params.get("i"))));
        } else if (uri.equals("/play")){
            // e.g., /play?x=1&y=1
            if (game.hasGame()) {
                game.playMove(Integer.parseInt(params.get("x")), Integer.parseInt(params.get("y")));
            }
        } else if (uri.equals("/start")){

        }
        // Extract the view-specific data from the game and apply it to the template.
        GameState gameplay = GameState.forGame(this.game);
        return newFixedLengthResponse(gameplay.toString());

    }


    /**
     * Load plugins listed in META-INF/services/...
     *
     * @return List of instantiated plugins
     */
    private static List<GamePlugin> loadPlugins() {
        ServiceLoader<GamePlugin> plugins = ServiceLoader.load(GamePlugin.class);
        List<GamePlugin> result = new ArrayList<>();
        for (GamePlugin plugin : plugins) {
            System.out.println("Loaded plugin " + plugin.getGameName());
            result.add(plugin);
        }
        return result;
    }

    public static class Test {
        public String getText() {
            return "Hello World!";
        }
    }
}

