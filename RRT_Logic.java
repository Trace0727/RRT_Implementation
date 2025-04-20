import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.JPanel;

/**
 * RRT_Logic - This class holds the core logic for the Rapidly-exploring Random Tree (RRT) algorithm.
 * It includes state space generation, collision checking, and the RRT search process.
 */
public class RRT_Logic {
    public static final int STATE_SPACE_SIZE = 300;
    public static final int OBSTACLE_SIZE = 40;
    public static final int NUM_OBSTACLES = 5;
    public static final int NUM_ENVIRONMENTS = 3;
    public static final int STEP_SIZE = 10;

    public static int currentEnvironment = 0;

    public static final List<Point> treeNodes = new ArrayList<>();
    public static final List<Point[]> treeEdges = new ArrayList<>();
    public static final List<Point> finalPath = new ArrayList<>();

    public static Point startP;
    public static Point endP;

    public static List<Point>[] obstacles;

    /**
     * Custom Point class to store 2D coordinates.
     * Includes equals and hashCode for correct use in collections.
     */
    public static class Point {
        private final int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() { return x; }
        public int getY() { return y; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // Create 3 different environments with obstacles
    @SuppressWarnings("unchecked")
    public static void generateEnvironments() {
        obstacles = new ArrayList[NUM_ENVIRONMENTS];
        for (int i = 0; i < NUM_ENVIRONMENTS; i++) {
            obstacles[i] = new ArrayList<>();
            int count = (i == 2) ? 10 : NUM_OBSTACLES;
            while (obstacles[i].size() < count) {
                Point p = new Point(random(STATE_SPACE_SIZE - OBSTACLE_SIZE), random(STATE_SPACE_SIZE - OBSTACLE_SIZE));
                if (isValidObstacle(p, obstacles[i])) {
                    obstacles[i].add(p);
                }
            }
        }
    }
    
    public static void runEnvironment(JPanel panel, int env) {
        currentEnvironment = env;
        treeNodes.clear();
        treeEdges.clear();
        finalPath.clear();
        startP = generateFreePoint();
        endP = generateFreePoint();
        rrtSearch(startP, endP);
        panel.repaint();
    }

    // Ensure obstacle does not overlap with others
    public static boolean isValidObstacle(Point p, List<Point> obstacleList) {
        for (Point o : obstacleList) {
            Rectangle r = new Rectangle(o.getX(), o.getY(), OBSTACLE_SIZE, OBSTACLE_SIZE);
            if (r.contains(p.getX(), p.getY())) return false;
        }
        return true;
    }

    // Create a random free point not inside any obstacle
    public static Point generateFreePoint() {
        Point p;
        do {
            p = new Point(random(STATE_SPACE_SIZE), random(STATE_SPACE_SIZE));
        } while (!isFree(p));
        return p;
    }

    // Check if a point is free (not inside obstacles)
    public static boolean isFree(Point p) {
        for (Point o : obstacles[currentEnvironment]) {
            Rectangle r = new Rectangle(o.getX(), o.getY(), OBSTACLE_SIZE, OBSTACLE_SIZE);
            if (r.contains(p.getX(), p.getY())) return false;
        }
        return true;
    }

 // Main RRT algorithm: expands tree toward random samples until goal is reached
    public static void rrtSearch(Point start, Point goal) {
        Map<Point, Point> parentMap = new HashMap<>();
        Random rand = new Random();
        treeNodes.add(start);

        int MIN_EXPANSION_BEFORE_GOAL = 300; // force exploration of at least 300 nodes
        boolean goalConnected = false;

        while (true) {
            Point randPoint;
            do {
                randPoint = new Point(rand.nextInt(STATE_SPACE_SIZE), rand.nextInt(STATE_SPACE_SIZE));
            } while (!isFree(randPoint));

            Point nearest = getNearest(randPoint);
            Point newPoint = steer(nearest, randPoint);

            if (!newPoint.equals(nearest) && isCollisionFree(nearest, newPoint)) {
                treeNodes.add(newPoint);
                treeEdges.add(new Point[]{nearest, newPoint});
                parentMap.put(newPoint, nearest);
            }

            // After sufficient expansion, allow connection to goal
            if (!goalConnected && treeNodes.size() > MIN_EXPANSION_BEFORE_GOAL) {
                Point closestToGoal = getNearest(goal);
                if (distance(closestToGoal, goal) < STEP_SIZE && isCollisionFree(closestToGoal, goal)) {
                    parentMap.put(goal, closestToGoal);
                    treeEdges.add(new Point[]{closestToGoal, goal});
                    goalConnected = true;

                    // Reconstruct shortest path
                    Point current = goal;
                    while (current != null) {
                        finalPath.add(0, current);
                        current = parentMap.get(current);
                    }
                    System.out.println("Path found!");
                    return;
                }
            }
        }
    }

    // Get the nearest node in the tree to a given point
    public static Point getNearest(Point target) {
        Point best = null;
        double minDist = Double.MAX_VALUE;
        for (Point p : treeNodes) {
            double dist = distance(p, target);
            if (dist < minDist) {
                minDist = dist;
                best = p;
            }
        }
        return best;
    }

    // Move a step from `from` to `to` at a fixed step size
    public static Point steer(Point from, Point to) {
        double angle = Math.atan2(to.getY() - from.getY(), to.getX() - from.getX());
        int newX = (int) (from.getX() + STEP_SIZE * Math.cos(angle));
        int newY = (int) (from.getY() + STEP_SIZE * Math.sin(angle));
        Point p = new Point(newX, newY);
        return isFree(p) ? p : from;
    }

    // Check if the path from a to b is collision-free (sampled)
    public static boolean isCollisionFree(Point a, Point b) {
        int steps = 20;
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            int x = (int) (a.getX() * (1 - t) + b.getX() * t);
            int y = (int) (a.getY() * (1 - t) + b.getY() * t);
            if (!isFree(new Point(x, y))) return false;
        }
        return true;
    }

    // Calculate Euclidean distance
    public static double distance(Point a, Point b) {
        return Math.hypot(a.getX() - b.getX(), a.getY() - b.getY());
    }

    // Return a random integer in range
    public static int random(int bound) {
        return new Random().nextInt(bound);
    }
}