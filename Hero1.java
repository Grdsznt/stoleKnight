import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * One of the heroes.
 * 
 * Note: Load the frames once manually before starting a world!
 * 
 * @author Jean Pan
 * @version May 2024
 */
public class Hero1 extends Hero
{
    private static int Hp = 100; //To test
    private static int shield = 0; //To test
    private static int speed = 2; //To test
    protected static int energy = 200; //To test
    protected static GreenfootImage[] idleFrames = new GreenfootImage[11];
    
    public Hero1() {
        super(Hp, shield, speed, energy, idleFrames);
    }
    
    /**
     * Load all idle frames of this Hero1. Should be called before initializing any Hero1 objects.
     */
    private static void loadIdleFrames(){
        for(int i=0; i<idleFrames.length; i++){
            idleFrames[i] = new GreenfootImage("hero1/idle"+i+".png");
        }
    }
    
    public void act()
    {
        // Add your action code here.
    }
    
    public void ability() {
        
    }
    
    public void animation(){
        
    }
}
