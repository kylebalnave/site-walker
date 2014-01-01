import com.balnave.rambler.RamblerConfig;
import com.balnave.rambler.RamblerReport;
import com.balnave.rambler.RamblerThreadFactory;
import com.balnave.rambler.RamblerResult;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author balnave
 */
public class RamblerThreadFactoryTest {
    
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() throws Exception {
        RamblerConfig config = new RamblerConfig("http://www.disney.co.uk/disney-channel/",
                "http://www\\.disney\\.co\\.uk/disney-channel/.*",
                ".*interstitial.*");
        RamblerThreadFactory instance = new RamblerThreadFactory(config);
        Collection<RamblerResult> results = instance.getResults();
        String reportPath = "./test/report.xml";
        boolean saved = RamblerReport.toXMLFile(config.getSiteUrl(), results, reportPath);
        System.out.println(String.format("Report saved to %s : %s", reportPath, saved));
    }
    
}
