package org.sonatype.script.listener.plugin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.goodies.lifecycle.LifecycleSupport;
import org.sonatype.nexus.common.app.ManagedLifecycle;
import org.sonatype.nexus.common.event.EventManager;
import org.sonatype.nexus.common.script.ScriptService;
import org.sonatype.nexus.script.Script;
import org.sonatype.nexus.script.ScriptManager;
import org.sonatype.script.listener.plugin.listeners.AScriptListener;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.sonatype.nexus.common.app.ManagedLifecycle.Phase.SERVICES;
import static org.sonatype.nexus.internal.script.ScriptEngineManagerProvider.DEFAULT_LANGUAGE;
import static org.sonatype.script.listener.plugin.listeners.AddScriptListener.SCRIPT_TYPE;

@Named
@Singleton
@ManagedLifecycle(phase = SERVICES)
public class ScriptListenerService extends LifecycleSupport {

    private final Logger log = LoggerFactory.getLogger(ScriptListenerService.class);
    private final EventManager eventManager;
    private final ScriptService scriptService;
    private final ScriptManager scriptManager;
    private final TreeMap<String, AScriptListener> map = new TreeMap<>();

    @Inject
    public ScriptListenerService(EventManager eventManager, ScriptService scriptService, ScriptManager scriptManager) {
        this.eventManager = eventManager;
        this.scriptService = scriptService;
        this.scriptManager = scriptManager;
        doStart();
    }

    @Override
    protected void doStart() {
        log.info("Started ListenerService");
        List<Script> scriptList = this.getScriptList();
        if (null != scriptList) {
            scriptList.stream()
                    .filter(script -> script.getType().equals(SCRIPT_TYPE))
                    .forEach(script -> {
                        try {
                            if (initiateAndAddScript(script.getName(), script)) {
                                log.info("Successfully added Listener. Script name {}", script.getName());
                            } else {
                                log.warn("Failed to ADD listener {}", script.getName());
                            }
                        } catch (Exception ex) {
                            log.error(ex.getMessage(), ex);
                        }
                    });
        }
    }
    @Override
    protected void doStop() {
        log.info("Start removing listeners");
        map.values().stream().forEach( listener -> eventManager.unregister(listener));
        log.info("Stop ListenerService");

    }

    public boolean removeListener(String scriptName) {
        if (map.containsKey(scriptName)) {
            eventManager.unregister(map.get(scriptName));
            return true;
        }
        return false;
    }

    public boolean removelistenerByEventType(final String type){
        List<AScriptListener> list = map.entrySet().stream()
                .map( e -> e.getValue())
                .filter( aScriptListener -> aScriptListener.getType().equals(type))
                .collect(Collectors.toList());
        if (list.size() > 0) {
            list.forEach(aScriptListener -> map.remove(aScriptListener));
        }
        return false;
    }

    public boolean addListener(String scriptName) {
        Script script = getScriptByName(scriptName);
        return initiateAndAddScript(scriptName,script);
    }

    private boolean initiateAndAddScript(String scriptName, Script script) {
        HashMap<String, Object> bindings = new HashMap<>();
        bindings.put("scriptService", scriptService);
        if (null != script && script.getType().equals(SCRIPT_TYPE)) {
            try {
                AScriptListener listener = (AScriptListener) scriptService.eval(DEFAULT_LANGUAGE,
                        script.getContent(), bindings);
                if (!map.containsKey(listener.getType())) {
                    map.put(scriptName, listener);
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
        return null != getScriptByName(scriptName);
    }

    private Script getScriptByName(String scriptName) {
         return scriptManager.get(scriptName);
    }

    private List<Script> getScriptList() {
        try {
            final List<Script> result = new ArrayList<>();
            scriptManager.browse().forEach( script -> result.add(script));
            if (!result.isEmpty()) {
                return result.stream().filter( script -> script.getType().equals(SCRIPT_TYPE)).collect(Collectors.toList());
            }
        } catch(Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }
}
