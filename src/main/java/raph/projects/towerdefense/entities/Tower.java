package raph.projects.towerdefense.entities;

import raph.projects.towerdefense.Sprite;

public class Tower extends Entity implements DamageDealer
{
    private TowerType type;
    private int range;
    private int fireRate;
    private int damage;

    public Tower(TowerType t, String path)
    {
        super(new Sprite(path,1,64,64,1));
        this.type = t;

        switch(this.type)
        {
            case CANNON -> {
                                this.range = 2;
                                this.fireRate = 2;
                                this.damage = 2;
                           }
            case ARCANE -> {
                                this.range = 3;
                                this.fireRate = 3;
                                this.damage = 1;
                           }
            case BOMB -> {
                                this.range = 2;
                                this.fireRate = 1;
                                this.damage = 4;
                         }
            case FROST -> {
                                this.range = 2;
                                this.fireRate = 1;
                                this.damage = 0;
                          }
            case BALLISTA ->
                           {
                                this.range = 5;
                                this.fireRate = 1;
                                this.damage = 2;
                           }
        }

    }

    @Override
    public void attack(int d, Damageable target)
    {
        target.damaged(d);
    }


}
