import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.*;

public class Main extends JPanel {
    private final int WIDTH = 900;
    private final int HEIGHT = 600;
    private JButton jButton1 = new JButton("    ");
    private Tile[] m_Tiles;
    private ArrayList<SOC.piece> m_Pieces = new ArrayList<SOC.piece>();
    private Font m_font = new Font("Arial", Font.BOLD, 18);
    FontMetrics metrics;
    int m_wood = 4;
    int m_wheat = 4;
    int m_desert = 1;
    int m_sheep = 4;
    int m_ore = 3;
    int m_brick = 3;
    int num2 = 1;
    int num3 = 2;
    int num4 = 2;
    int num5 = 2;
    int num6 = 2;
    int num8 = 2;
    int num9 = 2;
    int num10 = 2;
    int num11 = 2;
    int num12 = 1;
    int currentPlayer =0;
    private Polygon cursor = new Polygon();
    Player[] m_players;
    public Main() {
        m_Tiles = new Tile[37];
        add(jButton1);
        jButton1.setMargin(new Insets(2, 2, 2, 2));
        m_players = new Player[4];

        jButton1.addActionListener(new ActionListener() 
            {

                public void actionPerformed(ActionEvent evt) {
                    jButton1_ActionPerformed(evt);
                }
            });
        m_players[0] = new Player("Bob", WIDTH, HEIGHT);
        m_players[1] = new Player("Sue", WIDTH, HEIGHT);
        m_players[2] = new Player("Joe", WIDTH, HEIGHT);
        m_players[3] = new Player("Lou", WIDTH, HEIGHT);
        jButton1.setBackground(m_players[currentPlayer].getColor());

        setupGrid();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addMouseListener(new HitTestAdapter());
        addMouseMotionListener(new MouseMoveAdapter());
    }

    public void jButton1_ActionPerformed(ActionEvent evt)
    {
        currentPlayer = (currentPlayer + 1) %4;
        jButton1.setBackground(m_players[currentPlayer].getColor());

    }

    public void build(SOC.buildType b, Point p)
    {
        m_Pieces.add(new SOC.piece(b, m_players[currentPlayer].getColor(),p));

    }

