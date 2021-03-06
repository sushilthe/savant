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

package savant.data.sources;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import savant.api.adapter.RangeAdapter;
import savant.api.adapter.RecordFilterAdapter;
import savant.api.data.IntervalRecord;
import savant.api.data.DataFormat;
import savant.api.util.Resolution;
import savant.data.types.GenericIntervalRecord;
import savant.file.*;
import savant.format.IntervalRecordGetter;
import savant.format.IntervalSearchTree;
import savant.format.SavantFileFormatter;
import savant.util.Range;


/**
 * Class to represent an track of generic intervals. Responsible for reading records within a given range.
 *
 * @author vwilliams
 */
public class GenericIntervalDataSource extends DataSource<GenericIntervalRecord> {
    private static Log LOG = LogFactory.getLog(GenericIntervalDataSource.class);

    private SavantROFile dFile;

    private Map<String,IntervalSearchTree> refnameToIntervalBSTIndex;

    public GenericIntervalDataSource(URI uri) throws IOException, SavantFileNotFormattedException {
        dFile = new SavantROFile(uri, FileType.INTERVAL_GENERIC);
        refnameToIntervalBSTIndex = SavantFileFormatter.readIntervalBSTs(dFile);

        if (LOG.isDebugEnabled()) {
            LOG.debug("HEADER SIZE && : "+  dFile.getFilePointer());

            LOG.debug("Found indices for:");
            for (String refname : refnameToIntervalBSTIndex.keySet()) {
                LOG.debug(refname);
            }
        }

    }

    public IntervalSearchTree getIntervalSearchTreeForReference(String refname) {
        return refnameToIntervalBSTIndex.get(refname);
    }

    @Override
    public List<GenericIntervalRecord> getRecords(String reference, RangeAdapter range, Resolution resolution, RecordFilterAdapter filt) throws IOException {
        IntervalSearchTree ist = getIntervalSearchTreeForReference(reference);

        if (ist == null) { return new ArrayList<GenericIntervalRecord>(); }


        List<IntervalRecord> data = IntervalRecordGetter.getData(dFile, reference, (Range)range, ist.getRoot());

        List<GenericIntervalRecord> girList = new ArrayList<GenericIntervalRecord>(data.size());
        for (int i = 0; i < data.size(); i++) {
            girList.add((GenericIntervalRecord) data.get(i));
        }

        return girList;
    }

    @Override
    public void close() {
        try {
            dFile.close();
        } catch (IOException ex) {
            LOG.warn("Error closing " + dFile, ex);
        }
    }

    @Override
    public Set<String> getReferenceNames() {
        return dFile.getReferenceMap().keySet();
    }

    @Override
    public URI getURI() {
        return dFile.getURI();
    }

    @Override
    public final DataFormat getDataFormat() {
        return DataFormat.GENERIC_INTERVAL;
    }

    @Override
    public final String[] getColumnNames() {
        return GenericIntervalRecord.COLUMN_NAMES;
    }
}
