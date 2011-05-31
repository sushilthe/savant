/*
 *    Copyright 2009-2011 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package savant.view.swing;

import java.awt.*;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import savant.controller.GraphPaneController;
import savant.controller.Listener;
import savant.controller.RangeController;
import savant.controller.event.GraphPaneEvent;
import savant.controller.event.RangeChangedEvent;
import savant.controller.event.RangeChangedListener;
import savant.util.MiscUtils;


/**
 *
 * @author mfiume
 */
public class Ruler extends JPanel {
    private static final Log LOG = LogFactory.getLog(Ruler.class);

    static final int CAP_HEIGHT = 23;

    /** This is the width at which the cap is drawn. */
    static final int CAP_WIDTH = 20;
    
    /** This is the actual width of the cap image. */
    static final int CAP_IMAGE_WIDTH = 8;

    static final AlphaComposite COMPOSITE_85 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75F);

    private int minimum = 0;
    private int maximum = 100;
    private static final long serialVersionUID = 1L;
    private final JLabel mousePosition;
    private final JLabel recStart;
    private final JLabel recStop;
    private final JLabel cords; // set up GUI and register mouse event handlers
    boolean isNewRect = true;
    private GraphPaneController graphPaneController = GraphPaneController.getInstance();
    private RangeController rangeController = RangeController.getInstance();

    public Ruler() {

        this.mousePosition = new JLabel();
        this.mousePosition.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(this.mousePosition, BorderLayout.CENTER);

        this.recStart = new JLabel();
        this.add(this.recStart, BorderLayout.WEST);

        this.recStop = new JLabel();
        this.add(this.recStop, BorderLayout.EAST);

        this.cords = new JLabel();
        this.add(this.cords, BorderLayout.NORTH);

        graphPaneController.addListener(new Listener<GraphPaneEvent>() {
            @Override
            public void handleEvent(GraphPaneEvent event) {
                if (graphPaneController.isPanning()) {
                    repaint();
                }
            }
        });

        rangeController.addRangeChangedListener(new RangeChangedListener() {
            @Override
            public void rangeChanged(RangeChangedEvent event) {
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        if (graphPaneController.isPanning()) {
            int fromx = MiscUtils.transformPositionToPixel(graphPaneController.getMouseDragRange().getFrom(), getWidth(), rangeController.getRange());
            int tox = MiscUtils.transformPositionToPixel(graphPaneController.getMouseDragRange().getTo(), getWidth(), rangeController.getRange());

            double shiftamount = tox-fromx;
            g.translate((int) shiftamount, 0);
        }

        renderBackground(g);
    }

    /**
     * Render the background of this graphpane
     * @param g The graphics object to use
     */
    private void renderBackground(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        try {

            Image image = javax.imageio.ImageIO.read(getClass().getResource("/savant/images/bar_selected_glossy.png"));
            Composite originalComposite = ((Graphics2D) g).getComposite();
            ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85F));
            g.drawImage(image, -getWidth(), 0, getWidth() * 3, getHeight(),this);
            ((Graphics2D) g).setComposite(originalComposite);
        } catch (Exception e) {
            LOG.error("Error drawing image background");
        }

        int numseparators = (int) Math.max(Math.ceil(Math.log(this.maximum-this.minimum)),2);
        int genomicSeparation = (maximum -  minimum) / Math.max(1, numseparators);

        if (numseparators != 0) {
            int width = this.getWidth();
            double barseparation = width / numseparators;

            int minstringseparation = 200;
            int skipstring = (int) Math.round(minstringseparation / barseparation);

            int startbarsfrom = MiscUtils.transformPositionToPixel(
                    (int) (Math.floor((rangeController.getRange().getFrom() / Math.max(1, genomicSeparation)))*genomicSeparation), width, rangeController.getRange());

            FontMetrics fm = g2.getFontMetrics();

            for (int i = 0; i <= numseparators*3; i++) {
                g2.setColor(new Color(50,50,50,50)); //BrowserDefaults.colorAxisGrid);
                int xOne = startbarsfrom + (int)Math.ceil(i*barseparation)+1 - this.getWidth();
                int xTwo = xOne;
                int yOne = this.getHeight();
                int yTwo = 0;
                g2.drawLine(xOne,yOne,xTwo,yTwo);

                if (skipstring != 0) {
                    if (i % skipstring == 0) {
                        g2.setColor(Color.black);
                        int a = MiscUtils.transformPixelToPosition(xOne, width, rangeController.getRange());
                        if (a >= 1 && a <= rangeController.getMaxRangeEnd()) {
                            String numstr = MiscUtils.posToShortStringWithSeparation(a, genomicSeparation);
                            g2.setColor(Color.black);
                            g2.drawString(numstr, (float) (xOne + 3), (float) ((this.getHeight()*0.5)+3));
                        }
                    }
                } else {
                    int a = MiscUtils.transformPixelToPosition(xOne, width, rangeController.getRange());
                    String numstr = MiscUtils.numToString(a);
                    g2.setColor(Color.black);
                    g2.drawString(numstr, (float) (xOne + 3), (float) ((this.getHeight()*0.5)+3));
                }
            }

            if (rangeController.getRange().getLength() >= rangeController.getRangeStart()) {
                try {
                    Image image_left_cap = javax.imageio.ImageIO.read(getClass().getResource("/savant/images/round_cap_left_bordered.png"));
                    int pos = getLeftCapPos();
                    g.drawImage(image_left_cap, pos, 0, CAP_WIDTH, CAP_HEIGHT, this);
                    g.setColor(Savant.getInstance().getBackground());
                    g.fillRect(pos,0, -getWidth(), getHeight());
                } catch (IOException ex) {
                    LOG.error("Drawing failed.", ex);
                }
            }

            if (rangeController.getRange().getLength() >= rangeController.getMaxRangeEnd() - rangeController.getRangeEnd()) {
                try {
                    Image image_right_cap = javax.imageio.ImageIO.read(getClass().getResource("/savant/images/round_cap_right_bordered.png"));
                    int pos = MiscUtils.transformPositionToPixel(rangeController.getMaxRangeEnd(), getWidth(), rangeController.getRange());
                    g.drawImage(image_right_cap, pos - CAP_WIDTH, 0, CAP_WIDTH, CAP_HEIGHT, this);
                    g.setColor(Savant.getInstance().getBackground());
                    g.fillRect(pos,0, this.getWidth(), this.getHeight());
                    
                } catch (IOException ex) {
                    LOG.error("Drawing failed.", ex);
                } 
            }
        }
    }

    int getLeftCapPos() {
        return MiscUtils.transformPositionToPixel(1, getWidth(), rangeController.getRange());
    }
}


