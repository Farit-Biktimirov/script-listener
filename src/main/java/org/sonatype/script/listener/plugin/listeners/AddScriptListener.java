package org.sonatype.script.listener.plugin.listeners;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.script.Script;
import org.sonatype.nexus.script.ScriptCreatedEvent;
import org.sonatype.script.listener.plugin.service.ScriptListenerService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class AddScriptListener implements EventAware {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public final static String SCRIPT_TYPE = "listener";

    private final ScriptListenerService scriptListenerService;

    @Inject
    public AddScriptListener(ScriptListenerService listenerService) {
        this.scriptListenerService = listenerService;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void on(ScriptCreatedEvent event) {
        Script script = event.getScript();
        if (script.getType().equals(SCRIPT_TYPE) && scriptListenerService.addListener(script.getName())) {
            log.info("Successfully added Listener. Script name {}", script.getName());
        }
    }

}
