public class Champ2V2 {
    public Vecteur2D[][] c;
    public Vecteur2D[][] cFX;
    public Vecteur2D[][] cFY;
    public int nX;
    public int nY;
    public double L;
    public double H;

    public boolean UtiliseFaces = false;

    public Champ2V2(double largeur, double hauteur, int nCasesX, int nCasesY){
        nX = nCasesX;
        nY = nCasesY;
        L = largeur;
        H = hauteur;
        c = new Vecteur2D[nX][nY];
        cFX = new Vecteur2D[nX+1][nY+1];
        cFY = new Vecteur2D[nX+1][nY+1];
        remplir(new Vecteur2D(0.0));
    }

    public Champ2V2(Champ2V2 v){
        copier(v);
    }

    public void remplir(Vecteur2D v){
        for (int x = 0; x < (UtiliseFaces?nX+1:nX); x++) {
            for (int y = 0; y < (UtiliseFaces?nY+1:nY); y++) {
                if(UtiliseFaces){
                    cFX[x][y] = v.copier();
                    cFY[x][y] = v.copier();
                }else{
                    c[x][y] = v.copier();
                }
            }
        }
    }

    /**
     * Renvoie un échantillon linéairement interpollé entre les quatres points définis les plus proches du point (x,y).
     * @param x - coordonée x en espace (0-1) du point d'échantillonage.
     * @param y - coordonée y en espace (0-1) du point d'échantillonage.
     * @return - Échantillon du champ.
     */
    public Vecteur2D c(double x, double y){
        if(!UtiliseFaces){
            int x0 = Math.clamp((int)Math.floor(x*(double)(nX-1)),0, nX-1);
            int x1 = Math.clamp(x0 + 1,0, nX-1);
            int y0 = Math.clamp((int)Math.floor(y*(double)(nY-1)),0,nY-1);
            int y1 = Math.clamp(y0 + 1,0,nY-1);

            double mX = Math.clamp((x*(double)(nX-1)-(double)x0),0.0,1.0);
            double mY = Math.clamp((y*(double)(nY-1)-(double)y0),0.0,1.0);

            Vecteur2D va = c[x0][y0].copier();
            Vecteur2D vb = c[x1][y0].copier();
            Vecteur2D vc = c[x0][y1].copier();
            Vecteur2D vd = c[x1][y1].copier();
            return V2.addi( V2.addi(va.mult(1.0 - mX), vb.mult(mX)).mult(1.0 - mY), V2.addi(vc.mult(1.0 - mX), vd.mult(mX)).mult(mY) );
        }else{
            //TODO #1 Trouver un algorithme d'interpolation en losange pour l'interpolation entre faces
            int x0 = Math.clamp((int)Math.floor(x*(double)(nX-1)),0, nX-1);
            int x1 = Math.clamp(x0 + 1,0, nX-1);
            int y0 = Math.clamp((int)Math.floor(y*(double)(nY-1)),0,nY-1);
            int y1 = Math.clamp(y0 + 1,0,nY-1);

            double mX = Math.clamp((x*(double)(nX-1)-(double)x0),0.0,1.0);
            double mY = Math.clamp((y*(double)(nY-1)-(double)y0),0.0,1.0);

            Vecteur2D v00 = V2.addi(V2.addi(cFX[x0][y0], cFX[x1][y0]),V2.addi(cFY[x0][y0], cFY[x0][y1])).mult(1.0/4.0);
            Vecteur2D v10 = V2.addi(V2.addi(cFX[x1][y0], cFX[x1+1][y0]),V2.addi(cFY[x1][y0], cFY[x1][y1])).mult(1.0/4.0);
            Vecteur2D v01 = V2.addi(V2.addi(cFX[x0][y1], cFX[x1][y1]),V2.addi(cFY[x0][y1], cFY[x0][y1+1])).mult(1.0/4.0);
            Vecteur2D v11 = V2.addi(V2.addi(cFX[x1][y1], cFX[x1+1][y1]),V2.addi(cFY[x1][y1], cFY[x1][y1+1])).mult(1.0/4.0);
            return V2.addi( V2.addi(v00.mult(1.0 - mX), v10.mult(mX)).mult(1.0 - mY), V2.addi(v01.mult(1.0 - mX), v11.mult(mX)).mult(mY) );
        }
    }

