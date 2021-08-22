package com.example.demo.e2e;

import com.github.dockerjava.api.command.CreateContainerCmd;
import java.net.URL;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class WebDriverFactory {

    private static final String VIEWER_DEBUG_IMAGE = "dperezcabrera/novnc";
    private static final String CHROME_IMAGE = "selenium/standalone-chrome:latest";
    private static final String CHROME_DEBUG_IMAGE = "selenium/standalone-chrome-debug:latest";
    private static final String FIREFOX_IMAGE = "selenium/standalone-firefox:latest";
    private static final String FIREFOX_DEBUG_IMAGE = "selenium/standalone-firefox-debug:latest";
    private static final String DEFAULT_VNC_PORT = "5900";

    private static final int REMOTE_WEBDRIVER_PORT = 4444;
    private static final int DEBUG_VIEWER_PORT = 6080;

    private boolean debug = "true".equalsIgnoreCase(System.getProperty("debug"));
    private Network network;

    public void debug(boolean debug) {
        this.debug = debug;
    }

    private Network network() {
        if (network == null) {
            network = Network.newNetwork();
        }
        return network;
    }

    private GenericContainer newBrowserContainer(String name, String image) {
        Consumer<CreateContainerCmd> cBrowser = c -> c.withName(name);
        return new GenericContainer(DockerImageName.parse(image))
                .withCreateContainerCmdModifier(cBrowser)
                .withNetwork(network())
                .withExposedPorts(REMOTE_WEBDRIVER_PORT)
                .withFileSystemBind("/dev/shm", "/dev/shm", BindMode.READ_WRITE).waitingFor(Wait.forLogMessage(".*Selenium Server is up and running on port.*", 1));
    }

    private GenericContainer newBrowserViewerContainer(String seleniumContainer) {
        return new GenericContainer(DockerImageName.parse(VIEWER_DEBUG_IMAGE))
                .withNetwork(network())
                .withEnv("REMOTE_HOST", seleniumContainer)
                .withEnv("REMOTE_PORT", DEFAULT_VNC_PORT)
                .withExposedPorts(DEBUG_VIEWER_PORT)
                .waitingFor(Wait.forLogMessage(".*INFO success: novnc entered RUNNING state, process has stayed up for > than 1 seconds.*", 1));
    }

    private WebDriver createWebDriver(String name, String image, Capabilities capabilities) throws Exception {
        log.debug("Creando WebDriver {}...", name);
        var c = newBrowserContainer(name, image);
        c.start();
        var driver = new RemoteWebDriver(new URL("http://localhost:" + c.getMappedPort(REMOTE_WEBDRIVER_PORT) + "/wd/hub"), capabilities);
        log.debug("WebDriver creado: {}", name);
        return driver;
    }

    private WebDriver createVirtualUser(String name, String image, Capabilities capabilities) {
        try {
            var driver = createWebDriver(name, image, capabilities);
            if (debug) {
                var viewer = newBrowserViewerContainer(name);
                viewer.start();
                log.debug("Viewer:\n\nhttp://localhost:" + viewer.getMappedPort(DEBUG_VIEWER_PORT) + "/vnc.html?autoconnect=true&view_only=false&password=secret\n");
            }
            return driver;
        } catch (Exception e) {
            throw new TestConfigurationException("No se pudo inicializar '" + name + "'", e);
        }
    }

    private String selectImage(String debugImage, String defaultImage) {
        return debug ? debugImage : defaultImage;
    }

    public WebDriver createChromeUser(String name) {
        return createVirtualUser(name, selectImage(CHROME_DEBUG_IMAGE, CHROME_IMAGE), new ChromeOptions());
    }

    public WebDriver createFirefoxUser(String name) {
        return createVirtualUser(name, selectImage(FIREFOX_DEBUG_IMAGE, FIREFOX_IMAGE), new FirefoxOptions());
    }
}
