/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sihomework2;

import Jama.Matrix;
import java.io.*;
import java.util.*;

/**
 *
 * @author cool
 */
public class driver {
    /*
     * take input and output file path from console
     */

    static ArrayList<ArrayList<Double>> OrigData = new ArrayList<>();
    static Matrix TransformedData;
    static Matrix MOrigData;
    static int NoData;
    static int origDim;
    static int RedDim;
    static Matrix DistanceMatrix_OrigSpace;
    static Matrix DistanceMatrix_TransformedSpace;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        double distortion, stress;
        System.out.println("Give path to data directory");
        Scanner sc = new Scanner(System.in);
        String cwd = sc.nextLine();
        System.out.println("name of dataset...format .dat");
        String Dsname = sc.next();
        String pathToDataSetFile = cwd + "\\" + Dsname + ".dat";
        loadData(pathToDataSetFile);
        //  System.out.println("DataLoaded--------reduce to how many dimensions...");
        //  RedDim = sc.nextInt();
        System.out.println("--------Method of dimensionality Reduction----------\n\t1. pca\n\t2. FastMap");
        int method = sc.nextInt();
        //Display();

        double[][] m = new double[NoData][origDim];
        for (int i = 0; i < NoData; i++) {
            for (int j = 0; j < origDim; j++) {
                m[i][j] = OrigData.get(i).get(j);
            }
        }
        MOrigData = new Matrix(m);
        int nIter = 2;

        pca pcaobj = new pca();
        fastmap fastmapobj = new fastmap();
        if (method == 1) {
            TransformedData = pcaobj.getTranformedDatabyPCA(MOrigData, NoData, origDim, RedDim);
            /*
             * write data in csvfile also for later use PrintWriter writer = new
             * PrintWriter(cwd + "\\redPca"+RedDim+".csv", "UTF-8");
             * TransformedData.print(writer, NoData, RedDim);
             *
             *
             * FileWriter tdata2Writer = new FileWriter(cwd + "//redPca" +
             * RedDim + ".csv"); for (int i = 0; i < NoData; i++) { for (int j =
             * 0; j < RedDim; j++) {
             * tdata2Writer.append(String.valueOf(TransformedData.get(i, j)));
             * tdata2Writer.append(","); }
             * tdata2Writer.append(System.lineSeparator());
            }
             */

        } else if (method == 2) {
            TransformedData = fastmapobj.getTranformedDatabyFastMap(MOrigData, NoData, origDim, RedDim);
            /*
             * write data in csvfile also for later use PrintWriter writer = new
             * PrintWriter(cwd + "\\redFm"+RedDim+".csv", "UTF-8");
             * TransformedData.print(writer, NoData, RedDim);
             *
             * FileWriter tdata2Writer = new FileWriter(cwd + "//redFm" + RedDim
             * + ".csv"); for (int i = 0; i < NoData; i++) { for (int j = 0; j <
             * RedDim; j++) {
             * tdata2Writer.append(String.valueOf(TransformedData.get(i, j)));
             * tdata2Writer.append(","); }
             * tdata2Writer.append(System.lineSeparator());
             *
             *
             * }
             */
        }

