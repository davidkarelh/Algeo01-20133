import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

class Regresi{

    public static Matriks inputMatriksDataFile(Scanner input) {
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
            return new Matriks(konten);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("file tidak ada.");
        }
        return null;
    }

    public static Matriks inputMatriksDataManual(Scanner input){
        System.out.print("Masukkan jumlah peubah x:");
        Integer N = input.nextInt();
        System.out.print("Masukkan jumlah sampel:");
        Integer M = input.nextInt();

        double[][] tabeldata = new double[M][N+1];

        /* Inisialisasi array */
        System.out.println("Format pengisian: x1 x2 .. xn y");
        for (int i = 0; i < M; i++){
            System.out.print("Masukkan sampel ke-"+ ( i + 1 ) + ": ");
            for (int j = 0; j < N + 1; j++){
                tabeldata[i][j] = input.nextDouble();
            }
        }
        return new Matriks(tabeldata);
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

    public static void aksi(Scanner input){
        String saveString = "";
        int pilihanInput;
        pilihanInput = pilihanInput(input);
        if (pilihanInput == 1 || pilihanInput == 2) {
            /* Regresi */
            double total;
            Matriks data = new Matriks();
            if (pilihanInput == 1) {
                data = inputMatriksDataManual(input);
            } else if (pilihanInput == 2) {
                data = inputMatriksDataFile(input);
            }
            double[][] tabelregresi = new double[data.getKolom()][data.getKolom() + 1];
        Matriks regresi = new Matriks(tabelregresi);
        for (int i = 0; i < regresi.getBaris(); i++){
            /* Baris Pertama */
            if (i==0){
                for (int j = 0; j < regresi.getKolom(); j++){
                    if (j == 0){
                        regresi.setElement(i, j, data.getBaris());
                    }
                    else{
                        total = 0;
                        for(int l = 0; l < data.getBaris(); l++){
                            total += data.getElement(l,j-1);
                        }
                        regresi.setElement(i, j, total);
                    }
                }
            }
            /* Baris bukan pertama */
            else{
                for (int j = 0; j < regresi.getKolom(); j++){
                    if (j == 0){
                        regresi.setElement(i, j, regresi.getElement(j, i));
                    }
                    else{
                        total = 0;
                        for (int l = 0; l < data.getBaris(); l++){
                            total += data.getElement(l, i-1) * data.getElement(l, j-1);
                        }
                        regresi.setElement(i, j, total);
                    }
                }
            }
        }
            
            saveString += SPL.solveWithGaussJordan(regresi);
            System.out.println("dengan");
            saveString += "dengan\n";
            for (int i = 0; i < regresi.getBaris(); i++) {
                System.out.printf("b_%d = Variabel %d\n", i, i + 1);
                saveString += String.format("b_%d = Variabel %d\n", i, i + 1);
            }
            konfirmasiInputFile(input, saveString);
            exitProc(input);
        }
    }

    private static void konfirmasiInputFile(Scanner input, String saveString) {
        String fileName = "";
        String konfirmasi = "";
        System.out.print("\nApakah Anda ingin menyimpan output program dalam file? (y/n) ");
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