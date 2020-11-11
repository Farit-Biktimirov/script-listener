package org.sonatype.script.listener.plugin.listeners;

import java.lang.reflect.Method;
import java.util.Arrays;

public abstract class AScriptListener {

    public String getType() {
        Method[] methods = this.getClass().getMethods();
        final String[] result = new String[1];
        Arrays.stream(methods)
                .filter(m -> m.getName().equals("on"))
                .filter(m -> m.getParameterCount() == 1L)
                .forEach(m -> {
                    Arrays.stream(m.getParameterTypes()).forEach(c->{
                        result[0] = c.getCanonicalName();
                    });
                });
        if (result[0] != null) {
            return result[0];
        }
        return null;
    };
}
