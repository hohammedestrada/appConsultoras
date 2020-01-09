package biz.belcorp.consultoras.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import biz.belcorp.consultoras.data.entity.ClienteEntity;
import biz.belcorp.consultoras.data.net.RestApi;
import biz.belcorp.consultoras.data.net.service.IClienteService;
import biz.belcorp.consultoras.data.net.service.IClienteService;
import biz.belcorp.library.net.exception.ServiceException;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceTest {

    private static final String TAG = "ClienteServiceTest";
    private IClienteService service;

    Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        service = RestApi.INSTANCE.create(IClienteService.class);
    }

    @Test
    public void testBajada() {
        TestObserver<List<ClienteEntity>> testObserver = new TestObserver<>();

        Observable<List<ClienteEntity>> observable = service.bajada(0, "");

        observable.subscribe(testObserver);
        observable.subscribe(clienteEntities -> {
            Log.i(TAG, clienteEntities.toString());
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
