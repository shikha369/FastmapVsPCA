/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sihomework2;

import Jama.Matrix;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author cool
 */
public class PreprocessDataQ2 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        HashMap<Integer, Integer> MapAtrributes = new HashMap<Integer, Integer>();
        ArrayList<ArrayList<dp>> Ldata = new ArrayList<ArrayList<dp>>();

        System.out.println("Give path to data directory");
        Scanner sc = new Scanner(System.in);
        String cwd = sc.nextLine();
        System.out.println("name of dataset...format .dat");
        String Dsname = sc.next();
        String path = cwd + "\\" + Dsname;
        DataInputStream input = new DataInputStream(new FileInputStream(path));
        int newAtrr = -1;
        int c = 0;
        while (input.available() > 0) {

            ArrayList<dp> data = new ArrayList<dp>();
            String x = input.readLine();
            x = x.replace("{", "").replace("}", "").replace(":", "").replace(",", "");
            // System.out.println(x);
            String[] tok = x.split(" ");
            for (int i = 0; i < tok.length; i++) {
                if ((i % 2) == 0)//looking for even indices for attributes
                {
                    if (!MapAtrributes.containsKey(Integer.parseInt(tok[i]))) {
                        MapAtrributes.put(Integer.parseInt(tok[i]), ++newAtrr);
                    }
                }
                dp d1 = new dp();
                d1.att = MapAtrributes.get(Integer.parseInt(tok[i]));
                d1.val = (Integer.parseInt(tok[i + 1]));
                data.add(d1);
                i++;
            }
            Ldata.add(data);
        }
        input.close();
        //printing corresponding attributes
      /*
         * for(int i=0;i<MapAtrributes.size();i++) {
         * if(MapAtrributes.containsValue(i)) System.out.println("yes"); }
         * System.out.println(MapAtrributes.toString());
         *
         */
        int att;
        Matrix pm = new Matrix(100, MapAtrributes.size());
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < MapAtrributes.size(); j++) {
                if (j < Ldata.get(i).size()) {
                    att = Ldata.get(i).get(j).att;
                    pm.set(i, att, Ldata.get(i).get(j).val);
                }

            }
        }
//System.out.println("done");
        //write data in a csv .... for all specified specified no of data in one go ;D
        int[] dim = new int[4];
        dim[0] = 5000;
        dim[1] = 10000;
        dim[2] = 100000;
        dim[3] = pm.getColumnDimension();
        for (int k = 0; k < 4; k++) {
            FileWriter tdata2Writer = new FileWriter(cwd + "\\" + dim[k] + "processedData.csv");
            for (int i = 0; i < pm.getRowDimension(); i++) {
                for (int j = 0; j < dim[k]; j++) {
                    tdata2Writer.append(String.valueOf(pm.get(i, j)));
                    tdata2Writer.append(",");
                }
                tdata2Writer.append(System.lineSeparator());
            }

            tdata2Writer.close();
        } /*to get running time for dim red..to k..change ur pca module a lil bit..pca2*/
    }
}