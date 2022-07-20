package com.netease.nis.smartercaptcha.utils;

import com.netease.nis.captcha.CaptchaConfiguration;

/**
 * @author liuxiaoshuai
 * @date 2022/7/19
 * @desc
 * @email liulingfeng@mistong.com
 */
public class LanguageTools {
    public static CaptchaConfiguration.LangType string2LangType(String langTypeStr) {
        CaptchaConfiguration.LangType langType;
        switch (langTypeStr) {
            case "am": {
                langType = CaptchaConfiguration.LangType.LANG_AM;
            }
            break;
            case "ar": {
                langType = CaptchaConfiguration.LangType.LANG_AR;
            }
            break;
            case "as": {
                langType = CaptchaConfiguration.LangType.LANG_AS;
            }
            break;
            case "az": {
                langType = CaptchaConfiguration.LangType.LANG_AZ;
            }
            break;
            case "be": {
                langType = CaptchaConfiguration.LangType.LANG_BE;
            }
            break;
            case "bg": {
                langType = CaptchaConfiguration.LangType.LANG_BG;
            }
            break;
            case "bn": {
                langType = CaptchaConfiguration.LangType.LANG_BN;
            }
            break;
            case "bo": {
                langType = CaptchaConfiguration.LangType.LANG_BO;
            }
            break;
            case "bs": {
                langType = CaptchaConfiguration.LangType.LANG_BS;
            }
            break;
            case "ca": {
                langType = CaptchaConfiguration.LangType.LANG_CA;
            }
            break;
            case "cs": {
                langType = CaptchaConfiguration.LangType.LANG_CS;
            }
            break;
            case "da": {
                langType = CaptchaConfiguration.LangType.LANG_DA;
            }
            break;
            case "de": {
                langType = CaptchaConfiguration.LangType.LANG_DE;
            }
            break;
            case "el": {
                langType = CaptchaConfiguration.LangType.LANG_EL;
            }
            break;
            case "en":
            case "en-GB": {
                langType = CaptchaConfiguration.LangType.LANG_EN;
            }
            break;
            case "en-US": {
                langType = CaptchaConfiguration.LangType.LANG_EN_US;
            }
            break;
            case "es": {
                langType = CaptchaConfiguration.LangType.LANG_ES;
            }
            break;
            case "es-la": {
                langType = CaptchaConfiguration.LangType.LANG_ES_LA;
            }
            break;
            case "et": {
                langType = CaptchaConfiguration.LangType.LANG_ET;
            }
            break;
            case "eu": {
                langType = CaptchaConfiguration.LangType.LANG_EU;
            }
            break;
            case "fa": {
                langType = CaptchaConfiguration.LangType.LANG_FA;
            }
            break;
            case "fi": {
                langType = CaptchaConfiguration.LangType.LANG_FI;
            }
            break;
            case "fr": {
                langType = CaptchaConfiguration.LangType.LANG_FR;
            }
            break;
            case "gl": {
                langType = CaptchaConfiguration.LangType.LANG_GL;
            }
            break;
            case "gu": {
                langType = CaptchaConfiguration.LangType.LANG_GU;
            }
            break;
            case "hi": {
                langType = CaptchaConfiguration.LangType.LANG_HI;
            }
            break;
            case "hr": {
                langType = CaptchaConfiguration.LangType.LANG_HR;
            }
            break;
            case "hu": {
                langType = CaptchaConfiguration.LangType.LANG_HU;
            }
            break;
            case "id": {
                langType = CaptchaConfiguration.LangType.LANG_ID;
            }
            break;
            case "it": {
                langType = CaptchaConfiguration.LangType.LANG_IT;
            }
            break;
            case "he": {
                langType = CaptchaConfiguration.LangType.LANG_HE;
            }
            break;
            case "ja": {
                langType = CaptchaConfiguration.LangType.LANG_JA;
            }
            break;
            case "jv": {
                langType = CaptchaConfiguration.LangType.LANG_JV;
            }
            break;
            case "ka": {
                langType = CaptchaConfiguration.LangType.LANG_KA;
            }
            break;
            case "kk": {
                langType = CaptchaConfiguration.LangType.LANG_KK;
            }
            break;
            case "km": {
                langType = CaptchaConfiguration.LangType.LANG_KM;
            }
            break;
            case "kn": {
                langType = CaptchaConfiguration.LangType.LANG_KN;
            }
            break;
            case "ko": {
                langType = CaptchaConfiguration.LangType.LANG_KO;
            }
            break;
            case "lo": {
                langType = CaptchaConfiguration.LangType.LANG_LO;
            }
            break;
            case "lt": {
                langType = CaptchaConfiguration.LangType.LANG_LT;
            }
            break;
            case "lv": {
                langType = CaptchaConfiguration.LangType.LANG_LV;
            }
            break;
            case "mai": {
                langType = CaptchaConfiguration.LangType.LANG_MAI;
            }
            break;
            case "mi": {
                langType = CaptchaConfiguration.LangType.LANG_MI;
            }
            break;
            case "mk": {
                langType = CaptchaConfiguration.LangType.LANG_MK;
            }
            break;
            case "ml": {
                langType = CaptchaConfiguration.LangType.LANG_ML;
            }
            break;
            case "mn": {
                langType = CaptchaConfiguration.LangType.LANG_MN;
            }
            break;
            case "mr": {
                langType = CaptchaConfiguration.LangType.LANG_MR;
            }
            break;
            case "ms": {
                langType = CaptchaConfiguration.LangType.LANG_MS;
            }
            break;
            case "my": {
                langType = CaptchaConfiguration.LangType.LANG_MY;
            }
            break;
            case "no": {
                langType = CaptchaConfiguration.LangType.LANG_NO;
            }
            break;
            case "ne": {
                langType = CaptchaConfiguration.LangType.LANG_NE;
            }
            break;
            case "nl": {
                langType = CaptchaConfiguration.LangType.LANG_NL;
            }
            break;
            case "or": {
                langType = CaptchaConfiguration.LangType.LANG_OR;
            }
            break;
            case "pa": {
                langType = CaptchaConfiguration.LangType.LANG_PA;
            }
            break;
            case "pl": {
                langType = CaptchaConfiguration.LangType.LANG_PL;
            }
            break;
            case "pt": {
                langType = CaptchaConfiguration.LangType.LANG_PT;
            }
            break;
            case "pt-br": {
                langType = CaptchaConfiguration.LangType.LANG_PT_BR;
            }
            break;
            case "ro": {
                langType = CaptchaConfiguration.LangType.LANG_RO;
            }
            break;
            case "ru": {
                langType = CaptchaConfiguration.LangType.LANG_RU;
            }
            break;
            case "si": {
                langType = CaptchaConfiguration.LangType.LANG_SI;
            }
            break;
            case "sk": {
                langType = CaptchaConfiguration.LangType.LANG_SK;
            }
            break;
            case "sl": {
                langType = CaptchaConfiguration.LangType.LANG_SL;
            }
            break;
            case "sr": {
                langType = CaptchaConfiguration.LangType.LANG_SR;
            }
            break;
            case "sv": {
                langType = CaptchaConfiguration.LangType.LANG_SV;
            }
            break;
            case "sw": {
                langType = CaptchaConfiguration.LangType.LANG_SW;
            }
            break;
            case "ta": {
                langType = CaptchaConfiguration.LangType.LANG_TA;
            }
            break;
            case "te": {
                langType = CaptchaConfiguration.LangType.LANG_TE;
            }
            break;
            case "th": {
                langType = CaptchaConfiguration.LangType.LANG_TH;
            }
            break;
            case "fil": {
                langType = CaptchaConfiguration.LangType.LANG_FIL;
            }
            break;
            case "tr": {
                langType = CaptchaConfiguration.LangType.LANG_TR;
            }
            break;
            case "ug": {
                langType = CaptchaConfiguration.LangType.LANG_UG;
            }
            break;
            case "uk": {
                langType = CaptchaConfiguration.LangType.LANG_UK;
            }
            break;
            case "ur": {
                langType = CaptchaConfiguration.LangType.LANG_UR;
            }
            break;
            case "uz": {
                langType = CaptchaConfiguration.LangType.LANG_UZ;
            }
            break;
            case "vi": {
                langType = CaptchaConfiguration.LangType.LANG_VI;
            }
            break;
            case "zh-HK": {
                langType = CaptchaConfiguration.LangType.LANG_ZH_HK;
            }
            break;
            case "zh-TW": {
                langType = CaptchaConfiguration.LangType.LANG_ZH_TW;
            }
            break;
            default: {
                langType = CaptchaConfiguration.LangType.LANG_ZH_CN;
            }
        }
        return langType;
    }
}
