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

package savant.data.event;

import java.util.List;

import savant.data.types.Record;


/**
 * Event class which allows asynchronous retrieval of data.
 *
 * @author tarkvara
 */
public class DataRetrievalEvent {
    public enum Type {
        STARTED,
        COMPLETED,
        FAILED
    };
    Type type;
    List<Record> data;
    Exception error;

    /**
     * Constructor for retrieval starting.
     */
    public DataRetrievalEvent() {
        this.type = Type.STARTED;
    }

    /**
     * Constructor when data is successfully retrieved.
     * @param data the records retrieved
     */
    public DataRetrievalEvent(List<Record> data) {
        this.type = Type.COMPLETED;
        this.data = data;
    }

    /**
     * Constructor when retrieval has failed.
     *
     * @param error
     */
    public DataRetrievalEvent(Exception error) {
        this.type = Type.FAILED;
        this.error = error;
    }

    public List<Record> getData() {
        return data;
    }

    public Exception getError() {
        return error;
    }
}