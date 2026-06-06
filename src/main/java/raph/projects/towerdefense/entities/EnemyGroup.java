package raph.projects.towerdefense.entities;

import raph.projects.towerdefense.Tile;

import java.util.ArrayList;
import java.util.List;

public class EnemyGroup
{
    private EnemyType type;
    private int quantity;
    private List<Enemy> enemies;

    public EnemyGroup(EnemyType t, int q, Tile[] path)
    {
        this.type = t;
        this.quantity = q;
        this.enemies = new ArrayList<Enemy>();
        for(int i = 0; i < q; i++)
        {
            this.enemies.add(new Enemy(this.type,path));
        }
    }

    public EnemyType getType()
    {
        return this.type;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public List<Enemy> getEnemies()
    {
        return this.enemies;
    }
}
