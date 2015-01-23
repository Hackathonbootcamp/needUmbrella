package hackathonbootcamp.needumbrella.common;

import jp.ne.docomo.smt.dev.common.http.AuthApiKey;
import jp.ne.docomo.smt.dev.environmentsensor.EnvironmentSensor;
import jp.ne.docomo.smt.dev.environmentsensor.data.EnvironmentSensorResultData;
import jp.ne.docomo.smt.dev.environmentsensor.param.EnvironmentSensorRequestParam;

public class GetEnvSensorResult {
    public GetEnvSensorResult(){
        AuthApiKey.initializeAuth(AlarmConstants.ENV_SENSOR_API_KEY);
    }

    public EnvironmentSensorResultData get(String geoCode) throws Exception {
        EnvironmentSensorRequestParam requestParam = new EnvironmentSensorRequestParam();
        requestParam.setId(geoCode);
        requestParam.setWithData("true");
        requestParam.setDataType("1213");

        EnvironmentSensor environment = new EnvironmentSensor();
        EnvironmentSensorResultData resultData = environment.request(requestParam);
        return resultData;
    }
}
