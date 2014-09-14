package br.com.flygowmobile.enums;

import br.com.flygowmobile.Utils.App;
import br.com.flygowmobile.activity.R;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */

public enum StaticTitles{
    ERROR(R.string.error),
    WARNING(R.string.warning),
    LOAD(R.string.loading),
    SUCCESS(R.string.successful),
    PROMOTIONS(R.string.promotions),
    INFORMATION(R.string.information),
    PROMOTION_INFO(R.string.promotionInfo),
    MENU(R.string.menu),
    SUBTITLE_MENU(R.string.subtitleMenu),
    MAKE_ORDER(R.string.makeOrder),
    ACCOMPANIMENT_POPUP(R.string.accompanimentPopup),
    CONTINUE(R.string.continueTitle),
    CANCEL(R.string.cancel),
    OK(R.string.ok),
    QUANTITY_CHOICES(R.string.quantityChoices),
    ENTER_OBSERVATIONS(R.string.enterObservations),
    MAIN_APP_TITLE(R.string.mainAppTitle),
    YOUR_ACCOUNT(R.string.yourAccount),
    CALL_ATTENDANT(R.string.callAttendant),
    YES(R.string.yes),
    NO(R.string.no),
    CART_TITLE(R.string.cartTitle),
    CART_SUBTITLE(R.string.cartSubTitle),
    CART_HEADER_DESCRIPTION(R.string.cartHeaderDescription),
    CART_HEADER_ACCOMPANIMENTS(R.string.cartHeaderAccompaniments),
    CART_HEADER_QUANTITY(R.string.cartHeaderQuantity),
    CART_HEADER_UNIT_VALUE(R.string.cartHeaderUnitValue),
    CART_HEADER_TOTAL_VALUE(R.string.cartHeaderTotalValue),
    CART_ROW_ACC_TITLE(R.string.cartRowAccTitle),
    CART_ROW_ACC_SUBTITLE(R.string.cartRowAccSubTitle),
    CART_CANCEL_ITEM(R.string.cartCancelItem),
    EXIT_APPLICATION(R.string.exitApp)
    ;

    private int resourceId;

    StaticTitles(int id) {
        this.resourceId = id;
    }

    public String getName(){
        return App.getContext().getResources().getString(resourceId);
    }
}
