public class Matriks {
    private int baris;
    private int kolom;
    private double[][] content;

    Matriks(double[][] konten) {
        this.baris = konten.length;
        this.kolom = konten[0].length;
        this.content = konten;
    }
    
    public int getBaris() {
        return baris;
    }

    public int getKolom() {
        return kolom;
    }

    public double[][] getKonten() {
        return content;
    }

    public void setKonten(double[][] content) {
        this.baris = content.length;
        this.kolom = content[0].length;
        this.content = content;
    }

    public void setElement(int i, int j, double x) {
        this.content[i][j] = x;
    }

    public double getElement(int i, int j) {
        return this.content[i][j];
    }

    public int getIdxBarisTerakhir() {
        return this.baris - 1;
    }

    public int getIdxKolomTerakhir() {
        return this.kolom - 1;
    }

    public boolean isSquare() {
        return this.baris == this.kolom;
    }

    public String getString() {
        String string = "";
        for (int i = 0; i < this.baris; i++) {
            for (int j = 0; j < this.kolom; j++) {
                string += "|";
                string += String.valueOf(this.content[i][j]);
                string += "";
            }
            string += "|\n";
        }
        return string;
    }

    public boolean hasDeterminan() {
        return this.baris == this.kolom;
    }

    public static Matriks scalarMultiplication(double scalar, Matriks matriks) {
        double[][] konten = matriks.getKonten();
        for (int i = 0; i < matriks.getBaris(); i++) {
            for (int j = 0; j < matriks.getKolom(); j++) {
                konten[i][j] *= scalar;
            }
        }
        return new Matriks(konten);
    }

    public static Matriks reduksiBaris(Matriks matriks, boolean eselon) {
        double[][] konten = matriks.getKonten();
        int m = matriks.getBaris();
        int n = matriks.getKolom();
        int[] posisi = {0, 0};
        while (posisi[0] < m && posisi[1] < n) {
            if (konten[posisi[0]][posisi[1]] == 0) {
                boolean tukar = false;
                for (int j = posisi[1]; j < n; j++) {
                    posisi[1] = j;
                    for (int i = posisi[0]; i < m; i++) {
                        if (konten[i][j] != 0) {
                            double[] temp = konten[posisi[0]];
                            konten[posisi[0]] = konten[i];
                            konten[i]= temp; 
                            tukar = true;
                            break;
                        }
                    }
                    if (tukar) 
                        break;
                }
            }

            if (eselon) {
                if (konten[posisi[0]][posisi[1]] == 0) break; 
                double constant = konten[posisi[0]][posisi[1]];

                for (int j = posisi[1]; j < n; j++) {
                    konten[posisi[0]][j] /= constant;
                }
            }

            double constant2 = 0;
            int k = 1;

            for (int i = posisi[0] + 1; i < m; i++) {
                for (int j = posisi[1]; j < n; j++) {
                    if (i == posisi[0] + k && j == posisi[1]) {
                        constant2 = konten[i][j] / konten[posisi[0]][posisi[1]];
                            
                    }
                    konten[i][j] -= constant2 * konten[posisi[0]][j];
                }
                k += 1;
            }
            posisi[0] += 1; 
            posisi[1] += 1; 
        }
        return new Matriks(konten);
    }

    public static Matriks eselonTereduksi(Matriks matriks) {
        matriks = Matriks.reduksiBaris(matriks, true);
        double[][] konten = matriks.getKonten();
        int[] posisi = {1, 1};
        int m = matriks.getBaris();
        int n = matriks.getKolom();
        while (posisi[0] < m && posisi[1] < n) {
            if (konten[posisi[0]][posisi[1]] == 0) {
                boolean ketemu = false;
                for (int j = posisi[1]; j < n - 1; j++) {
                    posisi[1] = j;
                    for (int i = posisi[0]; i < m; i++) {
                        if (konten[i][j] != 0) {
                            ketemu = true;
                            break;
                        }
                    }
                    if (ketemu) 
                        break;
                }
            }
            if (konten[posisi[0]][posisi[1]] == 0) break; 
            if (konten[posisi[0] - 1][posisi[1]] != 0) {
                for (int k = 1; k <= posisi[0]; k++){
                    double constant = konten[posisi[0] - k][posisi[1]] / konten[posisi[0]][posisi[1]];
                    for (int j = 0; j < n; j++) {
                        konten[posisi[0] - k][j] -= constant * konten[posisi[0]][j];
                    }
                }
            }
            posisi[0] += 1; 
            posisi[1] += 1; 
        }
        return new Matriks(konten);
    }

    public static Matriks add(Matriks matriks1, Matriks matriks2) {
        double[][] konten = matriks1.getKonten();
        for (int i = 0; i < matriks1.baris; i++) {
            for (int j = 0; j < matriks1.kolom; j++) {
                konten[i][j] += matriks2.getElement(i, j);
            }
        }
        return new Matriks(konten);
    }

