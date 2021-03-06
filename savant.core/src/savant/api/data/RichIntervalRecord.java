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
package savant.api.data;

import java.util.List;
import savant.data.types.ItemRGB;


/**
 * Extends an IntervalRecord by adding all the various gene-related fields which are
 * typically found in a Bed file.
 *
 * @author tarkvara
 */
public interface RichIntervalRecord extends IntervalRecord {
    List<Block> getBlocks();
    float getScore();
    Strand getStrand();
    int getThickStart();
    int getThickEnd();
    ItemRGB getItemRGB();
    String getAlternateName();
}
