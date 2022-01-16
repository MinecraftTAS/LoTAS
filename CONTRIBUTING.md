## Developers
If you want to contribute to LoTAS, you need to understand how the current system works. I am using an pre-processor made by the Replay-Mod guy... He is a legend!

## Eclipse

1. You need JDK 17 or newer to run 1.18 things, you can download the newest version from [here](https://adoptium.net/)  
Additionally you may want to use JDK 8 or 16, because I had trouble decompiling the source with 17... And jdk 8 may be better for the old versions...

2. Import the project using `Import -> Gradle -> Import Gradle Project`
3. To set up the workspace, navigate to the `Gradle Task Window`. If everything worked out as it should, you should see folders with LoTAS-Fabric and LoTAS-Forge
4. For Forge, you need to doubleclick `LoTAS-Forge->forgegradle->setupDecompWorkspace` or something else if you know what you are doing. Running sDW will take a really long time since it decompiles 1.8.9-1.12.2. (You can also decompile single versions by opening the correct version folder. `LoTAS-Forge->1.12.2->forgegradle->setupDecompWorkspace`)
5. If that is complete run `LoTAS-Forge->ide->eclipse`
6. Rightclick on the versions project in the Package Explorer, then choose "Gradle->Refresh Gradle Project" to update the files
7. For Fabric, you run `LoTASFabric->fabric->genSources` and afterwards `LoTASFabric->ide->eclipse` and refresh gradle project

### Settings up a Working Set to make the Workspace look cleaner.
Open the `Package Explorer` select and right click on all the versions (1.12, 1.12.2, 1.etc...) and 'versions' and click "Assign Working Set", then create a new Working set and put all of them in. Do the same (with a new set) for core and LoTAS... and now you can click on "..." and select the Working Set with 2 Subprojects. Now it's visually easier to work with the core project.

## Editing the source
If you set up the working set, want to edit the source under `LoTAS-Forge->versions->forge` or `LoTAS-Fabric->versions2->fabric`. This code will have all versions merged into one. You can find the source code for each version individually in the other folders under version.

## Debugging MC
You can find the file under `LoTAS-Fabric->versions2->fabric->Run_Fabric.launch`, similar to the forge folder. Select it and click on the eclipse debug.

## Switching versions
In either LoTAS-Fabric or LoTAS-Forge you can find a `versions.gradle` file. Change the specified version here.

```
Note that 1.16.0 represents the 20w14∞ snapshot, but it is labeled 1.16.0 since the preprocessor doesn't support snapshots
```
Go to the Gradle task window. Top right of that are 3 dots, and select `Show all tasks`. Now you can find the task under `LoTAS-Fabric->other` and all the way at the bottom is setCoreVersion

Afterwards you need to refresh the gradle project to fully update the forge or fabric sources.

You may also need to run genSources/setupDecompWorkspace in `LoTASFabric->fabric->fabric->genSources` again to get stuff applied...

## Building
To build everything select the `LoTASFabric->build->build`
You will find the versions under the 1.14.4-1.18.1 folders under LoTAS-Fabric/version2/mcversion/build/libs

## Pushing
Before Pushing, please switch to 1.12.2/1.14.4 again

## Troubleshooting
Yes we do understand that this setup is pretty jank... If you need help you can contact us under this discord https://discord.gg/sdMc5UrGPN and look for Pancake or Scribble