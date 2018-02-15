import java.awt.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class SOC
{
    /// Don't change these
    public enum resource
    {
        EMPTY, DESERT, SHEEP, ORE, WOOD, WHEAT, BRICK
    }
    public enum buildType
    {
        EMPTY, SETTLEMENT, CITY, ROAD
    }
    public enum location
    {
        N, NE, SE, S, SW, NW
    }
   static class piece 
    {
        Color m_color;
        buildType m_type;
        Polygon m_poly;
        public piece(buildType t, Color c, Point p)
        {
            m_color = c;
            if (t == SOC.buildType.SETTLEMENT)
            {
                int x[] = {p.x, p.x + 16, p.x - 16};
                int y[] = {p.y - 10, p.y +  10, p.y + 10};
                m_poly = new Polygon(x, y, x.length);
            }
            else if (t == SOC.buildType.CITY)
            {
                int x[] = {p.x - 18, p.x + 18, p.x + 18, p.x - 18};
                int y[] = {p.y - 9, p.y - 9, p.y + 9, p.y + 9};
                m_poly = new Polygon(x, y, x.length);
            }
            else if (t == SOC.buildType.ROAD)
            {
                int x[] = {p.x - 5, p.x + 5, p.x + 5, p.x - 5};
                int y[] = {p.y - 5, p.y - 5, p.y + 5, p.y + 5};
                m_poly = new Polygon(x, y, x.length);
            }
            
        }

        Polygon shape()
        {
            return m_poly;
        }

        Color color()
        {
            return m_color;
        }
    }
}
