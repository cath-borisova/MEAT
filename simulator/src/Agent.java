package simulator.src;

public class Agent {
    public int visRadius;
    public int curX;
    public int curY;
    public int goalX;
    public int goalY;
    Grid grid;

    public Agent(int visRadius, int curX, int curY, int goalX, int goalY, Grid grid){
        this.visRadius = visRadius;
        this.curX = curX;
        this.curY = curY;
        this.goalX = goalX;
        this.goalY = goalY;
        this.grid = grid;
    }

    public void Move(Algo algorithm){
        Coords new_pos = algorithm.nextMove(grid, curX, curY, goalX, goalY);
        grid.setLocation(new_pos.x, new_pos.y, grid.getLocation(curX, curY));
        //empty the original spot?
    }
}
