import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;

public class InterpolasiPolinom {
    public static Matriks inputIPFile(Scanner input) {
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

            int n = konten.length;
            double[][] kontenFix = new double[n][n + 1];
            for (i = 0; i < n; i++) {
                double a = 1;
                double x = konten[i][0];
                double y = konten[i][1];
                for (j = 0; j < n; j++) {
                    kontenFix[i][j] = a;
                    a *= x;
                }
                kontenFix[i][n] = y;
            }
            return new Matriks(kontenFix);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("file tidak ada.");
        }
        return null;
    }

    public static Matriks inputIPmanual (Scanner input) {
        System.out.print("Masukkan n (banyaknya titik): ");
        int n = input.nextInt();
    
        double[][] konten = new double[n][n + 1];

    
        for (int i = 0; i < n; i++) {
            double a = 1;
            System.out.print(String.format("Masukkan titik ke-%d: ", i + 1));
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



    private static String IPSolver(Matriks m, Scanner input) {
        String saveString = "";
        Matriks mr = Matriks.reduksiBaris(m, true);
        int jumlahVariabel = mr.getKolom() - 1;
        double[] hasil = new double[jumlahVariabel];
        for (int k = jumlahVariabel - 1; k >= 0; k--) {
            hasil[k] = mr.getElement(k, mr.getIdxKolomTerakhir());
            for (int j = hasil.length - 1; j > k; j--) {
                hasil[k] -= hasil[j] * mr.getElement(k, j);
            }
        }

        System.out.println("\nPersamaan hasil interpolasi polinom:");
        saveString += "Persamaan hasil interpolasi polinom:\n";
        System.out.printf("%s", String.valueOf(hasil[0]));
        saveString += String.format("%s", String.valueOf(hasil[0]));
        for (int i = 1; i < hasil.length; i++) {
            if (hasil[i] != 0) {
                if (hasil[i] > 0) {
                    if (i == 1) {
                        System.out.printf(" + %s(x)", String.valueOf(hasil[i]), i);
                        saveString += String.format(" + %s(x)", String.valueOf(hasil[i]), i);
                    } else {
                        System.out.printf(" + %s(x^%d)", String.valueOf(hasil[i]), i);
                        saveString += String.format(" + %s(x^%d)", String.valueOf(hasil[i]), i);
                    }
                } else {
                    if (i == 1) {
                        System.out.printf(" - %s(x)", String.valueOf(hasil[i] * -1), i);
                        saveString += String.format(" - %s(x)", String.valueOf(hasil[i] * -1), i);
                    } else {
                        System.out.printf(" - %s(x^%d)", String.valueOf(hasil[i] * -1), i);
                        saveString += String.format(" - %s(x^%d)", String.valueOf(hasil[i] * -1), i);
                    }
                }
            }
        }
        System.out.print("\nMasukkan nilai x yang akan ditaksir: ");
        double x = input.nextDouble();      
          
        double xm = 1;
        double y = 0;

        
        for (int i = 0; i < hasil.length; i++) {
            y += hasil[i] * xm;
            xm *= x;
        }
        System.out.printf("p_%d(%s) = %s%n\n", mr.getBaris(), String.valueOf(x), String.valueOf(y));
        saveString += String.format("\np_%d(%s) = %s%n\n", mr.getBaris(), String.valueOf(x), String.valueOf(y));
        return saveString;
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
        int pilihanInput;
        pilihanInput = pilihanInput(input);
        if (pilihanInput == 1) {
            konfirmasiInputFile(input,IPSolver(inputIPmanual(input), input));
            exitProc(input);
        } else if (pilihanInput == 2) {
            konfirmasiInputFile(input,IPSolver(inputIPFile(input), input));
            exitProc(input);
        }
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