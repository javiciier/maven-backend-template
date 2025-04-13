package ${package};

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@ActiveProfiles("test")
public class ${appName}ApplicationTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue( true );
    }
}
