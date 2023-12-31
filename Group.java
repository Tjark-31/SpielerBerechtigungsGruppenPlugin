package com.example;

public class Group {  // um Gruppenobjekte erstellen zu können, diese Datenbanken hinzufügenzukönnen und generell damit arbeiten können 
    private String groupname;
    private String prefix;
 
    public Group( String groupname, String prefix) {
        this.groupname = groupname;
        this.prefix = prefix;
    }
 
    public String getGroupname() {
        return groupname;
    }
 
    public String getPrefix() {
        return prefix;
    }

    
}
