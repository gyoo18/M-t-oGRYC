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

        Champ2V2 vélocité = new Champ2V2(TailleX, TailleY, 40, 40);
        for (int x = 0; x < vélocité.c.length; x++) {
            for (int y = 0; y < vélocité.c[x].length; y++) {
                vélocité.c[x][y] = new Vecteur2D(((double)x/(double)vélocité.nX - 0.5),((double)y/(double)vélocité.nY - 0.5)).mult(100.0);
            }
        }

        while (true) {
            for (int x = 0; x < vélocité.c.length; x++) {
                for (int y = 0; y < vélocité.c[x].length; y++) {
                    vélocité.c[x][y].rot(0.05);
                }
            }
            if(vélocité.c[0][0].longueur() < 0.01){
                System.currentTimeMillis();
            }
            Champ2V1 div = vélocité.Div();
            //Champ2V2 divVec = div.grad();
            //Champ2V2 velRot = Champ2V2.sous(vélocité,divVec);
            dessinerChamp(div, 2, g, frame);
            SwingUtilities.updateComponentTreeUI(frame);
            Thread.sleep(30);
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
        cL = (int)((double)saut*(double)TailleX/((double)champ.nX));
        cH = (int)((double)saut*(double)TailleY/((double)champ.nX));
        g.setColor(Color.red);
        for (int x = 0; x < champ.nX/saut; x++) {
            for (int y = 0; y < champ.nY/saut; y++) {
                Vecteur2D V = champ.c((double)(x*saut)/(double)champ.nX,(double)(y*saut)/(double)champ.nY).copier();
                double L = V.longueur();
                V.norm();
                int lX = (int)((1.0-1.0/(L+1.0))*Math.min(cL, cH)*V.x);
                int lY = (int)((1.0-1.0/(L+1.0))*Math.min(cL, cH)*V.y);
                int a = (int)((double)x*cL + (cL/2.0));
                int b = (int)((double)TailleY - (double)(y+1)*cH + (cH/2.0));
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
                g.setColor(new Color(col,col,col));
                g.fillRect((int)((double)x*cL), TailleY - (int)((double)(y+1)*cH), (int)Math.ceil(cL), (int)Math.ceil(cH));
                //SwingUtilities.updateComponentTreeUI(frame);
            }
        }
    }
}
