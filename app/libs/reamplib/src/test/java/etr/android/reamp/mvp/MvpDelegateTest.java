package etr.android.reamp.mvp;

import android.app.Activity;
import android.content.Context;

import org.junit.Test;

public class MvpDelegateTest extends BaseTest {

    @Test
    public void normalBehaviorWithRegularActivity() throws Exception {
        MockActivity activity = new MockActivity();
        MvpDelegate mvpDelegate = new MvpDelegate(activity);
        mvpDelegate.onResult(0, 0, null);

    }

    private class MockActivity extends Activity implements ReampView {

        @Override
        public Context getContext() {
            return this;
        }

        @Override
        public void onStateChanged(ReampStateModel stateModel) {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public String getMvpId() {
            return null;
        }

        @Override
        public ReampStateModel onCreateStateModel() {
            return null;
        }

        @Override
        public ReampPresenter onCreatePresenter() {
            return null;
        }

        @Override
        public ReampPresenter getPresenter() {
            return new ReampPresenter();
        }
    }
}