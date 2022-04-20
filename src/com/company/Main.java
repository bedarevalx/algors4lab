package com.company;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
public class Main {
    static double[][] GetRandomSymMatr(int n){
        double[][] symMatr = new double[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                symMatr[i][j] = (Math.random()*(10)) + 0;
                symMatr[j][i] = symMatr[i][j];
            }
        }
        return symMatr;
    }
    static double[][] GetCustomMatrix(int size,boolean isIdentity, boolean isHilbert){
        double[][] customMatrix = new double[size][size];
        if(isHilbert){
            for(int i=0;i<size;i++){
                for(int j=0;j<size;j++){
                    customMatrix[i][j] = (double) 1 / (j + i + 1);
                }
            }
            return customMatrix;
        }
        for(int i=0;i<size;i++){
            for(int j=0; j<size;j++){
                if(i==j && isIdentity)
                    customMatrix[i][j] = 1;
                else
                    customMatrix[i][j] = 0;
            }
        }
        return customMatrix;
    }
    static double[][] MultiplySquareMatrix(double[][] matrA, double[][] matrB){
        int size = matrA.length;
        double[][] matrC = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    matrC[i][j] += matrA[i][k] * matrB[k][j];
                }
            }
        }
          return matrC;
    }
    static double[][] TransposeMatrix(double[][] matrix){
        double[][] transposeMatrix = new double[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix.length; j++)
                transposeMatrix[j][i] = matrix[i][j];
        return transposeMatrix;
    }
    static double[][] RotationMethodYakobi(double[][] matrK, double[][] solutions,double epsilont){
        int counter=0;
        double[][] bufSoltuions = solutions.clone();
        while (true) {
            int iMax = 0, jMax = 0;
            double angleFi = 0;
            double[][] rotationMatrix = GetCustomMatrix(matrK.length,true,false);
            double maxElement = Math.abs(matrK[0][1]);
            //находим макс элемент выше диагонали
            for (int i = 0; i < matrK.length; i++) {
                for (int j = i + 1; j < matrK.length; j++) {
                    if (Math.abs(matrK[i][j]) < Math.abs(maxElement)) {
                        continue;
                    }
                        maxElement = matrK[i][j];
                        iMax = i;
                        jMax = j;
                }
            }
            //находим угол вращения
            angleFi = 0.5 * Math.atan((2 * matrK[iMax][jMax]) / (matrK[iMax][iMax] - matrK[jMax][jMax]));
            rotationMatrix[iMax][iMax] = Math.cos(angleFi);
            rotationMatrix[iMax][jMax] = -(Math.sin(angleFi));
            rotationMatrix[jMax][iMax] = Math.sin(angleFi);
            rotationMatrix[jMax][jMax] = Math.cos(angleFi);
            if (matrK[iMax][iMax] == matrK[jMax][jMax]) {
                angleFi = Math.sqrt(2) / 2;
                rotationMatrix[iMax][jMax] = -angleFi;
                rotationMatrix[jMax][iMax] = angleFi;
                rotationMatrix[iMax][iMax] = angleFi;
                rotationMatrix[jMax][jMax] = angleFi;
            }
            //умножаем матрицы
            double[][] matrK1;
            double[][] transposeRotationMatrix = TransposeMatrix(rotationMatrix);
            matrK1 = MultiplySquareMatrix(transposeRotationMatrix,matrK);
            matrK1 = MultiplySquareMatrix(matrK1, rotationMatrix);
            double[][] bufArray = GetCustomMatrix(matrK.length, false,false);
            for(int i=0;i<matrK1.length;i++){
                for(int j=0;j<matrK1.length;j++){
                    for(int m=0;m<matrK1.length;m++){
                        bufArray[i][j] += bufSoltuions[i][m] * rotationMatrix[m][j];
                    }
                }
            }
            bufSoltuions = bufArray.clone();
            double endIteration = 0;
            for (int i = 0; i < matrK1.length; i++) {
                for (int j = i + 1; j < matrK1.length; j++) {
                    endIteration += matrK1[i][j] * matrK1[i][j] ;
                }
            }
            endIteration = Math.sqrt(2 * endIteration);
            counter++;
            matrK=matrK1.clone();
            if (endIteration < epsilont) {
                System.out.println("Number of iterations - " + counter);
                for(int i=0;i<solutions.length;i++)
                    for(int j=0;j<solutions.length;j++)
                        solutions[i][j] = bufSoltuions[i][j];
                PrintMatrix(matrK);
                break;
            }
        }
        return matrK;
    }
    static void PrintMatrix(double[][] matrix){
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix.length;j++){
                System.out.printf("%.4f ",matrix[i][j]);
            }
            System.out.println("\n");
        }
    }
    public static void main(String[] args) {
//        double[][] matr = {
//                {6, 15, 55},
//                {15, 55, 225},
//                {55, 225, 979}
//        };
//        double[][] matrB = GetCustomMatrix(4, false, true);
//        PrintMatrix(matrB);
        double[][] matr = GetRandomSymMatr(4);
        double[][] sol = GetCustomMatrix(matr.length,true,false);
        Scanner in = new Scanner(System.in);
        System.out.print("Введите точность:");
        double epsilont = in.nextDouble();
        matr = RotationMethodYakobi(matr,sol,epsilont);
        ArrayList<Double> ownValues = new ArrayList();
        for(int i=0;i<matr.length;i++)
            ownValues.add(matr[i][i]);
        for(int i=0;i<matr.length;i++){
            System.out.println("Найдено собственное значение - " + ownValues.get(i));
            System.out.println("Найден собственный вектор:");
            System.out.print("[");
            for(int j=matr.length-1;j>=0;j--)
                System.out.print(sol[j][i] + ", ");
            System.out.print("]\n");
        }
        System.out.println("Число обусловленности - " + (double) Collections.max(ownValues) / Collections.min(ownValues));
    }
}
