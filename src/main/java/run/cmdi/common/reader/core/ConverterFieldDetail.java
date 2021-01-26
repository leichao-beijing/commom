package run.cmdi.common.reader.core;

/**
 * @author leichao
 */
@Deprecated
public class ConverterFieldDetail {

//    public static <T> Map<String, FieldDetailOld> toMap(Class<T> clazz, ExcelHeadModel excelHeadModel,
//                                                        List<String> indexes) {
//        HashMap<String, FieldDetailOld> map = new HashMap<>();
//        Field[] fields = ReflectUtil.getFields(clazz);
//        FieldDetailOld fieldDetailOld;
//
//        boolean check;//TODO 构造fieldDetail
//        for (Field field : fields) {
//            check = false;
//            if (indexes != null && indexes.contains(field.getName()))
//                check = true;
//            FindColumn findColumn = field.getAnnotation(FindColumn.class);
//            FindColumns findColumns = field.getAnnotation(FindColumns.class);
//            if (findColumn != null && findColumns != null)
//                throw new NullPointerException("@ExcelConverter and @ExcelConverterList Override");
//            if (findColumn == null && findColumns == null)
//                continue;
//
//            FormatDate format = field.getAnnotation(FormatDate.class);
//
//            if (findColumn != null) {
//                fieldDetailOld = FieldDetailOld.build(field, clazz, format, findColumn);
//                if (check)//将 具有唯一性验证的列 添加列空检查
//                    fieldDetailOld.setCheckColumn(true);
//                map.put(field.getName(), fieldDetailOld);
//                continue;
//            }
//
//            if (findColumns != null) {
//                FindColumn[] values = findColumns.value();
//                if (values.length == 0) {//TODO 将配置装配至FieldDetail
//                    throw new NullPointerException("@FindColumn no data");
//                }
//                for (int i = 0; i < values.length; i++) {
//                    FieldDetailOld value = FieldDetailOld.build(field, clazz, format, values[i]);
//                    if (check)
//                        value.setCheckColumn(true);
//                    fieldDetailOld = map.get(field.getName());
//                    if (fieldDetailOld == null)
//                        map.put(field.getName(), value);
//                    else {
//                        fieldDetailOld.converterList(value, i);
//                    }
//                }
//            }
//        }
//        return map;
//    }
//
//    public static <T> FindSheetModel<T> toFindModel(Class<T> classes, ExcelHeadModel excelHeadModel) {
//        return new FindSheetModel<T>(toMap(classes, excelHeadModel, null));
//    }

//    /**
//     * 验证classes 中是否存在interfaceClasses接口类
//     */
//    public static boolean IsInterface(Class<?> classes, Class<?> interfaceClasses) {
//        Class<?>[] is = classes.getInterfaces();
//        if (classes.equals(interfaceClasses)) {
//            return true;
//        }
//        for (Class<?> i : is) {
//            if (i.equals(interfaceClasses))
//                return true;
//        }
//        return false;
//    }

//    public static final Set<Class<?>> SupportClassesList = new HashSet<Class<?>>() {
//        private static final long serialVersionUID = 1L;
//
//        {
//            add(String.class);
//
//            add(int.class);
//            add(Integer.class);
//
//            add(double.class);
//            add(Double.class);
//
//            add(long.class);
//            add(Long.class);
//
//            add(float.class);
//            add(Float.class);
//
//            add(short.class);
//            add(Short.class);
//
//            add(boolean.class);
//            add(Boolean.class);
//
//            add(Date.class);
//        }
//    };

}