    public Vecteur2D c(int x, int y){
        if(!UtiliseFaces){
            return c[x][y];
        }else{ 
            return V2.addi(V2.addi(cFX[x][y], cFX[x+1][y]),V2.addi(cFY[x][y], cFY[x][y+1])).mult(1.0/4.0);
        }
    }

    public Champ2V2 c(double x, double y, Vecteur2D v){
        if(!UtiliseFaces){
            int x0 = Math.clamp((int)Math.floor(x*(double)(nX-1)),0, nX-1);
            int x1 = Math.clamp(x0 + 1,0, nX-1);
            int y0 = Math.clamp((int)Math.floor(y*(double)(nY-1)),0,nY-1);
            int y1 = Math.clamp(y0 + 1,0,nY-1);

            double mX = Math.clamp((x*(double)(nX-1)-(double)x0),0.0,1.0);
            double mY = Math.clamp((y*(double)(nY-1)-(double)y0),0.0,1.0);

            c[x0][y0] = v.copier().mult(1.0-mX).mult(1.0-mY);
            c[x1][y0] = v.copier().mult(mX).mult(1.0-mY);
            c[x0][y1] = v.copier().mult(1.0-mX).mult(mY);
            c[x1][y1] = v.copier().mult(mX).mult(mY);
            return this;
        }else{
            //TODO #1 Trouver un algorithme d'interpolation en losange pour l'interpolation entre faces
            int x0 = Math.clamp((int)Math.floor(x*(double)(nX-1)),0, nX-1);
            int x1 = Math.clamp(x0 + 1,0, nX-1);
            int y0 = Math.clamp((int)Math.floor(y*(double)(nY-1)),0,nY-1);
            int y1 = Math.clamp(y0 + 1,0,nY-1);

            double mX = Math.clamp((x*(double)(nX-1)-(double)x0),0.0,1.0);
            double mY = Math.clamp((y*(double)(nY-1)-(double)y0),0.0,1.0);

            c[x0][y0] = v.copier().mult(1.0-mX).mult(1.0-mY);
            c[x1][y0] = v.copier().mult(mX).mult(1.0-mY);
            c[x0][y1] = v.copier().mult(1.0-mX).mult(mY);
            c[x1][y1] = v.copier().mult(mX).mult(mY);

            cFX[x1][y0] = V2.addi(c[x0][y0], c[x1][y0]).mult(1.0/2.0);
            cFX[x1][y1] = V2.addi(c[x0][y1], c[x1][y1]).mult(1.0/2.0);
            cFY[x0][y1] = V2.addi(c[x0][y0], c[x0][y1]).mult(1.0/2.0);
            cFY[x1][y1] = V2.addi(c[x1][y0], c[x1][y1]).mult(1.0/2.0);
            return this;
        }
    }

    public Champ2V2 c(int x, int y, Vecteur2D v){
        if(!UtiliseFaces){
            c[x][y] = v;
            return this;
        }else{
            c[x][y] = v;
            if(x != 0){
                cFX[x][y] = V2.addi(c[x][y], c[x-1][y]).mult(1.0/2.0);
            }else{
                cFX[x][y] = V2.mult(c[x][y],new V2(0.0,1.0));
            }
            if(y != 0){
                cFY[x][y] = V2.addi(c[x][y], c[x][y-1]).mult(1.0/2.0);
            }else{
                cFY[x][y] = V2.mult(c[x][y],new V2(1.0,0.0));
            }
            if(x != nX-1){
                cFX[x+1][y] = V2.addi(c[x][y], c[x+1][y]).mult(1.0/2.0);
            }else{
                cFX[x+1][y] = V2.mult(c[x][y],new V2(0.0,1.0));
            }
            if(y != nY-1){
                cFY[x][y+1] = V2.addi(c[x][y], c[x][y+1]).mult(1.0/2.0);
            }else{
                cFY[x][y+1] = V2.mult(c[x][y],new V2(1.0,0.0));
            }
            return this;
        }
    }

