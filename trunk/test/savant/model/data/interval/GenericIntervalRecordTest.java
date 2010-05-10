/*
 *    Copyright 2009 Vanessa Williams
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

/*
 * GenericIntervalRecordTest.java
 * Created on Jan 11, 2010
 */

package savant.model.data.interval;

import savant.model.GenericIntervalRecord;
import savant.model.Interval;
import junit.framework.TestCase;

public class GenericIntervalRecordTest extends TestCase {

    private GenericIntervalRecord a, b, c, d;

    public void setUp() {
        a = new GenericIntervalRecord(new Interval(4,57), "abcde");
        b = new GenericIntervalRecord(new Interval(4,57), "abcde");
        c = new GenericIntervalRecord(new Interval(4,57), "abcde");
        d = new GenericIntervalRecord(new Interval(1,82), "fghij");
    }

    public void testConstruct() {
        try {
            // this is an invalid Interval item and should fail
            GenericIntervalRecord e = new GenericIntervalRecord(null, "abcde");
            fail("Expected IllegalArgumentException");
        } catch (Exception success) {}
    }
    public void testEquals() {

        try {
            // reflexivity: A = A
            assertTrue(a.equals(a));

            // symmetry: A = B & B = A
            assertTrue(a.equals(b));
            assertTrue(b.equals(a));

            // transitivity: A = B & B = C & A = C
            assertTrue(b.equals(c));
            assertTrue(a.equals(c));

            // inequality: A <> D
            assertFalse(a.equals(d));

            // A <> null
            assertFalse(a.equals(null));

            // A <> object of another type
            assertFalse(a.equals(new Object()));

        } catch (Exception e) {
            fail("Unexpected exception " + e.getMessage());
        }
    }

    public void testHashCode() {

        try {
            // objects which are equal have same hash
            assertEquals(a.hashCode(), b.hashCode());

            // unequal objects have unequal hashes
            assertFalse(a.hashCode() == d.hashCode());
        } catch (Exception e) {
            fail("Unexpected exception " + e.getMessage());
        }
    }

    public void testToString() {

        // make sure no null pointers are thrown
        try {
            a.toString();
        }
        catch(Exception e) {
            fail("Unexpected exception " + e.getMessage());
        }
    }
}
