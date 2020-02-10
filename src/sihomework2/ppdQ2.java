/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sihomework2;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author cool
 */
public class ppdQ2 {
 
    public static void main(String[] args) throws FileNotFoundException, IOException{
    HashMap<Integer,Integer> MapAtrributes=new HashMap<Integer,Integer>();
    ArrayList<ArrayList<dp>> Ldata=new ArrayList<ArrayList<dp>>();
    DataInputStream input = new DataInputStream(new FileInputStream("D:\\my courses\\searching n indexing\\2016\\hw2\\ChemicalFingerprint\\indexdata\\indexdata"));
    int newAtrr=-1;
    int c=0;    
    while (input.available() > 0&&c++<100) {
        
            ArrayList<dp> data=new ArrayList<dp>();
            String x = input.readLine();
            x=x.replace("{","").replace("}", "").replace(":", "").replace(",", "");
           // System.out.println(x);
            String[] tok=x.split(" ");
            for(int i=0;i<tok.length;i++)
            {
            if((i%2)==0)//looking for even indices for attributes
                {
                if(!MapAtrributes.containsKey(Integer.parseInt(tok[i])))
                    MapAtrributes.put(Integer.parseInt(tok[i]),++newAtrr);   
                }
            dp d1=new dp();
            d1.att=MapAtrributes.get(Integer.parseInt(tok[i]));
            d1.val=(Integer.parseInt(tok[i+1]));
            data.add(d1);
            i++;
            }
            Ldata.add(data);
    }
        input.close();
        //printing corresponding attributes
        for(int i=0;i<MapAtrributes.size();i++)
        {
            if(MapAtrributes.containsValue(i))
             System.out.println("yes");
        }
        System.out.println(MapAtrributes.toString());
}
}
