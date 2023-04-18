package simulator.src;

public class Agent {
    public int visRadius;
    public Coords initial;
    public Coords goal;
    Grid grid;

    public Agent(int visRadius, int curX, int curY, int goalX, int goalY, Grid grid){
        this.visRadius = visRadius;
        initial = new Coords(curX, curY);
        goal = new Coords(goalX, goalY);
        this.grid = grid;
    }

    public void Move(Algo algorithm){
        Coords new_pos = algorithm.nextMove(grid, initial, goal, visRadius);
        grid.setLocation(new_pos.x, new_pos.y, grid.getLocation(initial.x, initial.y));
        //empty the original spot?
    }
}
