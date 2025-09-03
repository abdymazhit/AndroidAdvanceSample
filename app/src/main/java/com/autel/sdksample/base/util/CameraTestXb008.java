package com.autel.sdksample.base.util;


public class CameraTestXb008 {
//    private final static String TAG = "FCTest";
//
//    public static void setCameraSDCardStateListener(final Handler handler) {
//        if (AModuleCamera.cameraManager().getParameterRangeManager().getError() != null) {
//            logOut(handler, "supportAspectRatio  error: " + AModuleCamera.cameraManager().getParameterRangeManager().getError().getDescription());
//        } else {
//            if(AModuleCamera.cameraManager().getParameterRangeManager().getResult().getCurrentProduct() == CameraProduct.R12){
//                AModuleCamera.cameraManager().getXb004().setSDCardStateListener(new CallbackWithOneParam<SDCardState>() {
//                    @Override
//                    public void onSuccess(SDCardState state) {
//                        logOut(handler, "setSDCardStateListener  state  " + state);
//                    }
//
//                    @Override
//                    public void onFailure(AutelError error) {
//                        logOut(handler, "setSDCardStateListener  description  " + error.getDescription());
//                    }
//                });
//            } else if(AModuleCamera.cameraManager().getParameterRangeManager().getResult().getCurrentProduct() == CameraProduct.XB008){
//                AModuleCamera.cameraManager().getXb008().setSDCardStateListener(new CallbackWithOneParam<SDCardState>() {
//                    @Override
//                    public void onSuccess(SDCardState state) {
//                        logOut(handler, "setSDCardStateListener  state  " + state);
//                    }
//
//                    @Override
//                    public void onFailure(AutelError error) {
//                        logOut(handler, "setSDCardStateListener  description  " + error.getDescription());
//                    }
//                });
//            }else{
//                logOut(handler, "setSDCardStateListener  description  camera type is not clear");
//            }
//        }
//
//    }
//
//    public static void resetCameraSDCardStateListener(final Handler handler) {
//        if (AModuleCamera.cameraManager().getParameterRangeManager().getError() != null) {
//            logOut(handler, "supportAspectRatio  error: " + AModuleCamera.cameraManager().getParameterRangeManager().getError().getDescription());
//        } else {
//            if(AModuleCamera.cameraManager().getParameterRangeManager().getResult().getCurrentProduct() == CameraProduct.R12){
//                AModuleCamera.cameraManager().getXb004().setSDCardStateListener(null);
//            } else if(AModuleCamera.cameraManager().getParameterRangeManager().getResult().getCurrentProduct() == CameraProduct.XB008){
//                AModuleCamera.cameraManager().getXb008().setSDCardStateListener(null);
//            }else{
//                logOut(handler, "setSDCardStateListener  description  camera type is not clear");
//            }
//        }
//    }
//
//    public static void setCameraAutoExposureState(final CameraAutoExposureLockState lockState, final Handler handler) {
//        if (AModuleCamera.cameraManager().getParameterRangeManager().getError() != null) {
//            logOut(handler, "setCameraAutoExposureState  error: " + AModuleCamera.cameraManager().getParameterRangeManager().getError().getDescription());
//        } else {
//            if(AModuleCamera.cameraManager().getParameterRangeManager().getResult().getCurrentProduct() == CameraProduct.R12){
//                AModuleCamera.cameraManager().getXb004().setAutoExposureLockState(lockState, new CallbackWithNoParam() {
//                    @Override
//                    public void onFailure(AutelError error) {
//                        logOut(handler, "setAutoExposureLockState  description  " + error.getDescription());
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        logOut(handler, "setAutoExposureLockState  lockState  " + lockState + " onSuccess ");
//                    }
//                });
//            } else if(AModuleCamera.cameraManager().getParameterRangeManager().getResult().getCurrentProduct() == CameraProduct.XB008){
//                AModuleCamera.cameraManager().getXb008().setAutoExposureLockState(lockState, new CallbackWithNoParam() {
//                    @Override
//                    public void onFailure(AutelError error) {
//                        logOut(handler, "setAutoExposureLockState  description  " + error.getDescription());
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        logOut(handler, "setAutoExposureLockState  lockState  " + lockState + " state " );
//                    }
//                });
//            }else{
//                logOut(handler, "setAutoExposureLockState  description  camera type is not clear");
//            }
//        }
//    }
//
//    public static void getCameraAutoExposureState(final Handler handler) {
//        if (AModuleCamera.cameraManager().getParameterRangeManager().getError() != null) {
//            logOut(handler, "setCameraAutoExposureState  error: " + AModuleCamera.cameraManager().getParameterRangeManager().getError().getDescription());
//        } else {
//            if(AModuleCamera.cameraManager().getParameterRangeManager().getResult().getCurrentProduct() == CameraProduct.R12){
//                AModuleCamera.cameraManager().getXb004().getAutoExposureLockState(new CallbackWithOneParam<CameraAutoExposureLockState>() {
//                    @Override
//                    public void onFailure(AutelError error) {
//                        logOut(handler, "getAutoExposureLockState  description  " + error.getDescription());
//                    }
//
//                    @Override
//                    public void onSuccess(CameraAutoExposureLockState data) {
//                        logOut(handler, "getAutoExposureLockState  " + data);
//                    }
//                });
//            } else if(AModuleCamera.cameraManager().getParameterRangeManager().getResult().getCurrentProduct() == CameraProduct.XB008){
//                AModuleCamera.cameraManager().getXb008().getAutoExposureLockState(new CallbackWithOneParam<CameraAutoExposureLockState>() {
//                    @Override
//                    public void onFailure(AutelError error) {
//                        logOut(handler, "getAutoExposureLockState  description  " + error.getDescription());
//                    }
//
//                    @Override
//                    public void onSuccess(CameraAutoExposureLockState data) {
//                        logOut(handler, "getAutoExposureLockState  " + data);
//                    }
//                });
//            }else{
//                logOut(handler, "getAutoExposureLockState  description  camera type is not clear");
//            }
//        }
//    }
//
//    public static void getPhotoExposureMode(final Handler handler) {
//        if (AModuleCamera.cameraManager().getParameterRangeManager().getError() != null) {
//            logOut(handler, "getExposureMode  error: " + AModuleCamera.cameraManager().getParameterRangeManager().getError().getDescription());
//        } else {
//            if(AModuleCamera.cameraManager().getParameterRangeManager().getResult().getCurrentProduct() == CameraProduct.R12){
//                AModuleCamera.cameraManager().getXb004().getExposureMode(new CallbackWithOneParam<CameraExposureMode>() {
//                    @Override
//                    public void onFailure(AutelError error) {
//                        logOut(handler, "getExposureMode  description  " + error.getDescription());
//                    }
//
//                    @Override
//                    public void onSuccess(CameraExposureMode data) {
//                        logOut(handler, "getExposureMode  " + data);
//                    }
//                });
//            } else if(AModuleCamera.cameraManager().getParameterRangeManager().getResult().getCurrentProduct() == CameraProduct.XB008){
//                AModuleCamera.cameraManager().getXb008().getExposureMode(new CallbackWithOneParam<CameraExposureMode>() {
//                    @Override
//                    public void onFailure(AutelError error) {
//                        logOut(handler, "getExposureMode  description  " + error.getDescription());
//                    }
//
//                    @Override
//                    public void onSuccess(CameraExposureMode data) {
//                        logOut(handler, "getExposureMode  " + data);
//                    }
//                });
//            }else{
//                logOut(handler, "getExposureMode  description  camera type is not clear");
//            }
//        }
//    }
//
//    public static void setCameraGear(final CameraExposureMode gear, final Handler handler) {
//        if (AModuleCamera.cameraManager().getParameterRangeManager().getError() != null) {
//            logOut(handler, "getExposureMode  error: " + AModuleCamera.cameraManager().getParameterRangeManager().getError().getDescription());
//        } else {
//            if(AModuleCamera.cameraManager().getParameterRangeManager().getResult().getCurrentProduct() == CameraProduct.R12){
////                AModuleCamera.cameraManager().getXb004().setExposureMode(gear, new CallbackWithNoParam() {
////                    @Override
////                    public void onFailure(AutelError error) {
////                        logOut(handler, "setExposureMode  description  " + error.getDescription());
////                    }
////
////                    @Override
////                    public void onSuccess() {
////                        logOut(handler, "setExposureMode  " + gear + "  state  onSuccess" );
////                    }
////                });
//            } else if(AModuleCamera.cameraManager().getParameterRangeManager().getResult().getCurrentProduct() == CameraProduct.XB008){
//                AModuleCamera.cameraManager().getXb008().setExposureMode(gear, new CallbackWithNoParam() {
//                    @Override
//                    public void onFailure(AutelError error) {
//                        logOut(handler, "setExposureMode  description  " + error.getDescription());
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        logOut(handler, "setExposureMode  " + gear + "  state  onSuccess" );
//                    }
//                });
//            }else{
//                logOut(handler, "setExposureMode  description  camera type is not clear");
//            }
//        }
//
//    }
////    public static void getCameraStatus(final Handler handler) {
////        AModuleCamera.camera().getStatusData(new CallbackWithOneParam<CameraStatus>() {
////            @Override
////            public void onFailure(AutelError error) {
////                logOut(handler, "getStatusData " + error.getDescription());
////            }
////
////            @Override
////            public void onSuccess(CameraStatus data) {
////                logOut(handler, "getStatusData " + data);
////            }
////        });
////    }
////
////    public static void getCameraSetting(final Handler handler) {
////        AModuleCamera.camera().getSetting(new CallbackWithOneParam<CameraSetting>() {
////            @Override
////            public void onFailure(AutelError error) {
////                logOut(handler, "getSetting " + error.getDescription());
////            }
////
////            @Override
////            public void onSuccess(CameraSetting data) {
////                logOut(handler, "getSetting " + data);
////            }
////        });
////    }
}
