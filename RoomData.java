import java.util.Arrays;
/**
 * This class is used to store all the rooms
 * 
 * @author Felix Zhao
 * @version (a version number or a date)
 */
public class RoomData  
{
    protected Tile[][] roomGrid;
    // 0000 - up, down, left, right
    // add unique room shapes later - special rooms e.x chest/shop/special room should be all the same
    public RoomData(int roomType) {
        roomGrid = new Tile[15][25]; 
        for (int i = 0; i < 25; i++) {
            if (i > 10 && i < 14) {
                if ((roomType & 8) == 0) {
                    roomGrid[0][i] = new Wall(this, 0, i);
                } else {
                    roomGrid[0][i] = new RoomExit(this, 0, i, "up");
                }
                
                if ((roomType & 4) == 0) {
                    roomGrid[14][i] = new Wall(this, 14, i);
                } else {
                    roomGrid[14][i] = new RoomExit(this, 14, i, "down");
                }
                continue;
            } 

            roomGrid[0][i] = new Wall(this, 0, i);
            roomGrid[14][i] = new Wall(this, 14, i);
        }
        
        for (int i = 0; i < 15; i++) {
            if (i > 5 && i < 9) {
                if ((roomType & 2) == 0) {
                    roomGrid[i][0] = new Wall(this, i, 0);
                } else {
                    roomGrid[i][0] = new RoomExit(this, i, 0, "left");
                }
                
                if ((roomType & 1) == 0) {
                    roomGrid[i][24] = new Wall(this, i, 24);
                } else {
                    roomGrid[i][24] = new RoomExit(this, i, 24, "right");
                }
                continue;
            } 
            roomGrid[i][0] = new Wall(this, i, 0);
            roomGrid[i][24] = new Wall(this, i, 24);
        }
        
       
    }
    
    public Tile[][] getTileData() {
        return roomGrid;
    }
    // 0000 - special rooms - start - end - loot/shop - special
    public static Tile[][] getRandomRoom(int type) {
        
        return null;
    }
}
