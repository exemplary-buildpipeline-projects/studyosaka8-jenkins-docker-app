package com.github.kazuhito_m.odf_edit_sample.workresult.pagetest;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

public class FirefoxWebDriverFactroy {

    private static final Logger logger = LoggerFactory.getLogger(FirefoxWebDriverFactroy.class);

    private static WebDriver driverCashe = null;

    private static DesiredCapabilities capabilities = null;

    public static WebDriver create() throws MalformedURLException {

        String remoteDriverUrl = EnvironmentModerator.getSeleniumeRemoteDriverUrl();

        if (StringUtils.isEmpty(remoteDriverUrl)) {
            return createLocalDriver();
        } else {
            return createRemoteDriver(remoteDriverUrl);
        }

    }

    private static WebDriver createLocalDriver() {
        String geckoDrivePath = FirefoxWebDriverFactroy.class.getResource("geckodriver").getPath();

        logger.debug("geckoDrivePath : " + geckoDrivePath);
        System.setProperty("webdriver.gecko.driver", geckoDrivePath);

        DesiredCapabilities capabilities = getBasicCapabilities();


        // marionetteをtrueにする
        capabilities.setCapability("marionette", true);

        // capabilitiesにプロファイルを指定してドライバを生成する
        return new MarionetteDriver(capabilities);
    }

    private static WebDriver createRemoteDriver(String remoteUrl) throws MalformedURLException {
        DesiredCapabilities capability = getBasicCapabilities();
        return new RemoteWebDriver(new URL(remoteUrl), capability);
    }

    private static DesiredCapabilities getBasicCapabilities() {

        System.setProperty("selenide.reports", "build/screenshot/");

        // https://id:pw@url/でアクセス可能なFireFox用Profileを生成・ロードする
        ProfilesIni profile = new ProfilesIni();
        FirefoxProfile myprofile = profile.getProfile("SeleniumProfile");

        // プロファイルをセットする
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability(FirefoxDriver.PROFILE, myprofile);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setJavascriptEnabled(true);
        return capabilities;
    }

}
