package br.com.flygowmobile.enums;

import br.com.flygowmobile.Utils.App;
import br.com.flygowmobile.activity.R;

/**
 * Created by Tiago Rocha Gomes on 07/05/14.
 */
public enum StaticMessages {
    TIMEOUT(R.string.timeout),
    NOT_SERVICE(R.string.notService),
    WAIT(R.string.wait),
    LOCAL_LOAD(R.string.localLoad),
    REGISTER_FROM_SERVER(R.string.registerFromServer),
    SUCCESS_SAVE_IN_SERVER(R.string.successSaveInServer),
    CALL_CONFIGURATION_FROM_SERVER(R.string.callConfigurationFromServer),
    EXCEPTION(R.string.exception),
    MISSING_CONFIGURATIONS(R.string.missingConfigurations),
    COIN_CONFIGURATION(R.string.coinConfiguration),
    ATTENDANT_CONFIGURATION(R.string.attendantConfiguration),
    ADVERTISEMENT_CONFIGURATION(R.string.advertisementConfiguration),
    MANDATORY_CONFIGURATIONS(R.string.mandatoryConfigurations),
    DEFINE_CHOICES(R.string.defineChoices),
    MORE_DETAILS(R.string.moreDetails),
    LOADING_ADVERTISEMENTS(R.string.loadingAdvertisements),
    LOADING_MEDIA_PRODUCTS(R.string.loadingMediaProducts),
    LOADING_MEDIA_PROMOTIONS(R.string.loadingMediaPromotions),
    LOAD_PRODUCT(R.string.loadProduct),
    LOAD_PROMOTION(R.string.loadPromotion),
    WARNING_LOAD_PRODUCTS(R.string.warningLoadProducts),
    WARNING_LOAD_PROMOTIONS(R.string.warningLoadPromotions),
    WARNING_LOAD_ADVERTISEMENTS(R.string.warningLoadAdvertisements),
    FREE(R.string.free),
    MAXIMUM_OF(R.string.maximunOf),
    OPTIONS(R.string.options),
    QUANTITY_SUBTILE(R.string.quantitySubtitle),
    OBSERVATIONS_SUBTITLE(R.string.observationsSubtitle),
    OBSERVATIONS_EMPTY_TEXT(R.string.observationsEmptyTitle),
    NO_OBSERVATIONS(R.string.noObservations),
    DEFAULT_QTD_ORDER(R.string.defaultQtdOrder),
    LOAD_ORDER_ITEM(R.string.loadOrderItem),
    CALLING_ATTENDANT(R.string.callingAttendant),
    CONFIRM_CALL_ATTENDANT(R.string.confirmCallAttendant),
    CALL_ATTENDANT(R.string.calledAttendant),
    CALL_ATTENDANT_ERROR(R.string.callAttendantError),
    CART_TOTAL_VALUE(R.string.cartTotalValue),
    SAVE_ORDER_FROM_SERVER(R.string.saveOrderFromServer),
    PRICE_DESCRIPTION(R.string.priceDescription),
    PRICE_SUBDESCRIPTION(R.string.priceSubdescription),
    SELECT_ONE(R.string.selectOne),
    SELECT_ONE_PAYMENT_FORM(R.string.selectOnePaymentForm),
    CANCEL_ITEM(R.string.cartCancelItemMsg),
    EXIT_APP(R.string.exitAppMsg),
    INVALID_PASSWORD(R.string.invalidPassword),
    EMPTY_ACCOMPANIMENTS(R.string.emptyAccompaniments),
    CONFIRM_CLOSING_ATTENDANCE(R.string.confirmClosingAttendance),
    ONLY_CLOSE_ORDER(R.string.onlyCloseOrder),
    SEND_CLOSE_ORDER(R.string.sendCloseOrder),
    NEW_ATTENDANCE(R.string.newAttendanceMsg),
    ALL_ITEMS_SENDED(R.string.allItemsSended),
    CONFIRM_SEND_ORDER(R.string.confirmSendOrder),
    GENERIC(R.string.generic, "");

    private int resId;
    private String value;

    StaticMessages(int resId) {
        this.resId = resId;
    }

    StaticMessages(int resId, String value) {
        this.resId = resId;
        this.value = value;
    }

    public static StaticMessages getCustomMessage(String message) {
        GENERIC.setValue(message);
        return StaticMessages.GENERIC;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName(){
        String resourceString = App.getContext().getResources().getString(resId);
        return resourceString != null && !resourceString.equals("") ? resourceString : getValue();
    }
}
