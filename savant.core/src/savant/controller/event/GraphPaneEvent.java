/*
 *    Copyright 2009-2011 University of Toronto
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
package savant.controller.event;


/**
 * Something has changed in the state of our GraphPaneController.  Tell our clients about it.
 *
 * @author mfiume, tarkvara
 */
public class GraphPaneEvent {
    private final Type type;
    private final int mouseX;
    private final double mouseY;
    private final boolean yIntegral;
    private final String status;

    /**
     * Create an event representing a change in highlighting.
     */
    public GraphPaneEvent() {
        this.type = Type.HIGHLIGHTING;
        this.mouseX = -1;
        this.mouseY = Double.NaN;
        this.yIntegral = false;
        this.status = null;
    }

    public GraphPaneEvent(int mouseX, double mouseY, boolean yIntegral) {
        this.type = Type.MOUSE;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.yIntegral = yIntegral;
        this.status = null;
    }

    public GraphPaneEvent(String status) {
        this.type = Type.STATUS;
        this.mouseX = -1;
        this.mouseY = Double.NaN;
        this.yIntegral = false;
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public int getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public boolean isYIntegral() {
        return yIntegral;
    }

    public String getStatus() {
        return status;
    }

    public enum Type {
        HIGHLIGHTING,
        MOUSE,
        STATUS
    }
}