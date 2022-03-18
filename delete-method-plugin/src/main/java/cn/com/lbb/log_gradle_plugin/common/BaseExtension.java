package cn.com.lbb.log_gradle_plugin.common;

public abstract class BaseExtension {
    private boolean enable = true;
    private boolean enableInDebug;

    public void enable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    public void enableInDebug(boolean enable) {
        this.enableInDebug = enable;
    }

    public boolean isEnableInDebug() {
        return enableInDebug;
    }

    abstract public String getName();

}
