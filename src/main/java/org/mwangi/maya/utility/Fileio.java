package org.mwangi.maya.utility;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Fileio {
    public  static  void saveATResponses(String response){
   try(FileOutputStream out=new FileOutputStream("ATResponses.txt",true)){
          out.write(response.getBytes());
          out.write("\n".getBytes());
   } catch (FileNotFoundException e) {
       throw new RuntimeException(e);
   } catch (IOException e) {
       throw new RuntimeException(e);
   }
    }

}
