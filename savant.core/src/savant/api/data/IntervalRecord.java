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

package savant.api.data;

/**
 * Interface to represent a record which contains an interval.
 *
 * @author vwilliams, tarkvara
 */
public interface IntervalRecord extends Record {

    /**
     * @return the interval contained by this object
     */
    public Interval getInterval();

    /**
     * @return a string which describes this particular interval
     */
    public String getName();
}
