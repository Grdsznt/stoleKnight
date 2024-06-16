import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * One of the heroes.
 * 
 * Note: Load the frames once manually before starting a world!
 * 
 * @author Jean Pan， Andy Feng
 * @version May 2024
 * 
 */
public class Hero1 extends Hero
{
    private static int Hp = 10; //To test
    private static int shield = 10; //To test
    private static int speed = 5; //To test
    protected static int energy = 100; //To test
    protected static GreenfootImage[] idleFramesRight = new GreenfootImage[4];
    protected static GreenfootImage[] idleFramesLeft = new GreenfootImage[4];
    private SimpleTimer animationTimer = new SimpleTimer();
    
    public Hero1() {
        super(Hp, shield, speed, energy, 0, 3);
        loadIdleFrames();
        setImage(idleFramesRight[0]);
        hitbox = new SimpleHitbox(this, getImage().getWidth()/2-3, getImage().getHeight()/2-2, 3, 0);
        // overlay = new Overlay(this, hitbox);
    }
    
    public Hero1(int health, int energy, int slot1, int slot2) {
        super(health, shield, speed, energy, slot1, slot2);
        Hp = health;
        this.energy = energy;
        loadIdleFrames();
        setImage(idleFramesRight[0]);
        hitbox = new SimpleHitbox(this, getImage().getWidth()/2-3, getImage().getHeight()/2-2, 3, 0);
    }
    
    public void addedToWorld(World w) {
        super.addedToWorld(w);
        // w.addObject(overlay, getX(), getY());
        SimpleHitbox.allHitboxesInWorld.add(hitbox);
    }
    
    /**
     * Load all idle frames of this Hero1. Should be called before initializing any Hero1 objects.
     */
    private void loadIdleFrames(){
        for(int i=0; i<idleFramesRight.length; i++){
            idleFramesRight[i] = new GreenfootImage("hero1updated/idle"+i+".png");
        }
        
        for(int i = 0; i < idleFramesLeft.length; i++){
            idleFramesLeft[i] = new GreenfootImage("hero1updated/idle"+i+".png");
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
    
    public SimpleHitbox getHitbox() {
        return hitbox;
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
