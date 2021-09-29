import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
public class SPL {

    public static Matriks inputSPLFile(Scanner input) {
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
        System.out.println();
        Matriks matriks = Matriks.reduksiBaris(m, true);
        System.out.println(matriks.getString());
        if (hasNoSolution(matriks)) {
            System.out.println("Sistem Persamaan Linear tidak mempunyai solusi.");
        } else if (hasManySolution(matriks)) {
            manyEselonSolver(matriks);
        } else {
            singleEselonSolver(matriks);
        }
        System.out.println();
    }

    public static void solveWithGaussJordan(Matriks m) {
        System.out.println();
        Matriks matriks = Matriks.eselonTereduksi(m);
        System.out.println(matriks.getString());
        if (hasNoSolution(matriks)) {
            System.out.println("Sistem Persamaan Linear tidak mempunyai solusi.");
        } else if (hasManySolution(matriks)) {
            manyEselonSolver(matriks);
        } else {
            singleEselonSolver(matriks);
        }
        System.out.println();
    }

    private static boolean hasManySolution(Matriks m) {
        boolean many;
        int j = 0;
        if (m.getBaris() < m.getKolom() - 1) {
            many = true;
        } else {
            many = true;
            while (many && j < m.getKolom()) {
                if (m.getElement(m.getIdxBarisTerakhir(), j) != 0) {
                    many = false;
                }
                j++;
            }
            while (many && j < m.getKolom()) {
                if (m.getElement(m.getIdxBarisTerakhir(), j) != 0) {
                    many = false;
                }
                j++;
            }
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

    public static boolean hasSingleSolution(Matriks m) {
        return !hasManySolution(m) && !hasNoSolution(m);
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
            System.out.println(String.format("Variabel %d = %s", i + 1, String.valueOf(hasil[i])));
        }
    }

    private static void manyEselonSolver(Matriks m) {
        int jumlahVariabel = m.getKolom() - 1;
        int i, j;
        String[][] hasil = new String[jumlahVariabel][jumlahVariabel];
        double zero = 0;
        boolean ketemu;
        for (i = 0; i < hasil.length; i++) {
            for (j = 0; j < hasil[0].length; j++) {
                if (i == j) {
                    hasil[i][j] = "real";
                } else {
                    hasil[i][j] = String.valueOf(zero);
                }
            }
        }
        for (i = m.getIdxBarisTerakhir(); i >= 0; i--) {
            j = 0;
            ketemu = false;
            while (j < m.getKolom() && !ketemu) {
                if (m.getElement(i, j) == 1) {
                    hasil[j][j] = String.valueOf(m.getElement(i, m.getIdxKolomTerakhir()));
                    for (int l = m.getKolom() - 2; l > j; l--) {
                        double constant = m.getElement(i, l) * -1;
                        hasil[j][l] = String.valueOf(m.getElement(i, l) * -1);
                        for (int k = l; k < hasil[0].length; k++) {
                            if (!hasil[l][k].equals("real")) {
                                hasil[j][k] = String.valueOf(Double.parseDouble(hasil[j][k]) + constant * Double.parseDouble(hasil[l][k]));
                            }
                        }
                    }
                    ketemu = true;
                }
                j++;
            }
        }
        for (i = 0; i < hasil.length; i++) {
            boolean ada = false;
            boolean sudah = false;
            System.out.print("Variabel " + (i + 1));
            if (hasil[i][i].equals("real")) {
                System.out.println(" adalah bilangan real bebas.");
            } else {
                System.out.print(" = ");
                for (j = i; j < hasil.length; j++) {
                    if (j == i) {
                        if (Double.parseDouble(hasil[i][j]) != 0) {
                            System.out.print(Double.parseDouble(hasil[i][j]));
                            ada = true;
                        }
                    } else {
                        if (Double.parseDouble(hasil[i][j]) != 0) {
                            if (!ada && !sudah) {
                                System.out.print(String.format("%s(Variabel %d)", hasil[i][j], j + 1));
                                sudah = true;
                            } else {
                                if (Double.parseDouble(hasil[i][j]) < 0) {
                                    System.out.print(String.format(" - %s(Variabel %d)", String.valueOf(Double.parseDouble(hasil[i][j]) * -1), j + 1));
                                } else {
                                    System.out.print(String.format(" + %s(Variabel %d)", hasil[i][j], j + 1));
                                }
                                
                            }
                        }
                    }
                }
                System.out.println();
            }
        }
    }

    public static void solveWithInversMatriks(Matriks m) {
        System.out.println();
        if (m.adaMatriksBalikan()) {
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
        } else {
            System.out.println("Matriks variabel (bagian kiri persamaan) tidak memiliki balikan.");
        }
        System.out.println();
    }

    public static void solveWithCramer(Matriks m) {
        System.out.println();
        if (hasSingleSolution(m)) {
            double[][] kontenB = new double[m.getBaris()][1];
            for (int i = 0; i < m.getBaris(); i++) {
                kontenB[i][0] = m.getElement(i, m.getIdxKolomTerakhir());
            }
            double[][] kontenA = new double[m.getBaris()][m.getKolom() - 1];
            for (int i = 0; i < m.getBaris(); i++) {
                for (int j = 0; j < m.getKolom() - 1; j++) {
                    kontenA[i][j] = m.getElement(i, j);
                }
            }
            for (int iterasi = 0; iterasi < kontenA[0].length; iterasi++) {
                double[][] kontenACopy = new double[kontenA.length][kontenA[0].length];
                for (int i = 0; i < kontenA.length; i++) {
                    for (int j = 0; j < kontenA[0].length; j++) {
                        kontenACopy[i][j] = kontenA[i][j];
                    }
                }
                for (int i = 0; i < m.getBaris(); i++) {
                    kontenACopy[i][iterasi] = kontenB[i][0];
                }
                System.out.println(String.format("Variabel %d = %f", iterasi + 1, new Matriks(kontenACopy).getDeterminanKofaktor() / new Matriks(kontenA).getDeterminanKofaktor()));
            }
        } else {
            System.out.println("SPL tidak mempunyai solusi tunggal (tidak bisa diselesaikan dengan kaidah Cramer).");
        }
        System.out.println();
    }
    public static int pilihanInput(Scanner input) {
        int pilihanInput;
        do {
            System.out.println(
            """
            PILIH METODE INPUT
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
        boolean valid = false;
        int aksi, pilihanInput;
        do {
            System.out.println(
            """
            PILIHAN METODE
            1. Metode eliminasi Gauss
            2. Metode eliminasi Gauss-Jordan
            3. Metode matriks balikan
            4. Kaidah Cramer
            5. Kembali
            """);
            System.out.print("Pilih metode yang mau digunakan: ");
            aksi = input.nextInt();
            if (aksi == 1) {
                valid = true;
                pilihanInput = pilihanInput(input);
                if (pilihanInput == 1) {
                    SPL.solveWithGauss(SPL.inputSPLManual(input));
                } else if (pilihanInput == 2) {
                    SPL.solveWithGauss(SPL.inputSPLFile(input));
                } else if (pilihanInput == 3) {
                    valid = false;
                }
            } else if (aksi == 2) {
                valid = true;
                pilihanInput = pilihanInput(input);
                if (pilihanInput == 1) {
                    SPL.solveWithGaussJordan(SPL.inputSPLManual(input));
                } else if (pilihanInput == 2) {
                    SPL.solveWithGaussJordan(SPL.inputSPLFile(input));
                } else if (pilihanInput == 3) {
                    valid = false;
                }
            } else if (aksi == 3) {
                valid = true;
                pilihanInput = pilihanInput(input);
                if (pilihanInput == 1) {
                    SPL.solveWithInversMatriks(SPL.inputSPLManual(input));
                } else if (pilihanInput == 2) {
                    SPL.solveWithInversMatriks(SPL.inputSPLFile(input));
                } else if (pilihanInput == 3) {
                    valid = false;
                }
            } else if (aksi == 4) {
                valid = true;
                pilihanInput = pilihanInput(input);
                if (pilihanInput == 1) {
                    SPL.solveWithCramer(SPL.inputSPLManual(input));
                } else if (pilihanInput == 2) {
                    SPL.solveWithCramer(SPL.inputSPLFile(input));
                } else if (pilihanInput == 3) {
                    valid = false;
                }
            } else if (aksi == 5) {
                valid = true;
                System.out.println();
            } else {
                System.out.println("Input tidak valid, ulangi!");
                System.out.println();
            }
        } while(!valid);
        System.out.print("Kembali ke menu utama? Masukkan apapun untuk kembali ke menu utama: ");
        input.next();
        System.out.println();
    }
}
