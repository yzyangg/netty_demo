package pro.rpc_demo.serial;

import com.google.gson.Gson;
import pro.rpc_demo.serial.Serializer;

import java.io.IOException;

/**
 * @author yzy
 * @version 1.0
 * @description TODO
 * @date 2023/7/27 14:07
 */
public class JSONSerializer implements Serializer {
    public static final Gson gson = new Gson();

    @Override
    public byte[] serialize(Object object) throws IOException {
        return gson.toJson(object).getBytes();
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException {
        return gson.fromJson(new String(bytes), clazz);
    }
}
