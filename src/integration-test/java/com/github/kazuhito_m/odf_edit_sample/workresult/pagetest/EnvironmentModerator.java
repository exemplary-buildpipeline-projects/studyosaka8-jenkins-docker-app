package com.github.kazuhito_m.odf_edit_sample.workresult.pagetest;

import org.apache.commons.lang3.StringUtils;

public class EnvironmentModerator {

    public static final String APP_ROOT_URL_ENV = "it.appRootUrl";

    public static final String SELENIUME_REMOTE_DRIVER_URL_ENV = "it.seleniumeRemoteDriverUrl";

    /**
     * 画面テストする際の「てっぺんのURL」を返す
     *
     * @return
     */
    public static String getAppRootUrl() {
        String url = System.getProperty(APP_ROOT_URL_ENV);
        if (StringUtils.isEmpty(url)) {
            return "http://localhost:8080/";
        }
        return url;
    }

    /**
     * 画面テストする際の「RemoteWebDriverを使う場合」のそのURL。
     *
     * @return
     */
    public static String getSeleniumeRemoteDriverUrl() {
        return System.getProperty(SELENIUME_REMOTE_DRIVER_URL_ENV);
    }

}
