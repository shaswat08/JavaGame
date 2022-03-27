

import java.util.ArrayList;
import java.util.Scanner;


/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Shaswat Shah
 * @version 2016.02.29
 */

public class Game
{
    private Parser parser;
    private Room currentRoom;
    private Player player;
    private Character character;
    int Hp;
    String playerName;
    
    private Scanner reader = new Scanner(System.in);
    
    ArrayList<Item> inventory=new ArrayList<Item>();
    private int count=1;
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        player = new Player(playerName);
        
        
    }
    public static void main(String[] args)
    {
        Game myGame=new Game();
        Scanner input = new Scanner(System.in);
        myGame.play();
        objectsInRoom obj = new objectsInRoom();
        
        
        
        
        
    }
     public void addCharacter(Character character)
    {
        this.character = character;
    }
     
    /**
     * Returns the character object.
     */
    public Character getCharacter()
    {
        return character;
    }
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, security, yard, ladder, chapel, mainEntrance, hallway, basement, lab, kitchen, insideLab ;
        
      
        outside = new Room("are outside the main entrance of the asylum");
        security = new Room("are in the security guard area");
        yard = new Room("are in the yard of the asylum");
        ladder = new Room("see a mysterious ladder that seems to lead to the first floor of the asylum");
        chapel = new Room("are in the chapel. You see blood all over the place.");
        mainEntrance = new Room("are in the main entrance of the asylum. The door seems budged shut. You need a key.");
        hallway = new Room("are in an eerie looking hallway.");
        basement = new Room("are in a dirty basement and see something stalking u from the shadows.");;
        lab = new Room("see a strange lab that seems to operate with a keycard, unfortunately u dont have one.");
        kitchen = new Room("enter a stained filled kitchen thats swarming with rats and all kinds of vermins. All the food has rotted away.");
        insideLab=new Room("open the lab and see a computer that is flashing.");
        
        // initialise room exits
        outside.setExit("east", security);
        outside.setExit("south", yard);
        outside.setExit("west", ladder); 
        outside.setExit("north", mainEntrance); 
        hallway.setExit("left", chapel);
        hallway.setExit("southeast", basement);
        hallway.setExit("west", kitchen);
        hallway.setExit("south", lab);
        hallway.setExit("southwest", ladder);
        ladder.setExit("up", hallway);
        ladder.setExit("down", outside);
        yard.setExit("north", outside);
        security.setExit("west", outside);
        mainEntrance.setExit("south", outside);
        mainEntrance.setExit("open", mainEntrance);
        chapel.setExit("east", hallway);
        basement.setExit("northwest", hallway);
        kitchen.setExit("southeast", hallway);
        lab.setExit("west", hallway);
        lab.setExit("open", insideLab);
        insideLab.setExit("out", lab);
        
        
        currentRoom = outside;  // start game outside
        
        
        
        security.setItem(new Item("Camera"));
        security.setItem(new Item("Documents"));
        yard.setItem(new Item("RustyShovel"));
        yard.setItem(new Item("BloodySign"));
        mainEntrance.setItem(new Item("BrokenDoorknob"));
        mainEntrance.setItem(new Item("Map"));
        ladder.setItem(new Item("Wood"));
        hallway.setItem(new Item("PieceOfGlass"));
        hallway.setItem(new Item("Money"));
        chapel.setItem(new Item("Book"));
        chapel.setItem(new Item("Lamp"));
        basement.setItem(new Item("Battery"));
        basement.setItem(new Item("KeyCard"));
        kitchen.setItem(new Item("Knife"));
        kitchen.setItem(new Item("Kerosene"));
        insideLab.setItem(new Item("Computer"));
    }
    

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println("What is your name?");
        playerName = reader.nextLine();
        System.out.println();
        System.out.println("Welcome" + playerName + "to Outlast!");
        System.out.println("Your Hp" + Hp);
        System.out.println("Outlast is a text based survival horror game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;
        

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("inventory")) {
            printInventory();
        }
        else if (commandWord.equals("get")) {
            wantToQuit=getItem(command);
        }
        else if (commandWord.equals("drop")) {
            dropItem(command);
        }
        
        
        return wantToQuit;
    }
    
    private void dropItem(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to drop...
            System.out.println("Drop what?");
            return;
        }

        String item = command.getSecondWord();

        
        Item newItem = null;
        int index = 0;
        for (int i=0; i < inventory.size(); i++){
            if (inventory.get(i).getDescription().equals(item)) {
                newItem = inventory.get(i);
                index = i;
            }
        }

        if (newItem == null) {
            System.out.println("That item is not in your inventory!");
        }
        else {
            inventory.remove(index);
            currentRoom.setItem(new Item(item));
            System.out.println("Dropped:" + item);
        }
    }
    private boolean getItem(Command command) 
    {   
       
        
        
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to pick up...
            System.out.println("Get what?");
            return false;
        }

        String item = command.getSecondWord();

        // Try to leave current room.
        Item newItem = currentRoom.getItem(item);

        if (newItem == null) {
            System.out.println("That item is not here!");
        }
        else {
            inventory.add(newItem);
            currentRoom.removeItem(item);
            System.out.println("Picked up:" + item);
            
            
     
        }
        
        if (item.equals("Computer")) {
            System.out.println("You log into the computer and find all the files exposing the secrets of the asylum and win the game");
            return true;
        }
        else {
            return false;
            
        
    }}


    
    
   private void printInventory() {
        String output= "";
        for (int i = 0; i < inventory.size(); i++) {
            output += inventory.get(i).getDescription() + " ";
        }
        System.out.println("You are carrying:");
        System.out.println(output); 
       
            
    }
    private void inventory() {
       
    }
            
        
        
      
        
    
    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are an investigative reporter.");
        System.out.println("and your job is to uncover the dark secrets within the asylum.");
        System.out.println("You arrive at the asylum and take a glance at the big gate outside the asylum.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
        

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        
        else{
            /*
             * setting the limit to number of moves the player can make before the game ends
             */
            if (count==20) {
            System.out.println("Game Over");
            System.exit(0);
    }
        else 
        {
            currentRoom = nextRoom;
            
            System.out.println(currentRoom.getLongDescription()+ ".\n" + count); 
            count++;
        }
    }}
         
                
                    
            
                
        
       
            

    




    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            
            return true;  // signal that we want to quit
        }
    }
}
