import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Wall here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wall extends Tile
{
    
    Line topBoundary, leftBoundary, rightBoundary, bottomBoundary;
    
    
    public Wall(RoomData parent, int row, int col) {
        super(parent, row, col);
         GreenfootImage image = new GreenfootImage("Tiles/wall0.png");
        
         image.scale(48, 48);
  
         setImage(image);
         
       
    }
    /**
     * Act - do whatever the Wall wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
       
    }
    
    public void addedToWorld(World w) {
        int centerX = getX(), centerY = getY();
        topBoundary = new Line(new Pair(centerX-44, centerY+44), new Pair(centerX+44, centerY+44));
        bottomBoundary = new Line(new Pair(centerX-44, centerY-44), new Pair(centerX+44, centerY-44));
        leftBoundary = new Line(new Pair(centerX-44, centerY-44), new Pair(centerX-44, centerY+44));
        rightBoundary = new Line(new Pair(centerX+44, centerY-44), new Pair(centerX+44, centerY+44));
    }
    public Line getTopBoundary() {
        return topBoundary;
    }
    public Line getBottomBoundary() {
        return bottomBoundary;
    }
    public Line getLeftBoundary() {
        return leftBoundary;
    }
    public Line getRightBoundary() {
        return rightBoundary;
    }
}

