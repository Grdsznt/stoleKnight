import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Write a description of class Ogre here.
 * 
 * @author Edwin
 * @version 31.5.24
 */
public class Ogre extends Enemy
{
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
        super(1000, 3, 50);
        
    }
    public void act()
    {
        if (!pursuing) {
            Pair p = getRandomPositionWithinRadius(targetRadius);
            // pathfind to this random position in radius
            if (actNum % 20 == 0) h = getHeroInRadius();
        }
    
        // if (h != null && hasLineOfSight(new Pair(getX(), getY()), new Pair(h.getX(), h.getY()), getWorld().getObstacles())) {
            // pursuing = true;
            // // chase the hero
            // turnTowards(h.getX(), h.getY());
            // move(speed);
            // // if the hero is no longer in the radius do not pursue
            // if (calculateDistance(getX(), getY(), h.getX(), h.getY()) > targetRadius) {
                // pursuing = false;
                // h = null;
            // }
        // }
        // animate();
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
