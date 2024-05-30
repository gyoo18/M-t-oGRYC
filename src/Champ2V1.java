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
        int x0 = Math.clamp((int)Math.floor(x*(double)(nX-1)),0, nX-1);
        int x1 = Math.clamp(x0 + 1,0, nX-1);
        int y0 = Math.clamp((int)Math.floor(y*(double)(nY-1)),0,nY-1);
        int y1 = Math.clamp(y0 + 1,0,nY-1);

        double mX = Math.clamp((x*(double)(nX-1)-(double)x0),0.0,1.0);
        double mY = Math.clamp((y*(double)(nY-1)-(double)y0),0.0,1.0);

        double va = c[x0][y0];
        double vb = c[x1][y0];
        double vc = c[x0][y1];
        double vd = c[x1][y1];
        return  (1.0 - mY)*((1.0 - mX)*va + mX*vb) + mY*((1.0 - mX)*vc + mX*vd);
    }

    public Champ2V1 c(double x, double y, double v){
        int x0 = Math.clamp((int)Math.floor(x*(double)(nX-1)),0, nX-1);
        int x1 = Math.clamp(x0 + 1,0, nX-1);
        int y0 = Math.clamp((int)Math.floor(y*(double)(nY-1)),0,nY-1);
        int y1 = Math.clamp(y0 + 1,0,nY-1);

        double mX = Math.clamp((x*(double)(nX-1)-(double)x0),0.0,1.0);
        double mY = Math.clamp((y*(double)(nY-1)-(double)y0),0.0,1.0);

        c[x0][y0] = (1.0-mY)*((1.0-mX)*v + mX*c[x0][y0]) + mY*c[x0][y0];
        c[x1][y0] = (1.0-mY)*((1.0-mX)*c[x1][y0] + mX*v) + mY*c[x1][y0];
        c[x0][y1] = (1.0-mY)*c[x0][y1] + mY*((1.0-mX)*v + mX*c[x0][y1]);
        c[x1][y1] = (1.0-mY)*c[x1][y1] + mY*((1.0-mX)*c[x1][y1] + mX*v);
        return this;
    }

    public Champ2V1 cA(double x, double y, double v){
        int x0 = Math.clamp((int)Math.floor(x*(double)(nX-1)),0, nX-1);
        int x1 = Math.clamp(x0 + 1,0, nX-1);
        int y0 = Math.clamp((int)Math.floor(y*(double)(nY-1)),0,nY-1);
        int y1 = Math.clamp(y0 + 1,0,nY-1);

        double mX = Math.clamp((x*(double)(nX-1)-(double)x0),0.0,1.0);
        double mY = Math.clamp((y*(double)(nY-1)-(double)y0),0.0,1.0);

        c[x0][y0] = (1.0-mY)*((1.0-mX)*(v+c[x0][y0]) + mX*c[x0][y0]) + mY*c[x0][y0];
        c[x1][y0] = (1.0-mY)*((1.0-mX)*c[x1][y0] + mX*(v+c[x1][y0])) + mY*c[x1][y0];
        c[x0][y1] = (1.0-mY)*c[x0][y1] + mY*((1.0-mX)*(v+c[x0][y1]) + mX*c[x0][y1]);
        c[x1][y1] = (1.0-mY)*c[x1][y1] + mY*((1.0-mX)*c[x1][y1] + mX*(v+c[x1][y1]));
        return this;
    }

    public Champ2V1 addi(Champ2V1 v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y] += v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1));
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
                c[x][y] -= v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1));
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
                c[x][y] *= v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1));
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
                c[x][y] /= v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1));
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

    public Champ2V1 diffuser(int largeur, double facteur){
        Champ2V1 tmp = new Champ2V1(this);
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                double vi = c[x][y];
                for (int x1 = -(largeur-1)/2; x1 <= (largeur-1)/2; x1++) {
                    for (int y1 = -(largeur-1)/2; y1 <= (largeur-1)/2; y1++) {
                        if(x1 == 0 && y1 == 0){continue;}
                        tmp.c[x][y] += c[Math.clamp(x1 + x,0,nX-1)][Math.clamp(y1 + y,0,nY-1)];
                    }
                }
                tmp.c[x][y] = tmp.c[x][y]*(facteur/(double)(largeur*largeur)) + vi*(1.0-facteur);
            }
        }
        copier(tmp);
        return this;
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