    public void setupGrid()
    {
        int size = 7;
        int radius = 50;
        int padding = 2;
        Point origin = new Point(WIDTH / 2, HEIGHT / 2);
        double ang30 = Math.toRadians(30);
        double xOff = Math.cos(ang30) * (radius + padding);
        double yOff = Math.sin(ang30) * (radius + padding);
        int half = size / 2;
        int tile = 0;
        int number = 0;
        for (int row = 0; row < size; row++) {
            int cols = size - java.lang.Math.abs(row - half);

            for (int col = 0; col < cols; col++) 
            {
                int x = (int) (origin.x + xOff * (col * 2 + 1 - cols));
                int y = (int) (origin.y + yOff * (row - half) * 3);

                SOC.resource res = SOC.resource.EMPTY;
                if (!(row>0 && row < size-1 && col>0 && col < cols-1) )
                    res = SOC.resource.EMPTY;
                else
                {
                    // Replace with proper setup
                    res = randomResource();
                    if(res != SOC.resource.DESERT)
                        number = randomNumber();
                    else
                        number = 0;
                }
                // End Replace
                if(res == SOC.resource.EMPTY)
                    number = 0;
                m_Tiles[tile] = new Tile(x, y, res, number);
                tile++;
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color tmpC = g.getColor();

        g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        g2d.setFont(m_font);

        int xpoints[] = {0, WIDTH , WIDTH, 0};
        int ypoints[] = {0, 0, HEIGHT, HEIGHT };

        g.setColor(new Color(0xFFFFFF));
        g.drawPolygon(xpoints, ypoints, xpoints.length);

        for (int i=0;i < m_Tiles.length; i++)
        {
            m_Tiles[i].draw(g2d, 0, true);
        }

        for (int i=0;i < m_Pieces.size();i++)
        {
            g.setColor(m_Pieces.get(i).color());
            g.fillPolygon(m_Pieces.get(i).shape());
        }
        for (int i=0;i < m_players.length;i++)
        {
            m_players[i].draw(g2d); 
        }

        g.setColor(new Color(0xFFFFFF));
        g.fillPolygon(cursor);

        g.setColor(tmpC);

        repaint();
    }

    class HitTestAdapter extends MouseAdapter
    {
        @Override
        public void mousePressed(MouseEvent e) 
        {
            int x = e.getX();
            int y = e.getY();
            Rectangle r = new Rectangle(x - 10, y - 10, 20, 20);
            int[] tiles = new int[3];
            int count = 0;
            SOC.location[] location = new SOC.location[3];
            SOC.location[] roadlocation = new SOC.location[4];
            for (int i = 0;i < m_Tiles.length;i++)
            {
                if (m_Tiles[i].intersects(r))
                    tiles[count++] = i;
            }
            if (count == 2)
            {
                roadlocation = findRoadLocations(tiles);
                if((m_Tiles[tiles[0]].canBuildRoad(m_players[currentPlayer],roadlocation[0], roadlocation[1])
                    || m_Tiles[tiles[1]].canBuildRoad(m_players[currentPlayer],roadlocation[2],roadlocation[3]))
                && m_players[currentPlayer].canBuild(SOC.buildType.ROAD))
                {
                    m_Tiles[tiles[0]].buildRoad(m_players[currentPlayer],roadlocation[0], roadlocation[1]);    
                    m_Tiles[tiles[1]].buildRoad(m_players[currentPlayer],roadlocation[2], roadlocation[3]);    
                    m_Pieces.add(new SOC.piece(SOC.buildType.ROAD, m_players[currentPlayer].getColor(),e.getPoint()));
                    m_players[currentPlayer].Build(SOC.buildType.ROAD);
                }
            }
            else if (count == 3)
            {
                System.out.println("Settlement: " + tiles[0] + "," + tiles[1]+ "," + tiles[2]);
                location = findLocations(tiles);
                if(m_Tiles[tiles[0]].canBuild(m_players[currentPlayer],SOC.buildType.CITY, location[0])
                && m_Tiles[tiles[1]].canBuild(m_players[currentPlayer], SOC.buildType.CITY,location[1])
                && m_Tiles[tiles[2]].canBuild(m_players[currentPlayer],SOC.buildType.CITY,location[2])
                && m_players[currentPlayer].canBuild(SOC.buildType.CITY))
                {
                    m_Tiles[tiles[0]].build(m_players[currentPlayer],SOC.buildType.CITY, location[0]);
                    m_Tiles[tiles[1]].build(m_players[currentPlayer],SOC.buildType.CITY, location[1]);
                    m_Tiles[tiles[2]].build(m_players[currentPlayer],SOC.buildType.CITY, location[2]);
                    m_Pieces.add(new SOC.piece(SOC.buildType.CITY, m_players[currentPlayer].getColor(),e.getPoint()));
                    m_players[currentPlayer].Build(SOC.buildType.CITY);
                }

                if(m_Tiles[tiles[0]].canBuild(m_players[currentPlayer],SOC.buildType.SETTLEMENT, location[0])
                && m_Tiles[tiles[1]].canBuild(m_players[currentPlayer], SOC.buildType.SETTLEMENT,location[1])
                && m_Tiles[tiles[2]].canBuild(m_players[currentPlayer],SOC.buildType.SETTLEMENT,location[2])
                && m_players[currentPlayer].canBuild(SOC.buildType.SETTLEMENT))
                {

                    m_Tiles[tiles[0]].build(m_players[currentPlayer],SOC.buildType.SETTLEMENT, location[0]);
                    m_Tiles[tiles[1]].build(m_players[currentPlayer],SOC.buildType.SETTLEMENT, location[1]);
                    m_Tiles[tiles[2]].build(m_players[currentPlayer],SOC.buildType.SETTLEMENT, location[2]);
                    m_Pieces.add(new SOC.piece(SOC.buildType.SETTLEMENT, m_players[currentPlayer].getColor(),e.getPoint()));
                    m_players[currentPlayer].Build(SOC.buildType.SETTLEMENT);

                }
            }
            getParent().repaint();
        }
    }
    public SOC.location[] findLocations(int[] tiles)
    {
        if(tiles[1] - tiles[0] == 1)
            return new SOC.location[] {SOC.location.SE, SOC.location.SW ,SOC.location.N};    
        return new SOC.location[] {SOC.location.S, SOC.location.NE ,SOC.location.NW};
    }

    public SOC.location[] findRoadLocations(int[] tiles)
    {
        if(tiles[1] - tiles[0] == 1) //adjacent tiles
            return new SOC.location[] {SOC.location.NE, SOC.location.SE, SOC.location.NW, SOC.location.SW};
        
        SOC.location left[] = new SOC.location[] { SOC.location.S, SOC.location.SW,SOC.location.N, SOC.location.NE};//down right
        SOC.location right[] = new SOC.location[] { SOC.location.S, SOC.location.SE,SOC.location.N, SOC.location.NW}; // down left
        
        
        if (tiles[0] < 4) return (tiles[0] % 2 == tiles[1] %2?left:right);
        if (tiles[0] < 9) return (tiles[0] % 2 == tiles[1] %2?right:left);
        if (tiles[0] < 15) return (tiles[0] % 2 == tiles[1] %2?left:right);
        if (tiles[0] < 22) return (tiles[0] % 2 == tiles[1] %2?left:right);
        if (tiles[0] < 28) return (tiles[0] % 2 == tiles[1] %2?right:left);
        if (tiles[0] < 33) return (tiles[0] % 2 == tiles[1] %2?left:right);
        
        return left;

    }

    

    class MouseMoveAdapter implements MouseMotionListener
    {
        public void mouseMoved(MouseEvent e)
        {
            {
                int x = e.getX();
                int y = e.getY();
                Rectangle r = new Rectangle(x - 10, y - 10, 20, 20);
                int[] tiles = new int[3];
                int count = 0;
                for (int i = 0;i < m_Tiles.length;i++)
                {
                    if (m_Tiles[i].intersects(r))
                        tiles[count++] = i;
                }
                if (count == 2)
                {
                    cursor = new SOC.piece(SOC.buildType.ROAD, m_players[currentPlayer].getColor(), e.getPoint()).shape();
                }
                else if (count == 3)
                {
                    cursor = new SOC.piece(SOC.buildType.SETTLEMENT, m_players[currentPlayer].getColor(), e.getPoint()).shape();
                }

            }   
            getParent().repaint();
        }

        public void mouseDragged(MouseEvent e)
        {
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        Main p = new Main();

        f.setContentPane(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    public SOC.resource randomResource()
    {
        int temp = (int)(Math.floor(Math.random() * 6) + 1);
        if(temp == 1 && m_wood > 0)
        {
            m_wood--;
            return SOC.resource.WOOD;
        }
        if(temp == 2 && m_wheat > 0)
        {
            m_wheat--;
            return SOC.resource.WHEAT;
        }
        if(temp == 3 && m_brick > 0)
        {
            m_brick--;
            return SOC.resource.BRICK;
        }
        if(temp == 4 && m_ore > 0)
        {
            m_ore--;
            return SOC.resource.ORE;
        }
        if(temp == 5 && m_sheep > 0)
        {
            m_sheep--;
            return SOC.resource.SHEEP;
        }
        if(temp == 6 && m_desert > 0)
        {
            m_desert--;
            return SOC.resource.DESERT;
        }
        return randomResource();
    }

    public int randomNumber()
    {
        int temp = (int)(Math.floor(Math.random() * 12) + 1);
        if(temp == 2 && num2 >0)
        {
            num2--;
            return 2;
        }
        if(temp == 3 && num3 >0)
        {
            num3--;
            return 3;
        }
        if(temp == 4 && num4 >0)
        {
            num4--;
            return 4;
        }
        if(temp == 5 && num5 >0)
        {
            num5--;
            return 5;
        }
        if(temp == 6 && num6 >0)
        {
            num6--;
            return 6;
        }
        if(temp == 8 && num8 >0)
        {
            num8--;
            return 8;
        }
        if(temp == 9 && num9 >0)
        {
            num9--;
            return 9;
        }
        if(temp == 10 && num10 >0)
        {
            num10--;
            return 10;
        }
        if(temp == 11 && num11 >0)
        {
            num11--;
            return 11;
        }
        if(temp == 12 && num12 >0)
        {
            num12--;
            return 12;
        }
        return randomNumber();
    }
}