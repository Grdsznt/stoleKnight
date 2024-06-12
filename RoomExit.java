import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class RoomExit here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RoomExit extends Tile
{
    private String direction;
    public RoomExit(RoomData parent, int row, int col, String direction) {
        super(parent, row, col);
        GreenfootImage image = new GreenfootImage("Tiles/notinbattle.png");
        image.scale(48, 48);
        
        setImage(image);
        this.direction = direction;
    }
    /**
     * Act - do whatever the RoomExit wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
    
    /**
     * Sets the images of the object depending on if a battle is currently going on
     *
     * @param isInBattle If the state is in or not in the battle
     */
    public void setState(boolean isInBattle) {
        if (!isInBattle) {
            GreenfootImage image = new GreenfootImage("Tiles/notinbattle.png");
            image.scale(48, 48);
            
            setImage(image);
        } else {
            GreenfootImage image = new GreenfootImage("Tiles/inbattle.png");
            image.scale(48, 48);
            
            setImage(image);
        }
    }
    
    public String activate() {
        GameWorld world = getWorldOfType(GameWorld.class);
        int[] pos = world.getRoomPosition();
        if (direction.equals("up")) {
            pos[0]--;

        } else if (direction.equals("down")) {
            pos[0]++;

        } else if (direction.equals("left")) {
            pos[1]--;

        } else if (direction.equals("right")) {
            pos[1]++;
        }
        
        if (world.changeRooms(pos)) {
            return direction;
        }
        
        return null;
    }
}
