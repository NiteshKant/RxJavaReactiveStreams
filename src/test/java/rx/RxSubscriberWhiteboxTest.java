package rx;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.tck.SubscriberWhiteboxVerification;
import org.reactivestreams.tck.TestEnvironment;
import org.testng.annotations.Test;
import rx.internal.RxSubscriberToRsSubscriberAdapter;
import rx.test.IterableDecrementer;

@Test
public class RxSubscriberWhiteboxTest extends SubscriberWhiteboxVerification<Long> {

    public static final long DEFAULT_TIMEOUT_MILLIS = 300L;

    protected RxSubscriberWhiteboxTest() {
        super(new TestEnvironment(DEFAULT_TIMEOUT_MILLIS));
    }

    @Override
    public Subscriber<Long> createSubscriber(final WhiteboxSubscriberProbe<Long> probe) {
        return new RxSubscriberToRsSubscriberAdapter<Long>(new rx.Subscriber<Long>() {

            @Override
            public void onStart() {
                probe.registerOnSubscribe(new SubscriberPuppet() {
                    @Override
                    public void triggerRequest(long elements) {
                        request(elements);
                    }

                    @Override
                    public void signalCancel() {
                        unsubscribe();
                    }
                });
            }

            @Override
            public void onNext(Long t) {
                probe.registerOnNext(t);
            }

            @Override
            public void onError(Throwable t) {
                probe.registerOnError(t);
            }

            @Override
            public void onCompleted() {
                probe.registerOnComplete();
            }
        });
    }

    @Override
    public Publisher<Long> createHelperPublisher(long elements) {
        return RxReactiveStreams.toPublisher(Observable.from(new IterableDecrementer(elements)));
    }

    @Override
    public void spec309_callingRequestWithNegativeNumberMustThrow() throws Throwable {
        notVerified(); // nonsense test, should be a publisher test
    }

    @Override
    public void spec309_callingRequestZeroMustThrow() throws Throwable {
        notVerified(); // nonsense test, should be a publisher test
    }

    @Override
    public void spec317_mustSignalOnErrorWhenPendingAboveLongMaxValue() throws Throwable {
        notVerified(); // nonsense test, should be a publisher test
    }
}
