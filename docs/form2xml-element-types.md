Element Types
=============

Module (root node)
------------------
  - version[integer]
  - xmlns[string]

ValidationUnits (enum)
----------------------
  - Item

CursorModes (enum)
------------------
  - Open

MouseNavigationLimits (enum)
----------------------------
  - Form

RealUnits (enum)
----------------
  - Point

CoordinateSystems (enum)
------------------------
  - Real

FillPatterns (enum)
-------------------
  - transparent

FontSyles (enum)
----------------
  - Plain

FontWeight (enum)
-----------------
  - Demilight

FontSpacings (enum)
-------------------
  - Normal

QueryDataSourceTypes (enum)
---------------------------
  - None

DMLDataTypes (enum)
-------------------
  - None

FormModule (container)
----------------------
  - Name[string]
  - ValidationUnit[enum]
  - Comment[string]
  - FirstNavigationBlockName[string]
  - MenuModule[file-name]
  - MenuRole[string]
  - CursorMode[enum]
  - InitializeMenu[string]
  - Use3dControls[boolean]
  - RuntimeComp[version]
  - DirtyInfo[boolean]
  - MouseNavigationLimit[enum]
  - SavepointMode[boolean]

Coordinate (container)
----------------------
  - CharacterCellWidth[integer]
  - RealUnit[enum]
  - DefaultFontScaling[boolean]
  - CharacterCellHeight[integer]
  - CoordinateSystem[enum]

Alert
-----
  - Name[string]
  - Button1Label[string]
  - Button2Label[string]
  - Title[string]
  - AlertMessage[string]
  - FillPattern[enum]
  - BackColor[string]
  - FontName[string]
  - FontSize[integer]
  - FontStyle[enum]
  - DirtyInfo[boolean]
  - FontWeight[enum]
  - FontSpacing[enum]
  - ForegroundColor[string]

Block (container)
-----------------
  - Name[string]
  - DirtyInfo[boolean]
  - DatabaseBlock[boolean]
  - QueryDataSourceType[enum]
  - ScrollbarWidth[integer]
  - QueryAllowed[boolean]
  - DMLDataType[enum]
  - ScrollbarLength[integer]

Item (container)
----------------
  - Name[string]
  - Prompt[string]
  - ShowHorizionScrollbar[boolean]
  - PromptAttachmentEdge[enum]
  - TabPageName[string]
  - PromptAttachmentOffset[integer]
  - PromptAlign[enum]
  - Width[integer]
  - ItemType[enum]
  - CompressionQuality[enum]
  - XPosition[integer]
  - Required[boolean]
  - YPosition[integer]
  - DatabaseItem[boolean]
  - DirtyInfo[boolean]
  - VisualAttributeName[string]
  - PromptVisualAttributeName[string]
  - CanvasName[string]
  - ListElementCount[integer]
  - ShowVerticalScrollbar[boolean]
  - Height[integer]

ListItemElement (container)
---------------------------
  - Name[string]
  - Index[integer]
  - Value[string]

RadioButton (container)
-----------------------
  - Name[string]
  - RadioButtonValue[enum]
  - XPosition[integer]
  - YPosition[integer]
  - DirtyInfo[boolean]
  - VisualAttributeName[string]
  - Label[string]
  - DistanceBetweenRecords[integer]
  - Width[integer]
  - Height[integer]

Trigger (container)
-------------------
  - Name[string]
  - DirtyInfo[boolean]
  - TriggerText[string]

Canvas
Graphics
CompoundText
TextSegment
ProgramUnit
PropertyClass
VisualAttribute
ObjectGroup
ObjectGroupChild
Relation
Editor
ModuleParameter
LOV
LOVColumnMapping
Menu
MenuItem
