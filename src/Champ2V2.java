public class Champ2V2 {
    public Vecteur2D[][] c;
    public int nX;
    public int nY;
    public double L;
    public double H;

    public Champ2V2(double largeur, double hauteur, int nCasesX, int nCasesY){
        nX = nCasesX;
        nY = nCasesY;
        L = largeur;
        H = hauteur;
        c = new Vecteur2D[nX][nY];
    }

    public Champ2V2(Champ2V2 v){
        copier(v);
    }

    public void remplir(Vecteur2D v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y] = v.copier();
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
    }

    public Champ2V2 addi(Champ2V2 v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y].addi( v.c((double)x/(double)v.nX,(double)y/(double)v.nY) );
            }
        }

        return this.copier();
    }
    
    public Champ2V2 addi(Vecteur2D v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y].addi(v);
            }
        }

        return this.copier();
    }

    public Champ2V2 sous(Champ2V2 v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y].sous( v.c((double)x/(double)v.nX,(double)y/(double)v.nY) );
            }
        }

        return this.copier();
    }

    public Champ2V2 sous(Vecteur2D v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y].sous(v);
            }
        }

        return this.copier();
    }

    public Champ2V2 mult(Champ2V2 v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y].mult( v.c((double)x/(double)v.nX,(double)y/(double)v.nY) );
            }
        }

        return this.copier();
    }

    public Champ2V2 mult(Vecteur2D v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y].mult(v);
            }
        }

        return this.copier();
    }

    public Champ2V2 div(Champ2V2 v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y].div( v.c((double)x/(double)v.nX,(double)y/(double)v.nY) );
            }
        }

        return this.copier();
    }

    public Champ2V2 div(Vecteur2D v){
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y].div(v);
            }
        }

        return this.copier();
    }

    public Champ2V1 Div(){
        Champ2V1 div = new Champ2V1(L, H, nY, nX);
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

                div.c[x][y] = (gradX0 + gradX1)/2.0 + (gradY0 + gradY1)/2.0;
            }
        }

        return div;
    }

    public Champ2V1 rot(){
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
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].addi( v.c((double)x/(double)v.nX,(double)y/(double)v.nY) );
            }
        }

        return r;
    }
    
    public static Champ2V2 addi(Champ2V2 u, Vecteur2D v){
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].addi(v);
            }
        }

        return r;
    }

    public static Champ2V2 sous(Champ2V2 u, Champ2V2 v){
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].sous( v.c((double)x/(double)v.nX,(double)y/(double)v.nY) );
            }
        }

        return r;
    }

    public static Champ2V2 sous(Champ2V2 u, Vecteur2D v){
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].sous(v);
            }
        }

        return r;
    }

    public static Champ2V2 mult(Champ2V2 u, Champ2V2 v){
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].mult( v.c((double)x/(double)v.nX,(double)y/(double)v.nY) );
            }
        }

        return r;
    }

    public static Champ2V2 mult(Champ2V2 u, Vecteur2D v){
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].mult(v);
            }
        }

        return r;
    }

    public static Champ2V2 div(Champ2V2 u, Champ2V2 v){
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].div( v.c((double)x/(double)v.nX,(double)y/(double)v.nY) );
            }
        }

        return r;
    }

    public static Champ2V2 div(Champ2V2 u, Vecteur2D v){
        Champ2V2 r = new Champ2V2(u);
        for (int x = 0; x < r.c.length; x++) {
            for (int y = 0; y < r.c[x].length; y++) {
                r.c[x][y].div(v);
            }
        }

        return r;
    }

    public static Champ2V1 Div(Champ2V2 u){
        Champ2V1 div = new Champ2V1(u.L, u.H, u.nY, u.nX);
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

                if(gradX0 == 0 || gradX1 == 0 || gradY0 == 0 || gradY1 == 0){
                    System.currentTimeMillis();
                }

                div.c[x][y] = (gradX0 + gradX1)/2.0 + (gradY0 + gradY1)/2.0;
            }
        }

        return div;
    }

    public static Champ2V1 rot(Champ2V2 u){
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

    public void copier(Champ2V2 v){
        this.nX = v.nX;
        this.nY = v.nY;
        this.L = v.L;
        this.H = v.H;
        this.c = new Vecteur2D[nX][nY];
        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                this.c[x][y] = v.c[x][y].copier();
            }
        }
    }

    public Champ2V2 copier(){
        return new Champ2V2(this);
    }
}
