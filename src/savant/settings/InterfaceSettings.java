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

package savant.settings;

import savant.file.DataFormat;


/**
 *
 * @author AndrewBrook
 */
public class InterfaceSettings {
    private static final PersistentSettings SETTINGS = PersistentSettings.getInstance();

    // The defaults
    private static final int BAM_INTERVAL_HEIGHT = 12;
    private static final int RICH_INTERVAL_HEIGHT = 16;
    private static final int GENERIC_INTERVAL_HEIGHT = 12;

    // The property keys.
    private static final String BAM_INTERVAL_HEIGHT_KEY = "BamIntervalHeight";
    private static final String RICH_INTERVAL_HEIGHT_KEY = "RichIntervalHeight";
    private static final String GENERIC_INTERVAL_HEIGHT_KEY = "GenericIntervalHeight";
    private static final String POPUPS_DISABLED_KEY = "PopupsDisabled";

    public static int getBamIntervalHeight() {
        return SETTINGS.getInt(BAM_INTERVAL_HEIGHT_KEY, BAM_INTERVAL_HEIGHT);
    }
    
    public static int getRichIntervalHeight() {
        return SETTINGS.getInt(RICH_INTERVAL_HEIGHT_KEY, RICH_INTERVAL_HEIGHT);
    }
    
    public static int getGenericIntervalHeight() {
        return SETTINGS.getInt(GENERIC_INTERVAL_HEIGHT_KEY, GENERIC_INTERVAL_HEIGHT);
    }

    public static int getIntervalHeight(DataFormat type) {
        switch (type) {
            case INTERVAL_BAM:
                return getBamIntervalHeight();
            case INTERVAL_RICH:
                return getRichIntervalHeight();
            default:
                return getGenericIntervalHeight();
        }
    }

    public static boolean isPopupsDisabled() {
        return SETTINGS.getBoolean(POPUPS_DISABLED_KEY, false);
    }

    public static void setBamIntervalHeight(int value) {
        SETTINGS.setInt(BAM_INTERVAL_HEIGHT_KEY, value);
    }

    public static void setRichIntervalHeight(int value) {
        SETTINGS.setInt(RICH_INTERVAL_HEIGHT_KEY, value);
    }

    public static void setGenericIntervalHeight(int value) {
        SETTINGS.setInt(GENERIC_INTERVAL_HEIGHT_KEY, value);
    }

    public static void setPopupsDisabled(boolean value) {
        SETTINGS.setBoolean(POPUPS_DISABLED_KEY, value);
    }
}