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

    public Level(int i,Map m)
    {
        this.id = i;
        this.map = m;
        this.phase = Phase.CONSTRUCTION;
        this.base = new Base();

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
            case 1 ->
                    {
                        //First wave
                        this.waves.add(new Wave());
                        this.waves.get(0).addGroup(new EnemyGroup(EnemyType.GOBELIN,5,this.map.getEnemyPath()));
                        //Second wave
                        this.waves.add(new Wave());
                        this.waves.get(1).addGroup(new EnemyGroup(EnemyType.GOBELIN,8,this.map.getEnemyPath()));
                        //Third wave
                        this.waves.add(new Wave());
                        this.waves.get(2).addGroup(new EnemyGroup(EnemyType.GOBELIN,12,this.map.getEnemyPath()));
                        //Fourth wave
                        this.waves.add(new Wave());
                        this.waves.get(3).addGroup(new EnemyGroup(EnemyType.GOBELIN,16,this.map.getEnemyPath()));
                        //Fifth wave
                        this.waves.add(new Wave());
                        this.waves.get(4).addGroup(new EnemyGroup(EnemyType.GOBELIN,22,this.map.getEnemyPath()));

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



}
