import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Filled here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Void extends Tile
{
    public Void(RoomData parent, int row, int col) {
        super(parent, row, col);
        GreenfootImage image = new GreenfootImage("Tiles/void.png");
        
        
        setImage(image);
        getImage().scale(48, 48);
        
    }
}
