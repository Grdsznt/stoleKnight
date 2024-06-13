import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.HashMap;
import java.util.ArrayList;

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
    private static HashMap<String, String> descriptions = new HashMap<String, String>() {{
        put("Extra HP", "Gain Extra 2 HP");
        put("Faster Shield Recover", "Gain Extra 2 HP");
    }};
    private static ArrayList<String> unselectedPowers;
    private ArrayList<BuffSelection> buffList;
    
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
        gameWorld = world;
        this.hero = hero;
        addObject(new Label("Select a Buff", 32), 600, 500);
        buffList = new ArrayList<BuffSelection>();
        
    }
}
