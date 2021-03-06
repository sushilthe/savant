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

package savant.ucsc;

/**
 * Class which keeps track of information about a genome in the UCSC database, most importantly
 * the name of the associated MySQL database.
 *
 * @author tarkvara
 */
public class GenomeDef {
    final String database;
    final String label;

    public GenomeDef(String database, String label) {
        this.database = database;
        this.label = label;
    }

    @Override
    public String toString() {
        return database + " - " + label;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GenomeDef) {
            return database.equals(((GenomeDef)o).database);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.database != null ? this.database.hashCode() : 0);
        return hash;
    }
    
    public String getDatabase() {
        return database;
    }
}

