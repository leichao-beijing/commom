package run.cmdi.common.convert;

import cn.hutool.core.util.ReflectUtil;
import run.cmdi.common.convert.model.WriterFieldInfo;
import run.cmdi.common.reader.model.eumns.FieldDetailType;
import run.cmdi.common.utils.ReflectLcUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WriterEntityBuild<T> implements WriterEntityInterface<T> {
    /**
     * @param heardState   是否创建头
     * @param t            转换的对象
     * @param integerMap   实体配置
     * @param writerEntity 保存实体
     */
    public WriterEntityBuild(Class t, WriterEntity writerEntity, Map<Integer, WriterFieldInfo> integerMap, boolean heardState) throws Exception {
        this.integerMap = integerMap;
        this.writerEntity = writerEntity;
        this.heardState = heardState;
    }

    /**
     * @param heardState   是否创建头
     * @param t            转换的对象
     * @param writerEntity 保存实体
     */
    public WriterEntityBuild(Class t, WriterEntity writerEntity, boolean heardState) throws Exception {
        this.integerMap = toIntegerMap(RegisterAnnotationUtils.build(t, WriterFieldInfo.class));
        this.writerEntity = writerEntity;
        this.heardState = heardState;
    }

    private final WriterEntity writerEntity;
    private final Map<Integer, WriterFieldInfo> integerMap;

    private static Map<Integer, WriterFieldInfo> toIntegerMap(Map<String, WriterFieldInfo> map) {
        Map<Integer, WriterFieldInfo> infos = new HashMap<>();
        Iterator<Map.Entry<String, WriterFieldInfo>> it = map.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry<String, WriterFieldInfo> next = it.next();
            WriterFieldInfo info = next.getValue();
            if (info.getType() == FieldDetailType.SINGLE) {
                infos.put(i, info);
                i++;
            } else {
                for (WriterFieldInfo writerFieldInfo : info.getList()) {
                    infos.put(i, writerFieldInfo);
                    i++;
                }
            }
        }
        return infos;
    }

    private boolean heardState;

    private final void createHeard() {
        if (!heardState)
            return;
        integerMap.forEach((i, info) -> {
            writerEntity.add(i, info.getName(), null);
        });
        heardState = false;
    }

    @Override
    public void add(T t) {
        createHeard();
        add(0, t);
    }

    @Override
    public void add(int column, T t) {
        createHeard();
        integerMap.forEach((i, info) -> {
            Object clazzValue = ReflectUtil.invoke(t, ReflectLcUtils.methodGetString(info.getFieldName()));
            if (clazzValue == null)
                return;
            writerEntity.add(writerEntity.getCount(), column + i, clazzValue, info);
        });
    }

    @Override
    public void replace(int row, int column, T t) {
        createHeard();
        integerMap.forEach((i, info) -> {
            Object clazzValue = ReflectUtil.invoke(t, ReflectLcUtils.methodGetString(info.getFieldName()));
            if (clazzValue == null)
                return;
            writerEntity.add(row, column + i, clazzValue, info);
        });
    }

    @Override
    public void replace(int row, T t) {
        createHeard();
        replace(row, 0, t);
    }

    @Override
    public void save(OutputStream os) throws IOException {
        writerEntity.save(os);
    }

    @Override
    public void close() throws IOException {
        writerEntity.close();
    }
}
