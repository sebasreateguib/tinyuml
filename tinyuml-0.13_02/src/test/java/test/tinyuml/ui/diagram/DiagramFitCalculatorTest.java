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
package test.tinyuml.ui.diagram;

import java.awt.geom.Rectangle2D;
import junit.framework.TestCase;
import org.tinyuml.model.ElementType;
import org.tinyuml.model.UmlModelImpl;
import org.tinyuml.umldraw.shared.UmlNode;
import org.tinyuml.umldraw.structure.StructureDiagram;
import org.tinyuml.ui.diagram.DiagramFitCalculator;

/**
 * Tests for the DiagramFitCalculator class (RF-005).
 *
 * @version 1.0
 */
public class DiagramFitCalculatorTest extends TestCase {

  private StructureDiagram diagram;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setUp() {
    diagram = new StructureDiagram(new UmlModelImpl());
  }

  /**
   * Creates a node and adds it to the diagram at the specified position.
   * @param x the absolute x position
   * @param y the absolute y position
   * @return the node
   */
  private UmlNode addNode(double x, double y) {
    UmlNode node = diagram.getElementFactory().createNode(ElementType.CLASS);
    diagram.addChild(node);
    node.setAbsolutePos(x, y);
    return node;
  }

  /**
   * The bounds contain all elements.
   */
  public void testBoundsContainAllElements() {
    UmlNode node1 = addNode(50, 60);
    UmlNode node2 = addNode(400, 300);
    Rectangle2D bounds = DiagramFitCalculator.getElementsBounds(diagram);
    assertTrue(bounds.contains(node1.getAbsoluteBounds()));
    assertTrue(bounds.contains(node2.getAbsoluteBounds()));
    assertEquals(50.0, bounds.getX());
    assertEquals(60.0, bounds.getY());
  }

  /**
   * An empty diagram uses the diagram bounds.
   */
  public void testEmptyDiagram() {
    Rectangle2D bounds = DiagramFitCalculator.getElementsBounds(diagram);
    assertEquals(diagram.getAbsoluteBounds(), bounds);
  }

  /**
   * Calculating the bounds does not change the elements (RF-005).
   */
  public void testElementsAreNotModified() {
    UmlNode node = addNode(120, 80);
    Rectangle2D before = (Rectangle2D) node.getAbsoluteBounds().clone();
    DiagramFitCalculator.getElementsBounds(diagram);
    assertEquals(before, node.getAbsoluteBounds());
  }

  /**
   * A big diagram in a small window is scaled down.
   */
  public void testScaleDown() {
    Rectangle2D bounds = new Rectangle2D.Double(0, 0, 2000, 1000);
    double factor = DiagramFitCalculator.calculateScaleFactor(bounds,
      1040, 1040);
    // width limits: (1040 - 2 * 20) / 2000 = 0.5
    assertEquals(0.5, factor, 0.0001);
  }

  /**
   * The height can be the limiting dimension.
   */
  public void testHeightLimits() {
    Rectangle2D bounds = new Rectangle2D.Double(0, 0, 100, 1000);
    double factor = DiagramFitCalculator.calculateScaleFactor(bounds,
      1040, 540);
    // height limits: (540 - 2 * 20) / 1000 = 0.5
    assertEquals(0.5, factor, 0.0001);
  }

  /**
   * The factor is limited to the allowed range.
   */
  public void testLimits() {
    Rectangle2D tiny = new Rectangle2D.Double(0, 0, 10, 10);
    assertEquals(DiagramFitCalculator.MAX_SCALE,
      DiagramFitCalculator.calculateScaleFactor(tiny, 800, 600));
    Rectangle2D huge = new Rectangle2D.Double(0, 0, 100000, 100000);
    assertEquals(DiagramFitCalculator.MIN_SCALE,
      DiagramFitCalculator.calculateScaleFactor(huge, 800, 600));
  }
}
