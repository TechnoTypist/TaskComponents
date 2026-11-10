# TaskComponents — Rush 2 project (v1.1.0)

A non-visible MIT App Inventor extension for creating and managing native Android layouts, labels, and buttons by unique ID.

## New in v1.1.0
- `CreateLayout` now accepts a ScrollArrangement/ScrollView directly. It creates or reuses one vertical content host inside the ScrollView, so multiple task rows do not become multiple direct ScrollView children.
- Existing `ComponentClicked(id, type)` event remains for compatibility.
- New `ComponentClickedWithText(id, type, text)` event supplies the clicked button's current text.
- New `ComponentLongClicked(id, type, text)` event for long-press actions.
- New `SetTextSize`, `SetAlpha`, `SetClickable`, `SetButtonColors`, `SetOrientation`, and `GetChildCount` functions.
- Existing `GetText(id)` returns the current text of a label or button.

## Build
Requires JDK 17+ and Rush CLI 2.1.2+.
1. Extract this ZIP.
2. Open PowerShell inside the `TaskComponents-Rush` folder (the folder containing `rush.yml`).
3. Run `rush build`.
4. Import the generated `.aix` from the `out` folder into MIT App Inventor.

See `BUILD-INSTRUCTIONS.txt` for the Windows steps.

## Existing functions
- `CreateLayout(parent, id, orientation, width, height, backgroundColor, cornerRadius, padding)`
- `CreateChildLayout(parentId, id, orientation, width, height, backgroundColor, cornerRadius, padding)`
- `AddText(parentId, id, text, textColor, textSize, width, height, bold)`
- `AddButton(parentId, id, text, textColor, backgroundColor, textSize, width, height, cornerRadius)`
- `SetText`, `SetTextColor`, `SetBackground`, `SetSize`, `SetVisible`, `SetEnabled`
- `SetPadding`, `SetSpacing`, `Fade`, `Slide`, `Remove`, `ClearAll`, `Exists`, `GetText`

## New functions
- `SetTextSize(id, textSize)`
- `SetAlpha(id, alpha)` — 0.0 transparent through 1.0 opaque.
- `SetClickable(id, clickable)`
- `SetButtonColors(id, textColor, backgroundColor, cornerRadius)`
- `SetOrientation(id, orientation)` — `horizontal` or `vertical`.
- `GetChildCount(id)`

## Events
- `ComponentClicked(id, type)` — kept for old projects.
- `ComponentClickedWithText(id, type, text)` — click event including the current visible text.
- `ComponentLongClicked(id, type, text)` — long press event including the current visible text.

## Sizing and colors
- Width/height values are dp. `-1` = Fill Parent; `-2` = Automatic.
- Text sizes are sp.
- Colors are Android ARGB integers. Use App Inventor's color blocks where available.
- `orientation` is the text `horizontal` or `vertical`.

## ScrollArrangement note
Preferred Designer structure:
`Screen1 > ScrollArrangement (Scrollable) > TaskListArrangement (Vertical Arrangement)`.
Pass `TaskListArrangement` as the `parent` to `CreateLayout`.
The extension also supports passing the ScrollArrangement itself; it will create/reuse a single vertical content host inside it. Do not put multiple direct children into a ScrollView.
