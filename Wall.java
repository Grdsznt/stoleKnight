import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Wall here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wall extends Tile
{
    
    /**
     * Pair is a utility class that stores 2 numbers. 
     * <p>
     * Used for coordinates in the world's tile system.
     */
    protected class Pair {
        int f, s;
        public Pair(int first, int second) {
           f = first; s = second; // stores 2 numbers
        } 
    }
    
     /**
     * Line is a utility class that stores 2 pairs. 
     * <p>
     * Used for Line of sight.
     */
    protected class Line {
        Pair start, end;
        Line(Pair start, Pair end) {
            this.start = start;
            this.end = end;
        }
    }
    
    Line topBoundary, leftBoundary, rightBoundary, bottomBoundary;
    
    
    public Wall(RoomData parent, int row, int col) {
        super(parent, row, col);
         GreenfootImage image = new GreenfootImage(48, 48);
       image.setColor(new Color(0, 0, 0));
       image.drawRect(0, 0, 47, 47);

       setImage(image);
       
    }
    /**
     * Act - do whatever the Wall wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
       
    }
    
    public void addedToWorld() {
        
    }
}

