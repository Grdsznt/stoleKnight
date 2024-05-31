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
    protected static GreenfootImage[] idleFramesRight = new GreenfootImage[11];
    protected static GreenfootImage[] idleFramesLeft = new GreenfootImage[11];
    private SimpleTimer animationTimer = new SimpleTimer();
    
    public Hero1() {
        super(Hp, shield, speed, energy, new Hammer());
        loadIdleFrames();
        setImage(idleFramesRight[0]);
    }
    
    /**r
     * Load all idle frames of this Hero1. Should be called before initializing any Hero1 objects.
     */
    private void loadIdleFrames(){
        for(int i=0; i<idleFramesRight.length; i++){
            idleFramesRight[i] = new GreenfootImage("hero1/idle"+i+".png");
        }
        
        for(int i = 0; i < idleFramesLeft.length; i++){
            idleFramesLeft[i] = new GreenfootImage("hero1/idle"+i+".png");
            idleFramesLeft[i].mirrorHorizontally();
        }
    }
    
    public void act()
    {
        // Add your action code here.
        super.act();
        animation();
    }
    
    public void ability() {
        
    }
    
    private int frame = 0;
    public void animation(){
        if(animationTimer.millisElapsed() < 100) return;
        animationTimer.mark();
        if(right){
            setImage(idleFramesRight[frame % idleFramesRight.length]);
            frame++;
        } else {
            setImage(idleFramesLeft[frame % idleFramesRight.length]);
            frame++;
        }
    }
}
