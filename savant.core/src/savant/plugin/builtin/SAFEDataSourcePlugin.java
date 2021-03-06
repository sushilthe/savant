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

package savant.plugin.builtin;

import savant.api.adapter.DataSourceAdapter;
import savant.plugin.SavantDataSourcePlugin;

public class SAFEDataSourcePlugin extends SavantDataSourcePlugin {

    @Override
    public void init() {
        
    }

    @Override
    public String getTitle() {
        return "SAFE Repository";
    }

    private boolean gotCredentials = false;

    @Override
    public DataSourceAdapter getDataSource() {

        try {
            SAFEBrowser d = SAFEBrowser.getInstance();
            d.setVisible(true);
            return d.getDataSource();
        } catch (Exception ex) {
            return null;
        }
    }
}
