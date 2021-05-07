package gui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JComponent;
import module.XGModule;
import xml.XMLNode;

public final class Keyboard extends JComponent {

	private static final long serialVersionUID = 1L;
	public static final float WHITE_KEY_ASPECT = (7f / 8f) / (5.7f);
    public static final float BLACK_KEY_HEIGHT = 4f / 6f;
    
    private char firstNote;
    private int whiteKeyCount;
    private int whiteKeyWidth;
    private int whiteKeyHeight;
    private List<KeyShape> keyShapes;
    
    private final Set<Integer> litKeys = new HashSet<>();

    public Keyboard(XMLNode n, XGModule mod)
    {	XGFrame f = new XGFrame("Keyboard");
    	Insets i = f.getInsets();
    	int h = f.getHeight() - (i.top + i.bottom);
        setFirstNote('C');
        setWhiteKeyCount(80);
        setWhiteKeySize(Math.round(h * WHITE_KEY_ASPECT), h);
    }
    
    
    public void setFirstNote(char n) {
        if (n < 'A' || n > 'G') throw new IllegalArgumentException();
        this.firstNote = n;
        revalidate();
    }
    
    
    public void setWhiteKeyCount(int c) {
        if (c < 0) throw new IllegalArgumentException();
        this.whiteKeyCount = c;
        revalidate();
    }
    
    
    public void setWhiteKeySize(int width, int height) {
        if (width < 0) throw new IllegalArgumentException();
        if (height < 0) throw new IllegalArgumentException();
        this.whiteKeyWidth = width;
        this.whiteKeyHeight = height;
        revalidate();
    }
    
    
    private static class KeyShape {
        final Shape shape;
        final char color; // 'W' or 'B'
        
        KeyShape(Shape shape, char color) {
            this.shape = shape;
            this.color = color;
        }
    }
    
    
    @Override
    public void invalidate() {
        super.invalidate();
        keyShapes = null;
    }
    
    
    private List<KeyShape> getKeyShapes() {
        if (keyShapes == null) {
            keyShapes = generateKeyShapes();
        }
        return keyShapes;
    }
    
    
    private List<KeyShape> generateKeyShapes() {
        List<KeyShape> shapes = new ArrayList<>();
        
        int x = 0;
        char note = firstNote;
        for (int w = 0; w < whiteKeyCount; w++) {
            float cutLeft = 0, cutRight = 0;
            switch (note) {
            case 'C':
                cutLeft  = 0 / 24f;
                cutRight = 9 / 24f;
                break;
            case 'D':
                cutLeft  = 5 / 24f;
                cutRight = 5 / 24f;
                break;
            case 'E':
                cutLeft  = 9 / 24f;
                break;
            case 'F':
                cutRight = 11 / 24f;
                break;
            case 'G':
                cutLeft  = 3 / 24f;
                cutRight = 7 / 24f;
                break;
            case 'A':
                cutLeft  = 7 / 24f;
                cutRight = 3 / 24f;
                break;
            case 'B':
                cutLeft  = 11 / 24f;
                cutRight = 0 / 24f;
                break;
            }
            if (w == 0)
                cutLeft = 0;
            if (w == whiteKeyCount - 1)
                cutRight = 0;
            
            shapes.add(new KeyShape(createWhiteKey(x, cutLeft, cutRight), 'W'));
            
            if (cutRight != 0) {
                shapes.add(new KeyShape(createBlackKey(x + whiteKeyWidth - (whiteKeyWidth * cutRight)), 'B'));
            }
            
            x += whiteKeyWidth;
            if (++note == 'H') note = 'A';
        }
        
        return Collections.unmodifiableList(shapes);
    }
    
    
    private Shape createWhiteKey(float x, float cutLeft, float cutRight) {
        float width = whiteKeyWidth, height = whiteKeyHeight;
        Path2D.Float path = new Path2D.Float();
        path.moveTo(x + cutLeft * width, 0);
        path.lineTo(x + width - (width * cutRight), 0);
        if (cutRight != 0) {
            path.lineTo(x + width - (width * cutRight), height * BLACK_KEY_HEIGHT);
            path.lineTo(x + width, height * BLACK_KEY_HEIGHT);
        }
        final float bevel = 0.15f;
        path.lineTo(x + width, height - (width * bevel) - 1);
        if (bevel != 0) {
            path.quadTo(x + width, height, x + width * (1 - bevel), height - 1);
        }
        path.lineTo(x + width * bevel, height - 1);
        if (bevel != 0) {
            path.quadTo(x, height, x, height - (width * bevel) - 1);
        }
        if (cutLeft != 0) {
            path.lineTo(x, height * BLACK_KEY_HEIGHT);
            path.lineTo(x + width * cutLeft, height * BLACK_KEY_HEIGHT);
        }
        path.closePath();
        return path;
    }
    
    
    private Shape createBlackKey(float x) {
        return new Rectangle2D.Float(
            x, 0,
            whiteKeyWidth * 14f / 24,
            whiteKeyHeight * BLACK_KEY_HEIGHT
        );
    }
    
    
    @Override
    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        Rectangle clipRect = g.getClipBounds();
        
