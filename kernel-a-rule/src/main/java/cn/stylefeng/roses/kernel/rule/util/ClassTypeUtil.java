package cn.stylefeng.roses.kernel.rule.util;

import cn.hutool.core.util.ClassUtil;
import cn.stylefeng.roses.kernel.rule.enums.FieldTypeEnum;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;
import sun.reflect.generics.reflectiveObjects.WildcardTypeImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * 获取类类型的工具
 *
 * @author fengshuonan
 * @since 2022/1/14 16:42
 */
@Slf4j
public class ClassTypeUtil {

    /**
     * 获取类类型的类别
     *
     * @author fengshuonan
     * @since 2022/1/14 0:25
     */
    public static FieldTypeEnum getClassFieldType(Type type) {

        // 如果是具体类，不带泛型
        if (type instanceof Class) {

            Class<?> clazz = (Class<?>) type;

            // 判断是否是基本类型，如果是Map或者Object，也认定为基本类型，不解析他们的字段
            if (ClassUtil.isSimpleValueType(clazz)) {
                return FieldTypeEnum.BASIC;
            }

            // 判断是否是数组类型
            else if (clazz.isArray()) {
                // 获取array的具体类型
                Class<?> componentType = clazz.getComponentType();
                if (ClassUtil.isSimpleValueType(componentType)) {
                    return FieldTypeEnum.BASE_ARRAY;
                } else {
                    return FieldTypeEnum.ARRAY_WITH_OBJECT;
                }
            }

            // 如果是集合类型，纯集合类型，不带泛型
            else if (Collection.class.isAssignableFrom(clazz)) {
                return FieldTypeEnum.BASE_COLLECTION;
            }

            // 如果是Object类型，则认定为基本类型，不解析他的具体内容
            else if (Object.class.equals(clazz)) {
                return FieldTypeEnum.BASIC;
            }

            // 其他类型，暂不处理
            else {
                log.debug("类型是Class，但有处理不到的情况，打印出类的信息如下：{}", clazz.toGenericString());
                return FieldTypeEnum.OTHER;
            }
        }

        // 如果带具体泛型的类
        else if (type instanceof ParameterizedType) {

            ParameterizedType parameterizedType = (ParameterizedType) type;

            // 泛型类的主体
            Type rawType = parameterizedType.getRawType();

            if (rawType instanceof Class<?>) {
                Class<?> rawTypeClass = (Class<?>) rawType;

                // 如果泛型主体是集合
                if (Collection.class.isAssignableFrom(rawTypeClass)) {
                    return FieldTypeEnum.COLLECTION_WITH_OBJECT;
                }

                // 如果是map类型，则认定为基本类型，不做处理，不解析他的元数据
                else if (Map.class.isAssignableFrom(rawTypeClass)) {
                    return FieldTypeEnum.BASIC;
                }

                // 泛型的主体情况不确定，不处理
                else {
                    log.debug("泛型的主体情况不确定，不处理，打印出rawTypeClass：{}", rawTypeClass.getName());
                    return FieldTypeEnum.OTHER;
                }
            } else {
                // 泛型的主体是别的类型
                log.debug("rawType为非Class类型？打印出rawType：{}", rawType.getTypeName());
                return FieldTypeEnum.OTHER;
            }
        }

        // 带T的参数，例如解析到ResponseData<T>中的data字段就是这种情况
        else if (type instanceof TypeVariableImpl<?>) {
            return FieldTypeEnum.WITH_UNKNOWN_GENERIC;
        }

        // 带?的参数，例如解析到ResponseData<?>中的data字段就是这种情况
        else if (type instanceof WildcardTypeImpl) {
            return FieldTypeEnum.OTHER;
        }

        // 其他情况，既不是class也不是ParameterizedType
        else {
            log.debug("未知类型的处理，既不是class也不是ParameterizedType，打印出类的信息如下：{}", type.getTypeName());
            return FieldTypeEnum.OTHER;
        }
    }

}
