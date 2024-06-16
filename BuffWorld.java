import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>
 * This world is after the player enters the portal. Every other completion the player can select from a few different buffs
 * </p>
 * @author Felix Zhao
 * @version 0.1
 */
public class BuffWorld extends World
{
    private GameWorld gameWorld;
    private Hero hero;
    private static HashMap<String, String> descriptions = new HashMap<String, String>() {{
        put("Extra HP", "Gain Extra 2 HP");
        put("Faster Shield Recover", "Shield Starts recovering in less time");
        put("More Shield", "Gain Extra 2 Shield");
        put("Longer Immunity", "Invincibility frames last longer");
        put("Swiftness", "Extra Speed");
        put("Better Loot", "Get more gold everywhere");
        put("Effective Potions", "Potions are more effective");
        put("More Energy", "Have 100 more Energy");
        put("More Attack Speed", "The time between attacks decreases");
        put("Energy Steal", "Steal energy from killing enemies");
        put("Life Steal", "Small chance to gain life from killing enemies");
    }};
    private static ArrayList<String> unselectedPowers = new ArrayList<String>(Arrays.asList(
        "Extra HP",
        "Faster Shield Recover",
        "More Shield",
        "Longer Immunity",
        "Swiftness",
        "Better Loot",
        "Effective Potions",
        "More Energy",
        "More Attack Speed",
        "Energy Steal",
        "Life Steal"
    ));
    private ArrayList<BuffSelection> buffList;
    private Label continueButton;
    
    /**
     * Constructor for objects of class BuffWorld.
     * 
     * @param world The GameWorld that created this world
     * @param hero The hero actor
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
        if (gameWorld.getFloor() % 2 == 0) {
            addObject(new Label("Select a Buff", 32), 600, 350);
            buffList = new ArrayList<BuffSelection>();
            Label buffName = new Label("", 32);
            Label buffDesc = new Label("", 24); 
            addObject(buffName, 600, 450);
            addObject(buffDesc, 600, 500);
            if (unselectedPowers.size() < 3) {
                return;
            } 
            ArrayList<String> randomList = new ArrayList<String>(unselectedPowers);
            for (int i = 0; i < 3; i++) {
                int selection = Greenfoot.getRandomNumber(randomList.size());
                buffList.add(new BuffSelection(randomList.get(selection), descriptions.get(randomList.get(selection)), buffList, buffName, buffDesc));
                randomList.remove(selection);
            }
            
            addObject(buffList.get(0), 500, 550);
            addObject(buffList.get(1), 600, 550);
            addObject(buffList.get(2), 700, 550);
        } else {
            addObject(new Label("Good Job! More buffs Soon", 32), 600, 500);
            
        }
        
        
        continueButton = new Label("Continue", 40);
        addObject(continueButton, 600, 620);
    }
    
    /**
     * Checks for clicks
     *
     */
    public void act() {
        if (Greenfoot.mouseMoved(continueButton)) {
            continueButton.setFillColor(new Color(200, 200, 200));
        } else if (Greenfoot.mouseMoved(null)) {
            continueButton.setFillColor(new Color(255, 255, 255));
        }
        
        if (Greenfoot.mouseClicked(continueButton)) {
            if (gameWorld.getFloor() % 2 == 0) {
                for (BuffSelection buff : buffList) {
                    if (buff.isSelected()) {
                        Greenfoot.setWorld(gameWorld);
                        hero.addPower(buff.getPowerName());
                        unselectedPowers.remove(buff.getPowerName());
                    }
                }
            } else {
                Greenfoot.setWorld(gameWorld);
            }
            
        }
    }
    
    /**
     * Resets the unselected list
     *
     */
    public static void resetUnselectedList() {
        unselectedPowers = new ArrayList<String>(Arrays.asList(
        "Extra HP",
        "Faster Shield Recover",
        "More Shield",
        "Longer Immunity",
        "Swiftness",
        "Better Loot",
        "Effective Potions",
        "More Energy",
        "More Attack Speed",
        "Energy Steal",
        "Life Steal"
    ));
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
