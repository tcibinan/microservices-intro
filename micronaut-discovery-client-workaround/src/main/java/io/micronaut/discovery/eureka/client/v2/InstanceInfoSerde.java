package io.micronaut.discovery.eureka.client.v2;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.Decoder;
import io.micronaut.serde.Encoder;
import io.micronaut.serde.Serde;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.util.Map;

@Singleton
public class InstanceInfoSerde implements Serde<InstanceInfo> {

    @Override
    public @Nullable InstanceInfo deserialize(@NonNull Decoder decoder,
                                              @NonNull DecoderContext context,
                                              @NonNull Argument<? super InstanceInfo> type) throws IOException {
        try (Decoder root = decoder.decodeObject(type)) {
            root.decodeKey();
            return decode(decoder);
        }
    }

    public InstanceInfo decode(Decoder decoder) throws IOException {
        try (Decoder object = decoder.decodeObject(Argument.of(Object.class))) {
            String hostName = null;
            int port = 0;
            String ipAddr = null;
            String app = null;
            String instanceId = null;
            while (true) {
                String key = object.decodeKey();
                if (key == null) {
                    break;
                }
                switch (key) {
                    case "instanceId" -> instanceId = object.decodeString();
                    case "app" -> app = object.decodeString();
                    case "ipAddr" -> ipAddr = object.decodeString();
                    case "port" -> port = decodePortWrapper(object);
                    case "hostName" -> hostName = object.decodeString();
                    default -> object.skipValue();
                }
            }
            return new InstanceInfo(hostName, port, ipAddr, app, instanceId);
        }
    }

    private static int decodePortWrapper(Decoder decoder) throws IOException {
        int port = 0;
        try (Decoder object = decoder.decodeObject(Argument.of(Object.class))) {
            while (true) {
                String key = object.decodeKey();
                if (key == null) {
                    break;
                }
                switch (key) {
                    case "$" -> port = object.decodeInt();
                    default -> object.skipValue();
                }
            }
        }
        return port;
    }

    @Override
    public void serialize(@NonNull Encoder encoder,
                          @NonNull EncoderContext context,
                          @NonNull Argument<? extends InstanceInfo> type,
                          @NonNull InstanceInfo value) throws IOException {
        try (Encoder root = encoder.encodeObject(Argument.of(Object.class))) {
            root.encodeKey("instance");
            try (Encoder obj = root.encodeObject(type)) {
                encodeString(obj, "instanceId", value.getInstanceId());
                encodeString(obj, "app", value.getApp());
                encodeString(obj, "appGroupName", value.getAppGroupName());
                encodeString(obj, "ipAddr", value.getIpAddr());
                encodePortWrapper(obj, "port", value.getPort());
                encodePortWrapper(obj, "securePort", value.getSecurePort());
                encodeString(obj, "homePageUrl", value.getHomePageUrl());
                encodeString(obj, "statusPageUrl", value.getStatusPageUrl());
                encodeString(obj, "healthCheckUrl", value.getHealthCheckUrl());
                encodeString(obj, "secureHealthCheckUrl", value.getSecureHealthCheckUrl());
                encodeString(obj, "vipAddress", value.getVipAddress());
                encodeString(obj, "secureVipAddress", value.getSecureVipAddress());
                encodeInt(obj, "countryId", value.getCountryId());
                encodeDataCenterInfo(obj, "dataCenterInfo", value.getDataCenterInfo());
                encodeString(obj, "hostName", value.getHostName());
                encodeString(obj, "status", value.getStatus().toString());
                encodeMap(obj, "metadata", value.getMetadata());
                encodeString(obj, "asgName", value.getAsgName());
            }
        }
    }

    private static void encodePortWrapper(Encoder encoder, String key, int value) throws IOException {
        if (value == 0) {
            return;
        }
        encoder.encodeKey(key);
        try (Encoder obj = encoder.encodeObject(Argument.of(Object.class))) {
            obj.encodeKey("@enabled"); obj.encodeBoolean(true);
            encodeInt(obj, "$", value);
        }
    }

    private static void encodeInt(Encoder encoder, String key, int value) throws IOException {
        if (value == 0) {
            return;
        }
        encoder.encodeKey(key);
        encoder.encodeInt(value);
    }

    private static void encodeString(Encoder encoder, String key, String value) throws IOException {
        if (value == null) {
            return;
        }
        encoder.encodeKey(key);
        encoder.encodeString(value);
    }

    private static void encodeMap(Encoder encoder, String key, Map<String, String> value) throws IOException {
        if (value == null) {
            return;
        }
        encoder.encodeKey(key);
        try (Encoder map = encoder.encodeObject(Argument.of(Object.class))) {
            for (Map.Entry<String, String> entry : value.entrySet()) {
                encodeString(map, entry.getKey(), entry.getValue());
            }
        }
    }

    private void encodeDataCenterInfo(Encoder encoder, String key, DataCenterInfo value) throws IOException {
        if (value == null) {
            return;
        }
        encoder.encodeKey(key);
        try (Encoder obj = encoder.encodeObject(Argument.of(Object.class))) {
            obj.encodeKey("@class"); obj.encodeString("com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo");
            encodeString(obj, "name", DataCenterInfo.Name.MyOwn.name());
        }
    }
}
