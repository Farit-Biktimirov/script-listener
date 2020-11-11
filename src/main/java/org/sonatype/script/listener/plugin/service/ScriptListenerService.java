package org.sonatype.script.listener.plugin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.common.app.ManagedLifecycle;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.common.script.ScriptService;
import org.sonatype.nexus.script.Script;
import org.sonatype.nexus.script.plugin.internal.ScriptStore;
import org.sonatype.script.listener.plugin.dao.ScriptListenerData;
import org.sonatype.script.listener.plugin.listeners.AScriptListener;
import org.sonatype.script.listener.plugin.store.ScriptListenerStore;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import static org.sonatype.nexus.common.app.ManagedLifecycle.Phase.SERVICES;
import static org.sonatype.nexus.internal.script.ScriptEngineManagerProvider.DEFAULT_LANGUAGE;
import static org.sonatype.script.listener.plugin.listeners.AddScriptListener.SCRIPT_TYPE;

@Named
@Singleton
@ManagedLifecycle(phase = SERVICES)
public class ScriptListenerService {

    private final Logger log = LoggerFactory.getLogger(ScriptListenerService.class);

    private final EventManager eventManager;
    private final ScriptService scriptService;
    private final ScriptStore scriptStore;
    private final ScriptListenerStore store;
    private final TreeMap<String, AScriptListener> map = new TreeMap<>();


    @Inject
    public ScriptListenerService(EventManager eventManager, ScriptService scriptService, ScriptStore scriptStore,  ScriptListenerStore store) {
        this.eventManager = eventManager;
        this.scriptService = scriptService;
        this.scriptStore = scriptStore;
        this.store = store;
    }

    @PostConstruct
    private void init() {
        log.info("Started ListenerService");
        List<Script> scriptList = this.scriptStore.list();
        scriptList.stream()
                  .filter( script -> script.getType().equals(SCRIPT_TYPE))
                  .forEach( script -> {
                      try {
                          if (initiateAndAddScript(script)) {
                              log.info("Successfully added Listener. Script name {}", script.getName());
                          }
                      } catch(Exception ex) {
                          log.error(ex.getMessage(),ex);
                      }
                  });
    }

    public boolean removeListener(String type) {
        if (map.containsKey(type)) {
            eventManager.unregister(map.get(type));
            return true;
        }
        return false;
    }

    public boolean addListener(String scriptName) {
        Script script = scriptStore.get(scriptName);
        return initiateAndAddScript(script);
    }


    public boolean addBond(String scriptName, String eventType) {
        if (map.containsKey(eventType) && validateScriptName(scriptName)) {
            ScriptListenerData data = new ScriptListenerData();
            data.setScriptname(scriptName);
            data.setEventtype(eventType);
            store.create(data);
        }
        return false;
    }

    private boolean initiateAndAddScript(Script script) {
        HashMap<String, Object> bindings = new HashMap<>();
        bindings.put("scriptService", scriptService);
        bindings.put("scriptStore", scriptStore);
        bindings.put("listenerStore", store);
        if (null != script && script.getType().equals(SCRIPT_TYPE)) {
            try {
                AScriptListener listener = (AScriptListener) scriptService.eval(DEFAULT_LANGUAGE,
                        script.getContent(), bindings);
                if (!map.containsKey(listener.getType())) {
                    map.put(listener.getType(), listener);
                    eventManager.register(listener);
                    return true;
                }
            } catch(Exception ex) {
                log.error(ex.getMessage(),ex);
            }
        }
        return false;
    }

    private boolean validateScriptName(String scriptName) {
        if (null != scriptStore.get(scriptName)) {
            return true;
        }
        return false;
    }

}
