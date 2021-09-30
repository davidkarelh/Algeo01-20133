import java.util.Scanner;

class Regresi{
    public static Matriks inputMatriksData(Scanner input){
        System.out.println("Masukkan jumlah peubah x:");
        Integer N = input.nextInt();
        System.out.println("Masukkan jumlah sampel:");
        Integer M = input.nextInt();

        double[][] tabeldata = new double[M][N+1];

        /* Inisialisasi array */
        System.out.println("Format pengisian: x1,x2,..,xn,y");
        for (int i=0;i<M;i++){
            System.out.println("Masukkan sampel ke-"+(i+1)+":");
            for (int j=0;j<N+1;j++){
                tabeldata[i][j] = scan.nextDouble();
            }
        }
        return new Matriks(tabeldata);
    }

    public static void aksi(Scanner input){
        /* Regresi */
        double total;
        Matriks data = inputMatriksData(input);
        double[][] tabelregresi = new double[data.getBaris()][data.getBaris() + 1];
        Matriks regresi = new Matriks(tabelregresi);
        for (int i = 0; i < regresi.getBaris(); i++){
            /* Baris Pertama */
            if (i==0){
                for (int j = 0; j < regresi.getKolom(); j++){
                    if (j == 0){
                        regresi.setElement(i, j, data.getKolom());
                        // regresi.getElement(i,j) = data.getKolom();
                    }
                    else{
                        total = 0;
                        for(int l = 0; l < data.getKolom(); l++){
                            total += data.getElement(l,j-1);
                        }
                        regresi.setElement(i, j, total);
                        // regresi.getElement(i,j) = total;                        
                    }
                }
            }
            /* Baris bukan pertama */
            else{
                for (int j = 0; j < regresi.getKolom(); j++){
                    if (j == 0){
                        regresi.setElement(i, j, regresi.getElement(j, i));
                        // regresi.setElement(i,j)=regresi.getElement(j,i);
                    }
                    else{
                        total = 0;
                        for (int l = 0; l < data.getKolom(); l++){
                            total += data.getElement(l, i-1) * data.getElement(l, j-1);
                            // total += tabeldata[i-1][l]*tabeldata[j-1][l];
                        }
                        regresi.setElement(i, j, total);
                        // regresi.getElement(i,j) = total;                        
                    }
                }
            }
        }
        SPL.solveWithGaussJordan(regresi);
        for (int i = 0; i < regresi.getBaris(); i++){
            System.out.println(String.format("B%d", i));
            // System.out.print("B%d: ",i);
            System.out.println(regresi.getElement(i,regresi.getIdxKolomTerakhir()));
        }
    }
}
