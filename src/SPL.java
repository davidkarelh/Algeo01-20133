import java.util.Scanner;
public class SPL {
    public static Matriks inputSPLManual(Scanner input) {
        System.out.print("Masukkan m (jumlah persamaan): ");
        int m = input.nextInt();
        System.out.print("Masukkan n (jumlah variabel): ");
        int n = input.nextInt();
        double[][] konten = new double[m][n + 1];
        
        for (int i = 0; i < m; i++) {
            System.out.print(String.format("Masukkan koefisien variabel persamaan ke-%d sejumlah %d: ", i + 1, n));
            for (int j = 0; j < n; j++) {
                konten[i][j] = input.nextDouble();
            }
        }
        System.out.print(String.format("Masukkan b[i] (sisi kanan persamaan) sejumlah %d: ", m));
        for (int i = 0; i < m; i++) {
            konten[i][n] = input.nextDouble();
        }
        return new Matriks(konten);
    }

    public static void solveWithGauss(Matriks m) {
        Matriks matriks = Matriks.reduksiBaris(m, true);
        for (int i = 0; i < matriks.getBaris(); i++) {
            for (int j = 0; j < matriks.getKolom(); j++) {
                System.out.print(matriks.getElement(i, j)+ "\t");;
            }
            System.out.println();
        }
        if (hasManySolution(matriks)) {
            System.out.println("banyak");
        } else if (hasNoSolution(matriks)) {
            System.out.println("Sistem Persamaan Linear tidak mempunyai solusi.");
        } else {
            singleEselonSolver(matriks);
        }
    }

    public static void solveWithGaussJordan(Matriks m) {
        Matriks matriks = Matriks.eselonTereduksi(m);
        for (int i = 0; i < matriks.getBaris(); i++) {
            for (int j = 0; j < matriks.getKolom(); j++) {
                System.out.print(matriks.getElement(i, j)+ "\t");;
            }
            System.out.println();
        }
        if (hasManySolution(matriks)) {
            System.out.println("banyak");
        } else if (hasNoSolution(matriks)) {
            System.out.println("Sistem Persamaan Linear tidak mempunyai solusi.");
        } else {
            singleEselonSolver(matriks);
        }
    }

    private static boolean hasManySolution(Matriks m) {
        boolean many = true;
        int j = 0;
        while (many && j < m.getKolom()) {
            if (m.getElement(m.getIdxBarisTerakhir(), j) != 0) {
                many = false;
            }
            j++;
        }
        return many;
    } 

    private static boolean hasNoSolution(Matriks m) {
        boolean noSolution = true;
        int j = 0;
        if (m.getElement(m.getIdxBarisTerakhir(), m.getIdxKolomTerakhir()) == 0) {
            noSolution = false;
        }
        while (noSolution && j < m.getKolom() - 1) {
            if (m.getElement(m.getIdxBarisTerakhir(), j) != 0) {
                noSolution = false;
            }
            j++;
        }
        return noSolution;
    } 

    private static void singleEselonSolver(Matriks m) {
        int jumlahVariabel = m.getKolom() - 1;
        double[] hasil = new double[jumlahVariabel];
        for (int k = jumlahVariabel - 1; k >= 0; k--) {
            hasil[k] = m.getElement(k, m.getIdxKolomTerakhir());
            for (int j = hasil.length - 1; j > k; j--) {
                hasil[k] -= hasil[j] * m.getElement(k, j);
            }
        }
        for (int i = 0; i < hasil.length; i++) {
            System.out.println(String.format("Variabel %d = %f", i + 1, hasil[i]));
        }
    }

    public static void solveWithInversMatriks(Matriks m) {
        double[][] konten1 = new double[m.getBaris()][m.getKolom() - 1];
        double[][] konten2 = new double[m.getBaris()][1];
        for (int i = 0; i < m.getBaris(); i++) {
            for (int j = 0; j < m.getKolom() - 1; j++) {
                konten1[i][j] = m.getElement(i, j);
            }
        }
        for (int i = 0; i < m.getBaris(); i++) {
            konten2[i][0] = m.getElement(i, m.getIdxKolomTerakhir());
        }
        Matriks matriks = Matriks.multiply(new Matriks(konten1).inversWithGaussJordan(), new Matriks(konten2));
        for (int i = 0; i < matriks.getBaris(); i++) {
            System.out.println(String.format("Variabel %d = %f", i + 1, matriks.getElement(i, 0)));
        }
    }

    public static void aksi(Scanner input) {
        boolean valid = false;
        int aksi;
        do {
            System.out.println(
            """
            PILIHAN METODE
            1. Metode eliminasi Gauss
            2. Metode eliminasi Gauss-Jordan
            3. Metode matriks balikan
            4. Kaidah Cramer
            """);
            System.out.print("Pilih metode yang mau digunakan: ");
            aksi = input.nextInt();
            if (aksi == 1) {
                SPL.solveWithGauss(SPL.inputSPLManual(input));
                valid = true;
            } else if (aksi == 2) {
                SPL.solveWithGaussJordan(SPL.inputSPLManual(input));
                valid = true;
            } else if (aksi == 3) {
                solveWithInversMatriks(inputSPLManual(input));
            } else if (aksi == 4) {

            } else {

            }
        } while(!valid);
    }
}
