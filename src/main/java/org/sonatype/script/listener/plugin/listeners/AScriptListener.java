package org.sonatype.script.listener.plugin.listeners;

import java.lang.reflect.Method;
import java.util.Arrays;

public abstract class AScriptListener {

    public String getType() {
        Method[] methods = this.getClass().getMethods();
        return Arrays.stream(methods)
                .filter(m -> m.getName().equals("on"))
                .filter(m -> m.getParameterCount() == 1L)
                .map(m -> m.getParameterTypes()[0].getCanonicalName())
                .findFirst().orElse("unknown");
    }
}
