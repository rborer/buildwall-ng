package net.morlhon.wall.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import net.morlhon.wall.common.GuiEventListener;
import net.morlhon.wall.ui.ushering.Usherette;

/**
 * WallFrame is a JFrame able to draw wall, most of the drawning specific to the buildWall is done here.
 *
 * @author Jean-Laurent de Morlhon
 */
public class WallFrame extends JFrame {
   private static final String NO_DATA_YET = "No data yet...";
   private static final String FRAME_TITLE = "Wall";
   private static final String WINDOWED = "WINDOWED";
   private static final Dimension DEFAULT_SIZE = new Dimension(640, 480);
   private static final int TOPMARGIN = 5;
   private static final int BOTTOMMARGIN = 5;
   private List<Brick> brickList = new ArrayList<Brick>();
   private final Usherette usherette;
   private ImageCache cache;
   private BufferedImage buffer;
   private boolean displayImage = true;
   private final URL facesURL;
   private final GuiEventListener guiEvent;
   private boolean isClosing = false;

   public WallFrame(GuiEventListener guiEvent, URL facesURL, Usherette usherette) {
      super(FRAME_TITLE);
      this.guiEvent = guiEvent;
      this.facesURL = facesURL;
      displayImage = (facesURL != null);
      this.usherette = usherette;
      setupFrame(System.getProperty(WINDOWED) == null);
   }

   private void initImageCache() {
      if (displayImage) {
         this.cache = new ImageCache(facesURL);
         this.cache.init();
      }
   }

   public void close() {
      if (!isClosing) {
         isClosing = true;
         setVisible(false);
         dispose();
      }
   }

