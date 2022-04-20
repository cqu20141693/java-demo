package com.gow.camera.api.lecheng;

import static com.alibaba.fastjson.JSON.parseObject;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gow.camera.api.lecheng.bussiness.DeviceConfigManager;
import com.gow.camera.api.lecheng.bussiness.DeviceLiveManager;
import com.gow.camera.api.lecheng.bussiness.DeviceManager;
import com.gow.camera.api.lecheng.bussiness.UserManager;
import com.gow.camera.api.lecheng.bussiness.impl.DeviceConfigManagerImpl;
import com.gow.camera.api.lecheng.bussiness.impl.DeviceLiveManagerImpl;
import com.gow.camera.api.lecheng.bussiness.impl.DeviceManagerImpl;
import com.gow.camera.api.lecheng.bussiness.impl.UserManagerImpl;
import com.gow.camera.api.lecheng.model.resp.AccessToken;
import com.gow.camera.api.lecheng.model.resp.DeviceLiveInfo;
import com.gow.camera.api.lecheng.model.resp.DeviceStatus;
import com.gow.camera.api.lecheng.model.resp.DeviceTime;
import com.gow.camera.api.lecheng.model.resp.GetDeviceOsd;
import com.gow.camera.api.lecheng.model.resp.LiveStatus;
import com.gow.camera.api.lecheng.model.resp.LiveStream;
import com.gow.camera.api.lecheng.model.resp.LiveStreamInfo;
import com.gow.camera.api.lecheng.model.resp.ResponseResult;
import com.gow.camera.api.lecheng.model.resp.Result;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author gow
 * @date 2021/8/12
 */
//@SpringBootTest  注解告诉 Spring Boot 查找带 @SpringBootApplication 注解的主配置类，并使用该类启动 Spring 应用程序上下文
@Slf4j
public class APITest {
    UserManager userManager = new UserManagerImpl();

    DeviceManager deviceManager = new DeviceManagerImpl();

    DeviceConfigManager deviceConfigManager = new DeviceConfigManagerImpl();

    DeviceLiveManager deviceLiveManager = new DeviceLiveManagerImpl();
    String accessToken = "At_0000c545c64e7dfc43439d74a3c33e50";

    @Test
    @DisplayName("accessToken by appId and secret")
    public void accessToken() {

        HashMap<String, Object> params = new HashMap<>();

        ResponseResult<AccessToken> accessTokenResponseResult = userManager.accessToken(params);
        assertNotNull(accessTokenResponseResult, "accessToken");
    }

    private <T> void assertNotNull(Object object, String key) {
        Assertions.assertNotNull(object);
        log.info("{}={}", key, JSONObject.toJSONString(object));
    }

    @Test
    @DisplayName("deviceOnline by accessToken")
    public void deviceOnline() {
        String deviceId = "6M068A6PAZC11AC";
        HashMap<String, Object> params = new HashMap<>();
        params.put("deviceId", deviceId);
        params.put("token", accessToken);
        ResponseResult<DeviceStatus> deviceStatusResponseResult = deviceManager.deviceOnline(params);
        assertNotNull(deviceStatusResponseResult, "deviceOnline");
    }
    @Test
    @DisplayName("getDeviceTime by accessToken")
    public void getDeviceTime() {
        String deviceId = "6M068A6PAZC11AC";
        HashMap<String, Object> params = new HashMap<>();
        params.put("deviceId", deviceId);
        params.put("token", accessToken);
        ResponseResult<DeviceTime> deviceTime = deviceManager.getDeviceTime(params);
        assertNotNull(deviceTime, "getDeviceTime");
    }
    @Test
    @DisplayName("queryDeviceOsd by channelId")
    public void queryDeviceOsd() {
        String accessToken = "At_0000db6d125e244646e6bb4344326935";
        String deviceId = "6M068A6PAZC11AC";
        String channelId = "0";
        HashMap<String, Object> params = new HashMap<>();
        params.put("deviceId", deviceId);
        params.put("token", accessToken);
        params.put("channelId", channelId);
        ResponseResult<GetDeviceOsd> result = deviceConfigManager.queryDeviceOsd(params);
        assertNotNull(result, "queryDeviceOsd");
    }

    @Test
    @DisplayName("liveList by accessToken")
    public void liveList() {
        String accessToken = "At_000008065070712b46968d1ae989ed6c";
        String queryRange = "1-10";
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", accessToken);
        params.put("queryRange", queryRange);
        // fa9fe5bf1a6049389b116d34484da229
        ResponseResult<DeviceLiveInfo> result = deviceLiveManager.liveList(params);
        assertNotNull(result, "liveList");
    }

