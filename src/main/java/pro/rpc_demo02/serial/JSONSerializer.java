package pro.rpc_demo02.serial;

import com.google.gson.Gson;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/28 9:34
 */
public class JSONSerializer implements Serializer {

    Gson gson = new Gson();

    @Override

    public byte[] serialize(Object object) {
        return gson.toJson(object).getBytes();
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return gson.fromJson(new String(bytes), clazz);
    }
}