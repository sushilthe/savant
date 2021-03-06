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

import java.io.File;

import savant.api.adapter.GenomeAdapter;
import savant.api.adapter.TrackAdapter;
import savant.api.event.GenomeChangedEvent;
import savant.controller.GenomeController;
import savant.data.types.Genome;
import savant.view.tracks.Track;


/**
 * Utility methods for dealing with Savant genomes.
 * @author tarkvara
 */
public class GenomeUtils {

    private static GenomeController genomeController = GenomeController.getInstance();

    /**
     * Tell whether Savant has loaded a genome yet.
     *
     * @return true if a genome is loaded
     */
    public static boolean isGenomeLoaded() {
        return genomeController.isGenomeLoaded();
    }

    /**
     * Get the loaded genome.
     *
     * @return the loaded genome
     */
    public static GenomeAdapter getGenome() {
        return genomeController.getGenome();
    }

    /**
     * Set the current genome.
     *
     * @param genome the genome to set
     */
    public static void setGenome(GenomeAdapter genome) {
        genomeController.setGenome((Genome) genome);
    }


    /**
     * Create a placeholder genome with the given name and length.
     */
    public static GenomeAdapter createGenome(String name, int length) {
        return new Genome(name, length);
    }

    /**
     * Create a new genome from the given track full of sequence data.
     * 
     * @param seqTrack a track containing sequence information
     * @return a genome object for the given sequence
     */
    public static GenomeAdapter createGenome(TrackAdapter seqTrack) {
        return Genome.createFromTrack((Track)seqTrack);
    }


    /**
     * Create a new genome from the given file full of sequence data.
     *
     * @param f a file containing sequence information
     * @return a genome object for the given sequence
     */
    public static GenomeAdapter createGenome(File f) throws Throwable {
        return createGenome(TrackUtils.createTrack(f)[0]);
    }
    
    /**
     * Add a listener to monitor changes in the reference genome.
     *
     * @param l the listener to be added
     */
    public static void addGenomeChangedListener(Listener<GenomeChangedEvent> l) {
        genomeController.addListener(l);
    }

    /**
     * Remove a genome change listener.
     *
     * @param l the listener to be removed
     */
    public static void removeGenomeChangedListener(Listener<GenomeChangedEvent> l) {
        genomeController.removeListener(l);
    }
}
