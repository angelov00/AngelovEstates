package com.softuni.angelovestates.model.enums;

public enum ProvinceEnum {

    BLAGOEVGRAD("Blagoevgrad"),
    BURGAS("Burgas"),
    VARNA("Varna"),
    VELIKO_TARNOVO("Veliko Tarnovo"),
    VIDIN("Vidin"),
    VRATSA("Vratsa"),
    GABROVO("Gabrovo"),
    DOBRICH("Dobrich"),
    KARDZHALI("Kardzhali"),
    KYUSTENDIL("Kyustendil"),
    LOVECH("Lovech"),
    MONTANA("Montana"),
    PAZARDZHIK("Pazardzhik"),
    PERNIK("Pernik"),
    PLEVEN("Pleven"),
    PLOVDIV("Plovdiv"),
    RAZGRAD("Razgrad"),
    RUSE("Ruse"),
    SHUMEN("Shumen"),
    SILISTRA("Silistra"),
    SLIVEN("Sliven"),
    SMOLYAN("Smolyan"),
    SOFIA_CITY("Sofia City"),
    SOFIA_PROVINCE("Sofia Province"),
    STARA_ZAGORA("Stara Zagora"),
    TARGOVISHTE("Targovishte"),
    HASKOVO("Haskovo"),
    YAMBOL("Yambol");

    private final String displayValue;

    ProvinceEnum(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDispayValue() {
        return this.displayValue;
    }

}
