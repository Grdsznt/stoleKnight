import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Necromancer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wizard extends Enemy
{
    /**
     * Act - do whatever the Necromancer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private static GreenfootImage[] idleFrames = {
        new GreenfootImage("Wizard/wizzard_m_idle_anim_f0.png"),new GreenfootImage("Wizard/wizzard_m_idle_anim_f1.png"),new GreenfootImage("Wizard/wizzard_m_idle_anim_f2.png"),
        new GreenfootImage("Wizard/wizzard_m_idle_anim_f3.png")
    };
    private static GreenfootImage[] runFrames = {
        new GreenfootImage("Wizard/wizzard_m_run_anim_f0.png"),new GreenfootImage("Wizard/wizzard_m_run_anim_f1.png"),new GreenfootImage("Wizard/wizzard_m_run_anim_f2.png"),
        new GreenfootImage("Wizard/wizzard_m_run_anim_f3.png")
    };
    
    private GreenfootImage hitImage = new GreenfotoImage("Wizard/wizzard_m_hit_anim_f0.png");
    
    
    public void act()
    {
        // Add your action code here.
    }
    
    public Wizard() {
        super(300, 4, 60, 450, 450);
    }
}
