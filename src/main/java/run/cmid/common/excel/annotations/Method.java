package run.cmid.common.excel.annotations;

import run.cmid.common.excel.model.eumns.ExcelReadType;

/**
 * 当该条条件成立时，数据将被写入对象。不成立时，对象将不会被写入数据。在对象check属性时为true时，当无法写入时间时，将会抛出异常。
 * 
 * @author leichao
 * @date 2020-05-12 03:27:46
 */
public @interface Method {
    /**
     * 读取匹配的对象的名称 未填写时，匹配对象未注解对象
     */
    String fieldName() default "";

    /**
     * 使用fieldName取toString进行按照 model 类型匹配<br>
     * 当values为默认值，空时。将只对对象null状态敏感。其他类型忽略。
     */
    String[] values() default {};

    ExcelReadType model() default ExcelReadType.EQUALS;

    /**
     * 满足mode条件检查对，执行的操作。检查或者不检查。不检查的对象，未匹配值将会置null。
     */
    boolean check() default false;
}
