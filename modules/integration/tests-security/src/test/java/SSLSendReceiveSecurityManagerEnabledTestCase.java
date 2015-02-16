import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.engine.context.TestUserMode;
import org.wso2.mb.integration.common.clients.AndesClient;
import org.wso2.mb.integration.common.clients.operations.utils.AndesClientUtils;
import org.wso2.mb.integration.common.utils.backend.MBIntegrationBaseTest;

import java.io.File;

import static org.testng.Assert.assertTrue;


/**
 * send messages using SSL and receive messages using SSL
 */
public class SSLSendReceiveSecurityManagerEnabledTestCase extends MBIntegrationBaseTest {

    @BeforeClass
    public void prepare() throws Exception {
        super.init(TestUserMode.SUPER_TENANT_USER);
        AndesClientUtils.sleepForInterval(15000);
    }

    @Test(groups = {"wso2.mb", "queue", "security"})
    public void performSingleQueueSendReceiveTestCase() {
        Integer sendCount = 100;
        Integer runTime = 20;
        Integer expectedCount = 100;
        String keyStorePath = System.getProperty("carbon.home") + File.separator + "repository" + File.separator
                + "resources" + File.separator + "security" + File.separator + "wso2carbon.jks";
        String trustStorePath = System.getProperty("carbon.home") + File.separator + "repository" + File.separator
                + "resources" + File.separator + "security" + File.separator + "client-truststore.jks";
        String keyStorePassword = "wso2carbon";
        String trustStorePassword = "wso2carbon";
        String sslConnectionURL = "amqp://admin:admin@carbon/carbon?brokerlist='tcp://localhost:8672?ssl='true'" +
                "&ssl_cert_alias='RootCA'&trust_store='" + trustStorePath + "'&trust_store_password='" +
                trustStorePassword
                + "'&key_store='" + keyStorePath + "'&key_store_password='" + keyStorePassword + "''";

        AndesClient receivingClient = new AndesClient("receive", "127.0.0.1:8672", "queue:SSLSingleQueue",
                "100", "false", runTime.toString(), expectedCount.toString(),
                "1", "listener=true,ackMode=1,delayBetweenMsg=0,stopAfter=" + expectedCount, sslConnectionURL);

        receivingClient.startWorking();

        AndesClient sendingClient = new AndesClient("send", "127.0.0.1:5672", "queue:SSLSingleQueue", "100", "false",
                runTime.toString(), sendCount.toString(), "1",
                "ackMode=1,delayBetweenMsg=0,stopAfter=" + sendCount, sslConnectionURL);

        sendingClient.startWorking();

        boolean receiveSuccess = AndesClientUtils.waitUntilMessagesAreReceived(receivingClient, expectedCount, runTime);
        boolean sendSuccess = AndesClientUtils.getIfSenderIsSuccess(sendingClient, sendCount);

        assertTrue(sendSuccess, "Message sending failed.");
        assertTrue(receiveSuccess, "Message receiving failed.");
    }
}
