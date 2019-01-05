package com.rs.cache;

/**
 *
 * @author paolo
 *
 */
public enum RS2Indexes {

    /*
     * Loaded: Skeletons, type=0
    Loaded: Skins, type=1
    Loaded: Config, type=2
    Loaded: Intefaces, type=3
    Loaded: Sound effects, type=4
    Loaded: Landscapes, type=5
    Loaded: Music, type=6
    Loaded: Models, type=7
    Loaded: Sprites, type=8
    Loaded: Textures, type=9
    Loaded: Huffman encoding, type=10
    Loaded: Music2, type=11
    Loaded: Interface scripts, type=12
    Loaded: Fonts, type=13
    Loaded: Sound effects2, type=14
    Loaded: Sound effects3, type=15
    Loaded: Objects, type=16
    Loaded: Clientscript mask, type=17
    Loaded: Npcs, type=18
    Loaded: Items, type=19
    Loaded: Animations, type=20
    Loaded: Graphics, type=21
    Loaded: Script configs, type=22
    Loaded: World map, type=23
    Loaded: Quick chat messages, type=24
    Loaded: Quick chat menus, type=25
    Loaded: Native libraries, type=30
    Loaded: Graphic shaders, type=31
    Loaded: P11 Fonts/Images, type=32
    Loaded: Game tips, type=33
    Loaded: P11 Fonts2/Images, type=34
    Loaded: Theora, type=35
    Loaded: Vorbis, type=36
    Finished loading. Identified 32/36 types.
     */
    SKELETONS(0),
    FRAMES(1),
    CONFIG(2),
    INTERFACES(3),
    SOUND_EFFECTS(4),
    LANDSCAPES(5),
    MUSIC(6),
    MODELS(7),
    SPRITES(8),
    TEXTURES(9),
    HUFFMAN(10),
    OBJECTS(16),
    NPCS(18),
    ITEMS(19),
    ANIMATIONS(20),
    GRAPHICS(21);



    private int index;
    RS2Indexes(int index){
        this.index = index;
    }
    public int getIndex() {
        return index;
    }

}

