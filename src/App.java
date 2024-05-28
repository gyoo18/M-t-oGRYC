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

        Champ2V2 vélocité = new Champ2V2(TailleX, TailleY, 30, 30);
        long timer = System.currentTimeMillis();
        for (int x = 0; x < vélocité.c.length; x++) {
            for (int y = 0; y < vélocité.c[x].length; y++) {
                vélocité.c[x][y] = new Vecteur2D(((double)x/(double)vélocité.nX - 0.5),((double)y/(double)vélocité.nY - 0.5)).norm().mult(10000.0);

                if(System.currentTimeMillis() - timer > 5000){
                    System.out.println("Initialisation vélocité : " + String.format("%.0f",100.0*(double)(x*vélocité.nX + y)/(double)(vélocité.nX*vélocité.nY)) + "%");
                    timer = System.currentTimeMillis();
                }
            }
        }

        Champ2V1 div = vélocité.div();
        Champ2V2 divVec = div.grad();

        while (true) {
            dessinerChamp(div, g, frame);
            SwingUtilities.updateComponentTreeUI(frame);
            Thread.sleep(30);
        }
    }

    public static void dessinerChamp(Champ2V2 champ, Graphics2D g, JFrame frame){
        int cL = (int)((double)TailleX/(double)champ.nX);
        int cH = (int)((double)TailleY/(double)champ.nX);
        for (int x = 0; x < champ.c.length; x++) {
            for (int y = 0; y < champ.c[x].length; y++) {
                g.setColor(new Color((int)((1.0-1.0/(Math.abs(champ.c[x][y].x)+1.0))*255.0),(int)((1.0-1.0/(Math.abs(champ.c[x][y].y)+1.0))*255.0),0));
                g.fillRect(x*cL, TailleY - (y+1)*cH, cL, cH);
                //SwingUtilities.updateComponentTreeUI(frame);
            }
        }
        g.setColor(Color.red);
        for (int x = 0; x < champ.c.length; x++) {
            for (int y = 0; y < champ.c[x].length; y++) {
                int a = x*cL + (cL/2);
                int b = TailleY - (y+1)*cH + (cH/2);
                int c = x*cL + (cL/2) + (int)((1.0-1.0/(Math.abs(champ.c[x][y].x)+1.0))*(double)cL*Math.signum(champ.c[x][y].x));
                int d = TailleY - (y+1)*cH + (cH/2) - (int)((1.0-1.0/(Math.abs(champ.c[x][y].y)+1.0))*(double)cH*Math.signum(champ.c[x][y].y));
                g.drawLine(a, b, c, d);
                //SwingUtilities.updateComponentTreeUI(frame);
            }
        }
        System.currentTimeMillis();
    }

    public static void dessinerChamp(Champ2V1 champ, Graphics2D g, JFrame frame){
        int cL = (int)((double)TailleX/(double)champ.nX);
        int cH = (int)((double)TailleY/(double)champ.nX);
        for (int x = 0; x < champ.c.length; x++) {
            for (int y = 0; y < champ.c[x].length; y++) {
                int col = (int)((1.0-1.0/(Math.abs(champ.c[x][y])+1.0))*255.0);
                g.setColor(new Color(col,col,col));
                g.fillRect(x*cL, TailleY - (y+1)*cH, cL, cH);
                //SwingUtilities.updateComponentTreeUI(frame);
            }
        }
    }
}
