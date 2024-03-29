/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.cache.memory;

import cn.hutool.cache.impl.CacheObj;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;

import java.util.*;

/**
 * 基于内存的缓存封装
 *
 * @author stylefeng
 * @since 2020/7/9 10:09
 */
public abstract class AbstractMemoryCacheOperator<T> implements CacheOperatorApi<T> {

    private final TimedCache<String, T> timedCache;

    public AbstractMemoryCacheOperator(TimedCache<String, T> timedCache) {
        this.timedCache = timedCache;
    }

    @Override
    public void put(String key, T value) {
        timedCache.put(calcKey(key), value);
    }

    @Override
    public void put(String key, T value, Long timeoutSeconds) {
        timedCache.put(calcKey(key), value, timeoutSeconds * 1000);
    }

    @Override
    public T get(String key) {
        // 如果用户在超时前调用了get(key)方法，会重头计算起始时间，false的作用就是不从头算
        return timedCache.get(calcKey(key), true);
    }

    @Override
    public void remove(String... key) {
        if (key.length > 0) {
            for (String itemKey : key) {
                timedCache.remove(calcKey(itemKey));
            }
        }
    }

    @Override
    public void remove(Collection<String> keys) {
        if (ObjectUtil.isEmpty(keys)) {
            return;
        }

        for (String key : keys) {
            timedCache.remove(calcKey(key));
        }
    }

    @Override
    public void expire(String key, Long expiredSeconds) {
        T value = timedCache.get(calcKey(key), true);
        timedCache.put(calcKey(key), value, expiredSeconds * 1000);
    }

    @Override
    public boolean contains(String key) {
        return timedCache.containsKey(calcKey(key));
    }

    @Override
    public Collection<String> getAllKeys() {
        Iterator<CacheObj<String, T>> cacheObjIterator = timedCache.cacheObjIterator();
        ArrayList<String> keys = CollectionUtil.newArrayList();
        while (cacheObjIterator.hasNext()) {
            // 去掉缓存key的common prefix前缀
            String key = cacheObjIterator.next().getKey();
            keys.add(removePrefix(key));
        }
        return keys;
    }

    @Override
    public Collection<T> getAllValues() {
        Iterator<CacheObj<String, T>> cacheObjIterator = timedCache.cacheObjIterator();
        ArrayList<T> values = CollectionUtil.newArrayList();
        while (cacheObjIterator.hasNext()) {
            values.add(cacheObjIterator.next().getValue());
        }
        return values;
    }

    @Override
    public Map<String, T> getAllKeyValues() {
        Collection<String> allKeys = this.getAllKeys();
        HashMap<String, T> results = MapUtil.newHashMap();
        for (String key : allKeys) {
            results.put(key, this.get(key));
        }
        return results;
    }
}
