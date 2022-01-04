package ru.bstu.it41.service.coordination;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import etr.android.reamp.mvp.ReampFragment;
import etr.android.reamp.mvp.ReampPresenter;
import ru.bstu.it41.service.MainActivity;
import ru.bstu.it41.service.MyFirebaseMessagingService;
import ru.bstu.it41.service.R;
import ru.bstu.it41.service.models.Message;
import ru.bstu.it41.service.models.Offer;
import ru.bstu.it41.service.models.Tasks;
import ru.bstu.it41.service.models.Tender;
import ru.bstu.it41.service.models.Userinfo;
import ru.bstu.it41.service.profile.review.add.ReviewAddActivity;

/**
 * Created by Герман on 18.10.2017.
 */

public class ChatFragment extends ReampFragment<ChatFragmentPresenter, ChatFragmentState> {
    //Элементы управления
    // private int currentTitle = R.string.title_tasks;
    private int downloadTitle = R.string.title_messages_downloads;

    public static String EXTRA_IS_CLIENT = "ru.bstu.it41.service.Coordination.is_client";
    public static String EXTRA_TENDER_ID = "ru.bstu.it41.service.Coordination.tender_id";

    //private ProgressBar mProgressBar;
    private TextView mTextListEmpty;
    private RecyclerView mRecycleChat;
    FloatingActionButton mFabAdd;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageButton mButtonSend;
    private Button mButtonWriteReview;
    private EditText mEditMessage;

