package com.mqbcoding.stats;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.anastr.speedviewlib.Gauge;
import com.github.anastr.speedviewlib.RaySpeedometer;
import com.github.anastr.speedviewlib.Speedometer;
import com.github.anastr.speedviewlib.components.Indicators.ImageIndicator;
import com.github.anastr.speedviewlib.components.Indicators.Indicator;
import com.github.martoreto.aauto.vex.CarStatsClient;
import com.github.martoreto.aauto.vex.FieldSchema;
import com.google.android.apps.auto.sdk.StatusBarController;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.jetbrains.annotations.NotNull;
import org.prowl.torque.remote.ITorqueService;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardFragment extends CarFragment {
   public final String QUERY_NONE = "none";
   public final String QUERY_TORQUE_RPM = "torque-rpm_0x0c";
   public final String QUERY_TORQUE_SPEED = "torque-speed_0x0d";
   public final String QUERY_TORQUE_FUELPRESSURE = "torque-fuelpressure_0x0a";
   public final String QUERY_TORQUE_ENGINELOAD = "torque-engineload_0x04";
   public final String QUERY_TORQUE_ENGINELOADABSOLUTE = "torque-engineloadabsolute_0x43";
   public final String QUERY_TORQUE_TIMING_ADVANCE = "torque-timing_advance_0x0e";
   public final String QUERY_TORQUE_INTAKE_AIR_TEMPERATURE = "torque-intake_air_temperature_0x0f";
   public final String QUERY_TORQUE_MASS_AIR_FLOW = "torque-mass_air_flow_0x10";
   public final String QUERY_TORQUE_THROTTLE_POSITION = "torque-throttle_position_0x11";
   public final String QUERY_TORQUE_RELATIVETHROTTLEPOSITION = "torque-relativethrottleposition_0x45";
   public final String QUERY_TORQUE_ABSOLUTETHROTTLEPOSTION = "torque-absolutethrottlepostion_0x47";
   public final String QUERY_TORQUE_TURBOBOOST = "torque-turboboost_0xff1202";
   public final String QUERY_TORQUE_VOLTAGE = "torque-voltage_0xff1238";
   public final String QUERY_TORQUE_AFR = "torque-AFR_0xff1249";
   public final String QUERY_TORQUE_AFRC = "torque-AFRc_0xff124d";
   public final String QUERY_TORQUE_FUELTRIMSHORTTERM1 = "torque-fueltrimshortterm1_0x06";
   public final String QUERY_TORQUE_FUELTRIMLONGTERM1 = "torque-fueltrimlongterm1_0x07";
   public final String QUERY_TORQUE_FUELTRIMSHORTTERM2 = "torque-fueltrimshortterm2_0x08";
   public final String QUERY_TORQUE_FUELTRIMLONGTERM2 = "torque-fueltrimlongterm2_0x09";
   public final String QUERY_TORQUE_ACCELEROMETER_TOTAL = "torque-accelerometer_total_0xff1223";
   public final String QUERY_TORQUE_PHONEBATTERYLEVEL = "torque-phonebatterylevel_0xff129a";
   public final String QUERY_TORQUE_PHONEBAROMETER = "torque-phonebarometer_0xff1270";
   public final String QUERY_TORQUE_OBDADAPTERVOLTAGE = "torque-obdadaptervoltage_0xff1238";
   public final String QUERY_TORQUE_FUELLEVEL = "torque-fuellevel_0x2f";
   public final String QUERY_TORQUE_VOLTAGEMODULE = "torque-voltagemodule_0x42";
   public final String QUERY_TORQUE_OILTEMPERATURE = "torque-oiltemperature_0x5c";
   public final String QUERY_TORQUE_TRANSMISSIONTEMP1 = "torque-transmissiontemp_0x0105";
   public final String QUERY_TORQUE_TRANSMISSIONTEMP2 = "torque-transmissiontemp_0xfe1805";
   public final String QUERY_TORQUE_PRESSURECONTROL = "torque-pressurecontrol_0x70";
   public final String QUERY_TORQUE_INTAKEMANIFOLDPRESSURE = "torque-intakemanifoldpressure_0x0b";
   public final String QUERY_TORQUE_COMMANDEDEQUIVALENCERATIOLAMBDA = "torque-commandedequivalenceratiolambda_0x44";
   public final String QUERY_TORQUE_O2SENSOR1EQUIVALENCERATIO = "torque-o2sensor1equivalenceratio_0x34";
   public final String QUERY_TORQUE_CHARGEAIRCOOLERTEMPERATURE = "torque-chargeaircoolertemperature_0x77";
   public final String QUERY_TORQUE_ENGINECOOLANTTEMP = "torque-enginecoolanttemp_0x05";
   public final String QUERY_TORQUE_AMBIENTAIRTEMP = "torque-ambientairtemp_0x46";
   public final String QUERY_TORQUE_CATALYSTTEMPERATURE = "torque-catalysttemperature_0x3c";
   public final String QUERY_TORQUE_FUELRAILPRESSURE = "torque-fuelrailpressure_0x23";
   public final String QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR1 = "torque-exhaustgastempbank1sensor1_0x78";
   public final String QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR2 = "torque-exhaustgastempbank1sensor2_0xff1282";
   public final String QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR3 = "torque-exhaustgastempbank1sensor3_0xff1283";
   public final String QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR4 = "torque-exhaustgastempbank1sensor4_0xff1284";
   public final String QUERY_TORQUE_LONGITUDINALGFORCE = "torque-longitudinalgforce_0xff1220";
   public final String QUERY_TORQUE_LATERALGFORCE = "torque-lateralgforce_0xff1221";
   public final String QUERY_TEST = "test";

    private final String TAG = "DashboardFragment";
    private Timer updateTimer;
    private CarStatsClient mStatsClient;
    private Speedometer mClockLeft, mClockCenter, mClockRight;
    private Speedometer mClockMaxLeft, mClockMaxCenter, mClockMaxRight;
    private RaySpeedometer mRayLeft, mRayCenter, mRayRight;
    private ImageView mSteeringWheelAngle;
    private String mElement1Query, mElement2Query, mElement3Query, mElement4Query;
    private String selectedTheme, selectedBackground;
    private String mClockLQuery, mClockCQuery, mClockRQuery;
    private String pressureUnit, temperatureUnit;
    private float pressureFactor, speedFactor, powerFactor, fueltanksize;
    private int pressureMin, pressureMax;
    //icons/labels of the data elements. upper left, upper right, lower left, lower right.
    private TextView mIconElement1, mIconElement2, mIconElement3, mIconElement4;

    private TextView mtextTitleMain;
    //values of the data elements. upper left, upper right, lower left, lower right.
    private TextView mValueElement1, mValueElement2, mValueElement3, mValueElement4, mTitleElement,
            mTitleElementRight, mTitleElementLeft, mTitleElementNavDistance, mTitleElementNavTime, mTitleNAVDestinationAddress;
    private TextView mTitleIcon1, mTitleIcon2, mTitleIcon3, mTitleIcon4, mTitleClockLeft, mTitleClockCenter, mTitleClockRight;
    private TextView mTitleConsumptionLeft, mTitleConsumptionCenter, mTitleConsumptionRight;
    private ConstraintLayout mConstraintClockLeft, mConstraintClockRight, mConstraintClockCenter;
    private ConstraintLayout mConstraintGraphLeft, mConstraintGraphRight, mConstraintGraphCenter;
    private ConstraintLayout mConstraintElementLeft, mConstraintElementRight, mConstraintElementCenter;
    private TextView  mTextMaxLeft,mTextMaxCenter,mTextMaxRight;
    //icons on the clocks
    private TextView mIconClockL, mIconClockC, mIconClockR;
    private Boolean pressureUnits, temperatureUnits, powerUnits;
    private Boolean stagingDone;
    private Boolean raysOn, maxOn, maxMarksOn, ticksOn, ambientOn, accurateOn, proximityOn;
    private Boolean Dashboard2_On,Dashboard3_On,Dashboard4_On,Dashboard5_On;
    private Map<String, Object> mLastMeasurements = new HashMap<>();
    private Handler mHandler = new Handler();
    private ITorqueService torqueService;
    private boolean torqueBind = false;
    private GraphView mGraphLeft, mGraphCenter, mGraphRight;
    private LineGraphSeries<DataPoint> mSpeedSeriesLeft;
    private LineGraphSeries<DataPoint> mSpeedSeriesCenter;
    private LineGraphSeries<DataPoint> mSpeedSeriesRight;
    private double graphLeftLastXValue = 5d;
    private double graphCenterLastXValue = 5d;
    private double graphRightLastXValue = 5d;
    //value displayed on graphlayout
    private TextView mGraphValueLeft, mGraphValueCenter, mGraphValueRight;
    private View rootView;
    private View mDashboard_gaudes, mDashboard_consumption;

    private String androidClockFormat = "hh:mm a";
    int dashboardNum=1;

    private Button mBtnNext, mBtnPrev;
    private String mLabelClockL, mLabelClockC, mLabelClockR;
    private HashMap<String, FieldSchema> mSchema;


    // notation formats
    private static final String FORMAT_DECIMALS = "%.1f";
    private static final String FORMAT_DECIMALS_WITH_UNIT = "%.1f %s";
    private static final String FORMAT_DEGREES = "%.1f°";
    private static final String FORMAT_GFORCE = "%.1fG";
    private static final String FORMAT_KM = "%.1f km";
    private static final String FORMAT_MILES = "%.1f miles";
    private static final String FORMAT_NO_DECIMALS = "%.0f";
    private static final String FORMAT_PERCENT = "%.1f";
    private static final String FORMAT_DEGREESPEC = "%.1f°/s";
    private static final String FORMAT_TEMPERATURE = "%.1f°";
    private static final String FORMAT_TEMPERATURE0 = "-,-°";
    private static final String FORMAT_TEMPERATUREC = "%.1f°C";
    private static final String FORMAT_TEMPERATUREF = "%.1f°F";
    private static final String FORMAT_VOLT = "%.1fV";
    private static final String FORMAT_VOLT0 = "-,-V";
    private boolean celsiusTempUnit;
    private String selectedFont;
    private boolean selectedPressureUnits;
    private int updateSpeed = 2000;

    private float[] MaxspeedLeft;
    private float[] MaxspeedCenter;
    private float[] MaxspeedRight;
    private TextView mValueLeftElement1,mValueLeftElement2,mValueLeftElement3,mValueLeftElement4,mValueLeftElement5,mValueLeftElement6;
    private TextView mValueCenterElement1,mValueCenterElement2,mValueCenterElement3,mValueCenterElement4,mValueCenterElement5,mValueCenterElement6;
    private TextView mValueRightElement1,mValueRightElement2,mValueRightElement3,mValueRightElement4,mValueRightElement5,mValueRightElement6;
    @Override
    protected void setupStatusBar(StatusBarController sc) {
        sc.hideTitle();
    }


    // todo: reset min/max when clock is touched

    private final View.OnClickListener resetMinMax = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            MaxspeedLeft[dashboardNum] = 0;
            MaxspeedCenter[dashboardNum] = 0;
            MaxspeedRight[dashboardNum] = 0;

            float speedLeft = MaxspeedLeft[dashboardNum];// mClockLeft.getSpeed();
            float speedCenter = MaxspeedCenter[dashboardNum]; //;mClockCenter.getSpeed();
            float speedRight = MaxspeedRight[dashboardNum]; //mClockRight.getSpeed();

            mClockMaxLeft.speedTo(speedLeft);
            mClockMaxCenter.speedTo(speedCenter);
            mClockMaxRight.speedTo(speedRight);

            mTextMaxLeft.setText(String.format(Locale.US, FORMAT_DECIMALS, speedLeft));
            mTextMaxCenter.setText(String.format(Locale.US, FORMAT_DECIMALS, speedCenter));
            mTextMaxRight.setText(String.format(Locale.US, FORMAT_DECIMALS, speedRight));

        }
    };

    private View.OnClickListener toggleView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v==mConstraintClockLeft){
                fadeOutfadeIn(v, mConstraintGraphLeft);
                mTextMaxLeft.setVisibility(View.INVISIBLE);
            } else if (v==mConstraintClockCenter) {
                fadeOutfadeIn(v, mConstraintGraphCenter);
                mTextMaxCenter.setVisibility(View.INVISIBLE);
            } else if (v==mConstraintClockRight) {
                fadeOutfadeIn(v, mConstraintGraphRight);
                mTextMaxRight.setVisibility(View.INVISIBLE);
            } else if (v == mGraphLeft) {
                fadeOutfadeIn(mConstraintGraphLeft, mConstraintClockLeft);
                if (maxOn) mTextMaxLeft.setVisibility(View.VISIBLE);
            } else if (v == mGraphCenter) {
                fadeOutfadeIn(mConstraintGraphCenter, mConstraintClockCenter);
                if (maxOn) mTextMaxCenter.setVisibility(View.VISIBLE);
            } else if (v == mGraphRight) {
                fadeOutfadeIn(mConstraintGraphRight, mConstraintClockRight);
                if (maxOn) mTextMaxRight.setVisibility(View.VISIBLE);
            } else if (v == mBtnPrev) {
                dashboardNum++;
                if (dashboardNum==2 && !Dashboard2_On) dashboardNum++;
                if (dashboardNum==3 && !Dashboard3_On) dashboardNum++;
                if (dashboardNum==4 && !Dashboard4_On) dashboardNum++;
                if (dashboardNum==5 && !Dashboard5_On) dashboardNum++;
                Log.v(TAG,"Button Prev: "+dashboardNum);
                if (dashboardNum > 4) dashboardNum = 1;
                onPreferencesChangeHandler();
            } else if (v == mBtnNext) {
                dashboardNum--;
                Log.v(TAG,"Button Next: "+dashboardNum);
                if (dashboardNum < 1) dashboardNum = 4;
                if (dashboardNum==5 && !Dashboard5_On) dashboardNum--;
                if (dashboardNum==4 && !Dashboard4_On) dashboardNum--;
                if (dashboardNum==3 && !Dashboard3_On) dashboardNum--;
                if (dashboardNum==2 && !Dashboard2_On) dashboardNum--;
                onPreferencesChangeHandler();
            }
        }
    };



    private final CarStatsClient.Listener mCarStatsListener = new CarStatsClient.Listener() {
        @Override
        public void onNewMeasurements(String provider, Date timestamp, Map<String, Object> values) {
            mLastMeasurements.putAll(values);

            postUpdate();

            //Log.i(TAG, "onCarStatsClient.Listener");
        }

        @Override
        public void onSchemaChanged() {
            // do nothing
        }


    };

    private final ServiceConnection mVexServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            CarStatsService.CarStatsBinder carStatsBinder = (CarStatsService.CarStatsBinder) iBinder;
            Log.i(TAG, "ServiceConnected");
            mStatsClient = carStatsBinder.getStatsClient();
            mLastMeasurements = mStatsClient.getMergedMeasurements();
            mStatsClient.registerListener(mCarStatsListener);
            doUpdate();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mStatsClient.unregisterListener(mCarStatsListener);
            Log.i(TAG, "ServiceDisconnected");
        }
    };


    // random, for use in Test value
    private static float randFloat(float min, float max) {
        Random rand = new Random();
        return rand.nextFloat() * (max - min) + min;
    }

    public static String convGear(String gear) {

        String convertedGear = "0";
        switch (gear) {
            case "Gear1":
                convertedGear = "1";
                break;
            case "Gear2":
                convertedGear = "2";
                break;
            case "Gear3":
                convertedGear = "3";
                break;
            case "Gear4":
                convertedGear = "4";
                break;
            case "Gear5":
                convertedGear = "5";
                break;
            case "Gear6":
                convertedGear = "6";
                break;
            case "Gear7":
                convertedGear = "7";
                break;
            case "Gear8":
                convertedGear = "8";
                break;
        }
        return convertedGear;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    private void updateDisplay() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                postUpdate();
            }

        }, 0, 500);//Update display 0.5 second
    }

    private void setupViews(View rootView) {
        //layouts/constrains:
        mDashboard_gaudes = rootView.findViewById(R.id.include);
        mDashboard_consumption = rootView.findViewById(R.id.include_consumption);

        mConstraintClockLeft = rootView.findViewById(R.id.constraintClockLeft);
        mConstraintClockCenter = rootView.findViewById(R.id.constraintClockCenter);
        mConstraintClockRight = rootView.findViewById(R.id.constraintClockRight);

        mConstraintGraphLeft = rootView.findViewById(R.id.constraintGraphLeft);
        mConstraintGraphCenter = rootView.findViewById(R.id.constraintGraphCenter);
        mConstraintGraphRight = rootView.findViewById(R.id.constraintGraphRight);

        mConstraintElementLeft = rootView.findViewById(R.id.constraintElementLeft);
        mConstraintElementCenter = rootView.findViewById(R.id.constraintElementCenter );
        mConstraintElementRight = rootView.findViewById(R.id.constraintElementRight);

        mConstraintElementLeft.setVisibility(View.INVISIBLE);
        mConstraintElementCenter.setVisibility(View.INVISIBLE);
        mConstraintElementRight.setVisibility(View.INVISIBLE);

        //clocks:
        mClockLeft = rootView.findViewById(R.id.dial_Left);
        mClockCenter = rootView.findViewById(R.id.dial_Center);
        mClockRight = rootView.findViewById(R.id.dial_Right);

        //max & min dials
        mClockMaxLeft = rootView.findViewById(R.id.dial_MaxLeft);
        mClockMaxCenter = rootView.findViewById(R.id.dial_MaxCenter);
        mClockMaxRight = rootView.findViewById(R.id.dial_MaxRight);

        mtextTitleMain = rootView.findViewById(R.id.textTitle);
        //reset value max
        MaxspeedLeft = null;
        MaxspeedCenter = null;
        MaxspeedRight = null;
        MaxspeedLeft = new float[5];
        MaxspeedCenter = new float[5];
        MaxspeedRight = new float[5];

        mBtnNext = rootView.findViewById(R.id.imageButton2);
        mBtnPrev = rootView.findViewById(R.id.imageButton3);

        //graph test
        mGraphLeft = rootView.findViewById(R.id.chart_Left);
        mGraphCenter = rootView.findViewById(R.id.chart_Center);
        mGraphRight = rootView.findViewById(R.id.chart_Right);

        mGraphValueLeft = rootView.findViewById(R.id.graphValueLeft);
        mGraphValueCenter = rootView.findViewById(R.id.graphValueCenter);
        mGraphValueRight = rootView.findViewById(R.id.graphValueRight);

        mSpeedSeriesLeft = new LineGraphSeries<>();
        mSpeedSeriesCenter = new LineGraphSeries<>();
        mSpeedSeriesRight = new LineGraphSeries<>();


        //icons on the clocks
        mIconClockL = rootView.findViewById(R.id.icon_ClockLeft);
        mIconClockC = rootView.findViewById(R.id.icon_ClockCenter);
        mIconClockR = rootView.findViewById(R.id.icon_ClockRight);

        //ray speedometers for high visibility
        mRayLeft = rootView.findViewById(R.id.rayLeft);
        mRayCenter = rootView.findViewById(R.id.rayCenter);
        mRayRight = rootView.findViewById(R.id.rayRight);

        //the 4 additional dashboard "text" elements:
        mValueElement1 = rootView.findViewById(R.id.value_Element1);
        mValueElement2 = rootView.findViewById(R.id.value_Element2);
        mValueElement3 = rootView.findViewById(R.id.value_Element3);
        mValueElement4 = rootView.findViewById(R.id.value_Element4);

        //title text element
        mTitleElement = rootView.findViewById(R.id.textTitleElement);
        mTitleElementRight = rootView.findViewById(R.id.textTitleElementRight);
        mTitleElementLeft = rootView.findViewById(R.id.textTitleElementLeft);
        mTitleNAVDestinationAddress = rootView.findViewById(R.id.textTitleNAVDestinationAddress);
        mTitleClockLeft = rootView.findViewById(R.id.textTitleLabel1);
        mTitleClockCenter = rootView.findViewById(R.id.textTitleLabel2);
        mTitleClockRight = rootView.findViewById(R.id.textTitleLabel3);
        mTitleConsumptionLeft = rootView.findViewById(R.id.textTitleConsumptionLeft);
        mTitleConsumptionCenter = rootView.findViewById(R.id.textTitleConsumptionCenter);
        mTitleConsumptionRight = rootView.findViewById(R.id.textTitleConsumptionRight);
        mTitleElementNavDistance = rootView.findViewById(R.id.textTitleNavDistance);
        mTitleElementNavTime = rootView.findViewById(R.id.textTitleNavTime);

        mTitleIcon1 = rootView.findViewById(R.id.titleIcon1);
        mTitleIcon2 = rootView.findViewById(R.id.titleIcon2);
        mTitleIcon3 = rootView.findViewById(R.id.titleIcon3);
        mTitleIcon4 = rootView.findViewById(R.id.titleIcon4);
        //labels at these text elements:
        mIconElement1 = rootView.findViewById(R.id.icon_Element1);
        mIconElement2 = rootView.findViewById(R.id.icon_Element2);
        mIconElement3 = rootView.findViewById(R.id.icon_Element3);
        mIconElement4 = rootView.findViewById(R.id.icon_Element4);

        //max texts:
        mTextMaxLeft = rootView.findViewById(R.id.textMaxLeft);
        mTextMaxCenter = rootView.findViewById(R.id.textMaxCenter);
        mTextMaxRight = rootView.findViewById(R.id.textMaxRight);

        //the elements for new dashboard (consumption, service)
        mValueLeftElement1 = rootView.findViewById(R.id.valueLeftElement1);
        mValueLeftElement2 = rootView.findViewById(R.id.valueLeftElement2);
        mValueLeftElement3 = rootView.findViewById(R.id.valueLeftElement3);
        mValueLeftElement4 = rootView.findViewById(R.id.valueLeftElement4);
        mValueLeftElement5 = rootView.findViewById(R.id.valueLeftElement5);
        mValueLeftElement6 = rootView.findViewById(R.id.valueLeftElement6);

        mValueCenterElement1 = rootView.findViewById(R.id.valueCenterElement1);
        mValueCenterElement2 = rootView.findViewById(R.id.valueCenterElement2);
        mValueCenterElement3 = rootView.findViewById(R.id.valueCenterElement3);
        mValueCenterElement4 = rootView.findViewById(R.id.valueCenterElement4);
        mValueCenterElement5 = rootView.findViewById(R.id.valueCenterElement5);
        mValueCenterElement6 = rootView.findViewById(R.id.valueCenterElement6);

        mValueRightElement1 = rootView.findViewById(R.id.valueRightElement1);
        mValueRightElement2 = rootView.findViewById(R.id.valueRightElement2);
        mValueRightElement3 = rootView.findViewById(R.id.valueRightElement3);
        mValueRightElement4 = rootView.findViewById(R.id.valueRightElement4);
        mValueRightElement5 = rootView.findViewById(R.id.valueRightElement5);
        mValueRightElement6 = rootView.findViewById(R.id.valueRightElement6);

        mSteeringWheelAngle = rootView.findViewById(R.id.wheel_angle_image);
        setupListeners();
    }

    private void setupListeners() {
        //click the
        mGraphLeft.setOnClickListener(toggleView);
        mConstraintClockLeft.setOnClickListener(toggleView);
        mGraphCenter.setOnClickListener(toggleView);
        mConstraintClockCenter.setOnClickListener(toggleView);
        mGraphRight.setOnClickListener(toggleView);
        mConstraintClockRight.setOnClickListener(toggleView);
        mBtnPrev.setOnClickListener(toggleView);
        mBtnNext.setOnClickListener(toggleView);
    }

    private void setupTypeface(String selectedFont) {
        AssetManager assetsMgr = getContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(assetsMgr, "digital.ttf");
        switch (selectedFont) {
            case "segments":
                typeface = Typeface.createFromAsset(assetsMgr, "digital.ttf");
                break;
            case "seat":
                typeface = Typeface.createFromAsset(assetsMgr, "SEAT_MetaStyle_MonoDigit_Regular.ttf");
                break;
            case "audi":
                typeface = Typeface.createFromAsset(assetsMgr, "AudiTypeDisplayHigh.ttf");
                break;
            case "vw":
                typeface = Typeface.createFromAsset(assetsMgr, "VWTextCarUI-Regular.ttf");
                break;
            case "vw2":
                typeface = Typeface.createFromAsset(assetsMgr, "VWThesis_MIB_Regular.ttf");
                break;
            case "frutiger":
                typeface = Typeface.createFromAsset(assetsMgr, "Frutiger.otf");
                break;
            case "vw3":
                typeface = Typeface.createFromAsset(assetsMgr, "VW_Digit_Reg.otf");
                break;
            case "skoda":
                typeface = Typeface.createFromAsset(assetsMgr, "Skoda.ttf");
                break;
            case "larabie":
                typeface = Typeface.createFromAsset(assetsMgr, "Larabie.ttf");
                break;
            case "ford":
                typeface = Typeface.createFromAsset(assetsMgr, "UnitedSans.otf");
                break;
        }
        //-------------------------------------------------------------
        //Give them all the right custom typeface
        //clocks
        mClockLeft.setSpeedTextTypeface(typeface);
        mClockCenter.setSpeedTextTypeface(typeface);
        mClockRight.setSpeedTextTypeface(typeface);
        mGraphValueLeft.setTypeface(typeface);
        mGraphValueCenter.setTypeface(typeface);
        mGraphValueRight.setTypeface(typeface);
        //elements
        mValueElement1.setTypeface(typeface);
        mValueElement2.setTypeface(typeface);
        mValueElement3.setTypeface(typeface);
        mValueElement4.setTypeface(typeface);
        mIconElement1.setTypeface(typeface);
        mIconElement2.setTypeface(typeface);
        mIconElement3.setTypeface(typeface);
        mIconElement4.setTypeface(typeface);
        mTitleElement.setTypeface(typeface);
        mTitleElementRight.setTypeface(typeface);
        mTitleElementLeft.setTypeface(typeface);
        mTitleNAVDestinationAddress.setTypeface(typeface);
        mTitleClockLeft.setTypeface(typeface);
        mTitleClockCenter.setTypeface(typeface);
        mTitleClockRight.setTypeface(typeface);
        mTitleConsumptionLeft.setTypeface(typeface);
        mTitleConsumptionCenter.setTypeface(typeface);
        mTitleConsumptionRight.setTypeface(typeface);
        mTitleElementNavDistance.setTypeface(typeface);
        mTitleElementNavTime.setTypeface(typeface);
        mtextTitleMain.setTypeface(typeface);

        //max
        mTextMaxLeft.setTypeface(typeface);
        mTextMaxCenter.setTypeface(typeface);
        mTextMaxRight.setTypeface(typeface);

        Log.d(TAG, "font: " + typeface);
        this.selectedFont = selectedFont;
    }

    private void onPreferencesChangeHandler() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        ambientOn = sharedPreferences.getBoolean("ambientActive", false);  //true = use ambient colors, false = don't use.
        accurateOn = sharedPreferences.getBoolean("accurateActive", false);  //true = be accurate. false = have 2000ms of animation time
        proximityOn = sharedPreferences.getBoolean("proximityActive", false);  //true = be accurate. false = have 2000ms of animation time
        if (accurateOn) {
            updateSpeed = 1;
        } else {
            updateSpeed = 2000;
        }

        if (!proximityOn) {
            mBtnNext.setVisibility(View.VISIBLE);
            mBtnPrev.setVisibility(View.VISIBLE);
            mtextTitleMain.setVisibility(View.VISIBLE);
        }

        // Load this only on first run, then leave it alone
        if (stagingDone == null) {
            stagingDone = !sharedPreferences.getBoolean("stagingActive", true);
        }

        fueltanksize = Float.parseFloat(sharedPreferences.getString("fueltanksize", "50"));

        float speedLeft = MaxspeedLeft[dashboardNum];
        float speedCenter = MaxspeedCenter[dashboardNum];
        float speedRight = MaxspeedRight[dashboardNum];

        mClockMaxLeft.speedTo(speedLeft);
        mClockMaxCenter.speedTo(speedCenter);
        mClockMaxRight.speedTo(speedRight);

        mTextMaxLeft.setText(String.format(Locale.US, FORMAT_DECIMALS, speedLeft));
        mTextMaxCenter.setText(String.format(Locale.US, FORMAT_DECIMALS, speedCenter));
        mTextMaxRight.setText(String.format(Locale.US, FORMAT_DECIMALS, speedRight));

        String dashboardId = String.valueOf(dashboardNum);
        String mtextTitlePerformance;
        if (dashboardNum<4) {
            mtextTitlePerformance = sharedPreferences.getString("performanceTitle" + dashboardId, "Performance monitor" + dashboardId);
        } else {
            mtextTitlePerformance = getResources().getString(R.string.pref_title_performance_4);
        }

        mtextTitleMain.setText(mtextTitlePerformance);

        String readedBackground = sharedPreferences.getString("selectedBackground", "background_incar_black");
        if (!readedBackground.equals(selectedBackground)) {
            setupBackground(readedBackground);
        }

        String readedFont = sharedPreferences.getString("selectedFont", "vw");
        if (!readedBackground.equals(selectedFont)) {
            setupTypeface(readedFont);
        }

        //show high visible rays on, according to the setting
        boolean readedRaysOn = sharedPreferences.getBoolean("highVisActive", false);  //true = show high vis rays, false = don't show them.
        if (raysOn == null || readedRaysOn != raysOn) {
            raysOn = readedRaysOn;
            turnRaysEnabled(raysOn);
        }

        String readedTheme = sharedPreferences.getString("selectedTheme", "");
        if (!readedTheme.equals(selectedTheme)) {
            selectedTheme = readedTheme;
            turnRaysEnabled(raysOn);
        }
        boolean readedTicksOn = sharedPreferences.getBoolean("ticksActive", false); // if true, it will display the value of each of the ticks
        if(ticksOn == null || readedTicksOn != ticksOn) {
            ticksOn = readedTicksOn;
            turnTickEnabled(ticksOn);
        }

        //determine what data the user wants to have on the 4 data views
        String readedElement1Query = sharedPreferences.getString("selectedView1_" + dashboardId, "none");
        if (!readedElement1Query.equals(mElement1Query)) {
            mElement1Query = readedElement1Query;
            setupElement(mElement1Query, mValueElement1, mIconElement1);
        }
        String readedElement2Query = sharedPreferences.getString("selectedView2_"+dashboardId, "none");
        if (!readedElement2Query.equals(mElement2Query)) {
            mElement2Query = readedElement2Query;
            setupElement(mElement2Query, mValueElement2, mIconElement2);
        }
        String readedElement3Query = sharedPreferences.getString("selectedView3_"+dashboardId, "none");
        if (!readedElement3Query.equals(mElement3Query)) {
            mElement3Query = readedElement3Query;
            setupElement(mElement3Query, mValueElement3, mIconElement3);
        }
        String readedElement4Query = sharedPreferences.getString("selectedView4_"+dashboardId, "none");
        if (!readedElement4Query.equals(mElement4Query)) {
            mElement4Query = readedElement4Query;
            setupElement(mElement4Query, mValueElement4, mIconElement4);
        }
        //determine what data the user wants to have on the 3 clocks, but set defaults first
        //setup clocks, including the max/min clocks and highvis rays and icons:
        //usage: setupClocks(query value, what clock, what icon, which ray, which min clock, which max clock)
        //could probably be done MUCH more efficient but that's for the future ;)
        // @TODO Check if min-max are being set properly here
        String readedClockLQuery = sharedPreferences.getString("selectedClockLeft"+dashboardId, QUERY_TORQUE_TURBOBOOST);
        if (!readedClockLQuery.equals(mClockLQuery)) {
            mClockLQuery = readedClockLQuery;
            setupClocks(mClockLQuery, mClockLeft, mIconClockL, mRayLeft, mClockMaxLeft);
            turnTickEnabled(ticksOn); // Due to bug in SpeedView, we need to re-enable ticks
        }
        String readedClockCQuery = sharedPreferences.getString("selectedClockCenter"+dashboardId, QUERY_TORQUE_OILTEMPERATURE);
        if (!readedClockCQuery.equals(mClockCQuery)) {
            mClockCQuery = readedClockCQuery;
            setupClocks(mClockCQuery, mClockCenter, mIconClockC, mRayCenter, mClockMaxCenter);
            turnTickEnabled(ticksOn); // Due to bug in SpeedView, we need to re-enable ticks
        }
        String readedClockRQuery = sharedPreferences.getString("selectedClockRight"+dashboardId, QUERY_TORQUE_ENGINECOOLANTTEMP);
        if (!readedClockRQuery.equals(mClockRQuery)) {
            mClockRQuery = readedClockRQuery;
            setupClocks(mClockRQuery, mClockRight, mIconClockR, mRayRight,mClockMaxRight);
            turnTickEnabled(ticksOn); // Due to bug in SpeedView, we need to re-enable ticks
        }
        //debug logging of each of the chosen elements
        Log.d(TAG, "element 1 selected:" + mElement1Query);
        Log.d(TAG, "element 2 selected:" + mElement2Query);
        Log.d(TAG, "element 3 selected:" + mElement3Query);
        Log.d(TAG, "element 4 selected:" + mElement4Query);

        Log.d(TAG, "clock l selected:" + mClockLQuery);
        Log.d(TAG, "clock c selected:" + mClockCQuery);
        Log.d(TAG, "clock r selected:" + mClockRQuery);

        //determine what data the user wants to have on the 4 data views
        mLabelClockL = getLabelClock(mClockLQuery);
        mLabelClockC = getLabelClock(mClockCQuery);
        mLabelClockR = getLabelClock(mClockRQuery);

        boolean readedPressureUnits = sharedPreferences.getBoolean("selectPressureUnit", true);  //true = bar, false = psi
        if (readedPressureUnits != selectedPressureUnits) {
            selectedPressureUnits = readedPressureUnits;
            pressureFactor = selectedPressureUnits ? 1 : (float) 14.5037738;
            pressureUnit = selectedPressureUnits ? "bar" : "psi";
            pressureMin = selectedPressureUnits ? -3 : -30;
            pressureMax = selectedPressureUnits ? 3 : 30;
        }

        boolean readedTempUnit = sharedPreferences.getBoolean("selectTemperatureUnit", true);  //true = celcius, false = fahrenheit
        if (readedTempUnit != celsiusTempUnit) {
            celsiusTempUnit = readedTempUnit;
            temperatureUnit = getString(celsiusTempUnit ? R.string.unit_c : R.string.unit_f);
        }

        boolean readedPowerUnits = sharedPreferences.getBoolean("selectPowerUnit", true);  //true = kw, false = ps
        if (powerUnits == null || readedPowerUnits != powerUnits) {
            powerUnits = readedPowerUnits;
            powerFactor = powerUnits ? 1 : 1.35962f;
        }
