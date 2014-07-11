
package multiuserdungeon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author james
 */
public class Player {

  private Room currentRoom; // current room the Player is in
  private String name;

  private Server server; // used to send messages and such

  public Player(String n, Server s){
    this.name = n;
    this.server = s;
  }

  public String getName(){
    return this.name;
  }

  public Server getServer(){
    return this.server;
  }

  public void setCurrentRoom(Room r){
    this.currentRoom = r;
  }

  public Room getCurrentRoom(){
    return this.currentRoom;
  }

}
