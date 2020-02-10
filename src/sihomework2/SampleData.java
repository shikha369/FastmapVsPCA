/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sihomework2;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 *
 * @author cool
 */
public class SampleData {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        DataInputStream input = new DataInputStream(new FileInputStream(
                "D:/my courses/searching n indexing/2016/hw2/image_data.dat/image_data.dat"));
        DataOutputStream output = new DataOutputStream(new FileOutputStream("D:/my courses/searching n indexing/2016/hw2/sampleimg.dat"));
        
        
       
        ArrayList<Vector> list=new ArrayList<>();
        Vector v;
        int c=0;
        
        while (input.available() > 0 &&c<100) {
            String x = input.readLine();
            System.out.println(x);
            //String[] tokens=x.split(" ");
         
            //v=new Vector();
           // for(int i=0;i<tokens.length;i++){
        //v.add(tokens[i]);
             //   output.writeUTF(x);
               // output.writeUTF(x);//writing extra charact
                output.writeBytes(x);
                 output.writeUTF(System.lineSeparator());
            System.out.println(c+" data stored !!");
             c++;
                
        }
            //list.add(v);
           
        
       // */
       // System.out.println(c+" data stored !!"); only 33000 data points could b stored ... so creating a dummy data 
        
        
        input.close();
        output.close();
        
         DataInputStream ip = new DataInputStream(new FileInputStream("D:/my courses/searching n indexing/2016/hw2/sampleimg.dat"));
          while (ip.available() > 0 ) {
          System.out.println(ip.readLine());
          }
          }


                
        
    }

