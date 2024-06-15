import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * <p>
 * The abstract superclass for the tiles. Some subclasses have a static final GreenfootImage to be more memory efficent
 * </p>
 * Different tiles have different interactions with other things in the world
 * 
 * @author Felix Zhao
 * @version 0.1
 */
public abstract class Tile extends Actor
{
    protected RoomData parentRoom;
    protected int row;
    protected int col;
    /**
     * Tile Constructor
     *
     * @param parent The room it belongs to
     * @param row The row it is in
     * @param col The col it is in
     */
    public Tile(RoomData parent, int row, int col) {
        parentRoom = parent;
    }
    
}
