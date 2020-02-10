/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sihomework2;

import Jama.Matrix;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author cool
 */
public class dimRedGrowth {

    /**
     * REad file into matrix *apply pca2 *no need to write it back... jus note
     * running time
     */
    static Matrix origData;
    static Matrix TransformedData;
    static int origDim;
    static String COMMA_DELIMITER = ",";

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("Give path to data directory");
        Scanner sc = new Scanner(System.in);
        String cwd = sc.nextLine();
        System.out.println("name of dataset...format .csv");
        String Dsname = sc.next();
        String pathToDataSetFile = cwd + "\\" + Dsname + ".csv";
        origDim = sc.nextInt();
        System.out.println("original dimensions... ??????");
        loadData(pathToDataSetFile);
        origData = new Matrix(264016, origDim); /*
         * statically assigned no of points.. can be modoified :)
         */

        int RedDim = 3;
        System.out.println("--------Method of dimensionality Reduction----------\n\t1. pca\n\t2. FastMap");
        int method = sc.nextInt();
        long lStartTime = new Date().getTime();
        if(method==1)
        {
            pca2 pcaobj=new pca2();
            TransformedData = pcaobj.getTranformedDatabyPCA(origData, 264016, origDim, RedDim);
        }
        else
        {
        //fastmap();
        }
        long lEndTime = new Date().getTime();
        long difference = lEndTime - lStartTime;
        System.out.println("Elapsed milliseconds: " + difference);
        
    }

    public static void loadData(String path) throws FileNotFoundException, IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(path));
        String line;int lineNo=0;
        while ((line = fileReader.readLine()) != null) {
            String[] tokens = line.split(COMMA_DELIMITER);
            //int i=0;
            if (tokens.length > 0) {
               for(int i=0;i<tokens.length;i++){
                origData.set(lineNo, i, Double.parseDouble(tokens[i]));
            }
             lineNo++;
            }
        }

    }
}

