package com.luoli.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @Author liluo
 * @create 2023/10/24 17:46
 */
@Configuration
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String xid = RootContext.getXID();
        if(StringUtils.hasText(xid)){
            requestTemplate.header(RootContext.KEY_XID, xid);
        }
    }
}