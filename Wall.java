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
        
    Wall() {
         GreenfootImage image = new GreenfootImage(75, 75);
       image.setColor(new Color(0, 0, 0));
       image.drawRect(0, 0, 74, 74);

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
        int centerX = getX(), centerY = getY();
        topBoundary = new Line(new Pair(centerX-75, centerY+75), new Pair(centerX+75, centerY+75));
        bottomBoundary = new Line(new Pair(centerX-75, centerY-75), new Pair(centerX+75, centerY-75));
        leftBoundary = new Line(new Pair(centerX-75, centerY-75), new Pair(centerX-75, centerY+75));
        rightBoundary = new Line(new Pair(centerX+75, centerY-75), new Pair(centerX+75, centerY+75));
        

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

