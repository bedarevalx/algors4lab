package com.company;
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
    static double[][] GetIdentityMatrix(int size){
        double[][] identityMatrix = new double[size][size];
        for(int i=0;i<size;i++){
            for(int j=0; j<size;j++){
                if(i==j)
                    identityMatrix[i][j] = 1;
                else
                    identityMatrix[i][j] = 0;
            }
        }
        return identityMatrix;
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
    static void RotationMethodYakobi(double[][] matrK, double[][] solutions,double epsilont){
        int counter=0;
        while (true) {
            int iMax = 0, jMax = 0;
            double angleFi = 0;
            double[][] rotationMatrix = GetIdentityMatrix(matrK.length);
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
            rotationMatrix[iMax][jMax] = -Math.sin(angleFi);
            rotationMatrix[jMax][iMax] = Math.sin(angleFi);
            rotationMatrix[iMax][iMax] = Math.cos(angleFi);
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
            //TODO: умножение на солюшены
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
                break;
            }
        }

    }
    static void PrintMatrix(double[][] matrix){
        for(int i=0;i<matrix.length;i++){
            for(int j=0;j<matrix.length;j++){
                System.out.printf("%.1f ",matrix[i][j]);
            }
            System.out.println("\n");
        }
    }
    public static void main(String[] args) {
        double[][] matrA = {
                {4, 1, -1},
                {1, 4, -1},
                {-1, -1, 4}
        };
        double[][] matr = GetRandomSymMatr(4);
        double[][] sol = new double[4][4];
        Scanner in = new Scanner(System.in);
        PrintMatrix(matr);
        System.out.print("Введите точность:");
        double epsilont = in.nextDouble();
        RotationMethodYakobi(matr,sol,epsilont);
    }
}
