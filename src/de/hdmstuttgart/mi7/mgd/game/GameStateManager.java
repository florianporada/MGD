package de.hdmstuttgart.mi7.mgd.game;

/**
 * Created by florianporada on 28.08.15.
 */
public class GameStateManager {
    private Game game;
    private GameState currentState;

    public GameStateManager(Game game) {
        this.game = game;
    }

    public GameState getGameState() {
        return currentState;
    }

    public void setGameState(GameState newState) {
        if (currentState != null) {
            currentState.shutdown(game);
            currentState = null;
        }

        if (newState != null) {
            newState.initialize(game);
            if (game.isInitialized())
                newState.loadContent(game);

            currentState = newState;
        }
    }

    public void loadContent() {
        if (currentState != null) {
            currentState.loadContent(game);
        }
    }

    public void update(float deltaSeconds) {
        if (currentState != null) {
            currentState.update(game, deltaSeconds);
        }
    }

    public void draw(float deltaSeconds) {
        if (currentState != null) {
            currentState.draw(game, deltaSeconds);
        }
    }

    public void resize(int width, int height) {
        if (currentState != null) {
            currentState.resize(game, width, height);
        }
    }

    public void pause() {
        if (currentState != null) {
            currentState.pause(game);
        }
    }

    public void resume() {
        if (currentState != null) {
            currentState.resume(game);
        }
    }
}
