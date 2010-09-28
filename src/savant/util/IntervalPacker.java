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
 * IntervalPacker.java
 * Created on Feb 1, 2010
 */

package savant.util;

import savant.data.types.Interval;
import savant.data.types.IntervalRecord;

import java.util.*;

/**
 * Utility class to do build the data structures necessary to draw packed intervals.
 * @author vwilliams
 */
public class IntervalPacker {

    private List<Object> data;

    public IntervalPacker(List<Object> data) {
        this.data = data;
    }

//    public Map<Integer, ArrayList<IntervalRecord>> pack(int breathingSpace) {
    public ArrayList<List<IntervalRecord>> pack(int breathingSpace) {

        int numdata = data.size();

        // Initialize some data structures to be filled by scanning through data
        TreeMap<Float, Integer> rightMostPositions = new TreeMap<Float, Integer>();
        ArrayList<List<IntervalRecord>> levels = new ArrayList<List<IntervalRecord>>();

//        ArrayList<List<Interval>> levels = new ArrayList<List<Interval>>();
//        Map<Integer, ArrayList<IntervalRecord>> intervals= new HashMap<Integer, ArrayList<IntervalRecord>>();
//        levels.add(new ArrayList<Interval>());
        rightMostPositions.put(0.000001f, 0);
        levels.add(new ArrayList<IntervalRecord>());


        // scan through data, keeping track of which intervals fit in which levels
        for (int i = 0; i < numdata; i++) {

            IntervalRecord record = (IntervalRecord)data.get(i);
            Interval inter = record.getInterval();

            int intervalEnd = inter.getEnd();
            int intervalStart = inter.getStart();

            // check for bogus intervals here
            if (!(intervalEnd >= intervalStart) || intervalEnd < 0 || intervalStart < 0)  continue;

            boolean fitsInExistingLevel = false;

            if (intervalStart >= rightMostPositions.firstKey()) {
                SortedMap<Float, Integer> headMap = rightMostPositions.headMap((float)intervalStart);
                if (headMap != null && !headMap.isEmpty()) {
                    Float positionKey = headMap.firstKey();
                    int fitLevel = rightMostPositions.get(positionKey);
                    fitsInExistingLevel = true;
                    levels.get(fitLevel).add(record);
                    rightMostPositions.remove(positionKey);
                    rightMostPositions.put((float)intervalEnd+(float)breathingSpace+(float)fitLevel/(10^6), fitLevel);
                }

            }
            if (!fitsInExistingLevel) {
                List<IntervalRecord> newLevel = new ArrayList<IntervalRecord>();
                newLevel.add(record);
                levels.add(newLevel);
                int levelNumber = levels.size()-1;
                rightMostPositions.put((float)intervalEnd+(float)breathingSpace+(float)+(float)levelNumber/(10^6), levelNumber);
            }
        }
        return levels;

//            for (int j=0; j<levels.size(); j++) {
//
//                // compare the left edge of the interval to the rightmost limit stored for the level
//                if (!intersectsOne(levels.get(j),inter, breathingSpace)) {
//
//                    fitsInExistingLevel = true;
//                    levels.get(j).add(inter);
//
//                    // store the interval in the 'intervals' Map as an IntervalRecord keyed by level
//                    ArrayList<IntervalRecord> intervalList;
//                    if (!intervals.keySet().contains(j)) {
//                        // start filling a new level
//                        intervalList = new ArrayList<IntervalRecord>();
//                        intervals.put(j, intervalList);
//                    }
//                    else {
//                        // level already exists
//                        intervalList = intervals.get(j);
//                    }
//                    intervalList.add(record);
//
//                    break;
//                }
//            }
//            if (!fitsInExistingLevel) {
//                // a new level is required
//                List<Interval> newLevel = new ArrayList<Interval>();
//                ArrayList<IntervalRecord> newLevelsRecords = new ArrayList<IntervalRecord>();
//
//                newLevel.add(inter);
//                newLevelsRecords.add(record);
//
//                levels.add(newLevel);
//                intervals.put(intervals.size(), newLevelsRecords);
//            }
//        }
//        return intervals;
    }

    public boolean intersects(Interval i1, Interval i2) {
        // FIXME: i think this should be i1.getStart() < i2.getEnd()-1 && i2.getStart() < i1.getEnd()-1;
        return i1.getStart() < i2.getEnd() && i2.getStart() < i1.getEnd();
    }

    private boolean intersectsOne(List<Interval> intervalList, Interval inter, int gap) {
        for (Interval i1 : intervalList) {
            if (intersects(i1,inter)) {
                return true;
            }
            else {
                // check for breathing room
                if ((Math.abs(inter.getStart() - i1.getEnd()) < gap) || (Math.abs(i1.getStart() - inter.getEnd()) < 5)) {
                    return true; // not enough space between the intervals

                }
            }
        }
        return false;
    }


}