    public Champ2V2 cA(double x, double y, Vecteur2D v){
        if(!UtiliseFaces){
            int x0 = Math.clamp((int)Math.floor(x*(double)(nX-1)),0, nX-1);
            int x1 = Math.clamp(x0 + 1,0, nX-1);
            int y0 = Math.clamp((int)Math.floor(y*(double)(nY-1)),0,nY-1);
            int y1 = Math.clamp(y0 + 1,0,nY-1);

            double mX = Math.clamp((x*(double)(nX-1)-(double)x0),0.0,1.0);
            double mY = Math.clamp((y*(double)(nY-1)-(double)y0),0.0,1.0);

            c[x0][y0].addi( v.copier().mult(1.0-mX).mult(1.0-mY) );
            c[x1][y0].addi( v.copier().mult(mX).mult(1.0-mY) );
            c[x0][y1].addi( v.copier().mult(1.0-mX).mult(mY) );
            c[x1][y1].addi( v.copier().mult(mX).mult(mY) );
            return this;
        }else{
            //TODO #1 Trouver un algorithme d'interpolation en losange pour l'interpolation entre faces
            int x0 = Math.clamp((int)Math.floor(x*(double)(nX-1)),0, nX-1);
            int x1 = Math.clamp(x0 + 1,0, nX-1);
            int y0 = Math.clamp((int)Math.floor(y*(double)(nY-1)),0,nY-1);
            int y1 = Math.clamp(y0 + 1,0,nY-1);

            double mX = Math.clamp((x*(double)(nX-1)-(double)x0),0.0,1.0);
            double mY = Math.clamp((y*(double)(nY-1)-(double)y0),0.0,1.0);

            cA(x0,y0,v.copier().mult(1.0-mX).mult(1.0-mY));
            cA(x1,y0,v.copier().mult(mX).mult(1.0-mY));
            cA(x0,y1,v.copier().mult(1.0-mX).mult(mY));
            cA(x1,y1,v.copier().mult(mX).mult(mY));

            //cFX[x1][y0] = V2.addi(c[x0][y0], c[x1][y0]).mult(1.0/2.0);
            //cFX[x1][y1] = V2.addi(c[x0][y1], c[x1][y1]).mult(1.0/2.0);
            //cFY[x0][y1] = V2.addi(c[x0][y0], c[x0][y1]).mult(1.0/2.0);
            //cFY[x1][y1] = V2.addi(c[x1][y0], c[x1][y1]).mult(1.0/2.0);
            return this;
        }
    }

    public Champ2V2 cA(int x, int y, Vecteur2D v){
        if(!UtiliseFaces){
            c[x][y].addi(v);
            return this;
        }else{
            c[x][y].addi(v);
            if(x != 0){
                cFX[x][y] = V2.addi(c[x][y], c[x-1][y]).mult(1.0/2.0);
            }else{
                cFX[x][y] = V2.mult(c[x][y],new V2(0.0,1.0));
            }
            if(y != 0){
                cFY[x][y] = V2.addi(c[x][y], c[x][y-1]).mult(1.0/2.0);
            }else{
                cFY[x][y] = V2.mult(c[x][y],new V2(1.0,0.0));
            }
            if(x != nX-1){
                cFX[x+1][y] = V2.addi(c[x][y], c[x+1][y]).mult(1.0/2.0);
            }else{
                cFX[x+1][y] = V2.mult(c[x][y],new V2(0.0,1.0));
            }
            if(y != nY-1){
                cFY[x][y+1] = V2.addi(c[x][y], c[x][y+1]).mult(1.0/2.0);
            }else{
                cFY[x][y+1] = V2.mult(c[x][y],new V2(1.0,0.0));
            }
            return this;
        }
    }

