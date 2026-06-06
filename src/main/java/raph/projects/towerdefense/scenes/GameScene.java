package raph.projects.towerdefense.scenes;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import raph.projects.towerdefense.Level;
import raph.projects.towerdefense.Map;
import raph.projects.towerdefense.Phase;
import raph.projects.towerdefense.WaveHandler;
import raph.projects.towerdefense.entities.Enemy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static raph.projects.towerdefense.Map.HEIGHT;
import static raph.projects.towerdefense.Map.WIDTH;

public class GameScene {

    private Scene scene;
    private Level level;
    private AnimationTimer gameLoop;
    private long lastUpdate;
    private WaveHandler waveHandler;
    private List<Enemy> enemies;
    private Pane paneMap;
    private Pane paneEnemies;

    public GameScene(Stage stage, int id) throws IOException
    {
        this.level = new Level(id, new Map(id));
        this.lastUpdate = 0;
        this.enemies = new ArrayList<>();
        this.waveHandler = new WaveHandler(level.getWaves(), 1.5, 3.0);

        // --- Panes ---
        this.paneMap = new Pane();
        for (int i = 0; i < HEIGHT; i++)
        {
            for (int j = 0; j < WIDTH; j++)
            {
                this.level.getMap().getTileTemplate()[i][j].getSprite().play();
                this.paneMap.getChildren().add(this.level.getMap().getTileTemplate()[i][j].getSprite().getCurrentFrame());
            }
        }

        this.paneEnemies = new Pane();
        Pane paneTowers  = new Pane();
        Pane paneEffects = new Pane();
        Pane paneHUD     = new Pane();
        paneHUD.setPrefSize(1920, 184);

        StackPane gameRoot = new StackPane(paneMap, paneTowers, paneEnemies, paneEffects);
        gameRoot.setPrefSize(1920, 896);

        VBox layout = new VBox(gameRoot, paneHUD);
        this.scene = new Scene(layout, 1920, 1080, Color.BLACK);

        // --- Game loop ---
        gameLoop = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                if (lastUpdate == 0)
                {
                    lastUpdate = now;
                    return;
                }
                double dt = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                update(dt);
            }
        };
    }

    public Scene getScene() {
        return this.scene;
    }

    public void startAttackPhase() {
        level.setPhase(Phase.DEFENSE);
        gameLoop.start();
    }

    public void stopAttackPhase() {
        gameLoop.stop();
        level.setPhase(Phase.CONSTRUCTION);
    }

    private void update(double dt) {
        if (level.getPhase() == Phase.DEFENSE) {
            // spawn
            waveHandler.update(dt);
            if (waveHandler.shouldSpawn()) {
                spawnEnemy();
                waveHandler.decrementSpawn();
            }

            // déplacement
            updateEnemies(dt);

            // fin de vague
            if (waveHandler.isWaveFinished() && enemies.isEmpty()) {
                if (waveHandler.isLevelFinished()) {
                    // TODO : niveau terminé → GameOverScene ou écran victoire
                } else {
                    waveHandler.notifyWaveCleared();
                    stopAttackPhase();
                }
            }
        }
    }

    private void updateEnemies(double dt)
    {
        for (Enemy e : enemies) {
            e.update(dt);
        }
        // supprime les ennemis morts ou arrivés à la base
        enemies.removeIf(e -> {
            if (!e.isAlive() || e.hasReachedBase()) {
                paneEnemies.getChildren().remove(e.getSprite().getCurrentFrame());
                return true;
            }
            return false;
        });
    }

    private void spawnEnemy()
    {
        Enemy e = waveHandler.getNextEnemy();
        enemies.add(e);
        e.getSprite().play();
        paneEnemies.getChildren().add(e.getSprite().getCurrentFrame());
    }
}


