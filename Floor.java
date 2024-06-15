import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The floor class doesn't do much other than for art
 * 
 * @author Felix Zhao
 * @version 0.1
 */
public class Floor extends Tile
{
    private static final GreenfootImage image = new GreenfootImage("Tiles/floor.png");
    /**
     * Floor construtor
     *
     * @param parent The room it belongs to
     * @param row The row it is in
     * @param col The col it is in
     */
    public Floor(RoomData parent, int row, int col) {
        super(parent, row, col);
         
        
         image.scale(48, 48);
  
         setImage(image);
         
       
    }
}
