public enum Access {
    NONE(0),
    PUBLIC(1),
    READ(2),
    WRITE(3),
    OWNER(4);

    private int level;

    Access( int level){
        this.level = level;
    }
    public int getLevel(){
        return( this.level );
    }

    public boolean atLeast( String candidate, String minLevel){
        return Access.valueOf(candidate).level >=  Access.valueOf( minLevel ).level;
    }

    public boolean isLess( String candidate, String topLevel){
        return Access.valueOf(candidate).level <  Access.valueOf( topLevel ).level;
    }



}
