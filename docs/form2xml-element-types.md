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
  - TriggerText[string]? | SubclassSubObject[boolean]?
  - DisplayInKeyboardHelp[boolean]?

Canvas (container)
------------------
  - Name[string]
  - ViewportXPositionOnCanvas[integer]
  - ViewportYPositionOnCanvas[integer]
  - ShowHorizontalScrollbar[boolean]
  - CanvasType[enum]
  - Width[integer]
  - WindowName[string]
  - Bevel[enum]
  - DirtyInfo[boolean]
  - VisualAttributeName[string]
  - Visible[boolean]
  - ShowVerticalScrollbar[boolean]
  - Height[integer]
  - ViewportHeight[integer]
  - ViewportXPosition[integer]
  - ViewportWidth[integer]
  - ViewportYPosition[integer]
  - RaiseOnEnter[boolean]

Graphics (container)
--------------------
  - Name[string]
  - InternalLineWidth[integer]
  - InternalRotationAngle[integer]
  - EdgeForegroundColor[string]
  - EdgeBackColor[string]
  - Width[integer]
  - FillPattern[enum]
  - EdgePattern[enum]
  - DashStyle[enum]
  - CapStype[enum]
  - JoinStyle[enum]
  - BackColor[string]
  - XPosition[integer]
  - YPosition[integer]
  - Bevel[enum]
  - DirtyInfo[boolean]
  - ForegroundColor[string]
  - GraphicsType[enum]
  - HorizontalMargin[integer]
  - ScrollbarWidth[integer]
  - VertialMargin[integer]
  - HorizontalObjectOffset[integer]
  - Height[integer]
  - StartPromptOffset[integer]

CompoundText (container)
------------------------
  - Name[string]

TextSegment (element)
---------------------
  - Name[string]
  - FontName[string]
  - FontSize[integer]
  - FontStyle[string]
  - DirtyInfo[boolean]
  - FontWeight[string]
  - FontSpacing[string]
  - ForegroundColor[string]
  - Text[string]

Editor (element)
----------------
  - Name[string]
  - WrapStyle[string]
  - XPosition[integer]
  - YPosition[integer]
  - DirtyInfo[boolean]
  - VisualAttributeName[string]
  - Title[string]
  - Width[integer]
  - Height[integer]
  - ShowVerticalScrollbar[boolean]
  
ModuleParameter (element)
-------------------------
  - Name[string]
  - DirtyInfo[boolean]

LOV (container)
---------------
  - Name[string]
  - DirtyInfo[boolean]
  - Title[string]
  - Width[integer]
  - RecordGroupName[string]
  - Height[integer]

LOVColumnMapping (element)
--------------------------
  - Name[string]
  - ReturnItem[string]
  - DirtyInfo[boolean]
  - Title[string]
  - DisplayWidth[integer]

Menu (container)
----------------
  - Name[string]
  - DirtyInfo[boolean]

MenuItem (element)
------------------
  - Name[string]
  - Hint[string]
  - KeyboardAccelerator[string]
  - IconFilename[string]
  - MagicItem[string]
  - CommandType[enum]
  - MenuItemType[string]
  - MenuItemRadioGroup[string]
  - DirtyInfo[boolean]
  - VisualAttributeName[string]
  - Label[string]
  - MenuItemCode[string]
  - DisplayNoPriv[boolean]

ProgramUnit (element)
---------------------
  - Name[string]
  - ProgramUnitText[string]

PropertyClass (element)
-----------------------
  - Name[string]
  - Iconic[boolean]?
  - Enabled[boolean]?
  - DistanceBetweenRecords[integer]?
  - Width[integer]?
  - FillPattern[string]?
  - ItemType[string]?
  - BackColor[string]?
  - DirtyInfo[boolean]?
  - AutoHint[boolean]?
  - DefaultButton[boolean]?
  - ForegroundColor[string]?
  - MouseNavigate[boolean]?
  - CanvasName[string]?
  - Visible[boolean]?
  - KeyboardNavigate[boolean]?
  - Height[integer]?
  - ExecuteHierarchy[enum]?
  - ParentModule[string]?
  - ParentName[string]?
  - ParentType[integer]

RecordGroup (container)
-----------------------
  - Name[string]
  - DirtyInfo[boolean]
  - RecordGroupFetchSize[integer]
  - RecordGroupQuery[string]
  - RecordGroupType[string]

RecordGroupColumn (element)
---------------------------
  - Name[string]
  - DirtyInfo[boolean]
  - ColumnDataType[string]
  - MaximumLength[integer]

VisualAttribute (element)
-------------------------
  - Name[string]
  - BackColor[string]
  - FontName[string]
  - FontSize[integer]
  - FontStyle[string]
  - DirtyInfo[boolean]
  - FontWeight[string]
  - FontSpacing[string]
  - ForegroundColor[string]
  - FillPattern[string]

Window (element)
----------------
  - Name[string]
  - ShowHorizontalScrollbar[boolean]
  - ClearAllowed[boolean]
  - MinimizeAllowed[boolean]

ObjectGroup
ObjectGroupChild
Relation
