public class Strategyee implements PlayerStrategy
{
    public int[] m_rank;
    int a = 0;
    public SOC.Junction placeFirstSettlement(BoardInterface b)
    {
        return b.availableJunctions(SOC.resource.BRICK).get(b.availableJunctions(SOC.resource.BRICK).size() - 2);
    }

    public SOC.Junction placeFirstRoad(BoardInterface b)
    {
        return b.availableRoads().get(1);
    }

    public SOC.Junction placeSecondSettlement(BoardInterface b)
    {
        populateArray(b);
        populateArray(b);
        return b.availableJunctions().get(findLargest(m_rank));
        //return b.availableJunctions(SOC.resource.ORE).get(0);
    }

    public SOC.Road placeSecondRoad(BoardInterface b)
    {
        return b.availableRoads().get(1);
    }

    public void prioritizeResources(BoardInterface b)
    {
        if(b.resourceCount(SOC.resource.SHEEP) > 4)
            b.trade(SOC.resource.SHEEP, SOC.resource.ORE);
    }

    
    public int rankJunction(BoardInterface b, SOC.Junction j)
    {
       
        for(int i = 0; i < j.address.length; i++)
        {
            switch(b.tileResource(j.address[i]))
            {
                case SHEEP:
                a += 2; 
                break;
                case WHEAT:
                a += 3; 
                break;
                case ORE:
                a += 4; 
                break;
                case BRICK:
                a += 5; 
                break;
                case WOOD:
                a += 3; 
                break;
            }
        }
        for(int i = 0; i < j.address.length; i++)
        {
            switch(b.tileNumber(j.address[i]))
            {
                case 2:
                a += 1; 
                case 3:
                a += 2; 
                case 4:
                a += 3; 
                case 5:
                a += 4; 
                case 6:
                a += 9; 
                case 8:
                a += 9; 
                case 9:
                a += 4; 
                case 10:
                a += 3; 
                case 11:
                a += 2; 
                case 12:
                a += 1; 
            }
        }
        return a;
    }

    public void playertrade(BoardInterface b)
    {
        if(b.resourceCount(SOC.resource.ORE) >= 4)
        {
            if(b.resourceCount(SOC.resource.WHEAT) == 0)
                b.trade(SOC.resource.ORE, SOC.resource.WHEAT);
            if(b.resourceCount(SOC.resource.BRICK) == 0)
                b.trade(SOC.resource.ORE, SOC.resource.BRICK);
            if(b.resourceCount(SOC.resource.WOOD) == 0)
                b.trade(SOC.resource.ORE, SOC.resource.WOOD);
            if(b.resourceCount(SOC.resource.SHEEP) == 0)
                b.trade(SOC.resource.ORE, SOC.resource.SHEEP);
            
        }
        if(b.resourceCount(SOC.resource.WHEAT) >= 4)
        {
            if(b.resourceCount(SOC.resource.WOOD) == 0)
                b.trade(SOC.resource.WHEAT, SOC.resource.WOOD);
            if(b.resourceCount(SOC.resource.ORE) == 0)
                b.trade(SOC.resource.WHEAT, SOC.resource.ORE);
            if(b.resourceCount(SOC.resource.BRICK) == 0)
                b.trade(SOC.resource.WHEAT, SOC.resource.BRICK);
            
            if(b.resourceCount(SOC.resource.SHEEP) == 0)
                b.trade(SOC.resource.WHEAT, SOC.resource.SHEEP);
            
        }
        if(b.resourceCount(SOC.resource.BRICK) >= 4)
        {
            if(b.resourceCount(SOC.resource.SHEEP) == 0)
                b.trade(SOC.resource.BRICK, SOC.resource.SHEEP);
            if(b.resourceCount(SOC.resource.WHEAT) == 0)
                b.trade(SOC.resource.BRICK, SOC.resource.WHEAT);
            if(b.resourceCount(SOC.resource.ORE) == 0)
                b.trade(SOC.resource.BRICK, SOC.resource.ORE);
            if(b.resourceCount(SOC.resource.WOOD) == 0)
                b.trade(SOC.resource.BRICK, SOC.resource.WOOD);
            
            
        }
        if(b.resourceCount(SOC.resource.SHEEP) >= 4)
        {
            if(b.resourceCount(SOC.resource.BRICK) == 0)
                b.trade(SOC.resource.SHEEP, SOC.resource.BRICK);
            if(b.resourceCount(SOC.resource.WHEAT) == 0)
                b.trade(SOC.resource.SHEEP, SOC.resource.WHEAT);
            
            if(b.resourceCount(SOC.resource.WOOD) == 0)
                b.trade(SOC.resource.SHEEP, SOC.resource.WOOD);
            if(b.resourceCount(SOC.resource.ORE) == 0)
                b.trade(SOC.resource.SHEEP, SOC.resource.ORE);
            
        }
        if(b.resourceCount(SOC.resource.WOOD) >= 4)
        {
            if(b.resourceCount(SOC.resource.ORE) == 0)
                b.trade(SOC.resource.WOOD, SOC.resource.ORE);
            if(b.resourceCount(SOC.resource.WHEAT) == 0)
                b.trade(SOC.resource.WOOD, SOC.resource.WHEAT);
            if(b.resourceCount(SOC.resource.BRICK) == 0)
                b.trade(SOC.resource.WOOD, SOC.resource.BRICK);
            
            if(b.resourceCount(SOC.resource.SHEEP) == 0)
                b.trade(SOC.resource.WOOD, SOC.resource.SHEEP);
            
        }
    }

    private static int findLargest(int array[])
    {
        int largest = array[0];
        int largestIndex = 0;

        for(int i = 0; i < array.length; i++)
        {
            if(array[i] > largest) {
                largest = array[i]; 
                largestIndex =i;
            }  
        }

        return largestIndex;
    }

    public void populateArray(BoardInterface b)
    {
        m_rank = new int[b.availableJunctions().size()]; 
        for(int i = 0; i < b.availableJunctions().size(); i++)
        {
            m_rank[i] = rankJunction(b, b.availableJunctions().get(i));
        }
    }

    public void takeTurn(BoardInterface b)
    {
        b.buyCard();
        populateArray(b);
        populateArray(b);
        if (b.availableJunctions().size() > 0)
        {
            b.build(b.availableJunctions().get(findLargest(m_rank)));
        }
        
        if (b.availableJunctions().size() > 0)
        {
            b.build(b.availableJunctions().get(findLargest(m_rank)));
        }
        
        if(b.availableRoads().size() > 0)
        {

            b.build(b.availableRoads().get(0));
        }
        playertrade(b); 
    }

    public int placeRobber()// return tile to place robber
    {return 0;}

    public int stealResource(String[] names) // return which player to steal from
    {return 0;}

    public void robbed(){
        
    }
}
