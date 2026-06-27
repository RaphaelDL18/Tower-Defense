package raph.projects.towerdefense.scenes;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import raph.projects.towerdefense.*;
import raph.projects.towerdefense.entities.Enemy;
import raph.projects.towerdefense.entities.Projectile;
import raph.projects.towerdefense.entities.Tower;
import raph.projects.towerdefense.entities.TowerType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static raph.projects.towerdefense.Map.HEIGHT;
import static raph.projects.towerdefense.Map.WIDTH;

public class GameScene {

    private Stage stage;
    private Scene scene;
    private Level level;
    private int id;
    private boolean baseDestroyed;
    private boolean gameOverShown;

    private AnimationTimer gameLoop;
    private long lastUpdate;
    private WaveHandler waveHandler;
    private List<Enemy> enemies;

    private List<Tower> towers;
    private TowerType selectedTower;
    private ImageView ghostTower;
    private Circle range;

    private List<Projectile> projectiles;

    private Pane paneMap;
    private Pane paneEnemies;
    private Pane paneEffects;
    private Pane paneTowers;
    private Pane paneProjectiles;
    private StackPane paneGameOver;
    private StackPane gameRoot;
    private StackPane rootStack;

    private Label goldLabel;
    private Label livesLabel;
    private Label waveLabel;
    private Button tower_1, tower_2, tower_3, tower_4, tower_5;

