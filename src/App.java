import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import java.awt.RenderingHints;
import java.awt.Stroke;

public class App {
    public static int TailleX = 512;
    public static int TailleY = 512;
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        BufferedImage b = new BufferedImage(TailleX, TailleY, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) b.getGraphics();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(TailleX + 100,TailleY + 100);
        frame.setVisible(true);
        JLabel image = new JLabel( new ImageIcon(b));
        frame.add(image);

        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g.setStroke(new BasicStroke());
        g.setColor(Color.red);
        g.fillRect(0, 0, 100, 100);

        Champ2V2 vélocité = new Champ2V2(TailleX, TailleY, 256, 256);
        vélocité.remplir(new Vecteur2D(0));
        //for (int x = 0; x < vélocité.c.length; x++) {
        //    for (int y = 0; y < vélocité.c[x].length; y++) {
        //        vélocité.c[x][y] = new Vecteur2D(((double)x/(double)vélocité.nX - 0.5),((double)y/(double)vélocité.nY - 0.5)).mult(10.0);
        //    }
        //}
        //Champ2V1 pression = new Champ2V1(TailleX, TailleY, 40,40);
        //pression.remplir(0.0);
        Vecteur2D dir = new Vecteur2D(0,10000.0);
        vélocité.c[vélocité.nX/2][vélocité.nY/2] = dir.rot(0.001);
        Champ2V1 fumée = new Champ2V1(TailleX, TailleY,256,256);
        fumée.remplir(0.0);
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                //fumée.c[fumée.nX/2 + x][fumée.nY/2 + y] = 10.0;
            }
        }

        while (true) {
            for (int N = 0; N  < 1; N ++) {
                for (int x = 0; x < vélocité.nX; x++) {
                    for (int y = 0; y < vélocité.nY; y++) {
                        if( x == 0 || y == 0 || x == vélocité.nX-1 || y == vélocité.nY-1){
                            vélocité.c[x][y] = new Vecteur2D(1000.0,0.0);
                        }
                    }
                }
                vélocité.c[vélocité.nX/2][vélocité.nY/2] = dir.rot(0.003);
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        fumée.c[fumée.nX/2 + x][fumée.nY/2 + y] = 10.0;
                    }
                }
                vélocité.diffuser(3,0.05);
                vélocité.retirerDiv();
                vélocité.addi(vélocité.copier().Div().grad().mult(new Vecteur2D(1.0)));
                for (int N2 = 0; N2 < 1; N2++) {
                    Champ2V2 tmp = new Champ2V2(vélocité);
                    //tmp.remplir(new Vecteur2D(0));
                    for (int x = 0; x < vélocité.nX; x++) {
                        for (int y = 0; y < vélocité.nY; y++) {
                            Vecteur2D vel = vélocité.c((double)x/(double)(vélocité.nX-1), (double)y/(double)(vélocité.nY-1)).mult(0.002/1.0);
                            double éX = ((double)x/(double)(vélocité.nX-1))-((double)vel.x/vélocité.L);
                            double éY = ((double)y/(double)(vélocité.nY-1))-((double)vel.y/vélocité.H);
                            if(éX >= 0.0 && éX <= 1.0 && éY >= 0.0 && éY <= 1.0){
                                tmp.c[x][y] = vélocité.c(éX,éY);
                            }else{
                                tmp.c[x][y] = new Vecteur2D(0.0);
                            }
                            //tmp.c(((double)x/(double)(vélocité.nX-1))+((double)vel.x/vélocité.L),((double)y/(double)(vélocité.nY-1))+((double)vel.y/vélocité.H), tmp.c[x][y]);
                        }
                    }
                    vélocité.copier(tmp);
                    Champ2V1 tmp2 = new Champ2V1(fumée);
                    //tmp2.remplir(0);
                    for (int x = 0; x < fumée.nX; x++) {
                        for (int y = 0; y < fumée.nY; y++) {
                            Vecteur2D vel = vélocité.c((double)x/(double)(vélocité.nX-1), (double)y/(double)(vélocité.nY-1)).mult(0.002/1.0);
                            double éX = ((double)x/(double)(fumée.nX-1))-((double)vel.x/fumée.L);
                            double éY = ((double)y/(double)(fumée.nY-1))-((double)vel.y/fumée.H);
                            if(éX >= 0.0 && éX <= 1.0 && éY >= 0.0 && éY <= 1.0){
                                tmp2.c[x][y] = fumée.c(éX,éY);
                            }else{
                                tmp2.c[x][y] = 0.0;
                            }
                            //tmp2.cA(((double)x/(double)(fumée.nX-1))+((double)vel.x/fumée.L),((double)y/(double)(fumée.nY-1))+((double)vel.y/fumée.H), fumée.c[x][y]);
                            //tmp2.c[x][y] -= fumée.c[x][y];
                        }
                    }
                    fumée.copier(tmp2);
                }
                fumée.diffuser(3, 0.01);
            }
            dessinerChamp(fumée, 3, g, frame);
            SwingUtilities.updateComponentTreeUI(frame);
            //Thread.sleep(30);
        }
    }

    public static void dessinerChamp(Champ2V2 champ, int saut, Graphics2D g, JFrame frame){
        double cL = (double)TailleX/(double)champ.nX;
        double cH = (double)TailleY/(double)champ.nX;
        for (int x = 0; x < champ.nX; x++) {
            for (int y = 0; y < champ.nY; y++) {
                g.setColor(new Color((int)((1.0-1.0/(Math.abs(champ.c[x][y].x)+1.0))*255.0),(int)((1.0-1.0/(Math.abs(champ.c[x][y].y)+1.0))*255.0),0));
                g.fillRect((int)((double)x*cL), TailleY - (int)((double)(y+1)*cH), (int)Math.ceil(cL), (int)Math.ceil(cH));
                //SwingUtilities.updateComponentTreeUI(frame);
            }
        }
        cL = (double)saut*(double)TailleX/(double)champ.nX;
        cH = (double)saut*(double)TailleY/(double)champ.nX;
        g.setColor(Color.red);
        for (int x = 0; x < champ.nX/saut; x++) {
            for (int y = 0; y < champ.nY/saut; y++) {
                Vecteur2D V = champ.c((double)(x*saut)/(double)(champ.nX-1),(double)(y*saut)/(double)(champ.nY-1)).copier();
                double L = V.longueur();
                V.norm();
                int lX = (int)((1.0-1.0/(L+1.0))*Math.min(cL, cH)*V.x);
                int lY = (int)((1.0-1.0/(L+1.0))*Math.min(cL, cH)*V.y);
                int a = (int)((double)x*cL) + (int)(cL/2.0);
                int b = (int)((double)TailleY - (double)(y+1)*cH) + (int)(cH/2.0);
                int c = (int)((double)x*cL + (cL/2) + lX);
                int d = (int)((double)TailleY - (double)(y+1)*cH + (cH/2.0) - lY);
                g.drawLine(a, b, c, d);
                //SwingUtilities.updateComponentTreeUI(frame);
            }
        }
        System.currentTimeMillis();
    }

    public static void dessinerChamp(Champ2V1 champ, int saut, Graphics2D g, JFrame frame){
        double cL = (double)TailleX/(double)champ.nX;
        double cH = (double)TailleY/(double)champ.nX;
        for (int x = 0; x < champ.c.length; x++) {
            for (int y = 0; y < champ.c[x].length; y++) {
                int col = (int)((1.0-1.0/(Math.abs(champ.c[x][y])+1.0))*255.0);
                g.setColor(new Color(Math.max((int)Math.signum(champ.c[x][y]),0)*col,0,Math.max(-(int)Math.signum(champ.c[x][y]),0)*col));
                g.fillRect((int)((double)x*cL), TailleY - (int)((double)(y+1)*cH), (int)Math.ceil(cL), (int)Math.ceil(cH));
                //SwingUtilities.updateComponentTreeUI(frame);
            }
        }
    }
}
