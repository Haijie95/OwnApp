import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Cart {

    //command
    private String fruits;
    private String username;
    
    private CartDBinMemory db;
    private String baseFolder;
    private String filepath;
    

    //constructor
    public Cart(String fruits,String username){
        this.fruits=fruits;
        this.username=username;
        this.filepath="db/"+username+".txt";
        checkFile(this.filepath);
        this.baseFolder="db"; //make it default for now
        this.setup(); //setup the database, create if folder does not exist else continue
        //this.db=new CartDBinMemory(this.baseFolder); //startt cart in db memory, and when this starts 
    }
    public Cart(String username){
        this.username=username;
        this.filepath="db/"+username+".txt";
        checkFile(this.filepath);
        this.baseFolder="db"; //make it default for now
        this.setup(); //setup the database, create if folder does not exist else continue
        //this.db=new CartDBinMemory(this.baseFolder); //startt cart in db memory, and when this starts 
    }

    public void checkFile(String filepath){
        //Path Obj
        Path pth=Paths.get(filepath); //making a path from string
        File fobj=pth.toFile(); //pointing to the file from the path

        //File Obj
        try{
            if(fobj.exists()){ //check if the file exist
                System.out.println(("User Exists!"));
            } else{
                fobj.createNewFile();
                System.out.println("New User Created!");
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void writeFile(String filepath,String summary){
        try{
            FileWriter fw = new FileWriter(filepath, true);
            BufferedWriter bfw = new BufferedWriter(fw);
            bfw.write(summary);
            //bfw.newLine();

            bfw.flush(); //make sure changes are saved before closing
            bfw.close();
        }
        catch(Exception e) {
            System.out.println("Unable to write line: "+e);
        }
    }

    public String readFile(String filepath){
        Path pth=Paths.get(filepath); //making a path from string
        File fobj=pth.toFile(); //pointing to the file from the path
        String line;
        String summary="";
        try{
            FileReader fr = new FileReader(fobj);
            BufferedReader bdf = new BufferedReader(fr);
            
            while(null!=(line=bdf.readLine())){
                summary=summary+line+"\n";
            }
            // if((line=bdf.readLine())!=""){
            //     line="File empty nothing to read!";
            // }
            // else{
            //     line=line+line+"\n";
            // };
            // while((line=bdf.readLine())!=""){
            //     line=line+line+"\n";
            // }
            
            bdf.close();
        }
        catch (FileNotFoundException e){
            System.out.println("File Not Found. Please Check Input!");
        }
        catch (IOException e){
            System.out.println("Unable to read lines: "+e.getMessage());
        }
        return summary;
    }


    public String addAction(){
        String summary="Fruit added: ";
        String[] allFruits=this.fruits.trim().split(",");
        if (allFruits.length<=1){
            //this.db.userMap.get(this.username).add(allFruits[0].trim());
            summary=summary+allFruits[0]+"\n";
        } else{
            for(String item: allFruits){
                this.db.userMap.get(this.username).add(item.trim());
                summary=summary+item+" ";
            }
            summary=summary+"\n";
        }
        writeFile(this.filepath, summary);
        return summary;
    }

    public String listAction(){
        String summary="Past Actions\n";
        summary=summary+readFile(this.filepath);
        return summary;
        // for (String key: this.db.userMap.keySet()){
        //     System.out.println("->"+key);
        // }
    }

    public String deleteAction(){
        String summary="Fruit deleted: ";
        String[] allFruits=this.fruits.split(",");
        if (allFruits.length<=1){
            //this.db.userMap.get(this.username).remove(allFruits[0]);
            summary=summary+allFruits[0]+"\n";
        } else{
        for(Integer i=0;i<allFruits.length;i++){
            summary=summary+allFruits[i]+" ";
            //this.db.userMap.get(this.username).remove(item);
            }
        summary=summary+"\n";
        }
        return summary;
    }

    public void setup(){
        Path p =Paths.get(this.baseFolder);
        if (Files.isDirectory(p)){
            //skip if directory exist
        } else{
                try {
                    Files.createDirectory(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }

}