    public GameScene(Stage stage, int id) throws IOException
    {
        this.stage = stage;
        this.level = new Level(id, new Map(id));
        this.id = id;
        this.baseDestroyed = false;
        this.gameOverShown = false;

        this.lastUpdate = 0;
        this.enemies = new ArrayList<>();
        this.waveHandler = new WaveHandler(level.getWaves(), 1.5, 3.0);

        this.selectedTower = null;
        this.range = new Circle();
        this.towers = new ArrayList<>();
        this.projectiles = new ArrayList<>();

        this.paneEffects = new Pane();
        this.paneEffects.setMouseTransparent(true);

        this.paneMap = new Pane();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                this.level.getMap().getTileTemplate()[i][j].getSprite().play();
                this.paneMap.getChildren().add(this.level.getMap().getTileTemplate()[i][j].getSprite().getCurrentFrame());
            }
        }

        this.paneEnemies = new Pane();
        this.paneTowers = new Pane();
        this.paneProjectiles = new Pane();
        this.paneGameOver = new StackPane();

        this.ghostTower = new ImageView();
        this.ghostTower.setFitWidth(64);
        this.ghostTower.setFitHeight(64);
        this.ghostTower.setOpacity(0.5);
        this.ghostTower.setVisible(false);
        this.paneEffects.getChildren().add(ghostTower);

        this.gameRoot = new StackPane(paneMap, paneTowers, paneEnemies, paneProjectiles, paneEffects);
        this.gameRoot.setPrefSize(1920, 896);
        this.gameRoot.setMinSize(1920, 896);
        this.gameRoot.setMaxSize(1920, 896);

        gameRoot.setOnMouseMoved(event -> {
            if (selectedTower == null) return;

            int col = (int) (event.getX() / Tile.TILE_SIZE);
            int row = (int) (event.getY() / Tile.TILE_SIZE);

            if (col < 0 || col >= WIDTH || row < 0 || row >= HEIGHT) return;

            ghostTower.setX(col * Tile.TILE_SIZE);
            ghostTower.setY(row * Tile.TILE_SIZE);
            ghostTower.setVisible(true);

            Tile tile = level.getMap().getTileTemplate()[row][col];
            boolean placable = tile.getType() == TileType.TOWER_SLOT;

            if (placable) {
                ghostTower.setStyle("-fx-effect: dropshadow(gaussian, green, 10, 0.5, 0, 0);");
                this.range.setStroke(Color.GREEN);
            } else {
                ghostTower.setStyle("-fx-effect: dropshadow(gaussian, red, 10, 0.5, 0, 0);");
                this.range.setStroke(Color.RED);
            }

            buildRangeCircle(this.choseRange());
            range.setCenterX(col * Tile.TILE_SIZE + Tile.TILE_SIZE / 2.0);
            range.setCenterY(row * Tile.TILE_SIZE + Tile.TILE_SIZE / 2.0);
            if (!paneEffects.getChildren().contains(range))
                paneEffects.getChildren().add(range);
        });

        gameRoot.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                cancelPlacement();
                this.range.setVisible(false);
            } else if (event.getButton() == MouseButton.PRIMARY && selectedTower != null) {
                int col = (int) (event.getX() / Tile.TILE_SIZE);
                int row = (int) (event.getY() / Tile.TILE_SIZE);

                if (col < 0 || col >= WIDTH || row < 0 || row >= HEIGHT) return;

                Tile tile = level.getMap().getTileTemplate()[row][col];
                boolean placable = tile.getType() == TileType.TOWER_SLOT;

                if (placable) {
                    this.placeTower(this.selectedTower, col, row);
                    this.selectedTower = null;
                }
            }
        });

        HBox hud = buildHUD();

        VBox layout = new VBox(gameRoot, hud);
        layout.setPrefSize(1920, 1080);

        this.rootStack = new StackPane(layout);
        this.rootStack.setPrefSize(1920, 1080);

        this.scene = new Scene(rootStack, 1920, 1080, Color.BLACK);

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) { lastUpdate = now; return; }
                double dt = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;
                update(dt);
                if (baseDestroyed && !gameOverShown) {
                    gameOverShown = true;
                    gameOver();
                }
            }
        };
    }

    private HBox buildHUD()
    {
        tower_1 = makeButton("/raph/projects/towerdefense/Images/Blank.png", TowerType.CANNON.getCost());
        tower_1.setOnAction(e -> selectTower(TowerType.CANNON));

        tower_2 = makeButton("/raph/projects/towerdefense/Images/Blank.png", TowerType.ARCANE.getCost());
        tower_2.setOnAction(e -> selectTower(TowerType.ARCANE));

        tower_3 = makeButton("/raph/projects/towerdefense/Images/Blank.png", TowerType.BOMB.getCost());
        tower_3.setOnAction(e -> selectTower(TowerType.BOMB));

        tower_4 = makeButton("/raph/projects/towerdefense/Images/Blank.png", TowerType.FROST.getCost());
        tower_4.setOnAction(e -> selectTower(TowerType.FROST));

        tower_5 = makeButton("/raph/projects/towerdefense/Images/Blank.png", TowerType.BALLISTA.getCost());
        tower_5.setOnAction(e -> selectTower(TowerType.BALLISTA));

        Button play = makeButton("/raph/projects/towerdefense/Images/HUD/Play.png", -1);
        play.setOnAction(e -> this.startAttackPhase());

        this.goldLabel  = makeLabel("💰 " + level.getGold());
        this.livesLabel = makeLabel("❤️ " + level.getBase().getHealth());
        this.waveLabel  = makeLabel("Wave " + (waveHandler.getCurrentWaveIndex() + 1) + "/" + level.getWaves().size());

        HBox towersBox = new HBox(10, tower_1, tower_2, tower_3, tower_4, tower_5);
        towersBox.setAlignment(Pos.CENTER_LEFT);

        HBox infoBox = new HBox(40, goldLabel, livesLabel, waveLabel);
        infoBox.setAlignment(Pos.CENTER);

        Region spacerLeft  = new Region();
        Region spacerRight = new Region();
        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        HBox hud = new HBox(20, towersBox, spacerLeft, infoBox, spacerRight, play);
        hud.setAlignment(Pos.CENTER);
        hud.setPrefSize(1920, 184);
        hud.setMinSize(1920, 184);
        hud.setMaxSize(Double.MAX_VALUE, 184);
        hud.setPadding(new Insets(0, 20, 0, 20));
        hud.setStyle("-fx-background-color: black;");

        updateTowerButtons();

        return hud;
    }

    private Button makeButton(String path, int cost)
    {
        Image img = new Image(getClass().getResourceAsStream(path));
        ImageView iv = new ImageView(img);
        iv.setFitWidth(140);
        iv.setFitHeight(140);
        iv.setPreserveRatio(true);

        Button b = new Button();
        if (cost >= 0) {
            Label costLabel = new Label(cost + "💰");
            costLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");
            VBox content = new VBox(2, iv, costLabel);
            content.setAlignment(Pos.CENTER);
            b.setGraphic(content);
        } else {
            b.setGraphic(iv);
        }

        b.setPrefSize(140, 140);
        b.setMinSize(140, 140);
        b.setMaxSize(140, 140);
        return b;
    }

    private Label makeLabel(String text)
    {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 28px; -fx-text-fill: white;");
        return l;
    }

    public Scene getScene() { return this.scene; }

    public void startAttackPhase()
    {
        level.setPhase(Phase.DEFENSE);
        gameLoop.start();
    }

    public void stopAttackPhase()
    {
        gameLoop.stop();
        level.setPhase(Phase.CONSTRUCTION);
    }

    private void update(double dt)
    {
        if (level.getPhase() == Phase.DEFENSE) {
            waveHandler.update(dt);
            if (waveHandler.shouldSpawn()) {
                spawnEnemy();
                waveHandler.decrementSpawn();
            }
            this.updateEnemies(dt);
            this.updateBase();
            if (waveHandler.isWaveFinished() && enemies.isEmpty()) {
                if (waveHandler.isLevelFinished()) {
                    gameOverShown = true;
                    gameOver();
                } else {
                    waveHandler.notifyWaveCleared();
                    updateWaveLabel();
                    stopAttackPhase();
                }
            }
            this.updateTowers(dt);
            this.updateProjectiles(dt);
        }
    }

    private void updateEnemies(double dt)
    {
        for (Enemy e : enemies) {
            e.update(dt);
            if (e.hasReachedBase()) {
                this.level.getBase().damaged(e.getDamage());
            }
        }
        enemies.removeIf(e -> {
            if (!e.isAlive() || e.hasReachedBase()) {
                if (!e.isAlive()) {
                    level.addGold(e.getGoldReward());
                    updateGoldLabel();
                }
                paneEnemies.getChildren().remove(e.getSprite().getCurrentFrame());
                return true;
            }
            return false;
        });
        updateLivesLabel();
    }

    private void spawnEnemy()
    {
        Enemy e = waveHandler.getNextEnemy();
        enemies.add(e);
        e.getSprite().play();
        paneEnemies.getChildren().add(e.getSprite().getCurrentFrame());
    }

    private void selectTower(TowerType t)
    {
        if (level.getGold() < t.getCost()) return; // sécurité si bouton pas désactivé à temps

        this.selectedTower = t;
        String path = switch (t) {
            case CANNON -> "/raph/projects/towerdefense/Images/Towers/Icons/Cannon_Icon.png";
            default -> "/raph/projects/towerdefense/Images/Blank.png";
        };
        ghostTower.setImage(new Image(getClass().getResourceAsStream(path)));
    }

    private void cancelPlacement()
    {
        selectedTower = null;
        ghostTower.setVisible(false);
    }

    private void buildRangeCircle(int range)
    {
        this.range.setRadius(range * Tile.TILE_SIZE);
        this.range.setFill(Color.TRANSPARENT);
        this.range.setStrokeWidth(2);
        this.range.setMouseTransparent(true);
        this.range.setVisible(true);
    }

    private int choseRange()
    {
        return switch (this.selectedTower) {
            case CANNON   -> 2;
            case ARCANE   -> 3;
            case BOMB     -> 2;
            case FROST    -> 2;
            case BALLISTA -> 5;
            default       -> 0;
        };
    }

    private void placeTower(TowerType type, int col, int row)
    {
        if (!level.spendGold(type.getCost())) return;

        Tower t = new Tower(type, "/raph/projects/towerdefense/Images/Towers/Icons/Cannon_Icon.png");
        this.towers.add(t);
        t.getSprite().play();
        t.move(col * Tile.TILE_SIZE, row * Tile.TILE_SIZE);
        t.getSprite().getCurrentFrame().setX(col * Tile.TILE_SIZE);
        t.getSprite().getCurrentFrame().setY(row * Tile.TILE_SIZE);
        this.paneTowers.getChildren().add(t.getSprite().getCurrentFrame());
        this.range.setVisible(false);
        this.ghostTower.setVisible(false);

        updateGoldLabel();
    }

    private void updateTowers(double dt)
    {
        for (Tower t : this.towers) {
            t.update(dt, this.enemies);
            Projectile p = t.shoot();
            if (p != null) {
                this.projectiles.add(p);
                this.paneProjectiles.getChildren().add(p.getSprite().getCurrentFrame());
            }
        }
    }

    private void updateProjectiles(double dt)
    {
        for (Projectile p : projectiles) p.update(dt);
        projectiles.removeIf(p -> {
            if (p.isFinished()) {
                paneProjectiles.getChildren().remove(p.getSprite().getCurrentFrame());
                return true;
            }
            return false;
        });
    }

    private void updateBase()
    {
        if (!this.level.getBase().isAlive()) this.baseDestroyed = true;
    }

    private void updateGoldLabel()
    {
        goldLabel.setText("💰 " + level.getGold());
        updateTowerButtons();
    }

    private void updateLivesLabel()
    {
        livesLabel.setText("❤️ " + level.getBase().getHealth());
    }

    private void updateTowerButtons()
    {
        tower_1.setDisable(level.getGold() < TowerType.CANNON.getCost());
        tower_2.setDisable(level.getGold() < TowerType.ARCANE.getCost());
        tower_3.setDisable(level.getGold() < TowerType.BOMB.getCost());
        tower_4.setDisable(level.getGold() < TowerType.FROST.getCost());
        tower_5.setDisable(level.getGold() < TowerType.BALLISTA.getCost());
    }

    private void gameOver()
    {
        gameLoop.stop();
        for (Enemy e : this.enemies) e.getSprite().stop();

        rootStack.getChildren().get(0).setEffect(new GaussianBlur(10));

        if (baseDestroyed) initGameOver();
        else initVictory();
    }

    private void initGameOver()
    {
        Rectangle overlay = new Rectangle(1920, 1080);
        overlay.setFill(Color.rgb(0, 0, 0, 0.5));

        Label title = new Label("GAME OVER");
        title.setStyle("-fx-font-size: 80px; -fx-text-fill: red; -fx-font-weight: bold;");

        Button retry = new Button("Rejouer");
        retry.setOnAction(e -> {
            try {
                stage.setScene(new GameScene(stage, id).getScene());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button menu = new Button("Menu principal");
        menu.setOnAction(e -> {
            try {
                stage.setScene(new MenuScene(stage).getScene());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox content = new VBox(30, title, retry, menu);
        content.setAlignment(Pos.CENTER);

        this.paneGameOver = new StackPane(overlay, content);
        this.paneGameOver.setPrefSize(1920, 1080);

        rootStack.getChildren().add(paneGameOver);
    }

    private void initVictory()
    {
        Rectangle overlay = new Rectangle(1920, 1080);
        overlay.setFill(Color.rgb(0, 0, 0, 0.5));

        Label title = new Label("VICTORY !");
        title.setStyle("-fx-font-size: 80px; -fx-text-fill: gold; -fx-font-weight: bold;");

        Button menu = new Button("Menu principal");
        menu.setOnAction(e -> {
            try {
                stage.setScene(new MenuScene(stage).getScene());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox content = new VBox(30, title, menu);
        content.setAlignment(Pos.CENTER);

        this.paneGameOver = new StackPane(overlay, content);
        this.paneGameOver.setPrefSize(1920, 1080);

        rootStack.getChildren().add(paneGameOver);
    }

    private void updateWaveLabel()
    {
        int current = waveHandler.getCurrentWaveIndex() + 1; // +1 car index commence à 0
        int total = level.getWaves().size();
        waveLabel.setText("Wave " + current + "/" + total);
    }
}