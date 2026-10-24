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
package test.tinyuml.umldraw.structure;

import junit.framework.TestCase;
import org.tinyuml.draw.CompositeNode;
import org.tinyuml.model.ElementType;
import org.tinyuml.model.RelationType;
import org.tinyuml.model.UmlModelImpl;
import org.tinyuml.umldraw.shared.UmlConnection;
import org.tinyuml.umldraw.shared.UmlNode;
import org.tinyuml.umldraw.structure.DiagramElementCounter;
import org.tinyuml.umldraw.structure.StructureDiagram;

/**
 * Tests for the DiagramElementCounter class (RF-001).
 *
 * @version 1.0
 */
public class DiagramElementCounterTest extends TestCase {

  private StructureDiagram diagram;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setUp() {
    diagram = new StructureDiagram(new UmlModelImpl());
  }

  /**
   * Creates a node of the specified type and adds it to the diagram.
   * @param type the element type
   * @return the created node
   */
  private UmlNode addNode(ElementType type) {
    UmlNode node = diagram.getElementFactory().createNode(type);
    diagram.addChild(node);
    return node;
  }

  /**
   * An empty diagram shows zero in every category.
   */
  public void testEmptyDiagram() {
    DiagramElementCounter counter = new DiagramElementCounter(diagram);
    assertEquals(0, counter.getTotalCount());
    assertEquals(
      "Total Items: 00 | Package: 00 | Class: 00 | Component: 00 | Note: 00",
      counter.getStatusText());
  }

  /**
   * A null diagram is treated as empty.
   */
  public void testNullDiagram() {
    assertEquals(0, new DiagramElementCounter(null).getTotalCount());
  }

  /**
   * Adding elements updates the total and the category counts.
   */
  public void testCountByCategory() {
    addNode(ElementType.PACKAGE);
    addNode(ElementType.PACKAGE);
    addNode(ElementType.CLASS);
    addNode(ElementType.CLASS);
    addNode(ElementType.COMPONENT);
    addNode(ElementType.COMPONENT);
    DiagramElementCounter counter = new DiagramElementCounter(diagram);
    assertEquals(2, counter.getPackageCount());
    assertEquals(2, counter.getClassCount());
    assertEquals(2, counter.getComponentCount());
    assertEquals(0, counter.getNoteCount());
    assertEquals(6, counter.getTotalCount());
    assertEquals(
      "Total Items: 06 | Package: 02 | Class: 02 | Component: 02 | Note: 00",
      counter.getStatusText());
  }

  /**
   * Removing an element updates the total and its category count.
   */
  public void testRemoveElement() {
    addNode(ElementType.NOTE);
    UmlNode clss = addNode(ElementType.CLASS);
    diagram.removeChild(clss);
    DiagramElementCounter counter = new DiagramElementCounter(diagram);
    assertEquals(0, counter.getClassCount());
    assertEquals(1, counter.getNoteCount());
    assertEquals(1, counter.getTotalCount());
  }

  /**
   * Relations between elements are not counted.
   */
  public void testConnectionsAreNotCounted() {
    UmlNode class1 = addNode(ElementType.CLASS);
    UmlNode class2 = addNode(ElementType.CLASS);
    UmlConnection conn = diagram.getElementFactory().createConnection(
      RelationType.ASSOCIATION, class1, class2);
    diagram.addChild(conn);
    DiagramElementCounter counter = new DiagramElementCounter(diagram);
    assertEquals(2, counter.getClassCount());
    assertEquals(2, counter.getTotalCount());
  }

  /**
   * Elements nested in a package are counted as well.
   */
  public void testNestedElements() {
    UmlNode pkg = addNode(ElementType.PACKAGE);
    UmlNode clss = diagram.getElementFactory().createNode(ElementType.CLASS);
    ((CompositeNode) pkg).addChild(clss);
    DiagramElementCounter counter = new DiagramElementCounter(diagram);
    assertEquals(1, counter.getPackageCount());
    assertEquals(1, counter.getClassCount());
    assertEquals(2, counter.getTotalCount());
  }
}
