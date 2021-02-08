/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nomnom;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author akkuw
 */
public class resourceManager {
    
    public static void save (Serializable data,String fileName) throws Exception{
        try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(fileName)))) {
            oos.writeObject(data);
        }
    }
    
    public static Object load (String fileName) throws Exception{
        try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(fileName)))) {
           return ois.readObject(); 
        }
        
    }
    
}


