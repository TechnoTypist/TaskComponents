package com.technomc.taskcomponents;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;

import com.google.appinventor.components.annotations.Extension;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;

import java.util.HashMap;
import java.util.Map;

@Extension(description = "Create and manage customizable native Android layouts, labels, and buttons by unique ID.")
public class TaskComponents extends AndroidNonvisibleComponent {
  private final Form form;
  private final Context context;
  private final Map<String, View> views = new HashMap<String, View>();
  private final Map<String, ViewGroup> groups = new HashMap<String, ViewGroup>();
  private final Map<ScrollView, LinearLayout> scrollHosts = new java.util.WeakHashMap<ScrollView, LinearLayout>();
  private final Map<ViewGroup, LinearLayout> verticalHosts = new java.util.WeakHashMap<ViewGroup, LinearLayout>();

  public TaskComponents(ComponentContainer container) {
    super(container.$form());
    form = container.$form();
    context = form;
  }

  private int dp(float value) {
    return Math.round(value * context.getResources().getDisplayMetrics().density);
  }

  private boolean validId(String id) {
    return id != null && id.trim().length() > 0;
  }

  private ViewGroup getGroup(String id) {
    return groups.get(id);
  }

  // ScrollView accepts one direct child. Vertical App Inventor arrangements
  // can also contain extension-created rows in a nested host. Keep one host
  // per parent so every task row is appended to the same vertical column.
  private ViewGroup resolveParent(AndroidViewComponent component) {
    if (component == null) return null;
    View target = component.getView();

    if (target instanceof ScrollView) {
      ScrollView scroll = (ScrollView) target;
      LinearLayout host = scrollHosts.get(scroll);
      if (host != null && host.getParent() == scroll) return host;
      if (scroll.getChildCount() > 0) {
        View existing = scroll.getChildAt(0);
        if (existing instanceof LinearLayout) {
          host = (LinearLayout) existing;
          scrollHosts.put(scroll, host);
          return host;
        }
        return null;
      }
      host = new LinearLayout(context);
      host.setOrientation(LinearLayout.VERTICAL);
      host.setGravity(Gravity.TOP);
      host.setClipChildren(false);
      host.setClipToPadding(false);
      host.setLayoutParams(new ScrollView.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
      scroll.addView(host);
      scrollHosts.put(scroll, host);
      return host;
    }

    if (target instanceof LinearLayout) {
      LinearLayout parent = (LinearLayout) target;
      if (parent.getOrientation() == LinearLayout.VERTICAL) {
        LinearLayout host = verticalHosts.get(parent);
        if (host != null && host.getParent() == parent) return host;

        host = new LinearLayout(context);
        host.setOrientation(LinearLayout.VERTICAL);
        host.setGravity(Gravity.TOP);
        host.setBaselineAligned(false);
        host.setClipChildren(false);
        host.setClipToPadding(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0f);
        parent.addView(host, parent.getChildCount(), params);
        verticalHosts.put(parent, host);
        parent.requestLayout();
        parent.invalidate();
        return host;
      }
    }

    return target instanceof ViewGroup ? (ViewGroup) target : null;
  }

  private void register(String id, View view) {
    views.put(id, view);
    if (view instanceof ViewGroup) groups.put(id, (ViewGroup) view);
  }

  private void addToParent(ViewGroup parent, View child, int width, int height) {
    if (parent == null || child == null) return;
    int resolvedWidth = width == -1 ? ViewGroup.LayoutParams.MATCH_PARENT
        : (width == -2 ? ViewGroup.LayoutParams.WRAP_CONTENT : dp(width));
    int resolvedHeight = height == -1 ? ViewGroup.LayoutParams.MATCH_PARENT
        : (height == -2 ? ViewGroup.LayoutParams.WRAP_CONTENT : dp(height));

    if (parent instanceof LinearLayout) {
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
          resolvedWidth, resolvedHeight, 0f);
      params.gravity = Gravity.TOP;
      parent.addView(child, parent.getChildCount(), params);
    } else {
      parent.addView(child, new ViewGroup.LayoutParams(resolvedWidth, resolvedHeight));
    }
    parent.requestLayout();
    parent.invalidate();
    child.requestLayout();
  }

