/*
 *    Copyright 2009-2010 University of Toronto
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

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import savant.controller.TrackController;
import savant.controller.ViewTrackController;
import savant.data.types.Genome;
import savant.data.sources.*;
import savant.file.*;
import savant.format.SavantFileFormatterUtils;
import savant.util.*;
import savant.view.dialog.BAMParametersDialog;
import savant.view.swing.continuous.ContinuousViewTrack;
import savant.view.swing.interval.BAMCoverageViewTrack;
import savant.view.swing.interval.BAMViewTrack;
import savant.view.swing.interval.BEDViewTrack;
import savant.view.swing.interval.IntervalViewTrack;
import savant.view.swing.point.PointViewTrack;
import savant.view.swing.sequence.SequenceViewTrack;
import savant.view.swing.util.DialogUtils;


/**
 * Class to handle the preparation for rendering of a track. Handles colour schemes and
 * drawing instructions, getting and filtering of data, setting of vertical axis, etc. The
 * ranges associated with various resolutions are also handled here, and the drawing modes
 * are defined.
 *
 * @author mfiume
 */
public abstract class ViewTrack {

    private static final Log LOG = LogFactory.getLog(ViewTrack.class);
    private final String name;
    private ColorScheme colorScheme;
    private final FileFormat dataType;
    private List<Object> dataInRange;
    private List<Mode> drawModes;
    private Mode drawMode;
    private List<TrackRenderer> trackRenderers;
    private DataSource dataSource;
    private ColorSchemeDialog colorDialog = new ColorSchemeDialog();
    private IntervalDialog intervalDialog = new IntervalDialog();
    // FIXME:
    private Frame frame;
    private static BAMParametersDialog paramDialog = new BAMParametersDialog(Savant.getInstance(), true);

    // TODO: put all of this in a ViewTrackFactory class
    // TODO: inform the user when there is a problem
    /**
     * Create one or more tracks from the given file name.
     *
     * @param trackURI
     * @return List of ViewTrack which can be added to a Frame
     * @throws IOException
     */
    public static List<ViewTrack> create(URI trackURI) throws IOException {

        LOG.info("Opening track " + trackURI);

        List<ViewTrack> results = new ArrayList<ViewTrack>();

        // determine default track name from filename
        String trackPath = trackURI.getPath();
        int lastSlashIndex = trackPath.lastIndexOf(System.getProperty("file.separator"));
        String name = trackPath.substring(lastSlashIndex + 1, trackPath.length());

        FileType fileType = SavantFileFormatterUtils.guessFileTypeFromPath(trackPath);

        ViewTrack viewTrack = null;
        DataSource dataTrack = null;

        // BAM
        if (fileType == FileType.INTERVAL_BAM) {

            LOG.info("Opening BAM file " + trackURI);

            try {
                dataTrack = BAMDataSource.fromURI(trackURI);
                if (dataTrack != null) {
                    viewTrack = new BAMViewTrack(name, (BAMDataSource) dataTrack);
                    results.add(viewTrack);
                } else {
                    String e = String.format("Could not create BAM track; check that index file exists and is named \"%1$s.bai\".", name);
                    JOptionPane.showConfirmDialog(Savant.getInstance(), e, "Error loading track", JOptionPane.DEFAULT_OPTION);
                    return null;
                }

                // TODO: Only resolves coverage files for local data.  Should also work for network URIs.
                if (trackURI.getScheme().equals("file")) {
                    File coverageFile = new File(new URI(trackURI.toString() + ".cov.savant"));

                    if (coverageFile.exists()) {
                        dataTrack = new GenericContinuousDataSource(coverageFile.toURI());
                        viewTrack = new BAMCoverageViewTrack(name + " (coverage)", (GenericContinuousDataSource)dataTrack);
                    } else {
                        //FIXME: this should not happen! plugins expect tracks to contain data, and not be vacuous
                        viewTrack = new BAMCoverageViewTrack(name + " (coverage)", null);
                    }
                }
            } catch (IOException e) {
                LOG.warn("Could not load coverage track", e);

                //FIXME: this should not happen! plugins expect tracks to contain data, and not be vacuous
                // create an empty ViewTrack that just displays an error message << see the above FIXME
                viewTrack = new BAMCoverageViewTrack(name + " (coverage)", null);
            } catch (SavantFileNotFormattedException e) {
                LOG.warn("Coverage track appears to be unformatted", e);
                viewTrack = new BAMCoverageViewTrack(name + " (coverage)", null);
            } catch (SavantUnsupportedVersionException e) {
                DialogUtils.displayMessage("This file was created using an older version of Savant. Please re-format the source.");
            } catch (URISyntaxException e) {
                DialogUtils.displayMessage("Syntax error on URI; file URI is not valid");
            }
            
            results.add(viewTrack);

        } else {

            try {

                // read file header
                SavantROFile trkFile = new SavantROFile(trackURI);

                LOG.debug("Reading file type header");
                LOG.debug("File type: " + trkFile.getFileType());

                trkFile.close();

                if (trkFile.getFileType() == null) {
                    Savant s = Savant.getInstance();
                    s.promptUserToFormatFile(trackURI);
                    return results;
                }

                switch (trkFile.getFileType()) {
                    case SEQUENCE_FASTA:
                        dataTrack = new FASTAFileDataSource(trackURI);
                        viewTrack = new SequenceViewTrack(name, (FASTAFileDataSource) dataTrack);
                        break;
                    case POINT_GENERIC:
                        dataTrack = new GenericPointDataSource(trackURI);
                        viewTrack = new PointViewTrack(name, (GenericPointDataSource) dataTrack);
                        break;
                    case CONTINUOUS_GENERIC:
                        dataTrack = new GenericContinuousDataSource(trackURI);
                        viewTrack = new ContinuousViewTrack(name, (GenericContinuousDataSource) dataTrack);
                        break;
                    case INTERVAL_GENERIC:
                        dataTrack = new GenericIntervalDataSource(trackURI);
                        viewTrack = new IntervalViewTrack(name, (GenericIntervalDataSource) dataTrack);
                        break;
                    case INTERVAL_GFF:
                        dataTrack = new GenericIntervalDataSource(trackURI);
                        viewTrack = new IntervalViewTrack(name, (GenericIntervalDataSource) dataTrack);
                        break;
                    case INTERVAL_BED:
                        dataTrack = new BEDFileDataSource(trackURI);
                        viewTrack = new BEDViewTrack(name, (BEDFileDataSource) dataTrack);
                        break;
                    default:
                        Savant s = Savant.getInstance();
                        s.promptUserToFormatFile(trackURI);
                }
                if (viewTrack != null) {
                    results.add(viewTrack);
                }
            } catch (SavantFileNotFormattedException e) {
                Savant s = Savant.getInstance();
                s.promptUserToFormatFile(trackURI);
            } catch (SavantUnsupportedVersionException e) {
                DialogUtils.displayMessage("This file was created using an older version of Savant. Please re-format the source.");
            } catch (IOException e) {
                DialogUtils.displayException("Error opening track", "There was a problem opening this file.", e);
            } catch (URISyntaxException e) {
                DialogUtils.displayMessage("Syntax error on URI; file URI is not valid");
            }
            /*
            if (viewTrack != null) {
                viewTrack.setURI(trackFilename);
            } else {
            }
             * 
             */
        }

        return results;
    }

