package raph.projects.towerdefense.entities;

import java.util.ArrayList;
import java.util.List;

public class Wave
{
    private List<EnemyGroup> groups;

    public Wave()
    {
        this.groups = new ArrayList<EnemyGroup>();
    }

    public List<EnemyGroup> getGroups()
    {
        return this.groups;
    }

    public void addGroup(EnemyGroup g)
    {
        this.groups.add(g);
    }
}
