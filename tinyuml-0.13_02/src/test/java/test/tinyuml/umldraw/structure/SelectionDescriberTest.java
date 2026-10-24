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

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.model.ElementType;
import org.tinyuml.model.RelationType;
import org.tinyuml.model.UmlModelImpl;
import org.tinyuml.umldraw.shared.NoteElement;
import org.tinyuml.umldraw.shared.UmlConnection;
import org.tinyuml.umldraw.shared.UmlNode;
import org.tinyuml.umldraw.structure.SelectionDescriber;
import org.tinyuml.umldraw.structure.StructureDiagram;

/**
 * Tests for the SelectionDescriber class (RF-002).
 *
 * @version 1.0
 */
public class SelectionDescriberTest extends TestCase {

  private StructureDiagram diagram;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setUp() {
    diagram = new StructureDiagram(new UmlModelImpl());
  }

  /**
   * Creates a node of the specified type.
   * @param type the element type
   * @return the created node
   */
  private UmlNode createNode(ElementType type) {
    return diagram.getElementFactory().createNode(type);
  }

  /**
   * Clicking on an empty area selects the diagram, this is not counted.
   */
  public void testDiagramIsNotASelectedElement() {
    List<DiagramElement> selection = new ArrayList<DiagramElement>();
    selection.add(diagram);
    assertTrue(SelectionDescriber.getSelectedItems(selection).isEmpty());
  }

  /**
   * The categories of the nodes.
   */
  public void testNodeCategories() {
    assertEquals("Package",
      SelectionDescriber.getCategory(createNode(ElementType.PACKAGE)));
    assertEquals("Class",
      SelectionDescriber.getCategory(createNode(ElementType.CLASS)));
    assertEquals("Component",
      SelectionDescriber.getCategory(createNode(ElementType.COMPONENT)));
    assertEquals("Note",
      SelectionDescriber.getCategory(createNode(ElementType.NOTE)));
  }

  /**
   * The categories of the relations.
   */
  public void testRelationCategories() {
    UmlNode class1 = createNode(ElementType.CLASS);
    UmlNode class2 = createNode(ElementType.CLASS);
    assertEquals("Association", categoryOf(RelationType.ASSOCIATION,
      class1, class2));
    assertEquals("Composition", categoryOf(RelationType.COMPOSITION,
      class1, class2));
    assertEquals("Aggregation", categoryOf(RelationType.AGGREGATION,
      class1, class2));
    assertEquals("Dependency", categoryOf(RelationType.DEPENDENCY,
      class1, class2));
    assertEquals("Inheritance", categoryOf(RelationType.INHERITANCE,
      class1, class2));
  }

  /**
   * Creates a connection and returns its category.
   * @param type the relation type
   * @param node1 the first node
   * @param node2 the second node
   * @return the category
   */
  private String categoryOf(RelationType type, UmlNode node1, UmlNode node2) {
    UmlConnection conn = diagram.getElementFactory().createConnection(type,
      node1, node2);
    return SelectionDescriber.getCategory(conn);
  }

  /**
   * The name of a class is the name of its model element.
   */
  public void testClassName() {
    UmlNode clss = createNode(ElementType.CLASS);
    clss.getModelElement().setName("Persona");
    assertEquals("Persona", SelectionDescriber.getName(clss));
  }

  /**
   * Elements without a name.
   */
  public void testNoName() {
    UmlNode clss = createNode(ElementType.CLASS);
    clss.getModelElement().setName("");
    assertEquals(SelectionDescriber.NO_NAME, SelectionDescriber.getName(clss));
    NoteElement note = (NoteElement) createNode(ElementType.NOTE);
    assertEquals(SelectionDescriber.NO_NAME, SelectionDescriber.getName(note));
  }

  /**
   * For a note, the first line of its text is shown.
   */
  public void testNoteName() {
    NoteElement note = (NoteElement) createNode(ElementType.NOTE);
    note.setLabelText("Primera linea\nSegunda linea");
    assertEquals("Primera linea...", SelectionDescriber.getName(note));
  }

  /**
   * Long names are shortened.
   */
  public void testLongName() {
    UmlNode clss = createNode(ElementType.CLASS);
    clss.getModelElement().setName("UnNombreDeClaseMuyLargoQueNoCabe");
    assertEquals("UnNombreDeClaseMuyLargoQueNoCa...",
      SelectionDescriber.getName(clss));
  }
}
