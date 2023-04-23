package simulator;

import battlecode.instrumenter.TeamClassLoaderFactory;
import simulator.algorithms.Dijkstras;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Agent {
    public int visRadius;
    public Coords initial;
    public Coords goal;
    public Grid grid;

    public Agent(Grid grid, int visRadius, Coords initial, Coords goal) {
        this.visRadius = visRadius;
        this.initial = initial;
        this.goal = goal;
        this.grid = grid;
    }

    /**
     *
     * @param algoClassName The name of the class in the simulator.algorithms package you want to run
     * @return The amount of bytecode used to calculate the move.
     */
    public int Move(String algoClassName){
        // Assumes we are running it from the base directory using the gradlew command
        TeamClassLoaderFactory factory = new TeamClassLoaderFactory("example-bots\\build\\classes");
        TeamClassLoaderFactory.Loader loader = factory.createLoader(false);
        Class<?> algoClass;
        Class<?> monitorClass;
        try {
            algoClass = loader.loadClass("simulator.algorithms." + algoClassName);
            monitorClass = loader.loadClass("battlecode.instrumenter.inject.RobotMonitor");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Error loading algorithm and monitor class. Did you spell the name right? Is it in the simulator.algorithms package? " + algoClassName
            );
        }

        Field bytecodesLeftField;
        try {
            bytecodesLeftField = monitorClass.getField("bytecodesLeft");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException("Okay, the monitor doesn't have bytecodesLeft. Things are... Wack.");
        }

        // What's a private variable? Reflection is cool.
        bytecodesLeftField.setAccessible(true);
        try {
            // Okay so the way Battlecode does it is by setting the value of the limit and subtracting from it.
            // We don't care about some limit, only the total used. So to avoid working with negatives we do this.
            // Also I am unsure how they assign this initially, so this removes some guess games.
            // The setting this left field this high also ensures we don't attempt to pause due to running out (This would cause a NPE).
            bytecodesLeftField.setInt(null, Integer.MAX_VALUE);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Illegal access to bytecodesLeft");
        }

        Coords newPos;
        try {
            Algo algo = (Algo) algoClass.newInstance();
            Method nextMoveMethod = algoClass.getMethod("nextMove", Grid.class, Coords.class, Coords.class, int.class);
            newPos = (Coords) nextMoveMethod.invoke(algo, grid, initial, goal, visRadius);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't access instance. Private constructor?");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't get method. Does the class implement Algo?");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException("Error invoking move. No idea why.");
        }

        // Actually update the position of the agent.
        grid.setLocation(newPos.x, newPos.y, grid.getLocation(initial.x, initial.y));
        grid.setLocation(initial.x, initial.y, new GridLocation(LocationType.EMPTY));
        initial.x = newPos.x;
        initial.y = newPos.y;

        // Calculate how much bytecode was used.
        int remaining;
        try {
            remaining = bytecodesLeftField.getInt(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Illegal access to bytecodesLeft");
        }

        int used = Integer.MAX_VALUE - remaining;
        return used;
    }
}
