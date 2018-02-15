import java.util.*;

public class Tile extends TileBase
{int m_number;
    SOC.resource m_resource;
    ArrayList<Player> m_givecards;
    Player[] m_players = new Player[6];
    SOC.buildType[] m_buildings = new SOC.buildType[6];
    Player[] m_roads = new Player[6];

    public Tile(SOC.resource r, int n)
    {
        this(0,0,r,n);

    }

    public Tile(int x, int y, SOC.resource r, int n)
    {
        super(x, y, r, n);
        m_number = n;
        m_resource = r;
        m_givecards = new ArrayList<Player>();
    }

    public boolean canBuild(Player p, SOC.buildType b, SOC.location loc)
    {
        switch(b)
        {
            case CITY:
            if(m_players[loc.ordinal()] ==  p && m_buildings[loc.ordinal()] == SOC.buildType.SETTLEMENT)
            {
                return true;
            }
            break;
            case SETTLEMENT:
            if(m_buildings[loc.ordinal()] == null)
            {

                if(loc.ordinal() == 0 )
                {
                    if(m_buildings[5] == null && m_buildings[1] == null)
                    {
                        return true;
                    }
                }
                else
                {
                    if(loc.ordinal() ==5 && m_buildings[4] == null && m_buildings[0] == null)
                    {
                        return true;
                    }
                    else
                    if(m_buildings[loc.ordinal() -1] == null && m_buildings [loc.ordinal() +1] == null)
                    {

                        return true;
                    }

                }
            }
            break;
        }

        return false;  // can't build it 
    }

    public boolean build(Player p, SOC.buildType b, SOC.location loc)
    {
        if(canBuild(p, b, loc) && b == SOC.buildType.CITY)
        {
            m_givecards.add(p);
            m_buildings[loc.ordinal()] = SOC.buildType.CITY;
            return true;
        }
        if(canBuild(p, b, loc) && b == SOC.buildType.SETTLEMENT)
        {
            m_buildings[loc.ordinal()] = SOC.buildType.SETTLEMENT;
            m_players[loc.ordinal()] = p; 
            m_givecards.add(p); 
            return true;
        }
        return false;
    }

    public int roadLocation( SOC.location loc, SOC.location loc2)
    {
        int location;
        //if is n
        if ((loc.ordinal() == 0 && loc2.ordinal() == 5) ||(loc.ordinal() == 5 && loc2.ordinal() == 0))
            location = 5;
        else if(loc.ordinal() > loc2.ordinal())
        {
            location = loc2.ordinal();
        }
        else
            location = loc.ordinal();
        return location;
    }

    public boolean canBuildRoad(Player p, SOC.location loc, SOC.location loc2)
    {
        int location = roadLocation(loc, loc2);
        if (m_resource == SOC.resource.EMPTY)
            return false;
            
        if(((m_players[loc.ordinal()] == p ||m_players[loc2.ordinal()] == p)||adjacentRoad(p, location)) && m_roads[location] == null)
        {
            if(Math.abs(loc.ordinal() - loc2.ordinal()) ==1)
            {
                return true;
            }
            if((loc.ordinal() == 5 && loc2.ordinal() == 0) ||(loc2.ordinal() == 5 && loc.ordinal() == 0)&& m_roads[location] == null)
            {
                return true;
            }
        }
        return false;
    }

    public boolean adjacentRoad(Player p, int location)
    {
        if(location == 0 && (m_roads[5] == p || m_roads[1] == p))
            return true;
        if(location == 5 && (m_roads[0] == p || m_roads[4] == p))
            return true;

        if(location != 0 && location != 5)
            if(m_roads[location - 1] == p || m_roads[location + 1] ==p)
                return true;

        return false;

    }

    public boolean buildRoad(Player p, SOC.location loc, SOC.location loc2)
    {
        m_roads[roadLocation(loc, loc2)] = p;
        return true;
    }

    public void roll(int n)
    {
        if(m_number == n)
        {
            for(int i = 0; i < m_givecards.size(); i ++)
            {
                m_givecards.get(i).collectResources(m_resource, 1);
            }
        }    }

    public static void main(String [] args)
    {
        Player p1 = new Player("Joe");
        Player p2 = new Player("Bob");
        p2.collectResources(SOC.resource.WOOD, 2); // P2 starts with 2 wood
        Tile t = new Tile(SOC.resource.WOOD, 4);
        t.build(p1, SOC.buildType.CITY, SOC.location.NE);  //should return false, no settlement was there
        t.build(p1, SOC.buildType.SETTLEMENT, SOC.location.NE);  //should return true
        t.build(p1, SOC.buildType.SETTLEMENT, SOC.location.NE);  //should return false (already there)
        t.build(p1, SOC.buildType.CITY, SOC.location.NE);  //should return true, settlement was there
        t.buildRoad(p2, SOC.location.S, SOC.location.SW); // false, no p2 settlement 
        t.roll(5); //nothing happens
        t.roll(4); // players with settlements get resources
        System.out.println(p1);
        System.out.println(p2);
    }

    //  public Point getPoint()
    //{
    //    return null;
    // }
    //
}
