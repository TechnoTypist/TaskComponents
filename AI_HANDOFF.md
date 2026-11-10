# AI Handoff — Task Manager App

Last updated: 2026-11-10

## Project and user preferences
- MIT App Inventor project: Task Focus Assistant / APCSP_CPT_PRACTICE.
- Main repository: https://github.com/TechnoTypist/TaskManagerApp (branch: main).
- User wants exact, step-by-step block instructions with actual block names and sockets. Be direct, concise, and do not end with questions.
- Do not edit or push GitHub files unless the user explicitly asks. In this session the user explicitly asked to upload the Rush extension source and maintain this handoff note.
- Do not claim a fix is tested unless it has been built and tested on-device.
- At the end of each future session, update this file with current findings, completed changes, and next steps so a future assistant can resume after chat compression.

## Current app architecture
- Screens: Screen1, NewTaskScreen, DataClearScreen.
- Screen1 UI: TaskScrollArrangement (VerticalScrollArrangement) > TaskListArrangement (VerticalArrangement).
- Task rows are created dynamically using the non-visible TaskComponents1 extension.
- createTaskRow parameters include taskTime, taskName, taskStatus, taskDescription, rowID, horizontal_row_id.
- taskTimes stores original duration in minutes.
- TinyDB stores per-task remaining seconds in tags secondsLeft_INDEX and timer state in timerRunning_INDEX.
- updateTimerLabel formats the currently running task's secondsLeft.
- User reports Start/Stop labels now display correctly, but task-row display formatting and completed display still need correction.

## Current bugs being worked on
1. Task display overlap: adding task 2 causes it to appear on top of task 1 rather than as a separate row below it. Deleting task 2 makes task 1 appear again. Restarting the app does not fix it. Previous theories about multiple Screen1 instances were rejected by the user; do not repeat that explanation.
2. Task time display: taskTime in createTaskRow is currently treated as seconds by floor(taskTime / 60) and modulo 60. But taskTimes stores minutes, so passing taskTimes directly produces values such as 0:1 for a 1-minute task. Correct row display must use the per-task saved remaining-seconds TinyDB tag, with fallback original minutes * 60. Completed rows must show 0:00. Seconds must be zero-padded (e.g. 2:03).

## Exact intended timer row logic
- In refreshTaskRows, for each loop index number, pass TinyDB1.GetValue to createTaskRow's taskTime input:
  - tag = join "secondsLeft_" + get global number
  - ValueIfTagNotThere = (select list item from global taskTimes at index get global number) * 60
- The index is get global number. The list is global taskTimes. Multiply the selected duration by 60 only for the fallback value, not for the index.
- In createTaskRow:
  - minutesPart = floor(taskTime / 60)
  - secondsPart = taskTime modulo 60
  - display minutesPart unless taskStatus = "Completed", then 0
  - display secondsPart with zero padding: if secondsPart < 10 then join "0" + secondsPart, else secondsPart; if Completed then "00".
  - Do not replace this with original taskTimes minutes; paused task at 2:33 must continue showing 2:33.
- Before changing blocks, inspect the current project source to verify current state; latest .aia upload may be newer than GitHub.

## Rush extension
- User uploaded TaskComponents-Rush-v1.1.1(1).zip in this conversation.
- Source includes TaskComponents.java at TaskComponents-Rush/src/com/technomc/taskcomponents/TaskComponents.java, rush.yml, README.md, and Windows build instructions.
- A proposed v1.1.2 patch changes addToParent() to use LinearLayout.LayoutParams whenever the parent is a LinearLayout, then requestLayout()/invalidate() on child and parent. This is intended to make task rows participate reliably in vertical/horizontal LinearLayout measurement and stacking. It is not yet built or device-tested.
- A rebuilt AIX has NOT been produced. User must run rush build from extracted TaskComponents-Rush folder using JDK 17+ and Rush CLI 2.1.2+, then import generated .aix into App Inventor.
- Do not claim extension patch definitely fixes overlap until user tests it.
- Preserve existing public methods/events and ID behavior for compatibility.

## Next steps
1. Help user wire row display input in refreshTaskRows exactly as specified above.
2. Confirm seconds zero-padding in createTaskRow.
3. Provide updated Rush source project ZIP and build instructions; do not make user modify unrelated screen navigation.
4. Have user replace/import newly built AIX, then test two tasks, deleting task 2, and paused timer at 2:33.
5. Update this handoff file at the end of the next session.