        g.setColor(Color.BLACK);
        g.fill(clipRect);
        
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(1f));
        
        List<KeyShape> keyShapes = getKeyShapes();
        for (int i = 0; i < keyShapes.size(); i++) {
            KeyShape ks = keyShapes.get(i);
            Rectangle bounds = ks.shape.getBounds();
            if (!bounds.intersects(clipRect)) continue;
            
            g.setColor(isKeyLit(i)
                ? (ks.color == 'W' ? new Color(0xFF5050) : new Color(0xDF3030))
                : (ks.color == 'W' ? Color.WHITE : Color.BLACK)
            );
            g.fill(ks.shape);
            
            if (true) { // gradient
                if (ks.color == 'W') {
                    g.setPaint(new LinearGradientPaint(
                        bounds.x, bounds.y, bounds.x, bounds.y + bounds.height,
                        new float[] { 0, 0.02f, 0.125f, 0.975f, 1 },
                        new Color[] {
                            new Color(0xA0000000, true),
                            new Color(0x30000000, true),
                            new Color(0x00000000, true),
                            new Color(0x00000000, true),
                            new Color(0x30000000, true),
                        }
                    ));
                    g.fill(ks.shape);
                } else {
                    bounds.setRect(
                        bounds.getX() + bounds.getWidth() * 0.15f,
                        bounds.getY() + bounds.getHeight() * 0.03f,
                        bounds.getWidth() * 0.7f,
                        bounds.getHeight() * 0.97f
                    );
                    g.setPaint(new GradientPaint(
                        bounds.x, bounds.y, new Color(0x60FFFFFF, true),
                        bounds.x, bounds.y + bounds.height * 0.5f, new Color(0x00FFFFFF, true)
                    ));
                    g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 4, 4);
                    g.setPaint(new LinearGradientPaint(
                        bounds.x, bounds.y, bounds.x + bounds.width, bounds.y,
                        new float[] { 0, 0.2f, 0.8f, 1 },
                        new Color[] {
                            new Color(0x60FFFFFF, true),
                            new Color(0x00FFFFFF, true),
                            new Color(0x00FFFFFF, true),
                            new Color(0x60FFFFFF, true),
                        }
                    ));
                    g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 4, 4);
                }
            }
            
            g.setColor(Color.BLACK);
            g.draw(ks.shape);
        }
    }
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
            whiteKeyCount * whiteKeyWidth,
            whiteKeyHeight
        );
    }
    
    
    public int getKeyAtPoint(Point2D p) {
        List<KeyShape> keyShapes = getKeyShapes();
        for (int i = 0; i < keyShapes.size(); i++) {
            if (keyShapes.get(i).shape.contains(p)) return i;
        }
        return -1;
    }
    
    
    public void setKeyLit(int index, boolean b) {
        if (index < 0 || index > getKeyShapes().size()) return;
        if (b) {
            litKeys.add(index);
        } else {
            litKeys.remove(index);
        }
        repaint(getKeyShapes().get(index).shape.getBounds());
    }
    
    
    public boolean isKeyLit(int index) {
        return litKeys.contains(index);
    }
    
    
    public void clearLitKeys() {
        litKeys.clear();
        repaint();
    }
    
    
}