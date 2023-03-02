package dev.kyle.enderchest.type;

import lombok.Getter;

/**
 * @author KyleS1872
 */
public enum MapType {

    //Misc
    None("None"),
    Other("Other"),
    Unknown("Unknown"),

    //Dedicated
    Lobby("Lobby"),

    //Games
    UHC("UHC", "Ultra Hardcore"),
    UHCSolo("Ultra Hardcore Solo", UHC.getFolderName()),

    Micro("Micro Battle"),

    Skywars("Skywars"),
    SkywarsTeams("Skywars Teams", Skywars.getFolderName());


    @Getter
    private final String categoryName;

    private final String folderName;

    MapType(String categoryName){
        this(categoryName, null);
    }

    MapType(String categoryName, String folderName){
        this.categoryName = categoryName;
        this.folderName = folderName;
    }

    public String getFolderName(){
        if(folderName == null)
            return categoryName != null ? categoryName : Unknown.categoryName;

        return folderName;
    }

    public static MapType getEnum(String value) {
        for(MapType v : values())
            if(v.getCategoryName().equalsIgnoreCase(value)) return v;

        for(MapType v : values())
            if(v.getFolderName().equalsIgnoreCase(value)) return v;

        return Unknown;
    }

}
