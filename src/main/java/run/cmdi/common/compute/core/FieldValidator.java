package run.cmdi.common.compute.core;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import run.cmdi.common.compute.enums.LinkState;

/**
 * @author leichao
 * @date 2020-04-20 07:16:07
 */
public class FieldValidator {
    public static void clear(Map<String, ComputeDetail> map) {
        Iterator<Entry<String, ComputeDetail>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, ComputeDetail> next = it.next();
            ComputeDetail fieldDetail = next.getValue();
            fieldDetail.clear();
        }
    }

    public static void field(Map<String, ComputeDetail> map) {
        Iterator<Entry<String, ComputeDetail>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, ComputeDetail> next = it.next();
            ComputeDetail fieldDetail = next.getValue();
            String computeValueFieldName = fieldDetail.getComputeValueFieldName();
            if (computeValueFieldName == null)
                continue;
            Object value = null;
            try {
                value = fieldDetail.find(computeValueFieldName).getObject();
            } catch (NullPointerException e) {
                throw new NullPointerException(
                        "@ComputeField " + fieldDetail.getSpotPath().getPath() + " " + e.getMessage());
            }
            if (value instanceof String) {
                fieldDetail.setComputeValue((String) value);
                List<String> list = fieldDetail.getComputeValues();
                for (String string : list) {
                    try {
                        fieldDetail.find(string);
                    } catch (NullPointerException e) {
                        throw new NullPointerException(
                                "@ComputeField " + fieldDetail.getSpotPath().getPath() + " " + e.getMessage());
                    }
                }
            } else {
                throw new NullPointerException(
                        "@ComputeField " + fieldDetail.getSpotPath().getPath() + " ClassType need String.class");
            }
        }

        map.forEach((str, value) -> {
            List<String> list = value.getComputeValues();
            for (String string : list) {
                ComputeDetail field = value.find(string);
                if (field.getState() == LinkState.NOT_COMPUTE)
                    throw new NullPointerException(field.getSpotPath().getPath() + " state NOT_COMPUTE");
            }
        });
    }

    public static void loop(Map<String, ComputeDetail> map, LinkedHashMap<String, ComputeDetail> runMap) {
        Iterator<Entry<String, ComputeDetail>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, ComputeDetail> next = it.next();
            ComputeDetail value = next.getValue();
            loop(null, value, runMap);
        }
    }

    public static LinkState loop(String parentName, ComputeDetail fieldComputeDetail,
                                 LinkedHashMap<String, ComputeDetail> runMap) {
        if (fieldComputeDetail.getState() == LinkState.DATA || fieldComputeDetail.getState() == LinkState.NOT_COMPUTE)
            return fieldComputeDetail.getState();
        if (parentName != null) {
            if (fieldComputeDetail.getParentNameList().contains(parentName))
                throw new NullPointerException(fieldComputeDetail.getSpotPath().getPath() + "error Loop");
            fieldComputeDetail.getParentNameList().add(parentName);
        }
        List<String> values = fieldComputeDetail.getComputeValues();
        for (String string : values) {
            ComputeDetail field = fieldComputeDetail.find(string);
            if (field.getState() == LinkState.DATA || field.getState() == LinkState.COMPUTE_DATA)
                continue;
            loop(fieldComputeDetail.getSpotPath().getPath(), field, runMap);
        }
        runMap.put(fieldComputeDetail.getSpotPath().getPath(), fieldComputeDetail);
        fieldComputeDetail.setState(LinkState.COMPUTE_DATA);
        return LinkState.COMPUTE_DATA;
    }
}
