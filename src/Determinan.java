import java.util.Scanner;
public class Determinan {
    public static Matriks inputDeterminanManual(Scanner input) {
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
            1. Reduksi Baris
            2. Ekspansi Kofaktor
            3. Kembali
            """);
            System.out.print("Pilih metode yang mau digunakan: ");
            aksi = input.nextInt();
            if (aksi == 1) {
                System.out.println(String.format("\nDeterminan matriks yang dimasukkan = %f\n", inputDeterminanManual(input).getDeterminanReduksiBaris()));
                exit = true;
            } else if (aksi == 2) {
                System.out.println(String.format("\nDeterminan matriks yang dimasukkan = %f\n", inputDeterminanManual(input).getDeterminanKofaktor()));
                exit = true;
            } else if (aksi == 3) {
                exit = true;
                System.out.println();
            } else {
                System.out.println("\nMasukkan tidak valid, ulangi!\n");
            }
        } while(!exit);
        System.out.print("Kembali ke menu utama? Masukkan apapun untuk kembali ke menu utama: ");
        input.next();
        System.out.println();
    }
}
