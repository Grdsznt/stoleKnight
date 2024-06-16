import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Creates a wall that blocks enemies and the player
 * 
 * @author Felix Zhao
 * @author Edwin Dong
 * @version 0.1
 */
public class Wall extends Tile
{
    
    private Line topBoundary, leftBoundary, rightBoundary, bottomBoundary;
    
    /**
     * Chest Constructor
     *
     * @param parent The room it belongs to
     * @param row The row it is in
     * @param col The col it is in
     */
    public Wall(RoomData parent, int row, int col) {
        super(parent, row, col);
         GreenfootImage image = new GreenfootImage("Tiles/wall0.png");
        
         image.scale(48, 48);
  
         setImage(image);
         
       
    }
    
    
    public void addedToWorld(World w) {
        int centerX = getX(), centerY = getY();
        topBoundary = new Line(new Pair(centerX-30, centerY+30), new Pair(centerX+30, centerY+30));
        bottomBoundary = new Line(new Pair(centerX-30, centerY-30), new Pair(centerX+30, centerY-30));
        leftBoundary = new Line(new Pair(centerX-30, centerY-30), new Pair(centerX-30, centerY+30));
        rightBoundary = new Line(new Pair(centerX+30, centerY-30), new Pair(centerX+30, centerY+30));
    }
    /**
     * Get the top boundary
     *
     * @return Get the top boundary
     */
    public Line getTopBoundary() {
        return topBoundary;
    }
    /**
     * Get the bottom boundary
     *
     * @return Get the bottom boundary
     */
    public Line getBottomBoundary() {
        return bottomBoundary;
    }
    /**
     * Get the left boundary
     *
     * @return Get the left boundary
     */
    public Line getLeftBoundary() {
        return leftBoundary;
    }
    /**
     * Get the right boundary
     *
     * @return Get the right boundary
     */
    public Line getRightBoundary() {
        return rightBoundary;
    }
}