    public static Matriks substract_by_(Matriks matriks1, Matriks matriks2) {
        double[][] konten = matriks1.getKonten();
        for (int i = 0; i < matriks1.baris; i++) {
            for (int j = 0; j < matriks1.kolom; j++) {
                konten[i][j] -= matriks2.getElement(i, j);
            }
        }
        return new Matriks(konten);
    }

    public static Matriks multiply(Matriks matriks1, Matriks matriks2) {
        double[][] konten = new double[matriks1.getBaris()][matriks2.getKolom()];
        for(int i = 0; i < matriks1.getBaris(); i++){    
            for(int j = 0; j < matriks2.getKolom(); j++) {    
               konten[i][j] = 0;     
                for(int k = 0; k < matriks1.getKolom(); k++) {     
                    konten[i][j] += matriks1.getElement(i, k) * matriks2.getElement(k, j);
                } 
            } 
        }   
        return new Matriks(konten);
    }

    public static Matriks transpose(Matriks matriks) {
        double[][] konten = new double[matriks.getKolom()][matriks.getBaris()];
        for (int i = 0; i < matriks.getKolom(); i++) {
            for (int j = 0; j < matriks.getBaris(); j++) {
                konten[i][j] = matriks.getElement(j, i);
            }
        }
        return new Matriks(konten);
    }

    public Matriks getSubMatrix(int rowIdx, int colIdx) {
        int kolom;
        double[][] konten = new double[this.baris - 1][this.kolom - 1];
        int baris = 0;
        for (int i = 0; i < this.baris; i++) {
            kolom = 0;
            for (int j = 0; j < this.kolom; j++) {
                if (i != rowIdx && j != colIdx) {
                    konten[baris][kolom] = this.content[i][j];
                    kolom++;
                    if (kolom == konten[0].length) {
                        baris++;
                    }
                }
            }
        }
        return new Matriks(konten);
    }

    public Matriks getMatriksAdjoin() {
        double[][] konten = new double[this.baris][this.kolom];
        for (int i = 0; i < this.baris; i++) {
            for (int j = 0; j < this.kolom; j++) {
                if ((i + j) % 2 == 0) {
                    konten[i][j] = new Matriks(this.content).getSubMatrix(i, j).getDeterminanKofaktor();
                } else {
                    konten[i][j] = -1 * new Matriks(this.content).getSubMatrix(i, j).getDeterminanKofaktor();
                }
            }
        }
        return Matriks.transpose(new Matriks(konten));
    }

    public Matriks inversWithAdjoin() {
        double scalar = 1 / new Matriks(this.content).getDeterminanKofaktor();
        return Matriks.scalarMultiplication(scalar, new Matriks(this.content).getMatriksAdjoin());
    }

    public double getDeterminanKofaktor() {
        if (this.baris == 1) {
            return this.content[0][0];
        } else if (this.baris == 2) {
            return this.content[0][0] * this.content[1][1] - this.content[0][1] * this.content[1][0];
        } else {
            double determinan = 0;
            for (int k = 0; k < this.baris; k++) {
                double[][] konten = new double[this.baris - 1][this.kolom - 1];
                int baris = 0;
                for (int i = 0; i < this.baris; i++) {
                    for (int j = 1; j < this.kolom; j++) {
                        if (i != k) {
                            konten[baris][j - 1] = this.content[i][j];
                        }
                    }
                    if (i != k) {
                        baris++;
                    }
                }
                Matriks newMatriks = new Matriks(konten);
                if ((k % 2) == 0) {
                    determinan += this.content[k][0] * newMatriks.getDeterminanKofaktor();
                } else {
                    determinan -= this.content[k][0] * newMatriks.getDeterminanKofaktor();
                }
            }
            return determinan;
        }
    }

    public double getDeterminanReduksiBaris() {
        Matriks matriks = new Matriks(this.content);
        matriks = Matriks.reduksiBaris(matriks, false);
        double determinan = 1;
        for (int i = 0; i < matriks.getBaris(); i++) {
            for (int j = 0; j < matriks.getKolom(); j++) {
                if (i == j) {
                    determinan *= matriks.getElement(i, j);
                }
            }
        }
        return determinan;
    }

    public boolean adaMatriksBalikan() {
        return hasDeterminan() && new Matriks(this.content).getDeterminanKofaktor() != 0;
    }

    public Matriks inversWithGaussJordan() {
        double[][] konten = new double[this.baris][2 * this.kolom];
        for (int i = 0; i < this.baris; i++) {
            for (int j = 0; j < this.kolom; j++) {
                konten[i][j] = this.content[i][j];
            }
        }
        for (int i = 0; i < this.baris; i++) {
            for (int j = this.kolom; j < 2 * this.kolom; j++) {
                if (i == j - this.kolom) {
                    konten[i][j] = 1;
                } else {
                    konten[i][j] = 0;
                }
            }
        }
        
        Matriks matriks = eselonTereduksi(new Matriks(konten));
        konten = new double[this.baris][this.kolom];
        for (int i = 0; i < this.baris; i++) {
            for (int j = this.kolom; j < 2 * this.kolom; j++) {
                konten[i][j - this.kolom] = matriks.getElement(i, j);
            }
        }
        return new Matriks(konten);
    }
}