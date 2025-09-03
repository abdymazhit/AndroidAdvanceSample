package com.autel.sdksample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.autel.AutelNet2.aircraft.battery.controller.BatteryRequestManager2
import com.autel.AutelNet2.aircraft.engine.BatteryInfoCmdParams
import com.autel.AutelNet2.aircraft.firmware.FirmwareManager
import com.autel.AutelNet2.aircraft.firmware.bean.FirmwareDeviceInfo
import com.autel.AutelNet2.aircraft.flycontroller.FlyControllerManager2
import com.autel.AutelNet2.aircraft.flycontroller.engine.AttitudeInfoInternal
import com.autel.AutelNet2.aircraft.flycontroller.engine.CommandInfoInternal
import com.autel.AutelNet2.aircraft.flycontroller.engine.ImuStateInfoImpl
import com.autel.AutelNet2.aircraft.flycontroller.engine.LocalCoordinateInfoImpl
import com.autel.AutelNet2.aircraft.flycontroller.parser.GPSInfoInternal
import com.autel.AutelNet2.aircraft.gimbal.controller.GimbalManager2
import com.autel.AutelNet2.aircraft.gimbal.engine.GimbalAngle
import com.autel.AutelNet2.aircraft.gimbal.engine.GimbalAngleRangeImpl
import com.autel.AutelNet2.aircraft.gimbal.engine.GimbalCmdInfo
import com.autel.AutelNet2.aircraft.mission.controller.MissionCommonManager2
import com.autel.AutelNet2.aircraft.mission.controller.WayPointMissionManager2
import com.autel.AutelNet2.aircraft.mission.engine.*
import com.autel.AutelNet2.aircraft.visual.VisualModelManager
import com.autel.AutelNet2.aircraft.visual.obstacleAvoidance.entity.ViewpointInfo
import com.autel.AutelNet2.aircraft.visual.obstacleAvoidance.entity.VisualHeartInfo
import com.autel.AutelNet2.aircraft.visual.obstacleAvoidance.entity.VisualSettingAckInfo
import com.autel.AutelNet2.dsp.controller.DspRFManager2
import com.autel.bean.dsp.*
import com.autel.camera.protocol.protocol20.interfaces.Xb015.CameraXb015
import com.autel.camera.protocol.protocol20.request.CameraFactory
import com.autel.camera.protocol.protocol20.xb015.Xb015
import com.autel.common.CallbackWithNoParam
import com.autel.common.CallbackWithOneParam
import com.autel.common.dsp.DeviceType
import com.autel.common.dsp.evo.BandMode
import com.autel.common.dsp.evo.Bandwidth
import com.autel.common.dsp.evo.TransferMode
import com.autel.common.error.AutelError
import com.autel.common.flycontroller.visual.VisualSettingSwitchblade
import com.autel.common.gimbal.GimbalAxisType
import com.autel.common.gimbal.evo.GimbalAngleData
import com.autel.common.gimbal.evo.GimbalAngleSpeed
import com.autel.common.mission.AutelCoordinate3D
import com.autel.common.mission.MissionType
import com.autel.common.mission.OrbitFinishedAction
import com.autel.common.mission.evo.*
import com.autel.common.mission.xstar.WaypointFinishedAction
import com.autel.internal.mission.evo.MissionSerializeUtil
import com.autel.internal.sdk.mission.VideoType
import java.util.*