  private GradientDrawable shape(int color, int radiusDp) {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setColor(color);
    drawable.setCornerRadius(dp(Math.max(0, radiusDp)));
    return drawable;
  }

  @SimpleFunction(description = "Create a vertical or horizontal layout inside an existing App Inventor arrangement. Use width/height -1 for Fill Parent and -2 for Automatic.")
  public void CreateLayout(AndroidViewComponent parent, String id, String orientation, int width, int height, int backgroundColor, int cornerRadius, int padding) {
    if (parent == null || !validId(id) || views.containsKey(id)) return;
    ViewGroup target = resolveParent(parent);
    if (target == null) return;
    LinearLayout layout = new LinearLayout(context);
    layout.setOrientation("horizontal".equalsIgnoreCase(orientation) ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
    layout.setGravity("horizontal".equalsIgnoreCase(orientation) ? Gravity.CENTER_VERTICAL : Gravity.TOP);
    layout.setPadding(dp(padding), dp(padding), dp(padding), dp(padding));
    layout.setBackground(shape(backgroundColor, cornerRadius));
    register(id, layout);
    addToParent(target, layout, width, height);
  }

  @SimpleFunction(description = "Create a nested vertical or horizontal layout inside another layout ID.")
  public void CreateChildLayout(String parentId, String id, String orientation, int width, int height, int backgroundColor, int cornerRadius, int padding) {
    ViewGroup parent = getGroup(parentId);
    if (parent == null || !validId(id) || views.containsKey(id)) return;
    LinearLayout layout = new LinearLayout(context);
    layout.setOrientation("horizontal".equalsIgnoreCase(orientation) ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
    layout.setGravity("horizontal".equalsIgnoreCase(orientation) ? Gravity.CENTER_VERTICAL : Gravity.TOP);
    layout.setPadding(dp(padding), dp(padding), dp(padding), dp(padding));
    layout.setBackground(shape(backgroundColor, cornerRadius));
    register(id, layout);
    addToParent(parent, layout, width, height);
  }

  @SimpleFunction(description = "Add a text label to a layout ID. textSize is in scaled pixels; width/height use pixels, -1 Fill Parent, -2 Automatic.")
  public void AddText(String parentId, String id, String text, int textColor, float textSize, int width, int height, boolean bold) {
    ViewGroup parent = getGroup(parentId);
    if (parent == null || !validId(id) || views.containsKey(id)) return;
    TextView label = new TextView(context);
    label.setText(text == null ? "" : text);
    label.setTextColor(textColor);
    label.setTextSize(textSize);
    label.setGravity(Gravity.CENTER_VERTICAL);
    label.setIncludeFontPadding(false);
    if (bold) label.setTypeface(label.getTypeface(), android.graphics.Typeface.BOLD);
    register(id, label);
    addToParent(parent, label, width, height);
  }

  @SimpleFunction(description = "Add a clickable button to a layout ID. A ComponentClicked event is raised with its ID and type=button.")
  public void AddButton(String parentId, String id, String text, int textColor, int backgroundColor, float textSize, int width, int height, int cornerRadius) {
    ViewGroup parent = getGroup(parentId);
    if (parent == null || !validId(id) || views.containsKey(id)) return;
    Button button = new Button(context);
    button.setText(text == null ? "" : text);
    button.setTextColor(textColor);
    button.setTextSize(textSize);
    button.setAllCaps(false);
    button.setMinWidth(0);
    button.setMinimumWidth(0);
    button.setMinHeight(0);
    button.setMinimumHeight(0);
    button.setPadding(dp(10), dp(4), dp(10), dp(4));
    button.setBackground(shape(backgroundColor, cornerRadius));
    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        ComponentClicked(id, "button");
        ComponentClickedWithText(id, "button", ((Button) view).getText().toString());
      }
    });
    button.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View view) {
        ComponentLongClicked(id, "button", ((Button) view).getText().toString());
        return true;
      }
    });
    register(id, button);
    addToParent(parent, button, width, height);
  }

  @SimpleFunction(description = "Change the text of an existing label or button by ID.")
  public void SetText(String id, String text) {
    View view = views.get(id);
    if (view instanceof TextView) ((TextView) view).setText(text == null ? "" : text);
  }

  @SimpleFunction(description = "Change the text color of an existing label or button by ID.")
  public void SetTextColor(String id, int color) {
    View view = views.get(id);
    if (view instanceof TextView) ((TextView) view).setTextColor(color);
  }

  @SimpleFunction(description = "Change the background color and corner radius of an existing component by ID.")
  public void SetBackground(String id, int color, int cornerRadius) {
    View view = views.get(id);
    if (view != null) view.setBackground(shape(color, cornerRadius));
  }

  @SimpleFunction(description = "Set a component's width and height in pixels. Use -1 for Fill Parent and -2 for Automatic.")
  public void SetSize(String id, int width, int height) {
    View view = views.get(id);
    if (view == null) return;
    ViewGroup.LayoutParams params = view.getLayoutParams();
    if (params == null) return;
    params.width = width == -1 ? -1 : (width == -2 ? -2 : dp(width));
    params.height = height == -1 ? -1 : (height == -2 ? -2 : dp(height));
    view.setLayoutParams(params);
  }

  @SimpleFunction(description = "Set a component visible or hidden by ID.")
  public void SetVisible(String id, boolean visible) {
    View view = views.get(id);
    if (view != null) view.setVisibility(visible ? View.VISIBLE : View.GONE);
  }

  @SimpleFunction(description = "Set a component's enabled state by ID.")
  public void SetEnabled(String id, boolean enabled) {
    View view = views.get(id);
    if (view != null) view.setEnabled(enabled);
  }



  @SimpleFunction(description = "Change the text size of a label or button. Size is in scaled pixels (sp).")
  public void SetTextSize(String id, float textSize) {
    View view = views.get(id);
    if (view instanceof TextView) ((TextView) view).setTextSize(Math.max(1f, textSize));
  }

  @SimpleFunction(description = "Set a component's alpha from 0 (transparent) to 1 (opaque).")
  public void SetAlpha(String id, float alpha) {
    View view = views.get(id);
    if (view != null) view.setAlpha(Math.max(0f, Math.min(1f, alpha)));
  }

  @SimpleFunction(description = "Set whether a component can receive clicks.")
  public void SetClickable(String id, boolean clickable) {
    View view = views.get(id);
    if (view != null) view.setClickable(clickable);
  }

  @SimpleFunction(description = "Change a button's text and background colors.")
  public void SetButtonColors(String id, int textColor, int backgroundColor, int cornerRadius) {
    View view = views.get(id);
    if (view instanceof Button) {
      ((Button) view).setTextColor(textColor);
      view.setBackground(shape(backgroundColor, cornerRadius));
    }
  }

  @SimpleFunction(description = "Set a layout's orientation to horizontal or vertical.")
  public void SetOrientation(String id, String orientation) {
    ViewGroup group = getGroup(id);
    if (group instanceof LinearLayout) {
      ((LinearLayout) group).setOrientation(
          "horizontal".equalsIgnoreCase(orientation) ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
    }
  }

  @SimpleFunction(description = "Get the number of direct children inside a dynamic layout.")
  public int GetChildCount(String id) {
    ViewGroup group = getGroup(id);
    return group == null ? 0 : group.getChildCount();
  }
  @SimpleFunction(description = "Set padding in dp for a layout ID.")
  public void SetPadding(String id, int left, int top, int right, int bottom) {
    View view = views.get(id);
    if (view != null) view.setPadding(dp(left), dp(top), dp(right), dp(bottom));
  }

  @SimpleFunction(description = "Set the spacing between children in a layout ID, in dp.")
  public void SetSpacing(String id, int spacing) {
    ViewGroup group = getGroup(id);
    if (!(group instanceof LinearLayout)) return;
    ((LinearLayout) group).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
    android.graphics.drawable.ColorDrawable divider = new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT);
    ((LinearLayout) group).setDividerDrawable(divider);
    ((LinearLayout) group).setDividerPadding(dp(Math.max(0, spacing)));
  }

  @SimpleFunction(description = "Fade a component in or out over durationMs milliseconds.")
  public void Fade(String id, boolean show, int durationMs) {
    final View view = views.get(id);
    if (view == null) return;
    if (show) {
      view.setVisibility(View.VISIBLE);
      view.setAlpha(0f);
    }
    ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", show ? 0f : 1f, show ? 1f : 0f);
    animator.setDuration(Math.max(0, durationMs));
    animator.setInterpolator(new DecelerateInterpolator());
    if (!show) animator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) { view.setVisibility(View.GONE); }
    });
    animator.start();
  }

  @SimpleFunction(description = "Slide a component up into view or down out of view over durationMs milliseconds.")
  public void Slide(String id, boolean show, int durationMs) {
    final View view = views.get(id);
    if (view == null) return;
    if (show) {
      view.setVisibility(View.VISIBLE);
      view.setTranslationY(dp(18));
    }
    ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", show ? dp(18) : 0f, show ? 0f : dp(18));
    animator.setDuration(Math.max(0, durationMs));
    animator.setInterpolator(new DecelerateInterpolator());
    if (!show) animator.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationEnd(Animator animation) { view.setVisibility(View.GONE); }
    });
    animator.start();
  }

  @SimpleFunction(description = "Remove one component or an entire nested layout and all of its descendants by ID.")
  public void Remove(String id) {
    View view = views.get(id);
    if (view == null) return;
    if (view instanceof ViewGroup) {
      ViewGroup group = (ViewGroup) view;
      for (int i = group.getChildCount() - 1; i >= 0; i--) removeViewTree(group.getChildAt(i));
    }
    if (view.getParent() instanceof ViewGroup) ((ViewGroup) view.getParent()).removeView(view);
    removeViewTree(view);
  }

  private void removeViewTree(View view) {
    if (view instanceof ViewGroup) {
      ViewGroup group = (ViewGroup) view;
      for (int i = group.getChildCount() - 1; i >= 0; i--) removeViewTree(group.getChildAt(i));
    }
    String found = null;
    for (Map.Entry<String, View> entry : views.entrySet()) {
      if (entry.getValue() == view) { found = entry.getKey(); break; }
    }
    if (found != null) { views.remove(found); groups.remove(found); }
  }

  @SimpleFunction(description = "Remove every component created by this extension. The original App Inventor arrangement remains.")
  public void ClearAll() {
    for (View view : views.values()) {
      if (view.getParent() instanceof ViewGroup) ((ViewGroup) view.getParent()).removeView(view);
    }
    views.clear();
    groups.clear();
  }

  @SimpleFunction(description = "Check whether a component ID currently exists.")
  public boolean Exists(String id) { return views.containsKey(id); }

  @SimpleFunction(description = "Get the current text of a label or button by ID.")
  public String GetText(String id) {
    View view = views.get(id);
    return view instanceof TextView ? ((TextView) view).getText().toString() : "";
  }

  @SimpleEvent(description = "Raised when a dynamic button is clicked. Returns the component ID and its type. Kept for compatibility.")
  public void ComponentClicked(String id, String type) {
    EventDispatcher.dispatchEvent(this, "ComponentClicked", id, type);
  }

  @SimpleEvent(description = "Raised when a dynamic button is clicked. Returns its unique ID, type, and current displayed text.")
  public void ComponentClickedWithText(String id, String type, String text) {
    EventDispatcher.dispatchEvent(this, "ComponentClickedWithText", id, type, text);
  }

  @SimpleEvent(description = "Raised when a dynamic button is long-pressed. Returns its unique ID, type, and current displayed text.")
  public void ComponentLongClicked(String id, String type, String text) {
    EventDispatcher.dispatchEvent(this, "ComponentLongClicked", id, type, text);
  }
}
