package org.sonatype.script.listener.plugin.listeners;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.common.event.EventAware;
import org.sonatype.nexus.script.ScriptDeletedEvent;
import org.sonatype.script.listener.plugin.store.ScriptListenerStore;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class DeleteScriptListener implements EventAware {

    final Logger log = LoggerFactory.getLogger(DeleteScriptListener.class);

    final ScriptListenerStore store;

    @Inject
    public DeleteScriptListener(ScriptListenerStore store) {
        this.store = store;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void on(ScriptDeletedEvent event) {
        if (store.deleteByScriptName(event.getScript().getName())) {
            log.info("Successfully deleted bond Script<->Event. Script name {}", event.getScript().getName());
        }
    }

}
