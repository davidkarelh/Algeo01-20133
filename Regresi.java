import java.util.Scanner;

class Regresi{
    public static void main(String[] args){
        float total;
        Scanner scan = new Scanner(System.in); 
        Integer N = scan.nextInt();
        float[][] tabeldata = new float[N+1][N];
        float[][] tabelregresi = new float[N+1][N+2];
        /* Inisialisasi array */
        for (int i=0;i<N+1;i++){
            if (i==N){
                System.out.println("Masukkan nilai y:");
            }
            else{
                System.out.println("Masukkan nilai x"+(i+1)+":");
            }
            for (int j=0;j<N;j++){
                tabeldata[i][j] = scan.nextFloat();
            }
        }

        /* Regresi */
        for (int i=0;i<N+1;i++){
            /* Baris Pertama */
            if (i==0){
                for (int j=0;j<N+2;j++){
                    if (j==0){
                        tabelregresi[i][j] = N;
                    }
                    else{
                        for(int k=0;k<N+1;k++){
                            total = 0;
                            for(int l=0;l<N;l++){
                                total += tabeldata[k][l];
                            }
                            tabelregresi[i][j] = total;
                        }
                    }
                }
            }
            /* Baris bukan pertama */
            else{
                for (int j=0;j<N+2;j++){
                    if (j==0){
                        tabelregresi[i][j]=tabelregresi[j][i];
                    }
                    else{
                        for(int k=0;k<N+1;k++){
                            total =0;
                            for (int l=0;l<N;l++){
                                total += tabeldata[j-1][l]*tabeldata[k][l];
                            }
                            tabelregresi[i][j] = total;
                        }
                    }
                }
            }
        }

        scan.close();
    }
}