    @Test
    @DisplayName("queryLiveStatus by liveToken")
    public void queryLiveStatus() {
        String accessToken = "At_0000db6d125e244646e6bb4344326935";
        String liveToken = "fa9fe5bf1a6049389b116d34484da229";
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", accessToken);
        params.put("liveToken", liveToken);
        // fa9fe5bf1a6049389b116d34484da229
        ResponseResult<LiveStatus> result = deviceLiveManager.queryLiveStatus(params);
        assertNotNull(result, "liveList");
    }

    @Test
    @DisplayName("modifyLivePlanStatus by liveToken")
    public void modifyLivePlanStatus() {
        String accessToken = "At_0000db6d125e244646e6bb4344326935";
        String liveToken = "fa9fe5bf1a6049389b116d34484da229";
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", accessToken);
        params.put("liveToken", liveToken);
        params.put("status", "off");
        // fa9fe5bf1a6049389b116d34484da229
        ResponseResult<Void> result = deviceLiveManager.modifyLivePlanStatus(params);
        assertNotNull(result, "offLivePlanStatus");

        params.put("status", "on");
        result = deviceLiveManager.modifyLivePlanStatus(params);
        assertNotNull(result, "onLivePlanStatus");

    }

    @Test
    @DisplayName("modifyLivePlan by liveToken")
    public void modifyLivePlan() {
        String accessToken = "At_0000db6d125e244646e6bb4344326935";
        String liveToken = "fa9fe5bf1a6049389b116d34484da229";
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", accessToken);
        params.put("liveToken", liveToken);
        params.put("period", "everyday");
        params.put("beginTime", "08:00:00");
        params.put("endTime", "20:00:00");
        // fa9fe5bf1a6049389b116d34484da229
        ResponseResult<Void> result = deviceLiveManager.modifyLivePlan(params);
        assertNotNull(result, "modifyLivePlan everyday");

        params.put("period", "always");
        result = deviceLiveManager.modifyLivePlan(params);
        assertNotNull(result, "modifyLivePlan always");

    }

    @Test
    @DisplayName("getLiveStreamInfo by channelId")
    public void getLiveStreamInfo() {
        String accessToken = "At_0000db6d125e244646e6bb4344326935";
        String liveToken = "fa9fe5bf1a6049389b116d34484da229";
        String deviceId = "6M068A6PAZC11AC";
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", accessToken);
        params.put("deviceId", deviceId);
        params.put("channelId", "0");

        ResponseResult<LiveStreamInfo> result = deviceLiveManager.getLiveStreamInfo(params);
        assertNotNull(result, "getLiveStreamInfo");

    }

    @Test
    public void test() {
        String json = "{\"streams\":[{\"streamId\":0,\"status\":\"0\"},{\"streamId\":1,\"status\":\"0\"},"
                + "{\"streamId\":0,\"status\":\"0\"},{\"streamId\":1,\"status\":\"0\"}]}";

        LiveStatus liveStatus = parseObject(json, LiveStatus.class);
        assertNotNull(liveStatus, "liveList");
    }

    @Test
    public void testObjectConvert() {


        LiveStreamInfo liveStreamInfo = new LiveStreamInfo();
        ArrayList<LiveStream> liveStreams = new ArrayList<>();
        LiveStream liveStream = new LiveStream();
        liveStream.setStreamId(0);
        liveStream.setLineToken("liveToken");
        liveStream.setHls("hls");
        liveStream.setStatus("0");
        liveStreams.add(liveStream);
        liveStreamInfo.setStreams(liveStreams);

        Result<LiveStreamInfo> ret = new Result<>();
        ret.setCode("0");
        ret.setMsg("操作成功");
        ret.setData(liveStreamInfo);

        ResponseResult<LiveStreamInfo> result = new ResponseResult<>();
        result.setId("12314");
        result.setResult(ret);

        String jsonString = JSONObject.toJSONString(result);
        ResponseResult<LiveStreamInfo> responseResult = convertObject(jsonString);
        ResponseResult<LiveStreamInfo> responseResult1 = parseObject(jsonString, new TypeReference<>() {
        });
        ResponseResult<LiveStreamInfo> responseResult2 = parseObject(jsonString, ResponseResult.class);
        assertNotNull(responseResult, "testObjectConvert");
    }

    private <T> ResponseResult<T> convertObject(String jsonString) {
        return parseObject(jsonString, new TypeReference<ResponseResult<T>>() {
        });
    }
}
