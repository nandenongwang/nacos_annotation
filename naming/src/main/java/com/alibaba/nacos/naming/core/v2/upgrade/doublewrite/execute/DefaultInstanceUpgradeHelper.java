/*
 * Copyright 1999-2020 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.naming.core.v2.upgrade.doublewrite.execute;

import com.alibaba.nacos.naming.core.Instance;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * A default implementation for instance upgrade/downgrade.
 *
 * @author gengtuo.ygt
 * on 2021/2/25
 */
public class DefaultInstanceUpgradeHelper implements InstanceUpgradeHelper {
    
    private static final String IGNORE_PROPERTIES = "metadata";

    /**
     * Fallback to default implementation when no other impls met.
     */
    @Configuration
    public static class Config {
        
        /**
         * A default impl of instance upgrade helper.
         *
         * @return default impl of instance upgrade helper
         */
        @Bean
        @ConditionalOnMissingBean(InstanceUpgradeHelper.class)
        public InstanceUpgradeHelper defaultInstanceUpgradeHelper() {
            return new DefaultInstanceUpgradeHelper();
        }
        
    }
    
    @Override
    public Instance toV1(com.alibaba.nacos.api.naming.pojo.Instance v2) {
        Instance v1 = new Instance(v2.getIp(), v2.getPort(), v2.getClusterName());
        BeanUtils.copyProperties(v2, v1);
        return v1;
    }

    @Override
    public com.alibaba.nacos.api.naming.pojo.Instance toV2(Instance v1) {
        com.alibaba.nacos.api.naming.pojo.Instance v2 = new com.alibaba.nacos.api.naming.pojo.Instance();
        BeanUtils.copyProperties(v1, v2, IGNORE_PROPERTIES);
        v2.getMetadata().putAll(v1.getMetadata());
        return v2;
    }

}
