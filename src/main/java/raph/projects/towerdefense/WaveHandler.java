package raph.projects.towerdefense;

import raph.projects.towerdefense.entities.Enemy;
import raph.projects.towerdefense.entities.EnemyGroup;
import raph.projects.towerdefense.entities.Wave;

import java.util.List;

public class WaveHandler {

    private List<Wave> waves;
    private int currentWaveIndex;
    private int currentGroupIndex;
    private int enemiesToSpawn;
    private double spawnTimer;
    private double spawnInterval;
    private double groupDelay;
    private boolean waitingNextGroup;
    private double groupTimer;
    private boolean levelFinished;

    public WaveHandler(List<Wave> waves, double spawnInterval, double groupDelay) {
        this.waves = waves;
        this.currentWaveIndex = 0;
        this.currentGroupIndex = 0;
        this.spawnInterval = spawnInterval;
        this.groupDelay = groupDelay;
        this.spawnTimer = spawnInterval;
        this.waitingNextGroup = false;
        this.groupTimer = 0;
        this.levelFinished = false;
        this.enemiesToSpawn = waves.get(0).getGroups().get(0).getQuantity();
    }

    public void update(double dt) {
        if (this.levelFinished) return;

        if (this.waitingNextGroup)
        {
            this.groupTimer += dt;
            if (this.groupTimer >= this.groupDelay)
            {
                this.waitingNextGroup = false;
                this.groupTimer = 0;
                this.currentGroupIndex++;
                this.enemiesToSpawn = getCurrentWave().getGroups().get(this.currentGroupIndex).getQuantity();
                this.spawnTimer = this.spawnInterval;
            }
            return;
        }

        this.spawnTimer += dt;
    }

    public boolean shouldSpawn() {
        if (this.levelFinished || this.waitingNextGroup) return false;
        if (this.enemiesToSpawn <= 0) return false;
        if (this.spawnTimer < this.spawnInterval) return false;

        this.spawnTimer = 0;
        return true;
    }

    public Enemy getNextEnemy()
    {
        EnemyGroup group = getCurrentWave().getGroups().get(this.currentGroupIndex);
        int index = group.getQuantity() - this.enemiesToSpawn;
        return group.getEnemies().get(index);
    }

    public void decrementSpawn()
    {
        this.enemiesToSpawn--;
        if (this.enemiesToSpawn == 0)
        {
            if (this.currentGroupIndex < getCurrentWave().getGroups().size() - 1)
            {
                this.waitingNextGroup = true;
            }
        }
    }

    public void notifyWaveCleared() {
        if (this.currentWaveIndex < this.waves.size() - 1) {
            this.currentWaveIndex++;
            this.currentGroupIndex = 0;
            this.spawnTimer = spawnInterval;
            this.waitingNextGroup = false;
            this.groupTimer = 0;
            this.levelFinished = false;
            this.enemiesToSpawn = getCurrentWave().getGroups().get(0).getQuantity();
        } else {
            this.levelFinished = true;
        }
    }

    public boolean isWaveFinished() {
        return this.enemiesToSpawn == 0
                && !this.waitingNextGroup
                && this.currentGroupIndex >= getCurrentWave().getGroups().size() - 1;
    }

    public boolean isLevelFinished() {
        return this.levelFinished;
    }

    public int getCurrentWaveIndex() {
        return this.currentWaveIndex;
    }

    private Wave getCurrentWave() {
        return this.waves.get(this.currentWaveIndex);
    }
}