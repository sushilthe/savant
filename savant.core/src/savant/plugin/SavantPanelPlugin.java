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

package savant.plugin;

import com.jidesoft.docking.DockingManager;
import javax.swing.JPanel;
import savant.controller.TrackController;
import savant.plugin.SavantPlugin;
import savant.util.MiscUtils;
import savant.view.swing.Savant;
import savant.view.tracks.Track;


/**
 * Plugin which displays its contents in a JPanel managed by the Savant user-interface.
 * The canonical example is our own data table plugin.
 *
 * @author mfiume
 */
public abstract class SavantPanelPlugin extends SavantPlugin {

    /**
     * This method is called once during application life cycle to allow a third-party
     * plugin to initialize and show itself.
     *
     * @param panel parent panel for auxiliary data components
     */
    public abstract void init(JPanel panel);

    /**
     * Show or hide all UI elements associated with this plugin.  This includes both the
     * frame for the plugin's GUI as well as any layer canvasses which have been created on
     * the plugin's behalf.
     */
    public void setVisible(boolean value) {
        String frameKey = getTitle();
        DockingManager auxDockingManager = Savant.getInstance().getAuxDockingManager();
        MiscUtils.setFrameVisibility(frameKey, value, auxDockingManager);

        for (Track t: TrackController.getInstance().getTracks()) {
            JPanel layerCanvas = t.getFrame().getLayerCanvas(this, false);
            if (layerCanvas != null) {
                layerCanvas.setVisible(value);
            }
        }
    }

    /**
     * Is the associated plugin currently visible?
     */
    public boolean isVisible() {
        return Savant.getInstance().getAuxDockingManager().getFrame(getTitle()).isVisible();
    }
}
