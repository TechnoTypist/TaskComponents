# TaskComponents

<p>Create and manage customizable native Android layouts, labels, and buttons by unique ID.</p>

## Events

### ComponentClicked(id, type)

Raised when a dynamic button is clicked. Returns the component ID and its type. Kept for compatibility.

| Parameter | Type |
|---|---|
| id | text |
| type | text |

### ComponentClickedWithText(id, type, text)

Raised when a dynamic button is clicked. Returns its unique ID, type, and current displayed text.

| Parameter | Type |
|---|---|
| id | text |
| type | text |
| text | text |

### ComponentLongClicked(id, type, text)

Raised when a dynamic button is long-pressed. Returns its unique ID, type, and current displayed text.

| Parameter | Type |
|---|---|
| id | text |
| type | text |
| text | text |

## Methods

### CreateLayout(parent, id, orientation, width, height, backgroundColor, cornerRadius, padding)

Create a vertical or horizontal layout inside an existing App Inventor arrangement. Use width/height -1 for Fill Parent and -2 for Automatic.

| Parameter | Type |
|---|---|
| parent | component |
| id | text |
| orientation | text |
| width | number |
| height | number |
| backgroundColor | number |
| cornerRadius | number |
| padding | number |

### CreateChildLayout(parentId, id, orientation, width, height, backgroundColor, cornerRadius, padding)

Create a nested vertical or horizontal layout inside another layout ID.

| Parameter | Type |
|---|---|
| parentId | text |
| id | text |
| orientation | text |
| width | number |
| height | number |
| backgroundColor | number |
| cornerRadius | number |
| padding | number |

### AddText(parentId, id, text, textColor, textSize, width, height, bold)

Add a text label to a layout ID. textSize is in scaled pixels; width/height use pixels, -1 Fill Parent, -2 Automatic.

| Parameter | Type |
|---|---|
| parentId | text |
| id | text |
| text | text |
| textColor | number |
| textSize | number |
| width | number |
| height | number |
| bold | boolean |

### AddButton(parentId, id, text, textColor, backgroundColor, textSize, width, height, cornerRadius)

Add a clickable button to a layout ID. A ComponentClicked event is raised with its ID and type=button.

| Parameter | Type |
|---|---|
| parentId | text |
| id | text |
| text | text |
| textColor | number |
| backgroundColor | number |
| textSize | number |
| width | number |
| height | number |
| cornerRadius | number |

### SetText(id, text)

Change the text of an existing label or button by ID.

| Parameter | Type |
|---|---|
| id | text |
| text | text |

### SetTextColor(id, color)

Change the text color of an existing label or button by ID.

| Parameter | Type |
|---|---|
| id | text |
| color | number |

### SetBackground(id, color, cornerRadius)

Change the background color and corner radius of an existing component by ID.

| Parameter | Type |
|---|---|
| id | text |
| color | number |
| cornerRadius | number |

### SetSize(id, width, height)

Set a component's width and height in pixels. Use -1 for Fill Parent and -2 for Automatic.

| Parameter | Type |
|---|---|
| id | text |
| width | number |
| height | number |

### SetVisible(id, visible)

Set a component visible or hidden by ID.

| Parameter | Type |
|---|---|
| id | text |
| visible | boolean |

### SetEnabled(id, enabled)

Set a component's enabled state by ID.

| Parameter | Type |
|---|---|
| id | text |
| enabled | boolean |

### SetTextSize(id, textSize)

Change the text size of a label or button. Size is in scaled pixels (sp).

| Parameter | Type |
|---|---|
| id | text |
| textSize | number |

### SetAlpha(id, alpha)

Set a component's alpha from 0 (transparent) to 1 (opaque).

| Parameter | Type |
|---|---|
| id | text |
| alpha | number |

### SetClickable(id, clickable)

Set whether a component can receive clicks.

| Parameter | Type |
|---|---|
| id | text |
| clickable | boolean |

### SetButtonColors(id, textColor, backgroundColor, cornerRadius)

Change a button's text and background colors.

| Parameter | Type |
|---|---|
| id | text |
| textColor | number |
| backgroundColor | number |
| cornerRadius | number |

### SetOrientation(id, orientation)

Set a layout's orientation to horizontal or vertical.

| Parameter | Type |
|---|---|
| id | text |
| orientation | text |

### GetChildCount(id)

Get the number of direct children inside a dynamic layout.

| Parameter | Type |
|---|---|
| id | text |

Returns `number`.

### SetPadding(id, left, top, right, bottom)

Set padding in dp for a layout ID.

| Parameter | Type |
|---|---|
| id | text |
| left | number |
| top | number |
| right | number |
| bottom | number |

### SetSpacing(id, spacing)

Set the spacing between children in a layout ID, in dp.

| Parameter | Type |
|---|---|
| id | text |
| spacing | number |

### Fade(id, show, durationMs)

Fade a component in or out over durationMs milliseconds.

| Parameter | Type |
|---|---|
| id | text |
| show | boolean |
| durationMs | number |

### Slide(id, show, durationMs)

Slide a component up into view or down out of view over durationMs milliseconds.

| Parameter | Type |
|---|---|
| id | text |
| show | boolean |
| durationMs | number |

### Remove(id)

Remove one component or an entire nested layout and all of its descendants by ID.

| Parameter | Type |
|---|---|
| id | text |

### ClearAll()

Remove every component created by this extension. The original App Inventor arrangement remains.

### Exists(id)

Check whether a component ID currently exists.

| Parameter | Type |
|---|---|
| id | text |

Returns `boolean`.

### GetText(id)

Get the current text of a label or button by ID.

| Parameter | Type |
|---|---|
| id | text |

Returns `text`.
