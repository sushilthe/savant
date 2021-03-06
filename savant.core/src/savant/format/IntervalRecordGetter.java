/*
 *    Copyright 2010-2011 University of Toronto
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
package savant.format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import savant.api.util.RangeUtils;
import savant.api.data.IntervalRecord;
import savant.file.SavantROFile;
import savant.util.Range;
import savant.util.SavantFileUtils;
import savant.util.IntervalRecordComparator;


/**
 *
 * @author mfiume, tarkvara
 */
public class IntervalRecordGetter {

    public static List<IntervalRecord> getData(SavantROFile dFile, String reference, Range r, IntervalTreeNode n) throws IOException {
        List<IntervalRecord> result = new ArrayList<IntervalRecord>();
        getData(dFile, result, reference, r, n);
        Collections.sort(result, new IntervalRecordComparator());
        return result;
    }

    private static void getData(SavantROFile dFile, List<IntervalRecord> data, String reference, Range r, IntervalTreeNode n) throws IOException {

        if (RangeUtils.intersects(r, n.range)) {
            //System.out.println("\tBin : " + n.range + " overlaps range " + r);
            data.addAll(getIntersectingIntervals(dFile, reference, r,n));
            for (IntervalTreeNode child : n.children) {
                if (child != null && child.subtreeSize > 0 && RangeUtils.intersects(child.range,r)) {
                    getData(dFile, data, reference, r,child);
                }
            }
        }
    }

    private static List<IntervalRecord> getIntersectingIntervals(SavantROFile dFile, String reference, Range r, IntervalTreeNode n) throws IOException {

        //System.out.println("\t\tGetting intersecting intervals");
        //System.out.println("Node range: " + n.range + " size: " + n.size);

        List<IntervalRecord> data = new ArrayList<IntervalRecord>();

        if (n.size > 0) {

            //System.out.println("Size of file: " + dFile.lengthSuper());
            //System.out.println("Position in file: " + (dFile.getHeaderOffset() + dFile.getReferenceOffset(reference) + n.startByte));

            //System.out.println("\tSeeking from : " + dFile.getFilePointer());

            dFile.seek(reference, n.startByte);

            //System.out.println("\tStart byte: " + n.startByte);
            //System.out.println("\tSeeking to : " + dFile.getFilePointer());

            for (int i = 0; i < n.size; i++) {

                //System.out.println("Reading record ...");
                /*
                for (FieldType ft : dFile.getFields()) {
                    System.out.println(ft);
                }
                 */

                List<Object> record = SavantFileUtils.readBinaryRecord(dFile, dFile.getFields());

                //System.out.println("Interval:");
                //for (Object o : record) { System.out.println("\t" + o); }

                IntervalRecord ir = SavantFileFormatterUtils.convertRecordToInterval(record, dFile.getFileType(), dFile.getFields());

                if (ir.getInterval().intersectsRange(r)) {
                    data.add(ir);
                }
            }
        }
        return data;
    }

    public static List<IntervalRecord> getRecordsInBin(SavantROFile dFile, String reference, IntervalTreeNode n) throws IOException {

        List<IntervalRecord> recs = new ArrayList<IntervalRecord>(n.size);
        if (n.size > 0) {
            dFile.seek(reference, n.startByte);
            for (int i = 0; i < n.size; i++) {
                List<Object> record = SavantFileUtils.readBinaryRecord(dFile, dFile.getFields());
                IntervalRecord ir = SavantFileFormatterUtils.convertRecordToInterval(record, dFile.getFileType(), dFile.getFields());
                recs.add(ir);
            }
        }
        
        return recs;
    }
}
