import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Shop here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Shop extends Tile
{
    public Shop(RoomData parent, int row, int col) {
        super(parent, row, col);
        GreenfootImage image = new GreenfootImage("Tiles/notinbattle.png");
        image.scale(48, 48);
        
        setImage(image);
    }
}
