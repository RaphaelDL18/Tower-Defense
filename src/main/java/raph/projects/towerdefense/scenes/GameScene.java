package raph.projects.towerdefense.scenes;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
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
    private Pane paneHUD;
    private StackPane gameRoot;

    public GameScene(Stage stage, int id) throws IOException
    {
        this.level = new Level(id, new Map(id));
        this.lastUpdate = 0;
        this.enemies = new ArrayList<>();
        this.waveHandler = new WaveHandler(level.getWaves(), 1.5, 3.0);

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

        this.gameRoot = new StackPane(paneMap, paneTowers, paneEnemies, paneEffects);
        gameRoot.setPrefSize(1920, 896);

        this.paneHUD = new Pane();
        this.initHUD();

        this.paneHUD.setPrefSize(1920, 184);



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

    private void initHUD()
    {
        Region spacerTop = new Region();
        spacerTop.setPrefHeight(17);
        spacerTop.setMaxHeight(17);

        Region spacerBottom = new Region();
        spacerBottom.setPrefHeight(17);
        spacerBottom.setMaxHeight(17);

        Button tower_1 = new Button("Tower_1");
        tower_1.setPrefSize(150, 150);
        tower_1.setMinSize(150, 150);
        tower_1.setMaxSize(150, 150);

        Button tower_2 = new Button("Tower_2");
        tower_2.setPrefSize(150, 150);
        tower_2.setMinSize(150, 150);
        tower_2.setMaxSize(150, 150);

        Button tower_3 = new Button("Tower_3");
        tower_3.setPrefSize(150, 150);
        tower_3.setMinSize(150, 150);
        tower_3.setMaxSize(150, 150);

        Button tower_4 = new Button("Tower_4");
        tower_4.setPrefSize(150, 150);
        tower_4.setMinSize(150, 150);
        tower_4.setMaxSize(150, 150);

        Button tower_5 = new Button("Tower_5");
        tower_5.setPrefSize(150, 150);
        tower_5.setMinSize(150, 150);
        tower_5.setMaxSize(150, 150);

        Label goldLabel = new Label("💰 250");
        Label livesLabel = new Label("❤️ 20");
        Label waveLabel = new Label("Wave 1/5");

        Button play = new Button("Play");
        play.setPrefSize(150, 150);
        play.setMinSize(150, 150);
        play.setMaxSize(150, 150);

        HBox subLayout = new HBox(tower_1,tower_2,tower_3,tower_4,tower_5,goldLabel,livesLabel,waveLabel,play);
        subLayout.setPrefWidth(1920);
        subLayout.setAlignment(Pos.CENTER_LEFT);
        subLayout.setSpacing(20);
        subLayout.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(subLayout, Priority.ALWAYS);
        HBox.setMargin(goldLabel, new Insets(0, 20, 0, 40)); // marge à gauche des labels

        VBox layout = new VBox(spacerTop, subLayout, spacerBottom );
        layout.setPrefSize(1920, 184);
        VBox.setVgrow(this.gameRoot, Priority.ALWAYS);

        this.paneHUD.getChildren().add(layout);

    }
}


