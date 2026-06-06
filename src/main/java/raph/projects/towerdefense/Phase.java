package raph.projects.towerdefense;

public enum Phase
{
    CONSTRUCTION(0),
    DEFENSE(1);

    private final int phase;

    Phase(int p)
    {
        this.phase=p;
    }

    public int getPhase()
    {
        return this.phase;
    }
}
