/*
 *    Copyright 2010-2012 University of Toronto
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

package savant.util;

import java.util.*;

import savant.api.data.Interval;
import savant.api.data.IntervalRecord;
import savant.api.data.Record;


/**
 * Utility class to do build the data structures necessary to draw packed intervals.
 * 
 * @author vwilliams
 */
public class IntervalPacker {

    private List<Record> data;

    private static final double ONE_MILLIONTH = Math.pow(10, -6);

    public IntervalPacker(List<Record> data) {
        this.data = data;
    }

    public List<List<IntervalRecord>> pack(int breathingSpace) {

        int numdata = data.size();

        // Initialize some data structures to be filled by scanning through data
        ArrayList<List<IntervalRecord>> levels = new ArrayList<List<IntervalRecord>>();

        TreeMap<Double, Integer> rightMostPositions = new TreeMap<Double, Integer>();
        PriorityQueue<Integer> availableLevels = new PriorityQueue<Integer>();

        int highestLevel = -1; // highest level currently available to use

        // scan through data, keeping track of which intervals fit in which levels
        for (int i = 0; i < numdata; i++) {
            IntervalRecord record = (IntervalRecord)data.get(i);
            Interval inter = record.getInterval();

            int intervalEnd = inter.getEnd();
            int intervalStart = inter.getStart();

            // check for bogus intervals here
            if (!(intervalEnd >= intervalStart) || intervalEnd < 0 || intervalStart < 0)  continue;

            int level; // level we're going to use for this interval
            if (!rightMostPositions.isEmpty()) {
                SortedMap<Double, Integer> headMap = rightMostPositions.headMap((double)intervalStart);
                if (headMap != null && !headMap.isEmpty()) {
                    Iterator<Double> positions = headMap.keySet().iterator();
                    while (positions.hasNext()) {
                        Double position = positions.next();
                        availableLevels.add(headMap.get(position));
                        positions.remove();
                    }
                }
            }

            Integer availableLevel = availableLevels.poll();
            if (availableLevel == null) {
                level = ++highestLevel;
                List<IntervalRecord> newLevel = new ArrayList<IntervalRecord>();
                levels.add(newLevel);
            } else {
                level = availableLevel;
            }

            levels.get(level).add(record);
            double tieBreaker = (double)(level+1) * ONE_MILLIONTH;
            rightMostPositions.put((double)intervalEnd+(double)breathingSpace-tieBreaker, level);
        }

        return levels;


    }

    private boolean intersectsOne(List<Interval> intervalList, Interval inter, int gap) {
        for (Interval i2 : intervalList) {
            if (i2.intersects(inter)) {
                return true;
            } else {
                // check for breathing room
                if ((Math.abs(inter.getStart() - i2.getEnd()) < gap) || (Math.abs(i2.getStart() - inter.getEnd()) < 5)) {
                    return true; // not enough space between the intervals

                }
            }
        }
        return false;
    }


}
