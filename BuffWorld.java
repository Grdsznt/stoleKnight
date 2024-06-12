import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BuffWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BuffWorld extends World
{
    private GameWorld gameWorld;
    private Hero hero;
    
    /**
     * Constructor for objects of class BuffWorld.
     * 
     */
    public BuffWorld(GameWorld world, Hero hero)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1200, 720, 1); 
        gameWorld = world;
        
        GreenfootImage background = new GreenfootImage(1200, 720);
        background.setColor(new Color(34, 34, 34));
        background.fillRect(0, 0, 1200, 720);
        setBackground(background);
    }
}
