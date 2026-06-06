package raph.projects.towerdefense.entities;

import raph.projects.towerdefense.Sprite;

public class Tower extends Entity implements DamageDealer
{
    private int type;
    private int range;
    private int fireRate;
    private int damage;

    public Tower(int t,int r,int f, int d)
    {
        super(new Sprite("",1,64,64,1));
        this.type = t;
        this.range = r;
        this.fireRate = f;
        this.damage = d;
    }

    @Override
    public void attack(int d, Damageable target)
    {
        target.damaged(d);
    }


}
