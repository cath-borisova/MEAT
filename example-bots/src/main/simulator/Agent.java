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

    public Agent(int visRadius, Coords initial, Coords goal) {
        this.visRadius = visRadius;
        this.initial = initial;
        this.goal = goal;
    }
}
