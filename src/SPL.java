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
        // System.out.println(matriks.getString());
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
        int j, indexSeharusnya;
        if (m.getBaris() < m.getKolom() - 1) {
            many = true;
        } else {
            indexSeharusnya = m.getIdxKolomTerakhir() - 1;
            int i = m.getIdxBarisTerakhir();
            boolean stopLoop;
            many = false;
            while (!many && i >= 0) {
                j = 0;
                stopLoop = false;
                while (!stopLoop && !many && j < m.getKolom()) {
                    if (m.getElement(i, j) != 0 && j == indexSeharusnya) {
                        indexSeharusnya--;
                        stopLoop = true;
                    } else if(m.getElement(i, j) != 0 && j != indexSeharusnya) {
                        many = true;
                    }
                    j++;
                }
                i--;
            }
        }
        return many;
    } 

    private static boolean hasNoSolution(Matriks m) {
        boolean noSolution = false;
        int i = m.getIdxBarisTerakhir();
        int j;
        boolean stopLoop = false;
        while (!stopLoop && i >= 0) {
            j = 0;
            while (!stopLoop && j < m.getKolom()) {
                if (m.getElement(i, j) != 0 && j == m.getIdxKolomTerakhir()) {
                    noSolution = true;
                    stopLoop = true;
                } else if(m.getElement(i, j) != 0 && j != m.getIdxKolomTerakhir()) {
                    stopLoop = true;
                }
                j++;
            }
            i--;
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
        // for (int x = 0; x < hasil.length; x++) {
        //     for (int y = 0; y < hasil[0].length; y++) {
        //         System.out.print(hasil[x][y] + " - ");
        //     }
        //     System.out.println();
        // }
        // System.out.println();
        for (i = m.getIdxBarisTerakhir(); i >= 0; i--) {
            j = 0;
            ketemu = false;
            while (j < m.getKolom() && !ketemu) {
                if (m.getElement(i, j) == 1) {
                    hasil[j][j] = String.valueOf(m.getElement(i, m.getIdxKolomTerakhir()));
                    for (int k = m.getKolom() - 2; k > j; k--) {
                        double constant = m.getElement(i, k) * -1;
                        for (int l = k; l < hasil[0].length; l++) {
                            if (l == k && !hasil[l][l].equals("real")) {
                                hasil[j][j] = String.valueOf(Double.valueOf(hasil[j][j]) + constant * Double.valueOf(hasil[k][l]));
                            } else if (l == k && hasil[l][l].equals("real")) {
                                hasil[j][l] = String.valueOf(Double.valueOf(hasil[j][l]) + constant);
                            } else {
                                hasil[j][l] = String.valueOf(Double.valueOf(hasil[j][l]) + constant * Double.parseDouble(hasil[k][l]));
                            }

                            // for (int x = 0; x < hasil.length; x++) {
                            //     for (int y = 0; y < hasil[0].length; y++) {
                            //         System.out.print(hasil[x][y] + " - ");
                            //     }
                            //     System.out.println();
                            // }
                            // System.out.println();
                        }
                    }
                    ketemu = true;
                }
                j++;
            }
        }
        // for (i = 0; i < hasil.length; i++) {
        //     for (j = 0; j < hasil[0].length; j++) {
        //         System.out.print(hasil[i][j] + " - ");
        //     }
        //     System.out.println();
        // }
        System.out.println();
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
                        if (Double.parseDouble(hasil[i][j]) == 0) {
                            boolean printZero = true;
                            for (int x = j + 1; x < hasil[0].length; x++) {
                                if (Double.parseDouble(hasil[i][x]) != 0) {
                                    printZero = false;
                                    break;
                                }
                            }
                            if (printZero) {
                                System.out.print(Double.parseDouble(hasil[i][j]));
                            }
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
        if (m.getBaris() == m.getKolom() - 1) {
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
        } else {
            System.out.println("Matriks variabel (bagian kiri persamaan) tidak memiliki jumlah baris dan kolom yang sama sehingga tidak mempunyai invers.");
        }
        
        System.out.println();
    }

    public static void solveWithCramer(Matriks m) {
        System.out.println();
        Matriks matriks = Matriks.reduksiBaris(new Matriks(m.getKonten()), true);
        if (hasSingleSolution(matriks)) {
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
                if (valid) {
                    exitProc(input);
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
                if (valid) {
                    exitProc(input);
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
                if (valid) {
                    exitProc(input);
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
                if (valid) {
                    exitProc(input);
                }
            } else if (aksi == 5) {
                valid = true;
                System.out.println();
            } else {
                System.out.println("Input tidak valid, ulangi!");
                System.out.println();
            }
        } while(!valid);   
    }

    private static void exitProc(Scanner input) {
        System.out.print("Kembali ke menu utama? Masukkan apapun untuk kembali ke menu utama: ");
        input.next();
        System.out.println();
    }
}
