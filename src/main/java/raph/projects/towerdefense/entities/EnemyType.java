package raph.projects.towerdefense.entities;

public enum EnemyType
{
    GOBELIN(0),
    WRAITH(1),
    OGRE(2),
    RAT(3),
    DEMON(4);

    private int type;

    EnemyType(int t)
    {
        this.type=t;
    }

    public int getType()
    {
        return this.type;
    }
}