class TestActivity : AppCompatActivity() {
    private val TAG: String = "TestActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initView()
        setListener()
    }


    fun setResetFactory() {
        var deviceType: DeviceType = DeviceType.UNKNOWN
        when (index) {
            0 -> {
                deviceType = DeviceType.ALL
            }
            1 -> {
                deviceType = DeviceType.DEV_UAV
            }
            2 -> {
                deviceType = DeviceType.DEV_DSP
            }
            3 -> {
                deviceType = DeviceType.DEV_DSP_RC
            }
            4 -> {
                deviceType = DeviceType.DEV_RC_PLAYER
            }
            5 -> {
                deviceType = DeviceType.DEV_ESC_PITCH
            }
            6 -> {
                deviceType = DeviceType.DEV_ESC_ROLL
            }
            7 -> {
                deviceType = DeviceType.DEV_ESC_YAW
            }
            8 -> {
                deviceType = DeviceType.DEV_BATTERY
            }
            9 -> {
                deviceType = DeviceType.DEV_GIMBAL
            }
            10 -> {
                deviceType = DeviceType.DEV_RC_ANDROID
            }
            11 -> {
                deviceType = DeviceType.DEV_RC
            }
            12 -> {
                deviceType = DeviceType.DEV_MOVIDIUS_1
            }
            13 -> {
                deviceType = DeviceType.DEV_CAMERA
            }
        }
        DspRFManager2.getInstance().resetFactory(deviceType, object : CallbackWithOneParam<Boolean> {
            override fun onSuccess(data: Boolean?) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(this@TestActivity, "onSuccess $data", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(error: AutelError?) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    var index = 0
    private fun setListener() {
        (findViewById<Button>(R.id.resetGimbalAngle)!!).setOnClickListener {
            GimbalManager2.getInstance().resetGimbalAngle(GimbalAxisType.PITCH, object : CallbackWithOneParam<GimbalCmdInfo> {
                override fun onFailure(error: AutelError?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess(data: GimbalCmdInfo?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onsuccess " + data?.ack + " result:" + data?.data!![0], Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        (findViewById<TextView>(R.id.setGimbalAngleRange)!!).setOnClickListener {
            GimbalManager2.getInstance().queryGimbalAngleRange(object : CallbackWithOneParam<GimbalAngleRangeImpl> {
                override fun onFailure(error: AutelError?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess(data: GimbalAngleRangeImpl?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onsuccess " + data?.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            })
            VisualModelManager.instance().setVisualHeartListener(TAG, object : CallbackWithOneParam<VisualHeartInfo> {
                override fun onFailure(error: AutelError?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess(data: VisualHeartInfo?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onSuccess " + data?.visionMode, Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
        (findViewById<TextView>(R.id.setGimbalAngleMax)!!).setOnClickListener {
            GimbalManager2.getInstance().setGimbalAngleMax(GimbalAxisType.PITCH, object : CallbackWithOneParam<GimbalCmdInfo> {
                override fun onFailure(error: AutelError?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess(data: GimbalCmdInfo?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onsuccess " + data?.ack, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        (findViewById<TextView>(R.id.setGimbalAngleMin)!!).setOnClickListener {
            GimbalManager2.getInstance().setGimbalAngleMin(GimbalAxisType.PITCH, object : CallbackWithOneParam<GimbalCmdInfo> {
                override fun onFailure(error: AutelError?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess(data: GimbalCmdInfo?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onsuccess " + data?.ack, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }



        (findViewById<Button>(R.id.queryBatteryInfo)!!).setOnClickListener {
            BatteryRequestManager2.getInstance().queryBatteryInfo(object : CallbackWithOneParam<BatteryInfoCmdParams> {
                override fun onFailure(error: AutelError?) {
                    Log.d(TAG, "queryBatteryInfo recv onFailure ")
                }

                override fun onSuccess(data: BatteryInfoCmdParams?) {
                    Log.d(TAG, "queryBatteryInfo recv data->$data")
                }
            })
        }
        (findViewById<Button>(R.id.setBattery)!!).setOnClickListener {
            BatteryRequestManager2.getInstance().setBatteryDisCharge(2, object : CallbackWithOneParam<CommandInfoInternal> {
                override fun onFailure(error: AutelError?) {
                    Log.d(TAG, "setBatteryDischargeDay recv onFailure ")
                }

                override fun onSuccess(data: CommandInfoInternal?) {
                    Log.d(TAG, "setBatteryDischargeDay recv data-> " + data?.isSuccess)
                }
            })
        }
        (findViewById<Button>(R.id.queryBatteryHistory)!!).setOnClickListener {
            BatteryRequestManager2.getInstance().queryBatteryHistory(object : CallbackWithOneParam<IntArray> {
                override fun onSuccess(data: IntArray) {
                    Log.d(TAG, "queryBatteryHistory recv data->$data")
                }

                override fun onFailure(error: AutelError) {
                    Log.d(TAG, "queryBatteryHistory recv onFailure ")
                }
            })
        }
        (findViewById<Button>(R.id.setLowBatteryWarning)!!).setOnClickListener {
            BatteryRequestManager2.getInstance().setLowBatteryWarning(0.25f, object : CallbackWithOneParam<Float> {
                override fun onFailure(error: AutelError?) {
                    Log.d(TAG, "queryBatteryHistory recv onFailure ")
                }

                override fun onSuccess(data: Float?) {
                    Log.d(TAG, "queryBatteryHistory recv data->$data")
                }
            })
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val spinner = findViewById<Spinner>(R.id.Spinner)
            if (spinner != null) {
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        if (view != null) {
                            // 在这里处理您的逻辑
                            index = position
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }
        }
    }


    private fun initView() {
        (findViewById<TextView>(R.id.queryVisual)!!).setOnClickListener {
            VisualModelManager.instance().setViewpointTargetAreaListener(TAG, object : CallbackWithOneParam<ViewpointInfo> {
                override fun onSuccess(data: ViewpointInfo?) {
                    if (data != null) {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this@TestActivity, "onSuccess $data", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(error: AutelError) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        (findViewById<Button>(R.id.setviewpointcoor)!!).setOnClickListener {
            VisualModelManager.instance().setVisualViewPointCoord(300, 240, object : CallbackWithOneParam<VisualSettingAckInfo> {
                override fun onSuccess(data: VisualSettingAckInfo?) {
                    if (data != null) {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this@TestActivity, "onSuccess $data", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(error: AutelError) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        (findViewById<Button>(R.id.setResolutionAngle)!!).setOnClickListener {
            VisualModelManager.instance().setVisualResolutionAngle(495, 500, object : CallbackWithOneParam<VisualSettingAckInfo> {
                override fun onSuccess(data: VisualSettingAckInfo?) {
                    if (data != null) {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this@TestActivity, "onSuccess $data", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(error: AutelError) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        (findViewById<TextView>(R.id.setVisual)!!).setOnClickListener {
            VisualModelManager.instance().setVisualSettingSwitchblade(VisualSettingSwitchblade.RADAR_MAP.cmdValue, true, object : CallbackWithOneParam<VisualSettingAckInfo> {
                override fun onSuccess(data: VisualSettingAckInfo?) {
                    if (data?.ack == 0) {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this@TestActivity, "onSuccess ", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(error: AutelError) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                    }
                }
            })
//            VisualModelManager.instance().setVisualSettingSwitchblade(VisualSettingSwitchblade.AVOIDANCE_SYSTEM.cmdValue, true, object : CallbackWithOneParam<VisualSettingAckInfo> {
//                override fun onSuccess(data: VisualSettingAckInfo?) {
//                    if (data?.ack == 0) {
//                        Handler(Looper.getMainLooper()).post {
//                            Toast.makeText(this@TestActivity, "onSuccess ", Toast.LENGTH_LONG).show()
//                        }
//                    } else {
//                        Handler(Looper.getMainLooper()).post {
//                            Toast.makeText(this@TestActivity, "onFailure " , Toast.LENGTH_LONG).show()
//                        }
//                    }
//                }
//
//                override fun onFailure(error: AutelError) {
//                    Handler(Looper.getMainLooper()).post {
//                        Toast.makeText(this@TestActivity, "onFailure " , Toast.LENGTH_LONG).show()
//                    }
//                }
//            })
//            VisualModelManager.instance().setVisualSettingSwitchblade(VisualSettingSwitchblade.VISUAL_LOCATION.cmdValue, true, object : CallbackWithOneParam<VisualSettingAckInfo> {
//                override fun onSuccess(data: VisualSettingAckInfo?) {
//                    if (data?.ack == 0) {
//                        Handler(Looper.getMainLooper()).post {
//                            Toast.makeText(this@TestActivity, "onSuccess ", Toast.LENGTH_LONG).show()
//                        }
//                    } else {
//                        Handler(Looper.getMainLooper()).post {
//                            Toast.makeText(this@TestActivity, "onFailure " , Toast.LENGTH_LONG).show()
//                        }
//                    }
//                }
//
//                override fun onFailure(error: AutelError) {
//                    Handler(Looper.getMainLooper()).post {
//                        Toast.makeText(this@TestActivity, "onFailure " , Toast.LENGTH_LONG).show()
//                    }
//                }
//            })
        }

        (findViewById<TextView>(R.id.getAircraftVersion)!!).setOnClickListener {
            FirmwareManager.instance().getDeviceFirmwareInfo(object : CallbackWithOneParam<List<FirmwareDeviceInfo.VersionBean>> {
                override fun onFailure(error: AutelError?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess(data: List<FirmwareDeviceInfo.VersionBean>?) {
                    Log.d(TAG, "data:" + data?.toString())
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "onSuccess " + data?.size, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        var status = false
        (findViewById<TextView>(R.id.getRemoteVersion)!!).setOnClickListener {
            when (status) {
                true -> {
                    DspRFManager2.getInstance().setVideoLinkStatus(true, object : CallbackWithOneParam<Boolean> {
                        override fun onFailure(error: AutelError?) {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(this@TestActivity, "setVideoLinkStatus onFailure ", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onSuccess(data: Boolean?) {
                            status = false
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(this@TestActivity, "setVideoLinkStatus $data", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
                false -> {
                    DspRFManager2.getInstance().setVideoLinkStatus(false, object : CallbackWithOneParam<Boolean> {
                        override fun onFailure(error: AutelError?) {
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(this@TestActivity, "setVideoLinkStatus onFailure ", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onSuccess(data: Boolean?) {
                            status = true
                            Handler(Looper.getMainLooper()).post {
                                Toast.makeText(this@TestActivity, "setVideoLinkStatus $data", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
            }

        }

        (findViewById<TextView>(R.id.textView2)!!).setOnClickListener {
            GimbalManager2.getInstance().addGimbalAngleListener("addGimbalAngleListener", object : CallbackWithOneParam<GimbalAngle> {
                override fun onSuccess(data: GimbalAngle?) {
                    Log.d(TAG, "data:" + data?.toString())
                }

                override fun onFailure(error: AutelError?) {

                }
            })
        }
        (findViewById<TextView>(R.id.textView3)!!).setOnClickListener {
            val angle = GimbalAngleData()
            angle.yaw = 100f
            angle.roll = 120f
            angle.pitch = 120f
            Log.d("Gimbal", "send msg json:")
            GimbalManager2.getInstance().setGimbalAngle(angle)
        }
        (findViewById<TextView>(R.id.textView4)!!).setOnClickListener {
            val speed = GimbalAngleSpeed()
            speed.yawSpeed = 100
            speed.rollSpeed = 120
            speed.pitchSpeed = 120f
            GimbalManager2.getInstance().setGimbalAngleSpeed(speed)
        }
        (findViewById<TextView>(R.id.textView5)!!).setOnClickListener {
            FlyControllerManager2.getInstance().addLocalCoordinateInfoListener("setAltitudeAndSpeedInfoListener", object : CallbackWithOneParam<LocalCoordinateInfoImpl> {
                override fun onSuccess(data: LocalCoordinateInfoImpl) {
                    Log.d(TAG, "addLocalCoordinateInfoListener data $data")
                }

                override fun onFailure(error: AutelError) {
                    Log.d(TAG, "setFlyControllerListener " + error.description)
                }
            })
        }
        (findViewById<TextView>(R.id.textView6)!!).setOnClickListener {
            FlyControllerManager2.getInstance().addGPSInfoListener("addGPSInfoListener", object : CallbackWithOneParam<GPSInfoInternal> {
                override fun onSuccess(data: GPSInfoInternal) {
                    Log.d(TAG, "addGPSInfoListener data $data")
                }

                override fun onFailure(error: AutelError) {
                    Log.d(TAG, "setFlyControllerListener " + error.description)
                }
            })
        }
        (findViewById<TextView>(R.id.textView7)!!).setOnClickListener {
            FlyControllerManager2.getInstance().addAttitudeInfoListener("addAttitudeInfoListener", object : CallbackWithOneParam<AttitudeInfoInternal> {
                override fun onSuccess(data: AttitudeInfoInternal) {
                    Log.d(TAG, "addAttitudeInfoListener data $data")
                }

                override fun onFailure(error: AutelError) {
                    Log.d(TAG, "addAttitudeInfoListener " + error.description)
                }
            })
        }
        (findViewById<TextView>(R.id.textView8)!!).setOnClickListener {
            FlyControllerManager2.getInstance().addImuStatusListener("addImuStatusListener", object : CallbackWithOneParam<ImuStateInfoImpl> {
                override fun onSuccess(data: ImuStateInfoImpl) {
                    Log.d(TAG, "addImuStatusListener data $data")
                }

                override fun onFailure(error: AutelError) {
                    Log.d(TAG, "setFlyControllerListener " + error.description)
                }
            })
        }
        (findViewById<TextView>(R.id.textView9)!!).setOnClickListener {
            FlyControllerManager2.getInstance().setMaxRange(100.0f, object : CallbackWithOneParam<Float> {
                override fun onSuccess(data: Float?) {
                    Log.d(TAG, "setMaxRange data $data")
                }

                override fun onFailure(error: AutelError) {
                    Log.d(TAG, "setMaxRange " + error.description)
                }
            })
        }
        (findViewById<TextView>(R.id.textView10)!!).setOnClickListener {
            MissionCommonManager2.getInstance().addMissionCurrentInfoListener("addMissionCurrentInfoListener", object : CallbackWithOneParam<CurrentMission> {
                override fun onSuccess(data: CurrentMission?) {
                    Log.d(TAG, "addMissionCurrentInfoListener data $data")
                }

                override fun onFailure(error: AutelError) {
                    Log.d(TAG, "addMissionCurrentInfoListener " + error.description)
                }
            })
        }
        (findViewById<TextView>(R.id.textView11)!!).setOnClickListener {
            MissionCommonManager2.getInstance().addMissionFollowMeListener("addMissionFollowMeListener", object : CallbackWithOneParam<FollowMeInfoInternal> {
                override fun onSuccess(data: FollowMeInfoInternal?) {
                    Log.d(TAG, "addMissionFollowMeListener data $data")
                }

                override fun onFailure(error: AutelError) {
                    Log.d(TAG, "addMissionCurrentInfoListener " + error.description)
                }
            })
        }
        (findViewById<TextView>(R.id.textView12)!!).setOnClickListener {
            MissionCommonManager2.getInstance().addMissionHotPointListener("addMissionHotPointListener", object : CallbackWithOneParam<HotPointInfoInternal> {
                override fun onSuccess(data: HotPointInfoInternal?) {
                    Log.d(TAG, "addMissionHotPointListener data $data")
                }

                override fun onFailure(error: AutelError) {
                    Log.d(TAG, "addMissionHotPointListener " + error.description)
                }
            })
        }

        (findViewById<TextView>(R.id.textView13)!!).setOnClickListener {
            DspRFManager2.getInstance().setVideoRateInfoListener(object : CallbackWithOneParam<VideoRateInfoImpl> {
                override fun onSuccess(data: VideoRateInfoImpl?) {
                    Log.d(TAG, "setVideoRateInfoListener data $data")
                }

                override fun onFailure(error: AutelError) {
                    Log.d(TAG, "setVideoRateInfoListener " + error.description)
                }
            })
        }
        (findViewById<TextView>(R.id.textView14)!!).setOnClickListener {
            DspRFManager2.getInstance().setReportBertInfoListener(object : CallbackWithOneParam<ReportBertInfo> {
                override fun onSuccess(data: ReportBertInfo?) {
                    Log.d(TAG, "setReportBertInfoListener data $data")
                }

                override fun onFailure(error: AutelError) {
                    Log.d(TAG, "setReportBertInfoListener " + error.description)
                }
            })
        }
        (findViewById<TextView>(R.id.textView15)!!).setOnClickListener {
            DspRFManager2.getInstance().addSignalStregthListener("addSignalStrengthListener", object : CallbackWithOneParam<SignalStrengthReport> {
                override fun onSuccess(data: SignalStrengthReport?) {
                    Log.d(TAG, "setReportBertInfoListener data $data")
                }

                override fun onFailure(error: AutelError) {
                    Log.d(TAG, "setReportBertInfoListener " + error.description)
                }
            })
        }
        val request: CameraXb015? = CameraFactory.getCameraProduct(Xb015::class.java)
        var index = 0
        (findViewById<TextView>(R.id.ll0)!!).setOnClickListener {
            val flag = (index % 2 == 0)
            index++
            request?.setProductSubtitleSNEnable(flag, object : CallbackWithNoParam {
                override fun onFailure(error: AutelError?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "setSubtitleEnable onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess() {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "setSubtitleEnable onSuccess $flag", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        (findViewById<LinearLayout>(R.id.ll1)!!).setOnClickListener {
            val value: EditText = findViewById(R.id.editText1)

            FlyControllerManager2.getInstance().setFlightControllerDirect(Integer.parseInt(value.text.toString()), object : CallbackWithOneParam<Boolean> {
                override fun onSuccess(data: Boolean?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "setSubtitleEnable onSuccess $data", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(error: AutelError?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "setSubtitleEnable onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
        (findViewById<TextView>(R.id.textView16)!!).setOnClickListener {
            val waypointMission = EvoWaypointMission()
            waypointMission.avoidanceMode = ObstacleAvoidanceMode.CLIMB_FIRST
            waypointMission.obstacleAvoidanceTimeout = 2
            waypointMission.remoteControlLostSignalAction = RemoteControlLostSignalAction.CONTINUE

            val waypoints = ArrayList<EvoWaypoint>()
            val coordinate3D = AutelCoordinate3D(22.5959987, 113.9977445, 60.0)

            var waypoint = EvoWaypoint(coordinate3D)
            waypoint.bezierMode = WaypointBezierMode.CLOCKWISE
            waypoint.cameraPitch = 20
            waypoint.cameraYaw = 30
            waypoint.customHeadingDirection = 2
            waypoint.focusAltitude = 80.0
            waypoint.focusLatitude = 22.5959987
            waypoint.focusLongitude = 113.9977445
            waypoint.headingMode = WaypointHeadingMode.CUSTOM_DIRECTION
            waypoint.isAltitudePriority = true
            waypoint.speed = 5
            waypoints.add(waypoint)

            waypoint = EvoWaypoint(coordinate3D)
            waypoint.bezierMode = WaypointBezierMode.CLOCKWISE
            waypoint.cameraPitch = 20
            waypoint.cameraYaw = 30
            waypoint.customHeadingDirection = 2
            waypoint.focusAltitude = 80.0
            waypoint.wSpeed = 5.5f
            waypoint.focusLatitude = 22.5959987
            waypoint.focusLongitude = 113.9977445
            waypoint.headingMode = WaypointHeadingMode.CUSTOM_DIRECTION
            waypoint.isAltitudePriority = true
            waypoint.speed = 5
            waypoints.add(waypoint)

            waypointMission.wpList = waypoints

            val settingInfoInternal = MissionSerializeUtil.getMissionAllInternal(waypointMission)
            MissionCommonManager2.getInstance().uploadMission(settingInfoInternal, object : CallbackWithOneParam<Boolean> {
                override fun onSuccess(data: Boolean) {
                    Log.d(TAG, "uploadMission onSuccess ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "uploadMission onSuccess ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(error: AutelError) {
                    Handler(Looper.getMainLooper()).post {
                        Log.d(TAG, "uploadMission onFailure " + error.description)
                    }
                }
            })
        }

        (findViewById<TextView>(R.id.textView17)!!).setOnClickListener {
            val wayParam = WaypointBean()
            wayParam.waypointId = 8
            wayParam.latitude = 4
            wayParam.longitude = 5
            wayParam.altitude = 10
//            wayParam.speed = 10
            wayParam.focusLatitude = 4
            wayParam.focusLongitude = 5
            wayParam.focusAltitude = 4
            wayParam.beizerParameter = 10
            wayParam.altitudePriorityMode = 1
            wayParam.headingMode = 3
            wayParam.userdefinedHeading = 100
            wayParam.cameraPitch = 89
            wayParam.cameraYaw = 123
            WayPointMissionManager2.getInstance().setWaypointInfo(wayParam)
        }
        (findViewById<TextView>(R.id.textView18)!!).setOnClickListener {
            val wayParam = WaypointActionInfo()
            wayParam.missionId = 100
            wayParam.waypointId = 8
            wayParam.actionTimeout = 1
            wayParam.actionType = 1
            wayParam.actionId = 1
            wayParam.actionParameters = listOf(100.9f, 2000.8f, 3000.2f, 800.0f, 1.0f, 0.0f, 0.0f, 0.0f)
            WayPointMissionManager2.getInstance().setWaypointActionInfo(wayParam)
        }
        (findViewById<TextView>(R.id.textView19)!!).setOnClickListener {
            val orbitMission = EvoOrbitMission.createMission()
            orbitMission.finishedAction = OrbitFinishedAction.HOVER
            orbitMission.latitude = 22.5959987
            orbitMission.longitude = 113.9977445
            orbitMission.mEntryDirection = OrbitEntryDirection.EAST
            orbitMission.mHeadingDirection = OrbitHeadingDirection.BACKWARD
            orbitMission.mRotateDirection = OrbitRotateDirection.Clockwise
            orbitMission.radius = 2
            orbitMission.oRadius = 12.2f
            orbitMission.speed = 3
            orbitMission.oSpeed = 3.5f
            orbitMission.remoteControlLostSignalAction = RemoteControlLostSignalAction.CONTINUE
            orbitMission.remainDegree = 33
            orbitMission.cycles = 3
            orbitMission.finishReturnHeight = 40
            val orbitInfo = MissionSerializeUtil.getOrbitInfo(orbitMission)
            val missionSingleInfo = MissionSerializeUtil.getMissionSingleInfo(orbitMission)
            val missionAllInternal = MissionAllInternal()
            missionAllInternal.fmuMissionInfo = missionSingleInfo
            missionAllInternal.orbitInfo = orbitInfo
            MissionCommonManager2.getInstance().uploadMission(missionAllInternal, object : CallbackWithOneParam<Boolean> {
                override fun onFailure(error: AutelError?) {
                    Log.d(TAG, "orbitMission onFailure ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "orbitMission onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess(data: Boolean?) {
                    Log.d(TAG, "orbitMission onSuccess ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "orbitMission onSuccess $data", Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
        (findViewById<TextView>(R.id.textView20)!!).setOnClickListener {
            val mission = OneShotVideoMission.createMission()
            mission.finishedAction = WaypointFinishedAction.HOVER
            mission.remoteControlLostSignalAction = RemoteControlLostSignalAction.CONTINUE
            mission.finishReturnHeight = 40
            mission.videoType = VideoType.TYPE_360_SHOOT
            mission.moveDistance = 100
            mission.moveSpeed = 5
            mission.rotateDirection = 35
            mission.rotateSpeed = 8
            mission.cycles = 4
            mission.missionType = MissionType.ONE_SHORT_VIDEO
            val missionAllInternal = MissionAllInternal()
            missionAllInternal.fmuMissionInfo = MissionSerializeUtil.getMissionSingleInfo(mission)
            missionAllInternal.oneShotVideo = MissionSerializeUtil.getOneShotVideo(mission)
            MissionCommonManager2.getInstance().uploadMission(missionAllInternal, object : CallbackWithOneParam<Boolean>{
                override fun onFailure(error: AutelError?) {
                    Log.d(TAG, "downloadOrbitInfo onFailure ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "uploadMission onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess(data: Boolean?) {
                    Log.d(TAG, "downloadOrbitInfo onSuccess ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "uploadMission onSuccess $data", Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
        (findViewById<TextView>(R.id.textView21)!!).setOnClickListener {
            val mission = TripodMission.createMission()
            mission.finishedAction = WaypointFinishedAction.HOVER
            mission.remoteControlLostSignalAction = RemoteControlLostSignalAction.CONTINUE
            mission.finishReturnHeight = 40
            mission.verticalSpeed = 100f
            mission.horizontalSpeed = 5f
            mission.rotateSpeed = 35f
            mission.missionId = 4
            mission.missionType = MissionType.TRIPOD
            val missionAllInternal = MissionAllInternal()
            missionAllInternal.fmuMissionInfo = MissionSerializeUtil.getMissionSingleInfo(mission)
            missionAllInternal.tripod = MissionSerializeUtil.getTripodInfo(mission)
            MissionCommonManager2.getInstance().uploadMission(missionAllInternal, object : CallbackWithOneParam<Boolean>{
                override fun onFailure(error: AutelError?) {
                    Log.d(TAG, "downloadOrbitInfo onFailure ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "uploadMission onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess(data: Boolean?) {
                    Log.d(TAG, "downloadOrbitInfo onSuccess ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "uploadMission onSuccess $data", Toast.LENGTH_LONG).show()
                    }
                }

            })

        }
        (findViewById<TextView>(R.id.textView22)!!).setOnClickListener {
            val mission = ImageStabilityMission.createMission()
            mission.finishedAction = WaypointFinishedAction.HOVER
            mission.remoteControlLostSignalAction = RemoteControlLostSignalAction.CONTINUE
            mission.finishReturnHeight = 40
            mission.missionId = 4
            mission.missionType = MissionType.IMAGE_STABILITY
            val missionAllInternal = MissionAllInternal()
            missionAllInternal.fmuMissionInfo = MissionSerializeUtil.getMissionSingleInfo(mission)
            MissionCommonManager2.getInstance().uploadMission(missionAllInternal, object : CallbackWithOneParam<Boolean>{
                override fun onFailure(error: AutelError?) {
                    Log.d(TAG, "downloadOrbitInfo onFailure ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "uploadMission onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess(data: Boolean?) {
                    Log.d(TAG, "downloadOrbitInfo onSuccess ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "uploadMission onSuccess $data", Toast.LENGTH_LONG).show()
                    }
                }

            })

        }
        (findViewById<TextView>(R.id.textView40)!!).setOnClickListener {
            MissionOperateInfoInternal()
            MissionCommonManager2.getInstance().downloadAllMissionInfo(object : CallbackWithOneParam<MissionAllInternal> {
                override fun onFailure(error: AutelError?) {
                    Log.d(TAG, "downloadOrbitInfo onFailure ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "downloadOrbitInfo onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess(data: MissionAllInternal?) {
                    Log.d(TAG, "downloadOrbitInfo onSuccess ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "downloadOrbitInfo onSuccess ", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        (findViewById<TextView>(R.id.textView25)!!).setOnClickListener {
            MissionOperateInfoInternal()
            MissionCommonManager2.getInstance().downloadAllMissionInfo(object : CallbackWithOneParam<MissionAllInternal> {
                override fun onFailure(error: AutelError?) {
                    Log.d(TAG, "downloadAllMissionInfo onFailure ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "downloadAllMissionInfo onFailure ", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onSuccess(data: MissionAllInternal?) {
                    Log.d(TAG, "downloadAllMissionInfo onSuccess ")
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@TestActivity, "downloadAllMissionInfo onSuccess ", Toast.LENGTH_LONG).show()
                    }
                }
            })


            (findViewById<TextView>(R.id.textView23)!!).setOnClickListener {
                val mission = TripodMission.createMission()
                mission.finishedAction = WaypointFinishedAction.HOVER
                mission.remoteControlLostSignalAction = RemoteControlLostSignalAction.CONTINUE
                mission.finishReturnHeight = 40
                mission.verticalSpeed = 100f
                mission.horizontalSpeed = 5f
                mission.rotateSpeed = 35f
                mission.missionId = 4
                mission.missionType = MissionType.TRIPOD
                val missionAllInternal = MissionAllInternal()
                missionAllInternal.fmuMissionInfo = MissionSerializeUtil.getMissionSingleInfo(mission)
                missionAllInternal.tripod = MissionSerializeUtil.getTripodInfo(mission)
                MissionCommonManager2.getInstance().setTripodSetting(MissionSerializeUtil.getTripodInfo(mission), object : CallbackWithOneParam<Boolean>{
                    override fun onFailure(error: AutelError?) {
                        Log.d(TAG, "downloadOrbitInfo onFailure ")
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this@TestActivity, "uploadMission onFailure ", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onSuccess(data: Boolean?) {
                        Log.d(TAG, "downloadOrbitInfo onSuccess ")
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this@TestActivity, "uploadMission onSuccess $data", Toast.LENGTH_LONG).show()
                        }
                    }

                })

//            var wayParam = GpsFollowMeInfo()
//            wayParam.attitudeStrategy = 100
//            wayParam.orbitDirection = 8
//            wayParam.orbitRadius = 1
//            wayParam.orbitSpeed = 1
//            wayParam.groundFollowMode = 1
//            wayParam.headingMode = 1
//            MissionCommonManager2.getInstance().gpsFollowMeMission(wayParam, null)
            }
            (findViewById<TextView>(R.id.textView24)!!).setOnClickListener {
                val wayParam = GpsTargetInfo()
                wayParam.alt = 100
                wayParam.lat = 8
                wayParam.lon = 1
                wayParam.precision = 1
                MissionCommonManager2.getInstance().gpsTargetMission(wayParam)
            }
            (findViewById<TextView>(R.id.setBandModeWidthInfo)!!).setOnClickListener {
                DspRFManager2.getInstance().setBandModeWidthInfo(BandMode.MODE_900M, Bandwidth.WIDTH_20M)
            }
            (findViewById<TextView>(R.id.setVideoTransferMode3)!!).setOnClickListener {
                DspRFManager2.getInstance().setVideoTransferMode(TransferMode.HIGH_DEFINITION, object : CallbackWithNoParam {
                    override fun onSuccess() {
                        Log.d(TAG, "setTransferMode success ")
                    }

                    override fun onFailure(error: AutelError?) {
                        Log.d(TAG, "setTransferMode onFailure ")
                    }
                })
            }
            (findViewById<TextView>(R.id.setVideoTransferMode1)!!).setOnClickListener {
                DspRFManager2.getInstance().setVideoTransferMode(TransferMode.FLUENCY, object : CallbackWithNoParam {
                    override fun onSuccess() {
                        Log.d(TAG, "setTransferMode success ")
                    }

                    override fun onFailure(error: AutelError?) {
                        Log.d(TAG, "setTransferMode onFailure ")
                    }
                })
            }
            (findViewById<TextView>(R.id.setVideoTransferMode2)!!).setOnClickListener {
                DspRFManager2.getInstance().setVideoTransferMode(TransferMode.NORMAL, object : CallbackWithNoParam {
                    override fun onSuccess() {
                        Log.d(TAG, "setTransferMode success ")
                    }

                    override fun onFailure(error: AutelError?) {
                        Log.d(TAG, "setTransferMode onFailure ")
                    }
                })
            }
            (findViewById<TextView>(R.id.getVideoTransferMode)!!).setOnClickListener {
                DspRFManager2.getInstance().getVideoTransferMode(object : CallbackWithOneParam<VideoTransferModeInfo> {
                    override fun onFailure(error: AutelError?) {

                        Log.d(TAG, "getTransferMode onFailure ")
                    }

                    override fun onSuccess(data: VideoTransferModeInfo?) {
                        Log.d(TAG, "getTransferMode success " + data?.transfMode)
                    }
                })
            }
            (findViewById<TextView>(R.id.setBandModeWidthInfoListener)!!).setOnClickListener {
                DspRFManager2.getInstance().setBandModeWidthInfoListener(object : CallbackWithOneParam<BandModeWidthInfo> {
                    override fun onFailure(error: AutelError?) {
                        Log.d(TAG, "setBandModeWidthInfoListener onFailure ")
                    }

                    override fun onSuccess(data: BandModeWidthInfo?) {
                        Log.d(TAG, "setBandModeWidthInfoListener success " + data?.bandWidth + " " + data?.bandMode)
                    }
                })
//            GimbalManager2.getInstance().getRollAdjustData(object : CallbackWithOneParam<GimbalCmdInfo> {
//                override fun onSuccess(data: GimbalCmdInfo) {
//                    if (data.data[0] == 0) {
//                        Log.d(TAG, "getRollAdjustData onSuccess " + (data.data[1] / 100f).toDouble())
//                    }
//                }
//
//                override fun onFailure(error: AutelError) {
//                    Log.d(TAG, "getRollAdjustData onFailure ")
//                }
//            })
            }
        }
    }
}

