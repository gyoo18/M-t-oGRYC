public class Champ2V1 {
    public double[][] c;
    public int nX;
    public int nY;
    public double L;
    public double H;

    public Champ2V1(double largeur, double hauteur, int nCasesX, int nCasesY){
        nX = nCasesX;
        nY = nCasesY;
        L = largeur;
        H = hauteur;
        c = new double[nX][nY];
    }

    public Champ2V1(Champ2V1 v){
        copier(v);
    }

    public void remplir(double v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y] = v;
            }
        }
    }

    /**
     * Renvoie un échantillon linéairement interpollé entre les quatres points définis les plus proches du point (x,y).
     * @param x - coordonée x en espace (0-1) du point d'échantillonage.
     * @param y - coordonée y en espace (0-1) du point d'échantillonage.
     * @return - Échantillon du champ.
     */
    public double c(double x, double y){
        int x0 = (int)Math.floor(x*nX);
        int x1 = x0 + 1;
        int y0 = (int)Math.floor(y*nY);
        int y1 = y0 + 1;

        double mX = (double)x0/(x-(double)x0);
        double mY = (double)y0/(y-(double)y0);
        return (1.0 - mY)*((1.0 - mX)*c[x0][y0] + mX*c[x1][y0]) + mY*((1.0 - mX)*c[x0][y1] + mX*c[x1][y1]);
    }

    public Champ2V1 addi(Champ2V1 v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y] += v.c((double)x/(double)v.nX,(double)y/(double)v.nY);
            }
        }

        return this.copier();
    }
    
    public Champ2V1 addi(double v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y] += v;
            }
        }

        return this.copier();
    }

    public Champ2V1 sous(Champ2V1 v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y] -= v.c((double)x/(double)v.nX,(double)y/(double)v.nY);
            }
        }

        return this.copier();
    }

    public Champ2V1 sous(double v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y] -= v;
            }
        }

        return this.copier();
    }

    public Champ2V1 mult(Champ2V1 v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y] *= v.c((double)x/(double)v.nX,(double)y/(double)v.nY);
            }
        }

        return this.copier();
    }

    public Champ2V1 mult(double v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y] *= v;
            }
        }

        return this.copier();
    }

    public Champ2V1 div(Champ2V1 v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y] /= v.c((double)x/(double)v.nX,(double)y/(double)v.nY);
            }
        }

        return this.copier();
    }

    public Champ2V1 div(double v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y] /= v;
            }
        }

        return this.copier();
    }

    public Champ2V2 grad(){
        Champ2V2 grad = new Champ2V2(L, H, nY, nX);
        double Dx = L/(double)nX;
        double Dy = H/(double)nY;
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                double gradX1 = 0;
                double gradX0 = 0;
                double gradY1 = 0;
                double gradY0 = 0;
                if(x + 1 < c.length && x - 1 >= 0){
                    gradX1 = (c[x][y] - c[x+1][y])/Dx;
                    gradX0 = (c[x][y] - c[x-1][y])/-Dx;
                }else if(x + 1 >= c.length){
                    gradX0 = (c[x][y] - c[x-1][y])/-Dx;
                    gradX1 = gradX0;
                }else if( x == 0){
                    gradX1 = (c[x][y] - c[x+1][y])/Dx;
                    gradX0 = gradX1;
                }
                if(y + 1 < c[y].length && y - 1 >= 0){
                    gradY1 = (c[x][y] - c[x][y+1])/Dy;
                    gradY0 = (c[x][y] - c[x][y-1])/-Dy;
                }else if(y + 1 >= c[x].length){
                    gradY0 = (c[x][y] - c[x][y-1])/-Dy;
                    gradY1 = gradY0;
                }else if( y == 0){
                    gradY1 = (c[x][y] - c[x][y+1])/Dy;
                    gradY0 = gradY1;
                }

                grad.c[x][y] = new Vecteur2D((gradX0 + gradX1)/2.0,(gradY0 + gradY1)/2.0);
            }
        }

        return grad;
    }

    public void copier(Champ2V1 v){
        this.nX = v.nX;
        this.nY = v.nY;
        this.L = v.L;
        this.H = v.H;
        this.c = v.c.clone();
    }

    public Champ2V1 copier(){
        return new Champ2V1(this);
    }
}
