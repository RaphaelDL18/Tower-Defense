package raph.projects.towerdefense;

import raph.projects.towerdefense.entities.Base;
import raph.projects.towerdefense.entities.EnemyGroup;
import raph.projects.towerdefense.entities.EnemyType;
import raph.projects.towerdefense.entities.Wave;

import java.util.ArrayList;
import java.util.List;

public class Level
{

    private int id;
    private Map map;
    private Phase phase;
    private Base base;
    private List<Wave> waves;
    private int currentWave;
    private int gold;

    public Level(int i,Map m)
    {
        this.id = i;
        this.map = m;
        this.phase = Phase.CONSTRUCTION;
        this.base = new Base();
        this.gold = switch(i) {
            case 1 -> 300;
            case 2 -> 350;
            case 3 -> 400;
            case 4 -> 450;
            case 5 -> 500;
            default -> 300;
        };

        this.currentWave = 0;
        this.waves = new ArrayList<Wave>();
        initWaves();
    }

    public Base getBase() {
        return base;
    }

    public Map getMap()
    {
        return this.map;
    }

    public void setPhase(Phase p)
    {
        this.phase = p;
    }

    public int getCurrentWave()
    {
        return this.currentWave;
    }

    private void initWaves()
    {
        switch(this.id)
        {
            case 1 -> {
                this.waves.add(new Wave());
                this.waves.get(0).addGroup(new EnemyGroup(EnemyType.GOBELIN, 5, this.map.getEnemyPath(), this.id));
                this.waves.add(new Wave());
                this.waves.get(1).addGroup(new EnemyGroup(EnemyType.GOBELIN, 8, this.map.getEnemyPath(), this.id));
                this.waves.add(new Wave());
                this.waves.get(2).addGroup(new EnemyGroup(EnemyType.GOBELIN, 12, this.map.getEnemyPath(), this.id));
                this.waves.add(new Wave());
                this.waves.get(3).addGroup(new EnemyGroup(EnemyType.GOBELIN, 16, this.map.getEnemyPath(), this.id));
                this.waves.add(new Wave());
                this.waves.get(4).addGroup(new EnemyGroup(EnemyType.GOBELIN, 22, this.map.getEnemyPath(), this.id));
            }
            default ->
                    {

                    }
        }
    }

    public Phase getPhase()
    {
        return this.phase;
    }

    public List<Wave> getWaves()
    {
        return this.waves;
    }

    public int getGold() { return this.gold; }

    public void addGold(int amount) { this.gold += amount; }

    public boolean spendGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            return true;
        }
        return false;
    }



}