        DistanceMatrix_OrigSpace = ComputeDistanceMatrix(MOrigData, NoData, origDim);
        while (nIter < 6) { //for all values of k
            FileWriter tdata2Writer;
            RedDim = (int) Math.pow(2, nIter);
            if (method == 1) {
                tdata2Writer = new FileWriter(cwd + "//redPca" + RedDim + ".csv");
            } else {
                tdata2Writer = new FileWriter(cwd + "//redFm" + RedDim + ".csv");
            }
            for (int i = 0; i < NoData; i++) {
                for (int j = 0; j < RedDim; j++) {
                    tdata2Writer.append(String.valueOf(TransformedData.get(i, j)));
                    tdata2Writer.append(",");
                }
                tdata2Writer.append(System.lineSeparator());
            }
            tdata2Writer.close();
            DistanceMatrix_TransformedSpace = ComputeDistanceMatrix(TransformedData, NoData, RedDim);

            distortion = ComputeDistortion();/*
             * for 50000 samples only
             */
            stress = ComputeStress();/*
             * for 50000 samples only
             */
            System.out.println("In " + RedDim + "distortion = " + distortion);
            System.out.println("In " + RedDim + "stress = " + stress);

            int avgSet = SolveForPartC();
            System.out.println("In " + RedDim + "avgset = " + avgSet);
            nIter++;
        }
    }

    public static void loadData(String pathToDataFile) throws FileNotFoundException, IOException {
        DataInputStream input = new DataInputStream(new FileInputStream(pathToDataFile));
        while (input.available() > 0) {
            String x = input.readLine();
            //System.out.println(x);
            String[] tokens = x.split(" ");
            ArrayList<Double> v = new ArrayList();
            for (int i = 0; i < tokens.length - 1; i++) {//eliminating extra space
                v.add(Double.parseDouble(tokens[i]));
            }
            OrigData.add(v);
        }
        NoData = OrigData.size();
        origDim = OrigData.get(0).size();

    }

    public static void Display() {
        System.out.println(OrigData.size());
        for (int i = 0; i < OrigData.size(); i++) {
            System.out.println(OrigData.get(i).size());
            for (int j = 0; j < OrigData.get(i).size(); j++) {

                System.out.print(OrigData.get(i).get(j) + " ");
            }
            System.out.println();
        }

    }

    public static Matrix ComputeDistanceMatrix(Matrix Data, int NoData, int Dim) {
        Matrix DistanceMatrix = new Matrix(NoData, NoData);
        for (int i = 0; i < NoData; i++) {
            for (int j = 0; j <= i; j++) {
                if (j == i) {
                    DistanceMatrix.set(i, j, 0);
                } else {
                    double delS = 0;
                    //Euclideon dist()
                    for (int d = 0; d < Dim; d++) {
                        delS = delS + Math.pow((Data.get(i, d) - Data.get(j, d)), 2);
                    }
                    delS = Math.sqrt(delS);
                    DistanceMatrix.set(i, j, delS);
                    DistanceMatrix.set(j, i, delS);
                }
            }
        }
        return DistanceMatrix;
    }

    public static double ComputeDistortion() {
        double distort, c1 = 0, c2 = 0;
        for (int i = 0; i < NoData; i++) {
            for (int j = 0; j < NoData; j++) {
                if (i != j) // avoiding 0/0 
                //distort=distort+(Math.max(DistanceMatrix_OrigSpace.get(i, j)/DistanceMatrix_TransformedSpace.get(i, j), 1)*Math.max(DistanceMatrix_TransformedSpace.get(i, j)/DistanceMatrix_OrigSpace.get(i, j),1))   
                {
                    if (c1 < DistanceMatrix_OrigSpace.get(i, j) / DistanceMatrix_TransformedSpace.get(i, j)) {
                        c1 = DistanceMatrix_OrigSpace.get(i, j) / DistanceMatrix_TransformedSpace.get(i, j);
                    }
                    if (c2 < DistanceMatrix_TransformedSpace.get(i, j) / DistanceMatrix_OrigSpace.get(i, j)) {
                        c2 = DistanceMatrix_TransformedSpace.get(i, j) / DistanceMatrix_OrigSpace.get(i, j);
                    }
                }
            }
        }

        distort = c1 * c2;
        return distort;
    }

    public static double ComputeStress() {
        double stress, nr = 0, dr = 0;
        for (int i = 0; i < NoData; i++) {
            for (int j = 0; j < NoData; j++) {
                if (i != j) // avoiding 0/0 
                {
                    nr = nr + Math.pow((DistanceMatrix_TransformedSpace.get(i, j) - DistanceMatrix_OrigSpace.get(i, j)), 2);
                    dr = dr + Math.pow(DistanceMatrix_OrigSpace.get(i, j), 2);
                }
            }
        }
        stress = nr / dr;
        stress = Math.sqrt(stress);
        return stress;
    }

    public static int SolveForPartC() {
        int avgset = 0;
        /*
         * u have both distance matrices in hand..just find 10th nn in
         * origmatrix..it will require u to sort the data .. now get the
         * distance 'd' of that 10th n in redmatrix and find the set having all
         * points having dist less than d
         */
        /*
         * start with getting random indices .. lower bound=0 .. upper
         * bound=NoData
         */
        //int[] ArrayRandomIndices=new int[100];
        int randomIndex;
        int sampleSize = 20; //random query points
        Random randomGenerator = new Random();

        ArrayList<Neighbor> ListNeighbors;
        for (int randomIndices = 0; randomIndices < sampleSize; randomIndices++) {
            randomIndex = randomGenerator.nextInt(NoData - 1);

            //find 10 nn in orig space
            ListNeighbors = new ArrayList<>();
            for (int j = 0; j < NoData; j++) {
                if (j != randomIndex) {
                    Neighbor n = new Neighbor();
                    n.index = j;
                    n.distance = DistanceMatrix_OrigSpace.get(randomIndex, j);
                    ListNeighbors.add(n);
                }
            }
            Collections.sort(ListNeighbors, new Comparator<Neighbor>() {

                @Override
                public int compare(Neighbor n1, Neighbor n2) {
                    return (int) Math.signum(-n2.getDist() + n1.getDist());
                }
            });

            //top 10..get dist of 10th n
            double dist_ten = ListNeighbors.get(9).distance;

            //find no of nns in red space 
            int nSet = 0;
            for (int j = 0; j < NoData; j++) {
                if (DistanceMatrix_TransformedSpace.get(randomIndex, j) <= dist_ten) {
                    nSet++;
                }
            }
            avgset = avgset + nSet;
        }

        avgset = avgset / sampleSize;
        return avgset;
    }
}