    public static Genome createGenome(URI genomeURI) throws IOException, SavantFileNotFormattedException, SavantUnsupportedVersionException {

        List<ViewTrack> tracks = ViewTrack.create(genomeURI);

        if (tracks == null || tracks.isEmpty()) { return null; }

        // determine default track name from filename
        String genomePath = genomeURI.getPath();
        int lastSlashIndex = genomePath.lastIndexOf("/");
        String name = genomePath.substring(lastSlashIndex + 1, genomePath.length());

        Genome g = null;
        if (tracks.get(0) instanceof SequenceViewTrack) {
            g = new Genome(name, (SequenceViewTrack) tracks.get(0));
        } else {
            JOptionPane.showMessageDialog(Savant.getInstance(), "Problem opening track as genome.");
        }

        return g;
    }

    /**
     * Constructor
     * @param name track name (typically, the file name)
     * @param dataType FileFormat representing file type, e.g. INTERVAL_BED, CONTINUOUS_GENERIC
     */
    public ViewTrack(String name, FileFormat dataType, DataSource dataSource) {
        this.name = name;
        this.dataType = dataType;
        drawModes = new ArrayList<Mode>();
        trackRenderers = new ArrayList<TrackRenderer>();
        this.dataSource = dataSource;
    }

    public void notifyViewTrackControllerOfCreation() {
        ViewTrackController tc = ViewTrackController.getInstance();
        tc.addTrack(this);
        if (dataSource != null) {
            TrackController.getInstance().addTrack(dataSource);
        }
    }

    //public void setFilename(String fn) {
    //    this.filename = fn;
    //}
    // FIXME: this shouldn't be a URI
    /*
    public String getPath() {
    if (this.getDataSource() == null) { return null; }
    return this.getDataSource().getURI().toString();
    }
     * 
     */
    /**
     * Get the type of file this view track represents
     *
     * @return  FileFormat
     */
    public FileFormat getDataType() {
        return this.dataType;
    }

    /**
     * Get the current colour scheme.
     *
     * @return ColorScheme
     */
    public ColorScheme getColorScheme() {
        return this.colorScheme;
    }

    /**
     * Set individual colour.
     *
     * @param name color name
     * @param color new color
     */
    public void setColor(String name, Color color) {
        this.colorScheme.addColorSetting(name, color);
    }

    /**
     * Get the name of this track. Usually constructed from the file name.
     *
     * @return track name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the data currently being displayed (or ready to be displayed)
     *
     * @return List of data objects
     */
    public List<Object> getDataInRange() {
        return this.dataInRange;
    }

    /*
    public DrawingInstructions getDrawingInstructions() {
    return this.DRAWING_INSTRUCTIONS;
    }
     */
    /**
     * Get current draw mode
     *
     * @return draw mode as Mode
     */
    public Mode getDrawMode() {
        return this.drawMode;
    }

