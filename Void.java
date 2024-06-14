import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Blocks the player and enemies from going over this tile
 * 
 * @author Felix Zhao
 * @version 0.1
 */
public class Void extends Tile
{
    /**
     * Creates a void tile
     *
     * @param parent The room it belongs to
     * @param row The row it is in
     * @param col The col it is in
     */
    public Void(RoomData parent, int row, int col) {
        super(parent, row, col);
        GreenfootImage image = new GreenfootImage("Tiles/void.png");
        
        
        setImage(image);
        getImage().scale(48, 48);
        
    }
}
