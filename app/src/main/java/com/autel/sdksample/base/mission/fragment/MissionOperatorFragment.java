package com.autel.sdksample.base.mission.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.autel.common.CallbackWithNoParam;
import com.autel.common.CallbackWithOneParam;
import com.autel.common.CallbackWithOneParamProgress;
import com.autel.common.error.AutelError;
import com.autel.common.mission.AutelMission;
import com.autel.common.mission.MissionExecuteState;
import com.autel.common.mission.RealTimeInfo;
import com.autel.common.mission.xstar.OrbitMission;
import com.autel.common.mission.xstar.Waypoint;
import com.autel.common.mission.xstar.WaypointMission;
import com.autel.internal.product.EvoAircraftImpl;
import com.autel.sdk.mission.MissionManager;
import com.autel.sdk.product.BaseProduct;
import com.autel.sdk.product.XStarAircraft;
import com.autel.sdk.product.XStarPremiumAircraft;
import com.autel.sdksample.R;
import com.autel.sdksample.TestApplication;
import com.autel.sdksample.base.mission.MapActivity;

import java.util.List;
import java.util.Objects;


public class MissionOperatorFragment extends Fragment {
    Button missionPrepare;
    Button missionStart;
    Button missionPause;
    Button missionResume;
    Button missionCancel;
    Button missionDownload;
    Button yawRestore;
    Button getCurrentMission;
    Button getMissionExecuteState;
    ProgressBar progressBarDownload;
    ProgressBar progressBarPrepare;

