## Developers
If you want to contribute to LoTAS, you need to understand how the current system works. I am using an pre-processor made by the Replay-Mod guy... He is a legend!

### Eclipse
Simply import the project using "Import -> Gradle Project"

You need to decompile Minecraft first, go into `tasks\` and run `SetupDecompWorkspace_and_AT.launch` or `GenSources.launch`. This can take up to 40 minutes... :(
After that you can start programming. The Code to work with is in the 'core' subproject.

Here's how to change the MC Version:
Change the Version in the version.gradle. (11202 = 1.12.2 XYYZZ)
Now, run the `Update_Version.launch` File. Don't Forget to refresh the Gradle Project after that

And Here's how to export the Project:
Run the `Build-All.launch` File in `tasks\`. The JAR's will be in versions\X.YY.Z\build\libs\´.


### Settings up a Working Set to make the Workspace look cleaner.
Open the `Package Explorer` select and right click on all the versions (1.12, 1.12.2, 1.etc...) and 'versions' and click "Assign Working Set", then create a new Working set and put all of them in. Do the same (with a new set) for core and LoTAS... and now you can click on "..." and select the Working Set with 2 Subprojects. Now it's visually easier to work with the core project.

## Pushing
Before Pushing, please switch to 1.12.2 again