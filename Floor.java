import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Floor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Floor extends Tile
{
    public Floor(RoomData parent, int row, int col) {
        super(parent, row, col);
         GreenfootImage image = new GreenfootImage("Tiles/floor.png");
        
         image.scale(48, 48);
  
         setImage(image);
         
       
    }
    /**
     * Act - do whatever the Floor wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
}
