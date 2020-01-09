package biz.belcorp.consultoras.domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TaskTest {

    private static final int FAKE_ID = -1;

    @Before
    public void init() {

    }

    @Test
    public void testTaskConstructor() throws Exception {
        final int id = -1;

        assertThat(id).isEqualTo(FAKE_ID);
    }
}

