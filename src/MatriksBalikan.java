import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class MatriksBalikan {

    public static Matriks inputMatriksBalikanFile(Scanner input) {
        System.out.print("Masukkan nama file yang berada di folder test: ");
        String chosenFile = input.next();
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path filePath = Paths.get(currentPath.toString(), "test", chosenFile);
        String barisFile;
        int i, j;
        ArrayList<ArrayList<Double>> kontenDinamis = new ArrayList<ArrayList<Double>>();
        try {
            File file = new File(filePath.toString());
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                barisFile = reader.nextLine();
                ArrayList<Double> kontenBaris = new ArrayList<Double>();
                for (String elemen: barisFile.split(" ")) {
                    kontenBaris.add(Double.parseDouble(elemen));
                }
                kontenDinamis.add(kontenBaris);
            }
            double[][] konten = new double[kontenDinamis.size()][kontenDinamis.get(0).size()];
            for (i = 0; i < kontenDinamis.size(); i++) {
                for (j = 0; j < kontenDinamis.get(0).size(); j++) {
                    konten[i][j] = kontenDinamis.get(i).get(j);
                }
            }
            // System.out.println(new Matriks(konten).getString());
            return new Matriks(konten);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("file tidak ada.");
        }
        return null;
    }

    public static int pilihanInput(Scanner input) {
        int pilihanInput;
        do {
            System.out.println(
            """
            PILIHAN METODE INPUT
            1. Manual
            2. File
            3. Kembali
            """);
            System.out.print("Pilih metode input matriks yang mau digunakan: ");
            pilihanInput = input.nextInt();
            if (pilihanInput != 1 && pilihanInput != 2 && pilihanInput != 3) {
                System.out.println("Input tidak valid, ulangi!");
                System.out.println();
            }
        } while (pilihanInput != 1 && pilihanInput != 2 && pilihanInput != 3);
        return pilihanInput;
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
        int aksi, pilihanInput;
        Matriks m;
        do {
            System.out.println(
            """
            PILIHAN METODE
            1. Eliminasi Gauss-Jordan
            2. Adjoin
            3. Kembali
            """);
            System.out.print("Pilih metode yang mau digunakan: ");
            aksi = input.nextInt();
            if (aksi == 1) {
                exit = true;
                pilihanInput = pilihanInput(input);
                if (pilihanInput == 1) {
                    m = inputMatriksBalikanManual(input);
                    if (m.adaMatriksBalikan()) {
                        System.out.println(m.inversWithGaussJordan().getString());
                    } else {
                        System.out.println("\nMatriks tidak mempunyai balikan.\n");
                    }
                } else if (pilihanInput == 2) {
                    m = inputMatriksBalikanFile(input);
                    if (m.adaMatriksBalikan()) {
                        System.out.println(m.inversWithGaussJordan().getString());
                    } else {
                        System.out.println("\nMatriks tidak mempunyai balikan.\n");
                    }
                } else if (pilihanInput == 3) {
                    exit = false;
                } 
                if (exit) {
                    exitProc(input);
                }   
            } else if (aksi == 2) {
                exit = true;
                pilihanInput = pilihanInput(input);
                if (pilihanInput == 1) {
                    m = inputMatriksBalikanManual(input);
                    if (m.adaMatriksBalikan()) {
                        System.out.println(m.inversWithAdjoin().getString());
                    } else {
                        System.out.println("\nMatriks tidak mempunyai balikan.\n");
                    }
                } else if (pilihanInput == 2) {
                    m = inputMatriksBalikanFile(input);
                    if (m.adaMatriksBalikan()) {
                        System.out.println(m.inversWithAdjoin().getString());
                    } else {
                        System.out.println("\nMatriks tidak mempunyai balikan.\n");
                    }
                }else if (pilihanInput == 3) {
                    exit = false;
                }
                if (exit) {
                    exitProc(input);
                }   
            } else if (aksi == 3) {
                exit = true;
                System.out.println();
            } else {
                System.out.println("\nMasukkan tidak valid, ulangi!\n");
            }
        } while(!exit);
    }

    private static void exitProc(Scanner input) {
        System.out.print("Kembali ke menu utama? Masukkan apapun untuk kembali ke menu utama: ");
        input.next();
        System.out.println();
    }
}
