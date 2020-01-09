package biz.belcorp.consultoras.domain;

import org.junit.Before;
import org.junit.Test;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

public class RxJavaTest {


    @Before
    public void setUp() {

    }

    @Test
    public void testTaskConstructor() throws Exception {
        getNumbers();

        execute(
            getNumbers().flatMap(this::transform).
                onErrorResumeNext(
                    askForException(
                        NumberFormatException.class,
                        getError()
                    )
                ),
            numbersObserver);
    }

    Observable<Object> getNumbers() {
        return Observable.create(e -> {
            e.onNext(1);
            e.onNext(2);
            e.onNext(3);
            e.onNext(4);
            e.onError(new NumberFormatException("ERRORRR!!!"));
            e.onNext(5);
            e.onComplete();
        });
    }

    Observable<Object> getError() {
        return Observable.create(e -> {
            e.onNext(-1);
            e.onComplete();
        });
    }

    Observable<Object> transform(Object n) {
        return Observable.create(e -> {
            e.onNext("Nuevo: " + n);
            e.onComplete();
        });
    }

    <T> void execute(Observable<Object> observable, DisposableObserver<Object> observer) {
        Preconditions.checkNotNull(observer);

        observable
            .subscribeWith(observer);
    }

    <T, E> Function<Throwable, Observable<T>> askForException(Class<E> what, Observable<T> observable) {
        return t -> {
            String name = t.getClass().getName();
            String name1 = what.getName();
            boolean equals = name.trim().equals(name1.trim());
            Observable response;
            if (equals) {
                response = observable;
            } else {
                response = Observable.error(t);
            }
            return response;
        };
    }

    private final DisposableObserver<Object> numbersObserver = new DisposableObserver<Object>() {
        @Override
        public void onNext(Object o) {
            System.out.println("Next: " + o);
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("Error: " + e);
        }

        @Override
        public void onComplete() {
            System.out.println("Completed");
        }
    };
}
