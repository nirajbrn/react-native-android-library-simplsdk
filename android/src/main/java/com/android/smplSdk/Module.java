package com.android.smplSdk;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.simpl.android.sdk.Simpl;
import com.simpl.android.sdk.SimplUserApprovalListenerV2;

import java.util.HashMap;
import java.util.Map;

public class Module extends ReactContextBaseJavaModule {

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
  public void isApproved(String merchantId, String mobileNumber, String emailId) {
    Simpl.init(getReactApplicationContext(), merchantId);

    Simpl.getInstance().runInStagingMode();
    Simpl.getInstance().isUserApproved(emailId, mobileNumber)
            .execute(new SimplUserApprovalListenerV2() {
              @Override
              public void onSuccess(boolean b, String s, boolean b1) {
                Toast.makeText(getReactApplicationContext(), "User Approved : "+b, Toast.LENGTH_LONG).show();
              }

              @Override
              public void onError(Throwable throwable) {
                Toast.makeText(getReactApplicationContext(), "Error in User Approval :", Toast.LENGTH_LONG).show();
              }
            });
  }
}