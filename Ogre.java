import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Ogre here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Ogre extends Enemy
{
    /**
     * Act - do whatever the Ogre wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private static GreenfootImage[] idleFrames = {
        new GreenfootImage("Ogre/ogre_idle_anim_f0"),new GreenfootImage("Ogre/ogre_idle_anim_f1"),new GreenfootImage("Ogre/ogre_idle_anim_f2"),
        new GreenfootImage("Ogre/ogre_idle_anim_f3")
    };
    private static GreenfootImage[] runFrames = {
        new GreenfootImage("Ogre/ogre_run_anim_f0"),new GreenfootImage("Ogre/ogre_run_anim_f1"),new GreenfootImage("Ogre/ogre_run_anim_f2"),
        new GreenfootImage("Ogre/ogre_run_anim_f3")
    };
    
    private int frameNum, actNum;
    public Ogre() {
        super(1000, 3);
        
    }
    public void act()
    {
        animate();
    }
    
    public void animate() {
        if (actNum % (speed !=0 ? (int) (-6 * speed + 20) : 10) == 0) {
            if (frameNum >= 4) {
            frameNum = 0;
            } else {
                frameNum++;
            }
            if (pursuing) {
                setImage(runFrames[frameNum]);
            } else {
                setImage(idleFrames[frameNum]);
            }
        }
    }
}
