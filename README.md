# MapEditor
A runescape 2D mapeditor for the 503+ revision. This is still a work in progress, let me know if you have any issues. You can edit, preview,dump and pack your packs. This editor was made with the 667 revision of Runescape but should work with lower and higher revisions (not RS3). This tools makes use of alex his cache lib and refactoring from the Runelite client. This tool was made for education.
# Setup
Find config.properties and change your cache path. (be sure to use //)
cache_path=your//path//here//

Required Librarys
- Guava
- Lombok (make sure the enable annotation processing in Intelij if using)
# Landscape editor
### Features
- Edit overlays, underlays,height,mask, figure and rotation of a single tile
- Brush tiles with a custom id
- Replace all specific overlays/underlays with another one
Information about the modes
- Select -> gives iformation
- Edit -> you can edit the clicked tiles
- Click -> changes the file your clicked with the filled information
- Brush -> hold 'P' to paint the hovered overlays (If you only want to change the overlays while brushing and not the other properties leave everything blank except the field you want, look picture)
![alt text](https://cdn.discordapp.com/attachments/423780466110234624/531118968040194064/unknown.png)
![alt text](https://cdn.discordapp.com/attachments/423780466110234624/531116735621890067/unknown.png)
# Objectlayer editor
### Features
- Edit objects
- Remove all objects in a square. (x1,x2 -> y1,y2)
- Render options walls,roofs,normal objects, ground decoration
![alt text](https://cdn.discordapp.com/attachments/423780466110234624/531116871953547264/unknown.png)
