import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
public class Determinan {

    public static Matriks inputDeterminanFile(Scanner input) {
        System.out.print("Masukkan nama file yang berada di folder test: ");
        String chosenFile = input.next();
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path filePath = Paths.get(currentPath.getParent().toString(), "test", chosenFile);
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

    public static void aksi(Scanner input) {
        String saveString = "";
        boolean exit = false;
        int aksi, pilihanInput;
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
                exit = true;
                pilihanInput = pilihanInput(input);
                if (pilihanInput == 1) {
                    saveString = String.format("Determinan matriks yang dimasukkan = %f\n", inputDeterminanManual(input).getDeterminanReduksiBaris());
                    System.out.println("\n" + saveString);
                    konfirmasiInputFile(input, saveString);
                } else if (pilihanInput == 2) {
                    Matriks m = inputDeterminanFile(input);
                    if (m.hasDeterminan()) {
                        saveString = String.format("Determinan matriks yang dimasukkan = %f\n", m.getDeterminanReduksiBaris());
                        System.out.println("\n" + saveString);
                        konfirmasiInputFile(input, saveString);
                    } else {
                        System.out.println("Matriks tidak memunyai jumlah baris dan kolom yang sama sehingga tidak mempunyai baris.");
                        konfirmasiInputFile(input, "Matriks tidak memunyai jumlah baris dan kolom yang sama sehingga tidak mempunyai baris.");
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
                    saveString = String.format("Determinan matriks yang dimasukkan = %f\n", inputDeterminanManual(input).getDeterminanKofaktor());
                    System.out.println("\n" + saveString);
                    konfirmasiInputFile(input, saveString);
                } else if (pilihanInput == 2) {
                    Matriks m = inputDeterminanFile(input);
                    if (m.hasDeterminan()) {
                        saveString = String.format("Determinan matriks yang dimasukkan = %f\n", m.getDeterminanKofaktor());
                        System.out.println("\n" + saveString);
                        konfirmasiInputFile(input, saveString);
                    } else {
                        System.out.println("\nMatriks mempunyai jumlah baris dan kolom yang tidak sama sehingga tidak mempunyai baris.\n");
                        konfirmasiInputFile(input, "Matriks mempunyai jumlah baris dan kolom yang tidak sama sehingga tidak mempunyai baris.\n");
                    }
                } else if (pilihanInput == 3) {
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

    private static void konfirmasiInputFile(Scanner input, String saveString) {
        String fileName = "";
        String konfirmasi = "";
        System.out.print("Apakah Anda ingin menyimpan output program dalam file? (y/n) ");
        konfirmasi = input.next();
        while ((!konfirmasi.equals("y")) && (!konfirmasi.equals("Y")) && (!konfirmasi.equals("n")) && (!konfirmasi.equals("N"))) {
            System.out.println("Masukkan tidak valid. Ulangi!");
            System.out.print("Apakah Anda ingin menyimpan output program dalam file? (y/n) ");
            konfirmasi = input.next();
        }
        if ((konfirmasi.equals("y")) || (konfirmasi.equals("Y"))) {
            System.out.print("Masukkan nama file penyimpanan (ekstensi harus .txt): ");
            fileName = input.next();
            outputFile(fileName, saveString);
            System.out.println("Output telah disimpan di folder test.");
        }
        System.out.println();
    }

    private static void outputFile(String fileName, String saveString) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path filePath = Paths.get(currentPath.getParent().toString(), "test", fileName);
        try {
            FileWriter writer = new FileWriter(filePath.toString());
            writer.write(saveString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void exitProc(Scanner input) {
        System.out.print("Kembali ke menu utama? Masukkan apapun untuk kembali ke menu utama: ");
        input.next();
        System.out.println();
    }
}
