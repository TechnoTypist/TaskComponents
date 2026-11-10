# AI Handoff — Task Manager App

Last updated: 2026-11-10

## Project and user preferences
- MIT App Inventor project: Task Focus Assistant / APCSP_CPT_PRACTICE.
- Main repository: https://github.com/TechnoTypist/TaskManagerApp (branch: main).
- User wants exact, step-by-step block instructions with actual block names and sockets. Be direct, concise, and do not end with questions.
- Do not edit or push GitHub files unless the user explicitly asks. User previously requested GitHub uploads and a persistent handoff note; continue updating this note at the end of future sessions.
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
- User reports Start/Stop timer display now uses remaining time correctly.
- Current completed-time formatting bug: display is 0:0 instead of 0:00. In createTaskRow's Completed branch, the THEN socket for seconds currently uses a blue numeric 00 block; replace it with a pink text literal "00" (or join "0" with "0") so leading zero is preserved.

## Current bugs being worked on
1. Task display overlap: adding task 2 causes it to appear on top of task 1 rather than as a separate row below it. Deleting task 2 makes task 1 appear again. Restarting the app does not fix it. User says the previous v1.1.2 extension upgrade made no difference. Do not repeat the earlier theory about multiple Screen1 instances or ask for unrelated screen-navigation changes.
2. Timer row values: taskTime must be remaining seconds, not original minutes. refreshTaskRows should pass TinyDB1.GetValue with tag join "secondsLeft_" + loop index; ValueIfTagNotThere = selected taskTimes[loop index] * 60. In createTaskRow, minutesPart = floor(taskTime / 60), secondsPart = taskTime modulo 60. Paused task at 2:33 must continue showing 2:33; seconds must be zero-padded.

## Rush extension history and latest patch
- User uploaded source archive TaskComponents-Rush-v1.1.1(1).zip. Source includes TaskComponents.java, rush.yml, README.md, and Windows build instructions.
- v1.1.2 changed only addToParent() to use LinearLayout.LayoutParams and requestLayout/invalidate. User tested it and reports it made no difference. Do not recommend v1.1.2 again.
- Created proposed TaskComponents-Rush-v1.1.3 source ZIP at /mnt/data/TaskComponents-Rush-v1.1.3.zip. It adds a dedicated tagged vertical host inside a VerticalArrangement. CreateLayout reuses that host so all task rows live in one vertical LinearLayout, appends each root row explicitly at childCount with zero weight and top gravity, and requests layout on parent containers. Public functions/events are unchanged. This is a materially different structural change from v1.1.2.
- v1.1.3 is source-only, not compiled to AIX and not device-tested. Rush CLI is not installed in the assistant runtime; JDK 21 is available. User must compile with Rush CLI 2.1.2+ and JDK 17+ from the extracted project folder using rush build, then import the generated AIX. Do not claim it is proven until tested.
- If v1.1.3 still fails, inspect current AIA and add diagnostic APIs (e.g. child counts/parent identity) or trace exact parent view, row IDs, and child layout params before making another guess.

## Next steps
1. User should replace the blue numeric 00 in createTaskRow's Completed seconds branch with a pink text literal "00"; verify completed display is 0:00.
2. Give user the v1.1.3 ZIP and exact Windows build/import instructions. Keep the existing AIX backed up.
3. Test creating two tasks and confirm task 2 is below task 1, then delete task 2 and confirm task 1 remains.
4. If overlap remains, ask for the complete Rush build output and a screenshot of the Designer component hierarchy; inspect exact current AIA before further changes.
5. Update this handoff file at the end of the next session.
