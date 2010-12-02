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
 * TrackListChangedEvent.java
 * Created on Mar 11, 2010
 */

package savant.controller.event;

import savant.data.sources.DataSource;

import java.util.EventObject;
import java.util.List;

public class TrackListChangedEvent extends EventObject {

    private List<DataSource> dataSources;

    public TrackListChangedEvent(Object source, List<DataSource> dataSources) {
        super(source);
        this.dataSources = dataSources;
    }

    public List<DataSource> getDataSources() {
        return this.dataSources;
    }
}