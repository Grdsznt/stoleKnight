import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * This tile is used to change rooms for the player
 * <br>
 * It has different states depending on if the rooms can be changed or not
 * @author Felix Zhao
 * @version 0.1
 */
public class RoomExit extends Tile
{
    private static final GreenfootImage notInBattleImage = new GreenfootImage("Tiles/notinbattle.png");
    private static final GreenfootImage InBattleImage = new GreenfootImage("Tiles/inbattle.png");
    private String direction;
    /**
     * Creates a tile that can change rooms
     *
     * @param parent The room it belongs to
     * @param row The row it is in
     * @param col The col it is in
     * @param direction This decides which room to switch to relative to the current room position
     */
    public RoomExit(RoomData parent, int row, int col, String direction) {
        super(parent, row, col);
        
        notInBattleImage.scale(48, 48);
        
        setImage(notInBattleImage);
        this.direction = direction;
    }
    
    
    /**
     * Sets the images of the object depending on if a battle is currently going on
     *
     * @param isInBattle If the state is in or not in the battle
     */
    public void setState(boolean isInBattle) {
        if (!isInBattle) {
            
            
            setImage(notInBattleImage);
        } else {
            
            InBattleImage.scale(48, 48);
            
            setImage(InBattleImage);
        }
    }
    
    /**
     * Returns the direction of the room change
     *
     * @return Returns the direction of the room change
     */
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
