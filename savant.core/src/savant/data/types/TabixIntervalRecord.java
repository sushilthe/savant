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
package savant.data.types;

import savant.api.adapter.RangeAdapter;
import savant.api.data.Interval;
import savant.api.data.IntervalRecord;
import savant.util.ColumnMapping;


/**
 * Immutable class to represent an interval record pulled from a Tabix file.
 * 
 * @author mfiume, tarkvara
 */
public class TabixIntervalRecord implements IntervalRecord {

    protected String[] values;
    protected final ColumnMapping mapping;
    protected Interval interval;
    private int count = 0;

    /**
     * Constructor. Clients should use static factory method valueOf() instead.
     */
    protected TabixIntervalRecord(String s, ColumnMapping mapping) {
        // String.split() will omit a trailing empty field.  We want it to be present as an empty value.
        if (s.endsWith("\t")) {
            values = (s + " ").split("\\t");
            values[values.length - 1] = "";
        } else {
            values = s.split("\\t");
        }
        this.mapping = mapping;

        int start = Integer.parseInt(values[mapping.start]);
        int end = mapping.end >= 0 ? Integer.parseInt(values[mapping.end]) : start; // VCF tabix files lack an end column.

        if (!mapping.oneBased) {
            start++;
        }
        interval = Interval.valueOf(start, end);
    }

    /**
     * Static factory method to construct a TabixIntervalRecord.  This checks the dataSource
     * to determine whether to return a plain TabixIntervalRecord or the more capable TabixRichIntervalRecord.
     *
     * @param s a tab-delimited string full of data
     * @param mapping defines how the columns in <code>s</code> should be interpreted
     */
    public static TabixIntervalRecord valueOf(String s, ColumnMapping mapping) {
        switch (mapping.format) {
            case RICH_INTERVAL:
                if (mapping == ColumnMapping.GFF) {
                    return new GFFIntervalRecord(s);
                } else if (mapping == ColumnMapping.GTF) {
                    return new GTFIntervalRecord(s);
                } else {
                    return new TabixRichIntervalRecord(s, mapping);
                }
            case VARIANT:
                return new VCFVariantRecord(s, mapping);
            default:
                return new TabixIntervalRecord(s, mapping);
        }
    }

    @Override
    public String getReference() {
        return values[mapping.chrom];
    }

    @Override
    public Interval getInterval() {
        return interval;
    }

    @Override
    public String getName() {
        return mapping.name >= 0 ? values[mapping.name] : null;
    }

    public String[] getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TabixIntervalRecord that = (TabixIntervalRecord) o;

        if (!interval.equals(that.interval)) return false;
        if (values.length != that.values.length) return false;
        for (int i = 0; i < values.length; i++) {
            if (!values[i].equals(that.values[i])) return false;
        }
        if (count != that.count) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (interval != null ? interval.hashCode() : 0);
        hash = 47 * hash + values.hashCode();
        hash = 47 * hash + count;
        return hash;
    }

    //note: count is a made up to differentiate between intervals in same position
    public void setCount(int count){
        this.count = count;
    }

    @Override
    public int compareTo(Object o) {

        TabixIntervalRecord that = (TabixIntervalRecord) o;

        // Compare point
        if (!interval.equals(that.interval)) {
            return interval.compareTo(that.interval);
        }
        
        // Compare other fields (for intervals in the exact same location)
        if (values.length < that.values.length) {
            return -1;
        } else if (values.length > that.values.length) {
            return 1;
        }
        for (int i = 0; i < values.length; i++) {
            int compare = values[i].compareTo(that.values[i]);
            if (compare != 0) {
                return compare;
            }
        }

        // Compare count
        // Note: count is a made up to differentiate between intervals in same position
        if (count < that.count) {
            return -1;
        } else if (count > that.count) {
            return 1;
        }
        
        // All checks yield exact same value
        return 0;
    }
    
    /**
     * For GFF records, we may sometimes need to request records from an expanded range, so that we can pick up all a gene's blocks.
     * @param r the unexpanded range
     * @return null
     */
    public RangeAdapter getExpandedRange(RangeAdapter r) {
        return null;
    }
}
