import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RRT_GUI extends JPanel {
    private static final long serialVersionUID = 1L;
	private static final int STATE_SPACE_SIZE = 300;
    private static final int OBSTACLE_SIZE = 40;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Centering offset to draw state space in the middle
        int offsetX = (getWidth() - STATE_SPACE_SIZE) / 2;
        int offsetY = (getHeight() - STATE_SPACE_SIZE) / 2;

        // Draw the state space boundary
        g.setColor(Color.GRAY);
        g.drawRect(offsetX, offsetY, STATE_SPACE_SIZE, STATE_SPACE_SIZE);

        // Draw obstacles
        if (RRT_Logic.obstacles != null && RRT_Logic.obstacles[RRT_Logic.currentEnvironment] != null) {
            g.setColor(Color.BLACK);
            for (RRT_Logic.Point p : RRT_Logic.obstacles[RRT_Logic.currentEnvironment]) {
                g.fillRect(p.getX() + offsetX, p.getY() + offsetY, OBSTACLE_SIZE, OBSTACLE_SIZE);
            }
        }

        // Draw RRT tree edges
        g.setColor(Color.LIGHT_GRAY);
        for (RRT_Logic.Point[] edge : RRT_Logic.treeEdges) {
            g.drawLine(edge[0].getX() + offsetX, edge[0].getY() + offsetY,
                       edge[1].getX() + offsetX, edge[1].getY() + offsetY);
        }

        // Draw final path in blue
        g.setColor(Color.BLUE);
        List<RRT_Logic.Point> path = RRT_Logic.finalPath;
        for (int i = 0; i < path.size() - 1; i++) {
            RRT_Logic.Point p1 = path.get(i);
            RRT_Logic.Point p2 = path.get(i + 1);
            g.drawLine(p1.getX() + offsetX, p1.getY() + offsetY,
                       p2.getX() + offsetX, p2.getY() + offsetY);
        }

        // ✅ Only draw start and goal if they are set
        if (RRT_Logic.startP != null && RRT_Logic.endP != null) {
            g.setColor(Color.RED);
            g.fillOval(RRT_Logic.startP.getX() + offsetX - 5, RRT_Logic.startP.getY() + offsetY - 5, 10, 10);
            g.drawString("S", RRT_Logic.startP.getX() + offsetX - 12, RRT_Logic.startP.getY() + offsetY);

            g.fillOval(RRT_Logic.endP.getX() + offsetX - 5, RRT_Logic.endP.getY() + offsetY - 5, 10, 10);
            g.drawString("G", RRT_Logic.endP.getX() + offsetX - 12, RRT_Logic.endP.getY() + offsetY);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("RRT Pathfinding");
        RRT_GUI panel = new RRT_GUI();
        frame.add(panel);
        frame.setSize(STATE_SPACE_SIZE + 50, STATE_SPACE_SIZE + 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Initialize environments and run RRT across them
        RRT_Logic.generateEnvironments();
        RRT_Logic.runEnvironment(panel, 0);
        pause();
        RRT_Logic.runEnvironment(panel, 1);
        pause();
        RRT_Logic.runEnvironment(panel, 2);
    }

    private static void pause() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {}
    }
}