    /**
     * Get all valid draw modes for this track.
     *
     * @return List of draw Modes
     */
    public List<Mode> getDrawModes() {
        return this.drawModes;
    }

    /**
     * Set colour scheme.
     *
     * @param cs new colour scheme
     */
    public void setColorScheme(ColorScheme cs) {
        this.colorScheme = cs;
    }

    /**
     * Reset colour scheme.
     *
     */
    public abstract void resetColorScheme();

    /*
    public void setDrawingInstructions(DrawingInstructions di) {
    this.DRAWING_INSTRUCTIONS = di;
    }
     */
    /**
     * Set the current draw mode.
     *
     * @param mode
     */
    public void setDrawMode(Mode mode) {
        this.drawMode = mode;
    }

    /**
     * Set the current draw mode by its name
     *
     * @param modename
     */
    public void setDrawMode(String modename) {
        for (Mode m : drawModes) {
            if (m.getName().equals(modename)) {
                setDrawMode(m);
                break;
            }
        }
    }

//    public void setDrawMode(Object o) {
//        setDrawMode(o.toString());
//    }
    /**
     * Set the list of valid draw modes
     *
     * @param modes
     */
    public void setDrawModes(List<Mode> modes) {
        this.drawModes = modes;
    }

    /**
     * Get the record (data) track associated with this view track (if any.)
     *
     * @return Record Track or null (in the case of a genome.)
     */
    public DataSource getDataSource() {
        return this.dataSource;
    }

    // FIXME:
    public Frame getFrame() {
        return frame;
    }

    // FIXME:
    public void setFrame(Frame frame) {
        this.frame = frame;
        colorDialog.setFrame(frame);
        intervalDialog.setFrame(frame);
    }

    /**
     * Prepare this view track to render the given range.
     *
     * @param range
     * @throws Exception
     */
    public abstract void prepareForRendering(String reference, Range range) throws Throwable;

    /**
     * Retrieve data from the underlying data track at the current resolution and save it.
     *
     * @param range The range within which to retrieve objects
     * @return a list of data objects in the given range
     * @throws Exception
     */
    public List<Object> retrieveAndSaveData(String reference, Range range) throws Throwable {
        Resolution resolution = getResolution(range);

        /*
        // Get current time
        long start = System.currentTimeMillis();
         */

        this.dataInRange = retrieveData(reference, range, resolution);

        /*
        // Get elapsed time in milliseconds
        long elapsedTimeMillis = System.currentTimeMillis()-start;

        // Get elapsed time in seconds
        float elapsedTimeSec = elapsedTimeMillis/1000F;

        System.out.println("\tData retreival for " + this.getName() + " took " + elapsedTimeSec + " seconds");
         */

        return this.dataInRange;
    }

    /**
     * Store null to dataInRange.
     *
     * @throws Exception
     */
    public void saveNullData() throws Throwable {
        this.dataInRange = null;
    }

    /**
     * Retrieve data from the underlying data track.
     *
     * @param range The range within which to retrieve objects
     * @param resolution The resolution at which to get data
     * @return a List of data objects from the given range and resolution
     * @throws Exception
     */
    public abstract List<Object> retrieveData(String reference, Range range, Resolution resolution) throws Throwable;

    /**
     * Add a renderer to this view track
     *
     * @param renderer
     */
    public void addTrackRenderer(TrackRenderer renderer) {
        this.trackRenderers.add(renderer);
    }

    /**
     * Get all renderers attached to this view track
     *
     * @return
     */
    public List<TrackRenderer> getTrackRenderers() {
        return this.trackRenderers;
    }

    /**
     * Get the resoultion associated with the given range
     *
     * @param range
     * @return resolution appropriate to the range
     */
    public abstract Resolution getResolution(Range range);

    /**
     * Get the default draw mode.
     *
     * @return  the default draw mode
     */
    public Mode getDefaultDrawMode() {
        return null;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static void captureBAMDisplayParameters(BAMViewTrack viewTrack) {
        paramDialog.setVisible(true);
        if (paramDialog.isAccepted()) {
            viewTrack.setArcSizeVisibilityThreshold(paramDialog.getArcLengthThreshold());
            viewTrack.setDiscordantMin(paramDialog.getDiscordantMin());
            viewTrack.setDiscordantMax(paramDialog.getDiscordantMax());
        }

    }

    public void captureColorParameters() {
        colorDialog.update(this);
        colorDialog.setVisible(true);
    }

    public void captureIntervalParameters() {
        intervalDialog.update(this);
        intervalDialog.setVisible(true);
    }

    // FIXME: URI is not appropriate for this usage;
    /*
    public void setURI(String name) {

        this.fileURI = new File(name).toURI();
//        name = name.replace("\\", "/");
//        name = name.replace(" ", "_");
//        try {
//            this.fileURI = new URI(name);
//        } catch (URISyntaxException ex) {
//            Logger.getLogger(ViewTrack.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
     * 
     */

    // FIXME: URI is not appropriate for this usage; 
    public URI getURI() {
        return (this.getDataSource() == null) ? null : this.getDataSource().getURI();
    }
}
