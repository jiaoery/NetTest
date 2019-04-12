package com.jixiang.nettest;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ClassName: NetUtils<br/>
 * Description: TODO Description. <br/>
 * date: 2019/4/12 14:14<br/>
 *
 * @author jixiang
 * @version V1.0.0
 * @since JDK 1.8
 */
public class NetUtils {
    /**
     * 得到当前的手机蜂窝网络信号强度
     * 获取LTE网络和3G/2G网络的信号强度的方式有一点不同，
     * LTE网络强度是通过解析字符串获取的，
     * 3G/2G网络信号强度是通过API接口函数完成的。
     * asu 与 dbm 之间的换算关系是 dbm=-113 + 2*asu
     */
    public static void getCurrentNetDBM(Context context) {

        final TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener mylistener = new PhoneStateListener(){

            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                super.onSignalStrengthsChanged(signalStrength);
                if (tm != null &&tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                    //4G网络 最佳范围   >-90dBm 越大越好
                    boolean isGsm=signalStrength.isGsm();
                    int cdmaDbm = getSignalStrengthsDbm(signalStrength);
//                    int evdoDbm=signalStrength.getEvdoDbm();
                    int asu=signalStrength.getGsmSignalStrength();
                    int level=getSignalStrengthLevel(signalStrength);
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                        level = signalStrength.getLevel();
//                    }
                    Log.e(Config.LOGTAG_NET, "Current 4G LTE :dbm=" + cdmaDbm+";level="+level+";isGsm="+isGsm+";asu="+asu);
                    //获取当前4g信号强度,保存在storage/cssface/network目录下
//                    SaveLocalLog.getInstance().saveFaceInfoFile(Config.LOGTAG_NET + ":Current 4G LTE :dbm=" + itedbm, Config.PATH_LOG_NETWORK_DIR);
                }

            }
        };
        //开始监听
        if (tm != null) {
            tm.listen(mylistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        }
    }



    /**
     * 获取网路信号dbm值
     * @param signalStrength
     * @return
     */
    public static int getSignalStrengthsDbm(SignalStrength signalStrength){
        Method method=null;
        try {
            method= signalStrength.getClass().getMethod("getDbm");
            int dbm = (int) method.invoke(signalStrength);
            return dbm;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取网络信号Level等级
     * @return
     */
    public static int getSignalStrengthLevel(SignalStrength signalStrength){
        Method method2 = null;
        try {
            method2 = signalStrength.getClass().getMethod("getLteLevel");
            int level = (int) method2.invoke(signalStrength);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;

    }


}
