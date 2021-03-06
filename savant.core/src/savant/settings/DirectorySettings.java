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

package savant.settings;

import java.io.File;
import savant.util.MiscUtils;


/**
 *
 * @author AndrewBrook
 */
public class DirectorySettings {
    private static PersistentSettings settings = PersistentSettings.getInstance();

    private static File savantDir;

    private static final String CACHE_DIR_KEY = "CacheDir";
    private static final String PLUGINS_DIR_KEY = "PluginsDir";
    private static final String PROJECTS_DIR_KEY = "ProjectsDir";

    public static File getSavantDirectory() {
        if (savantDir == null) {
            File f = new File(System.getProperty("user.home"), MiscUtils.WINDOWS ? "savant" : ".savant");
            if (!f.exists()) {
                f.mkdir();
            }
            savantDir = f;
        }
        return savantDir;
    }

    public static File getLibsDirectory() {
        if (MiscUtils.MAC) {
            File result = new File(com.apple.eio.FileManager.getPathToApplicationBundle() + "/Contents/Resources/Java");
            if (result.exists()) {
                return result;
            }
        }
        return new File("lib");
    }

    private static File getDirectory(String key, String dirName) {
        File result = settings.getFile(key);
        if (result == null) {
            result = new File(getSavantDirectory(), dirName);
        }
        if (!result.exists()) {
            result.mkdirs();
        }
        return result;
    }

    private static void setDirectory(String key, File value) {
        if (!value.exists()) {
            value.mkdirs();
        }
        settings.setFile(key, value);
    }

    public static File getCacheDirectory() {
        return getDirectory(CACHE_DIR_KEY, "cache");
    }

    public static File getPluginsDirectory(){
        return getDirectory(PLUGINS_DIR_KEY, "plugins");
    }

    public static File getProjectsDirectory() {
        return getDirectory(PROJECTS_DIR_KEY, "projects");
    }

    public static File getTmpDirectory() {
        return new File(System.getProperty("java.io.tmpdir"));
    }

    public static void setPluginsDirectory(File dir) {
        setDirectory(PLUGINS_DIR_KEY, dir);
    }

    public static void setCacheDirectory(File dir) {
        setDirectory(CACHE_DIR_KEY, dir);
    }
}
