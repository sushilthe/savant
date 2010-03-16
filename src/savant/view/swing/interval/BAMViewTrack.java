/*
 *    Copyright 2010 University of Toronto
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

/*
 * BAMViewTrack.java
 * Created on Feb 1, 2010
 */

package savant.view.swing.interval;

import net.sf.samtools.SAMRecord;
import savant.model.BAMIntervalRecord;
import savant.model.Interval;
import savant.model.Resolution;
import savant.model.FileFormat;
import savant.model.data.interval.BAMIntervalTrack;
import savant.model.view.*;
import savant.util.Range;
import savant.view.swing.TrackRenderer;
import savant.view.swing.ViewTrack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BAMViewTrack extends ViewTrack {
    
    private static Log log = LogFactory.getLog(BAMViewTrack.class);

    public enum DrawingMode {
        STANDARD,
        VARIANTS,
        MATE_PAIRS
    };

    private static final Mode STANDARD_MODE = new Mode(DrawingMode.STANDARD, "Colour by strand");
    private static final Mode VARIANTS_MODE = new Mode(DrawingMode.VARIANTS, "Show indels and mismatches");
    private static final Mode MATE_PAIRS_MODE = new Mode(DrawingMode.MATE_PAIRS, "Join mate pairs with arcs");



    public BAMViewTrack(String name, BAMIntervalTrack bamTrack) {
        super(name, FileFormat.INTERVAL_BAM, bamTrack);
        setColorScheme(getDefaultColorScheme());
        setDrawModes(getDefaultDrawModes());
        setDrawMode(VARIANTS_MODE);
    }

    private ColorScheme getDefaultColorScheme() {
        ColorScheme c = new ColorScheme();

        /* add settings here */
        c.addColorSetting("FORWARD_STRAND", new Color(0,131,192));
        c.addColorSetting("REVERSE_STRAND", new Color(0,174,255));
        c.addColorSetting("INVERTED_READ", Color.yellow);
        c.addColorSetting("INVERTED_MATE", Color.magenta);
        c.addColorSetting("EVERTED_PAIR", Color.green);
        c.addColorSetting("LINE", new Color(128,128,128));

        return c;
    }

    public List<Mode> getDefaultDrawModes()
    {
        List<Mode> modes = new ArrayList<Mode>();
        modes.add(STANDARD_MODE);
        modes.add(VARIANTS_MODE);
        modes.add(MATE_PAIRS_MODE);
        return modes;
    }

    @Override
    public void prepareForRendering(Range range) throws Exception {
        Resolution r = getResolution(range);
        List<Object> data = null;
        if (r == Resolution.VERY_HIGH || r == Resolution.HIGH) {
            data = retrieveAndSaveData(range);
        }
        for (TrackRenderer renderer : getTrackRenderers()) {
            renderer.getDrawingInstructions().addInstruction(DrawingInstructions.InstructionName.RANGE, range);
            renderer.getDrawingInstructions().addInstruction(DrawingInstructions.InstructionName.RESOLUTION, r);
            renderer.getDrawingInstructions().addInstruction(DrawingInstructions.InstructionName.COLOR_SCHEME, this.getColorScheme());
            if (getDrawMode().getName() == "MATE_PAIRS") {
                int maxDataValue = getMaxValue(data);
                renderer.getDrawingInstructions().addInstruction(DrawingInstructions.InstructionName.AXIS_RANGE, new AxisRange(range, new Range(0,(int)Math.round(Math.log(maxDataValue)))));
            }
            else renderer.getDrawingInstructions().addInstruction(DrawingInstructions.InstructionName.AXIS_RANGE, new AxisRange(range, getDefaultYRange()));
            renderer.getDrawingInstructions().addInstruction(DrawingInstructions.InstructionName.MODE, getDrawMode());
            renderer.setData(data);
        }
    }

    private int getMaxValue(List<Object>data) {
        double max = 0;
        for (Object o: data) {
            BAMIntervalRecord record = (BAMIntervalRecord)o;
            SAMRecord samRecord = record.getSamRecord();
            Interval interval = record.getInterval();
            boolean firstOfPair = samRecord.getFirstOfPairFlag();
            int startPos=0;
            int endPos=0;
            if (firstOfPair) {
 
                SAMRecord firstRecord;
                SAMRecord secondRecord;
                if (samRecord.getAlignmentStart() < samRecord.getMateAlignmentStart()) {
                    firstRecord = samRecord;
                    secondRecord = record.getMateRecord();
                }
                else {
                    firstRecord = record.getMateRecord();
                    secondRecord = samRecord;
                }
                BAMIntervalRecord.PairType pairType = record.getType();
                int alignmentStart = samRecord.getAlignmentStart();
                int alignmentEnd = samRecord.getAlignmentEnd();
                int mateAlignmentStart = samRecord.getMateAlignmentStart();
                switch (pairType) {
                    case NORMAL:
                        if (alignmentStart < mateAlignmentStart) {
                            startPos = alignmentEnd;
                            endPos = mateAlignmentStart;
                        }
                        else {
                            startPos = mateAlignmentStart;
                            endPos = alignmentEnd;
                        }
                        break;
                    case INVERTED_READ:
                        if (alignmentStart < mateAlignmentStart) {
                            startPos = alignmentStart;
                            endPos = mateAlignmentStart;
                        }
                        else {
                            startPos = mateAlignmentStart;
                            endPos = alignmentStart;
                        }
                        break;
                    case INVERTED_MATE:
                        // we actually have a mate record to interrogate here.
                        if (alignmentStart < mateAlignmentStart) {
                            firstRecord = samRecord;
                            secondRecord = record.getMateRecord();
                        }
                        else {
                            firstRecord = record.getMateRecord();
                            secondRecord = samRecord;
                        }
                        startPos = firstRecord.getAlignmentEnd();
                        endPos = secondRecord.getAlignmentEnd();
                        break;
                    case EVERTED:
                        // we actually have a mate record to interrogate here.
                        if (alignmentStart < mateAlignmentStart) {
                            firstRecord = samRecord;
                            secondRecord = record.getMateRecord();
                        }
                        else {
                            firstRecord = record.getMateRecord();
                            secondRecord = samRecord;
                        }
                        startPos = firstRecord.getAlignmentStart();
                        endPos = secondRecord.getAlignmentEnd();
                        break;
                }
            }
            else {
                continue;
            }
            double val = endPos - startPos + 1;
            if (val > max) max = val;
        }
        return (int)Math.ceil(max);
    }

    private Range getDefaultYRange() {
        return new Range(0, 1);
    }

    @Override
    public List<Object> retrieveData(Range range, Resolution resolution) throws Exception {
        return new ArrayList<Object>(getTrack().getRecords(range, resolution));
    }

    @Override
    public Resolution getResolution(Range range) {
        return getResolution(range, getDrawMode());
    }

    public Resolution getResolution(Range range, Mode mode)
    {
        return getDefaultModeResolution(range);       
    }

    public Resolution getDefaultModeResolution(Range range)
    {
        int length = range.getLength();

//        if (length < 10000) { return Resolution.VERY_HIGH; }
//        else if (length < 50000) { return Resolution.HIGH; }
//        else if (length < 1000000) { return Resolution.MEDIUM; }
//        else if (length < 10000000) { return Resolution.LOW; }
//        else if (length >= 10000000) { return Resolution.VERY_LOW; }
//        else { return Resolution.VERY_HIGH; }

        if (length < 5000) { return Resolution.VERY_HIGH; }
        else if (length < 10000) { return Resolution.HIGH; }
        else if (length < 20000) { return Resolution.MEDIUM; }
        else if (length < 10000000) { return Resolution.LOW; }
        else if (length >= 10000000) { return Resolution.VERY_LOW; }
        else { return Resolution.VERY_HIGH; }
    }

    @Override
    public Mode getDefaultDrawMode() {
        return VARIANTS_MODE;
    }
}
