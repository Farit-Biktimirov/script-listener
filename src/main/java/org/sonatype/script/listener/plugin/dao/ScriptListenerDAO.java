package org.sonatype.script.listener.plugin.dao;

import org.sonatype.nexus.datastore.api.IterableDataAccess;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Optional;

public interface ScriptListenerDAO extends IterableDataAccess<ScriptListenerData>{

    Optional<List<ScriptListenerData>> readByEventType(@Param("value") String eventType);
    Optional<List<String>> getScriptsNamesByEventType(@Param("value") String eventType);
    boolean deleteByScriptName(@Param("value") String scriptName);
}
