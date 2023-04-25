package simulator;

public class Agent {
    public int visRadius;
    public Coords initial;
    public Coords goal;

    public Agent(int visRadius, Coords initial, Coords goal) {
        this.visRadius = visRadius;
        this.initial = initial;
        this.goal = goal;
    }
}
