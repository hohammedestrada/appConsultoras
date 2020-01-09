package biz.belcorp.consultoras.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import biz.belcorp.consultoras.data.entity.ConfigResponseEntity;
import biz.belcorp.consultoras.data.net.RestApi;
import biz.belcorp.consultoras.data.net.service.IConfigService;
import biz.belcorp.library.net.exception.ServiceException;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

@RunWith(MockitoJUnitRunner.class)
public class ConfigServiceTest {

    private static final String TAG = "ConfigServiceTest";
    private IConfigService service;

    Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        service = RestApi.INSTANCE.create(IConfigService.class);
    }

    @Test
    public void testGetConfig() {
        TestObserver<ConfigResponseEntity> testObserver = new TestObserver<>();

        Observable<ConfigResponseEntity> observable = service.get();

        observable.subscribe(testObserver);
        observable.subscribe(configEntity -> {
            Log.i(TAG, configEntity.getTextGreeting());
        }, error -> {
            ServiceException exception = RestApi.INSTANCE.parseError(error);
            Log.e(TAG, exception.getResponse().toString());
        });

        testObserver.awaitTerminalEvent();

        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(entity -> entity != null);
    }

}
