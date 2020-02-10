/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sihomework2;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 *
 * @author cool
 */
public class pca {

    /*
     * public Matrix getTranformedDatabyFastMap(Matrix MOrigData,int NoData,int
     * origDim,int RedDim){ Matrix tdata = null;
     *
     *
     * return tdata; }
     */
    Matrix getTranformedDatabyPCA(Matrix MOrigData, int NoData, int origDim, int RedDim) {

        Matrix X = Standardize(NoData, origDim, MOrigData);
        // use Jama matrix class  
        // Matrix X = new Matrix(indatstd);

        // Sums of squares and cross-products matrix
        Matrix Xprime = X.transpose();
        Matrix SSCP = Xprime.times(X);
        // Eigen decomposition
        EigenvalueDecomposition evaldec = SSCP.eig();
        Matrix evecs = evaldec.getV();
        double[] evals = evaldec.getRealEigenvalues();
        double tot = 0.0;
        for (int j = 0; j < evals.length; j++) {
            tot += evals[j];
        }

        // reverse order of evals into Evals
        double[] Evals = new double[origDim];
        for (int j = 0; j < origDim; j++) {
            Evals[j] = evals[origDim - j - 1];
        }

        double[][] tempold = evecs.getArray();
        double[][] tempnew = new double[origDim][origDim];
        for (int j1 = 0; j1 < origDim; j1++) {
            for (int j2 = 0; j2 < origDim; j2++) {
                tempnew[j1][j2] = tempold[j1][origDim - j2 - 1];
            }
        }
        Matrix Evecs = new Matrix(tempnew);
        Matrix rowproj = X.times(Evecs);
        return rowproj;
    }

    public Matrix Standardize(int nrow, int ncol, Matrix MOrigData) {
        double[] colmeans = new double[ncol];
        double[] colstdevs = new double[ncol];
        // Adat will contain the standardized data and will be returned
        // double[][] Adat = new double[nrow][ncol];
        Matrix Adat = new Matrix(nrow, ncol);
        double[] tempcol = new double[nrow];
        double tot;

        // Determine means and standard deviations of variables/columns
        for (int j = 0; j < ncol; j++) {
            tot = 0.0;
            for (int i = 0; i < nrow; i++) {
                tempcol[i] = MOrigData.get(i, j)/*
                         * A[i][j]
                         */;
                tot += tempcol[i];
            }

            // For this col, det mean
            colmeans[j] = tot / (double) nrow;
            for (int i = 0; i < nrow; i++) {
                colstdevs[j] += Math.pow(tempcol[i] - colmeans[j], 2.0);
            }
            colstdevs[j] = Math.sqrt(colstdevs[j] / ((double) nrow));
            if (colstdevs[j] < 0.0001) {
                colstdevs[j] = 1.0;
            }
        }


        // Now ceter to zero mean, and reduce to unit standard deviation
        for (int j = 0; j < ncol; j++) {
            for (int i = 0; i < nrow; i++) {
                Adat.set(i, j, ((MOrigData.get(i, j) - colmeans[j]) / (Math.sqrt((double) nrow) * colstdevs[j])));
            }
        }
        return Adat;
    } // Standardize
}
