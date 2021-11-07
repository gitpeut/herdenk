package org.peut.herdenk.utility;

public enum Access {
    NONE(0),
    PUBLIC(1),
    READ(2),
    WRITE(3),
    OWNER(4);

    private final int level;

    Access(int level){
        this.level = level;
    }

    public int getLevel(){
        return( this.level );
    }

    public static boolean atLeast( String candidate, String minLevel){
        return Access.valueOf(candidate).level >=  Access.valueOf( minLevel ).level;
    }

    public static boolean isLess( String candidate, String topLevel){
        return Access.valueOf(candidate).level <  Access.valueOf( topLevel ).level;
    }

    public static boolean isNameValid(String name) {
        boolean valid = true;
        try {
            int dummy = Access.valueOf(name).getLevel();
        } catch (IllegalArgumentException e) {
            valid = false;
        }
        return valid;
    }

}