//

        //show texts and backgrounds for max/min, according to the setting
        boolean readedMaxOn = sharedPreferences.getBoolean("maxValuesActive", false); //true = show max values, false = hide them
        if (maxOn == null || readedMaxOn != maxOn) {
            maxOn = readedMaxOn;
            turnMinMaxTextViewsEnabled(maxOn);
        }

        boolean readedMaxMarksOn = sharedPreferences.getBoolean("maxMarksActive", false); //true = show max values as a mark on the clock, false = hide them
        if (maxMarksOn == null || readedMaxMarksOn != maxMarksOn) {
            maxMarksOn = readedMaxMarksOn;
            turnMinMaxMarksEnabled(maxMarksOn);
        }
    }

    private void setupBackground(String newBackground) {
        int resId = getResources().getIdentifier(newBackground, "drawable", getContext().getPackageName());
        if (resId != 0) {
            Drawable wallpaperImage = ContextCompat.getDrawable(getContext(), resId);
            rootView.setBackground(wallpaperImage);
        }
        selectedBackground = newBackground;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            onPreferencesChangeHandler();
        }
    };

    private void turnMinMaxMarksEnabled(boolean enabled) {
        //show clock marks for max/min, according to the setting
        mClockMaxLeft.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
        mClockMaxCenter.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
        mClockMaxRight.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
    }

    private void turnMinMaxTextViewsEnabled(boolean enabled) {
        mTextMaxLeft.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
        mTextMaxCenter.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
        mTextMaxRight.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
    }

    private void turnRaysEnabled(boolean enabled) {
        mRayLeft.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
        mRayCenter.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
        mRayRight.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
        if (enabled) {
            //also hide the needle on the clocks
            mClockLeft.setIndicator(Indicator.Indicators.NoIndicator);
            mClockCenter.setIndicator(Indicator.Indicators.NoIndicator);
            mClockRight.setIndicator(Indicator.Indicators.NoIndicator);
        } else {
            setupIndicators();
        }
    }

    private void turnTickEnabled(boolean enabled) {
        int tickNum = 9;
        mClockLeft.setTickNumber(enabled ? tickNum : 0);
        mClockLeft.setTextColor(Color.WHITE);
        mClockCenter.setTickNumber(enabled ? tickNum : 0);
        mClockCenter.setTextColor(Color.WHITE);
        mClockRight.setTickNumber(enabled ? tickNum : 0);
        mClockRight.setTextColor(Color.WHITE);
    }

    private void setupIndicators() {
        int clockSize = mClockLeft.getHeight();
        if (clockSize == 0) {
            clockSize = 250;
        }
        //this is to enable an image as indicator.
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.themedNeedle});
        int resourceId = typedArray.getResourceId(0, 0);
        typedArray.recycle();

        ImageIndicator imageIndicator = new ImageIndicator(getContext(), resourceId, clockSize, clockSize);


        int color = mClockLeft.getIndicatorColor();
        Log.i(TAG, "IndicatorColor: " + color);

        if (color == 1996533487) {       // if indicator color in the style is @color:aqua, make it an imageindicator
            mClockLeft.setIndicator(imageIndicator);
            mClockCenter.setIndicator(imageIndicator);
            mClockRight.setIndicator(imageIndicator);
            mClockLeft.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mClockCenter.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mClockRight.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mRayLeft.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mRayRight.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mRayCenter.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
        } else {
            //mClockLeft.setIndicator(Indicator.Indicators.HalfLineIndicator);
            //mClockCenter.setIndicator(Indicator.Indicators.HalfLineIndicator);
            //mClockRight.setIndicator(Indicator.Indicators.HalfLineIndicator);

            // do something to get the other type of indicator

        }

        // if rays on, turn off everything else.
        // it doesn't look too efficient at the moment, but that's to prevent the theme from adding an indicator to the rays.
        if (raysOn) {
            // todo: move this to setupClock

            mClockLeft.setIndicator(Indicator.Indicators.NoIndicator);
            mClockCenter.setIndicator(Indicator.Indicators.NoIndicator);
            mClockRight.setIndicator(Indicator.Indicators.NoIndicator);

            mRayLeft.setIndicator(Indicator.Indicators.NoIndicator);
            mRayRight.setIndicator(Indicator.Indicators.NoIndicator);
            mRayCenter.setIndicator(Indicator.Indicators.NoIndicator);

            //make indicatorlight color transparent if you don't need it:
            mClockLeft.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mClockCenter.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mClockRight.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
//
            mRayLeft.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mRayRight.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mRayCenter.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));


        } else if (color == -14575885) {
            //if theme has transparent indicator color, give clocks a custom image indicator
            //todo: do this on other fragments as well
            mClockLeft.setIndicator(imageIndicator);
            mClockCenter.setIndicator(imageIndicator);
            mClockRight.setIndicator(imageIndicator);
            mClockLeft.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mClockCenter.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mClockRight.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mRayLeft.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mRayRight.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
            mRayCenter.setIndicatorLightColor(Color.parseColor("#00FFFFFF"));
        }
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setupViews(rootView);
        onPreferencesChangeHandler();

        //Get preferences
        //set pressure dial to the wanted units
        //Most bar dials go from -2 to 3 bar.
        //Most PSI dials go from -30 to 30 psi.
        //pressurefactor is used to calculate the right value for psi later
        // build ImageIndicator using the resourceId
        // get the size of the Clock, to make sure the imageindicator has the right size.

        mClockLeft.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mClockLeft.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setupIndicators();

                setupGraph(mClockLeft, mGraphLeft, mSpeedSeriesLeft, mConstraintGraphLeft);
                setupGraph(mClockCenter, mGraphCenter, mSpeedSeriesCenter, mConstraintGraphCenter);
                setupGraph(mClockRight, mGraphRight, mSpeedSeriesRight, mConstraintGraphRight);
                turnTickEnabled(ticksOn);
                runStagingAnimation();
            }

        });

        androidClockFormat = android.text.format.DateFormat.is24HourFormat(getContext())
                ? "HH:mm" : "hh:mm a";

        //update!
        doUpdate();

        return rootView;
    }

    private String getLabelClock (String queryClock ) {
        String mtext = "";
        if ((queryClock != null && !queryClock.equals(""))){
            String[] valueArray = getResources().getStringArray(R.array.DataElementsValues);
            String[] stringArray = getResources().getStringArray(R.array.DataElementsEntries);
            int lindex = Arrays.asList(valueArray).indexOf(queryClock);
            if (lindex >= 0) {
                mtext = stringArray[lindex];
            }
        }
        return mtext;
    }



    private void runStagingAnimation() {
        if (!stagingDone) {

            mClockLeft.speedPercentTo(100, 1000);
            mClockCenter.speedPercentTo(100, 1000);
            mClockRight.speedPercentTo(100, 1000);
            mRayLeft.speedPercentTo(100, 1000);
            mRayCenter.speedPercentTo(100, 1000);
            mRayRight.speedPercentTo(100, 1000);

            final Handler staging = new Handler();
            staging.postDelayed(new Runnable() {
                public void run() {
                    if (mClockLeft != null) {
                        mClockLeft.speedTo(0, 1000);
                        mClockCenter.speedTo(0, 1000);
                        mClockRight.speedTo(0, 1000);
                        mRayLeft.speedTo(0, 1000);
                        mRayCenter.speedTo(0, 1000);
                        mRayRight.speedTo(0, 1000);
                    }
                }
            }, 1700);

            final Handler stagingReset = new Handler();
            stagingReset.postDelayed(new Runnable() {
                public void run() {
                    if (mClockLeft != null) {
                        mClockMaxLeft.speedTo(mClockLeft.getSpeed(), 1000);
                        mClockMaxCenter.speedTo(mClockCenter.getSpeed(), 1000);
                        mClockMaxRight.speedTo(mClockRight.getSpeed(), 1000);

                        mTextMaxLeft.setText("-");
                        mTextMaxCenter.setText("-");
                        mTextMaxRight.setText("-");
                        stagingDone = true;

                    }
                }
            }, 2700);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onActivate");
        Intent serviceIntent = new Intent(getContext(), CarStatsService.class);
        getContext().bindService(serviceIntent, mVexServiceConnection, Context.BIND_AUTO_CREATE);
        startTorque();
        createAndStartUpdateTimer();

        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        onPreferencesChangeHandler();
        // Force reload of components
        turnRaysEnabled(raysOn);
        turnMinMaxTextViewsEnabled(maxOn);
        turnMinMaxMarksEnabled(maxMarksOn);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Dashboard2_On = sharedPreferences.getBoolean("d2_active", false);  //Enabled dasboard2.
        Dashboard3_On = sharedPreferences.getBoolean("d3_active", false);  //Enabled dasboard3
        // @TODO Delete code related to these 2 or fix for non MQB
        Dashboard4_On = sharedPreferences.getBoolean("d4_active", false);  //Enabled dasboard4
        //Dashboard5_On = sharedPreferences.getBoolean("d5_active", false);  //Enabled dasboard5
        Dashboard5_On = false;
    }



    private void startTorque() {
        Intent intent = new Intent();
        intent.setClassName("org.prowl.torque", "org.prowl.torque.remote.TorqueService");
        getContext().startService(intent);
        Log.d(TAG, "Torque start");

        boolean successfulBind = getContext().bindService(intent, torqueConnection, 0);
        if (successfulBind) {
            torqueBind = true;
            Log.d("HU", "Connected to torque service!");
        } else {
            torqueBind = false;
            Log.e("HU", "Unable to connect to Torque plugin service");
        }
    }


    private void stopTorque() {
        Intent sendIntent = new Intent();
        sendIntent.setAction("org.prowl.torque.REQUEST_TORQUE_QUIT");
        getContext().sendBroadcast(sendIntent);
        Log.d(TAG, "Torque stop");
    }

    private void createAndStartUpdateTimer() {
        updateTimer = new Timer();
        updateTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                Runnable updateTimerRunnable = new Runnable() {
                    public void run() {
                        doUpdate();
                    }
                };
                //experimental delay
                if (mHandler != null)
                    mHandler.postDelayed(updateTimerRunnable, 1);
            }

        }, 0, 250);//Update display 0,25 second
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onDeactivate");
        updateTimer.cancel();

        mStatsClient.unregisterListener(mCarStatsListener);
        getContext().unbindService(mVexServiceConnection);

        if (torqueBind)
            try {
                getContext().unbindService(torqueConnection);
                stopTorque();
            } catch (Exception E) {
                throw E;
            }

        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);

        super.onPause();
    }



    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView");

        //put things back to null.
        //todo: check if this list is complete (probably some things are still missing)

        mClockLeft = null;
        mClockCenter = null;
        mClockRight = null;
        mSteeringWheelAngle = null;
        mValueElement1 = null;
        mValueElement2 = null;
        mValueElement3 = null;
        mValueElement4 = null;
        mTitleElement = null;
        mTitleElementRight = null;
        mTitleElementLeft = null;
        mTitleNAVDestinationAddress = null;
        mTitleClockLeft = null;
        mTitleClockCenter = null;
        mTitleClockRight = null;
        mTitleConsumptionLeft = null;
        mTitleConsumptionCenter = null;
        mTitleConsumptionRight = null;
        mTitleElementNavDistance = null;
        mTitleElementNavTime = null;
        mTitleIcon1 = null;
        mTitleIcon2 = null;
        mTitleIcon3 = null;
        mTitleIcon4 = null;
        mIconElement1 = null;
        mIconElement2 = null;
        mIconElement3 = null;
        mIconElement4 = null;
        mElement1Query = null;
        mElement2Query = null;
        mElement3Query = null;
        mElement4Query = null;
        mClockLQuery = null;
        mClockCQuery = null;
        mClockRQuery = null;
        mIconClockL = null;
        mIconClockC = null;
        mIconClockR = null;
        mClockMaxLeft = null;
        mClockMaxCenter = null;
        mClockMaxRight = null;
        mRayLeft = null;
        mRayCenter = null;
        mRayRight = null;
        selectedFont = null;
        pressureUnit = null;
        //stagingDone = false;
        mGraphCenter = null;
        mGraphLeft = null;
        mGraphRight = null;
        mSpeedSeriesCenter = null;
        mSpeedSeriesLeft = null;
        mSpeedSeriesRight = null;
        mConstraintClockLeft = null;
        mConstraintClockRight = null;
        mConstraintClockCenter = null;
        mConstraintGraphLeft = null;
        mConstraintGraphRight = null;
        mConstraintGraphCenter = null;
        mConstraintElementLeft = null;
        mConstraintElementRight = null;
        mConstraintElementCenter = null;
        mGraphValueLeft = null;
        mGraphValueCenter = null;
        mGraphValueRight = null;

        super.onDestroyView();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, "onDetach");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }


    private final static int UPDATE_AFTER = 200; //ms
    private long lastUpdate = -1;
    private void postUpdate() {
        if (lastUpdate<0 || (System.currentTimeMillis()-lastUpdate) > UPDATE_AFTER) {
            lastUpdate = System.currentTimeMillis();
            mHandler.post(new Runnable() {
                public void run() {
                    doUpdate();
                }
            });
        }
    }

    private void SetLayoutElements(TextView mValueElement, String mMeasurements, String mUnit, String mDefUnit,  String mFormat) {
        Float mGetMeasurement;
        String mGetUnit;
        if (mMeasurements == null || mMeasurements.isEmpty()) {
            mValueElement.setText("");
        } else {
            mGetMeasurement = (Float) mLastMeasurements.get(mMeasurements);
            if (mGetMeasurement == null) mGetMeasurement = Float.valueOf(0);
            if (mMeasurements=="tankLevelPrimary") mGetMeasurement = mGetMeasurement * fueltanksize;
            if (mMeasurements=="driving distance") {
                mGetMeasurement = (Float) mLastMeasurements.get("tankLevelPrimary");
                if (mGetMeasurement==null) mGetMeasurement= Float.valueOf(0);
                mGetMeasurement = mGetMeasurement * fueltanksize;
                Float mShortCons = (Float) mLastMeasurements.get("shortTermConsumptionPrimary");
                Float mLongCons = (Float) mLastMeasurements.get("LongTermConsumptionPrimary");
                if (mShortCons==null || mShortCons==0){
                    if (mLongCons==null || mLongCons==0){
                        mGetMeasurement = Float.valueOf(0);
                    } else {
                        mGetMeasurement = (mGetMeasurement/mLongCons) * 100 ;
                    }
                } else {
                    mGetMeasurement = (mGetMeasurement/mShortCons)*100;
                }
            }

            if (mUnit == null || mUnit.isEmpty()) {
                mGetUnit=mDefUnit;
            } else {
                mGetUnit = (String) mLastMeasurements.get(mUnit);
                if (mGetUnit == null || mGetUnit.isEmpty()) {
                    mGetUnit = mDefUnit;
                }
            }

            if (mFormat == "FORMAT_SHORTTIME") {
                mValueElement.setText(ConvertMinutesTime(mGetMeasurement.intValue()) + " " + mGetUnit);
            } else {
                mValueElement.setText(String.format(mFormat, mGetMeasurement) + " " + mGetUnit);
            }
        }
    };

    private static String ConvertMinutesTime(int minutesTime) {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        df.setTimeZone(timeZone);
        String time = df.format(new Date(minutesTime * 60 * 1000L));
        return time;
    }

    private void UpdateLayoutElements() {
        //Left elements
        SetLayoutElements(mValueLeftElement1,"consumptionShortTermGeneral.distanceValue","consumptionShortTermGeneral.distanceUnit","km",FORMAT_DECIMALS);
        SetLayoutElements(mValueLeftElement2,"consumptionShortTermGeneral.speedValue","consumptionShortTermGeneral.speedUnit","km/h",FORMAT_DECIMALS);
        SetLayoutElements(mValueLeftElement3,"consumptionShortTermGeneral.time","","","FORMAT_SHORTTIME");
        SetLayoutElements(mValueLeftElement5,"","","",FORMAT_DECIMALS );
        SetLayoutElements(mValueLeftElement5,"","","",FORMAT_DECIMALS );
        SetLayoutElements(mValueLeftElement6,"shortTermConsumptionPrimary","shortTermConsumptionPrimary.unit","l/100km",FORMAT_DECIMALS );

        //Center elements
        SetLayoutElements(mValueCenterElement1,"consumptionLongTermGeneral.distanceValue","consumptionLongTermGeneral.distanceUnit","km",FORMAT_DECIMALS);
        SetLayoutElements(mValueCenterElement2,"consumptionLongTermGeneral.speedValue","consumptionLongTermGeneral.speedUnit","km/h",FORMAT_DECIMALS);
        SetLayoutElements(mValueCenterElement3,"consumptionLongTermGeneral.time","","","FORMAT_SHORTTIME");
        SetLayoutElements(mValueCenterElement5,"","","",FORMAT_DECIMALS );
        SetLayoutElements(mValueCenterElement5,"","","",FORMAT_DECIMALS );
        SetLayoutElements(mValueCenterElement6,"longTermConsumptionPrimary","longTermConsumptionPrimary.unit","l/100km",FORMAT_DECIMALS );

        //Right elements
        SetLayoutElements(mValueRightElement1,"tankLevelPrimary","","l",FORMAT_DECIMALS);
        SetLayoutElements(mValueRightElement2,"driving distance","consumptionShortTermGeneral.distanceUnit","km",FORMAT_NO_DECIMALS);
        SetLayoutElements(mValueRightElement3,"","", "","");
        SetLayoutElements(mValueRightElement5,"","","",FORMAT_DECIMALS );
        SetLayoutElements(mValueRightElement5,"","","",FORMAT_DECIMALS );
        SetLayoutElements(mValueRightElement6,"currentConsumptionPrimary","currentConsumptionPrimary.unit","l/100km",FORMAT_DECIMALS );

    };


    private void doUpdate() {

        if (mClockLeft == null) {
            return;
        }

        //wait until staging is done before displaying any data on the clocks.
        if (!stagingDone) {
            Log.d(TAG,"Staging not done yet");
            return;
        }
        // Update Title - always!!!
        updateTitle();

        // Visiblle layout_dashboard_gauges or layouu_dashboard_consumption
        if (dashboardNum < 4) {
            // settings
            mConstraintElementLeft.setVisibility(View.INVISIBLE);
            mConstraintElementCenter.setVisibility(View.INVISIBLE);
            mConstraintElementRight.setVisibility(View.INVISIBLE);
            mConstraintClockLeft.setVisibility(View.VISIBLE);
            mConstraintClockCenter.setVisibility(View.VISIBLE);
            mConstraintClockRight.setVisibility(View.VISIBLE);

        //update each of the elements:
        updateElement(mElement1Query, mValueElement1, mIconElement1);
        updateElement(mElement2Query, mValueElement2, mIconElement2);
        updateElement(mElement3Query, mValueElement3, mIconElement3);
        updateElement(mElement4Query, mValueElement4, mIconElement4);

        //update each of the clocks and the min/max/ray elements that go with it
        // query, dial, visray, textmax, textmin, clockmax, clockmin)

        updateClock(mClockLQuery, mClockLeft, mRayLeft, mTextMaxLeft, mClockMaxLeft, mGraphLeft, mSpeedSeriesLeft, graphLeftLastXValue, mGraphValueLeft, MaxspeedLeft);
        updateClock(mClockCQuery, mClockCenter, mRayCenter, mTextMaxCenter, mClockMaxCenter, mGraphCenter, mSpeedSeriesCenter, graphCenterLastXValue, mGraphValueCenter, MaxspeedCenter);
        updateClock(mClockRQuery, mClockRight, mRayRight, mTextMaxRight,  mClockMaxRight,  mGraphRight, mSpeedSeriesRight, graphRightLastXValue, mGraphValueRight, MaxspeedRight);


        // get ambient color, change color of some elements to match the ambient color.
        // this can't be done during setup, because then the ambientColor is probably not received yet.
        if (ambientOn) {
            String ambientColor =
                    mLastMeasurements.containsKey("Car_ambienceLightColour.ColourSRGB")?
                            (String) mLastMeasurements.get("Car_ambienceLightColour.ColourSRGB") : null;
            //ambientColor = "#FF0000"; // for testing purposes
            if (ambientColor != null && !ambientColor.equals("")) {
                int parsedColor = Color.parseColor(ambientColor);

                if ((parsedColor != mClockLeft.getIndicatorColor()) || ((parsedColor != mRayLeft.getLowSpeedColor()))){
                    if (raysOn) {
                        mRayLeft.setLowSpeedColor(parsedColor);
                        mRayCenter.setLowSpeedColor(parsedColor);
                        mRayRight.setLowSpeedColor(parsedColor);
                        mRayLeft.setMediumSpeedColor(parsedColor);
                        mRayCenter.setMediumSpeedColor(parsedColor);
                        mRayRight.setMediumSpeedColor(parsedColor);
                    } else {
                        mClockLeft.setIndicatorColor(parsedColor);
                        mClockCenter.setIndicatorColor(parsedColor);
                        mClockRight.setIndicatorColor(parsedColor);
                        mClockLeft.setIndicatorLightColor(parsedColor);
                        mClockCenter.setIndicatorLightColor(parsedColor);
                        mClockRight.setIndicatorLightColor(parsedColor);
                    }

                    switch (selectedBackground) {
                        case "background_incar_dots":
                        case "background_incar_skoda2":
                            int resId = getResources().getIdentifier(selectedBackground, "drawable", getContext().getPackageName());
                            Drawable wallpaperImage = ContextCompat.getDrawable(getContext(),resId);

                            wallpaperImage.setColorFilter(new LightingColorFilter(parsedColor, Color.parseColor("#010101")));

                            rootView.setBackground(wallpaperImage);
                            break;
                    }
                }
            }
        }
        } else {
            mConstraintElementLeft.setVisibility(View.VISIBLE);
            mConstraintElementCenter.setVisibility(View.VISIBLE);
            mConstraintElementRight.setVisibility(View.VISIBLE);
            mConstraintClockLeft.setVisibility(View.INVISIBLE);
            mConstraintClockCenter.setVisibility(View.INVISIBLE);
            mConstraintClockRight.setVisibility(View.INVISIBLE);

            UpdateLayoutElements();
        }

        // wheel angle monitor
        Float currentWheelAngle = (Float) mLastMeasurements.get("wheelAngle");
        mSteeringWheelAngle.setRotation(currentWheelAngle == null ? 0.0f :
                Math.min(Math.max(-WheelStateMonitor.WHEEL_CENTER_THRESHOLD_DEG, -currentWheelAngle),
                        WheelStateMonitor.WHEEL_CENTER_THRESHOLD_DEG));
        mSteeringWheelAngle.setVisibility(View.INVISIBLE);

    }

    // this sets all the labels/values in an initial state, depending on the chosen options
    private void setupElement(String queryElement, TextView value, TextView label) {

        //set element label/value to default value first
        label.setBackgroundResource(0);
        value.setVisibility(View.VISIBLE);
        value.setText("-");
        label.setText("");
        String icon = "";


        // set items to have a "-" as value.
        //todo: clean this up. This can be done much nicer.
        if (queryElement.equals("none")) {
            label.setText("");
            value.setText("");
            icon = "empty";
            value.setVisibility(View.INVISIBLE);
        } else {
            label.setText("");
            value.setText("-");
        }


        // @TODO List of queries starts here
        // set the labels
        switch (queryElement) {
            case QUERY_NONE:
                icon = "empty";
                break;
            case QUERY_TEST:
                label.setBackground(getContext().getDrawable(R.drawable.ic_measurement));
                break;
            case QUERY_TORQUE_VOLTAGE:
                value.setText(FORMAT_VOLT0);
                label.setBackground(getContext().getDrawable(R.drawable.ic_battery));
                break;
            case QUERY_TORQUE_ENGINECOOLANTTEMP:
                label.setText("");
                value.setText(FORMAT_TEMPERATURE0);
                label.setBackground(getContext().getDrawable(R.drawable.ic_water));
                break;
            case QUERY_TORQUE_OILTEMPERATURE:
                value.setText(FORMAT_TEMPERATURE0);
                label.setBackground(getContext().getDrawable(R.drawable.ic_oil));
                break;
            case QUERY_TORQUE_SPEED:
                label.setText(R.string.unit_kmh);
                icon = "empty";
                break;
            case QUERY_TORQUE_RPM:
                label.setText(R.string.unit_rpm);
                icon = "empty";
                break;
            case QUERY_TORQUE_TRANSMISSIONTEMP1:
            case QUERY_TORQUE_TRANSMISSIONTEMP2:
                value.setText(FORMAT_TEMPERATURE0);
                label.setBackground(getContext().getDrawable(R.drawable.ic_gearbox));
                break;
            case QUERY_TORQUE_AMBIENTAIRTEMP:
                value.setText("-");//value.setText(R.string.format_temperature0);
                label.setBackground(getContext().getDrawable(R.drawable.ic_outsidetemperature));
                break;
            case QUERY_TORQUE_ACCELEROMETER_TOTAL:
                label.setBackground(getContext().getDrawable(R.drawable.ic_lateral));
                break;
            case QUERY_TORQUE_FUELLEVEL:
                //label.setText("1");
                label.setBackground(getContext().getDrawable(R.drawable.ic_fuel));
                break;
            case QUERY_TORQUE_ENGINELOAD:
                label.setText(getString(R.string.label_load));
                icon = "empty";
                break;
            case QUERY_TORQUE_TIMING_ADVANCE:
                label.setBackground(getContext().getDrawable(R.drawable.ic_timing));
                break;
            case QUERY_TORQUE_INTAKE_AIR_TEMPERATURE:
                label.setText(getString(R.string.label_iat));
                icon = "empty";
                break;
            case QUERY_TORQUE_MASS_AIR_FLOW:
                label.setText(getString(R.string.label_maf));
                icon = "empty";
                break;
            case QUERY_TORQUE_THROTTLE_POSITION:
                label.setBackground(getContext().getDrawable(R.drawable.ic_throttle));
                break;
            case QUERY_TORQUE_TURBOBOOST:
                label.setBackground(getContext().getDrawable(R.drawable.ic_turbo));
                break;
            case QUERY_TORQUE_AFR:
                label.setText(getString(R.string.label_afr));
                icon = "empty";
                break;
            case QUERY_TORQUE_AFRC:
                label.setText(getString(R.string.label_afrc));
                icon = "empty";
                break;
            case QUERY_TORQUE_FUELTRIMSHORTTERM1:
                label.setText(getString(R.string.label_ftst1));
                icon = "empty";
                break;
            case QUERY_TORQUE_FUELTRIMLONGTERM1:
                label.setText(getString(R.string.label_ftlt1));
                icon = "empty";
                break;
            case QUERY_TORQUE_FUELTRIMSHORTTERM2:
                label.setText(getString(R.string.label_ftst2));
                icon = "empty";
                break;
            case QUERY_TORQUE_FUELTRIMLONGTERM2:
                label.setText(getString(R.string.label_ftlt2));
                icon = "empty";
                break;
            case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR1:
                label.setText("1");
                label.setBackground(getContext().getDrawable(R.drawable.ic_fuelpressure));
                break;
            case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR2:
                label.setText("2");
                label.setBackground(getContext().getDrawable(R.drawable.ic_fuelpressure));
                break;
            case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR3:
                label.setText("3");
                label.setBackground(getContext().getDrawable(R.drawable.ic_fuelpressure));
                break;
            case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR4:
                label.setText("4");
                label.setBackground(getContext().getDrawable(R.drawable.ic_exhaust));
                break;
            case QUERY_TORQUE_FUELRAILPRESSURE:
            case QUERY_TORQUE_FUELPRESSURE:
                label.setBackground(getContext().getDrawable(R.drawable.ic_fuelpressure));
                break;
            case QUERY_TORQUE_ABSOLUTETHROTTLEPOSTION:
                label.setBackground(getContext().getDrawable(R.drawable.ic_throttle));
                break;
            case QUERY_TORQUE_CATALYSTTEMPERATURE:
                label.setBackground(getContext().getDrawable(R.drawable.ic_catalyst));
                break;
            case QUERY_TORQUE_CHARGEAIRCOOLERTEMPERATURE:
                label.setBackground(getContext().getDrawable(R.drawable.ic_cact));
                break;
            case QUERY_TORQUE_COMMANDEDEQUIVALENCERATIOLAMBDA:
                label.setText("λ");
                break;
            case QUERY_TORQUE_O2SENSOR1EQUIVALENCERATIO:
                label.setText("O²");
                break;
            case QUERY_TORQUE_PHONEBAROMETER:
                label.setBackground(getContext().getDrawable(R.drawable.ic_barometer));
                break;
            case QUERY_TORQUE_ENGINELOADABSOLUTE:
                label.setText("Load");
                break;
            case QUERY_TORQUE_PHONEBATTERYLEVEL:
                label.setBackground(getContext().getDrawable(R.drawable.ic_phone));
                break;
            case QUERY_TORQUE_OBDADAPTERVOLTAGE:
                label.setBackground(getContext().getDrawable(R.drawable.ic_obd2));
                break;
            case QUERY_TORQUE_INTAKEMANIFOLDPRESSURE:
                label.setBackground(getContext().getDrawable(R.drawable.ic_manifold));
                break;
            case QUERY_TORQUE_PRESSURECONTROL:
                label.setBackground(getContext().getDrawable(R.drawable.ic_turbo));
                break;
            case QUERY_TORQUE_RELATIVETHROTTLEPOSITION:
                label.setBackground(getContext().getDrawable(R.drawable.ic_throttle));
                break;
            case QUERY_TORQUE_VOLTAGEMODULE:
                label.setBackground(getContext().getDrawable(R.drawable.ic_voltage));
                break;
            case QUERY_TORQUE_LONGITUDINALGFORCE:
                label.setText(getString(R.string.label_longitudinalgforce));
                break;
            case QUERY_TORQUE_LATERALGFORCE:
                label.setText(getString(R.string.label_lateralgforce));
                break;
            default:
                label.setText("");
                value.setText("");
                icon = "empty";
                break;
        }


        if (icon.equals("empty")) {
            label.setBackgroundResource(0);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) label.getLayoutParams();
            params.width = 40;
            label.setLayoutParams(params);

        }
    }

    private void setupGraph(Speedometer clock, GraphView graph, LineGraphSeries<DataPoint> serie, ConstraintLayout constraint) {

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.themedBlankDialBackground});
        int blankBackgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();

        graph.addSeries(serie);

        graph.setTitle(clock.getUnit());

        constraint.setBackgroundResource(blankBackgroundResource); // put blank background
        serie.setAnimated(true);
        graph.setElevation(55);
        Viewport graphViewport = graph.getViewport();
        GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();

        graphViewport.setXAxisBoundsManual(true);
        graphViewport.setYAxisBoundsManual(true);
        graphViewport.setMinX(0);
        // set default max and min, these will be set dynamically later
        graphViewport.setMaxX(120);

        graphViewport.setScrollable(false);
        gridLabelRenderer.setVerticalLabelsVisible(true);
        gridLabelRenderer.setHighlightZeroLines(false);
        gridLabelRenderer.setGridColor(Color.parseColor("#22FFFFFF"));
        gridLabelRenderer.setVerticalLabelsColor(Color.parseColor("#22FFFFFF"));

        gridLabelRenderer.setHorizontalLabelsVisible(false);
        gridLabelRenderer.setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);

        graphViewport.setBackgroundColor(Color.argb(0, 255, 0, 0));
        serie.setDrawDataPoints(false);
        serie.setThickness(3);

        serie.setColor(Color.argb(80, 255, 255, 255));
    }

    private void setupClocks(String queryClock, Speedometer clock, TextView icon, RaySpeedometer ray,  Speedometer max) {
        String queryTrim;
        String queryLong = queryClock;
        String torqueUnit = "";
        int torqueMin = 0;
        int torqueMax = 100;


        TypedArray typedArray2 = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.themedStopWatchBackground});
        int swBackgroundResource = typedArray2.getResourceId(0, 0);
        typedArray2.recycle();

        if (queryClock.contains("-")) {
            queryTrim = queryClock.substring(0, queryClock.indexOf("-")); // check the prefix
        } else {
            queryTrim = "other";
        }
        // get min/max values and unit from torque
        if (queryTrim.equals("torque")) {
            queryClock = queryClock.substring(queryClock.lastIndexOf('_') + 1);
            queryClock = queryClock.substring(2);
            long queryPid = new BigInteger(queryClock, 16).longValue();

            try {
                if (torqueService != null) {
                    torqueUnit = torqueService.getUnitForPid(queryPid);

                    //todo: use torque min and max values to determine min/max values for torque elements
                    torqueMin = Math.round(torqueService.getMinValueForPid(queryPid));
                    torqueMax = Math.round(torqueService.getMaxValueForPid(queryPid));
                    if (torqueMin == torqueMax) {
                        torqueMin = torqueMax - 1;  // prevent min and max are equal. Speedview cannot handle this.
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        } else {
            torqueMin = 0;
            torqueMax = 100;
        }


        Log.d(TAG, "minmax speed: " + torqueMin + " " + torqueMax);

        pressureUnit = "bar";
        pressureMax = 5;
        pressureMin = -1;

        // setup each of the clocks:
        switch (queryLong) {
            case QUERY_NONE:
                setupClock(icon, "ic_none", "Off", clock, false, "", 0, 1, "float", "float");
                break;
            case QUERY_TEST:
                setupClock(icon, "ic_measurement", "", clock, false, getString(R.string.testing), 0, 360, "float", "integer");
                break;
            case QUERY_TORQUE_SPEED:
                setupClock(icon, "ic_none", "", clock, false, getString(R.string.unit_kmh), 0, 300, "integer", "integer");
                break;
            case QUERY_TORQUE_RPM:
                setupClock(icon, "ic_none", getString(R.string.unit_rpm), clock, true, getString(R.string.unit_rpm1000), 0, 9, "float", "integer");
                clock.setTicks();
                clock.setTickTextFormat(0);
                break;
            case QUERY_TORQUE_VOLTAGE:
            case QUERY_TORQUE_VOLTAGEMODULE:
                setupClock(icon, "ic_battery", "", clock, false, getString(R.string.unit_volt), 0, 17, "float", "integer");
                break;
            case QUERY_TORQUE_OILTEMPERATURE:
                setupClock(icon, "ic_oil", "", clock, true, "°", 0, 200, "float", "integer");
                break;
            case QUERY_TORQUE_ENGINECOOLANTTEMP:
                setupClock(icon, "ic_water", "", clock, true, "°", 0, 200, "float", "integer");
                break;
            case QUERY_TORQUE_AMBIENTAIRTEMP:
                setupClock(icon, "ic_outsidetemperature", "", clock, false, "°", -25, 50, "float", "integer");
                break;
            case QUERY_TORQUE_TRANSMISSIONTEMP1:
            case QUERY_TORQUE_TRANSMISSIONTEMP2:
                setupClock(icon, "ic_gearbox", "", clock, false, "°", 0, 200, "float", "integer");
                break;
            case QUERY_TORQUE_TURBOBOOST:
                setupClock(icon, "ic_turbo", "", clock, true, torqueUnit, torqueMin, torqueMax, "float", "float");
                break;
            case QUERY_TORQUE_FUELLEVEL:
                setupClock(icon, "ic_fuelprimary", "", clock, false, "l", 0, 100, "float", "integer");
                break;
            case QUERY_TORQUE_FUELPRESSURE:
                setupClock(icon, "ic_fuelpressure", "", clock, false, torqueUnit, torqueMin, torqueMax, "float", "integer");
                break;
            case QUERY_TORQUE_ENGINELOAD:
            case QUERY_TORQUE_ENGINELOADABSOLUTE:
                setupClock(icon, "ic_none", getString(R.string.label_load), clock, false, torqueUnit, 0, 100, "float", "integer");
                break;
            case QUERY_TORQUE_TIMING_ADVANCE:
                setupClock(icon, "ic_timing", "", clock, false, torqueUnit, torqueMin, torqueMax, "float", "integer");
                break;
            case QUERY_TORQUE_INTAKE_AIR_TEMPERATURE:
                setupClock(icon, "ic_none", getString(R.string.label_iat), clock, false, torqueUnit, 0, 100, "float", "integer");
                break;
            case QUERY_TORQUE_MASS_AIR_FLOW:
                setupClock(icon, "ic_none", getString(R.string.label_maf), clock, false, torqueUnit, torqueMin, torqueMax, "float", "integer");
                break;
            case QUERY_TORQUE_AFR:
                setupClock(icon, "ic_none", getString(R.string.label_afr), clock, false, torqueUnit, 0, 35, "float", "integer");
                break;
            case QUERY_TORQUE_AFRC:
                setupClock(icon, "ic_none", getString(R.string.label_afrc), clock, false, torqueUnit, 0, 35, "float", "integer");
                break;
            case QUERY_TORQUE_FUELTRIMSHORTTERM1:
                setupClock(icon, "ic_none", getString(R.string.label_ftst1), clock, false, torqueUnit, -20, 20, "float", "integer");
                break;
            case QUERY_TORQUE_FUELTRIMLONGTERM1:
                setupClock(icon, "ic_none", getString(R.string.label_ftlt1), clock, false, torqueUnit, -20, 20, "float", "integer");
                break;
            case QUERY_TORQUE_FUELTRIMSHORTTERM2:
                setupClock(icon, "ic_none", getString(R.string.label_ftst2), clock, false, torqueUnit, -20, 20, "float", "integer");
                break;
            case QUERY_TORQUE_FUELTRIMLONGTERM2:
                setupClock(icon, "ic_none", getString(R.string.label_ftlt2), clock, false, torqueUnit, -20, 20, "float", "integer");
                break;
            case QUERY_TORQUE_ACCELEROMETER_TOTAL:
                setupClock(icon, "ic_none", "", clock, false, "G", -3, 3, "float", "float");
                break;
            case QUERY_TORQUE_PHONEBATTERYLEVEL:
                setupClock(icon, "ic_phone", "", clock, false, "%", 0, 100, "integer", "integer");
                break;
            case QUERY_TORQUE_PHONEBAROMETER:
                setupClock(icon, "ic_barometer", "", clock, false, torqueUnit, torqueMin, torqueMax, "float", "integer");
                break;
            case QUERY_TORQUE_OBDADAPTERVOLTAGE:
                setupClock(icon, "ic_obd2", "", clock, false, torqueUnit, 0, 17, "float", "integer");
                break;
            case "torque-hybridbattlevel_0x5b":
                setupClock(icon, "ic_battery", "", clock, false, "%", 0, 100, "float", "integer");
                break;
            case QUERY_TORQUE_COMMANDEDEQUIVALENCERATIOLAMBDA:
                setupClock(icon, "ic_none", "lambda", clock, false, torqueUnit, 0, 3, "float", "float");
                break;
            case QUERY_TORQUE_CATALYSTTEMPERATURE:
                setupClock(icon, "ic_catalyst", "", clock, false, torqueUnit, 0, 1000, "float", "integer");
                break;
            case QUERY_TORQUE_RELATIVETHROTTLEPOSITION:
            case QUERY_TORQUE_ABSOLUTETHROTTLEPOSTION:
            case QUERY_TORQUE_THROTTLE_POSITION:
                setupClock(icon, "ic_throttle", "", clock, false, torqueUnit, 0, 100, "float", "integer");
                break;
            case QUERY_TORQUE_INTAKEMANIFOLDPRESSURE:
                setupClock(icon, "ic_manifold", "", clock, false, torqueUnit, 0, 200, "float", "integer");
                break;
            case QUERY_TORQUE_CHARGEAIRCOOLERTEMPERATURE:
                setupClock(icon, "ic_cact", "", clock, false, torqueUnit, 0, 100, "float", "integer");
                break;
            case QUERY_TORQUE_PRESSURECONTROL:
                setupClock(icon, "ic_turbo", "", clock, false, pressureUnit, pressureMin * 30, pressureMax * 30, "float", "integer");
                break;
            case QUERY_TORQUE_O2SENSOR1EQUIVALENCERATIO:
                setupClock(icon, "ic_none", "O2 sensor", clock, false, torqueUnit, 0, 3, "float", "float");
                break;
            case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR1:
                setupClock(icon, "ic_exhaust", "1", clock, false, torqueUnit, 0, 1000, "float", "integer");
                break;
            case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR2:
                setupClock(icon, "ic_exhaust", "2", clock, false, torqueUnit, 0, 1000, "float", "integer");
                break;
            case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR3:
                setupClock(icon, "ic_exhaust", "3", clock, false, torqueUnit, 0, 1000, "float", "integer");
                break;
            case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR4:
                setupClock(icon, "ic_exhaust", "4", clock, false, torqueUnit, 0, 1000, "float", "integer");
                break;
            case QUERY_TORQUE_FUELRAILPRESSURE:
                setupClock(icon, "ic_fuelpressure", "", clock, false, torqueUnit, 0, 100, "float", "integer");
                break;
            case QUERY_TORQUE_LONGITUDINALGFORCE:
                setupClock(icon, "ic_longitudinal", "", clock, false, "G", -3, +3, "float", "integer");
                break;
            case QUERY_TORQUE_LATERALGFORCE:
                setupClock(icon, "ic_lateral", "", clock, false, "G", -3, +3, "float", "integer");
                break;
        }

        // make the icon appear in the color of unitTextColor
        Drawable iconBackground = icon.getBackground();
        if (iconBackground != null) {
            int iconTint = clock.getUnitTextColor();
            iconBackground.setColorFilter(iconTint, PorterDuff.Mode.SRC_ATOP);
            icon.setBackground(iconBackground);
            icon.setTextColor(iconTint);
        }

        // bring mins and max's in line with the clock
        float minimum = clock.getMinSpeed();
        float maximum = clock.getMaxSpeed();

        //min.setMinMaxSpeed(minimum, maximum);
        ray.setMinMaxSpeed(minimum, maximum);
        max.setMinMaxSpeed(minimum, maximum);
    }

    //update clock with data
    private void updateClock(String query, Speedometer clock, RaySpeedometer visray, TextView
            textmax, Speedometer clockmax, GraphView graph, LineGraphSeries<DataPoint> series, Double graphLastXValue,
             TextView graphValue, float[] MaxSpeed) {
        if (query != null && stagingDone) {

            float speedFactor = 1f;
            pressureFactor = 1f;

            Float clockValue = 0f;
            Float oldValue = clock.getSpeed();

            String queryLong = query;
            String unitText = "";

            // Get the value that should be put on the clock, depending on the query
            // torque pid queries use torqueService.getValueForPid(queryPid), queryPid is trimmed from the query string
            String queryTrim = query.contains("-") ? query.substring(0, query.indexOf("-")) : "other";
            switch (queryTrim) {
                case "torque":
                    query = query.substring(query.lastIndexOf('_') + 1);
                    query = query.substring(2);
                    long queryPid = new BigInteger(query, 16).longValue();

                    try {
                        if (torqueService != null) {
                            clockValue = torqueService.getValueForPid(queryPid, true);
                            unitText = torqueService.getUnitForPid(queryPid);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error: " + e.getMessage());
                    }
                    break;
                default:  // the only other kind of query is the  "random" one.
                    clockValue = randFloat(0, 360);
                    break;
            }

            if (clockValue != null) {
                switch (queryLong) {
                    case QUERY_TEST:
                        //don't do anything
                        break;
                    case QUERY_NONE:    // none cannot happen currently
                        clockValue = 0f;
                        break;
                    case QUERY_TORQUE_RPM:
                        clockValue = clockValue / 1000;
                        break;
                    // torque data elements:
                    case QUERY_TORQUE_SPEED:
                    case QUERY_TORQUE_FUELPRESSURE:
                    case QUERY_TORQUE_ENGINELOAD:
                    case QUERY_TORQUE_TIMING_ADVANCE:
                    case QUERY_TORQUE_MASS_AIR_FLOW:
                    case QUERY_TORQUE_THROTTLE_POSITION:
                    case QUERY_TORQUE_AFR:
                    case QUERY_TORQUE_AFRC:
                    case QUERY_TORQUE_FUELTRIMSHORTTERM1:
                    case QUERY_TORQUE_FUELTRIMLONGTERM1:
                    case QUERY_TORQUE_FUELTRIMSHORTTERM2:
                    case QUERY_TORQUE_FUELTRIMLONGTERM2:
                    case QUERY_TORQUE_ACCELEROMETER_TOTAL:
                    case QUERY_TORQUE_PHONEBATTERYLEVEL:
                    case QUERY_TORQUE_PHONEBAROMETER:
                    case QUERY_TORQUE_OBDADAPTERVOLTAGE:
                    case "torque-hybridbattlevel_0x5b":
                    case QUERY_TORQUE_VOLTAGE:
                    case QUERY_TORQUE_PRESSURECONTROL:
                    case QUERY_TORQUE_RELATIVETHROTTLEPOSITION:
                    case QUERY_TORQUE_ABSOLUTETHROTTLEPOSTION:
                    case QUERY_TORQUE_VOLTAGEMODULE:
                    case QUERY_TORQUE_AMBIENTAIRTEMP:
                    case QUERY_TORQUE_INTAKEMANIFOLDPRESSURE:
                    case QUERY_TORQUE_COMMANDEDEQUIVALENCERATIOLAMBDA:
                    case QUERY_TORQUE_O2SENSOR1EQUIVALENCERATIO:
                    case QUERY_TORQUE_ENGINELOADABSOLUTE:
                    case QUERY_TORQUE_FUELLEVEL:
                    case QUERY_TORQUE_FUELRAILPRESSURE:
                    case QUERY_TORQUE_LONGITUDINALGFORCE:
                    case QUERY_TORQUE_LATERALGFORCE:
                        clock.setUnit(unitText); // use the units Torque is providing
                        break;
                    case QUERY_TORQUE_TURBOBOOST:
                        if (unitText.equals("psi") && pressureUnit.equals("bar")) {
                            clockValue = clockValue / 14.5037738f;
                            unitText = "bar";
                        }

                        clock.setUnit(unitText);
                        break;
                    case QUERY_TORQUE_INTAKE_AIR_TEMPERATURE:
                    case QUERY_TORQUE_TRANSMISSIONTEMP1:
                    case QUERY_TORQUE_TRANSMISSIONTEMP2:
                    case QUERY_TORQUE_OILTEMPERATURE:
                    case QUERY_TORQUE_CATALYSTTEMPERATURE:
                    case QUERY_TORQUE_CHARGEAIRCOOLERTEMPERATURE:
                    case QUERY_TORQUE_ENGINECOOLANTTEMP:
                    case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR1:
                    case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR2:
                    case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR3:
                    case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR4:
                        if (unitText.equals("°C") && temperatureUnit.equals("°C")) {
                            unitText = "°C";
                        } else {
                            unitText = "°F";
                            clockValue = clockValue * 1.8f;
                            clockValue = clockValue + 32;
                        }
                        clock.setUnit(unitText);
                        break;
                }

                // only shift x asis 0.5 positions when there's new data
                if (clock == mClockLeft) {
                    graphLeftLastXValue += 0.5d;
                } else if (clock == mClockCenter) {
                    graphCenterLastXValue += 0.5d;
                } else if (clock == mClockRight) {
                    graphRightLastXValue += 0.5d;
                }

                graph.getViewport().setMaxY(clock.getMaxSpeed());
                graph.getViewport().setMinY(clock.getMinSpeed());

            }

            // get the speed from the clock and have the high-visibility rays move to this speed as well

            boolean noNewData = clockValue==null;
            if (noNewData)
                clockValue=oldValue;


            //TODO: Updates with a non fixed period could lead to strange graphs
            series.appendData(new DataPoint(graphLastXValue, clockValue), true, 2400);
            String tempString = (String.format(Locale.US, FORMAT_DECIMALS, clockValue));
            graphValue.setText(tempString);


            // don't update when there's nothing to update
            // check if old value and new value (rounded to 1 decimal placed) are equal
            if (noNewData || Math.round(clockValue*10) == Math.round(oldValue*10)) {
                return;
            }

            // update clock with latest clockValue
            clock.speedTo(clockValue);

            if (visray.isShown()) {
                visray.speedTo(clockValue);
            }

            // update the max clocks and text
            float maxValue = clockmax.getSpeed();

            if (clockValue > maxValue) {
                if (clockmax.isShown()) {
                    clockmax.setSpeedAt(clockValue);
                }
            }
            // Max Value update
            if (maxOn && clockValue > MaxSpeed[dashboardNum]) {
                textmax.setText(String.format(Locale.US, FORMAT_DECIMALS, clockValue));
            // Save max Value
            MaxSpeed[dashboardNum] = clockValue;
            }
        }

     /*           // update the min clocks and text
                if (clockValueToGraph < minValue) {
                    clockmin.setSpeedAt(clockValueToGraph);
                    textmin.setText(String.format(Locale.US, getContext().getText(R.string.format_decimals).toString(), clockValueToGraph));
                }
      */
    }


    private String getTime() {
        String clockFormat = "hh:mm a";

        // If available, force car clock format
        String carClockFormat = (String)mLastMeasurements.get("unitTimeFormat.clockFormat");
        if (carClockFormat != null) {
            switch (carClockFormat) {
                case "format_24h":
                    clockFormat = "HH:mm";
                    break;
                case "format_12h":
                    clockFormat = "hh:mm a";
                    break;
            }
        } else { // if not, set time format based on phone settings
            clockFormat = androidClockFormat;
        }
        return new SimpleDateFormat(clockFormat, Locale.US).format(new Date());
    }


    private Boolean checkTextNav(String mText){
        Boolean mistWrong = false;
        String str2=" �";
        String str3 = "  ";
        mText = mText.trim();

        if (mText.contains(str2)){
            mistWrong = true;
        }
        if (mText.contains(str3)){
            mistWrong = true;
        }
        return mistWrong;
    }

    private void updateTitle() {

        String currentTitleValue = mTitleElement.getText().toString();
        String currentRightTitleValue = mTitleElementRight.getText().toString();
        String currentLeftTitleValue = mTitleElementLeft.getText().toString();
        String currentNavDistanceTitleValue = mTitleElementNavDistance.getText().toString();
        String currentNavTimeTitleValue = mTitleElementNavTime.getText().toString();

        Boolean mProximity = (Boolean) mLastMeasurements.get("System_ProximityRecognition.InRange");


        //mProximity = true;
        if (mProximity != null && mProximity && proximityOn) {
            ObjectAnimator animation;
            if (dashboardNum<4) animation = ObjectAnimator.ofFloat(mDashboard_gaudes, "y", 90);
                else animation = ObjectAnimator.ofFloat(mDashboard_consumption, "y", 90);

            animation.setDuration(200);
            animation.start();
            mTitleClockLeft.setText(mLabelClockL);
            mTitleClockCenter.setText(mLabelClockC);
            mTitleClockRight.setText(mLabelClockR);
            mBtnNext.setVisibility(View.VISIBLE);
            mBtnPrev.setVisibility(View.VISIBLE);
            mtextTitleMain.setVisibility(View.VISIBLE);
            // mtextTitleMain.setTextColor(Color.WHITE);
            mTitleConsumptionRight.setVisibility(View.VISIBLE);
            mTitleConsumptionLeft.setVisibility(View.VISIBLE);
            mTitleConsumptionCenter.setVisibility(View.VISIBLE);


        } else if (!proximityOn) {
            ObjectAnimator animation;
            if (dashboardNum<4) animation = ObjectAnimator.ofFloat(mDashboard_gaudes, "y", 90);
                else animation = ObjectAnimator.ofFloat(mDashboard_consumption, "y", 90);
            animation.setDuration(200);
            animation.start();
            mTitleClockLeft.setText("");
            mTitleClockCenter.setText("");
            mTitleClockRight.setText("");
            mBtnNext.setVisibility(View.VISIBLE);
            mBtnPrev.setVisibility(View.VISIBLE);
            mtextTitleMain.setVisibility(View.VISIBLE);
            //mtextTitleMain.setTextColor(Color.WHITE);
            mTitleConsumptionRight.setVisibility(View.INVISIBLE);
            mTitleConsumptionLeft.setVisibility(View.INVISIBLE);
            mTitleConsumptionCenter.setVisibility(View.INVISIBLE);

        } else {
            mTitleClockLeft.setText("");
            mTitleClockCenter.setText("");
            mTitleClockRight.setText("");
            mBtnNext.setVisibility(View.INVISIBLE);
            mBtnPrev.setVisibility(View.INVISIBLE);
            mtextTitleMain.setVisibility(View.INVISIBLE);
            //mtextTitleMain.setTextColor(Color.DKGRAY);
            mTitleConsumptionRight.setVisibility(View.INVISIBLE);
            mTitleConsumptionLeft.setVisibility(View.INVISIBLE);
            mTitleConsumptionCenter.setVisibility(View.INVISIBLE);
            ObjectAnimator animation;
            if (dashboardNum<4) animation = ObjectAnimator.ofFloat(mDashboard_gaudes, "y", 45);
                else animation = ObjectAnimator.ofFloat(mDashboard_consumption, "y", 45);
            animation.setDuration(200);
            animation.start();
        }

        String currentTime = getTime();

        if (!Objects.equals(currentTitleValue, currentTime)) {
            mTitleElement.setText(currentTime);
        }

        // Display temperature in right side of Title  bar
        Float currentTemperature = (Float) mLastMeasurements.get("outsideTemperature");
        if (currentTemperature != null) {
            if (!celsiusTempUnit) {
                currentTemperature = CarUtils.celsiusToFahrenheit(currentTemperature);
            }
            mTitleIcon1.setVisibility(View.VISIBLE);
            String temperature =
                    String.format(Locale.US, FORMAT_DECIMALS, currentTemperature) + " " + temperatureUnit;
            if (!temperature.equals(currentRightTitleValue)){
                mTitleElementRight.setText(temperature);
            }
        } else {
            mTitleIcon1.setVisibility(View.INVISIBLE);
            mTitleElementRight.setText("");
        }

        // Display NAV Distance, only value <> 0
        Float currentNavDistance = (Float) mLastMeasurements.get("Nav_GuidanceRemaining.DTD");
        //     int testDistance = 35623;
        //    currentNavDistance = new Float(testDistance);

        if (currentNavDistance != null) {
            if (currentNavDistance != 0) {
                currentNavDistance = currentNavDistance / 1000;
                String NavDistance = String.format(Locale.US, "%.1f km", currentNavDistance);
                mTitleIcon3.setVisibility(View.VISIBLE);
                mTitleElementNavDistance.setVisibility(View.VISIBLE);
                if (NavDistance != currentNavDistanceTitleValue) {
                    mTitleElementNavDistance.setText(NavDistance);
                }
            } else if (currentNavDistance == 0) {
                mTitleIcon3.setVisibility(View.INVISIBLE);
                mTitleElementNavDistance.setVisibility(View.INVISIBLE);
                mTitleElementNavDistance.setText("");
            }
        } else if (currentNavDistance == null) {
            mTitleIcon3.setVisibility(View.INVISIBLE);
            mTitleElementNavDistance.setVisibility(View.INVISIBLE);
            mTitleElementNavDistance.setText("");
        }

        // Display NAV Time, only value <> 0
        Float currentNavTime = (Float) mLastMeasurements.get("Nav_GuidanceRemaining.RTT");


        //       int testValueTime = 2100;
        //       currentNavTime = new Float(testValueTime);
        Date time = new Date();
        DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);

        if (currentNavTime != null) {
            if (currentNavTime != 0) {
                //current time
                int NAVseconds = Math.round(currentNavTime);
                Calendar gcal = new GregorianCalendar();
                gcal.setTime(time);
                gcal.add(Calendar.SECOND, NAVseconds);
                Date NAVTimeNew = gcal.getTime();
                String NAVTime = df.format(NAVTimeNew);

                mTitleIcon4.setVisibility(View.VISIBLE);
                mTitleElementNavTime.setVisibility(View.VISIBLE);
                if (NAVTime != currentNavTimeTitleValue) {
                    mTitleElementNavTime.setText(NAVTime);
                }
            } else if (currentNavTime == 0) {
                mTitleIcon4.setVisibility(View.INVISIBLE);
                mTitleElementNavTime.setVisibility(View.INVISIBLE);
                mTitleElementNavTime.setText("");
            }
        } else if (currentNavTime == null) {
            mTitleIcon4.setVisibility(View.INVISIBLE);
            mTitleElementNavTime.setVisibility(View.INVISIBLE);
            mTitleElementNavTime.setText("");
        }

        String mNAVStreet = (String) mLastMeasurements.get("Nav_GuidanceDestination.Street");
        String mNAVHousenumber = (String) mLastMeasurements.get("Nav_GuidanceDestination.Housenumber");
        String mNAVCity = (String) mLastMeasurements.get("Nav_GuidanceDestination.City");
        String mNAVadress = "";
        if (mNAVStreet != null && !mNAVStreet.equals("")) {
            mNAVadress=mNAVadress+mNAVStreet.trim()+" ";
        }
        if (mNAVHousenumber != null && !mNAVHousenumber.equals("")) {
            mNAVadress=mNAVadress+mNAVHousenumber.trim();
        }
        if (mNAVCity != null && !mNAVCity.equals("")) {
            if (!mNAVadress.equals("")) mNAVadress = mNAVadress + ", ";
            mNAVadress=mNAVadress+mNAVCity.trim();
        }
        mTitleNAVDestinationAddress.setText(mNAVadress);

        if (mProximity != null && !mNAVadress.equals("") && mProximity) {
            mTitleNAVDestinationAddress.setVisibility(View.VISIBLE);
            mTitleIcon4.setVisibility(View.INVISIBLE);
            mTitleElementNavTime.setVisibility(View.INVISIBLE);
            mTitleIcon3.setVisibility(View.INVISIBLE);
            mTitleElementNavDistance.setVisibility(View.INVISIBLE);
        }
        else {
            mTitleNAVDestinationAddress.setVisibility(View.INVISIBLE);
        }

    }


    //update the elements
    private void updateElement(String queryElement, TextView value, TextView label) {
        long queryPid;
        if (queryElement != null) {
            switch (queryElement) {
                case QUERY_NONE:
                    value.setText("");
                    break;
                case QUERY_TEST:
                    value.setText(String.format(Locale.US, FORMAT_DECIMALS, randFloat(0, 100)));
                    break;
                // the following are torque PIDs.
                case QUERY_TORQUE_FUELPRESSURE:
                case QUERY_TORQUE_ENGINELOAD:
                case QUERY_TORQUE_TIMING_ADVANCE:
                case QUERY_TORQUE_INTAKE_AIR_TEMPERATURE:
                case QUERY_TORQUE_MASS_AIR_FLOW:
                case QUERY_TORQUE_THROTTLE_POSITION:
                case QUERY_TORQUE_VOLTAGE:
                case QUERY_TORQUE_AFR:
                case QUERY_TORQUE_AFRC:
                case QUERY_TORQUE_FUELTRIMSHORTTERM1:
                case QUERY_TORQUE_FUELTRIMLONGTERM1:
                case QUERY_TORQUE_FUELTRIMSHORTTERM2:
                case QUERY_TORQUE_FUELTRIMLONGTERM2:
                case QUERY_TORQUE_ACCELEROMETER_TOTAL:
                case QUERY_TORQUE_FUELRAILPRESSURE:
                case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR1:
                case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR2:
                case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR3:
                case QUERY_TORQUE_EXHAUSTGASTEMPBANK1SENSOR4:
                case QUERY_TORQUE_ABSOLUTETHROTTLEPOSTION:
                case QUERY_TORQUE_AMBIENTAIRTEMP:
                case QUERY_TORQUE_CATALYSTTEMPERATURE:
                case QUERY_TORQUE_CHARGEAIRCOOLERTEMPERATURE:
                case QUERY_TORQUE_COMMANDEDEQUIVALENCERATIOLAMBDA:
                case QUERY_TORQUE_ENGINECOOLANTTEMP:
                case QUERY_TORQUE_ENGINELOADABSOLUTE:
                case QUERY_TORQUE_FUELLEVEL:
                case QUERY_TORQUE_INTAKEMANIFOLDPRESSURE:
                case QUERY_TORQUE_O2SENSOR1EQUIVALENCERATIO:
                case QUERY_TORQUE_OBDADAPTERVOLTAGE:
                case QUERY_TORQUE_OILTEMPERATURE:
                case QUERY_TORQUE_PHONEBAROMETER:
                case QUERY_TORQUE_PHONEBATTERYLEVEL:
                case QUERY_TORQUE_PRESSURECONTROL:
                case QUERY_TORQUE_RELATIVETHROTTLEPOSITION:
                case QUERY_TORQUE_TRANSMISSIONTEMP1:
                case QUERY_TORQUE_TRANSMISSIONTEMP2:
                case QUERY_TORQUE_VOLTAGEMODULE:
                case QUERY_TORQUE_LONGITUDINALGFORCE:
                case QUERY_TORQUE_LATERALGFORCE:

                    // TODO: this seems useless, becuase we check the torqueQuery earlier than this
                    // @TODO Icon for HP Measurement: ic_powermeter
                    /**
                     * @TODO PIDs to add:
                     *  - ff1226: HP @wheels
                     *  - ff1225: Torque @wheels
                     *  - Double check list for GR Yaris specific PIDs
                     */

                    queryElement = queryElement.substring(queryElement.lastIndexOf('_') + 1);
                    queryElement = queryElement.substring(2);
                    queryPid = new BigInteger(queryElement, 16).longValue();
                    float torqueData;

                    try {
                        if (torqueService != null) {
                            torqueData = torqueService.getValueForPid(queryPid, true);
                            String unitText = torqueService.getUnitForPid(queryPid);
                            value.setText(String.format(Locale.US, FORMAT_DECIMALS_WITH_UNIT, torqueData, unitText));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error: " + e.getMessage());
                    }
                    break;
                // the following torque values should have the unit as label
                case QUERY_TORQUE_TURBOBOOST:
                    queryElement = queryElement.substring(queryElement.lastIndexOf('_') + 1);
                    queryElement = queryElement.substring(2);
                    queryPid = new BigInteger(queryElement, 16).longValue();
                    float torqueData3;

                    try {
                        if (torqueService != null) {
                            torqueData3 = torqueService.getValueForPid(queryPid, true);


                            String unitText = torqueService.getUnitForPid(queryPid);
                            // workaround for Torque displaying the unit for turbo pressure
                            if (unitText.equals("psi") && pressureUnit.equals("bar")) {
                                torqueData3 = torqueData3 / 14.5037738f;
                                unitText = "bar";
                            }
                            value.setText(String.format(Locale.US, FORMAT_DECIMALS_WITH_UNIT, torqueData3,unitText));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error: " + e.getMessage());
                    }
                    break;
                case QUERY_TORQUE_RPM:
                case QUERY_TORQUE_SPEED:
                    queryElement = queryElement.substring(queryElement.lastIndexOf('_') + 1);
                    queryElement = queryElement.substring(2);
                    queryPid = new BigInteger(queryElement, 16).longValue();
                    try {
                        if (torqueService != null) {
                            float torqueData2 = torqueService.getValueForPid(queryPid, true);
                            String unitText = torqueService.getUnitForPid(queryPid);
                            value.setText(String.format(Locale.US, FORMAT_NO_DECIMALS, torqueData2));
                            label.setText(unitText);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error: " + e.getMessage());
                    }
                    break;
            }
        }
    }

    // set clock label, units, etc.
    private void setupClock(TextView icon, String iconDrawableName, String iconText, Speedometer clock, Boolean backgroundWithWarningArea, String unit, Integer minspeed, Integer maxspeed, String speedFormat, String tickFormat) {

        Log.d(TAG, "icon: " + icon + " iconDrawableName: " + iconDrawableName);

        int resId = getResources().getIdentifier(iconDrawableName, "drawable", getContext().getPackageName());
        Drawable iconDrawable = ContextCompat.getDrawable(getContext(),resId);
        int resIdEmpty = getResources().getIdentifier("ic_none", "drawable", getContext().getPackageName());

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.themedEmptyDialBackground});
        int emptyBackgroundResource = typedArray.getResourceId(0, 0);
        typedArray.recycle();

        // set icon. Clocks that don't need an icon have ic_none as icon
        icon.setBackground(iconDrawable);
        icon.setText(iconText);
        clock.setUnit(unit);
        clock.setMinMaxSpeed(minspeed, maxspeed);

        if (tickFormat == "float") {
            clock.setTickTextFormat(Gauge.FLOAT_FORMAT);

        } else {
            clock.setTickTextFormat(Gauge.INTEGER_FORMAT);
        }


        //dynamically scale the icon_space in case there's only an icon, and no text
        if (!iconText.equals("") && resId == resIdEmpty) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) icon.getLayoutParams();
            params.width = 40;
            icon.setLayoutParams(params);
        }


        // determine if an empty background, without red warning area is wanted
        if (!backgroundWithWarningArea) {
            clock.setBackgroundResource(emptyBackgroundResource);
        }

        //determine the clock format
        if (speedFormat.equals("float")) {
            clock.setSpeedTextFormat(Gauge.FLOAT_FORMAT);

        } else if (speedFormat.equals("integer")) {
            clock.setSpeedTextFormat(Gauge.INTEGER_FORMAT);
        }

    }

    private final ServiceConnection torqueConnection = new ServiceConnection() {
        /**
         * What to do when we get connected to Torque.
         *
         * @param arg0
         * @param service
         */
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            torqueService = ITorqueService.Stub.asInterface(service);
        }

        /**
         * What to do when we get disconnected from Torque.
         *
         * @param name
         */
        public void onServiceDisconnected(ComponentName name) {
            torqueService = null;
        }
    };

    // fade out 1 view, fade the other in during 500ms.
    private void fadeOutfadeIn(final View oldView, final View newView) {
        oldView.animate()
                .alpha(0f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        oldView.setVisibility(View.INVISIBLE);
                        newView.setVisibility(View.VISIBLE);
                        newView.setAlpha(1f);
                    }
                });
        newView.setAlpha(0f);
        newView.setVisibility(View.VISIBLE);
        newView.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }
                });
    }
}