    private MessageAdapter mMessageAdapter;

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        MyFirebaseMessagingService.setNotification(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

        MyFirebaseMessagingService.setNotification(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStateChanged(ChatFragmentState stateModel) {
        if (!stateModel.isDownloadInProgress())
            getActivity().setTitle(stateModel.getOtherUserinfo().getFirstname() + " " + stateModel.getOtherUserinfo().getLastname());
        else
            getActivity().setTitle(downloadTitle);

        if (!stateModel.isDownloaded()) {
            stateModel.setDownloaded(true);
            getPresenter().downloadAllMessages();
            //getPresenter().sendToken(FirebaseInstanceId.getInstance().getToken());
        }

        if(stateModel.getTask().getStatus().equals(Tasks.Status.STATUS_EXECUTING)){
            mButtonWriteReview.setVisibility(View.GONE);
            mButtonSend.setVisibility(View.VISIBLE);
            mEditMessage.setVisibility(View.VISIBLE);
        }else{
            mButtonWriteReview.setVisibility(View.VISIBLE);
            mButtonSend.setVisibility(View.GONE);
            mEditMessage.setVisibility(View.GONE);
        }

        /*if(stateModel.isDownloadInProgress()){
            mSwipeRefreshLayout.setRefreshing(true);
            stateModel.setDownloadInProgress(false);
        }*/

        /*if (stateModel.getTaskAndTenders().size() == 0)
            mTextListEmpty.setVisibility(View.VISIBLE);
        else
            mTextListEmpty.setVisibility(View.GONE);*/

        mMessageAdapter.notifyDataSetChanged();

        mRecycleChat.scrollToPosition(getPresenter().getStateModel().getMessages().size()-1);

        if (!stateModel.isDownloadInProgress()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        if(stateModel.isReviewWritten()){
            mButtonWriteReview.setVisibility(View.GONE);
        }
        else
            mButtonWriteReview.setVisibility(View.VISIBLE);
    }

    @Override
    public ReampPresenter<ChatFragmentState> onCreatePresenter() {
        ChatFragmentPresenter presenter = new ChatFragmentPresenter();
        return presenter;
    }

    @Override
    public ChatFragmentState onCreateStateModel() {
        ChatFragmentState state = new ChatFragmentState();

        if (this.getArguments() != null && this.getArguments().containsKey(EXTRA_TENDER_ID)
                && this.getArguments().containsKey(EXTRA_IS_CLIENT)) {

            state.setTenderId(getArguments().getInt(EXTRA_TENDER_ID));
            state.setClient(getArguments().getBoolean(EXTRA_IS_CLIENT));

            state.setMessages(new Select().from(Message.class).where("auctionId = ?",
                    state.getTenderId()).orderBy("date").<Message>execute());

            Tender tender = new Select().from(Tender.class).where("tenderId = ?",
                    state.getTenderId()).<Tender>executeSingle();

            Tasks task = new Select().from(Tasks.class).where("taskId = ?",
                    tender.getTaskId()).<Tasks>executeSingle();
            state.setTask(task);

            if(!state.isClient()) {
                state.setOtherUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                        task.getUserId()).<Userinfo>executeSingle());
            } else{
                try {
                    Offer offer = new Select().from(Offer.class).where("tenderId = ? and selected = 1",
                            state.getTenderId()).<Offer>executeSingle();

                    state.setOtherUserinfo(new Select().from(Userinfo.class).where("userId = ?",
                            offer.getUserId()).<Userinfo>executeSingle());
                }catch (Exception e){
                    Toast.makeText(getContext(),"Не найдено предложение",Toast.LENGTH_SHORT).show();
                }
            }

            state.setDownloaded(false);
        }

        // Log and toast
        //Log.d(TAG, "token = " + token);
        //Toast.makeText(MainActivity.this, "token = " + token, Toast.LENGTH_SHORT).show();

        return state;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        mRecycleChat = view.findViewById(R.id.recycle_chat);

        mRecycleChat.setLayoutManager(new LinearLayoutManager(getContext()));
        mMessageAdapter = new MessageAdapter(getPresenter(), getContext());
        mRecycleChat.setAdapter(mMessageAdapter);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().downloadAllMessages();

                if(!getPresenter().getStateModel().isTokenSent()){
                    getPresenter().sendToken(FirebaseInstanceId.getInstance().getToken());
                }
            }
        });

        mButtonSend = view.findViewById(R.id.button_send);
        mEditMessage = view.findViewById(R.id.edit_message);
        mButtonWriteReview = view.findViewById(R.id.button_write_review);

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mEditMessage.getText().toString().trim().equals("")) {
                    getPresenter().sendMessage(mEditMessage.getText().toString());
                    mEditMessage.setText("");
                }
            }
        });

        mButtonWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ReviewAddActivity.getIntent(getContext(),getPresenter().getStateModel().getTenderId(),
                        getPresenter().getStateModel().getOtherUserinfo().getUserId());
                startActivity(intent);
            }
        });
        mEditMessage.setImeOptions(EditorInfo.IME_ACTION_DONE);

        mEditMessage.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event)
            {
                if(!mEditMessage.getText().toString().trim().equals("")) {
                    getPresenter().sendMessage(mEditMessage.getText().toString());
                    mEditMessage.setText("");
                }

                return true;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if(getPresenter().getStateModel().getTask().getStatus().equals(Tasks.Status.STATUS_EXECUTING)) {
            inflater.inflate(R.menu.fragment_chat_menu, menu);

            if(!getPresenter().getStateModel().isClient())
                menu.removeItem(R.id.menu_item_task_complete);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_task_complete:
                getPresenter().completeTask();
                return true;
            case R.id.menu_item_task_finish:
                getPresenter().finishTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static public ChatFragment getInstance(int tenderId, boolean isClient) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_TENDER_ID, tenderId);
        bundle.putBoolean(EXTRA_IS_CLIENT, isClient);
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    static public ChatFragment getInstance(Bundle bundle) {
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    static public Bundle getExtras(int tenderId, boolean isClient) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_TENDER_ID, tenderId);
        bundle.putBoolean(EXTRA_IS_CLIENT, isClient);
        return bundle;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message message) {
        if(message.getDate()!=null){
            message.save();
            getPresenter().getStateModel().getMessages().add(message);
            mMessageAdapter.notifyItemInserted(getPresenter().getStateModel().getMessages().size()-1);
            mRecycleChat.scrollToPosition(getPresenter().getStateModel().getMessages().size()-1);
        }
    }
}
