package org.sonatype.script.listener.plugin.dao;

import org.sonatype.nexus.datastore.api.DataAccess;
import org.apache.ibatis.annotations.Param;
import sun.util.locale.provider.JRELocaleProviderAdapter;


import java.util.Optional;

public interface ScriptListenerDAO extends DataAccess {

    Optional<Iterable<ScriptListenerData>> browse();
    void    create(ScriptListenerData scriptListenerData);
    Optional<Iterable<ScriptListenerData>> readByEventType(@Param("value") String eventType);
    Optional<Iterable<String>> getScriptsNamesByEventType(@Param("value") String eventType);
    boolean deleteByScriptName(@Param("value") String scriptName);
}
