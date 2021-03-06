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

package savant.api.util;

import savant.api.adapter.TrackAdapter;
import savant.api.event.SelectionChangedEvent;
import savant.api.data.Record;
import savant.selection.SelectionController;

/**
 * Utilities for data selection in Savant.
 *
 * @author mfiume
 */
public class SelectionUtils {

    private static SelectionController sc = SelectionController.getInstance();

    /**
     * Add a selection to the given track.  Equivalent to selecting the given data point in the user interface
     * (at which point it would normally turn green).
     *
     * @param t the track for which to add the selection
     * @param data the data point to select
     */
    public static void addSelection(TrackAdapter t, Record data) {
        sc.addSelection(t.getName(), data);
    }

    /**
     * Toggle the selection state of the given data point.
     *
     * @param t the track for which to toggle the selection
     * @param data the data point to select/deselect
     */
    public static void toggleSelection(TrackAdapter t, Record data) {
        sc.toggleSelection(t.getName(), data);
    }

    /**
     * Subscribe a listener to be notified when the selection changes.
     *
     * @param l the listener to subscribe
     */
    public static void addSelectionChangedListener(Listener<SelectionChangedEvent> l) {
        sc.addListener(l);
    }

    /**
     * Unsubscribe a listener from being notified when the selection changes.
     *
     * @param l the listener to unsubscribe
     */
    public static void removeSelectionChangedListener(Listener<SelectionChangedEvent> l) {
        sc.removeListener(l);
    }
}
