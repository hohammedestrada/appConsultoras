package biz.belcorp.consultoras.common.model.client;

import biz.belcorp.consultoras.util.anotation.ContactType;
import biz.belcorp.library.util.CountryUtil;

/**
 *
 */
public class ClienteValidator {

    public static boolean validate(ClienteModel clienteModel, String paisISO) {
        ContactoModel mobileContactoModel = clienteModel.getContactoModelMap().get(ContactType.MOBILE);
        ContactoModel phoneContactoModel = clienteModel.getContactoModelMap().get(ContactType.PHONE);
        if (mobileContactoModel == null && phoneContactoModel == null) {
            return false;
        }
        String mobile = null;
        if (mobileContactoModel != null) mobile = mobileContactoModel.getValor();
        String phone = null;
        if (phoneContactoModel != null) phone = phoneContactoModel.getValor();
        if (!validateRequiredString(mobile) && !validateRequiredString(phone)) {
            return false;
        } else if (!validateRequiredString(mobile) && validateRequiredString(phone)) {
            return validatePhone(phone, paisISO);
        } else if (validateRequiredString(mobile) && !validateRequiredString(phone)) {
            return validateMobile(mobile, paisISO);
        } else {
            return validateMobile(mobile, paisISO) && validatePhone(phone, paisISO);
        }
    }

    private static boolean validateMobile(String mobile, String paisISO) {
        return validateMobileLength(mobile, paisISO) && validateMobilePattern(mobile, paisISO);
    }

    private static boolean validatePhone(String phone, String paisISO) {
        return validatePhoneLength(phone, paisISO) && validatePhonePattern(phone, paisISO);
    }

    public static boolean validateRequiredString(String string) {
        return (string != null && !string.isEmpty());
    }

    public static boolean validateStartNumber(String mobile, String paisISO) {
        Integer mobileStartNumber = CountryUtil.getMobileStartNumberMap().get(paisISO);
        if (mobileStartNumber == null) return true;
        return mobileStartNumber.toString().equals(mobile.substring(0,1));
    }

    public static boolean validateMobileLength(String mobile, String paisISO) {
        Integer mobileLength = CountryUtil.getMobileLengthMap().get(paisISO);
        if (mobileLength == null) return true;
        return mobileLength == mobile.length();
    }

    public static boolean validatePhoneLength(String phone, String paisISO) {
        Integer phoneLength = CountryUtil.getPhoneLengthMap().get(paisISO);
        if (phoneLength == null) return true;
        return phoneLength == phone.length();
    }

    public static boolean validateMobilePattern(String mobile, String paisISO) {
        String regexp = CountryUtil.getMobileRegexpMap().get(paisISO);
        if (regexp == null) return true;
        return mobile.matches(regexp);
    }

    public static boolean validatePhonePattern(String phone, String paisISO) {
        String regexp = CountryUtil.getPhoneRegexpMap().get(paisISO);
        if (regexp == null) return true;
        return phone.matches(regexp);
    }

}
