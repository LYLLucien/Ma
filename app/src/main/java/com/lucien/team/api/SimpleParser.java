package com.lucien.team.api;

import android.text.TextUtils;


import com.lucien.team.app.Enumeration.ApiStatus;
import com.lucien.team.util.common.CommonLog;

import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lucien.li on 2015/6/4.
 */
public class SimpleParser extends DefaultHandler {

    private final String CLASSTAG = SimpleParser.class.getSimpleName();
    private ApiResult result;

    public SimpleParser(ApiResult result) {
        this.result = result;
    }

    public ApiStatus doParse(InputStream is) throws IOException {
        String msg = getStringFromIS(is);

        if (!TextUtils.isEmpty(msg) && !msg.contains("Fatal error")) {
            result.setState(ApiResult.API_STATE_NORMAL);
            result.setMsg(msg);
            return ApiStatus.Success;
        }
        return ApiStatus.Connection_Error;
    }

    public String getStringFromIS(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));

        String inputLine;
        String inputResult = "";
        while ((inputLine = in.readLine()) != null) {
            inputResult += inputLine;
        }
        in.close();

        if (!inputResult.startsWith("{") && !inputResult.startsWith("[")) {
            inputResult = inputResult.substring(1);
        }
        CommonLog.d(CLASSTAG, "input result: " + inputResult);
        return inputResult;
    }
}
