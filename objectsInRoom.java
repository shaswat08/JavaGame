 


/**
 *
 * @author Shaswat
 */
class objectsInRoom {
    String pickedObject;
   
   public void take (String objectName){
       pickedObject = objectName;
       System.out.println("The "+objectName+" is picked");
   }
   
   public void use(){
    if(pickedObject.equalsIgnoreCase("door 3 key")){
        System.out.println("Door 3 is unlocked now\nAndyou are in the library");
    }
    else{
        System.out.println("No suitable use of this object found yet");
        System.out.println("You are now stuck in this room with "+pickedObject);
    }
}          
}