> [!IMPORTANT]  
> # This project is no longer actively being developed!
>We are shifting our focus to [TASmod](https://github.com/MinecraftTAS/TASmod).  
>For more information and plans, read this announcement: [Future of Mineraft TAS tools](https://>gist.github.com/ScribbleTAS/43b18e8a92a111e92bf4bb0d9cf49e99)

# LoTAS
Used to create non-playback, Low optimization Tool-Assisted Speedruns (LoTAS).  
You essentially play through your route in slow motion while using a recording program such as [OBS](https://obsproject.com) to capture yourself playing the game. Later you speed up the video footage and edit your attempts into a final video.
then speed up the video footage.  

LoTAS is a collection of tools commonly used for TASing, with a big focus on RNG manipulation.  

## Tools
- *Tickrate Changer*: Used for slowing down the game speed
- *Savestates*: Used for creating backups of your world, basically quick save/quick load
- *Drop Manipulation*: Manipulates mob drops to always drop the best items
- *Dragon Manipulation*: Manipulates the dragon phases
- *Item Duplication (Dupemod)*: Replicates the duping bug from vanilla, without the need to close the game.
- *Spawn Manipulation*: Spawn entities at a certain location.
- *AI Manipulation*: Let entities pathfind to a certain place  

*and more...*
## Bugs / Feature Requests
We won't accept any feature-requests at this time, as well as bug reports.

If you need help with setting up LoTAS then join this [Discord](https://discord.gg/minecraft-tas-373166430478401555)

## Translations
Credits:  
Simplified Chinese: [RayXu6](https://github.com/Naruyoko), Proofreading by normalzombie  
Japanese: [Naruyoko](https://github.com/Naruyoko)  
Korean: Sidite  
French: azulamazigh  

# FAQ
## How do I start?
- Most of LoTAS is accessible via the in-game pause menu.
- The main menu has a "configuration" button
- Pressing F6 opens an in game HUD, where you can display various information onscreen.

## Can I disable the potion in the hotbar?
No, we do not provide that feature. We want to prevent cheating with our mod and this is our compromise for making it not too annoying. Please live with it and support our fight against cheaters.

## Can this mod record and play back inputs?
No, it can not. This mod was specifically made to not allow for that, hence the "non-playback" in the description. For a playback mod, check [TASmod](https://github.com/MinecraftTAS/TASmod).

## How do I use item duplication?
This differs if you are on versions below 1.12.2 (Forge) or versions higher than 1.14.4 (Fabric)

### 1.14.4-1.20.1
1. Open the pause menu and click in the top left **Save Items**
2. Throw item(s) on the ground
3. Open the pause menu and click in the top left **Load Items**
4. An item should spawn *in your inventory*

### 1.8.9-1.12.2
1. Throw the item(s) you want to duplicate on the ground
2. Open the pause menu and click in the top left **Save Items**
3. Pick up the item(s)
4. Open the pause menu and click in the top left **Load Items**
5. An item should spawn *on the ground*

## Where is the video upspeeder?
This feature that was a minor desaster...  
We decided to remove it, because video editors like [Shotcut](https://shotcut.org) are much better, provide more functionality and happen to work as intended.  

## Why is there no multiplayer?
This mod wasn't developed with multiplayer in mind. The plan was to add it later but this turned out to be *very* tedious. Multiplayer support is/was planned in [LoTAS 3.0](https://github.com/MinecraftTAS/LoTAS/tree/dev-3.0.0)
