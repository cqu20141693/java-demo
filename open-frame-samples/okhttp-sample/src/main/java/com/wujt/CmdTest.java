package com.wujt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.HashSet;

/**
 * @author gow
 * @date 2021/10/29
 */
public class CmdTest {

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {

        String urlQuery = "http://192.168.96.168:8840/device/instance/_query";
        RequestBody body = RequestBody.create(JSON, "{\"pageSize\":100,\"currentPage\":0,\"sorts\":[{\"name\":\"createTime\",\"order\":\"desc\"},{\"name\":\"id\",\"order\":\"desc\"}],\"terms\":[{\"column\":\"productId\",\"value\":\"power_meter\",\"type\":\"and\",\"termType\":\"eq\"}]}");

        Request instance = new Request.Builder()
                .post(body)
                .addHeader("Authorization", "mes2")
                .url(urlQuery).build();
        try (Response response = client.newCall(instance).execute()) {
            String result = response.body().string();
            //System.out.println(result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject result1 = jsonObject.getJSONObject("result");
            JSONArray records = result1.getJSONArray("records");
            JSONObject object = new JSONObject();
            object.put("functions", new JSONArray());
            object.put("properties", new JSONArray());
            object.put("events", new JSONArray());
            object.put("tags", new JSONArray());
            HashSet<String> ids = new HashSet<>();
            for (Object record : records) {
                JSONObject metadata = JSONObject.parseObject(((JSONObject) record).getString("deriveMetadata"));
                JSONArray properties = metadata.getJSONArray("properties");
                JSONArray properties1 = object.getJSONArray("properties");
                properties.forEach(property -> {
                    JSONObject p = (JSONObject) property;
                    String id = p.getString("id");
                    if (!ids.contains(id)) {
                        properties1.add(property);
                        ids.add(id);
                    }
                });
            }
            System.out.println(object.toJSONString());

        } catch (IOException e) {
            e.printStackTrace();
        }

//{
//  "functions": [],
//  "properties": [
//    {
//      "valueType": {
//        "name": "字符串",
//        "id": "string",
//        "type": "string"
//      },
//      "name": "Avoltage",
//      "id": "Avoltage",
//      "expands": {
//        "propertyType": "timeseries",
//        "storageType": "direct",
//        "readOnly": [
//          "report"
//        ],
//        "source": "device"
//      }
//    },
//    {
//      "valueType": {
//        "name": "字符串",
//        "id": "string",
//        "type": "string"
//      },
//      "name": "PowerFactor",
//      "id": "PowerFactor",
//      "expands": {
//        "propertyType": "timeseries",
//        "storageType": "direct",
//        "readOnly": [
//          "report"
//        ],
//        "source": "device"
//      }
//    },
//    {
//      "valueType": {
//        "name": "字符串",
//        "id": "string",
//        "type": "string"
//      },
//      "name": "Bvoltage",
//      "id": "Bvoltage",
//      "expands": {
//        "propertyType": "timeseries",
//        "storageType": "direct",
//        "readOnly": [
//          "report"
//        ],
//        "source": "device"
//      }
//    },
//    {
//      "valueType": {
//        "name": "字符串",
//        "id": "string",
//        "type": "string"
//      },
//      "name": "ReactivePower",
//      "id": "ReactivePower",
//      "expands": {
//        "propertyType": "timeseries",
//        "storageType": "direct",
//        "readOnly": [
//          "report"
//        ],
//        "source": "device"
//      }
//    },
//    {
//      "valueType": {
//        "name": "字符串",
//        "id": "string",
//        "type": "string"
//      },
//      "name": "Electric",
//      "id": "Electric",
//      "expands": {
//        "propertyType": "timeseries",
//        "storageType": "direct",
//        "readOnly": [
//          "report"
//        ],
//        "source": "device"
//      }
//    },
//    {
//      "valueType": {
//        "name": "字符串",
//        "id": "string",
//        "type": "string"
//      },
//      "name": "Cvoltage",
//      "id": "Cvoltage",
//      "expands": {
//        "propertyType": "timeseries",
//        "storageType": "direct",
//        "readOnly": [
//          "report"
//        ],
//        "source": "device"
//      }
//    },
//    {
//      "valueType": {
//        "name": "字符串",
//        "id": "string",
//        "type": "string"
//      },
//      "name": "CVoltage",
//      "id": "CVoltage",
//      "expands": {
//        "propertyType": "timeseries",
//        "storageType": "direct",
//        "readOnly": [
//          "report"
//        ],
//        "source": "device"
//      }
//    },
//    {
//      "valueType": {
//        "name": "字符串",
//        "id": "string",
//        "type": "string"
//      },
//      "name": "AVoltage",
//      "id": "AVoltage",
//      "expands": {
//        "propertyType": "timeseries",
//        "storageType": "direct",
//        "readOnly": [
//          "report"
//        ],
//        "source": "device"
//      }
//    },
//    {
//      "valueType": {
//        "name": "字符串",
//        "id": "string",
//        "type": "string"
//      },
//      "name": "TotaElectricity",
//      "id": "TotaElectricity",
//      "expands": {
//        "propertyType": "timeseries",
//        "storageType": "direct",
//        "readOnly": [
//          "report"
//        ],
//        "source": "device"
//      }
//    },
//    {
//      "valueType": {
//        "name": "字符串",
//        "id": "string",
//        "type": "string"
//      },
//      "name": "BElectric",
//      "id": "BElectric",
//      "expands": {
//        "propertyType": "timeseries",
//        "storageType": "direct",
//        "readOnly": [
//          "report"
//        ],
//        "source": "device"
//      }
//    },
//    {
//      "valueType": {
//        "name": "字符串",
//        "id": "string",
//        "type": "string"
//      },
//      "name": "AElectric",
//      "id": "AElectric",
//      "expands": {
//        "propertyType": "timeseries",
//        "storageType": "direct",
//        "readOnly": [
//          "report"
//        ],
//        "source": "device"
//      }
//    },
//    {
//      "valueType": {
//        "name": "字符串",
//        "id": "string",
//        "type": "string"
//      },
//      "name": "CElectric",
//      "id": "CElectric",
//      "expands": {
//        "propertyType": "timeseries",
//        "storageType": "direct",
//        "readOnly": [
//          "report"
//        ],
//        "source": "device"
//      }
//    }
//  ],
//  "events": [],
//  "tags": []
//}
    }

}
