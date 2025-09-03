package com.autel.sdksample.base.camera.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.autel.common.CallbackWithNoParam;
import com.autel.common.CallbackWithOneParam;
import com.autel.common.CallbackWithTwoParams;
import com.autel.common.camera.base.MediaMode;
import com.autel.common.camera.base.MediaStatus;
import com.autel.common.camera.base.SDCardState;
import com.autel.common.camera.base.WorkState;
import com.autel.common.error.AutelError;
import com.autel.sdk.camera.AutelBaseCamera;
import com.autel.sdksample.R;
import com.autel.sdksample.base.camera.CameraActivity;

import java.util.Objects;

public class CameraBaseFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();
    protected TextView log_output;

    AutelBaseCamera baseCamera;

    private Spinner mediaModeList;
    protected MediaMode mediaMode;

    protected void initClick(View view) {
        log_output = (TextView) view.findViewById(R.id.camera_log_output);
        baseCamera = ((CameraActivity) Objects.requireNonNull(getActivity())).getCurrentCamera();

        mediaModeList = (Spinner) view.findViewById(R.id.mediaModeList);
        mediaModeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mediaMode = MediaMode.SINGLE;
                        break;
                    case 1:
                        mediaMode = MediaMode.VIDEO;
                        break;
                    case 2:
                        mediaMode = MediaMode.TIMELAPSE;
                        break;
                    case 3:
                        mediaMode = MediaMode.BURST;
                        break;
                    case 4:
                        mediaMode = MediaMode.AEB;
                        break;
                    case 5:
                        mediaMode = MediaMode.MOTION_DELAY_SHOT;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        view.findViewById(R.id.setMediaMode).setOnClickListener(v -> baseCamera.setMediaMode(mediaMode, new CallbackWithNoParam() {
            @Override
            public void onFailure(AutelError error) {
                logOut("setMediaMode  description  " + error.getDescription());
            }

            @Override
            public void onSuccess() {
                logOut("setMediaMode state onSuccess");
            }
        }));

        view.findViewById(R.id.setCameraSDCardStateListener).setOnClickListener(v -> baseCamera.setSDCardStateListener(new CallbackWithOneParam<SDCardState>() {
            @Override
            public void onSuccess(SDCardState state) {
                logOut("setSDCardStateListener  state  " + state);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("setSDCardStateListener  description  " + error.getDescription());
            }
        }));
        view.findViewById(R.id.resetCameraSDCardStateListener).setOnClickListener(v -> baseCamera.setSDCardStateListener(null));
        view.findViewById(R.id.getMediaMode).setOnClickListener(v -> baseCamera.getMediaMode(new CallbackWithOneParam<MediaMode>() {
            @Override
            public void onSuccess(MediaMode data) {
                logOut("getMediaMode " + data);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("getMediaMode " + error.getDescription());
            }
        }));
        view.findViewById(R.id.getVersion).setOnClickListener(v -> baseCamera.getVersion(new CallbackWithOneParam<String>() {
            @Override
            public void onSuccess(String data) {
                logOut("getVersion " + data);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("getVersion " + error.getDescription());
            }
        }));
        view.findViewById(R.id.getTimeStamp).setOnClickListener(v -> {
        });
        view.findViewById(R.id.getProduct).setOnClickListener(v -> logOut("getProduct " + baseCamera.getProduct()));
        view.findViewById(R.id.getSdFreeSpace).setOnClickListener(v -> baseCamera.getSDCardFreeSpace(new CallbackWithOneParam<Long>() {
            @Override
            public void onSuccess(Long data) {
                logOut("getSDCardFreeSpace " + data);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("getSDCardFreeSpace " + error.getDescription());
            }
        }));
        view.findViewById(R.id.getSDCardStatus).setOnClickListener(v -> baseCamera.getSDCardState(new CallbackWithOneParam<SDCardState>() {
            @Override
            public void onSuccess(SDCardState data) {
                logOut("getSDCardState " + data);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("getSDCardState " + error.getDescription());
            }
        }));
        view.findViewById(R.id.getWorkStatus).setOnClickListener(v -> baseCamera.getWorkState(new CallbackWithOneParam<WorkState>() {
            @Override
            public void onSuccess(WorkState data) {
                logOut("getWorkState " + data);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("getWorkState " + error.getDescription());
            }
        }));
        view.findViewById(R.id.resetCamera).setOnClickListener(v -> baseCamera.resetDefaults(new CallbackWithNoParam() {
            @Override
            public void onFailure(AutelError error) {
                logOut("resetDefaults " + error.getDescription());
            }

            @Override
            public void onSuccess() {
                logOut("resetDefaults onSuccess");
            }
        }));
        view.findViewById(R.id.formatSDCard).setOnClickListener(v -> baseCamera.formatSDCard(new CallbackWithNoParam() {
            @Override
            public void onFailure(AutelError error) {
                logOut("formatSDCard " + error.getDescription());
            }

            @Override
            public void onSuccess() {
                logOut("formatSDCard onSuccess");
            }
        }));
        view.findViewById(R.id.stopTimelapse).setOnClickListener(v -> baseCamera.stopTakePhoto(new CallbackWithNoParam() {
            @Override
            public void onFailure(AutelError error) {
                logOut("stopTakePhoto " + error.getDescription());
            }

            @Override
            public void onSuccess() {
                logOut("stopTakePhoto onSuccess");
            }
        }));
        view.findViewById(R.id.takePhoto).setOnClickListener(v -> baseCamera.startTakePhoto(new CallbackWithNoParam() {
            @Override
            public void onFailure(AutelError error) {
                logOut("startTakePhoto " + error.getDescription());
            }

            @Override
            public void onSuccess() {
                logOut("startTakePhoto onSuccess");
            }
        }));
        view.findViewById(R.id.stopRecordVideo).setOnClickListener(v -> baseCamera.stopRecordVideo(new CallbackWithNoParam() {
            @Override
            public void onFailure(AutelError error) {
                logOut("stopRecordVideo " + error.getDescription());
            }

            @Override
            public void onSuccess() {
                logOut("stopRecordVideo onSuccess");
            }
        }));
        view.findViewById(R.id.startRecordVideo).setOnClickListener(v -> baseCamera.startRecordVideo(new CallbackWithNoParam() {
            @Override
            public void onFailure(AutelError error) {
                logOut("startRecordVideo " + error.getDescription());
            }

            @Override
            public void onSuccess() {
                logOut("startRecordVideo onSuccess");
            }
        }));
        view.findViewById(R.id.resetCameraMediaStateListener).setOnClickListener(v -> baseCamera.setMediaStateListener(null));
        view.findViewById(R.id.setCameraMediaStateListener).setOnClickListener(v -> baseCamera.setMediaStateListener(new CallbackWithTwoParams<MediaStatus, String>() {
            @Override
            public void onSuccess(MediaStatus state, String data) {
                logOut("setMediaStateListener state " + state+" data "+data);
            }

            @Override
            public void onFailure(AutelError error) {
                logOut("setMediaStateListener error " + error.getDescription());
            }
        }));
    }

    protected void logOut(String log) {
        CameraActivity activity = ((CameraActivity) getActivity());
        if (null != activity) {
            activity.logOut(log);
        }

    }

    protected boolean isEmpty(String value) {
        return null == value || value.isEmpty();
    }
}
