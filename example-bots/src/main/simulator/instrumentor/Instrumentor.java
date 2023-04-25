package simulator.instrumentor;

import battlecode.instrumenter.TeamClassLoaderFactory;
import simulator.Agent;
import simulator.Coords;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Instrumentor {

    private TeamClassLoaderFactory.Loader loader;
    private TeamClassLoaderFactory factory;
    private Class<?> monitorClass;

    public Instrumentor() {
        factory = new TeamClassLoaderFactory("example-bots\\build\\classes");
        loader = factory.createLoader(false);

        // To avoid hitting the limit.
        startMonitor();
    }

    // TODO Check this is cached and isn't returning a new instance of these clazzes
    public Class<?> getGridClass() {
        try {
            return loader.loadClass("simulator.Grid");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Class<?> getCoordsClass() {
        try {
            return loader.loadClass("simulator.Coords");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Class<?> getAlgorithmClass(Class<?> clazz) {
        String algoClassName = clazz.getSimpleName();
        try {
            return loader.loadClass("simulator.algorithms." + algoClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void startMonitor() {
        try {
            monitorClass = loader.loadClass("battlecode.instrumenter.inject.RobotMonitor");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Field bytecodesLeftField;
        try {
            bytecodesLeftField = monitorClass.getDeclaredField("bytecodesLeft");
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
    }

    public int stopMonitor() {
        Field bytecodesLeftField;
        try {
            bytecodesLeftField = monitorClass.getDeclaredField("bytecodesLeft");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException("Okay, the monitor doesn't have bytecodesLeft. Things are... Wack.");
        }
        bytecodesLeftField.setAccessible(true);

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

    public InstrumentedGrid newInstrumentedGrid(long seed, double congestion) {
        Class<?> gridClass = getGridClass();
        try {
            return new InstrumentedGrid(gridClass.getDeclaredConstructor(long.class, double.class).newInstance(seed, congestion));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InstrumentedCoords newInstrumentedCoords(int x, int y) {
        Class<?> coordsClass = getCoordsClass();
        try {
            return new InstrumentedCoords(coordsClass.getDeclaredConstructor(int.class, int.class).newInstance(x, y));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InstrumentedCoords newInstrumentedCoords(int x, int y, double pathCost) {
        Class<?> coordsClass = getCoordsClass();
        try {
            return new InstrumentedCoords(coordsClass.getDeclaredConstructor(int.class, int.class, double.class).newInstance(x, y, pathCost));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Class<?> getGridLocationClass() {
        try {
            return loader.loadClass("simulator.GridLocation");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Class<Enum> getLocationTypeClass() {
        try {
            return (Class<Enum>) loader.loadClass("simulator.LocationType");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object instrumentedEmptyLocationType() {
        Class<Enum> locationTypeClass = getLocationTypeClass();
        try {
            return Enum.valueOf(locationTypeClass, "EMPTY");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }

    }

    public InstrumentedGridLocation newInstrumentedEmptyGridLocation() {
        Class<?> gridLocationClass = getGridLocationClass();
        Class<Enum> locationTypeClass = getLocationTypeClass();
        Object emptyLocationType = instrumentedEmptyLocationType();

        try {
            return new InstrumentedGridLocation(gridLocationClass.getConstructor(locationTypeClass).newInstance(emptyLocationType));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InstrumentedGridLocation instrumentedGridGetLocation(InstrumentedGrid grid, int x, int y) {
        Class<?> gridClass = getGridClass();
        try {
            Method getLocation = gridClass.getMethod("getLocation", int.class, int.class);
            return new InstrumentedGridLocation(getLocation.invoke(grid.getUnderlyingObject(), x, y));
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void instrumentedGridSetLocation(InstrumentedGrid grid, int x, int y, InstrumentedGridLocation gridLocation) {
        Class<?> gridClass = getGridClass();
        try {
            Method setLocation = gridClass.getMethod("setLocation", int.class, int.class, gridLocation.getUnderlyingObject().getClass());
            setLocation.invoke(grid.getUnderlyingObject(), x, y, gridLocation.getUnderlyingObject());
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void instrumentedGridSetLocationEmpty(InstrumentedGrid grid, int x, int y) {
        instrumentedGridSetLocation(grid, x, y, newInstrumentedEmptyGridLocation());
    }

    public InstrumentedCoords coordsToInstrumentedCoords(Coords coords) {
        return newInstrumentedCoords(coords.x, coords.y, coords.pathCost);
    }

    public Coords instrumentedCoordsToCoords(InstrumentedCoords instrumentedCoords) {
        int x, y;
        double pathCost;

        Class<?> coordsClass = getCoordsClass();
        try {
            Field xField = coordsClass.getField("x");
            Field yField = coordsClass.getField("y");
            Field pathCostField = coordsClass.getField("pathCost");
            x = xField.getInt(instrumentedCoords.getUnderlyingObject());
            y = yField.getInt(instrumentedCoords.getUnderlyingObject());
            pathCost = pathCostField.getDouble(instrumentedCoords.getUnderlyingObject());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        return new Coords(x,y,pathCost);
    }

    public InstrumentedCoords getInstrumentedAgentPos(InstrumentedGrid grid) {
        Class<?> gridClass = getGridClass();

        try {
            Field agentPosField = gridClass.getField("agentPos");
            return new InstrumentedCoords(agentPosField.get(grid.getUnderlyingObject()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public InstrumentedCoords getInstrumentedGoalPos(InstrumentedGrid grid) {
        Class<?> gridClass = getGridClass();

        try {
            Field agentPosField = gridClass.getField("goalPos");
            return new InstrumentedCoords(agentPosField.get(grid.getUnderlyingObject()));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String instrumentedGridToString(InstrumentedGrid grid) {
        try {
            return getGridClass().getMethod("toString").invoke(grid.getUnderlyingObject()).toString();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return The amount of bytecode used to calculate the move.
     */
    public int moveAgent(Agent agent, InstrumentedGrid grid, Class<?> algoUninstrumentedClass) {
        Class<?> algoClass = getAlgorithmClass(algoUninstrumentedClass);
        Class<?> gridClass = getGridClass();
        Class<?> coordsClass = getCoordsClass();
        startMonitor();

        Coords newPos;
        try {
            Object algo = algoClass.newInstance();
            Method nextMoveMethod = algoClass.getMethod("nextMove", gridClass, coordsClass, coordsClass, int.class);
            Object initial = coordsToInstrumentedCoords(agent.initial).getUnderlyingObject();
            Object goal = coordsToInstrumentedCoords(agent.goal).getUnderlyingObject();
            Object newPosObj = nextMoveMethod.invoke(algo, grid.getUnderlyingObject(), initial, goal, agent.visRadius);
            newPos = instrumentedCoordsToCoords(new InstrumentedCoords(newPosObj));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return -1;
        }
        int used = stopMonitor();
        instrumentedGridSetLocation(grid, newPos.x, newPos.y, instrumentedGridGetLocation(grid, agent.initial.x, agent.initial.y));
        instrumentedGridSetLocationEmpty(grid, agent.initial.x, agent.initial.y);
        agent.initial.x = newPos.x;
        agent.initial.y = newPos.y;

        return used;
    }
}
