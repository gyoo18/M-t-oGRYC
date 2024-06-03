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
        remplir(0);
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

        c[x0][y0] = (1.0-mX)*(1.0-mY)*v;
        c[x1][y0] = mX*(1.0-mY)*v;
        c[x0][y1] = (1.0-mX)*mY*v;
        c[x1][y1] = mX*mY*v;
        return this;
    }

    public Champ2V1 cA(double x, double y, double v){
        int x0 = Math.clamp((int)Math.floor(x*(double)(nX-1)),0, nX-1);
        int x1 = Math.clamp(x0 + 1,0, nX-1);
        int y0 = Math.clamp((int)Math.floor(y*(double)(nY-1)),0,nY-1);
        int y1 = Math.clamp(y0 + 1,0,nY-1);

        double mX = Math.clamp((x*(double)(nX-1)-(double)x0),0.0,1.0);
        double mY = Math.clamp((y*(double)(nY-1)-(double)y0),0.0,1.0);

        c[x0][y0] += (1.0-mX)*(1.0-mY)*v;
        c[x1][y0] += mX*(1.0-mY)*v;
        c[x0][y1] += (1.0-mX)*mY*v;
        c[x1][y1] += mX*mY*v;
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

    public Champ2V2 grad(Champ2V1 obstacle){
        Champ2V2 grad = new Champ2V2(L, H, nX, nY);
        double Dx = L/(double)nX;
        double Dy = H/(double)nY;
        for (int x = 0; x < grad.nX; x++) {
            for (int y = 0; y < grad.nY; y++) {
                double gradX1 = 0.0;
                double gradX0 = 0.0;
                double gradY1 = 0.0;
                double gradY0 = 0.0;
                double n_X = 0.0;
                double n_Y = 0.0;
                if(x > 0 && (obstacle == null || obstacle.c[x-1][y] > 0.001)){
                    gradX0 = (c[x][y] - c[x-1][y]);
                    n_X++;
                }
                if( x < nX-1 && (obstacle == null || obstacle.c[x+1][y] > 0.001)){
                    gradX1 = -(c[x][y] - c[x+1][y]);
                    n_X++;
                }
                if(y > 0 && (obstacle == null || obstacle.c[x][y-1] > 0.001)){
                    gradY0 = (c[x][y] - c[x][y-1]);
                    n_Y++;
                }
                if( y < nY-1 && (obstacle == null || obstacle.c[x][y+1] > 0.001)){
                    gradY1 = -(c[x][y] - c[x][y+1]);
                    n_Y++;
                }

                if(obstacle.c[x][y] > 0.001){
                    grad.c[x][y] = new Vecteur2D( (gradX0 + gradX1)/(n_X*Dx) , (gradY0 + gradY1)/(n_Y*Dy) );
                }else{
                    grad.c[x][y] = new Vecteur2D(0.0);
                }
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
        this.c = new double[v.nX][v.nY];
        for (int i = 0; i < v.nX; i++) {
            for (int j = 0; j < v.nY; j++) {
                this.c[i][j] = v.c[i][j];
            }
        }
    }

    public Champ2V1 copier(){
        return new Champ2V1(this);
    }
}
