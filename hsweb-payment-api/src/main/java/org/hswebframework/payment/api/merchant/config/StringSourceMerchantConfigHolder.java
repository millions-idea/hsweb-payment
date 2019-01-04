package org.hswebframework.payment.api.merchant.config;


import com.alibaba.fastjson.JSON;
import org.hswebframework.utils.StringUtils;
import org.hswebframework.web.dict.EnumDict;

import java.util.List;
import java.util.Optional;

/**
 * @author zhouhao
 * @since 1.0.0
 */
public class StringSourceMerchantConfigHolder implements MerchantConfigHolder {

    private String value;

    public StringSourceMerchantConfigHolder(String value) {
        this.value = value;
    }

    @Override
    public <T> Optional<List<T>> asList(Class<T> t) {
        return getNativeValue()
                .map(v -> JSON.parseArray(v, t));
    }

    protected <T> T convert(String value, Class<T> t) {
        if (t.isEnum()) {
            if (EnumDict.class.isAssignableFrom(t)) {
                T val = (T) EnumDict.find((Class) t, value).orElse(null);
                if (null != val) {
                    return val;
                }
            }
            for (T enumConstant : t.getEnumConstants()) {
                if (((Enum) enumConstant).name().equalsIgnoreCase(value)) {
                    return enumConstant;
                }
            }
        }
        return JSON.parseObject(value, t);
    }

    @Override
    @SuppressWarnings("all")
    public <T> Optional<T> as(Class<T> t) {
        if (t == String.class) {
            return (Optional) asString();
        } else if (Long.class == t || long.class == t) {
            return (Optional) asLong();
        } else if (Integer.class == t || int.class == t) {
            return (Optional) asInt();
        } else if (Double.class == t || double.class == t) {
            return (Optional) asDouble();
        }
        return getNativeValue().map(v -> convert(v, t));
    }

    @Override
    public Optional<String> asString() {
        return getNativeValue();
    }

    @Override
    public Optional<Long> asLong() {
        return getNativeValue().map(StringUtils::toLong);
    }

    @Override
    public Optional<Integer> asInt() {
        return getNativeValue().map(StringUtils::toInt);
    }

    @Override
    public Optional<Double> asDouble() {
        return getNativeValue().map(StringUtils::toDouble);
    }

    private Optional<String> getNativeValue() {
        return Optional.ofNullable(value);
    }

    @Override
    public Optional<Object> getValue() {
        return Optional.ofNullable(value);
    }
}
