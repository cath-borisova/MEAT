public class Agent {
    public int visRadius;
    public int curX;
    public int curY;
    public int goalX;
    public int goalY;

    public Agent(int visRadius, int curX, int curY, int goalX, int goalY){
        this.visRadius = visRadius;
        this.curX = curX;
        this.curY = curY;
        this.goalX = goalX;
        this.goalY = goalY;
    }

    public void Move(Algo algorithm){
        int [] new_pos = algorithm.NextMove(grid, curX, curY, goalX, goalY);
        grid.setLoc(new_pos[0], new_pos[1], grid.getLoc(curX, curY));
        //empty the original spot?
    }
}
