import java.util.Scanner;

public class MatriksBalikan {
    public static boolean adaDeterminan(Matriks m) {
        boolean ada = false;
        double[][] konten = new double[m.getBaris()][2 * m.getKolom()];
        for (int i = 0; i < m.getBaris(); i++) {
            for (int j = 0; j < m.getKolom(); j++) {
                konten[i][j] = m.getElement(i, j);
            }
        }
        for (int i = 0; i < m.getBaris(); i++) {
            for (int j = m.getKolom(); j < 2 * m.getKolom(); j++) {
                if (i == j - m.getKolom()) {
                    konten[i][j] = 1;
                } else {
                    konten[i][j] = 0;
                }
            }
        }
        
        Matriks matriks = Matriks.eselonTereduksi(new Matriks(konten));
        int j = 0;
        while (!ada && j < m.getKolom()) {
            if (matriks.getElement(m.getIdxBarisTerakhir(), j) != 0) {
                ada = true;
            }
            j++;
        }
        return ada;
    }

    public static Matriks inputMatriksBalikanManual(Scanner input) {
        System.out.print("Masukkan n (ukuran baris dan kolom matriks): ");
        int n = input.nextInt();
        double[][] konten = new double[n][n];
        
        for (int i = 0; i < n; i++) {
            System.out.print(String.format("Masukkan isi matriks baris ke-%d sejumlah %d: ", i + 1, n));
            for (int j = 0; j < n; j++) {
                konten[i][j] = input.nextDouble();
            }
        }
        return new Matriks(konten);
    }

    public static void aksi(Scanner input) {
        boolean exit = false;
        int aksi;
        do {
            System.out.println(
            """
            PILIHAN METODE
            1. Eliminasi Gauss-Jordan
            2. ---
            """);
            System.out.print("Pilih metode yang mau digunakan: ");
            aksi = input.nextInt();
            if (aksi == 1) {
                Matriks m = inputMatriksBalikanManual(input);
                if (adaDeterminan(m)) {
                    System.out.println(m.inversWithGaussJordan().getString());
                } else {
                    System.out.println("Matriks tidak mempunyai balikan");
                }
                exit = true;
            } else if (aksi == 2) {
                // System.out.println(String.format("\nDeterminan matriks yang dimasukkan = %f\n", inputDeterminanManual(input).getDeterminanKofaktor()));
                exit = true;
            } else {
                System.out.println("\nMasukkan tidak valid, ulangi!\n");
            }
        } while(!exit);
    }
}
