# Code Style
LoTAS has (not-very) strict Code Style settings.

1) Do not duplicate Code. Lets try to copy as little code as possible
2) Keep Classes within themselves. For example: The TickrateChangerLoWidget.java should not be updated from the TickrateChanger.java
3) @Environment has to occur in front of Client-only methods
4) Mind static stuff. Again take Tickrate Changer as an example: LoTAS.java has a singleton with a single Instance
5) Full Explanation of Classes should occur in the first line
6) Every Field/Method must be annoted using this. or the entire class
7) .equals should have the pattern to look for first, making the code easier to read
8) Keep the following syntax. `this.example = 0; // Sets the local variable example to 0`. Notice the spaces inbetween almost everything?
9) Commits should not have multiple changes, commit these seperately
10) Every event should run over LoTAS.java and ClientLoTAS.java.