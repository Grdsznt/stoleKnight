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
    private Label nameLabel;
    private Label descLabel;
    private String buffName;
    private String description;
    private ArrayList<BuffSelection> otherOptions;
    private boolean selected;
    
    /**
     * Creates a button for the user to select
     *
     * @param buff The buff name - has a coorelated image
     * @param desc The description
     * @param otherOptions The other buffs to choose from
     */
    public BuffSelection(String buff, String desc, ArrayList<BuffSelection> otherOptions, Label nameLabel, Label descLabel) {
        buffName = buff;
        description = desc;
        this.otherOptions = otherOptions;
        setImage("buffs/" + buff + ".png");
        getImage().scale(64, 64);
        selected = false;
        this.nameLabel = nameLabel;
        this.descLabel = descLabel;
    }
    /**
     * Act - do whatever the BuffSelection wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (Greenfoot.mousePressed(this)) {
            selected = true;
            for (BuffSelection buff : otherOptions) {
                if (buff != this) {
                    buff.unselect();
                }
            }
            nameLabel.setValue(buffName);
            descLabel.setValue(description);
        }
    }
    
    /**
     * Returns if the power is currently selected 
     *
     * @return Returns if the power is currently selected 
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * Deactivates the button
     *
     */
    public void unselect() {
        selected = false;
    }
    
    /**
     * Returns the buff Name
     *
     * @return Returns the buff name
     */
    public String getPowerName() {
        return buffName;
    }
}
