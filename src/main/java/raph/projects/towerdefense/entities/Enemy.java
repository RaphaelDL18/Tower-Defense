package raph.projects.towerdefense.entities;

import raph.projects.towerdefense.Sprite;
import raph.projects.towerdefense.Tile;

public class Enemy extends Entity implements Damageable, DamageDealer
{
    private EnemyType type;
    private int health;
    private int speed;
    private int damage;
    private Tile[] path;
    private int pathIndex;

    public Enemy(EnemyType t, Tile[] path)
    {
        super(initSprite(t));
        this.type = t;
        this.path = path;
        this.pathIndex = 0;

        this.x = path[0].getGridY() * Tile.TILE_SIZE;
        this.y = path[0].getGridX() * Tile.TILE_SIZE;

        switch(this.type) {
            case GOBELIN -> {
                this.health = 3;
                this.speed = 3;
                this.damage = 3;
            }
            default ->
            {
                this.health = 1;
                this.speed = 1;
                this.damage = 1;
            }
        }
    }

    public EnemyType getType()
    {
        return this.type;
    }

    public int getHealth()
    {
        return this.health;
    }

    public int getSpeed()
    {
        return this.speed;
    }

    public int getDamage()
    {
        return this.damage;
    }

    public void setHealth(int h)
    {
        this.health = h;
    }

    public void setSpeed(int s)
    {
        this.speed = s;
    }

    public Sprite getSprite()
    {
        return this.sprite;
    }

    public static Sprite initSprite(EnemyType t)
    {
        return switch(t)
        {
            case GOBELIN -> new Sprite("/raph/projects/towerdefense/Images/Enemies/gobelin.png",8,64,64,4);
            default -> new Sprite("/raph/projects/towerdefense/Images/Blank.png",1,64,64,1);
        };
    }

    public void update(double dt)
    {
        if (this.pathIndex >= this.path.length) return; // arrivé à la base

        // cible en pixels
        double targetX = this.path[this.pathIndex].getGridY() * Tile.TILE_SIZE;
        double targetY = this.path[this.pathIndex].getGridX() * Tile.TILE_SIZE;

        // direction vers la cible
        double dx = targetX - this.x;
        double dy = targetY - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        double pixelsPerSecond = this.speed * 64; // 1 de speed = 64px/s

        if (distance <= pixelsPerSecond * dt) {
            // on est arrivé sur la case cible
            this.x = targetX;
            this.y = targetY;
            pathIndex++;
        } else {
            // on avance vers la cible
            this.x += (dx / distance) * pixelsPerSecond * dt;
            this.y += (dy / distance) * pixelsPerSecond * dt;
        }

        // mise à jour de l'ImageView
        this.sprite.getCurrentFrame().setX(this.x);
        this.sprite.getCurrentFrame().setY(this.y);
    }

    public boolean hasReachedBase() {
        return this.pathIndex >= this.path.length;
    }

    @Override
    public void attack(int d, Damageable target)
    {
        target.damaged(d);
    }

    @Override
    public void damaged(int d)
    {
        this.health-=d;
    }

    @Override
    public boolean isAlive()
    {
        return this.health>0;
    }

}
