/*
 *    Copyright 2011 University of Toronto
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

package savant.sql;

import java.io.IOException;
import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import net.sf.samtools.util.SeekableHTTPStream;

import net.sf.samtools.util.SeekableStream;

import savant.api.adapter.RangeAdapter;
import savant.data.types.Continuous;
import savant.data.types.GenericContinuousRecord;
import savant.file.DataFormat;
import savant.util.NetworkUtils;
import savant.util.Resolution;


/**
 * DataSource class which retrieves continuous data from an SQL database.  The actual
 * data values are stored in an external Wib (binned Wig) file.  This is very idiosyncratic
 * to UCSC, so the class might more properly belong in the savant.ucsc plugin.
 *
 * @author tarkvara
 */
public class WigSQLDataSource extends SQLDataSource<GenericContinuousRecord> {

    WigSQLDataSource(MappedTable table, Set<String> references) throws SQLException {
        super(table, references);
    }

    @Override
    public List<GenericContinuousRecord> getRecords(String reference, RangeAdapter range, Resolution resolution) throws IOException {
        // TODO: This is wrong.  We should be stuffing in NaNs, not zeroes.
        GenericContinuousRecord[] result = new GenericContinuousRecord[range.getLengthAsInt()];
        for (int i = 0; i < range.getLengthAsInt(); i++) {
            result[i] = GenericContinuousRecord.valueOf(reference, range.getFrom() + i, Continuous.valueOf(0.0F));
        }
        try {
            ResultSet rs = table.database.executeQuery("SELECT * FROM %s WHERE %s = '%s' AND ((%s >= '%d' AND %s <= '%d') OR (%s >= '%d' AND %s <= '%d') OR (%s < '%d' AND %s > '%d')) ORDER BY %s", table, columns.chrom, reference,
                    columns.start, range.getFrom(), columns.start, range.getTo(),
                    columns.end, range.getFrom(), columns.end, range.getTo(),
                    columns.start, range.getFrom(), columns.end, range.getTo(),
                    columns.start);
            URI wibURI = null;
            SeekableStream wibStream = null;
            while (rs.next()) {
                String chrom = rs.getString(columns.chrom);
                int start = rs.getInt(columns.start);
                int span = columns.span != null ? rs.getInt(columns.span) : 1;
                int count = rs.getInt(columns.count);
                int offset = rs.getInt(columns.offset);
                String file = rs.getString(columns.file);
                float lowerLimit = rs.getFloat(columns.lowerLimit);
                float dataRange = rs.getFloat(columns.dataRange);

                // If the URI has changed, open a new stream.
                URI newWibURI = URI.create("http://hgdownload.cse.ucsc.edu" + file);
                if (!newWibURI.equals(wibURI)) {
                    if (wibStream != null) {
                        wibStream.close();
                    }
                    wibURI = newWibURI;
                    // Note, we can't use NetworkUtils.getSeekableStreamForURI because it imposes
                    // block-based cacheing on us.  In theory, it should work, but for 1.4.4 it
                    // throws an EOFError.
                    wibStream = new SeekableHTTPStream(wibURI.toURL());
//                    wibStream = NetworkUtils.getSeekableStreamForURI(wibURI);
                }

                wibStream.seek(offset);
                byte[] buf = new byte[count];
                wibStream.read(buf);

                int chromPos = start + 1;
                for (int i = 0; i < count; ++i, chromPos += span) {
                    if (buf[i] < 128) {
                        float value = lowerLimit + dataRange * buf[i] / 127.0F;
                        for (int j = 0; j < span; j++) {
                            if (chromPos + j >= range.getFromAsInt()) {
                                if (chromPos + j > range.getToAsInt()) {
                                    break;
                                }
                                result[chromPos + j - range.getFromAsInt()] = GenericContinuousRecord.valueOf(reference, chromPos + j, Continuous.valueOf(value));
                            }
                        }
                    }
                }
            }
        } catch (SQLException sqlx) {
            LOG.error(sqlx);
            throw new IOException(sqlx);
        }
        return Arrays.asList(result);
    }


    @Override
    public DataFormat getDataFormat() {
        return DataFormat.CONTINUOUS_GENERIC;
    }
}