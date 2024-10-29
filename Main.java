import java.io.FileWriter;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
public class Main {

    public static void main(String[] args) throws IOException {

        /*String inputFile;
        String outputFile;
        inputFile = args[0];
        outputFile = args[1];*/

        //we create our writer to write output file.
        FileWriter writer = new FileWriter("outputFile.txt");

        //creating AVLTree object.
        AVLTree obj = new AVLTree(writer);

        try {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
            int i=0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //these are all conitions:
                if (i==0){//for first line, we don't need to write anything. Just get the first line as rootNode.
                    String name=data.split(" ")[0];
                    String gmsString=data.split(" ")[1];
                    float gms=Float.parseFloat(gmsString);
                    obj.insertElement(gms,name);
                }

                else if(data.contains("MEMBER_IN")){
                    String name=data.split(" ")[1];
                    String gmsString=data.split(" ")[2];
                    float gms=Float.parseFloat(gmsString);
                    obj.insertElement(gms,name);
                }

                else if(data.contains("MEMBER_OUT")){
                    String name=data.split(" ")[1];
                    String gmsString=data.split(" ")[2];
                    float gms=Float.parseFloat(gmsString);
                    String x="";
                    obj.removeElement(gms);
                    if(obj.success!=null){
                        x=obj.success.name;
                        obj.success=null;
                    }
                    else {
                        x=obj.findNotSuccess();
                        obj.notSuccess=null;
                    }
                    //System.out.println(name+" left the family, replaced by "+x);
                    writer.write(name + " left the family, replaced by " + x + "\n");
                }

                else if(data.contains("INTEL_TARGET")){
                    String name1=data.split(" ")[1];
                    String name2=data.split(" ")[3];
                    String gmsString1=data.split(" ")[2];
                    String gmsString2=data.split(" ")[4];
                    float gms1=Float.parseFloat(gmsString1);
                    float gms2=Float.parseFloat(gmsString2);
                    //System.out.println("Target Analysis Result: "+obj.findCommonAncestor(gms1,gms2));
                    writer.write("Target Analysis Result: "+obj.findCommonAncestor(gms1,gms2)+"\n");
                }

                else if(data.contains("INTEL_DIVIDE")){
                    //System.out.println("Division Analysis Result: "+obj.divide());
                    writer.write("Division Analysis Result: "+obj.divide()+"\n");
                }

                else if(data.contains("INTEL_RANK")){
                    String name=data.split(" ")[1];
                    String gmsString=data.split(" ")[2];
                    float gms=Float.parseFloat(gmsString);
                    obj.findNode(gms,0);
                    //System.out.print("Rank Analysis Result:");
                    writer.write("Rank Analysis Result:");
                    obj.allSameRanks();
                    //System.out.println("");
                    writer.write("\n");

                }

                i+=1;//i is esssential for first line
            }
            myReader.close(); //we closed the reader.
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. File not found!!!");
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        writer.close();//we closed the file to saving.

    }
}
