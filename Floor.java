import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The floor class doesn't do much other than for art
 * 
 * @author Felix Zhao
 * @version 0.1
 */
public class Floor extends Tile
{
    /**
     * Floor construtor
     *
     * @param parent The room it belongs to
     * @param row The row it is in
     * @param col The col it is in
     */
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
