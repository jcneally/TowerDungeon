/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multiuserdungeon;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author james
 */

// Rooms exist in a Dungeon
public class Room {


  // Know about adjacent rooms
  private Room south;
  private Room east;
  private Room north;
  private Room west;
  public int x; // location in a 2d grid for level setup
  public int y;
  public boolean done = false; // if true, room is finished building

  private boolean hasStairsUp = false; // whether this room has stairs to the next level

  // description of the room
  private String description;

  // all players currently in the room
  private ArrayList<Player> players;


  // fun things for users to get in their rooms
  private String[] adjectives = {"tastefully furnished", "shoddy", "colorful", "dull", "cold and lifeless", "cramped", "spacious", "well-lit", "dingy"};
  private String[] nouns = {"a large suit of armor", "absolutely nothing", "several objects you don't recognize", "a full IKEA furniture set",
                            "a toilet, but no toilet paper", "an old writing desk", "a swarm of rats", "two hundred thousand dollars cash"};


  public Room(){
    this.description = generateDescription();
    players = new ArrayList<Player>();
  }

  public String getDescription(){
    return this.description;
  }

  public ArrayList<Player> getPlayers(){
    return players;
  }

  // adds player to room
  public void addPlayer(Player p){
    this.players.add(p);
    p.setCurrentRoom(this);
  }

  public void removePlayer(Player p){
    this.players.remove(p);
  }

  public Room north(){
    return this.north;
  }

  public Room south(){
    return this.south;
  }

  public Room east(){
    return this.east;
  }

  public Room west(){
    return this.west;
  }

  public String getRoomInfo(){
    String info = "";
    info += this.description + " Exits are ";
    info += (this.north == null) ? "" : "NORTH ";
    info += (this.south == null) ? "" : "SOUTH ";
    info += (this.east == null) ? "" : "EAST ";
    info += (this.west == null) ? "" : "WEST";
    return info;
  }

  public void setAdjacentRoom(String dir, Room r){
    if(dir.equals("north")){
      this.north = r;
    }
    else if(dir.equals("east")){
      this.east = r;
    }
    else if(dir.equals("south")){
      this.south = r;
    }
    else if(dir.equals("west")){
      this.west = r;
    }
  }

  // generates a description for a room
  private String generateDescription(){
    String desc = "";

    Random rand = new Random();

    // a couple patterns for room descriptions to choose from
    switch(rand.nextInt(3)){
      case 0:
        desc += "A " + adjectives[rand.nextInt(adjectives.length)] + " room, featuring " + nouns[rand.nextInt(nouns.length)] + ".";
        break;
      case 1:
        desc += "You can clearly see " + nouns[rand.nextInt(nouns.length)] + " in the center of the " + adjectives[rand.nextInt(adjectives.length)] + " room.";
        break;
      case 2:
        desc += "You would say this room is quite " + adjectives[rand.nextInt(adjectives.length)] + ". But what really sparks your interest is " + nouns[rand.nextInt(nouns.length)] + ".";
        break;
    }

    return desc;
  }

}
