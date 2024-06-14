import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * A button to select a buff
 * 
 * @author Felix Zhao
 * @version 1
 */
public class BuffSelection extends Actor
{
    
    private String buffName;
    private String description;
    private ArrayList<BuffSelection> otherOptions;
    
    /**
     * Creates a button for the user to select
     *
     * @param buff The buff name - has a coorelated image
     * @param desc The description
     * @param otherOptions The other buffs to choose from
     */
    public BuffSelection(String buff, String desc, ArrayList<BuffSelection> otherOptions) {
        buffName = buff;
        description = desc;
        this.otherOptions = otherOptions;
        setImage("buffs/" + buff);
        getImage().scale(48, 48);
    }
    /**
     * Act - do whatever the BuffSelection wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
}
