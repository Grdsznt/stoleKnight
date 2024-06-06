import java.util.Arrays;
import java.util.ArrayList;
import greenfoot.*;
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
        if (Greenfoot.getRandomNumber(4) == 0) {
            // special room stuff
            
        }
        roomGrid = new Tile[15][21]; 
        for (int i = 0; i < 21; i++) {
            if (i > 8 && i < 12) {
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
                    roomGrid[i][20] = new Wall(this, i, 24);
                } else {
                    roomGrid[i][20] = new RoomExit(this, i, 21, "right");
                }
                continue;
            } 
            roomGrid[i][0] = new Wall(this, i, 0);
            roomGrid[i][20] = new Wall(this, i, 21);
        }
        
        if ((roomType & 16) != 0) {

        } else if ((roomType & 32) != 0) {
            roomGrid[5][5] = new Wall(this, 5, 5);
            roomGrid[6][5] = new Wall(this, 6, 5);
        } else if ((roomType & 64) != 0) {
            roomGrid[7][10] = new Chest(this, 10, 7);
        }
        String[][] innerTiles = getRandomRoom(roomType);
        if (innerTiles != null) {
            for (int i = 0; i < innerTiles.length; i++) {
                for (int j = 0; j < innerTiles[i].length; j++) {
                    if (innerTiles[i][j] == null) {
                        continue;
                    }
                    switch (innerTiles[i][j]) {
                        case "Wall":
                            roomGrid[i+3][j+3] = new Wall(this, i+3, j+3);
                            break;
                    }
                    
                    
                }
            }
        }
        
        
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 21; j++) {
                if (roomGrid[i][j] instanceof Wall) {
                    int count = 0;
                    if (i != 0 && roomGrid[i-1][j] instanceof Wall) {
                        count += 8;
                    }
                    if (i+1 < 15 && roomGrid[i+1][j] instanceof Wall) {
                        count+=4;
                    }
                    if (j != 0 && roomGrid[i][j-1] instanceof Wall) {
                        count+=2;
                    }
                    if (j+1 < 21 && roomGrid[i][j+1] instanceof Wall) {
                        count+=1;
                    }
                    roomGrid[i][j].setImage("Tiles/wall" + count + ".png");
                    System.out.println("Stf: "+ count);
                    roomGrid[i][j].getImage().scale(48, 48);
                    
                }
            }
        }
        
    }
    
    public Tile[][] getTileData() {
        return roomGrid;
    }
    // 0000 - special rooms - start - end - loot/shop - special
    public static String[][] getRandomRoom(int type) {
        if (type >= 16) {
            return null;
        }
        return innerRooms[Greenfoot.getRandomNumber(innerRooms.length)];
    }
    
    
    // for the non-special shapes
    // 1-15 - 16 start - 32 end -64 chest - 128 - special
    // 19*9 (25-5, 15-4)
    // start from (3,3) end at (21, 11) 0-indexed
    private static String[][][] innerRooms = new String[][][] {
        {
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            
        },
        
        {
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", "Wall", null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, "Wall", null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
        }
    };
}
