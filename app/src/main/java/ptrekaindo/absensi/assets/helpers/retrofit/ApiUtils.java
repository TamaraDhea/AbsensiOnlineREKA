package ptrekaindo.absensi.assets.helpers.retrofit;

import ptrekaindo.absensi.assets.helpers.AppData;

public class ApiUtils {
    public static ApiServices getApiServices(){
        return RetrofitClient.getClient(AppData.URL_API).create(ApiServices.class);
    }
}
