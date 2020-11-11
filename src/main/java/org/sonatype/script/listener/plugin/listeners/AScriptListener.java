package org.sonatype.script.listener.plugin.listeners;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import org.sonatype.nexus.common.script.ScriptService;
import org.sonatype.nexus.script.plugin.internal.ScriptStore;
import org.sonatype.script.listener.plugin.store.ScriptListenerStore;

public abstract class AScriptListener {

    private final ScriptService scriptService;
    private final ScriptStore scriptStore;
    private final ScriptListenerStore store;

    protected AScriptListener(ScriptService scriptService, ScriptStore scriptStore, ScriptListenerStore store) {
        this.scriptService = scriptService;
        this.scriptStore = scriptStore;
        this.store = store;
    }

    @Subscribe
    @AllowConcurrentEvents
    public abstract void on(Object event);

    public abstract String getType();
}
