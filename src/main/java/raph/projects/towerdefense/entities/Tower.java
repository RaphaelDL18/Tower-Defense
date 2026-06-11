package raph.projects.towerdefense.entities;

import raph.projects.towerdefense.Sprite;
import raph.projects.towerdefense.Tile;

import java.util.List;

public class Tower extends Entity implements DamageDealer
{
    private TowerType type;
    private int range;
    private double fireRate;
    private int damage;
    private Enemy target;
    private double timeSinceLastShot;

    public Tower(TowerType t, String path)
    {
        super(new Sprite(path,1,64,64,1));
        this.target = null;
        this.type = t;

        switch(this.type)
        {
            case CANNON ->
            {
                this.range = 2;
                this.fireRate = 1.2;
                this.damage = 12;
            }
            case ARCANE ->
            {
                this.range = 3;
                this.fireRate = 0.8;
                this.damage = 6;
            }
            case BOMB ->
            {
                this.range = 2;
                this.fireRate = 2.5;
                this.damage = 25;
            }
            case FROST ->
            {
                this.range = 2;
                this.fireRate = 5.0;
                this.damage = 0;
            }
            case BALLISTA ->
            {
                this.range = 5;
                this.fireRate = 2.0;
                this.damage = 10;
            }
        }
        this.timeSinceLastShot = 0;
    }

    public int getRange() {
        return range;
    }

    public int getDamage() {
        return damage;
    }

    public TowerType getType() {
        return type;
    }

    @Override
    public void attack(int d, Damageable target)
    {
        target.damaged(d);
    }

    public void updateTarget(List<Enemy> enemies)
    {
        if(!(this.target != null && this.target.isAlive() && isInRange(this.target)))
        {
            target = null;
            int bestPathIndex = -1;
            for (Enemy e : enemies)
            {
                if (isInRange(e) && e.getPathIndex() > bestPathIndex)
                {
                    target = e;
                    bestPathIndex = e.getPathIndex();
                }
            }
        }

    }

    public boolean isInRange(Enemy e)
    {
        double dx = e.getX() - this.x;
        double dy = e.getY() - this.y;
        return Math.sqrt(dx * dx + dy * dy) <= this.range * Tile.TILE_SIZE;
    }

    public void update(double dt, List<Enemy> enemies)
    {
        this.updateTarget(enemies);
        this.timeSinceLastShot += dt;
    }

    public Projectile shoot() {
        if (target == null) return null;
        if (timeSinceLastShot < fireRate) return null;
        timeSinceLastShot = 0;
        return new Projectile(this,this.target);
    }
}
