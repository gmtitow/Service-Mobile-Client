package ru.bstu.it41.service.any;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.activeandroid.query.Select;
import com.google.android.gms.tasks.Task;

import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.coordination.ChatFragment;
import ru.bstu.it41.service.login.LoginFragment;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.offers.OffersFragment;
import ru.bstu.it41.service.offers.view.OfferViewActivity;
import ru.bstu.it41.service.tasks.TaskFragment;
import ru.bstu.it41.service.tenders.TenderFragment;
import ru.bstu.it41.service.tenders.tendersMap.TendersMapFragment;

import static ru.bstu.it41.service.offers.view.OfferViewActivity.OFFER_ID_KEY;
import static ru.bstu.it41.service.tasks.view.TaskViewActivity.TASK_ID_KEY;

/**
 * Created by Герман on 20.05.2018.
 */

public class FragmentRetainer {
    static public final String EXTRA_FRAGMENT = "ru.bstu.it41.service.extra_for_fragment";

    static public final int FRAGMENT_TASKS = 2;
    static public final int FRAGMENT_TASK_VIEW = 21;
    static public final int FRAGMENT_TENDERS = 3;
    static public final int FRAGMENT_TENDERS_MAP = 32;
    static public final int FRAGMENT_TENDERS_MAP_TO_POINT = 322;
    static public final int FRAGMENT_PROFILE = 1;
    static public final int FRAGMENT_LOGIN = 0;
    static public final int FRAGMENT_OFFERS = 4;
    static public final int FRAGMENT_COORDINATION = 5;

    static public final int ACTIVITY_TASK_VIEW = 10;

    static public Fragment getFragment(int fragmentId, Bundle extra){
        MainActivity.sIsCoordination = false;
        switch(fragmentId){
            case FRAGMENT_TASKS: {
                if(extra.containsKey(TASK_ID_KEY)) {
                    int taskId = extra.getInt(TASK_ID_KEY);
                    if (taskId != -1) {
                        Tasks task = new Select().from(Tasks.class).where("taskId = ?", taskId).executeSingle();
                        if (!task.getStatus().equals(Tasks.Status.STATUS_SEARCH)) {
                            return TaskFragment.getInstanceExecuted();
                        }
                    }
                }
                return TaskFragment.getInstance();
            }
            case FRAGMENT_LOGIN:
                return new LoginFragment();
            case FRAGMENT_TENDERS:
                return new TenderFragment();
            case FRAGMENT_TENDERS_MAP:
                return new TendersMapFragment();
            case FRAGMENT_TENDERS_MAP_TO_POINT:
                return TendersMapFragment.getInstance(extra);
            case FRAGMENT_OFFERS: {
                int offerId = extra.getInt(OFFER_ID_KEY);
                if(extra.containsKey(OFFER_ID_KEY)) {
                    if (offerId != -1) {
                        Offer offer = new Select().from(Offer.class).where("offerId = ?", offerId).executeSingle();
                        if (offer.getSelected() == 1) {
                            return OffersFragment.getInstanceExecuted();
                        }
                    }
                }
                return new OffersFragment();
            }
            case FRAGMENT_COORDINATION:
                MainActivity.sIsClient = extra.getBoolean(ChatFragment.EXTRA_IS_CLIENT);
                MainActivity.sIsCoordination = true;
                int tenderId = extra.getInt(ChatFragment.EXTRA_TENDER_ID);
                Tender tender = new Select().from(Tender.class).where("tenderId = ?",tenderId).executeSingle();
                MainActivity.sTaskId = tender.getTaskId();

                Offer offer = new Select().from(Offer.class).where("tenderId = ? and selected = 1",tenderId).executeSingle();
                MainActivity.sOfferId = offer.getOfferId();
                return ChatFragment.getInstance(extra);
            default:
                return null;
        }
    }
}
