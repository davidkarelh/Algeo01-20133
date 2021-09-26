import java.util.Scanner;

public class TubesDriver {
    public static void main(String[] args) {
        boolean exit = false;
        int aksi;
        Scanner input = new Scanner(System.in);
        do {
            System.out.println(
            """
            MENU UTAMA
            1. Sistem Persamaaan Linier
            2. Determinan
            3. Matriks balikan
            4. Interpolasi Polinom
            5. Regresi linier berganda
            6. Keluar
            """);
            System.out.print("Masukkan pilihan aksi yang mau dilakukan: ");
            aksi = input.nextInt();
            if (aksi == 1) {
                SPL.aksi(input);
            } else if (aksi == 2) {
                Determinan.aksi(input);
            } else if (aksi == 3) {
                MatriksBalikan.aksi(input);
            } else if (aksi == 4) {
                InterpolasiPolinom.aksi(input);
            } else if (aksi == 5) {
                Regresi.aksi(input);
            } else if (aksi == 6) {
                exit = true;
                System.out.println("Keluar dari program...");
            } else {
                System.out.println("Masukkan tidak valid, ulangi!");
            }
        } while(!exit);
        input.close();
    }
}