   private void setupFrame(boolean fullScreen) {
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      initImageCache();
      if (fullScreen) {
         this.setUndecorated(true);
         GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
      } else {
         this.setSize(DEFAULT_SIZE);
      }
      this.setVisible(true);

      addWindowListener(new WindowAdapter() {

         @Override
         public void windowClosed(WindowEvent e) {
            guiEvent.askForShutdown();
         }

      });

      getContentPane().addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            buffer = null;
            setBrickList(WallFrame.this.brickList);
         }
      });

      addKeyListener(new KeyAdapter() {
         @Override
         public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_Q) {
               guiEvent.askForShutdown();
            }
         }
      });

      addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent me) {
            if (me.getClickCount() == 2) {
               guiEvent.askForShutdown();
            }
         }
      });
   }

   public void setBrickList(List<Brick> brickList) {
      synchronized (this.brickList) {
         this.brickList = brickList;
         usherette.usher(brickList, getWidth(), getHeight());
      }
      drawBuffer();
      this.repaint();
   }

   @Override
   public void paint(Graphics g) {
      if (buffer == null) {
         drawBuffer();
      }
      g.drawImage(buffer, 0, 0, null);
   }

   private void drawBuffer() {
      if (buffer == null) {
         buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
      }
      Graphics g = buffer.getGraphics();
      drawBackground(g, this.brickList.size() == 0);
      for (Brick brick : this.brickList) {
         drawBrick(g, brick);
         int size = drawBrickName(g, brick);
         size = drawBrickComment(g, brick, size);
         if (!drawBrickAuthorsAsImage(g, brick, size)) {
            drawBrickAuthorsAsText(g, brick);
         }
      }
      g.dispose();
   }

   private void drawBackground(Graphics g, boolean empty) {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, getWidth(), getHeight());
      if (empty) {
         writeBackgroundText(g);
      }
   }

   private void writeBackgroundText(Graphics g) {
      g.setFont(g.getFont().deriveFont(Font.BOLD, 48f));
      Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(NO_DATA_YET, null);
      double textHeight = stringBounds.getHeight();
      int sx = (int) ((getWidth() - stringBounds.getWidth()) / 2);
      int sy = (int) ((getHeight() - textHeight) / 2 + textHeight);
      g.setColor(Color.WHITE);
      g.drawString(NO_DATA_YET, sx, sy);
   }

   private void drawBrick(Graphics g, Brick brick) {
      g.setColor(brick.getColor());
      g.fillRect(brick.x, brick.y, brick.width, brick.height);
      g.setColor(Color.BLACK);
      g.drawRect(brick.x, brick.y, brick.width, brick.height);
   }

   private int drawBrickName(Graphics g, Brick brick) {
      if (brick.getName() == null) {
         return 0;
      }
      Rectangle2D stringBounds = null;
      double textWidth = 0;
      float startingFontSize = 52f;
      do {
         g.setFont(g.getFont().deriveFont(Font.BOLD, startingFontSize));
         startingFontSize -= 2;
         stringBounds = g.getFontMetrics().getStringBounds(brick.getName(), null);
         textWidth = stringBounds.getWidth();
      } while (textWidth > brick.width || startingFontSize >= 40f);
      double textHeight = stringBounds.getHeight() + stringBounds.getMaxY();
      int sx = (int) ((brick.width - textWidth) / 2);
      int sy = (int) (Math.ceil(textHeight) + TOPMARGIN);
      g.setColor(textColor(brick.getColor()));
      Shape savedClip = g.getClip();
      g.setClip(brick.x, brick.y, brick.width, brick.height);
      g.drawString(brick.getName(), brick.x + sx, brick.y + sy);
      g.setClip(savedClip);
      return (int) Math.round(textHeight + TOPMARGIN);
   }

   private int drawBrickComment(Graphics g, Brick brick, int size) {
      if (brick.getComment() == null) {
         return size;
      }
      g.setFont(g.getFont().deriveFont(Font.BOLD, 24f));
      Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(brick.getComment(), null);
      int sx = (int) ((brick.width - stringBounds.getWidth()) / 2);
      double textHeight = stringBounds.getHeight();
      int sy = (int) (size + textHeight);
      g.setColor(textColor(brick.getColor()));
      Shape savedClip = g.getClip();
      g.setClip(brick.x, brick.y, brick.width, brick.height);
      g.drawString(brick.getComment(), brick.x + sx, brick.y + sy);
      g.setClip(savedClip);
      return sy;
   }

   private boolean drawBrickAuthorsAsImage(Graphics g, Brick brick, int size) {
      if (!displayImage) {
         return false;
      }
      BufferedImage image = cache.getImage(brick.getFooter());
      if (image == null) {
         return false;
      }
      float maxHeight = brick.height - size - 2 * BOTTOMMARGIN;
      float maxWidth = brick.width - 2 * BOTTOMMARGIN;
      float scaleFactorHeight = maxHeight / image.getHeight();
      float scaleFactorWidth = maxWidth / image.getWidth();
      float scaleFactor = Math.min(scaleFactorHeight, scaleFactorWidth);
      if (scaleFactor < 0) {
         scaleFactor = 0;
      }
      float newHeight = image.getHeight() * scaleFactor;
      float newWidth = image.getWidth() * scaleFactor;
      int sx = (int) (brick.width - newWidth) / 2 + brick.x;
      int sy = brick.y + size + BOTTOMMARGIN + (brick.height - (size + BOTTOMMARGIN) - (int) newHeight) / 2;
      g.drawImage(image, sx, sy, Math.round(newWidth), Math.round(newHeight), null);
      g.setColor(Color.BLACK);
      g.drawRect(sx, sy, Math.round(newWidth), Math.round(newHeight));
      return true;
   }

   private void drawBrickAuthorsAsText(Graphics g, Brick brick) {
      if (brick.getFooter() == null) {
         return;
      }
      String[] lineArray = brick.getFooter().split("\n");
      g.setFont(g.getFont().deriveFont(Font.BOLD, 24f));
      int arrayLength = lineArray.length;
      for (int i =0; i< lineArray.length; i++) {
         Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(lineArray[i], null);
         int sx = (int) ((brick.width - stringBounds.getWidth()) / 2);
         double textHeight = stringBounds.getHeight();
         int sy = (int) (((brick.height - textHeight) * 3) / 4 + ((textHeight / 2) * (arrayLength + 1 - (2 * i))));
         g.setColor(textColor(brick.getColor()));
         Shape savedClip = g.getClip();
         g.setClip(brick.x, brick.y, brick.width, brick.height);
         g.drawString(lineArray[i], brick.x + sx, brick.y + sy);
         g.setClip(savedClip);
      }
   }

   private Color textColor(Color color) {
      int sum = (color.getRed() + color.getBlue() + color.getGreen()) / 3;
      if (sum > 128) {
         return Color.BLACK;
      }
      return Color.WHITE;
   }

   public void reload() {
      cache.init();
      drawBuffer();
      this.repaint();
   }

}
