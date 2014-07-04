package org.wso2.carbon.mb.ui.test.login;

import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.mb.integration.common.utils.backend.MBIntegrationUiBaseTest;
import org.wso2.mb.integration.common.utils.ui.UIElementMapper;
import org.wso2.mb.integration.common.utils.ui.home.HomePage;
import org.wso2.mb.integration.common.utils.ui.login.LoginPage;

public class LoginTestCase extends MBIntegrationUiBaseTest {

    @BeforeClass()
    public void init() throws Exception {
        super.init();
    }

    @Test()
    public void testLogin() throws Exception {
        driver.get(getLoginURL());
        LoginPage loginPage = new LoginPage(driver);
        HomePage homePage = loginPage.loginAs(mbServer.getContextTenant().getContextUser().getUserName(),
                mbServer.getContextTenant().getContextUser().getPassword());
        homePage.logout();

//        UIElementMapper mapper = UIElementMapper.getInstance();
//        driver.findElement(By.linkText(mapper.getElement("search.page.link"))).click();

    }

    @AfterClass()
    public void tearDown(){
       driver.quit();
    }
}
