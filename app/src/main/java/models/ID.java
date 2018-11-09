package models;

public class ID {
    private String TYPE;
    private Long ID;
    private String NAME;
    private String SURNAME;
    private String DATE_OF_BIRTH;
    private String SERIAL_NO;
    private String VALID_UNTIL;
    private String NATIONALITY;


    public ID(String TYPE, Long ID, String NAME, String SURNAME, String DATE_OF_BIRTH, String SERIAL_NO, String VALID_UNTIL, String NATIONALITY) {
        this.TYPE = TYPE;
        this.ID = ID;
        this.NAME = NAME;
        this.SURNAME = SURNAME;
        this.DATE_OF_BIRTH = DATE_OF_BIRTH;
        this.SERIAL_NO = SERIAL_NO;
        this.VALID_UNTIL = VALID_UNTIL;
        this.NATIONALITY = NATIONALITY;
    }

    public ID() {
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getSURNAME() {
        return SURNAME;
    }

    public void setSURNAME(String SURNAME) {
        this.SURNAME = SURNAME;
    }

    public String getDATE_OF_BIRTH() {
        return DATE_OF_BIRTH;
    }

    public void setDATE_OF_BIRTH(String DATE_OF_BIRTH) {
        this.DATE_OF_BIRTH = DATE_OF_BIRTH;
    }

    public String getSERIAL_NO() {
        return SERIAL_NO;
    }

    public void setSERIAL_NO(String SERIAL_NO) {
        this.SERIAL_NO = SERIAL_NO;
    }

    public String getVALID_UNTIL() {
        return VALID_UNTIL;
    }

    public void setVALID_UNTIL(String VALID_UNTIL) {
        this.VALID_UNTIL = VALID_UNTIL;
    }

    public String getNATIONALITY() {
        return NATIONALITY;
    }

    public void setNATIONALITY(String NATIONALITY) {
        this.NATIONALITY = NATIONALITY;
    }
}
