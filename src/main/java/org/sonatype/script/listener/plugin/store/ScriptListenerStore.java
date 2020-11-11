package org.sonatype.script.listener.plugin.store;

import org.sonatype.goodies.lifecycle.Lifecycle;
import org.sonatype.script.listener.plugin.dao.ScriptListenerData;

import java.util.List;

public interface ScriptListenerStore extends Lifecycle {
    List<ScriptListenerData> list();

    ScriptListenerData create(ScriptListenerData data);

     List<ScriptListenerData> readByEventType(String name);

    List<String> getScriptsNamesByEventType(String eventType);

    boolean deleteByScriptName(String scriptName);

}
