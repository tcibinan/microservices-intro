package io.micronaut.discovery.eureka.client.v2;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.Decoder;
import io.micronaut.serde.Encoder;
import io.micronaut.serde.Serde;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ApplicationInfoSerde implements Serde<ApplicationInfo> {

    private final InstanceInfoSerde instanceInfoSerde;

    public ApplicationInfoSerde(InstanceInfoSerde instanceInfoSerde) {
        this.instanceInfoSerde = instanceInfoSerde;
    }

    @Override
    public @Nullable ApplicationInfo deserialize(@NonNull Decoder decoder,
                                                 @NonNull DecoderContext context,
                                                 @NonNull Argument<? super ApplicationInfo> type) throws IOException {
        try (Decoder root = decoder.decodeObject(type)) {
            root.decodeKey();
            return decode(root);
        }
    }

    public ApplicationInfo decode(Decoder root) throws IOException {
        try (Decoder obj = root.decodeObject(Argument.of(Object.class))) {
            String name = null;
            List<InstanceInfo> instances = null;
            while (true) {
                String key = obj.decodeKey();
                if (key == null) {
                    break;
                }
                switch (key) {
                    case "name" -> name = obj.decodeString();
                    case "instance" -> instances = decodeArray(obj, instanceInfoSerde::decode);
                    default -> obj.skipValue();
                }
            }
            return new ApplicationInfo(name, instances);
        }
    }

    private <T> List<T> decodeArray(Decoder obj, IOFunction<Decoder, T> function) throws IOException {
        List<T> instances = new ArrayList<>();
        try (Decoder array = obj.decodeArray()) {
            while (array.hasNextArrayValue()) {
                instances.add(function.apply(array));
            }
        }
        return instances;
    }

    @Override
    public void serialize(@NonNull Encoder encoder,
                          @NonNull EncoderContext context,
                          @NonNull Argument<? extends ApplicationInfo> type,
                          @NonNull ApplicationInfo value) {
        throw new UnsupportedOperationException();
    }

    @FunctionalInterface
    public interface IOFunction<T, R> {
        R apply(T t) throws IOException;
    }
}
