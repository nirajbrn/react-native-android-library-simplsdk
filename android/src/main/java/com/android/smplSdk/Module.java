package com.android.smplSdk;

import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.UiThreadUtil;
import com.simpl.android.sdk.Simpl;
import com.simpl.android.sdk.SimplAuthorizeTransactionListener;
import com.simpl.android.sdk.SimplTransactionAuthorization;
import com.simpl.android.sdk.SimplUserApprovalListenerV2;

import java.util.HashMap;
import java.util.Map;

public class Module extends ReactContextBaseJavaModule {
    public static final String TAG = Module.class.getSimpleName();

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  public Module(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "Boilerplate";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
    constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
    return constants;
  }

  @ReactMethod
  public void isApproved(String merchantId, String mobileNumber, String emailId, final Callback successCallback) {
    Simpl.init(getReactApplicationContext(), merchantId);

      Log.d(TAG, "isApproved(): merchantId: "+merchantId+" mobileNumber: "+mobileNumber+" emailId: "+emailId);
    Simpl.getInstance().runInStagingMode();
    Simpl.getInstance().isUserApproved(mobileNumber, emailId)
            .execute(new SimplUserApprovalListenerV2() {
              @Override
              public void onSuccess(final boolean b, String s, boolean b1) {
                  successCallback.invoke(b);
                  UiThreadUtil.runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          Toast.makeText(getReactApplicationContext(), "User Approved : "+b, Toast.LENGTH_LONG).show();
                      }
                  });
              }

              @Override
              public void onError(Throwable throwable) {
                  UiThreadUtil.runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                        Toast.makeText(getReactApplicationContext(), "Error in User Approval :", Toast.LENGTH_LONG).show();
                      }
                  });
              }
            });
  }

  @ReactMethod
    public void authorizeTransaction(int transactionAmountInPaise, final Callback successCallback, final Callback errorCallback) {
      Simpl.getInstance().authorizeTransaction(getReactApplicationContext(), transactionAmountInPaise)
              .execute(new SimplAuthorizeTransactionListener() {
                  @Override
                  public void onSuccess(SimplTransactionAuthorization simplTransactionAuthorization) {
                      successCallback.invoke(simplTransactionAuthorization.getTransactionToken());
                  }

                  @Override
                  public void onError(Throwable throwable) {
                      errorCallback.invoke(throwable.getMessage());
                  }
              });
  }

}