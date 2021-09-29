import java.util.Scanner;

public class InterpolasiPolinom {
    public static Matriks inputIPmanual (Scanner input) {
        System.out.println("Masukkan n (banyaknya titik): ");
        int n = input.nextInt();
    
        double[][] konten = new double[n][n + 1];

    
        for (int i = 0; i < n; i++) {
            double a = 1;
            System.out.println(String.format("Masukkan titik ke-%d sejumlah %d: ", i + 1, n));
            double x = input.nextDouble();
            double y = input.nextDouble();
            for (int j = 0; j < n; j++) {
                konten[i][j] = a;
                a *= x;
            }
            konten[i][n] = y;
        }
        return new Matriks(konten);
    }



    private static void IPSolver(Matriks m, Scanner input) {
        Matriks mr = Matriks.reduksiBaris(m, true);
        int jumlahVariabel = mr.getKolom() - 1;
        double[] hasil = new double[jumlahVariabel];
        for (int k = jumlahVariabel - 1; k >= 0; k--) {
            hasil[k] = mr.getElement(k, mr.getIdxKolomTerakhir());
            for (int j = hasil.length - 1; j > k; j--) {
                hasil[k] -= hasil[j] * mr.getElement(k, j);
            }
        }

        System.out.printf("%s", String.valueOf(hasil[0]));
        for (int i = 1; i < hasil.length; i++) {
            System.out.printf(" + %sx^%d", String.valueOf(hasil[i]), i);
        }
        System.out.printf("%n");

        double x = input.nextInt();
        double y = input.nextDouble();
        
        double xm = 1;
        y = 0;

        for (int i = 0; i < hasil.length; i++) {
            y += hasil[i] * xm;
            xm *= x;
        }
        System.out.printf("p%d(%s) = %s%n", mr.getBaris(), String.valueOf(x), String.valueOf(y));
    }

    public static void aksi(Scanner input) {
        InterpolasiPolinom.IPSolver(InterpolasiPolinom.inputIPmanual(input), input);
    }
}