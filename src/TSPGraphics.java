import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class TSPGraphics extends JPanel {

    private static final int SCALE = 40;

    public static void main(String[] args) {
        TSPTest.runSamples();

        JFrame frame = new JFrame();
        frame.setSize(1000, 1000);
        frame.setTitle("TSP Test");
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        TSPGraphics graphics = new TSPGraphics(TSPTest.route, TSPTest.GRID_SIZE);
        frame.add(graphics);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    TSPTest.runSamples();
                    graphics.route = TSPTest.route;
                    frame.repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        frame.setVisible(true);
    }

    private List<Vector> route;
    private int size;

    public TSPGraphics(List<Vector> route, int size) {
        this.route = route;
        this.size = size;
        this.setPreferredSize(new Dimension(1000, 1000));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                g.drawOval(((x + 10) * SCALE) - 5, ((y + 10) * SCALE) - 5, 10, 10);
            }
        }

        g.setColor(Color.RED);
        for (int i = 1; i < this.route.size(); i++) {
            Vector p1 = this.route.get(i - 1);
            Vector p2 = this.route.get(i);
            if (p1 != null && p2 != null) {
                if (i == 1) {
                    g.fillOval(((p1.x + 10) * SCALE) - 5, ((p1.y + 10) * SCALE) - 5, 10, 10);
                } else {
                    g.drawOval(((p1.x + 10) * SCALE) - 5, ((p1.y + 10) * SCALE) - 5, 10, 10);
                }
                g.drawOval(((p2.x + 10) * SCALE) - 5, ((p2.y + 10) * SCALE) - 5, 10, 10);
                g.drawLine((p1.x + 10) * SCALE, (p1.y + 10) * SCALE, (p2.x + 10) * SCALE, (p2.y + 10) * SCALE);
            }
        }
    }
}
