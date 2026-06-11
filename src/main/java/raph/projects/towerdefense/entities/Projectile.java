package raph.projects.towerdefense.entities;

import raph.projects.towerdefense.Sprite;

public class Projectile
{
    private double x;
    private double y;
    private double targetX;
    private double targetY;
    private Enemy target;
    private double speed;
    private int damage;
    private Sprite sprite;
    private boolean finished;

    public Projectile(Tower t, Enemy e)
    {
        this.target = e;
        this.x = t.getX();
        this.y = t.getY();
        this.damage = t.getDamage();
        this.speed = t.getRange() * 256;
        this.finished = false;

        // calcul du point d'interception
        double[] intercept = predictIntercept(
                this.x, this.y, this.speed,
                e.getX(), e.getY(), e.getVelocityX(), e.getVelocityY()
        );
        this.targetX = intercept[0];
        this.targetY = intercept[1];

        switch(t.getType())
        {
            case CANNON -> this.sprite = new Sprite("/raph/projects/towerdefense/Images/Projectiles/Cannon_Pro.png",1,10,3,1);
            default -> this.sprite = new Sprite("/raph/projects/towerdefense/Images/Blank.png",1,64,64,1);
        }

        this.sprite.getCurrentFrame().setX(this.x);
        this.sprite.getCurrentFrame().setY(this.y);
    }

    public boolean isFinished() {
        return finished;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void update(double dt) {
        if (finished) return;

        double dx = targetX - this.x;
        double dy = targetY - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double pixelsThisFrame = speed * dt;

        if (distance <= pixelsThisFrame) {
            if (target.isAlive()) target.damaged(damage);
            finished = true;
        } else {
            this.x += (dx / distance) * pixelsThisFrame;
            this.y += (dy / distance) * pixelsThisFrame;
            sprite.getCurrentFrame().setX(this.x);
            sprite.getCurrentFrame().setY(this.y);
        }
    }

    private double[] predictIntercept(double px, double py, double speed,
                                      double ex, double ey, double evx, double evy)
    {
        double t = Math.sqrt((ex-px)*(ex-px) + (ey-py)*(ey-py)) / speed;
        for (int i = 0; i < 3; i++) {
            double ix = ex + evx * t;
            double iy = ey + evy * t;
            t = Math.sqrt((ix-px)*(ix-px) + (iy-py)*(iy-py)) / speed;
        }
        return new double[]{ex + evx * t, ey + evy * t};
    }
}
