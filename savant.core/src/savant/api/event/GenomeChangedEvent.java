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
package savant.api.event;

import savant.api.adapter.GenomeAdapter;
import savant.data.types.Genome;

/**
 * Event set by the ReferenceController when the genome has changed and there is a
 * new list of references.
 *
 * @author tarkvara
 */
public class GenomeChangedEvent {
    private final GenomeAdapter oldGenome, newGenome;

    public GenomeChangedEvent(Genome oldGenome, Genome newGenome) {
        this.oldGenome = oldGenome;
        this.newGenome = newGenome;
    }

    public GenomeAdapter getOldGenome() {
        return oldGenome;
    }

    public GenomeAdapter getNewGenome() {
        return newGenome;
    }
}
