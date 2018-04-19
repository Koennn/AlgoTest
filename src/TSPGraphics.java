import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TSPGraphics extends JPanel {

    private static final int SCALE = 100;
    private static final int ANIMATE_INTERVAL = 100;

    public static void main(String[] args) {
        List<Vector> points = TSPTest.generatePoints();

        System.out.println("NN");
        TSPTest.runSamples(new ArrayList<>(points), true);
        createFrame(TSPTest.route, "Nearest Neighbour 2");

        /*System.out.println("N2N");
        TSPTest.runSamples(new ArrayList<>(points), false);
        createFrame(TSPTest.route, "Nearest Two Neighbours");*/
    }

    private static void createFrame(List<Vector> route, String name) {
        JFrame frame = new JFrame();
        frame.setSize(1000, 1000);
        frame.setTitle(name);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        TSPGraphics graphics = new TSPGraphics(route, TSPTest.GRID_SIZE);
        frame.add(graphics);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    restart(graphics, frame);
                } else if (e.getKeyCode() == KeyEvent.VK_C) {
                    graphics.draw.clear();
                    graphics.route.clear();
                    graphics.repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        frame.setVisible(true);
    }

    public static void restart(TSPGraphics graphics, JFrame frame) {
        TSPTest.runSamples(TSPTest.generatePoints(), false);
        graphics.route = new ArrayList<>(TSPTest.route);
        graphics.route.set(graphics.route.size() - 1, new Vector(0, 0));
        graphics.draw.clear();
        graphics.draw.addAll(Arrays.asList(graphics.route.get(0), graphics.route.get(1)));
        new Thread(() -> {
            try {
                Thread.sleep(ANIMATE_INTERVAL);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            for (int i = 2; i < graphics.route.size(); i++) {
                graphics.draw.add(graphics.route.get(i));
                try {
                    Thread.sleep(ANIMATE_INTERVAL);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                graphics.repaint();
            }

            /*try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            restart(graphics, frame);*/
        }).start();
        frame.repaint();
    }

    private List<Vector> route;
    private List<Vector> draw;
    private int size;

    public TSPGraphics(List<Vector> route, int size) {
        this.route = route;
        this.size = size;
        this.setPreferredSize(new Dimension(1000, 1000));
        this.draw = new ArrayList<>();
        this.route.set(this.route.size() - 1, new Vector(0, 0));
        //this.draw.addAll(route);
        this.draw.addAll(Arrays.asList(route.get(0), route.get(1)));
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 2; i < route.size(); i++) {
                this.draw.add(route.get(i));
                try {
                    Thread.sleep(ANIMATE_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.repaint();
            }
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (this.draw.size() > 2) {
            g.setColor(Color.BLACK);
            for (int x = 0; x < this.size; x++) {
                for (int y = 0; y < this.size; y++) {
                    g.drawOval((x * SCALE) + 5, (y * SCALE) + 5, 10, 10);
                }
            }

            g.setColor(Color.RED);
            for (int i = 1; i < this.route.size() - 1; i++) {
                Vector p1 = this.route.get(i - 1);
                Vector p2 = this.route.get(i);
                if (p1 != null && p2 != null) {
                    if (i == 1) {
                        g.drawOval((p1.x * SCALE) + 5, (p1.y * SCALE) + 5, 10, 10);
                    } else {
                        g.fillOval((p1.x * SCALE) + 5, (p1.y * SCALE) + 5, 10, 10);
                    }
                    g.fillOval((p2.x * SCALE) + 5, (p2.y * SCALE) + 5, 10, 10);
                }
            }

            for (int i = 1; i < this.draw.size(); i++) {
                Vector p1 = this.draw.get(i - 1);
                Vector p2 = this.draw.get(i);
                if (p1 != null && p2 != null) {
                    g.drawLine((p1.x * SCALE) + 10, (p1.y * SCALE) + 10, (p2.x * SCALE) + 10, (p2.y * SCALE) + 10);
                }
            }

            /*Vector p1 = this.draw.get(0);
            Vector p2 = this.draw.get(this.draw.size() - 2);
            g.drawLine((p1.x * SCALE) + 10, (p1.y * SCALE) + 10, (p2.x * SCALE) + 10, (p2.y * SCALE) + 10);*/
        }
    }
}
