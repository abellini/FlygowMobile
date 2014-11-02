package br.com.flygowmobile.enums;

/**
 * Created by Tiago Rocha Gomes on 01/11/14.
 */
public enum FontTypeEnum {

    LOGOTYPE("fonts/Bad Striped.ttf"),
    PRODUCT_TITLE("fonts/ERASBD.TTF"),
    GENERAL_DESCRIPTIONS("fonts/ERASMD.TTF"),
    PRICE_DESCRIPTIONS("fonts/GABRIOLA.TTF")
    ;

    private String path;

    FontTypeEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
