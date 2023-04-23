package simulator;

public class Sim {
    public static void main(String[] args) {
        Grid grid = new Grid(5734531, 0.05);
        Agent agent = new Agent(grid, 10000, grid.agentPos, grid.goalPos);
        System.out.println(agent.grid);
        agent.Move("BFS");
        System.out.println(agent.grid);
    }
}