    MissionManager missionManager;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return createView(R.layout.fragment_mission_menu);
    }

    protected MissionManager getMissionManager() {
        BaseProduct
                product = ((TestApplication) Objects.requireNonNull(getActivity()).getApplicationContext()).getCurrentProduct();
        if (null != product) {
            switch (product.getType()) {
                case X_STAR:
                    return product.getMissionManager();
                case PREMIUM:
                    return product.getMissionManager();
                case EVO:
                    return product.getMissionManager();
            }

        }
        return null;
    }

    protected View createView(@LayoutRes int resource) {
        View view = View.inflate(getContext(), resource, null);
        initUi(view);
        return view;
    }

    private void initUi(final View view) {
        if (getActivity() != null){
            ((MapActivity) getActivity()).updateMissionInfo("Mission state : ");
            ((MapActivity) getActivity()).updateLogInfo("RealTimeInfo : ");
        }
        missionManager = getMissionManager();

        view.findViewById(R.id.setRealTimeInfoListener).setOnClickListener(v -> {
            if (null != missionManager) {
                missionManager.setRealTimeInfoListener(new CallbackWithOneParam<RealTimeInfo>() {
                    @Override
                    public void onSuccess(RealTimeInfo realTimeInfo) {
                        if (getActivity() != null){
                            ((MapActivity) getActivity()).updateLogInfo("RealTimeInfo : " + realTimeInfo);
                        }
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        if (getActivity() != null)
                            ((MapActivity) getActivity()).updateMissionInfo("Mission state : " + autelError.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.resetRealTimeInfoListener).setOnClickListener(v -> {
            if (null != missionManager) {
                missionManager.setRealTimeInfoListener(null);
            }
        });

        final Context applicationContext = Objects.requireNonNull(getActivity()).getApplicationContext();
        progressBarDownload = (ProgressBar) view.findViewById(R.id.progressBarDownload);
        progressBarPrepare = (ProgressBar) view.findViewById(R.id.progressBarPrepare);

        missionPrepare = (Button) view.findViewById(R.id.missionPrepare);
        missionPrepare.setOnClickListener(v -> {
            if (null != missionManager) {
                progressBarPrepare.setVisibility(View.VISIBLE);
                missionManager.prepareMission(((MapActivity)getActivity()).createMission(), new CallbackWithOneParamProgress<Boolean>() {
                    @Override
                    public void onProgress(float v) {

                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        toastView( R.string.mission_prepare_notify);
                        mHandler.post(() -> progressBarPrepare.setVisibility(View.GONE));

                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        toastView(autelError);
                        mHandler.post(() -> progressBarPrepare.setVisibility(View.GONE));
                    }
                });
            }
        });

        missionStart = (Button) view.findViewById(R.id.missionStart);
        missionStart.setOnClickListener(v -> {
            if (null != missionManager) {
                missionManager.startMission("",new CallbackWithNoParam() {
                    @Override
                    public void onSuccess() {
                        toastView( R.string.mission_start_notify);
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        toastView(autelError);
                    }
                });
            }
        });

        missionPause = (Button) view.findViewById(R.id.missionPause);
        missionPause.setOnClickListener(v -> {
            if (null != missionManager) {
                missionManager.pauseMission(new CallbackWithNoParam() {
                    @Override
                    public void onSuccess() {
                        toastView( R.string.mission_pause_notify);
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        toastView(autelError);
                    }
                });
            }
        });

        missionResume = (Button) view.findViewById(R.id.missionResume);
        missionResume.setOnClickListener(v -> {
            if (null != missionManager) {
                missionManager.resumeMission("",new CallbackWithNoParam() {
                    @Override
                    public void onSuccess() {
                        toastView( R.string.mission_resume_notify);
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        toastView(autelError);
                    }
                });
            }
        });

        missionCancel = (Button) view.findViewById(R.id.missionCancel);
        missionCancel.setOnClickListener(v -> {
            if (null != missionManager) {
                missionManager.cancelMission(new CallbackWithNoParam() {
                    @Override
                    public void onSuccess() {
                        toastView( R.string.mission_cancel_notify);
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        toastView(autelError);
                    }
                });
            }
        });

        missionDownload = (Button) view.findViewById(R.id.missionDownload);
        missionDownload.setOnClickListener(v -> {
            if (null != missionManager) {
                progressBarDownload.setVisibility(View.VISIBLE);
                missionManager.downloadMission(new CallbackWithOneParamProgress<AutelMission>() {
                    @Override
                    public void onProgress(float v) {

                    }

                    @Override
                    public void onSuccess(AutelMission autelMission) {
                        toastView( R.string.mission_download_notify);
                        mHandler.post(() -> progressBarDownload.setVisibility(View.GONE));

                        if (autelMission instanceof WaypointMission) {

                            List<Waypoint> wpList = ((WaypointMission) autelMission).wpList;
                        } else if (autelMission instanceof OrbitMission) {

                        }
                        showDownloadMission(autelMission.toString());
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        toastView(autelError);
                        mHandler.post(() -> progressBarDownload.setVisibility(View.GONE));
                    }
                });
            }
        });

        view.findViewById(R.id.yawRestore).setOnClickListener(v -> {
            if (null != missionManager) {
                missionManager.yawRestore(new CallbackWithNoParam() {
                    @Override
                    public void onSuccess() {
                        toastView(R.string.mission_yaw_restore_notify);
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        toastView(autelError);
                    }
                });
            }
        });

        view.findViewById(R.id.getCurrentMission).setOnClickListener(v -> {
            if (null != missionManager) {
                AutelMission mission = missionManager.getCurrentMission();
                ((MapActivity) getActivity()).updateLogInfo(null != mission ? mission.toString() : "null");
            }
        });

        view.findViewById(R.id.getMissionExecuteState).setOnClickListener(v -> {
            if (null != missionManager) {
                MissionExecuteState state = missionManager.getMissionExecuteState();
                ((MapActivity) getActivity()).updateLogInfo(null != state ? state.toString() : "UNKNOWN");
            }
        });
        final TextView layoutShowState = (TextView)view.findViewById(R.id.layoutShowState);
        layoutShowState.setOnClickListener(v -> {
            int distance = view.findViewById(R.id.operatorScroll).getWidth();
            Log.v("showhide", "x  : "+view.getX());
            boolean toShow = view.getX()< 0;
            if(toShow){
                view.setX(0);
                layoutShowState.setText("HIDE");
                ((MapActivity)getActivity()).setMissionContainerVisible(true);
            }else{
                view.setX(-distance);
                layoutShowState.setText("SHOW");
                ((MapActivity)getActivity()).setMissionContainerVisible(false);
            }
        });
    }

    private void showDownloadMission(String info) {
        ((MapActivity) Objects.requireNonNull(getActivity())).updateLogInfo(info);
    }

    protected boolean isEmpty(String value) {
        return null == value || value.isEmpty();
    }

    public void onDestroy() {
        super.onDestroy();
        if (null != missionManager) {
            missionManager.setRealTimeInfoListener(null);
        }
    }
    final Handler mHandler = new Handler(Looper.getMainLooper());
    private void toastView(final AutelError autelError){
        mHandler.post(() -> Toast.makeText(getActivity().getApplicationContext(), autelError.getDescription(), Toast.LENGTH_LONG).show());
    } private void toastView(final int log){
        mHandler.post(() -> Toast.makeText(getActivity().getApplicationContext(), getString(log), Toast.LENGTH_LONG).show());
    }
}
