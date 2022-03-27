
/**
 * Write a description of class Item here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Item
{
    
    String  description;
    int size;
    //constructor
    public Item(String newdescription) {
        description=newdescription;
        
    }
    public String getDescription()
    {
    return description;
}
public Item(int newsize){
    size=newsize;
}
public int newSize()
{
    return size;
}


    
}
