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

import org.tinyuml.draw.AbstractCompositeNode;
import org.tinyuml.draw.Connection;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.umldraw.shared.NoteElement;

/**
 * Counts the elements of a structure diagram by category (Package, Class,
 * Component and Note). Connections (relations) are not counted. Elements
 * nested inside other elements (e.g. classes inside a package) are counted
 * as well.
 * Implemented for requirement RF-001.
 *
 * @version 1.0
 */
public class DiagramElementCounter {

  private int packageCount;
  private int classCount;
  private int componentCount;
  private int noteCount;

  /**
   * Constructor. Counts the elements of the specified diagram.
   * @param diagram the diagram to count, may be null
   */
  public DiagramElementCounter(StructureDiagram diagram) {
    if (diagram != null) {
      countChildren(diagram);
    }
  }

  /**
   * Recursively counts the children of the specified node.
   * @param node the composite node
   */
  private void countChildren(AbstractCompositeNode node) {
    for (DiagramElement child : node.getChildren()) {
      if (child instanceof Connection) {
        continue;
      }
      if (child instanceof PackageElement) {
        packageCount++;
      } else if (child instanceof ClassElement) {
        classCount++;
      } else if (child instanceof ComponentElement) {
        componentCount++;
      } else if (child instanceof NoteElement) {
        noteCount++;
      }
      if (child instanceof AbstractCompositeNode) {
        countChildren((AbstractCompositeNode) child);
      }
    }
  }

  /**
   * Returns the number of packages.
   * @return the number of packages
   */
  public int getPackageCount() { return packageCount; }

  /**
   * Returns the number of classes.
   * @return the number of classes
   */
  public int getClassCount() { return classCount; }

  /**
   * Returns the number of components.
   * @return the number of components
   */
  public int getComponentCount() { return componentCount; }

  /**
   * Returns the number of notes.
   * @return the number of notes
   */
  public int getNoteCount() { return noteCount; }

  /**
   * Returns the total number of elements, relations excluded.
   * @return the total number of elements
   */
  public int getTotalCount() {
    return packageCount + classCount + componentCount + noteCount;
  }

  /**
   * Returns the status bar text, e.g.
   * "Total Items: 06 | Package: 02 | Class: 02 | Component: 02 | Note: 00".
   * @return the status text
   */
  public String getStatusText() {
    return String.format(
      "Total Items: %02d | Package: %02d | Class: %02d | Component: %02d"
      + " | Note: %02d", getTotalCount(), packageCount, classCount,
      componentCount, noteCount);
  }
}
