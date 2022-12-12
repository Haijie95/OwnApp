import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ShoppingCartDB {
    
    private CartDBinMemory db;
    private String currentUser;
    private String baseFolder;

    public ShoppingCartDB(){
        this.baseFolder="db"; //by default
        this.setup();
        this.db=new CartDBinMemory(this.baseFolder);
    }
    public ShoppingCartDB(String baseFolder){
        this.baseFolder=baseFolder;
        this.setup();
        this.db=new CartDBinMemory(this.baseFolder);
    }

    public void setup(){
        Path p =Paths.get(this.baseFolder);
        if (Files.isDirectory(p)){
            //skip if directory exist
        } else{
            try{
                Files.createDirectory(p);
            }
            catch(IOException e){
                System.out.println("Error :"+e.getMessage());
            }
        }
    }
}