    public Champ2V2 addi(Champ2V2 v){
        if(!UtiliseFaces){
            for (int x = 0; x < nX; x++) {
                for (int y = 0; y < nY; y++) {
                    c[x][y].addi( v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1)) );
                }
            }
        }else{
            if(v.nX == nX && v.nY == nY && v.UtiliseFaces){
                for (int x = 0; x < nX+1; x++) {
                    for (int y = 0; y < nY+1; y++) {
                        cFX[x][y].addi( v.cFX[x][y] );
                        cFY[x][y].addi( v.cFY[x][y] );
                    }
                }
                for (int x = 0; x < nX; x++) {
                    for (int y = 0; y < nY; y++) {
                        c[x][y] = V2.addi(V2.addi(cFX[x][y],cFX[x+1][y]),V2.addi(cFY[x][y], cFY[x][y+1])).mult(1.0/4.0);
                    }
                }
            }else{
                for (int x = 0; x < nX; x++) {
                    for (int y = 0; y < nY; y++) {
                        c[x][y].addi( v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1)) );
                    }
                }
                for (int x = 0; x < nX+1; x++) {
                    for (int y = 0; y < nY+1; y++) {
                        if(x != 0){
                            cFX[x][y] = V2.addi(c[x][y], c[x-1][y]).mult(1.0/2.0);
                        }else{
                            cFX[x][y] = c[x][y];
                        }
                        if(y != 0){
                            cFY[x][y] = V2.addi(c[x][y], c[x][y-1]).mult(1.0/2.0);
                        }else{
                            cFY[x][y] = c[x][y];
                        }
                        cFX[x+1][y] = V2.addi(c[x][y], c[x+1][y]).mult(1.0/2.0);
                        cFY[x][y+1] = V2.addi(c[x][y], c[x][y+1]).mult(1.0/2.0);
                    }
                }
            }
        }

        return this;
    }
    
    public Champ2V2 addi(Vecteur2D v){
        for (int x = 0; x < nX; x++) {
            for (int y = 0; y < nY; y++) {
                c[x][y].addi(v);
            }
        }
        if(UtiliseFaces){
            for (int x = 0; x < nX+1; x++) {
                for (int y = 0; y < nY+1; y++) {
                    cFX[x][y].addi(v);
                    cFY[x][y].addi(v);
                }
            }
        }

        return this;
    }

    public Champ2V2 sous(Champ2V2 v){
        if(!UtiliseFaces){
            for (int x = 0; x < c.length; x++) {
                for (int y = 0; y < c[x].length; y++) {
                    c[x][y].sous( v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1)) );
                }
            }
        }else{
            if(v.nX == nX && v.nY == nY && v.UtiliseFaces){
                for (int x = 0; x < nX+1; x++) {
                    for (int y = 0; y < nY+1; y++) {
                        cFX[x][y].sous( v.cFX[x][y] );
                        cFY[x][y].sous( v.cFY[x][y] );
                    }
                }
                for (int x = 0; x < nX; x++) {
                    for (int y = 0; y < nY; y++) {
                        c[x][y] = V2.addi(V2.addi(cFX[x][y],cFX[x+1][y]),V2.addi(cFY[x][y], cFY[x][y+1])).mult(1.0/4.0);
                    }
                }
            }else{
                for (int x = 0; x < nX; x++) {
                    for (int y = 0; y < nY; y++) {
                        c[x][y].sous( v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1)) );
                    }
                }
                for (int x = 0; x < nX+1; x++) {
                    for (int y = 0; y < nY+1; y++) {
                        if(x != 0){
                            cFX[x][y] = V2.addi(c[x][y], c[x-1][y]).mult(1.0/2.0);
                        }else{
                            cFX[x][y] = c[x][y];
                        }
                        if(y != 0){
                            cFY[x][y] = V2.addi(c[x][y], c[x][y-1]).mult(1.0/2.0);
                        }else{
                            cFY[x][y] = c[x][y];
                        }
                        cFX[x+1][y] = V2.addi(c[x][y], c[x+1][y]).mult(1.0/2.0);
                        cFY[x][y+1] = V2.addi(c[x][y], c[x][y+1]).mult(1.0/2.0);
                    }
                }
            }
        }
        return this;
    }

    public Champ2V2 sous(Vecteur2D v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y].sous(v);
            }
        }
        if(UtiliseFaces){
            for (int x = 0; x < nX+1; x++) {
                for (int y = 0; y < nY+1; y++) {
                    cFX[x][y].sous(v);
                    cFY[x][y].sous(v);
                }
            }
        }

        return this;
    }

    public Champ2V2 mult(Champ2V2 v){
        if(!UtiliseFaces){
            for (int x = 0; x < c.length; x++) {
                for (int y = 0; y < c[x].length; y++) {
                    c[x][y].mult( v.c((double)x/(double)v.nX,(double)y/(double)v.nY) );
                }
            }
        }else{
            if(v.nX == nX && v.nY == nY && v.UtiliseFaces){
                for (int x = 0; x < nX+1; x++) {
                    for (int y = 0; y < nY+1; y++) {
                        cFX[x][y].mult( v.cFX[x][y] );
                        cFY[x][y].mult( v.cFY[x][y] );
                    }
                }
                for (int x = 0; x < nX; x++) {
                    for (int y = 0; y < nY; y++) {
                        c[x][y] = V2.addi(V2.addi(cFX[x][y],cFX[x+1][y]),V2.addi(cFY[x][y], cFY[x][y+1])).mult(1.0/4.0);
                    }
                }
            }else{
                for (int x = 0; x < nX; x++) {
                    for (int y = 0; y < nY; y++) {
                        c[x][y].mult( v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1)) );
                    }
                }
                for (int x = 0; x < nX+1; x++) {
                    for (int y = 0; y < nY+1; y++) {
                        if(x != 0){
                            cFX[x][y] = V2.addi(c[x][y], c[x-1][y]).mult(1.0/2.0);
                        }else{
                            cFX[x][y] = c[x][y];
                        }
                        if(y != 0){
                            cFY[x][y] = V2.addi(c[x][y], c[x][y-1]).mult(1.0/2.0);
                        }else{
                            cFY[x][y] = c[x][y];
                        }
                        cFX[x+1][y] = V2.addi(c[x][y], c[x+1][y]).mult(1.0/2.0);
                        cFY[x][y+1] = V2.addi(c[x][y], c[x][y+1]).mult(1.0/2.0);
                    }
                }
            }
        }

        return this;
    }

    public Champ2V2 mult(Vecteur2D v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y].mult(v);
            }
        }
        if(UtiliseFaces){
            for (int x = 0; x < nX+1; x++) {
                for (int y = 0; y < nY+1; y++) {
                    cFX[x][y].mult(v);
                    cFY[x][y].mult(v);
                }
            }
        }

        return this;
    }

    public Champ2V2 div(Champ2V2 v){
        if(!UtiliseFaces){
            for (int x = 0; x < c.length; x++) {
                for (int y = 0; y < c[x].length; y++) {
                    c[x][y].div( v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1)) );
                }
            }
        }else{
            if(v.nX == nX && v.nY == nY && v.UtiliseFaces){
                for (int x = 0; x < nX+1; x++) {
                    for (int y = 0; y < nY+1; y++) {
                        cFX[x][y].div( v.cFX[x][y] );
                        cFY[x][y].div( v.cFY[x][y] );
                    }
                }
                for (int x = 0; x < nX; x++) {
                    for (int y = 0; y < nY; y++) {
                        c[x][y] = V2.addi(V2.addi(cFX[x][y],cFX[x+1][y]),V2.addi(cFY[x][y], cFY[x][y+1])).mult(1.0/4.0);
                    }
                }
            }else{
                for (int x = 0; x < nX; x++) {
                    for (int y = 0; y < nY; y++) {
                        c[x][y].div( v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1)) );
                    }
                }
                for (int x = 0; x < nX+1; x++) {
                    for (int y = 0; y < nY+1; y++) {
                        if(x != 0){
                            cFX[x][y] = V2.addi(c[x][y], c[x-1][y]).mult(1.0/2.0);
                        }else{
                            cFX[x][y] = c[x][y];
                        }
                        if(y != 0){
                            cFY[x][y] = V2.addi(c[x][y], c[x][y-1]).mult(1.0/2.0);
                        }else{
                            cFY[x][y] = c[x][y];
                        }
                        cFX[x+1][y] = V2.addi(c[x][y], c[x+1][y]).mult(1.0/2.0);
                        cFY[x][y+1] = V2.addi(c[x][y], c[x][y+1]).mult(1.0/2.0);
                    }
                }
            }
        }

        return this;
    }

    public Champ2V2 div(Vecteur2D v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y].div(v);
            }
        }
        if(UtiliseFaces){
            for (int x = 0; x < nX+1; x++) {
                for (int y = 0; y < nY+1; y++) {
                    cFX[x][y].div(v);
                    cFY[x][y].div(v);
                }
            }
        }

        return this;
    }

    public Champ2V1 Div(Champ2V1 obstacle){
        Champ2V1 div = new Champ2V1(L, H, nX, nY);
        double Dx = L/(double)nX;
        double Dy = H/(double)nY;
        if(!UtiliseFaces){
            for (int x = 0; x < nX; x++) {
                for (int y = 0; y < nY; y++) {
                    double gradX1 = 0.0;
                    double gradX0 = 0.0;
                    double gradY1 = 0.0;
                    double gradY0 = 0.0;
                    double n = 0;
                    if(x > 0 && (obstacle == null || obstacle.c[x-1][y] > 0.001)){
                        gradX0 = V2.scal(V2.addi(c[x-1][y],c[x][y]).mult(0.5),new V2(-1.0,0.0)) * Dy;
                        n++;
                    }
                    if( x < nX-1 && (obstacle == null || obstacle.c[x+1][y] > 0.001)){
                        gradX1 = V2.scal(V2.addi(c[x+1][y],c[x][y]).mult(0.5),new V2(1.0,0.0)) * Dy;
                        n++;
                    }
                    if(y > 0 && (obstacle == null || obstacle.c[x][y-1] > 0.001)){
                        gradY0 = V2.scal(V2.addi(c[x][y-1],c[x][y]).mult(0.5),new V2(0.0,-1.0)) * Dx;
                        n++;
                    }
                    if( y < nY-1 && (obstacle == null || obstacle.c[x][y+1] > 0.001)){
                        gradY1 = V2.scal(V2.addi(c[x][y+1],c[x][y]).mult(0.5),new V2(0.0,1.0)) * Dx;
                        n++;
                    }

                    if(obstacle.c[x][y] > 0.001){
                        div.c[x][y] = (gradX0 + gradX1 + gradY0 + gradY1)/(Dx*Dy);
                    }else{
                        div.c[x][y] = 0.0;
                    }
                }
            }
        }else{
            for (int x = 0; x < nX; x++) {
                for (int y = 0; y < nY; y++) {
                    double gradX1 = 0.0;
                    double gradX0 = 0.0;
                    double gradY1 = 0.0;
                    double gradY0 = 0.0;
                    double n = 0.0;
                    if(x > 0 && (obstacle == null || obstacle.c[x-1][y] > 0.001)){
                        gradX0 = V2.scal(cFX[x][y],new V2(-1.0,0.0)) * Dy;
                        n++;
                    }
                    if(x > 0 && (obstacle == null || obstacle.c[x-1][y] > 0.001)){
                        gradX1 = V2.scal(cFX[x+1][y],new V2(1.0,0.0)) * Dy;
                        n++;
                    }
                    if(y > 0 && (obstacle == null || obstacle.c[x][y-1] > 0.001)){
                        gradY0 = V2.scal(cFY[x][y],new V2(0.0,-1.0)) * Dx;
                        n++;
                    }
                    if( y < nY-1 && (obstacle == null || obstacle.c[x][y+1] > 0.001)){
                        gradY1 = V2.scal(cFY[x][y+1],new V2(0.0,1.0)) * Dx;
                        n++;
                    }

                    if(obstacle.c[x][y] > 0.001){
                        div.c[x][y] = (gradX0 + gradX1 + gradY0 + gradY1)/(n*Dx*Dy);
                    }else{
                        div.c[x][y] = 0.0;
                    }
                    if(Double.isNaN(div.c[x][y]) || Double.isInfinite(div.c[x][y])){
                        System.currentTimeMillis();
                    }
                }
            }
        }

        return div;
    }

    public Champ2V1 rot(){
        //TODO #2 adapter rot à faces
        Champ2V1 rot = new Champ2V1(L, H, nY, nX);
        double Dx = L/(double)nX;
        double Dy = H/(double)nY;
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                double gradX1 = 0;
                double gradX0 = 0;
                double gradY1 = 0;
                double gradY0 = 0;
                if(x + 1 < c.length && x - 1 >= 0){
                    gradX1 = (c[x][y].x - c[x+1][y].x)/Dx;
                    gradX0 = (c[x][y].x - c[x-1][y].x)/-Dx;
                }else if(x + 1 >= c.length){
                    gradX0 = (c[x][y].x - c[x-1][y].x)/-Dx;
                    gradX1 = gradX0;
                }else if( x == 0){
                    gradX1 = (c[x][y].x - c[x+1][y].x)/Dx;
                    gradX0 = gradX1;
                }
                if(y + 1 < c[y].length && y - 1 >= 0){
                    gradY1 = (c[x][y].y - c[x][y+1].y)/Dy;
                    gradY0 = (c[x][y].y - c[x][y-1].y)/-Dy;
                }else if(y + 1 >= c[x].length){
                    gradY0 = (c[x][y].y - c[x][y-1].y)/-Dy;
                    gradY1 = gradY0;
                }else if( y == 0){
                    gradY1 = (c[x][y].y - c[x][y+1].y)/Dy;
                    gradY0 = gradY1;
                }

                rot.c[x][y] = (gradX0 + gradX1)/2.0 - (gradY0 + gradY1)/2.0;
            }
        }

        return rot;
    }

    public static Champ2V2 addi(Champ2V2 u, Champ2V2 v){
        //TODO #3 adapter addi static à faces
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].addi( v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1)) );
            }
        }

        return r;
    }
    
    public static Champ2V2 addi(Champ2V2 u, Vecteur2D v){
        //TODO #3 adapter addi static à faces
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].addi(v);
            }
        }

        return r;
    }

    public static Champ2V2 sous(Champ2V2 u, Champ2V2 v){
        //TODO #3 adapter sous static à faces
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].sous( v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1)) );
            }
        }

        return r;
    }

    public static Champ2V2 sous(Champ2V2 u, Vecteur2D v){
        //TODO #3 adapter sous static à faces
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].sous(v);
            }
        }

        return r;
    }

    public static Champ2V2 mult(Champ2V2 u, Champ2V2 v){
        //TODO #3 adapter mult static à faces
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].mult( v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1)) );
            }
        }

        return r;
    }

    public static Champ2V2 mult(Champ2V2 u, Vecteur2D v){
        //TODO #3 adapter mult static à faces
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].mult(v);
            }
        }

        return r;
    }

    public static Champ2V2 div(Champ2V2 u, Champ2V2 v){
        //TODO #3 adapter div static à faces
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].div( v.c((double)x/(double)(v.nX-1),(double)y/(double)(v.nY-1)) );
            }
        }

        return r;
    }

    public static Champ2V2 div(Champ2V2 u, Vecteur2D v){
        //TODO #3 adapter div static à faces
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].div(v);
            }
        }

        return r;
    }

    public static Champ2V1 Div(Champ2V2 u, Champ2V1 obstacle){
        //TODO #3 adapter Div static à faces
        Champ2V1 div = new Champ2V1(u.L, u.H, u.nY, u.nX);
        double Dx = u.L/(double)u.nX;
        double Dy = u.H/(double)u.nY;
        for (int x = 0; x < u.c.length; x++) {
            for (int y = 0; y < u.c[x].length; y++) {
                double gradX1 = 0;
                double gradX0 = 0;
                double gradY1 = 0;
                double gradY0 = 0;
                double n = 0.0;
                if(x >= u.nX-1){
                    gradX0 = (u.c[x][y].x - u.c[x-1][y].x)/-Dx;
                    n++;
                }
                if( x == 0){
                    gradX1 = (u.c[x][y].x - u.c[x+1][y].x)/Dx;
                    n++;
                }
                if(y + 1 >= u.c[x].length){
                    gradY0 = (u.c[x][y].y - u.c[x][y-1].y)/-Dy;
                    n++;
                }
                if( y == 0){
                    gradY1 = (u.c[x][y].y - u.c[x][y+1].y)/Dy;
                    n++;
                }

                div.c[x][y] = (gradX0 + gradX1 + gradY0 + gradY1)/n;
            }
        }

        return div;
    }

    public static Champ2V1 rot(Champ2V2 u){
        //TODO #3 adapter rot static à faces
        Champ2V1 rot = new Champ2V1(u.L, u.H, u.nY, u.nX);
        double Dx = u.L/(double)u.nX;
        double Dy = u.H/(double)u.nY;
        for (int x = 0; x < u.c.length; x++) {
            for (int y = 0; y < u.c[x].length; y++) {
                double gradX1 = 0;
                double gradX0 = 0;
                double gradY1 = 0;
                double gradY0 = 0;
                if(x + 1 < u.c.length && x - 1 >= 0){
                    gradX1 = (u.c[x][y].x - u.c[x+1][y].x)/Dx;
                    gradX0 = (u.c[x][y].x - u.c[x-1][y].x)/-Dx;
                }else if(x + 1 >= u.c.length){
                    gradX0 = (u.c[x][y].x - u.c[x-1][y].x)/-Dx;
                    gradX1 = gradX0;
                }else if( x == 0){
                    gradX1 = (u.c[x][y].x - u.c[x+1][y].x)/Dx;
                    gradX0 = gradX1;
                }
                if(y + 1 < u.c[y].length && y - 1 >= 0){
                    gradY1 = (u.c[x][y].y - u.c[x][y+1].y)/Dy;
                    gradY0 = (u.c[x][y].y - u.c[x][y-1].y)/-Dy;
                }else if(y + 1 >= u.c[x].length){
                    gradY0 = (u.c[x][y].y - u.c[x][y-1].y)/-Dy;
                    gradY1 = gradY0;
                }else if( y == 0){
                    gradY1 = (u.c[x][y].y - u.c[x][y+1].y)/Dy;
                    gradY0 = gradY1;
                }

                rot.c[x][y] = (gradX0 + gradX1)/2.0 - (gradY0 + gradY1)/2.0;
            }
        }

        return rot;
    }

    public Champ2V2 diffuser(int largeur, double facteur){
        //TODO #3 adapter diffuser static à faces
        Champ2V2 tmp = new Champ2V2(this);
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                Vecteur2D vi = c[x][y].copier();
                for (int x1 = -(largeur-1)/2; x1 <= (largeur-1)/2; x1++) {
                    for (int y1 = -(largeur-1)/2; y1 <= (largeur-1)/2; y1++) {
                        if(x1 == 0 && y1 == 0){continue;}
                        tmp.c[x][y].addi(c[Math.clamp(x1 + x,0,nX-1)][Math.clamp(y1 + y,0,nY-1)]);
                    }
                }
                tmp.c[x][y].mult(facteur/(double)(largeur*largeur)).addi(vi.mult(1.0-facteur));
            }
        }
        copier(tmp);
        return this;
    }

    public Champ2V2 retirerDiv(int iter, Champ2V1 obstacle){
        //if()
        for (int i = 0; i < iter; i++) {
            double Dx = L/(double)nX;
            double Dy = H/(double)nY;
            for (int x = 0; x < nX; x++) {
                for (int y = 0; y < nY; y++) {
                    double gradX1 = 0.0;
                    double gradX0 = 0.0;
                    double gradY1 = 0.0;
                    double gradY0 = 0.0;
                    double n = 0;
                    if(x > 0 && (obstacle == null || obstacle.c[x-1][y] > 0.001)){
                        gradX0 = V2.scal(V2.addi(c(x-1,y),c(x,y)).mult(0.5),new V2(-1.0,0.0)) * Dy;
                        n++;
                    }
                    if( x < nX-1 && (obstacle == null || obstacle.c[x+1][y] > 0.001)){
                        gradX1 = V2.scal(V2.addi(c(x+1,y),c(x,y)).mult(0.5),new V2(1.0,0.0)) * Dy;
                        n++;
                    }
                    if(y > 0 && (obstacle == null || obstacle.c[x][y-1] > 0.001)){
                        gradY0 = V2.scal(V2.addi(c(x,y-1),c(x,y)).mult(0.5),new V2(0.0,-1.0)) * Dx;
                        n++;
                    }
                    if( y < nY-1 && (obstacle == null || obstacle.c[x][y+1] > 0.001)){
                        gradY1 = V2.scal(V2.addi(c(x,y+1),c(x,y)).mult(0.5),new V2(0.0,1.0)) * Dx;
                        n++;
                    }

                    Double D = 2.0*(gradX0 + gradX1 + gradY0 + gradY1)/n;

                    if(x != 0 && (obstacle == null || obstacle.c[x-1][y] > 0.001)){
                        c(x-1,y).sous(new V2(-1.0,0.0).mult(D/Dy));
                    }
                    if(x != nX-1 && (obstacle == null || obstacle.c[x+1][y] > 0.001)){
                        c(x+1,y).sous(new V2(1.0,0.0).mult(D/Dy));
                    }
                    if(y != 0 && (obstacle == null || obstacle.c[x][y-1] > 0.001)){
                        c(x,y-1).sous(new V2(0.0,-1.0).mult(D/Dx));
                    }
                    if(y != nY-1 && (obstacle == null || obstacle.c[x][y+1] > 0.001)){
                        c(x,y+1).sous(new V2(0.0,1.0).mult(D/Dx));
                    }
                    if(obstacle != null && obstacle.c[x][y] < 0.001){
                        c(x,y, new Vecteur2D(0.0));
                    }
                }
            }
        }
        return this;
    }

    public void copier(Champ2V2 v){
        this.nX = v.nX;
        this.nY = v.nY;
        this.L = v.L;
        this.H = v.H;
        this.UtiliseFaces = v.UtiliseFaces;
        this.c = new Vecteur2D[nX][nY];
        this.cFX = new Vecteur2D[nX+1][nY+1];
        this.cFY = new Vecteur2D[nX+1][nY+1];
        for (int x = 0; x < nX; x++) {
            for (int y = 0; y < nY; y++) {
                this.c[x][y] = v.c[x][y].copier();
            }
        }
        for (int x = 0; x < nX+1; x++) {
            for (int y = 0; y < nY+1; y++) {
                this.cFX[x][y] = v.cFX[x][y].copier();
                this.cFY[x][y] = v.cFY[x][y].copier();
            }
        }
    }

    public Champ2V2 copier(){
        return new Champ2V2(this);
    }
}
