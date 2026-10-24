/**
 * Copyright 2007 Wei-ju Wu
 *
 * This file is part of TinyUML.
 *
 * TinyUML is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * TinyUML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TinyUML; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.tinyuml.ui.diagram;

import java.awt.geom.Rectangle2D;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.umldraw.structure.StructureDiagram;

/**
 * Calculations for the "Fit to window" action (RF-005). The methods only read
 * the diagram, they never change the position or the size of an element.
 *
 * @version 1.0
 */
public final class DiagramFitCalculator {

  /**
   * The smallest zoom factor the action can set.
   */
  public static final double MIN_SCALE = 0.1;

  /**
   * The largest zoom factor the action can set, so a diagram with only a few
   * small elements is not magnified too much.
   */
  public static final double MAX_SCALE = 2.0;

  /**
   * Free space in pixels between the elements and the window border.
   */
  public static final double PADDING = 20;

  /**
   * Private constructor, only static methods.
   */
  private DiagramFitCalculator() { }

  /**
   * Returns the rectangle that contains all elements of the diagram,
   * relations included, in diagram coordinates. If the diagram is empty,
   * the bounds of the diagram itself are returned.
   * @param diagram the diagram
   * @return the bounding rectangle
   */
  public static Rectangle2D getElementsBounds(StructureDiagram diagram) {
    Rectangle2D result = null;
    for (DiagramElement element : diagram.getChildren()) {
      Rectangle2D elementBounds = element.getAbsoluteBounds();
      if (result == null) {
        result = (Rectangle2D) elementBounds.clone();
      } else {
        result.add(elementBounds);
      }
    }
    if (result == null) {
      result = diagram.getAbsoluteBounds();
    }
    return result;
  }

  /**
   * Calculates the zoom factor that makes the specified bounds fit into the
   * visible area. The result is limited to [MIN_SCALE, MAX_SCALE].
   * @param bounds the bounds to show, in diagram coordinates
   * @param viewWidth the width of the visible area in pixels
   * @param viewHeight the height of the visible area in pixels
   * @return the zoom factor
   */
  public static double calculateScaleFactor(Rectangle2D bounds,
    double viewWidth, double viewHeight) {
    double width = Math.max(bounds.getWidth(), 1.0);
    double height = Math.max(bounds.getHeight(), 1.0);
    double factor = Math.min((viewWidth - 2 * PADDING) / width,
      (viewHeight - 2 * PADDING) / height);
    return Math.max(MIN_SCALE, Math.min(MAX_SCALE, factor));
  }
}
