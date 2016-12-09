import com.github.kazuhito_m.odf_edit_sample.workresult.pagetest.EnvironmentModerator;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

public class SystemPropertyTest {

    @Test
    public void システムプロパティを出す() throws IOException {
        System.out.println("システムプロパティを出す");
        Properties properties = System.getProperties();
        FileOutputStream fso = new FileOutputStream("./test.log");
        PrintStream ps = new PrintStream(fso);
        properties.list(ps);

        ps.println("getAppRootUrl : " + EnvironmentModerator.getAppRootUrl());
        ps.println("getSeleniumeRemoteDriverUrl : " + EnvironmentModerator.getSeleniumeRemoteDriverUrl());

        ps.close();
        fso.close();
    }

}
