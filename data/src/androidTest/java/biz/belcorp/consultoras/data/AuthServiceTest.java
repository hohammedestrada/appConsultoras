package biz.belcorp.consultoras.data;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import biz.belcorp.consultoras.data.entity.LoginEntity;
import biz.belcorp.consultoras.data.net.RestApi;
import biz.belcorp.consultoras.data.net.service.IAuthService;
import biz.belcorp.library.net.exception.ServiceException;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    private static final String TAG = "AuthServiceTest";
    private IAuthService service;

    String grantType = "password";
    String username = "032610099";
    String password = "032610099";
    String pais = "PE";
    int type_datos = 1;
    int type_social = 2;

    Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        service = RestApi.INSTANCE.create(IAuthService.class);
    }

    @Test
    public void testAuthDatos() {
        TestObserver<LoginEntity> testObserver = new TestObserver<>();

        Observable<LoginEntity> observable = service.login(grantType, username, password, pais, type_datos, "");

        observable.subscribe(testObserver);
        observable.subscribe(loginEntity -> {
            Log.i(TAG, loginEntity.getAccessToken());
        }, error -> {
            ServiceException exception = RestApi.INSTANCE.parseError(error);
            Log.e(TAG, exception.getMessage());
            Log.e(TAG, exception.getResponse().toString());
        });

        testObserver.awaitTerminalEvent();

        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(entity -> entity != null);
    }

    @Test
    public void testAuthSocial() {
        TestObserver<LoginEntity> testObserver = new TestObserver<>();

        Observable<LoginEntity> observable = service.login(grantType, username, password, pais, type_social, "");

        observable.subscribe(testObserver);
        observable.subscribe(loginEntity -> {
            Log.i(TAG, loginEntity.getAccessToken());
        }, error -> {
            ServiceException exception = RestApi.INSTANCE.parseError(error);
            Log.e(TAG, exception.getMessage());
            Log.e(TAG, exception.getResponse().toString());
        });

        testObserver.awaitTerminalEvent();

        testObserver
            .assertComplete()
            .assertNoErrors()
            .assertValue(entity -> entity != null);
    }

}
