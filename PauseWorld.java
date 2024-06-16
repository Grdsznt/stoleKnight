import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * This is the pause screen where you can pause the game and/or leave to save current progress
 * 
 * @author Felix Zhao
 * @author Andy Feng
 * @version 0.1
 */
public class PauseWorld extends World
{
    private SuperTextBox resume = new SuperTextBox("Resume", new Color(150,75,0), Color.WHITE, new Font(30), true, 400, 0, Color.WHITE);
    private SuperTextBox saveAndQuit = new SuperTextBox("Save & Quit", new Color(150,75,0), Color.WHITE, new Font(30), true, 400, 0, Color.WHITE);
    private GameWorld gameWorld;
    
    /**
     * Creates the pause world and draws the actors in GameWorld to show the illusion of it still being there
     *
     * @param world The game world to resume back into
     * @param actors The list of actors to draw
     */
    public PauseWorld(GameWorld world, ArrayList<Actor> actors)
    {    
        super(1200, 720, 1); 
        this.gameWorld = world;
        GreenfootImage background = new GreenfootImage(1200, 720);
        background.setColor(new Color(34, 34, 34));
        background.fillRect(0, 0, 1200, 720);
        GreenfootImage stats = new GreenfootImage("statbar.png");
        stats.scale(183, 123);
        background.setColor(Color.WHITE);
        
        background.drawImage(stats, 15, 15);
        setBackground(background);
        drawActors(actors);        
        GreenfootImage filter = new GreenfootImage(1200, 720);
        filter.setColor(new Color(20, 20, 80, 80));
        filter.fillRect(0, 0, 1200, 720);
        getBackground().drawImage(filter, 0, 0);
        addObject(resume, 600, 300);
        addObject(saveAndQuit, 600, 500);
    }
    
    private void drawActors(ArrayList<Actor> actors) {
        // First is on top - last on bottom - everything else more on the bottom
        Class[] classOrder = new Class[] {Weapon.class, Hero.class, Projectile.class, BallProjectile.class, Enemy.class};
        for (Actor actor : actors) {
            GreenfootImage image = actor.getImage();
            
            getBackground().drawImage(image, actor.getX() - image.getWidth()/2, actor.getY() - image.getHeight()/2);    
        }
        
        for (Class<?> cls : classOrder) {
            for (Actor actor : actors) {
                if (!(cls).isAssignableFrom(actor.getClass())) {
                    continue;
                }
                GreenfootImage image = actor.getImage();
                
                getBackground().drawImage(image, actor.getX() - image.getWidth()/2, actor.getY() - image.getHeight()/2);    
            }
        }
        
    }
    
    /**
     * Checks for the buttons to be pressed
     *
     */
    public void act() {
        if(Greenfoot.mouseMoved(resume)) {
            resume.updateFont(new Font(40), 450);
        } else if(Greenfoot.mouseMoved(saveAndQuit)) {
            saveAndQuit.updateFont(new Font(40), 450);
        } 
        
        //Return to normal if not hovering on them
        if(Greenfoot.mouseMoved(this)) {
            resume.updateFont(new Font(30), 400);
            saveAndQuit.updateFont(new Font(30), 400);
        }
        
        if(Greenfoot.mousePressed(resume)) {
            Greenfoot.setWorld(gameWorld);
        }
        if(Greenfoot.mousePressed(saveAndQuit)) {
            // implemnt saving here - you can get the hero data by doing gameWorld.getObjects(Hero.class). 
            GameWorld.stopMusic();
            Greenfoot.setWorld(new StartWorld());
        }
    }
    
    /**
     * When the world is stopped, stop the music
     *
     */
    public void stopped() {
        GameWorld.pauseMusic();
    }
    
    /**
     * When the world is started again, play the music
     *
     */
    public void started() {
        GameWorld.playMusic();
    }
}
