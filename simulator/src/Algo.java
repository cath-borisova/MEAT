package simulator.src;
public interface Algo {
    public Coords nextMove(Grid grid, Coords initial, Coords goal, int visRadius);
}
