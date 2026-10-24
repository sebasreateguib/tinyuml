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
package org.tinyuml.umldraw.structure;

import java.util.ArrayList;
import java.util.List;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.model.UmlModelElement;
import org.tinyuml.umldraw.shared.NoteConnection;
import org.tinyuml.umldraw.shared.NoteElement;
import org.tinyuml.umldraw.shared.UmlDiagramElement;
import org.tinyuml.umldraw.structure.Association.AssociationType;

/**
 * Helper methods to describe the current selection of a diagram editor.
 * Implemented for requirements RF-002 (category and name of the selected
 * element) and RF-003 (number of selected elements).
 *
 * @version 1.0
 */
public final class SelectionDescriber {

  /**
   * The maximum length of a displayed name.
   */
  private static final int MAX_NAME_LENGTH = 30;

  /**
   * The name displayed for elements without a name.
   */
  public static final String NO_NAME = "(sin nombre)";

  /**
   * Private constructor, only static methods.
   */
  private SelectionDescriber() { }

  /**
   * Returns the really selected elements. When the user clicks on an empty
   * area of the diagram, the selection contains the diagram itself, which is
   * not an element of the diagram and is therefore removed here.
   * @param selection the selection as returned by the editor
   * @return the selected elements without the diagram
   */
  public static List<DiagramElement> getSelectedItems(
    List<DiagramElement> selection) {
    List<DiagramElement> result = new ArrayList<DiagramElement>();
    if (selection != null) {
      for (DiagramElement element : selection) {
        if (!(element instanceof StructureDiagram)) {
          result.add(element);
        }
      }
    }
    return result;
  }

  /**
   * Returns the number of really selected elements.
   * @param selection the selection as returned by the editor
   * @return the number of selected elements without the diagram
   */
  public static int getSelectedCount(List<DiagramElement> selection) {
    return getSelectedItems(selection).size();
  }

  /**
   * Returns the category of the specified element.
   * @param element the element
   * @return the category name
   */
  public static String getCategory(DiagramElement element) {
    if (element instanceof PackageElement) {
      return "Package";
    } else if (element instanceof ClassElement) {
      return "Class";
    } else if (element instanceof ComponentElement) {
      return "Component";
    } else if (element instanceof NoteElement) {
      return "Note";
    } else if (element instanceof Association) {
      AssociationType type = ((Association) element).getAssociationType();
      if (type == AssociationType.COMPOSITION) {
        return "Composition";
      } else if (type == AssociationType.AGGREGATION) {
        return "Aggregation";
      }
      return "Association";
    } else if (element instanceof Dependency) {
      return "Dependency";
    } else if (element instanceof Inheritance) {
      return "Inheritance";
    } else if (element instanceof NoteConnection) {
      return "Note connector";
    }
    return "Element";
  }

  /**
   * Returns the name of the specified element. For notes the first line of
   * the text is used. Long names are shortened.
   * @param element the element
   * @return the name or NO_NAME if the element has no name
   */
  public static String getName(DiagramElement element) {
    String name = null;
    if (element instanceof NoteElement) {
      name = ((NoteElement) element).getLabelText();
    } else if (element instanceof UmlDiagramElement) {
      UmlModelElement modelElement =
        ((UmlDiagramElement) element).getModelElement();
      if (modelElement != null) {
        name = modelElement.getName();
      }
    }
    if (name == null || name.trim().length() == 0) {
      return NO_NAME;
    }
    name = name.trim();
    int lineEnd = name.indexOf('\n');
    if (lineEnd >= 0) {
      name = name.substring(0, lineEnd).trim() + "...";
    }
    if (name.length() > MAX_NAME_LENGTH) {
      name = name.substring(0, MAX_NAME_LENGTH) + "...";
    }
    return name;
  }
}
