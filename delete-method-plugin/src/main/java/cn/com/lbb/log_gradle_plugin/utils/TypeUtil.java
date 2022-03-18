package cn.com.lbb.log_gradle_plugin.utils;

public class TypeUtil {
    public static final String SLOT = "cn.com.lbb.log_gradle_plugin.Slot";
    public static final String SLOT_X2 = "cn.com.lbb.log_gradle_plugin.Slot_x2";
    public static final String ARRAY_REF = "cn.com.lbb.array_ref";
    public static final String REF = "cn.com.lbb.ref";


    public enum ComputationCategory {
        Category1, Category2, ILLEGAL
    }

    public static ComputationCategory coputeCategory(String type) {
        if (type == null || type.length() <= 0) {
            return ComputationCategory.ILLEGAL;
        }
        if (SLOT_X2.equals(type)) {
            return ComputationCategory.Category2;
        }
        return ComputationCategory.Category1;
    }
}
