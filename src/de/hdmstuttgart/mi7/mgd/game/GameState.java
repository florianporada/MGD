package de.hdmstuttgart.mi7.mgd.game;

/**
 * Created by florianporada on 28.08.15.
 */
public interface GameState {
    public abstract void initialize(Game game);
    public abstract void loadContent(Game game);
    public abstract void update(Game game, float deltaSeconds);
    public abstract void draw(Game game, float deltaSeconds);
    public abstract void resize(Game game, int width, int height);
    public abstract void pause(Game game);
    public abstract void resume(Game game);
    public abstract void shutdown(Game game);
}
